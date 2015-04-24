package com.greatapp.qpinion.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greatapp.qpinion.R;
import com.greatapp.qpinion.contacts.QpinionContact;
import com.greatapp.qpinion.group.GroupManager;
import com.greatapp.qpinion.group.QpinionGroup;


public class ContactListItemView extends LinearLayout{
      QpinionContact qpinionContact;
	private QpinionGroup qpinionGroup;
	private Context mContext;
	private ImageView b_add_contact;
	private TextView tv_name;
	private ImageView b_invite_contact;
	private static String TAG = "ContactListItemView";
	public ContactListItemView(Context context,AttributeSet attrs, QpinionContact contact,QpinionGroup targetGroup) {
		super(context, attrs);
		mContext = context;
		qpinionContact = contact;
		qpinionGroup = targetGroup;
		 LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 ViewGroup  viewGroup = (ViewGroup) inflater.inflate(R.layout.contact_item_view, this, true);
		 tv_name = (TextView)viewGroup.findViewById(R.id.textView_contact_name);
		 TextView tv_number = (TextView)viewGroup.findViewById(R.id.textView_contact_number);
		 b_add_contact = (ImageView)viewGroup.findViewById(R.id.iv_add);
		 b_invite_contact = (ImageView)viewGroup.findViewById(R.id.iv_invite);
		 Log.d("GCM","creating contact view with : "+contact.getName());
		 tv_name.setText(contact.getName());
		 tv_number.setText(contact.getmPhoneNumber());
		 if(targetGroup.hasContact(contact)) {
			 b_add_contact.setVisibility(View.GONE);
			 b_invite_contact.setVisibility(View.GONE);
			 viewGroup.setBackgroundColor(getResources().getColor(R.color.darkmagenta));
		 } else {
			if(contact.getRegistered()) {
				viewGroup.setBackgroundColor(getResources().getColor(R.color.q_blue_300));
		         b_add_contact.setOnClickListener(new OnClickListener() {
			     
			      @Override
			      public void onClick(View v) {
			         onClickAddContactToGroup();
				
		    	   }
		         });
		         b_invite_contact.setVisibility(View.GONE);
			} else {
				b_invite_contact.setOnClickListener(new OnClickListener() {
						
				      @Override
				      public void onClick(View v) {
				         onClickInviteContact();
					
			    	   }
			         });
			     b_add_contact.setVisibility(View.GONE);	
			}
		 }
	}

	protected void onClickInviteContact() {
       Log.d(TAG ,"Contact invited");
	}

	protected void onClickAddContactToGroup() {
        GroupManager gm = new GroupManager(mContext);
        gm.addGroupContact(qpinionContact, qpinionGroup);
        b_add_contact.setVisibility(View.GONE);
	}
}