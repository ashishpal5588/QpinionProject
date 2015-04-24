package com.greatapp.qpinion.group;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.greatapp.qpinion.contacts.QpinionContact;
import com.greatapp.qpinion.view.GroupContactListItemView;

public class GroupContactAdapter extends ArrayAdapter<QpinionContact>{

	private Context mContext;
	private int mResId;
	private ArrayList<QpinionContact> mGroupContacts;
	private QpinionGroup mGroup;

	public GroupContactAdapter(Context context, int resId,QpinionGroup group,ArrayList<QpinionContact> groupContacts) {
		super(context, resId, groupContacts);
		mGroupContacts = groupContacts;
        mContext = context;
        mResId = resId;
        mGroup = group;
        Log.d("MANAGE GROUP","creating adapter Total Item : "+mGroupContacts.size());
	}


	@Override
	public View getView(int pos, View arg1, ViewGroup arg2) {
		Log.d("MANAGE GROUP","getView for pos : "+pos+" Total Item : "+mGroupContacts.size());
		
          GroupContactListItemView view = new GroupContactListItemView(mContext, null,mGroup, mGroupContacts.get(pos));
		return view;
	}


}
