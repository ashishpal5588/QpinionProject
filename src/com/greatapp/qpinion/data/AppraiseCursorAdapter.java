package com.greatapp.qpinion.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.greatapp.qpinion.view.AppraiseView;



public class AppraiseCursorAdapter extends CursorAdapter{


public static String TAG = "QPINION_A_CURSOR_ADAPTER";
private Appraise appraise;
private Context mContext;

	public AppraiseCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		Log.d(TAG,"constructor");
		mContext = context;

	}



	@Override
	public void bindView(View view, Context context, Cursor cursor) {
        DataManager dm = new DataManager(context);
        int c = cursor.getCount();
        int pos = cursor.getPosition();
        pos = pos+1;
        int p = (c -pos +1);
        //cursor.move(p -pos);
        cursor.moveToPosition(p-1);
        appraise = dm.createAppraiseFromCursor(context,cursor);
        if(appraise == null) {
       	 Log.d(TAG,"opinion = null");
        }
		((AppraiseView)view).initView(appraise);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup arg2) {
		Log.d(TAG,"newView");

         View view = new AppraiseView(context, null);
		return view;
	}
	

}
