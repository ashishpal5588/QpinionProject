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
package com.greatapp.qpinion.sever;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.greatapp.qpinion.User;
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.registration.RegistrationManager;

/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtilities {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
	private static Context mContext;
    private static String TAG = "ServerUtills";
    /**
     * Register this account/device pair within the server.
     *
     * @return whether the registration succeeded or not.
     */
    public static boolean register(Context context) {
        String serverUrl = V.SERVER_URL + "/register";
        Map<String, String> params = new HashMap<String, String>();
        User user = new User(context);
        params.put(V.PHONE_NUMBER, user.getPhoneNumber());
        params.put(V.USER_NAME, user.getName());
        params.put(V.REG_ID, user.getRegId());
        params.put(V.EMAIL_ID,user.getEmail());
        params.put(V.GENDER,""+user.getGender());
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register it in the
        // demo server. As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Attempt #" + i + " to register");
            try {
                RegistrationManager.displayMessage(context, "registering on opinium : "+ i+"out of "+ MAX_ATTEMPTS);
                
                boolean success = postForRegistration(serverUrl, params);
                if(success) {
                GCMRegistrar.setRegisteredOnServer(context, true);
                String message = "Registered on opinium";
                RegistrationManager.displayMessage(context, message);
                return true;
                }
            } catch (IOException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(TAG, "Failed to register on attempt " + i, e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return false;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = "MAX ATTEMPS : "+MAX_ATTEMPTS;
        RegistrationManager.displayMessage(context, message);
        return false;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final Context context, final String regId) {
    	mContext = context;
        Log.i(TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = V.SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            postForRegistration(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = "Unregistered On opinium";
            RegistrationManager.displayMessage(context, message);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = "Server Unregister ERROR : "+ e.getMessage();
            RegistrationManager.displayMessage(context, message);
        }
    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
    private static boolean postForRegistration(String endpoint, Map<String, String> params)
            throws IOException {
    	boolean success = false;
    	Log.d("GCM","Posting to :-> "+endpoint);
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            Log.d("GCM","Written to output stream ");
            // handle the response
            int status = conn.getResponseCode();
            String response_msg = conn.getResponseMessage();
            Log.d("GCM","response_msg : "+response_msg);
            if(response_msg.equals("OK")) {
            	success = true;
            }
            BufferedReader in = new BufferedReader(
    		        new InputStreamReader(conn.getInputStream()));
    		String inputLine;
    		StringBuffer response = new StringBuffer();
    		Log.d("GCM","Collect response");
    		while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    		}
    		in.close();
    		Log.d("GCM","response_msg inputstream : "+response);
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
                
            }
            return success;
        }
      }

	public static void sendToAllOnQpinion(final Context context, String msg) {
		
        
        final String serverUrl = V.SERVER_URL + "/send";
        final Map<String, String> serverData = new HashMap<String, String>();
        User user = new User(context);
        serverData.put(V.FROM, user.getPhoneNumber());
        serverData.put(V.MSG, msg);
        
        final AsyncTask<Void, Void, Void> sendtask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
            	 try {
                     postForRegistration(serverUrl, serverData);
                              
                    } catch (IOException e) {
                     // At this point the device is unregistered from GCM, but still
                     // registered in the server.
                     // We could try to unregister again, but it is not necessary:
                     // if the server tries to send a message to the device, it will get
                     // a "NotRegistered" error message and should unregister the device.
                     String message = "Server SEND ERROR : "+ e.getMessage();
                     RegistrationManager.displayMessage(context, message);
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

	public static String setUpContacts(Context context, String contactsString) {
        final String serverUrl = V.SERVER_URL + "/setupcontacts";
        final Map<String, String> serverData = new HashMap<String, String>();
        User user = new User(context);
        serverData.put(V.FROM, user.getPhoneNumber());
        serverData.put(V.MSG, contactsString);
        String q_contacts = "";
        try {
            q_contacts = postForSetupContacts(serverUrl, serverData);
        } catch (IOException e) {
            String message = "Server SEND ERROR : "+ e.getMessage();
            Log.e(TAG,message);
        } finally {
        	return q_contacts;
        }
	}

    public static String postForSetupContacts(String endpoint, Map<String, String> params)
            throws IOException {
    	boolean success = false;
    	Log.d(TAG,"postForSetupContacts to :-> "+endpoint);
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        String result = "";
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            Log.d(TAG,"Written to output stream ");
            // handle the response
            int status = conn.getResponseCode();
            String response_msg = conn.getResponseMessage();
            Log.d(TAG,"response_msg : "+response_msg);
            if(response_msg.equals("OK")) {
            	success = true;
            	getResponse(conn);
            }

            BufferedReader in = new BufferedReader(
    		        new InputStreamReader(conn.getInputStream()));
    		String inputLine;
    		StringBuffer response = new StringBuffer();
    		Log.d(TAG,"Collect response");
    		while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    		}
    		in.close();
    		result = response.toString();
    		Log.d(TAG,"response from server : "+result);
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
                
            }
            return result;
        }
      }
    
    private static String getResponse(HttpURLConnection httpURLConnection) {
/*    	Map<String, List<String>> map = httpURLConnection.getHeaderFields();
    	String resp = "";
    	for (Map.Entry<String, List<String>> entry : map.entrySet())
    	{
    	    if (entry.getKey() == null) 
    	        continue;
    	    resp = resp+ entry.getKey() +": ";

    	    List<String> headerValues = entry.getValue();
    	    Iterator<String> it = headerValues.iterator();
    	    if (it.hasNext()) {
    	    	resp = resp+it.next();

    	        while (it.hasNext()) {
    	            resp = resp+ ", "+it.next();
    	        }
    	    }

    	    resp = resp + "\n";
    	}
    	Log.d(TAG,"RESPONSE FROM SERVER : "+resp);
    	return resp;
    	*/
    	return "THIS METHOD NOT USED";
    }

    
    public static boolean postToQpinion(String endpoint, Map<String, String> params)
            throws Exception {
    	boolean success = false;
    	Log.d("GCM","postToQpinion to :-> "+endpoint);
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting [" + body + "] to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        boolean isPosted = false;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            Log.d(TAG,"Writing to output stream");
            // post the request
            Log.d(TAG,"is output allowed : "+conn.getDoOutput());
            OutputStream out = conn.getOutputStream();
            Log.d("GCM","ABOUT TO WRITE");
            out.write(bytes);
            out.close();
            Log.d("GCM","Written to output stream ");
            isPosted = true;
            // handle the response
            int status = conn.getResponseCode();
            String response_msg = conn.getResponseMessage();
            Log.d("GCM","response_msg : "+response_msg);
            if(response_msg.equals("OK")) {
            	success = true;
            }
            BufferedReader in = new BufferedReader(
    		        new InputStreamReader(conn.getInputStream()));
    		String inputLine;
    		StringBuffer response = new StringBuffer();
    		Log.d("GCM","Collect response");
    		while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    		}
    		in.close();
    		Log.d("GCM","response_msg inputstream : "+response);
            if (status != 200) {
                Log.e(TAG,"Post failed!!");
              throw new IOException("Post failed with error code " + status);
            }
        } catch (UnknownServiceException e){
        	Log.d(TAG,"Exception : "+e.getMessage().toString());
        }
        finally {
            if (conn != null) {
                conn.disconnect();
                if(isPosted) {
                	Log.d(TAG,"Posted to Server");
                } else {
                	Log.d(TAG,"Not Posted to Server");
                }
                
            }
            return success;
        }
      }
    
}
