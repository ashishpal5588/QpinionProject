package com.greatapp.qpinion.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.util.Log;

import com.greatapp.qpinion.Tools;
import com.greatapp.qpinion.constants.DB;
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.contacts.QpinionContact;

public class Opinion {
	public final static int RECEIVER_OCEAN = 11;
	public final static int RECEIVER_ALL_CONTACTS = 22;
	public final static int RECEIVER_SELECTED = 33;
	public static final String NOT_REPLIED = "n_r_p";
	public static final int YES = 12;
	public static final int NO = 15;
	private static final String TAG = "Opinion";

	
	private String body = V.NA;
	private int id;
	private String UID;
	private ArrayList<QpinionContact> tagContacts = new ArrayList<QpinionContact>();
	private ArrayList<String> replies = new ArrayList<String>();
	private int replyCount = 0;
	private int tagContactsCount = 0;
	private int optionCount = 2;
	private ArrayList<String> options = new ArrayList<String>();
	private ArrayList<Integer> statics = new ArrayList<Integer>();
	private boolean isAnonymouslyAskedByYou = false;
	private int receiverType = RECEIVER_OCEAN;
	private int type = Appraise.TYPE_COMMENTS;//default
	private QpinionContact lastRepliedBy = null;
	private String lastReply = V.NA;
	private boolean isLastUpdateSeen = false;
	
	public Opinion() {
		
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public ArrayList<QpinionContact> getTagContacts() {
		return tagContacts;
	}

	public void setTagContacts(ArrayList<QpinionContact> tagContacts) {
		this.tagContacts = tagContacts;
	}

	public ArrayList<String> getReplies() {
		return replies;
	}

	public void setReplies(ArrayList<String> replies) {
		this.replies = replies;
		int rply_count = 0;
		for(String reply : replies) {
			if(!reply.equals(NOT_REPLIED)) {
				rply_count++;
			}
		}
		setReplyCount(rply_count);
	}

	public int getTagContactsCount() {
		return tagContactsCount;
	}

	public void setTagContactsCount(int tagContactsCount) {
		this.tagContactsCount = tagContactsCount;
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

	public ArrayList<Integer> getStatics() {
		return statics;
	}

	public void setStatics(ArrayList<Integer> statics) {
		this.statics = statics;
	}

	public int getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(int receiverType) {
		this.receiverType = receiverType;
	}

	public boolean isAnonymouslyAskedByYou() {
		return isAnonymouslyAskedByYou;
	}

	public void setAnonymouslyAskedByYou(boolean isAnonymouslyAsked) {
		this.isAnonymouslyAskedByYou = isAnonymouslyAsked;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOptionSelectedCount(String optioncode) {
       if(replies == null) return 0;
       int count = 0;
       if(getType() == Appraise.TYPE_COMMENTS) return 0;
       for(String reply : replies) {
    	   if(reply.equals(optioncode)) {
    		   count++;
    	   }
       }
       return count;
	}

	public int getTotalReplyCount() {
	       if(replies == null) return 0;
	       int count = replies.size();
	       for(String reply : replies) {
	    	   if(reply.equals(NOT_REPLIED)) {
	    		   count--;
	    	   }
	       }
	       return count;
		}
	public String getReply(QpinionContact contact) {
	       if(replies == null || contact == null || tagContacts == null) return null;
	       int i = tagContacts.indexOf(contact);
	       return replies.get(i);
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

	public ContentValues getOpinionValues() {

		ContentValues row = new ContentValues();
		
		row.put(DB.OPINION_UID, getUID());
		row.put(DB.OPINION_BODY, getBody());
		row.put(DB.OPINION_TAG_CONTACTS , Tools.getMergedPhoneStringFromContacts(getTagContacts()));
		row.put(DB.OPINION_REPLIES , Tools.getMergedStringFromStringList(getReplies()));
		row.put(DB.OPINION_TAG_CONTACTS_COUNT , getTagContactsCount());
		row.put(DB.OPINION_OPTIONS , Tools.getMergedStringFromStringList(getOptions()));
		row.put(DB.OPINION_OPTIONS_COUNT , getOptionCount());
		row.put(DB.OPINION_STATICS , Tools.getMergedStringFromIntegerList(getStatics()));
		row.put(DB.OPINION_IS_YOU_ANONYMOUS , (isAnonymouslyAskedByYou())?Opinion.YES:Opinion.NO);
		row.put(DB.OPINION_RECEIVER_TYPE , getReceiverType());
		row.put(DB.OPINION_TYPE , getType());
		row.put(DB.OPINION_LAST_REPLIED_BY , (getLastRepliedBy() == null)?V.NA:getLastRepliedBy().getmPhoneNumber());
		row.put(DB.OPINION_LAST_REPLY , getLastReply());
		row.put(DB.OPINION_IS_LAST_UPDATE_SEEN , (isLastUpdateSeen()?Opinion.YES:Opinion.NO));		
		return row;

	}

	public static ArrayList<String> createNewReplies(int tag_contact_count) {
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i< tag_contact_count ; i++){
			list.add(NOT_REPLIED);
		}
		return list;
	}

	public static ArrayList<Integer> createNewStatics(int option_count) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i< option_count ; i++){
			list.add(0);
		}
		return list;
	}

	public void updateReplies(String answer, String comingFrom) {
		Log.d(TAG,"updating replies with answer:"+answer);
       if(tagContacts != null){
    	   int index = findIndexOfContact(comingFrom,tagContacts);
    	   if(index < 0){
    		   Log.e(TAG,"ERROR in finding contacts for updating replies");
    		   return;
    	   }
    	   if(replies != null){
    		   replies.set(index, answer);
    		   Log.d(TAG,"update answer:"+answer+" at index "+index+" for contact : "+tagContacts.get(index).getName());
    	   }
    	   updateStatics(answer);
       }
	}

	public int findIndexOfContact(String comingFrom, ArrayList<QpinionContact> list) {
		int i = 0;
        for(QpinionContact contact : list){
        	if(contact.getmPhoneNumber().equals(comingFrom)) {
        		return i; 
        	}
        	i++;
        }
		return -1;
	}

	private void updateStatics(String answer) {
       if(statics != null) {
    	   if(type == Appraise.TYPE_OPTIONS) {
    		   if(answer.equals(Appraise.OPTION_1)) {
    			   statics.set(0,statics.get(0)+1);
    		   } else if(answer.equals(Appraise.OPTION_2)) {
    			   statics.set(1,statics.get(1)+1);
    		   } else if(answer.equals(Appraise.OPTION_3)) {
    			   statics.set(2,statics.get(2)+1);
    		   } else if(answer.equals(Appraise.OPTION_4)) {
    			   statics.set(3,statics.get(3)+1);
    		   }
    	   } else if(type == Appraise.TYPE_COMMENTS) {
    		   
    	   } else if(type == Appraise.TYPE_RATING) {
    		   
    	   }
       }
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public QpinionContact getLastRepliedBy() {
		return lastRepliedBy;
	}

	public void setLastRepliedBy(QpinionContact lastRepliedBy) {
		this.lastRepliedBy = lastRepliedBy;
	}

	public String getLastReply() {
		return lastReply;
	}

	public void setLastReply(String lastReply) {
		this.lastReply = lastReply;
	}

	public boolean isLastUpdateSeen() {
		return isLastUpdateSeen;
	}

	public void setLastUpdateSeen(boolean isLastUpdateSeen) {
		this.isLastUpdateSeen = isLastUpdateSeen;
	}



}
