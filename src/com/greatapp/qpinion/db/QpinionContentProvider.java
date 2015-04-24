package com.greatapp.qpinion.db;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.greatapp.qpinion.constants.DB;
import com.greatapp.qpinion.constants.V;

public class QpinionContentProvider extends ContentProvider{
	
	private static final String TAG = "PROVIDER";
	private SQLiteDatabase qpinionDBinstance;
	public static QpinionDBhelper HelperInstance;
	
	@Override
	public boolean onCreate() {
		Log.d("in  cp","Creating DataBase");
		
		//A DBframe will be created with name DB_NAME ="AdminBankDb";
		HelperInstance = new QpinionDBhelper(getContext(), null, null, V.DB_qpinion_DB_VERSION);
		//creating database
		qpinionDBinstance = HelperInstance.getWritableDatabase();
		
		return true;
	}

	static UriMatcher uriMatcher;
	static int OPINION_TABLE = 1;
	static int APPRAISE_TABLE = 2;
	static int CONTACT_TABLE = 3;
	static int GROUP_TABLE = 4;
	//static int TABLE3 = 3;
	static {
	    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	    uriMatcher.addURI("com.greatapp.qpinion.qpiniondb",DB.OPINION_TABLE_NAME, OPINION_TABLE);
	          
	    uriMatcher.addURI("com.greatapp.qpinion.qpiniondb", DB.APPRAISE_TABLE_NAME, APPRAISE_TABLE);
	    
	    uriMatcher.addURI("com.greatapp.qpinion.qpiniondb", V.DB_CONTACT_TABLE_NAME, CONTACT_TABLE);
	    
	    uriMatcher.addURI("com.greatapp.qpinion.qpiniondb", V.DB_GROUP_TABLE_NAME, GROUP_TABLE);
	        
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues newRow) {
		Log.d("in cp","inserting..");
		int table_id =  uriMatcher.match(uri);
		
		Log.e("table_ID-->>",""+table_id);
		
		switch(table_id)//data will be inserted in t1 or t3 of corresponding month
		{
		case 1:
			{
				//this method will accept a row(values) and and insert into the table
				long rowId = qpinionDBinstance.insert(DB.OPINION_TABLE_NAME, null, newRow);
				//Toast.makeText(getContext(), "Inserted", Toast.LENGTH_SHORT).show();
			    if (rowId > 0) {
					Log.d("in cp","inserted in "+DB.OPINION_TABLE_NAME);
			        Uri objUri = ContentUris.withAppendedId(uri, rowId);
			        getContext().getContentResolver().notifyChange(objUri, null);
			    }
				break;
			}

		case 2:
			{
				long rowId = qpinionDBinstance.insert(DB.APPRAISE_TABLE_NAME, null, newRow);
				if (rowId > 0) {
					Log.d("in cp","inserted in "+DB.APPRAISE_TABLE_NAME);
			        Uri objUri = ContentUris.withAppendedId(uri, rowId);
			        getContext().getContentResolver().notifyChange(objUri, null);
			    }

				break;
			}
			
		case 3: 
	      	{
	      		long rowId = qpinionDBinstance.insert(V.DB_CONTACT_TABLE_NAME, null, newRow);
	      		if (rowId > 0) {
					Log.d("in cp","inserted in "+V.DB_CONTACT_TABLE_NAME);
			        Uri objUri = ContentUris.withAppendedId(uri, rowId);
			        getContext().getContentResolver().notifyChange(objUri, null);
			    }

				break;
		    }
		case 4: 
      	{
      		long rowId = qpinionDBinstance.insert(V.DB_GROUP_TABLE_NAME, null, newRow);
      		if (rowId > 0) {
      			Log.d("in cp","inserted in "+V.DB_GROUP_TABLE_NAME);
		        Uri objUri = ContentUris.withAppendedId(uri, rowId);
		        getContext().getContentResolver().notifyChange(objUri, null);
		    }
			break;
	    }
		default:
			Log.e("in cp","URI NOT MATCHED for Inserting!!!");
		
		}//switch
		return null;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder)
	{		
		Cursor c;
		
		int table_id =  uriMatcher.match(uri);
		
		switch(table_id)
		{
		case 1:
			{
				Log.d("in cp","Quering data from "+DB.OPINION_TABLE_NAME);
				c =  qpinionDBinstance.query(DB.OPINION_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
				Log.d("in CP","coursor returned!");
				c.setNotificationUri(getContext().getContentResolver(), uri);
				return c;
			
			}

			
		case 2:
			{Log.d("in cp","Quering data from "+DB.APPRAISE_TABLE_NAME);
			c =  qpinionDBinstance.query(DB.APPRAISE_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
			Log.d("in CP","coursor returned!");
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
			}
		case 3:
		{
			Log.d("in cp","Quering data from "+V.DB_CONTACT_TABLE_NAME);
		    c =  qpinionDBinstance.query(V.DB_CONTACT_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		    Log.d("in CP","coursor returned!");
			c.setNotificationUri(getContext().getContentResolver(), uri);
		    return c;
		}
		case 4:
		{
			Log.d("in cp","Quering data from "+V.DB_GROUP_TABLE_NAME);
		    c =  qpinionDBinstance.query(V.DB_GROUP_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		    Log.d("in CP","coursor returned!");
			c.setNotificationUri(getContext().getContentResolver(), uri);
		    return c;
		}
		default:
			Log.e("in cp","URI NOT MATCHED for Quering data!!!");
		
		}//switch
		
		return null;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) 
	{
		int table_id =  uriMatcher.match(uri);
		Log.d("in CP","updating..");
		
		switch(table_id)
		{
		case 1:
			{
				//this method will accept a row(values) and and insert into the table
				long rowId = qpinionDBinstance.update(DB.OPINION_TABLE_NAME, values, selection, selectionArgs);
				//Toast.makeText(getContext(), "Inserted", Toast.LENGTH_SHORT).show();
				
			    if (rowId > 0) {
			        Uri objUri = ContentUris.withAppendedId(uri, rowId);
			        getContext().getContentResolver().notifyChange(objUri, null);
			        Log.d("in cp","updated : "+DB.OPINION_TABLE_NAME);
			    }
				break;
			}

		case 2:
			{
				long rowId = qpinionDBinstance.update(DB.APPRAISE_TABLE_NAME, values, selection, selectionArgs);
				 if (rowId > 0) {
				        Uri objUri = ContentUris.withAppendedId(uri, rowId);
				        getContext().getContentResolver().notifyChange(objUri, null);
						Log.d("in cp","updated : "+DB.APPRAISE_TABLE_NAME);
				    }

				break;
			}
		case 3:
		{
			long rowId = qpinionDBinstance.update(V.DB_CONTACT_TABLE_NAME, values, selection, selectionArgs);
			 if (rowId > 0) {
			        Uri objUri = ContentUris.withAppendedId(uri, rowId);
			        getContext().getContentResolver().notifyChange(objUri, null);
					Log.d("in cp","DB_CONTACT_TABLE_NAME updated");
			    }

			break;
		}
		
		case 4:
		{
			long rowId = qpinionDBinstance.update(V.DB_GROUP_TABLE_NAME, values, selection, selectionArgs);
			 if (rowId > 0) {
			        Uri objUri = ContentUris.withAppendedId(uri, rowId);
			        getContext().getContentResolver().notifyChange(objUri, null);
					Log.d("in cp","DB_GROUP_TABLE_NAME updated");
			    }

			break;
		}
		default:
			Log.e("in cp","URI NOT MATCHED for Updating data!!!");
		
		}//switch
		return 0;
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) 
	{int table_id =  uriMatcher.match(uri);
	
	switch(table_id)
	{
	case 1:
		{
			Log.d("in cp","Deleting data from "+DB.OPINION_TABLE_NAME);
			long rowId = qpinionDBinstance.delete(DB.OPINION_TABLE_NAME,  selection, selectionArgs);

		    if (rowId > 0) {
		        Uri objUri = ContentUris.withAppendedId(uri, rowId);
		        getContext().getContentResolver().notifyChange(objUri, null);
				Log.d("in CP","Opinion deleted from db");
		    }
			return 0;
		
		}

		
	case 2:
		{
			Log.d("in cp","Deleting data from "+DB.APPRAISE_TABLE_NAME);
		    long rowId = qpinionDBinstance.delete(DB.APPRAISE_TABLE_NAME, selection, selectionArgs);

		    if (rowId > 0) {
		        Uri objUri = ContentUris.withAppendedId(uri, rowId);
		        getContext().getContentResolver().notifyChange(objUri, null);
		        Log.d("in CP","appraise deleted from db");
		    }

		    return 0;
		}
	case 3:
	{
		Log.d("in cp","Deleting data from "+V.DB_CONTACT_TABLE_NAME);
	    long rowId = qpinionDBinstance.delete(V.DB_CONTACT_TABLE_NAME, selection, selectionArgs);
	    if (rowId > 0) {
	        Uri objUri = ContentUris.withAppendedId(uri, rowId);
	        getContext().getContentResolver().notifyChange(objUri, null);
	        Log.d("in CP","contact deleted from db");
	    }
	    return 0;
	}
	case 4:
	{
		Log.d("in cp","Deleting data from "+V.DB_GROUP_TABLE_NAME);
	    long rowId = qpinionDBinstance.delete(V.DB_GROUP_TABLE_NAME, selection, selectionArgs);
	    if (rowId > 0) {
	        Uri objUri = ContentUris.withAppendedId(uri, rowId);
	        getContext().getContentResolver().notifyChange(objUri, null);
	        Log.d("in CP","appraise deleted from db");
	    }
	    return 0;
	}
	default:
		Log.e("in cp","URI NOT MATCHED for Deleting data!!!");
	
	}//switch
	
	return 1;
	}
	@Override
	public String getType(Uri uri) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	

}
