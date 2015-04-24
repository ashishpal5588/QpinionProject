package com.greatapp.qpinion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.contacts.ContactAdapter;
import com.greatapp.qpinion.contacts.ContactManager;
import com.greatapp.qpinion.contacts.QpinionContact;
import com.greatapp.qpinion.group.GroupManager;

public class AddMembersActivity extends Activity {

	protected static final String TAG = "QPINION_ADD_MEMBER";
	private String groupName;
	private TextView tv_splash_progress;
	private ProgressBar pb_refresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		IntentFilter IF = new IntentFilter();
		IF.addAction(V.DISPLAY_MESSAGE_ACTION);
		registerReceiver(mProgressReceiver, IF);
	}
	
	private void init() {
		setContentView(R.layout.activity_add_members);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		groupName = i.getStringExtra("group_name");
		
		TextView TV_title = (TextView)findViewById(R.id.tv_add_member_view_title);
		TV_title.setText("Add Members to group "+groupName);
		
		ContactManager cm = new ContactManager(this);
		GroupManager gm = new GroupManager(this);
		
		//contacts.add(new opiniumContact("Dummy","000000000"));
		
		ContactAdapter adapter = new ContactAdapter(this, R.id.list_item,
				cm.getLoadedContacts(this),gm.getGroupByName(this,groupName));

		ListView lv = (ListView)findViewById(R.id.listView_contacts);
		lv.setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_members, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			refreshContacts(this);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(this,ManageGroupActivity.class);
            i.putExtra("group_name", groupName);
            startActivity(i);
            finish();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}

	protected void refreshContacts(final Context context) {

		AsyncTask<Void, Integer, Boolean> mSetUpContactsTask = new AsyncTask<Void, Integer, Boolean>() {
           @Override
           protected void onPreExecute(){
        	   setContentView(R.layout.loading_splash_layout);
       		tv_splash_progress = (TextView)findViewById(R.id.tv_loading_splash_progress);
    		pb_refresh = (ProgressBar)findViewById(R.id.pb_loading_splash);
    		TextView tv_loading_title = (TextView)findViewById(R.id.tv_loading_splash_title);
    		tv_loading_title.setText("Wait !, We are refreshing all contacts..");
        	   displayMessage(context,"refreshing contacts..");
           }

			@Override
	        protected Boolean doInBackground(Void... params) {
	        	Log.d(TAG,"retriving contacts from device..");
	        	displayMessage(context,"retriving contacts from device..");
	        	publishProgress(5);
	        	ContactManager cm = new ContactManager(context);
	        	cm.retriveContactsFromDevice(context);
	        	publishProgress(25);
	        	displayMessage(context,"getting contacts from DB for validating..");
	        	ArrayList<QpinionContact> contactList = cm.getLoadedContacts(context);
	        	publishProgress(50);
	        	boolean setUpDone = false;
	        	if(contactList != null) {
	        	   Log.d(TAG,"Sending device contacts to Server for matching. contacts..");
	        	   displayMessage(context,"verifying from server..");
	        	   ArrayList<QpinionContact> qpinionList = TransactionManager.setUpContactsOnServer(context,contactList);
	        	   publishProgress(75);
	        	   if(qpinionList != null && !qpinionList.isEmpty()) {
	        	       Log.d(TAG,"updating matching contacts to DB...");
	        	       displayMessage(context,"server response received! SUCCESS");
	        	       displayMessage(context,"updating contacts in DB..");
	        	       cm.updateQpinionContacts(context,qpinionList);
	        	       GroupManager gm = new GroupManager(context);
	        	       gm.updateDefaultGroup(context);
	        	       displayMessage(context,"COMPLETED SUCCESSFULLY !");
	        	       publishProgress(95);
	        	       setUpDone = true;
	        	   }
	        	}
	            return setUpDone;
	        }
            @Override
            protected void onProgressUpdate(Integer ...values) {
            	pb_refresh.setProgress(values[0]);
            };
	        @Override
	        protected void onPostExecute(Boolean result) {
	            GCMRegistrar.setRegisteredOnServer(context, result);
	            String message = "";
	            if(result) {
	           	 message = "Contacts SetUp DONE !!" ;
	            } else {
	           	 message = "Contacts setup NOT DONE !!" ;
	            }
	            Log.e(TAG,message);
	            //displayMessage(context,message);
	            onContactSetUpCompleted(context,result);
	        }

	    };
	    mSetUpContactsTask.execute(null, null, null);	
	}
	int setUpAttempt = 0;
	
	protected void onContactSetUpCompleted(Context context, Boolean result) {
		new Handler().postDelayed(new Runnable(){
	           @Override
	            public void run() {
	       		    init();
	            }
	          }, 3000);
	}
	
	private BroadcastReceiver mProgressReceiver =
	        new BroadcastReceiver() {
	             @Override
	             public void onReceive(Context context, Intent intent) {
	            	 Log.d(TAG,"intent recied in Receiver");
	            	 String action = intent.getAction();
	            		if(action.equals(V.DISPLAY_MESSAGE_ACTION)) {
	                        handleDisplayIntent(intent);
	            		} else {
	            			Log.e(TAG,"Invalid action received !! ");
	            		}

	              } 
	 };

	protected void handleDisplayIntent(Intent intent) {
		if(intent != null && intent.hasExtra(V.DISPLAY_MESSAGE)) {
	    String newMessage = intent.getExtras().getString(V.DISPLAY_MESSAGE);
	    Log.d(TAG,"update Display with : "+newMessage);
	    tv_splash_progress.append(newMessage + "\n");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mProgressReceiver != null) {
			try{
		  unregisterReceiver(mProgressReceiver);
			} catch(Exception e) {
				
			} finally {
				  mProgressReceiver = null;			
			}

		}
	}
	public static void displayMessage(Context context, String message) {
	    Intent intent = new Intent(V.DISPLAY_MESSAGE_ACTION);
	    intent.putExtra(V.DISPLAY_MESSAGE, message);
	    context.sendBroadcast(intent);
	}
//--------------------------------------------------------------------------------------
}
