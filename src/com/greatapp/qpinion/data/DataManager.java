package com.greatapp.qpinion.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.greatapp.qpinion.Tools;
import com.greatapp.qpinion.constants.DB;
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.contacts.ContactManager;
import com.greatapp.qpinion.contacts.QpinionContact;

public class DataManager {

	private static final String TAG = "QPINION_DATA_MANAGER";

	public DataManager(Context context) {
		// TODO Auto-generated constructor stub
	}

	public void updateAppraise(Context context, Appraise appraise) {
		String where = DB.APPRAISE_ID + "=" + appraise.getId();
		String[] selectionArgs = null;
		ContentValues values = appraise.getAppraiseValues();
		context.getContentResolver().update(DB.APPRAISE_TABLE_URI, values, where, selectionArgs);
	}
	
	public void updateOpinion(Context context, Opinion opinion) {
		String where = DB.OPINION_ID + "=" + opinion.getId();
		String[] selectionArgs = null;
		ContentValues values = opinion.getOpinionValues();
		context.getContentResolver().update(DB.OPINION_TABLE_URI, values, where, selectionArgs);
		
	}

	public ArrayList<Opinion> getOpinionList(Context context) {
		ArrayList<Opinion> opinions = new ArrayList<Opinion>();
        Cursor c = context.getContentResolver().query(DB.OPINION_TABLE_URI,
        		           DB.OPINION_TABLE_PROJECTION, null, null, null);
        if(c != null && c.getCount() > 0) {
        	c.moveToFirst();
        	
        	while(!c.isAfterLast()) {
        		Opinion opinion = createOpinionFromCursor(context,c);
        		Log.d(TAG,"opinionn created from cursor UID:"+opinion.getUID());
        		opinions.add(opinion);
                c.moveToNext();
        	}
        	c.close();
        }
        return opinions;
	}

	public Opinion createOpinionFromCursor(Context context,Cursor c) {
		if(c == null) return null;
		int id               = c.getInt(c.getColumnIndex(DB.OPINION_ID));
		String uid          = c.getString(c.getColumnIndex(DB.OPINION_UID));
		String body          = c.getString(c.getColumnIndex(DB.OPINION_BODY));
		String _tag_contacts = c.getString(c.getColumnIndex(DB.OPINION_TAG_CONTACTS));
		String _replies      = c.getString(c.getColumnIndex(DB.OPINION_REPLIES));
		int contacts_count   = c.getInt(c.getColumnIndex(DB.OPINION_TAG_CONTACTS_COUNT));
		String _options      = c.getString(c.getColumnIndex(DB.OPINION_OPTIONS));
		int option_count               = c.getInt(c.getColumnIndex(DB.OPINION_OPTIONS_COUNT));
		String _statics      = c.getString(c.getColumnIndex(DB.OPINION_STATICS));
		int is_you_anonymous = c.getInt(c.getColumnIndex(DB.OPINION_IS_YOU_ANONYMOUS));
		int receiver_type    = c.getInt(c.getColumnIndex(DB.OPINION_RECEIVER_TYPE));
		String _last_replyer  = c.getString(c.getColumnIndex(DB.OPINION_LAST_REPLIED_BY));
		String last_reply    = c.getString(c.getColumnIndex(DB.OPINION_LAST_REPLY));
		int is_update_seen    = c.getInt(c.getColumnIndex(DB.OPINION_IS_LAST_UPDATE_SEEN));
		int type    = c.getInt(c.getColumnIndex(DB.OPINION_TYPE));
		
		Opinion opinion = new Opinion();
		opinion.setId(id);
		opinion.setUID(uid);
		opinion.setBody(body);
		ContactManager cm = new ContactManager(context);
		String[] tag_contacts = _tag_contacts.split(V.SPLIT);
		opinion.setTagContacts(cm.getContactListByPhones(tag_contacts));
		String[] replies = _replies.split(V.SPLIT);
		opinion.setReplies(new ArrayList<String>( Arrays.asList(replies)));
		opinion.setTagContactsCount(contacts_count);
		String[] options = _options.split(V.SPLIT);
		opinion.setOptions(new ArrayList<String>( Arrays.asList(options)));
		opinion.setOptionCount(option_count);
		String[] staticsS = _statics.split(V.SPLIT);
		Integer[] statics = Tools.getIntegerArrayFromStringArray(staticsS);
		opinion.setStatics(new ArrayList<Integer>(Arrays.asList(statics)));
		opinion.setAnonymouslyAskedByYou(((is_you_anonymous == Opinion.YES)?true:false));
		opinion.setReceiverType(receiver_type);
		QpinionContact last_replyer = cm.getContactByPhone(_last_replyer);
		opinion.setLastRepliedBy(last_replyer);
		opinion.setLastReply(last_reply);
		opinion.setLastUpdateSeen(((is_update_seen == Opinion.YES)?true:false));
		opinion.setType(type);
		Log.d(TAG,"returning opinion");
		return opinion;
	}

