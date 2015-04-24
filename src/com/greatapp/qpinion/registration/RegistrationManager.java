package com.greatapp.qpinion.registration;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.greatapp.qpinion.User;
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.sever.ServerUtilities;


public class RegistrationManager {

static String TAG  = "RegistrationManager";
    

	public  static boolean validateCredentials() {
    	return false;
	}

    public  static boolean isRegisteredIOnGCMServer(Context context) {
    	final String regId = GCMRegistrar.getRegistrationId(context);
        
        if (regId.equals("")) {
        	Log.d(TAG,"Not registered in GCM Server");
        	User u = new User(context);
        	u.updateInfo(User.U_GCM_IS_REGISTERED, ""+false);
        	return false;
        } else {
        	User u = new User(context);
        	u.updateInfo(User.U_GCM_IS_REGISTERED, ""+true);
        	u.updateInfo(User.U_REGID,regId);
            return true;
        }

	}
    
    public static boolean isDeviceRegisteredAtBothServer(Context context) {
    	return (isRegisteredIOnGCMServer(context) && isRegisteredOnQpinionServer(context));
    }
    
    public  static boolean isRegisteredOnQpinionServer(Context context) {
    	return GCMRegistrar.isRegisteredOnServer(context);
	}
    

    public static void registerDeviceOnGCM(Context context) {
    	Log.d("REGISTRATION","Registering on GCM");
        GCMRegistrar.checkDevice(context);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(context);
        GCMRegistrar.register(context,V.SENDER_ID);
        
    }

    public static void registerUserOnQpinionServer(final Context context) {
    	 
             // Try to register again, but not in the UI thread.
             // It's also necessary to cancel the thread onDestroy(),
             // hence the use of AsyncTask instead of a raw thread.
             
             AsyncTask<Void, Void, Boolean> mRegisterTask = new AsyncTask<Void, Void, Boolean>() {

                 @Override
                 protected Boolean doInBackground(Void... params) {
                 	Log.d(TAG,"Trying to register on Qpinion in Background......");
                     boolean registered = ServerUtilities.register(context);
                     // At this point all attempts to register with the app
                     // server failed, so we need to unregister the device
                     // from GCM - the app will try to register again when
                     // it is restarted. Note that GCM will send an
                     // unregistered callback upon completion, but
                     // GCMIntentService.onUnregistered() will ignore it.
                     if (!registered) {
                     	Log.e(TAG,"Couldn't register on AppServer, Unregistering from GCM Server..");
                         GCMRegistrar.unregister(context);
                     } 
                     return registered;
                 }

                 @Override
                 protected void onPostExecute(Boolean result) {
                     GCMRegistrar.setRegisteredOnServer(context, result);
                     String message = "";
                     if(result) {
                    	 message = "Successfully Registered on Qpinion !!" ;
                     } else {
                    	 message = "Sorry !! Couldn,t register on Qpinion" ;
                     }
                     displayMessage(context,message);
                     sendRegistrationResult(context,result);
                 }

             };
             mRegisterTask.execute(null, null, null);
    }
    
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(V.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(V.DISPLAY_MESSAGE, message);
        context.sendBroadcast(intent);
    }

    public static void sendRegistrationResult(Context context,boolean result) {
    	Intent intent = new Intent(V.REGISTRATION_MESSAGE_ACTION);
        intent.putExtra(V.REGISTRATION_RESULT, result);
        context.sendBroadcast(intent);
    }

}
