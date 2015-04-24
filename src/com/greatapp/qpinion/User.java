package com.greatapp.qpinion;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.greatapp.qpinion.constants.V;

public class User {
	
	private String regId;
	private String name;
	private String phone;
	private String email;
	private int gender;
	private String sid;
	private boolean gcm_is_registered;
	private boolean qpinion_is_registered;
	
	public static final int U_REGID = 10;
	public static final int U_NAME = 11;
	public static final int U_PHONE = 12;
	public static final int U_EMAIL = 13;
	public static final int U_GENDER = 14;
	public static final int U_SID = 15;
	public static final int U_GCM_IS_REGISTERED = 16;
	public static final int U_QPINION_IS_REGISTERED = 17;
	private static final String TAG = "USER";
	
	public static final int MALE = 1;
	public static final int FEMALE = 2;
	public static final String PREFS_REG_ID = "RegId";
	public static final String PREFS_USER_NAME = "UserName";
	public static final String PREFS_PHONE_NUMBER = "phoneNumber";
	public static final String PREFS_EMAIL_ID = "emailId";
	public static final String PREFS_IS_REGISTERED_ON_GCM = "isRegisteredOnGCM";
	public static final String PREFS_IS_REGISTERED_ON_QPINION = "isRegisteredOnQpinion";
	public static final String PREFS_GENDER = "gender";
	public static final String PREFS_SID = "sid";
	
	private SharedPreferences userPrefs;
	private Context mContext;
	
public User(Context context) {
	mContext = context;	
	userPrefs = mContext.getSharedPreferences(V.USER_PREFERENCES, mContext.MODE_PRIVATE);
	regId = userPrefs.getString(PREFS_REG_ID, "NA");
	name = userPrefs.getString(PREFS_USER_NAME, "Unnammed");
	phone = userPrefs.getString(PREFS_PHONE_NUMBER, "0101010101");
	email = userPrefs.getString(PREFS_EMAIL_ID, "abcd@qpinion.com");
	gender = userPrefs.getInt(PREFS_GENDER, MALE);
	sid = userPrefs.getString(PREFS_SID, "000000");
	gcm_is_registered = userPrefs.getBoolean(PREFS_IS_REGISTERED_ON_GCM, false);
	qpinion_is_registered = userPrefs.getBoolean(PREFS_IS_REGISTERED_ON_QPINION, false);
}

public User(Context _c, String _name, String _phone, String _email, int _gender) {
	mContext = _c;
	userPrefs = mContext.getSharedPreferences(V.USER_PREFERENCES, mContext.MODE_PRIVATE);
    SharedPreferences.Editor prefEditor = userPrefs.edit();
    if(_name != null)prefEditor.putString(PREFS_USER_NAME, _name);this.name = _name;
    if(_phone != null)prefEditor.putString(PREFS_PHONE_NUMBER, _phone);this.phone = _phone;
    if(_email != null)prefEditor.putString(PREFS_EMAIL_ID, _email);this.email = _email;
    if(_gender != MALE || _gender != FEMALE)
    	prefEditor.putInt(PREFS_GENDER, gender);this.gender = _gender;
    prefEditor.commit();	
}

public void updateInfo(int flag, String msg) {
	userPrefs = mContext.getSharedPreferences(V.USER_PREFERENCES, mContext.MODE_PRIVATE);
    SharedPreferences.Editor prefEditor = userPrefs.edit();
    switch(flag) {
    case U_REGID:
    	prefEditor.putString(PREFS_REG_ID, msg);this.regId = msg;
    	break;
    case U_NAME:
    	prefEditor.putString(PREFS_USER_NAME, msg);this.name = msg;
    	break;
    case U_PHONE:
    	prefEditor.putString(PREFS_PHONE_NUMBER, msg);this.phone = msg;
    	break;
    case U_EMAIL:
    	prefEditor.putString(PREFS_EMAIL_ID, msg);this.email = msg;
    	break;
    case U_GENDER:
    	prefEditor.putInt(PREFS_GENDER, Integer.parseInt(msg));this.gender = Integer.parseInt(msg);
    	break;
    case U_SID:
    	prefEditor.putString(PREFS_SID, msg);this.sid = msg;
    	break;
    case U_GCM_IS_REGISTERED:
    	prefEditor.putBoolean(PREFS_IS_REGISTERED_ON_GCM, Boolean.parseBoolean(msg));
    	this.gcm_is_registered = Boolean.parseBoolean(msg);
    	break;
    case U_QPINION_IS_REGISTERED:
    	prefEditor.putBoolean(PREFS_IS_REGISTERED_ON_QPINION, Boolean.parseBoolean(msg));
    	this.qpinion_is_registered = Boolean.parseBoolean(msg);
    default:
    	Log.e(TAG,"illegal modifier FLAG");
    }
    prefEditor.commit();
    Log.i(TAG,"User Preferences Updated!!");
}

public String getRegId() {
	return regId;
}


public String getName() {
	return name;
}



public String getPhoneNumber() {
	return phone;
}


public String getEmail() {
	return email;
}



public int getGender() {
	return gender;
}


public String getSid() {
	return sid;
}



public boolean isRegisteredOnGCM() {
	return gcm_is_registered;
}



public boolean isRegisteredOnQpinion() {
	return qpinion_is_registered;
}



}
