package com.greatapp.qpinion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

import com.greatapp.qpinion.constants.DB;
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.contacts.ContactManager;
import com.greatapp.qpinion.contacts.QpinionContact;
import com.greatapp.qpinion.data.Appraise;
import com.greatapp.qpinion.data.Opinion;
import com.greatapp.qpinion.sever.ServerUtilities;

public class TransactionManager {
	
  private static final String POST_TYPE = "postType";
private static final String POST_APPRAISE = "appraise";
private static final String POST_OPINION = "opinion";

public static void sendToAllOnQpinion(Context context,String msg) {
	ServerUtilities.sendToAllOnQpinion(context, msg);
  }

  
  //Synchronous time expensive call
  // do not call this method from UI thread
public static ArrayList<QpinionContact> setUpContactsOnServer(Context context,
		ArrayList<QpinionContact> contactList) {
	     ArrayList<QpinionContact> q_contacts = new ArrayList<QpinionContact>();
	     String contactString = "";
	     for(QpinionContact contact : contactList){
	    	 contactString = contactString+contact.getmPhoneNumber();
	    	 contactString = contactString+V.SPLIT;
	     }
         String qpinionString = ServerUtilities.setUpContacts(context,contactString);
         String[] qpinionContacts = qpinionString.split(V.SPLIT);
         ContactManager cm = new ContactManager(context);
         q_contacts = cm.getContactListByPhones(qpinionContacts);

	     return q_contacts;
}


public static void sendAppraiseToQpinion(Appraise mAppraise, User user) {
	final String endpoint = V.SERVER_URL + "/sendtoqpinion";
	final Map<String, String> msg = createAppraisePost(mAppraise,user);
    sendData(endpoint,msg);
}


private static Map<String, String> createAppraisePost(Appraise mAppraise,User user) {
	Map<String, String> params = new HashMap<String, String>();
	params.put(POST_TYPE, POST_APPRAISE);
	params.put(V.FROM,user.getPhoneNumber());
	params.put(DB.APPRAISE_OWNER , mAppraise.getOwnerNumber());
	params.put(DB.APPRAISE_IS_YOU_ANONYMOUSLY_REPLIED , (mAppraise.isYouAnonymouslyReplied())?""+Opinion.YES:""+Opinion.NO);
	params.put(DB.APPRAISE_ANSWER , mAppraise.getAnswer());
	params.put(DB.APPRAISE_UID, mAppraise.getUID());
	return params;
}


public static void AskOpinion( Opinion opinion,  User user) {
	final String endpoint = V.SERVER_URL + "/sendtoqpinion";
	final Map<String, String> msg = createOpinionPost(opinion,user);
	//String endpoint = V.SERVER_URL + "/setupcontacts";
    sendData(endpoint,msg);

}

private static void sendData(final String endpoint, final Map<String, String> msg) {
	final AsyncTask<Void, Void, Void> sendtask = new AsyncTask<Void, Void, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
        	 try {
        		 try {
					ServerUtilities.postToQpinion(endpoint, msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                } catch (Exception e) {
                 String message = "Server SEND ERROR : "+ e.getMessage();
               }
          return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        	//sendtask = null;
        }

    };
    sendtask.execute(null, null, null);
}


public static Map<String, String> createOpinionPost(Opinion opinion,User user) {
	Map<String, String> params = new HashMap<String, String>();
	
	params.put(POST_TYPE, POST_OPINION);
	params.put(V.FROM,user.getPhoneNumber());
	params.put(DB.OPINION_UID, opinion.getUID());
	params.put(DB.OPINION_BODY, opinion.getBody());
	params.put(DB.OPINION_TAG_CONTACTS , Tools.getMergedPhoneStringFromContacts(opinion.getTagContacts()));
	//params.put(DB.OPINION_REPLIES , Tools.getMergedStringFromStringList(opinion.getReplies()));
	params.put(DB.OPINION_TAG_CONTACTS_COUNT , ""+opinion.getTagContactsCount());
	params.put(DB.OPINION_OPTIONS , Tools.getMergedStringFromStringList(opinion.getOptions()));
	params.put(DB.OPINION_OPTIONS_COUNT , ""+opinion.getOptionCount());
	//params.put(DB.OPINION_STATICS , Tools.getMergedStringFromIntegerList(opinion.getStatics()));
	params.put(DB.OPINION_IS_YOU_ANONYMOUS , (opinion.isAnonymouslyAskedByYou())?""+Opinion.YES:""+Opinion.NO);
	params.put(DB.OPINION_RECEIVER_TYPE , ""+opinion.getReceiverType());
	params.put(DB.OPINION_TYPE , ""+opinion.getType());

	return params;
}


}
