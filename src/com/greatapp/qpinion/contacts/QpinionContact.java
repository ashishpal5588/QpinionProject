package com.greatapp.qpinion.contacts;



import android.content.ContentValues;

import com.greatapp.qpinion.constants.V;

public class QpinionContact {

	private String mName;
	private String mPhoneNumber;
	private String[] mGroupIds;
	private int mGroupCount;
	private int mId = 8888;
	private boolean mRegistered = false;

	public QpinionContact(String name, String phoneNumber) {
		setmName(name);
		setmPhoneNumber(phoneNumber);
		setmGroupIds(new String[] {});
		setmGroupCount(0);
		setRegistered(false);
	}

	public String getmPhoneNumber() {
		return mPhoneNumber;
	}

	public void setmPhoneNumber(String mPhoneNumber) {
		this.mPhoneNumber = mPhoneNumber;
	}

	public String getName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public int getmGroupCount() {
		return mGroupCount;
	}

	public void setmGroupCount(int mGroupCount) {
		this.mGroupCount = mGroupCount;
	}

	public String[] getmGroupIds() {
		return mGroupIds;
	}

	public void setmGroupIds(String[] mGroupIds) {
		this.mGroupIds = mGroupIds;
	}

	public ContentValues getContactValues() {
		ContentValues row = new ContentValues();
		row.put(V.DB_CONTACT_NAME , mName);
		row.put(V.DB_CONTACT_PHONE_NUMBER , mPhoneNumber);
		row.put(V.DB_CONTACT_GROUP_COUNTS , mGroupCount);
		String ids = "";
		if(mGroupIds != null) {
			
			int l = mGroupIds.length;
			if(l > 0) {
			for (int i = 0; i < l-1;i++) {
				ids = ids + mGroupIds[i]+V.SPLIT;
			}
			ids = ids + mGroupIds[l-1];
			} else {
				ids = "";
			}
		}
		
		row.put(V.DB_CONTACT_GROUPS_NAMES , ids);
		row.put(V.DB_REGISTERED, mRegistered?1:0);
		
		return row;
	}

	public int getId() {
      return mId ; 
	}

	public void setId(int id) {
       mId = id;
		
	}

	public void setRegistered(boolean b) {
      mRegistered = b;
	}
	
	public boolean getRegistered() {
	      return this.mRegistered;
	}
	
	@Override
	public boolean equals(Object object){
		super.equals(object);
		if (object == null) return false;
	    if (!(object instanceof QpinionContact))return false;
	    QpinionContact c = (QpinionContact)object;
	    
	    if(this.getmPhoneNumber().equals(c.getmPhoneNumber())) {
	    	return true;
	    } else 
	    	return false;
	}
}
