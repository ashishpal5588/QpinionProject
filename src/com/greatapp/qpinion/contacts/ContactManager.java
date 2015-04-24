package com.greatapp.qpinion.contacts;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.greatapp.qpinion.Tools;
import com.greatapp.qpinion.constants.V;

public class ContactManager {

	
	
	private Context mContext;
	private static ArrayList<QpinionContact> mContacts;
	private ArrayList<String> mNewlyAddedNumbers;

	public ContactManager(Context context) {
		mContext = context;
		mNewlyAddedNumbers = new ArrayList<String>();
		mContacts = loadContacts(context);

	}
	public ArrayList<QpinionContact> getLoadedContacts(Context context) {
		return mContacts;
	}
	public ArrayList<QpinionContact> getLoadedRegisteredContacts(Context context) {
		ArrayList<QpinionContact> contacts = new ArrayList<QpinionContact>();
		if(mContacts != null){
			for(QpinionContact contact : mContacts){
				if(contact.getRegistered()) {
					contacts.add(contact);
				}
			}
		}
		return contacts;
	}
	public void refreshContacts(Context context){
		retriveContactsFromDevice(context);
	}
	public void retriveContactsFromDevice(Context context) {
		Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
		
		if(phones != null) {

		while (phones.moveToNext())
		{
		     String Name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		     String Number=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		     Number = getPlainNumber(Number);
		     if(!ifAlreadyExistInqpinionDatabase(Number)) {
		        QpinionContact qpinionContact = new QpinionContact(Name, Number);
		         if( !TextUtils.isEmpty(Number) ) {
		             ContentValues values = qpinionContact.getContactValues();
		             context.getContentResolver().insert(V.DB_CONTACTS_TABLE_URI, values);
		             mContacts.add(qpinionContact);
		             if(phones.isLast()) {
		    	        break;
		             }
		        }
		   } else {
			   
		   }
		}
	  }
	}
	
	private String getPlainNumber(String number) {
        int length = number.length();
        String plain = "";
        for (int i = 0; i < length;i++){
        	char ch = number.charAt(i);
        	if(TextUtils.isDigitsOnly(""+ch)){
        		plain = plain+ch;
        	}
        }
		return plain;
	}

	private boolean ifAlreadyExistInqpinionDatabase(String Number) {
		
		for(int i = 0; i < mContacts.size();i++) {
			if(Number.equals((mContacts.get(i).getmPhoneNumber()))){
				return true;
			}
		}
		for(int j = 0;j< mNewlyAddedNumbers.size();j++) {
			if(Number.equals((mNewlyAddedNumbers.get(j)))){
				return true;
			}
		}
		mNewlyAddedNumbers.add(Number);
		return false;
	}

	public ArrayList<QpinionContact> loadContacts(Context context) {
		ArrayList<QpinionContact> contacts = new ArrayList<QpinionContact>();
        Cursor c = context.getContentResolver().query(V.DB_CONTACTS_TABLE_URI,
        		           V.DB_CONTACT_TABLE_PROJECTION, null, null, null);
        if(c != null && c.getCount() > 0) {
        	c.moveToFirst();
        	
        	while(!c.isAfterLast()) {
        		
        		QpinionContact qpinionContact = createContactFromCursor(context,c);
        		if(qpinionContact.getRegistered()){
        			contacts.add(0, qpinionContact);
        		} else {
            		contacts.add(qpinionContact);
        		}
                c.moveToNext();
        	}
        	c.close();
        }
        return contacts;
		
	}
	
	public QpinionContact createContactFromCursor(Context context, Cursor c) {
		String name = c.getString(c.getColumnIndex(V.DB_CONTACT_NAME));
		//Log.d("GCM","contact name  : "+name );
		String phoneNumber = c.getString(c.getColumnIndex(V.DB_CONTACT_PHONE_NUMBER));
		int groupCount = c.getInt(c.getColumnIndex(V.DB_CONTACT_GROUP_COUNTS));
		int registered = c.getInt(c.getColumnIndex(V.DB_REGISTERED));
		int id = c.getInt(c.getColumnIndex(V.DB_CONTACT_ID));
		String groupIds = c.getString(c.getColumnIndex(V.DB_CONTACT_GROUPS_NAMES));
		QpinionContact qpinionContact = new QpinionContact(name, phoneNumber);
		qpinionContact.setmGroupCount(groupCount);
		qpinionContact.setId(id);
		qpinionContact.setRegistered((registered == 1)?true:false);
		String[] gids = new String[]{};
		gids = Tools.getSplittedArray(groupIds,V.SPLIT);
		qpinionContact.setmGroupIds(gids);
		return qpinionContact;
	}
	private void refresh() {
		Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
		Cursor qpinionContacts = mContext.getContentResolver().query(V.DB_CONTACTS_TABLE_URI, V.DB_CONTACT_TABLE_PROJECTION, null, null, null);
	}

	public ArrayList<QpinionContact> getContactListByNames(String[] ids) {
        ArrayList<QpinionContact> list = new ArrayList<QpinionContact>();
        for (int i =0; i<ids.length;i++) {
        	QpinionContact contact = getContactByName(ids[i]);
        	if(contact != null)
        	list.add(contact);
        }
		return list;
	}

	private QpinionContact getContactByName(String string) {
            for(int i = 0; i < mContacts.size();i++) {
            	QpinionContact contact = mContacts.get(i);
            	if(contact.getName().equals(string)) {
            		return contact;
            	}
            }
            return null;
	}

	public ArrayList<QpinionContact> getContactListByPhones(String[] phones) {
		ArrayList<QpinionContact> list = new ArrayList<QpinionContact>();
        for (int i =0; i<phones.length;i++) {
        	QpinionContact contact = getContactByPhone(phones[i]);
        	if(contact != null)
        	list.add(contact);
        }
		return list;
	}

	public QpinionContact getContactByPhone(String string) {
		if(mContacts != null)
        for(int i = 0; i < mContacts.size();i++) {
        	QpinionContact contact = mContacts.get(i);
        	if(contact.getmPhoneNumber().equals(string)) {
        		return contact;
        	}
        }
        return null;
}

	public void updateQpinionContacts(Context context,
			ArrayList<QpinionContact> qpinionList) {
		for(QpinionContact contact : qpinionList) {
			String where = V.DB_CONTACT_ID + "=" + contact.getId();
			String[] selectionArgs = null;
			contact.setRegistered(true);
			ContentValues values = contact.getContactValues();
			mContext.getContentResolver().update(V.DB_CONTACTS_TABLE_URI, values, where, selectionArgs);
 			
		}
		
	}

	public String getContactNameFromNumber(String ownerNumber) {
		QpinionContact contact = getContactByPhone(ownerNumber);
		if(contact != null){
			return contact.getName();
		}
		return null;
	}


}
