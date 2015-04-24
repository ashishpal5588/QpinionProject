package com.greatapp.qpinion.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.greatapp.qpinion.view.OpinionView;



public class OpinionCursorAdapter extends CursorAdapter{


public static String TAG = "QPINION_CURSOR_ADAPTER";
private Opinion opinion;
private Context mContext;

	public OpinionCursorAdapter(Context context, Cursor c, boolean autoRequery) {
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
        DataManager dm = new DataManager(context);
        opinion = dm.createOpinionFromCursor(context,cursor);
        if(opinion == null) {
       	 Log.d(TAG,"opinion = null");
        }
        ((OpinionView)view).initView(opinion);
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup arg2) {
		Log.d(TAG,"newView");

         View view = new OpinionView(context, null);
		return view;
	}
	

}