	public ArrayList<Appraise> getAppraiseList(Context context) {
		ArrayList<Appraise> appraises = new ArrayList<Appraise>();
        Cursor c = context.getContentResolver().query(DB.APPRAISE_TABLE_URI,
        		           DB.APPRAISE_TABLE_PROJECTION, null, null, null);
        if(c != null && c.getCount() > 0) {
        	c.moveToFirst();
        	
        	while(!c.isAfterLast()) {
                Appraise appraise = createAppraiseFromCursor(context,c);
        		appraises.add(appraise);
                c.moveToNext();
        	}
        	c.close();
        }
        return appraises;
 }
	public Appraise createAppraiseFromCursor(Context context, Cursor c) {
        if(c == null) return new Appraise();
		int id                   = c.getInt(c.getColumnIndex(DB.APPRAISE_ID));
		String uid               = c.getString(c.getColumnIndex(DB.APPRAISE_UID));
		String body              = c.getString(c.getColumnIndex(DB.APPRAISE_BODY));
		String owner             = c.getString(c.getColumnIndex(DB.APPRAISE_OWNER));
		int option_count         = c.getInt(c.getColumnIndex(DB.APPRAISE_OPTION_COUNT));
		String _options          = c.getString(c.getColumnIndex(DB.APPRAISE_OPTIONS));
		int is_owner_anonymous   = c.getInt(c.getColumnIndex(DB.APPRAISE_IS_OWNER_ANONYMOUS));
		int is_you_anonymous     = c.getInt(c.getColumnIndex(DB.APPRAISE_IS_YOU_ANONYMOUSLY_REPLIED));
		String answer            = c.getString(c.getColumnIndex(DB.APPRAISE_ANSWER));
		int type                 = c.getInt(c.getColumnIndex(DB.APPRAISE_TYPE));

		Appraise appraise = new Appraise();
		appraise.setId(id);
		appraise.setUID(uid);
		appraise.setBody(body);
		appraise.setOwnerNumber(owner);
		ContactManager cm = new ContactManager(context);
		String name = cm.getContactNameFromNumber(owner);
		appraise.setOwnerName(name);
		appraise.setOptionCount(option_count);
		String[] options = _options.split(V.SPLIT);
		appraise.setOptions(new ArrayList<String>( Arrays.asList(options)));
		appraise.setOwnerAnonymous(((is_owner_anonymous == Opinion.YES)?true:false));
		appraise.setYouAnonymouslyReplied(((is_you_anonymous == Opinion.YES)?true:false));
		appraise.setAnswer(answer);
		appraise.setType(type);
		return appraise;
	}
	public static void insertOpinion(Context context,Opinion opinion) {
        if(opinion != null) {
            ContentValues values = opinion.getOpinionValues();
            context.getContentResolver().insert(DB.OPINION_TABLE_URI, values);
         }
	}

	public static void insertAppraise(Context context,Appraise appraise) {
        if(appraise != null) {
            ContentValues values = appraise.getAppraiseValues();
            context.getContentResolver().insert(DB.APPRAISE_TABLE_URI, values);
         }
	}
	public static String generateUniqueId() {
        String uid = UUID.randomUUID().toString();
		return uid;
	}

