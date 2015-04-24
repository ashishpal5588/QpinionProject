package com.greatapp.qpinion;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.group.GroupCursorAdapter;
import com.greatapp.qpinion.group.GroupManager;

public class GroupActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int GROUP_LODER_ID = 3;
	private ListView group_list_view;
	private GroupCursorAdapter mGroupCursorAdapter;
	private ProgressBar pb_loading;
	private Button b_create_group;
	private String groupName;
    public static String TAG;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		((ActionBarActivity) this).setProgressBarIndeterminateVisibility(true);
		b_create_group = (Button)findViewById(R.id.b_create_group);
		//b_create_group.setVisibility(View.GONE);
           group_list_view = (ListView)findViewById(R.id.lv_group_list);
           b_create_group.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
                 onClickCreateGroup();
				
			}
		});
           
         //  GroupManager gm = new GroupManager(this);
         //  groups = gm.getLoadedGroups();
    	//	GroupAdapter adapter = new GroupAdapter(this, R.id.list_item,groups);
    	  mGroupCursorAdapter = new GroupCursorAdapter(this, null, false);	
    		group_list_view.setAdapter(mGroupCursorAdapter);
    		getLoaderManager().initLoader(GROUP_LODER_ID, null,this);
    	//	adapter.notifyDataSetChanged();
    		group_list_view.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					TextView tv = (TextView)arg1.findViewById(R.id.tv_group_name);
					groupName = tv.getText().toString();
					onClickManageGroup(groupName);
					
				}
			});

	}

@Override
public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(TAG,"onCreateLoader group loader");
	       return new CursorLoader(this, V.DB_GROUPS_TABLE_URI,
	    	        V.DB_GROUPS_TABLE_PROJECTION, null, null,
	    	        null);

}

@Override
public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
	Log.d(TAG,"onLoadFinished group loader");
	mGroupCursorAdapter.swapCursor(data);
	mGroupCursorAdapter.notifyDataSetChanged();
    ((ActionBarActivity) this).setProgressBarIndeterminateVisibility(false);
    ((ActionBarActivity) this).invalidateOptionsMenu();

}
@Override
public void onLoaderReset(Loader<Cursor> loader) {
	Log.d(TAG,"reseting group loader");
	mGroupCursorAdapter.swapCursor(null);	
} 

	protected void onClickCreateGroup() {
		 AlertDialog.Builder alert = new AlertDialog.Builder(this);

		    final EditText edittext= new EditText(this);
		    alert.setMessage("Create Group Message");
		    alert.setTitle("Create Group title");

		    alert.setView(edittext);

		    alert.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		     //What ever you want to do with the value
		      String name = edittext.getText().toString();
		         if(!TextUtils.isEmpty(name)) {
		        	 addGroup(name);
		    	    
		         }
		         dialog.dismiss();
		      }
		    });

		    alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int whichButton) {
		        // what ever you want to do with No option.
		    	  dialog.dismiss();
		      }
		    });

		    alert.show();
		
	}

	protected void addGroup(String name) {
      GroupManager gm = new GroupManager(getBaseContext());
      gm.createNewGroupInDB(name);
      onClickManageGroup(name);//let user add contacts as soon he create a group
     // groups.add(new QpinionGroup(name));//to notify UI
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group, menu);
		//pb = (ProgressBar)findViewById(R.id.menu_progress);
		//pb.startAnimation(null);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return super.onOptionsItemSelected(item);
	}
	
	protected void onClickManageGroup(String name) {
        Intent i = new Intent(this,ManageGroupActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("group_name",name);
        startActivity(i);
	}
}
