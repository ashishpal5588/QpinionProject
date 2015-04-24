package com.greatapp.qpinion.constants;

import com.greatapp.qpinion.data.Opinion;

import android.net.Uri;

public class DB {

	public final static String OPINION_TABLE_NAME = "opinionTable";
	public static final Uri OPINION_TABLE_URI =Uri.parse("content://com.greatapp.qpinion.qpiniondb/"+OPINION_TABLE_NAME);
		
	public final static String OPINION_BODY = "opinionBody";
	public final static String OPINION_ID = "_id";
	public final static String OPINION_UID = "opinionUId";
	public final static String OPINION_TAG_CONTACTS = "tagContacts";
	public final static String OPINION_REPLIES = "replies";
	public final static String OPINION_TAG_CONTACTS_COUNT = "tagContactsCount";
	public final static String OPINION_OPTIONS = "options";
	public static final String OPINION_OPTIONS_COUNT = "optionsCount";
	public final static String OPINION_STATICS = "statics";
	public final static String OPINION_IS_YOU_ANONYMOUS = "isYouAnonymous";
	public final static String OPINION_RECEIVER_TYPE = "receiverType";
	public final static String OPINION_TYPE = "opinionType";
	public final static String OPINION_LAST_REPLIED_BY = "lastRepliedBy";
	public final static String OPINION_LAST_REPLY = "lastReply";
	public final static String OPINION_IS_LAST_UPDATE_SEEN ="updateSeen";
	
	public static final String[] OPINION_TABLE_PROJECTION = new String[]{
		OPINION_BODY,OPINION_ID,OPINION_UID,OPINION_TAG_CONTACTS,OPINION_REPLIES,OPINION_TAG_CONTACTS_COUNT,
		OPINION_OPTIONS,OPINION_OPTIONS_COUNT,OPINION_STATICS,OPINION_IS_YOU_ANONYMOUS,OPINION_RECEIVER_TYPE,
		OPINION_TYPE,OPINION_LAST_REPLIED_BY,OPINION_LAST_REPLY, OPINION_IS_LAST_UPDATE_SEEN};

	
	
	
	public final static String APPRAISE_TABLE_NAME = "appraiseTable";
	public static final Uri APPRAISE_TABLE_URI =Uri.parse("content://com.greatapp.qpinion.qpiniondb/"+APPRAISE_TABLE_NAME);
	
    public final static String APPRAISE_BODY  = "appraiseBody";
    public final static String APPRAISE_ID  = "_id";
    public final static String APPRAISE_UID  = "appraiseUId";
    public final static String APPRAISE_OWNER = "owner";
    public final static String APPRAISE_OPTION_COUNT = "optionsCount";
    public final static String APPRAISE_OPTIONS = "options";
    public final static String APPRAISE_IS_OWNER_ANONYMOUS = "isOwnerAnom";
    public final static String APPRAISE_IS_YOU_ANONYMOUSLY_REPLIED = "isYouRplyAnom";
    public final static String APPRAISE_ANSWER = "answer";
    public static final String APPRAISE_TYPE = "appraiseType";
    
    public static final String[] APPRAISE_TABLE_PROJECTION = new String[]{
          APPRAISE_BODY,APPRAISE_ID,APPRAISE_UID,APPRAISE_OWNER,APPRAISE_OPTION_COUNT,APPRAISE_OPTIONS,
          APPRAISE_IS_OWNER_ANONYMOUS,APPRAISE_IS_YOU_ANONYMOUSLY_REPLIED,
          APPRAISE_ANSWER,APPRAISE_TYPE };
	
	
	
}
