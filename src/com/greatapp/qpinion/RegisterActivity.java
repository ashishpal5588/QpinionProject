package com.greatapp.qpinion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.contacts.ContactManager;
import com.greatapp.qpinion.contacts.QpinionContact;
import com.greatapp.qpinion.group.GroupManager;
import com.greatapp.qpinion.registration.RegistrationManager;

public class RegisterActivity extends Activity {

	protected static final String TAG = "REGISTER_ACTIVITY";
	private RadioGroup rdg_gender;
	private EditText et_user_name;
	private EditText et_emailId;
	private TextView tv_user_number;
	private AsyncTask<Void, Void, Boolean> mRegisterTask;
	private Button b_register_user;
	private SharedPreferences registrationPreference;
	private TextView tv_splash_progress;
	protected ProgressBar pb_refresh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		Intent i = getIntent();
		String msg = i.getExtras().getString("Message");
		TextView tv_title = (TextView)findViewById(R.id.tv_registration_title);
		tv_title.setText(msg);
		User user = new User(this);
		et_user_name = (EditText)findViewById(R.id.et_user_name);
		et_emailId = (EditText)findViewById(R.id.et_email_id);
		tv_user_number = (TextView)findViewById(R.id.tv_user_phone_number);
		tv_user_number.setText(user.getPhoneNumber());
		rdg_gender = (RadioGroup)findViewById(R.id.rdg_gender);
		b_register_user = (Button)findViewById(R.id.b_register);
		b_register_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickRegister();
			}
		});
		IntentFilter IF = new IntentFilter();
		IF.addAction(V.DISPLAY_MESSAGE_ACTION);
		IF.addAction(V.REGISTRATION_MESSAGE_ACTION);
		registerReceiver(mProgressReceiver, IF);
		
	}
	private void hideKeyboard() {   
	    // Check if no view has focus:
	    View view = this.getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
private void onClickRegister() {
	hideKeyboard();
    User user;
	int id = rdg_gender.getCheckedRadioButtonId();	
	String name = et_user_name.getText().toString();
	String phone = tv_user_number.getText().toString();
	String email = et_emailId.getText().toString();
    
	switch(id) {
	case -1:
		return;

	case R.id.r_female:
		user = new User(this, name, phone, email, User.FEMALE);		
	case R.id.r_male:
		user = new User(this, name, phone, email, User.MALE);
	}
    Register();
}

public void Register() {
	   setContentView(R.layout.loading_splash_layout);
	   tv_splash_progress = (TextView)findViewById(R.id.tv_loading_splash_progress);
	   pb_refresh = (ProgressBar)findViewById(R.id.pb_loading_splash);
	   TextView tv_loading_title = (TextView)findViewById(R.id.tv_loading_splash_title);
	   tv_loading_title.setText("Wait !, We are registering your profile on Qpinion..");
	   pb_refresh.setProgress(5);
	if(!RegistrationManager.isRegisteredIOnGCMServer(this)){
		RegistrationManager.registerDeviceOnGCM(this);
	    } else {
	    	 if(!RegistrationManager.isRegisteredOnQpinionServer(this)) {
	    		 //Asynchronous Call
	    		 RegistrationManager.registerUserOnQpinionServer(this);
	    	 }
	    }
}

protected void onRegistrationResult(Intent intent) {
	boolean result = intent.getExtras().getBoolean(V.REGISTRATION_RESULT);
	Log.d(TAG,"registration Result : "+result);
	if(result) {
		pb_refresh.setProgress(15);
		Log.d(TAG,"Setting Up contacts to server..");
		setUpContacts(this);

	} else {
	    new Handler().postDelayed(new Runnable(){
           @Override
            public void run() {
        		Log.e(TAG,"Again Register");
        		Intent i = new Intent(RegisterActivity.this,RegisterActivity.class);
        		i.putExtra("Message", "! Try Registration Again !");
        		RegisterActivity.this.startActivity(i);
        		RegisterActivity.this.finish();
            }
          }, 4000);
	}

}

protected void setUpContacts(final Context context) {

	AsyncTask<Void, Integer, Boolean> mSetUpContactsTask = new AsyncTask<Void, Integer, Boolean>() {

		@Override
        protected void onPreExecute(){

 		   publishProgress(20);
     	   displayMessage(context,"refreshing contacts..");
        }
        @Override
        protected Boolean doInBackground(Void... params) {
        	displayMessage(context, "setting up contacts..");
        	Log.d(TAG,"retriving contacts from device..");
        	ContactManager cm = new ContactManager(context);
        	cm.retriveContactsFromDevice(context);
        	publishProgress(25);
        	ArrayList<QpinionContact> contactList = cm.getLoadedContacts(context);
        	publishProgress(50);
        	boolean setUpDone = false;
        	if(contactList != null) {
        	   Log.d(TAG,"Sending device contacts to Server for matching. contacts..");
        	   ArrayList<QpinionContact> qpinionList = TransactionManager.setUpContactsOnServer(context,contactList);
        	   publishProgress(70);
        	   if(qpinionList != null && !qpinionList.isEmpty()) {
        	       Log.d(TAG,"updating matching contacts to DB...");
        	       cm.updateQpinionContacts(context,qpinionList);
        	       publishProgress(95);
        	       setUpDone = true;
        	   } else if(qpinionList != null){
        		   //may be no contacts found hence list can be empty
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
           	 message = "Contacts SetUp Successfull" ;
            } else {
           	 message = "Contacts setup NOT DONE !!" ;
            }
            Log.e(TAG,message);
            displayMessage(context, message);
            //displayMessage(context,message);
            onContactSetUpCompleted(context,result);
        }

    };
    mSetUpContactsTask.execute(null, null, null);	
}
int setUpAttempt = 0;
protected void onContactSetUpCompleted(Context context, Boolean result) {
	if(result) {
		(new GroupManager(RegisterActivity.this)).createDefaultGroup(RegisterActivity.this);
		pb_refresh.setProgress(100);
	    Intent i = new Intent(RegisterActivity.this,GroupActivity.class);
	    RegisterActivity.this.startActivity(i);
	    RegisterActivity.this.finish();
	} else if(setUpAttempt++ <= 1) {
		String message = "SETUP CONTACT RE-ATTAMPT : "+ setUpAttempt+" .....";
		Log.v(TAG,"SETUP CONTACT RE-ATTAMPT : "+ setUpAttempt+" .....");
         displayMessage(context,message);

		setUpContacts(context);
	} else {
		String message ="FINAL ATTAMPT OF CONTACT SETUP : FAILED !!";
		Log.e(TAG,"FINAL ATTAMPT OF CONTACT SETUP : FAILED !!");
		displayMessage(context,message);
		new Handler().postDelayed(new Runnable(){
	           @Override
	            public void run() {
	        		Log.e(TAG,"SETUP NOT COMPLETE!!");
	        	    Intent i = new Intent(RegisterActivity.this,GroupActivity.class);
	        	    RegisterActivity.this.startActivity(i);
	        	    RegisterActivity.this.finish();
	            }
	          }, 4000);
	}
}
private BroadcastReceiver mProgressReceiver =
        new BroadcastReceiver() {
             @Override
             public void onReceive(Context context, Intent intent) {
            	 Log.d(TAG,"intent recied in Receiver");
            	 String action = intent.getAction();
            		if(action.equals(V.DISPLAY_MESSAGE_ACTION)) {
                        handleDisplayIntent(intent);
            		} else if (action.equals(V.REGISTRATION_MESSAGE_ACTION)) {
            			onRegistrationResult(intent);
            		} else {
            			Log.e(TAG,"Invalid action received !! ");
            		}

              } 
 };

protected void handleDisplayIntent(Intent intent) {
	
    String newMessage = intent.getExtras().getString(V.DISPLAY_MESSAGE);
    Log.d(TAG,"update Display with : "+newMessage);
    tv_splash_progress.append(newMessage + "\n");
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

}
