package com.greatapp.qpinion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.greatapp.qpinion.contacts.QpinionContact;
import com.greatapp.qpinion.group.GroupContactAdapter;
import com.greatapp.qpinion.group.GroupManager;
import com.greatapp.qpinion.group.QpinionGroup;

public class ManageGroupActivity extends Activity {

	private String groupName;
	private ArrayList<QpinionContact> groupContacts;
	private GroupContactAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_group);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		groupName = i.getStringExtra("group_name");
		Button b_add_member = (Button)findViewById(R.id.b_add_group_member);
		TextView tv_group_name = (TextView)findViewById(R.id.tv_manage_group_name);
		ListView lv_group_contacts = (ListView)findViewById(R.id.lv_group_contact_list); 
		tv_group_name.setText(groupName);
		b_add_member.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
                 onClickAddGroupMembers();
				
			}
		});
           
           GroupManager gm = new GroupManager(this);
           QpinionGroup group = gm.getGroupByName(this,groupName);
           groupContacts = group.getGroupContacts();
    		mAdapter = new GroupContactAdapter(this, R.id.list_item,group,groupContacts);
    		lv_group_contacts.setAdapter(mAdapter);
    		mAdapter.notifyDataSetChanged();

	}
	@Override
	protected void onResume() {
		Log.d("MANAGE GROUP","On Resume");
		super.onResume();

	}

	protected void onClickAddGroupMembers() {
         Intent i = new Intent(this,AddMembersActivity.class);
         i.putExtra("group_name", groupName);
         startActivity(i);
         finish();
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(this,GroupActivity.class);
            startActivity(i);
            finish();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}

}
