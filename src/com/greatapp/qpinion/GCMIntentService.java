/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greatapp.qpinion;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.data.DataManager;
import com.greatapp.qpinion.registration.RegistrationManager;
import com.greatapp.qpinion.sever.ServerUtilities;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(V.SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        RegistrationManager.displayMessage(context,"Device Registered on GCM Successfully !!");
        User u = new User(context);
        u.updateInfo(User.U_GCM_IS_REGISTERED, ""+true);
        u.updateInfo(User.U_REGID, registrationId);
        RegistrationManager.registerUserOnQpinionServer(context);

    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        RegistrationManager.displayMessage(context,"Device Unregistered on GCM Successfully !!");
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            Log.i(TAG, "Ignoring unregister callback");
        }
    }
	private void notifyBroadCast(Context context, String message) {
			    Intent intent = new Intent(V.NOTIFY_BROADCAST_ACTION);
			    intent.putExtra("msg",message);
			    context.sendBroadcast(intent);
			    Log.d(TAG,"Broadcast sent for Notify");
	}
	
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        // check to see if it is a message
        if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
           String type = intent.getExtras().getString(V.MESSAGE_TYPE);
           
           if(type.equals(V.MESSAGE_APPRAISE)) {
        	//String owner = intent.getExtras().getString(DB.APPRAISE_OWNER);
    		DataManager dm  = new DataManager(context);
    		String owner_name = dm.handleAppraiseMSG(context,intent);
    		generateNotification(context,owner_name +" asked you a question!",1);
    		
           } else if(type.equals(V.MESSAGE_OPINION)) {
             Log.d(TAG,"OPINION RECEIVED");

           // String comingFrom = intent.getExtras().getString(V.FROM);
       		DataManager dm  = new DataManager(context);
       		String replyer_name = dm.handleOpinionMSG(context,intent);
       		if(replyer_name != null) {
       		   notifyBroadCast(context,replyer_name+" has replied");
    		   generateNotification(context,replyer_name +" has replied your Question!",1);
       		}
           }

        }

    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = "GCM deleted : "+ total + " messages..!!";
        RegistrationManager.displayMessage(context, message);
        // notifies user
        generateNotification(context, message,1);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        RegistrationManager.displayMessage(context, "GCM ERROR : "+errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        RegistrationManager.displayMessage(context, "Recoverable Error : "+errorId);
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message, int openingClass) {
        int icon = R.drawable.qpinion_icon;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = null;
        if(openingClass == 1){
        	notificationIntent  = new Intent(context, QpinionMainActivity.class);
        } else if(openingClass == 2) {
        	notificationIntent  = new Intent(context, QpinionMainActivity.class);
        }

        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.sound = Uri.parse("android.resource://" + "com.greatapp.qpinion" + "/" + R.raw.notifysound);
        
        notificationManager.notify(0, notification);
    }

}
