package com.greatapp.qpinion;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.greatapp.qpinion.constants.V;

public class QpinionMainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private static final String TAG = "QPINION_MAIN";

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
    
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_qpinion_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		Log.d(TAG,"REFERESH RECEIVER REGISTERED.");
		registerReceiver(mNotifyReceiver, new IntentFilter(V.NOTIFY_BROADCAST_ACTION));
		

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		if(position == 2) {
			Intent i = new Intent(this,GroupActivity.class);
			startActivity(i);
			return;
		}
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						(Fragment)PlaceHolderFragment.newInstance(position + 1)).commit();
	}


	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.qpinion_main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
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
	
	private BroadcastReceiver mNotifyReceiver =
	        new BroadcastReceiver() {
	             @Override
	             public void onReceive(Context context, Intent intent) {
	            	 Log.d(TAG,"Notify RECEIVER ONRECEIVED");
	            	 String action = intent.getAction();
	            		if(action.equals(V.NOTIFY_BROADCAST_ACTION)) {
	            			notifyNow(intent);
	            		} else {
	            			Log.e(TAG,"Invalid action received !! ");
	            		}

	              } 
	 };
		protected void notifyNow(Intent intent) {
			String msg = intent.getStringExtra("msg");
			Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
		}
	@Override	
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(mNotifyReceiver);
		Log.d(TAG,"REFERESH RECEIVER -- UNREGISTERED");
	}
	

}
