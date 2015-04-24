package com.greatapp.qpinion.group;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class GroupAdapter extends ArrayAdapter<QpinionGroup>{

	private ArrayList<QpinionGroup> mGroups;
	private Context mContext;
	private int mResId;

	public GroupAdapter(Context context, int resId,ArrayList<QpinionGroup> groups) {
		super(context, resId, groups);
		mGroups = groups;
        mContext = context;
        mResId = resId;
        Log.d("GROUP","creating adapter Total Item : "+mGroups.size());
	}


	@Override
	public View getView(int pos, View arg1, ViewGroup arg2) {
		Log.d("GROUP","getView for pos : "+pos+" Total Item : "+mGroups.size());
		
         // GroupListItemView view = new GroupListItemView(mContext, null, mGroups.get(pos));
		return null;
	}
	

	


}
