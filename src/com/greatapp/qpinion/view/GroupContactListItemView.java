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
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.contacts.QpinionContact;
import com.greatapp.qpinion.group.GroupManager;
import com.greatapp.qpinion.group.QpinionGroup;


public class GroupContactListItemView extends LinearLayout{

	private Context mContext;
	private String groupContactName;
	private QpinionGroup mGroup;
	private QpinionContact mGroupContact;
	private ViewGroup viewGroup;

	public GroupContactListItemView(Context context,AttributeSet attrs,QpinionGroup group, QpinionContact groupContact) {
		super(context, attrs);
		mContext = context;
		mGroup = group;
		mGroupContact = groupContact;
		 LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  viewGroup = (ViewGroup) inflater.inflate(R.layout.group_contact_list_item_view, this, true);
		 TextView tv_name = (TextView)viewGroup.findViewById(R.id.tv_group_contact_name);
		 TextView tv_phone = (TextView)viewGroup.findViewById(R.id.tv_phone);
		 ImageView b_remove_group_contact = (ImageView)viewGroup.findViewById(R.id.iv_remove);
		 Log.d("GROUP","creating group contact view with : "+groupContact.getName());
		 tv_name.setText(groupContact.getName());
		 tv_phone.setText(groupContact.getmPhoneNumber());
		 groupContactName = groupContact.getName();
		 b_remove_group_contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

			onClickRemoveGroupContact();
			}
		});
		if(mGroup.getName().equals(V.DEFAULT_GROUP_NAME)) {
			b_remove_group_contact.setVisibility(View.GONE);
		}
	}

	protected void onClickRemoveGroupContact() {
        GroupManager gm = new GroupManager(getContext());
		//gm.getGroupByName(getContext(), name);
        viewGroup.setVisibility(View.GONE);
        gm.deleteGroupContactFromGroup(mGroup,mGroupContact);

	}
}