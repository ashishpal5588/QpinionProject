package com.greatapp.qpinion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.greatapp.qpinion.registration.RegistrationManager;

public class GoToActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_splash_layout);
		new Handler().postDelayed(new Runnable(){
	        @Override
	        public void run() {
	        	if(!RegistrationManager.isDeviceRegisteredAtBothServer(GoToActivity.this)) {
	    		    //Intent i = new Intent(this,GroupActivity.class);
	    		    Intent i = new Intent(GoToActivity.this,NumberVerificationActivity.class);
	    		    startActivity(i);
	    		    finish();
	    		} else {
	    			Intent intent = new Intent(GoToActivity.this, QpinionMainActivity.class);
	    			startActivity(intent);
	    			finish();
	    		}
	        }
	    }, 1000);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
