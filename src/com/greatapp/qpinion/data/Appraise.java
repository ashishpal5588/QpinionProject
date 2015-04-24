package com.greatapp.qpinion.data;

import java.util.ArrayList;

import android.content.ContentValues;

import com.greatapp.qpinion.Tools;
import com.greatapp.qpinion.constants.DB;
import com.greatapp.qpinion.contacts.ContactManager;

public class Appraise {
	public final static int NOT_ANSWERED = 10;
	
	public final static int  TYPE_RATING = 1;
	public final static int  TYPE_OPTIONS = 2;
	public final static int  TYPE_COMMENTS = 3;

	public static final String OPTION_1 = "o1";
	public static final String OPTION_2 = "o2";
	public static final String OPTION_3 = "o3";
	public static final String OPTION_4 = "o4";

	private String body = "NA";
	private int id;
	private String UID;
	private String ownerNumber = "NA";
	private String ownerName = "NA";
	private int optionCount = 2;
	private ArrayList<String> options = new ArrayList<String>();
	private boolean isOwnerAnonymous = false;
	private boolean isYouAnonymouslyReplied = false;
    private String answer = ""+NOT_ANSWERED;
    private int type = TYPE_OPTIONS;//default

	public Appraise() {
		
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}


	public String getOwnerNumber() {
		return ownerNumber;
	}

	public void setOwnerNumber(String ownerNumber) {
		this.ownerNumber = ownerNumber;
	}
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getOptionCount() {
		return optionCount;
	}

	public void setOptionCount(int optionCount) {
		this.optionCount = optionCount;
	}

	public ArrayList<String> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<String> options) {
		this.options = options;
	}

	public boolean isOwnerAnonymous() {
		return isOwnerAnonymous;
	}

	public void setOwnerAnonymous(boolean isOwnerAnonymous) {
		this.isOwnerAnonymous = isOwnerAnonymous;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isYouAnonymouslyReplied() {
		return isYouAnonymouslyReplied;
	}

	public void setYouAnonymouslyReplied(boolean isYouAnonymouslyReplied) {
		this.isYouAnonymouslyReplied = isYouAnonymouslyReplied;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public ContentValues getAppraiseValues() {

		ContentValues row = new ContentValues();
		
		row.put(DB.APPRAISE_UID, getUID());
		row.put(DB.APPRAISE_BODY, getBody());
		row.put(DB.APPRAISE_OWNER , getOwnerNumber());
		row.put(DB.APPRAISE_OPTION_COUNT , getOptionCount());
		row.put(DB.APPRAISE_OPTIONS , Tools.getMergedStringFromStringList(getOptions()));
		row.put(DB.APPRAISE_IS_OWNER_ANONYMOUS , (isOwnerAnonymous())?Opinion.YES:Opinion.NO);
		row.put(DB.APPRAISE_IS_YOU_ANONYMOUSLY_REPLIED , (isYouAnonymouslyReplied())?Opinion.YES:Opinion.NO);
		row.put(DB.APPRAISE_ANSWER , getAnswer());
		row.put(DB.APPRAISE_TYPE , getType());		
		
		return row;
	}


}
