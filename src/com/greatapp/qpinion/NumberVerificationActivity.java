package com.greatapp.qpinion;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NumberVerificationActivity extends Activity {

	private String[] country_code ={"111","222","333","444","555"};
	private EditText et_phone_number;
	private TextView tv_country_code;
	private ProgressBar pb_verification;
	private SharedPreferences registrationPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_number_verification);
		
				Spinner sp_county_list = (Spinner)findViewById(R.id.sp_country_list);
				tv_country_code = (TextView)findViewById(R.id.tv_country_code);
				tv_status = (TextView)findViewById(R.id.tv_status);
				et_phone_number = (EditText)findViewById(R.id.et_phone_number);
				Button b_verify = (Button)findViewById(R.id.b_verify);
				pb_verification = (ProgressBar)findViewById(R.id.pb_number_verification);
				pb_verification.setVisibility(View.GONE);
				b_verify.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
		                onVerifyClicked();
					}
				});
				
				String[] country = {"INDIA","PAKISTAN","USA","GANGOH","SILKBOARD"};
				
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, country);
		        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

				sp_county_list.setAdapter(adapter);
				
				sp_county_list.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						onSpinnerItemSelected(pos);
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				registerBroadCastReciever();
		

	}
	

	protected void onVerifyClicked() {
		
        String number = et_phone_number.getText().toString();
        if(TextUtils.isEmpty(number)) {
        	Toast.makeText(this,"Enter Phone Number !",Toast.LENGTH_SHORT).show();
        } else if(number.length() > 10){
        	Toast.makeText(this,"Incorrect Phone Number !",Toast.LENGTH_SHORT).show();
        } else if(!isNumeric(number)) {
        	Toast.makeText(this,"Incorrect Phone Number !",Toast.LENGTH_SHORT).show();
        } else {
        	User u = new User(this);
        	u.updateInfo(User.U_PHONE,number);
        	pb_verification.setVisibility(View.VISIBLE);
        	tv_status.setText("Verifying phone number .... ");
        	//finaly we should show one dialog to proceed for below
        	Toast.makeText(this,"Verification SMS will be set to "+number+"\nCaution:Operator Charges Applies!!",Toast.LENGTH_LONG).show();
        	onVerifiedSuccessfully();//remove this hack
        	//Authenticator.authenticateNumber(number);
        }
	}


	private boolean isNumeric(String number) {
		try{
			  Double num = Double.parseDouble(number);
			  // is an integer!
			} catch (NumberFormatException e) {
			  return false;
			}
		return true;
	}


	protected void onSpinnerItemSelected(int pos) {
       String code = country_code[pos];		
       tv_country_code.setText(code);
	}


	private static BroadcastReceiver br;
	private EditText et_number;
	private TextView tv_status;



	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(br != null)
		this.unregisterReceiver(br);
	}


	private void registerBroadCastReciever() {

		IntentFilter filter = new IntentFilter();
		String action = "android.provider.Telephony.SMS_RECEIVED";
		filter.addAction(action);
		filter.setPriority(99999);
		br = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d("CHECK","SMS DETECTED");
				update("SMS DETECTED");
				//---get the SMS message passed in---//
				Bundle bundle = intent.getExtras();
				SmsMessage[] msgs = null;
				String str = "null";
				if (bundle != null) {
					str = "";
					// ---retrieve the SMS message received---
					Object[] pdus = (Object[]) bundle.get("pdus");
					msgs = new SmsMessage[pdus.length];
					
//					  for (int i = 0; i < msgs.length; i++) { 
//						  msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//						  str +=
//					  "SMS from " + msgs[i].getOriginatingAddress(); str +=
//					  " :"; str += msgs[i].getMessageBody().toString(); str +=
//					  "\n"; }
     				msgs[0] = SmsMessage.createFromPdu((byte[]) pdus[0]);
					String sms = msgs[0].getMessageBody().toString();
					String original_sender = msgs[0].getOriginatingAddress();
					boolean verified = Authenticator.VerifySms(original_sender,
							sms);

					if(verified) {
						update("Successfully Verified !! :-)");
						onVerifiedSuccessfully();
					} else {
						update("Verification Failed !!! :-(");
					}
				}
			}
		};
		Intent intent = registerReceiver(br, filter);
		if(intent != null) {
			update("Receiver Registered..");
		}

	}
	
	protected void onVerifiedSuccessfully() {
       pb_verification.setVisibility(View.GONE);
		Intent i = new Intent(this,RegisterActivity.class);
		i.putExtra("Message", "Register on Qpinion");
		startActivity(i);
		finish();
	}


	protected void update(String str) {
		tv_status.setText(str);
		Toast.makeText(getApplicationContext(), str,
				Toast.LENGTH_SHORT).show();
	}


}