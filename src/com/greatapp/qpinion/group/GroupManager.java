package com.greatapp.qpinion.group;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.greatapp.qpinion.Tools;
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.contacts.ContactManager;
import com.greatapp.qpinion.contacts.QpinionContact;

public class GroupManager {
	

	private Context mContext;
	private ArrayList<String> mNewlyAddedNumbers;
	public GroupManager(Context context) {
		mContext = context;
	//	mGroups = getLoadedGroups(context);
	}
	
	private int getGroupCount(Context context) {
		return getLoadedGroups(context).size();
	}
	
	

	public ArrayList<QpinionGroup> getLoadedGroups(Context context) {
		ArrayList<QpinionGroup> groups = new ArrayList<QpinionGroup>();
        Cursor c = mContext.getContentResolver().query(V.DB_GROUPS_TABLE_URI,
        		           V.DB_GROUPS_TABLE_PROJECTION, null, null, null);
        if(c != null && c.getCount() > 0) {
        	Log.d("GROUP","cursor Count : "+c.getCount());
        	c.moveToFirst();
        	int count = 0;
        	while(!c.isAfterLast()) {
        		
        		QpinionGroup qpinionGroup = createGroupFromCursor(context,c);
        		count++;
        		Log.d("GROUP","added in list : "+count);
        		groups.add(qpinionGroup);
        		//if(!c.isLast())
        		c.moveToNext();
        	}
        	//while(!c.isLast());
        	c.close();
        }
        return groups;
		
	}
	
	public QpinionGroup createGroupFromCursor(Context context, Cursor c) {
		if(c == null) return null;
		String name = c.getString(c.getColumnIndex(V.DB_GROUP_NAME));
		Log.d("GROUP","Creating Group for name  : "+name );
		//String phoneNumber = c.getString(c.getColumnIndex(Values.DB_CONTACT_PHONE_NUMBER));
		int contactCount = c.getInt(c.getColumnIndex(V.DB_GROUP_CONTACTS_COUNTS));
		int groupId = c.getInt(c.getColumnIndex(V.DB_GROUP_ID));
		String contactNames = c.getString(c.getColumnIndex(V.DB_GROUP_CONTACTS_NAMES));
		QpinionGroup qpinionGroup = new QpinionGroup(name);
		qpinionGroup.setContactCount(contactCount);
		String[] names = new String[]{};
		names = Tools.getSplittedArray(contactNames,V.SPLIT);
		//qpinionGroup.setContactNames(names);
		qpinionGroup.setId(groupId);
		ArrayList<QpinionContact> contacts = getContactListByNames(names);
		qpinionGroup.setGroupContacts(contacts);
		return qpinionGroup;
	}
	private ArrayList<QpinionContact> getContactListByNames(String[] cids) {
		ContactManager cm = new ContactManager(mContext);
		return cm.getContactListByNames(cids);

	}

	private void refresh() {
		Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
		Cursor qpinionContacts = mContext.getContentResolver().query(V.DB_CONTACTS_TABLE_URI, V.DB_CONTACT_TABLE_PROJECTION, null, null, null);
	}

	public void createNewGroupInDB(String name) {
		QpinionGroup qpinionGroup = new QpinionGroup(name);
        if( !TextUtils.isEmpty(name) ) {
            ContentValues values = qpinionGroup.getGroupValues();
            mContext.getContentResolver().insert(V.DB_GROUPS_TABLE_URI, values);
         }
	}
	
	public void addGroupContact(QpinionContact contact,QpinionGroup targetGroup) {
		String where = V.DB_GROUP_ID + "=" + targetGroup.getId();
		String[] selectionArgs = null;
		targetGroup.getGroupContacts().add(contact);
		targetGroup.setContactCount(targetGroup.getContactCount()+1);
		ContentValues values = targetGroup.getGroupValues();
		mContext.getContentResolver().update(V.DB_GROUPS_TABLE_URI, values, where, selectionArgs);
	}


	public QpinionGroup getGroupByName(Context context,String groupName) {
		ArrayList<QpinionGroup> groups = getLoadedGroups(context);
      for(int i = 0; i< groups.size();i++) {
    	  if(groups.get(i).getName().equals(groupName)) {
    		  return groups.get(i);
    	  }
      }
		return null;
	}

	public void deleteGroup(QpinionGroup group) {
		String where = V.DB_GROUP_ID + "=" + group.getId();
		mContext.getContentResolver().delete(V.DB_GROUPS_TABLE_URI, where, null);
	}

	public void deleteGroupContactFromGroup(QpinionGroup group,QpinionContact contact) {
       String where = V.DB_GROUP_ID + "=" + group.getId();
	   ArrayList<QpinionContact> contacts = group.getGroupContacts();
	   contacts.remove(contact);
	   group.setGroupContacts(contacts);
	   mContext.getContentResolver().update(V.DB_GROUPS_TABLE_URI, group.getGroupValues(), where, null);
	}

	public void createDefaultGroup(Context context) {
		QpinionGroup defaultGroup = new QpinionGroup(V.DEFAULT_GROUP_NAME);
		ContactManager cm = new ContactManager(context);
		defaultGroup.setGroupContacts(cm.getLoadedRegisteredContacts(context));
        ContentValues values = defaultGroup.getGroupValues();
        mContext.getContentResolver().insert(V.DB_GROUPS_TABLE_URI, values);
	}

	public void updateDefaultGroup(Context context) {
		QpinionGroup defaultGroup = getGroupByName(context, V.DEFAULT_GROUP_NAME);
		String where = V.DB_GROUP_ID + "=" + defaultGroup.getId();
		String[] selectionArgs = null;
		ContactManager cm = new ContactManager(context);
		defaultGroup.setGroupContacts(cm.getLoadedRegisteredContacts(context));
		ContentValues values = defaultGroup.getGroupValues();
		mContext.getContentResolver().update(V.DB_GROUPS_TABLE_URI, values, where, selectionArgs);
	}




}
