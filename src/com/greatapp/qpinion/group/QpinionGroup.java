package com.greatapp.qpinion.group;



import java.util.ArrayList;

import android.content.ContentValues;

import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.contacts.QpinionContact;

public class QpinionGroup {

	private String mName;
	private String[] mContactNames;
	private ArrayList<QpinionContact> mQpinionGroupContacts;
	private int mContactCount;
	private int mId = 9999;

	public QpinionGroup(String name) {
		setGroupContacts(new ArrayList<QpinionContact>());
		setName(name);
		setContactCount(0);
	}



	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public int getContactCount() {
		return mContactCount;
	}

	public void setContactCount(int contactCount) {
		this.mContactCount = contactCount;
	}


	public ContentValues getGroupValues() {
		ContentValues row = new ContentValues();
		row.put(V.DB_GROUP_NAME , mName);
		row.put(V.DB_GROUP_CONTACTS_COUNTS , mContactCount);
		String names = "";
		if( mQpinionGroupContacts != null) {
			for (int i = 0; i < mQpinionGroupContacts.size();i++) {
				names = names + mQpinionGroupContacts.get(i).getName()+V.SPLIT;
			}
		}
		
		row.put(V.DB_GROUP_CONTACTS_NAMES , names);
		
		return row;
	}


	public ArrayList<QpinionContact> getGroupContacts() {
		return mQpinionGroupContacts;
	}
	
	public void setGroupContacts(ArrayList<QpinionContact> qpinionContacts) {

		mQpinionGroupContacts = qpinionContacts;
		setContactCount(qpinionContacts.size());
	}

public int getId() {
	return mId;
}

	public void setId(int groupId) {
      mId = groupId;		
	}



	public boolean hasContact(QpinionContact contact) {
       for(int i = 0;i<mQpinionGroupContacts.size();i++) {
    	   int id = mQpinionGroupContacts.get(i).getId();
    	   if(id == contact.getId()) {
    		   return true;
    	   }
       }
       return false;

	}


}
