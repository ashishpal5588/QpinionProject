package com.greatapp.qpinion.group;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.greatapp.qpinion.view.GroupListItemView;



public class GroupCursorAdapter extends CursorAdapter{


public static String TAG = "QPINION_CURSOR_ADAPTER";



	public GroupCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		Log.d(TAG,"constructor");

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(TAG,"bindView");
        int c = cursor.getCount();
        int pos = cursor.getPosition();
        pos = pos+1;
        int p = (c -pos +1);
        //cursor.move(p -pos);
        cursor.moveToPosition(p-1);
		 GroupManager gm = new GroupManager(context);
         QpinionGroup group = gm.createGroupFromCursor(context,cursor);
         if(group == null) {
        	 Log.d(TAG,"group = null");
         }
         ((GroupListItemView)view).initView(group);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup arg2) {


		return new GroupListItemView(context, null);
	}

}