	public String handleAppraiseMSG(Context context,Intent intent) {
		String owner = intent.getExtras().getString(DB.APPRAISE_OWNER);
		String uid = intent.getExtras().getString(DB.APPRAISE_UID);
		String body = intent.getExtras().getString(DB.APPRAISE_BODY);
		String o_count = intent.getExtras().getString(DB.APPRAISE_OPTION_COUNT);
		String options = intent.getExtras().getString(DB.APPRAISE_OPTIONS);
		String o_anonymous = intent.getExtras().getString(DB.APPRAISE_IS_OWNER_ANONYMOUS);
	//	String u_anonymous = intent.getExtras().getString(DB.APPRAISE_IS_YOU_ANONYMOUSLY_REPLIED);
	//	String answer = intent.getExtras().getString(DB.APPRAISE_ANSWER);
		String appraise_type = intent.getExtras().getString(DB.APPRAISE_TYPE);
		
		Appraise appraise = new Appraise();
		appraise.setOwnerNumber(owner);
		ContactManager cm = new ContactManager(context);
		String name = cm.getContactNameFromNumber(owner);
		if(name != null) {
			appraise.setOwnerName(name);
		}
		appraise.setUID(uid);
		appraise.setBody(body);
		appraise.setOptionCount(Integer.parseInt(o_count));
		appraise.setOptions(Tools.getArrayListFromStringArray(Tools.getSplittedArray(options, V.SPLIT)));
		appraise.setOwnerAnonymous(o_anonymous.equals(Opinion.YES) ? true:false);
		appraise.setType(Integer.parseInt(appraise_type));
		insertAppraise(context, appraise);
		//Toast.makeText(context, "Question received !",Toast.LENGTH_SHORT).show();
		return name;
	}

	public String handleOpinionMSG(Context context, Intent intent) {
        String answer = intent.getExtras().getString(DB.APPRAISE_ANSWER);
    	String comingFrom = intent.getExtras().getString(V.FROM);
    	String uid = intent.getExtras().getString(DB.OPINION_UID);
        Opinion opinion = getOpinionByUID(context,uid);
        if(opinion == null) {
        	Log.d(TAG,"opinion with UID:"+uid+" not found");
        	return null;
        }
        int i = opinion.findIndexOfContact(comingFrom, opinion.getTagContacts());
        String replyer_name = opinion.getTagContacts().get(i).getName();
        opinion.setLastRepliedBy(opinion.getTagContacts().get(i));
        opinion.setLastReply(answer);
        opinion.setLastUpdateSeen(false);
  		opinion.updateReplies(answer,comingFrom);
  		updateOpinion(context,opinion);
  		//Toast.makeText(context, "Opinion received !",Toast.LENGTH_SHORT).show();
  		return replyer_name;
	}

	private Opinion getOpinionByUID(Context context,String uid) {
        ArrayList<Opinion> list = getOpinionList(context);
        Opinion opinion = getOpinionByUIDFromList(list,uid);
		return opinion;
	}

	private Opinion getOpinionByUIDFromList(ArrayList<Opinion> list, String uid) {
       for(Opinion opinion : list) {
    	   if(uid.equals(opinion.getUID())){
    		   return opinion;
    	   }
       }
		return null;
	}

	public Opinion getOpinionFromId(Context context, int id) {
		ArrayList<Opinion> list = getOpinionList(context);
	       for(Opinion opinion : list) {
	    	   if(id == opinion.getId()){
	    		   return opinion;
	    	   }
	       }
			return null;
	}

	public void deleteOpinion(Context context, Opinion opinion) {
		String where = DB.OPINION_ID + "=" + opinion.getId();
		String[] selectionArgs = null;
		ContentValues values = opinion.getOpinionValues();
		context.getContentResolver().delete(DB.OPINION_TABLE_URI, where, selectionArgs);
	}

	public void deleteAppraise(Context context, Appraise appraise) {
		String where = DB.APPRAISE_ID + "=" + appraise.getId();
		String[] selectionArgs = null;
		ContentValues values = appraise.getAppraiseValues();
		context.getContentResolver().delete(DB.APPRAISE_TABLE_URI, where, selectionArgs);		
	}

	public int getUnseenOpinionCount(Context context) {
      ArrayList<Opinion> list = getOpinionList(context);
      int count = 0;
      for(Opinion opinion : list) {
    	  if(!opinion.isLastUpdateSeen()){
    		  count++;
    	  }
      }
      return count;
	}

	public int getUnAnsweredAppraiseCount(Context context) {
	      ArrayList<Appraise> list = getAppraiseList(context);
	      int count = 0;
	      for(Appraise appraise : list) {
	    	  if(appraise.getAnswer().equals(""+Appraise.NOT_ANSWERED)){
	    		  count++;
	    	  }
	      }
	      return count;
	}


}
