package com.greatapp.qpinion.constants;

import android.net.Uri;

public class V {
	public static final int MAX_CONTENT_COUNT_TO_VIEW = 20;
	public static final String SPLIT = "split";
	//---------------Data Base----------------------------------
	public static final String DB_qpinion_DB_NAME = "qpinionClientDataBase";
	public static final int DB_qpinion_DB_VERSION = 1;
	
	
	public static final String DB_CLIENT_TABLE_NAME = "clientTableName";
	public static final String DB_SERVER_TABLE_NAME = "serverTableName";
	public static final Uri DB_CLIENT_TABLE_URI = Uri.parse("content://com.greatapp.qpinion.qpiniondb/"+DB_CLIENT_TABLE_NAME);
	public static final Uri DB_SERVER_TABLE_URI = Uri.parse("content://com.greatapp.qpinion.qpiniondb/"+DB_SERVER_TABLE_NAME);
	public static final String PROVIDER = "com.greatapp.qpinion.qpiniondb";
	public static final String DB_CONTACT_PHONE_NUMBER = "contactPhoneNumber";
	public static final String DB_ITEM_ID = "dbItemId";
	public static final String DB_CONTENT_ID = "contentId";
	public static final String DB_OWNER_NAME = "ownerName";
	public static final String DB_CONTACT_NAME = "contactName";
	public static final String DB_CONTENT_TYPE = "contentType";
	public static final String DB_CONTENT_ANSWER_TYPE = "contentAnswerType";
	public static final String DB_CONTENT_TEXT = "contentText";
	public static final String DB_OPTION_COUNT = "optionCount";
	public static final String DB_OPTIONS = "options";
	public static final String DB_TIME = "time";
	public static final String DB_DURATION = "duration";
	public static final String DB_IS_ANONYMOSLY_ANSWERED = "anonymouslyAnswered";
	public static final String DB_IS_ANONYMOSLY_ASKED = "anonymouslyAsked";
	public static final String DB_IS_ANSWERED = "answerd";
	public static final String DB_ANSWER = "answer";
	
	public static final String DB_GROUP_TABLE_NAME = "groupTableName";
	public static final String DB_GROUP_NAME = "groupName";
	public static final String DB_GROUP_ID = "_id";
	public static final String DB_GROUP_CONTACTS_COUNTS = "groupCount";
	public static final Uri DB_GROUPS_TABLE_URI =Uri.parse("content://com.greatapp.qpinion.qpiniondb/"+DB_GROUP_TABLE_NAME);
	public static final String DB_GROUP_CONTACTS_NAMES = "groupContactsNames";
	
	public static final String DB_CONTACT_TABLE_NAME = "contactTableName";
	public static final String DB_CONTACT_ID = "_id";
	public static final Uri DB_CONTACTS_TABLE_URI =Uri.parse("content://com.greatapp.qpinion.qpiniondb/"+DB_CONTACT_TABLE_NAME);
	public static final String DB_CONTACT_GROUP_COUNTS = "contactGroupCounts";
	public static final String DB_CONTACT_GROUPS_NAMES = "contactGroupNames";
	public static final String DB_REGISTERED = "registered";
	
	public static final String[] DB_GROUPS_TABLE_PROJECTION = new String[]{
        DB_GROUP_ID,DB_GROUP_NAME,DB_GROUP_CONTACTS_COUNTS,DB_GROUP_CONTACTS_NAMES
};
	
	public static final String[] DB_CONTENT_TABLE_PROJECTION = new String[]{
		                              DB_ITEM_ID,DB_CONTENT_ID,DB_OWNER_NAME
		                              ,DB_CONTACT_NAME
		                              ,DB_CONTENT_TYPE,DB_CONTENT_ANSWER_TYPE
		                              ,DB_CONTENT_TEXT,DB_OPTION_COUNT,DB_OPTIONS
		                              ,DB_TIME,DB_DURATION,DB_IS_ANONYMOSLY_ANSWERED
		                              ,DB_IS_ANONYMOSLY_ASKED,DB_IS_ANSWERED,DB_ANSWER};
	public static final String[] DB_CONTACT_TABLE_PROJECTION = new String[]{
		                             DB_CONTACT_ID,DB_CONTACT_NAME,DB_CONTACT_PHONE_NUMBER
		                             ,DB_CONTACT_GROUP_COUNTS,DB_REGISTERED,DB_CONTACT_GROUPS_NAMES
	};
	//==========================================================
	
	public static final String NOTIFY_BROADCAST_ACTION = "com.greatapp.qpinion.NOTIFY_ACTION";
	
	
	
	//================== SEVER DETAILS ====================================
    /**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */
  //  static final String SERVER_URL = "http://qpinion.webuda.com/";
    public static final String SERVER_URL = "http://1-dot-gyan-opinium1727.appspot.com";
    public static final String SENDER_ID ="692708394032";// "summer-gadget-630";
    public static final String TAG = "GCMDemo";
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.greatapp.qpinion.ACTION.DISPLAY_ACTION";
    public static final String DISPLAY_MESSAGE = "displayMessage";
    public static final String REGISTRATION_MESSAGE_ACTION = "com.greatapp.qpinion.ACTION.REGISTRATION_ACTION";
	public static final String REGISTRATION_RESULT = "registrationResult";
	public static final String USER_PREFERENCES = "qpinionUserPreferences";
	
	/*Project ID: gyan-qpinion1727
	Project Number: 692708394032

	API KEY : AIzaSyDNbhhCVN_M7BepHZEkc0_250ceLVZiamQ */
	
	
     //-------------PARAMETERS FOR SERVER POST----------//
	
	public static final String EMAIL_ID = "email";
	public static final String REG_ID = "regId";
	public static final String MSG = "msg";
	public static final String EMAIL_TO = "emailTo";
	public static final String USER_NAME = "userName";
	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String GENDER = "gender";
	public static final String FROM = "comingfrom";
	//---------------------------------------------------//
	public static final int GROUP_CONTACTS_LOADER = 1;
	
	public static final String MESSAGE_TYPE = "messageType";
	public static final String MESSAGE_APPRAISE = "messageTypeAppraise";
	public static final String MESSAGE_OPINION = "messageTypeOpinion";

  //================== SEVER DETAILS ====================================
	public static final String DEFAULT_GROUP_NAME = "Qpinions";
	public static final String NA = "NA";	


}
