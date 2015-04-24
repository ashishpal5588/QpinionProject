package com.greatapp.qpinion.db;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.greatapp.qpinion.constants.DB;
import com.greatapp.qpinion.constants.V;

public class QpinionDBhelper extends SQLiteOpenHelper {

	public QpinionDBhelper(Context context, String name, CursorFactory factory,int version)
		{
			
			super(context, V.DB_qpinion_DB_NAME, null, V.DB_qpinion_DB_VERSION);
			Log.d("DBhelper","DB constructed!");
			
		}

		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d("DBhelper","Creating Tables...");

			db.execSQL(      "create table "    +DB.OPINION_TABLE_NAME+"("
					+DB.OPINION_ID	     	              +" Integer primary key ,"
					+DB.OPINION_UID                       +" text,"
					+DB.OPINION_BODY      	              +" text,"
					+DB.OPINION_TAG_CONTACTS 	          +" text,"
					+DB.OPINION_REPLIES                   +" text,"
					+DB.OPINION_TAG_CONTACTS_COUNT        +" int,"
					+DB.OPINION_OPTIONS                   +" text,"
					+DB.OPINION_OPTIONS_COUNT             +" int,"
					+DB.OPINION_STATICS                   +" text,"
					+DB.OPINION_IS_YOU_ANONYMOUS          +" int,"
					+DB.OPINION_TYPE                      +" int,"
					+DB.OPINION_LAST_REPLIED_BY 	      +" text,"
					+DB.OPINION_LAST_REPLY                +" text,"
					+DB.OPINION_IS_LAST_UPDATE_SEEN       +" int,"							
					+DB.OPINION_RECEIVER_TYPE  	+" int);"
                    );
			Log.d("DBhelper","query sent for"+DB.OPINION_TABLE_NAME);

			
			db.execSQL(      "create table "    +DB.APPRAISE_TABLE_NAME+"("
					+DB.APPRAISE_ID	     	                 +" Integer primary key ,"
					+DB.APPRAISE_UID                         +" text,"
					+DB.APPRAISE_BODY      	                 +" text,"
					+DB.APPRAISE_OWNER 	                     +" text,"
					+DB.APPRAISE_OPTION_COUNT                +" int,"
					+DB.APPRAISE_OPTIONS                     +" text,"
					+DB.APPRAISE_IS_OWNER_ANONYMOUS          +" int,"
					+DB.APPRAISE_IS_YOU_ANONYMOUSLY_REPLIED  +" int,"
					+DB.APPRAISE_ANSWER                      +" text,"
					+DB.APPRAISE_TYPE                        +" int);"
                    );

            Log.d("DBhelper","query sent for"+DB.APPRAISE_TABLE_NAME);

			db.execSQL(      "create table "    +V.DB_GROUP_TABLE_NAME+"("
												+V.DB_GROUP_ID	     	+" Integer primary key ,"
												+V.DB_GROUP_NAME      	+" text,"
												+V.DB_GROUP_CONTACTS_COUNTS 	+" int,"
												+V.DB_GROUP_CONTACTS_NAMES  	+" text);"
					 );
			
			Log.d("DBhelper","query sent for"+V.DB_GROUP_TABLE_NAME);
			
			db.execSQL(      "create table "    +V.DB_CONTACT_TABLE_NAME+"("

												+V.DB_CONTACT_ID	     	+" Integer primary key ,"
												+V.DB_CONTACT_NAME      	+" text,"

												+V.DB_CONTACT_PHONE_NUMBER  	+" text,"
												+V.DB_CONTACT_GROUP_COUNTS 	+" int,"
												+V.DB_REGISTERED 	+" int,"
												+V.DB_CONTACT_GROUPS_NAMES  	+" text);"
												  
						);
			Log.d("DBhelper","query sent for"+V.DB_CONTACT_TABLE_NAME);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d("DBhelper","upgrading dadabase.... ");


			db.execSQL(      "create table "    +DB.OPINION_TABLE_NAME+"("
					+DB.OPINION_ID	     	              +" Integer primary key ,"
					+DB.OPINION_UID                       +" text,"
					+DB.OPINION_BODY      	              +" text,"
					+DB.OPINION_TAG_CONTACTS 	          +" text,"
					+DB.OPINION_REPLIES                   +" text,"
					+DB.OPINION_TAG_CONTACTS_COUNT        +" int,"
					+DB.OPINION_OPTIONS                   +" text,"
					+DB.OPINION_OPTIONS_COUNT             +" int,"
					+DB.OPINION_STATICS                   +" text,"
					+DB.OPINION_IS_YOU_ANONYMOUS          +" int,"
					+DB.OPINION_TYPE                      +" int,"
					+DB.OPINION_LAST_REPLIED_BY 	      +" text,"
					+DB.OPINION_LAST_REPLY                +" text,"
					+DB.OPINION_IS_LAST_UPDATE_SEEN       +" int,"							
					+DB.OPINION_RECEIVER_TYPE  	+" int);"
                    );
			Log.d("DBhelper","query sent for"+DB.OPINION_TABLE_NAME);

			
			db.execSQL(      "create table "    +DB.APPRAISE_TABLE_NAME+"("
					+DB.APPRAISE_ID	     	                 +" Integer primary key ,"
					+DB.APPRAISE_UID                         +" text,"
					+DB.APPRAISE_BODY      	                 +" text,"
					+DB.APPRAISE_OWNER 	                     +" text,"
					+DB.APPRAISE_OPTION_COUNT                +" int,"
					+DB.APPRAISE_OPTIONS                     +" text,"
					+DB.APPRAISE_IS_OWNER_ANONYMOUS          +" int,"
					+DB.APPRAISE_IS_YOU_ANONYMOUSLY_REPLIED  +" int,"
					+DB.APPRAISE_ANSWER                      +" text,"
					+DB.APPRAISE_TYPE                        +" int);"
                    );

            Log.d("DBhelper","query sent for"+DB.APPRAISE_TABLE_NAME);

			db.execSQL(      "create table "    +V.DB_GROUP_TABLE_NAME+"("

												+V.DB_GROUP_ID	     	+" Integer primary key ,"
												+V.DB_GROUP_NAME      	+" text,"

												+V.DB_GROUP_CONTACTS_COUNTS 	+" int,"
												+V.DB_GROUP_CONTACTS_NAMES  	+" text);"
					 );
			Log.d("DBhelper","query sent for"+V.DB_GROUP_TABLE_NAME);
			
			db.execSQL(      "create table "    +V.DB_CONTACT_TABLE_NAME+"("

												+V.DB_CONTACT_ID	     	+" Integer primary key ,"
												+V.DB_CONTACT_NAME      	+" text,"

												+V.DB_CONTACT_PHONE_NUMBER  	+" text,"
												+V.DB_CONTACT_GROUP_COUNTS 	+" int,"
												+V.DB_REGISTERED 	+" int,"
												+V.DB_CONTACT_GROUPS_NAMES  	+" text);"
												  
						);
			Log.d("DBhelper","query sent for"+V.DB_CONTACT_TABLE_NAME);

		}

	}

