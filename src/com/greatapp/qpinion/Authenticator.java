package com.greatapp.qpinion;

import java.util.Random;

import android.telephony.SmsManager;
import android.util.Log;

public class Authenticator {
	private static String AUTHENTICATION_KEY = "AppAndroid";

	public static void authenticateNumber(String number) {
		AUTHENTICATION_KEY = "Qpinion";
		SmsManager sm = SmsManager.getDefault();
		AUTHENTICATION_KEY = AUTHENTICATION_KEY + randInt(1000, 4999);
		sm.sendTextMessage(number, null, getSMScontent(AUTHENTICATION_KEY), null, null);
	}
	
	private static String getSMScontent(String key) {
		String sms = "Hi,\nYour Qpinion verification code is "+key+".\n\nThanks,\nQpinion";
		return sms;
	}

	public interface AuthenticatorCallBack {
		void onSuccess();

		void onFailed();
	}

	public static boolean VerifySms(String original_sender, String sms) {
		Log.d("CHECK","KEY "+AUTHENTICATION_KEY);
		Log.d("CHECK","SMS "+sms);
		if (sms.contains(AUTHENTICATION_KEY)) {
			return true;
		} else {
			return false;
		}
	}

	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

}
