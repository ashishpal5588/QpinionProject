package com.greatapp.qpinion.contacts;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.greatapp.qpinion.group.QpinionGroup;
import com.greatapp.qpinion.view.ContactListItemView;

public class ContactAdapter extends ArrayAdapter<QpinionContact>{

	private ArrayList<QpinionContact> mContacts;
	private Context mContext;
	private int mResId;
	private QpinionGroup mTargetGroup;

	public ContactAdapter(Context context, int resId,ArrayList<QpinionContact> contacts , QpinionGroup targetGroup) {
		super(context, resId, contacts);
     mContacts = contacts;
     mContext = context;
     mResId = resId;
     mTargetGroup = targetGroup;
	}






	@Override
	public View getView(int pos, View arg1, ViewGroup arg2) {
		Log.d("GCM","get ContactView  @ pos : "+pos);
		
          ContactListItemView view = new ContactListItemView(mContext, null, mContacts.get(pos),mTargetGroup);
		return view;
	}
	

	


}
