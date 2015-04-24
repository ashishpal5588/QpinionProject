package com.greatapp.qpinion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.greatapp.qpinion.contacts.QpinionContact;
import com.greatapp.qpinion.data.Appraise;
import com.greatapp.qpinion.data.DataManager;
import com.greatapp.qpinion.data.Opinion;
import com.greatapp.qpinion.group.GroupManager;
import com.greatapp.qpinion.group.QpinionGroup;

public class AskActivity extends Activity {

	private Spinner sp_ask_type;
	private Spinner sp_ask_group;
	private LinearLayout optionView;
	private EditText questionContentView;
	private CheckBox checkBox;
	private Button askButtonView;
	private EditText et_a;
	private EditText et_b;
	private EditText et_c;
	private EditText et_d;
	
	private static int selectedGroupIndex = 44;
	private static int opinionType = 99;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        sp_ask_type = (Spinner)findViewById(R.id.sp_ask_type);
        sp_ask_group = (Spinner)findViewById(R.id.sp_ask_group);
        optionView = (LinearLayout)findViewById(R.id.linearLayout_options_view);
        questionContentView = (EditText)findViewById(R.id.et_question_body);
        checkBox = (CheckBox)findViewById(R.id.cb_ask_anonymously);
        askButtonView = (Button)findViewById(R.id.b_ask);
        
        et_a = (EditText)findViewById(R.id.editText_option_a);
        et_b = (EditText)findViewById(R.id.editText_option_b);
        et_c = (EditText)findViewById(R.id.editText_option_c);
        et_d = (EditText)findViewById(R.id.editText_option_d);
        
        ArrayList<String> questionTypeList = new ArrayList<String>();
        //questionTypeList.add("Yes/No"); //after changing here change the switch block case value
        questionTypeList.add("Text");
        questionTypeList.add("Options");
       // questionTypeList.add("Rating");
        

        
        ArrayAdapter<String> adp= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,questionTypeList);
        sp_ask_type.setAdapter(adp);
        sp_ask_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int pos, long arg3) {
				switch (pos) {	
				 case 0 : 
					 opinionType = Appraise.TYPE_COMMENTS;
					 optionView.setVisibility(View.GONE);
					 break;
				 case 1:
					 optionView.setVisibility(View.VISIBLE);
					 opinionType = Appraise.TYPE_OPTIONS;
					 break;
				default :
					 optionView.setVisibility(View.GONE);
				}
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}

		});

        GroupManager gm = new GroupManager(this);
        ArrayList<QpinionGroup> qpinionGroups = gm.getLoadedGroups(this);
        ArrayList<String> grouplist = Tools.getGroupNamesFromList(qpinionGroups);
        int index = 0;
        for(QpinionGroup qpinionGroup : qpinionGroups){
        	int count = qpinionGroup.getContactCount();
        	grouplist.set(index, grouplist.get(index)+" ("+count+" members)");
        	index++;
        }
        ArrayAdapter<String> adp2= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1
        		                   ,grouplist);
        sp_ask_group.setAdapter(adp2);
        sp_ask_group.setOnItemSelectedListener(new OnItemSelectedListener() {


			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int pos, long arg3) {
                selectedGroupIndex = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

		});

        askButtonView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                onClickedAsk();
			}
		});

		
	}

protected void onClickedAsk() {
   Opinion opinion = createOpinion();
   if(opinion == null) return;
      TransactionManager.AskOpinion(opinion,new User(this));
      DataManager.insertOpinion(this,opinion);
      Intent i = new Intent(this,QpinionMainActivity.class);
      this.startActivity(i);
      finish();
}


protected Opinion createOpinion() {
     String uid =  DataManager.generateUniqueId();
     String body = questionContentView.getText().toString();
     if(TextUtils.isEmpty(body)) {
          Toast.makeText(this,"Enter Question", Toast.LENGTH_SHORT).show();
          return null;
      }
    GroupManager gm = new GroupManager(this);
    QpinionGroup group = gm.getLoadedGroups(this).get(selectedGroupIndex);
    ArrayList<QpinionContact> tag_contacts = group.getGroupContacts();
    int tag_contact_count = group.getContactCount();
    ArrayList<String> replies = Opinion.createNewReplies(tag_contact_count);
    String Oa = et_a.getText().toString();
    String Ob = et_b.getText().toString();
    String Oc = et_c.getText().toString();
    String Od = et_d.getText().toString();
    ArrayList<String> options = new ArrayList<String>();
    if(!TextUtils.isEmpty(Oa))options.add(Oa);
    if(!TextUtils.isEmpty(Ob))options.add(Ob);
    if(!TextUtils.isEmpty(Oc))options.add(Oc);
    if(!TextUtils.isEmpty(Od))options.add(Od);
    if(optionView.isShown() && options.size() < 2) {
       Toast.makeText(this, "Enter atleast two options", Toast.LENGTH_SHORT).show();
       return null; 
    }
   int option_count = options.size();
   ArrayList<Integer> statics =  Opinion.createNewStatics(option_count);

   boolean isYouAnonymous = checkBox.isChecked();
   int receiver_type = Opinion.RECEIVER_SELECTED;
   Opinion opinion = new Opinion();
   opinion.setUID(uid);
   opinion.setBody(body);
   opinion.setTagContacts(tag_contacts);
   opinion.setTagContactsCount(tag_contact_count);
   opinion.setReplies(replies);
   opinion.setOptions(options);
   opinion.setOptionCount(option_count);
   opinion.setStatics(statics);
   opinion.setAnonymouslyAskedByYou(isYouAnonymous);
   opinion.setReceiverType(receiver_type);
   opinion.setType(opinionType);
   return opinion;
 }

}
