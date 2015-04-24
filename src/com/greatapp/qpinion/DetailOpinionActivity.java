package com.greatapp.qpinion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greatapp.qpinion.contacts.QpinionContact;
import com.greatapp.qpinion.data.Appraise;
import com.greatapp.qpinion.data.DataManager;
import com.greatapp.qpinion.data.Opinion;

public class DetailOpinionActivity extends Activity {

	private static final String TAG = "QPINION_DETAILS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_opinion);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		int id  = i.getIntExtra("id", 0);
		DataManager dm = new DataManager(this);
		Opinion opinion = dm.getOpinionFromId(this,id);
	     LinearLayout ll_statics = (LinearLayout)findViewById(R.id.ll_statics);
	     LinearLayout ll_answers = (LinearLayout)findViewById(R.id.ll_answers);
	     TextView tv_body = (TextView)findViewById(R.id.tv_body);
	     tv_body.setText(opinion.getBody());
         setAnswerView(ll_answers,opinion);
         setStaticsView(ll_statics,opinion);
	}
	
	private void setAnswerView(LinearLayout ll_answers, Opinion opinion) {

	        int count = 0;
	        ArrayList<String> replies = opinion.getReplies();
	        ArrayList<QpinionContact> contacts = opinion.getTagContacts();
	        for(String reply : replies){

	        	if(!reply.equals(Opinion.NOT_REPLIED)){
	        		AnswerItemView view = new AnswerItemView(this, null);
				    TextView tv_name = (TextView)view.findViewById(R.id.tv_name);
				    TextView tv_answer = (TextView)view.findViewById(R.id.tv_answer_body);
				    tv_name.setText(contacts.get(count).getName());
				    Log.d(TAG,"OPINION TYPE : "+opinion.getType());
				    if(opinion.getType() == Appraise.TYPE_OPTIONS) {
                       if(reply.equals(Appraise.OPTION_1))tv_answer.setText(opinion.getOptions().get(0));
                       else if(reply.equals(Appraise.OPTION_2))tv_answer.setText(opinion.getOptions().get(1));
                       else if(reply.equals(Appraise.OPTION_3))tv_answer.setText(opinion.getOptions().get(2));
                       else if(reply.equals(Appraise.OPTION_4))tv_answer.setText(opinion.getOptions().get(3));
                       else tv_answer.setText(reply);
				    } else {
				    	tv_answer.setText(reply);
				    }
				    ll_answers.addView(view);
	        	}

	        	count++;
	        }

	}

	private void setStaticsView(LinearLayout ll_statics, Opinion opinion) {
		TextView tv_total_appraised = (TextView)ll_statics.findViewById(R.id.tv_total_appraised);
		TextView tv_o1_t = (TextView)ll_statics.findViewById(R.id.tv_o1_title);
		TextView tv_o1_c = (TextView)ll_statics.findViewById(R.id.tv_o1_count);
		TextView tv_o2_t = (TextView)ll_statics.findViewById(R.id.tv_o2_title);
		TextView tv_o2_c = (TextView)ll_statics.findViewById(R.id.tv_o2_count);
		TextView tv_o3_t = (TextView)ll_statics.findViewById(R.id.tv_o3_title);
		TextView tv_o3_c = (TextView)ll_statics.findViewById(R.id.tv_o3_count);
		TextView tv_o4_t = (TextView)ll_statics.findViewById(R.id.tv_o4_title);
		TextView tv_o4_c = (TextView)ll_statics.findViewById(R.id.tv_o4_count);
		
		tv_o1_t.setVisibility(View.GONE);
		tv_o2_t.setVisibility(View.GONE);
		tv_o3_t.setVisibility(View.GONE);
		tv_o4_t.setVisibility(View.GONE);
		tv_o1_c.setVisibility(View.GONE);
		tv_o2_c.setVisibility(View.GONE);
		tv_o3_c.setVisibility(View.GONE);
		tv_o4_c.setVisibility(View.GONE);

		
		
		switch(opinion.getType()) {
	
		case Appraise.TYPE_OPTIONS:
		case Appraise.TYPE_RATING ://ratings will be treated as options

			switch(opinion.getOptionCount()) {
			case 4:
				tv_o4_t.setVisibility(View.VISIBLE);
				tv_o4_c.setVisibility(View.VISIBLE);
				tv_o4_t.setText(opinion.getOptions().get(3));
				tv_o4_c.setText(opinion.getOptionSelectedCount(Appraise.OPTION_4)+"");
			case 3:
				tv_o3_t.setVisibility(View.VISIBLE);
				tv_o3_c.setVisibility(View.VISIBLE);
				tv_o3_t.setText(opinion.getOptions().get(2));
				tv_o3_c.setText(opinion.getOptionSelectedCount(Appraise.OPTION_3)+"");
			case 2:
				tv_o2_t.setVisibility(View.VISIBLE);
				tv_o2_c.setVisibility(View.VISIBLE);
				tv_o2_t.setText(opinion.getOptions().get(1));
				tv_o2_c.setText(opinion.getOptionSelectedCount(Appraise.OPTION_2)+"");
			case 1:
				tv_o1_t.setVisibility(View.VISIBLE);
				tv_o1_c.setVisibility(View.VISIBLE);
				tv_o1_t.setText(opinion.getOptions().get(0));
				tv_o1_c.setText(opinion.getOptionSelectedCount(Appraise.OPTION_1)+"");
			}

			break;

		case Appraise.TYPE_COMMENTS :
			tv_o1_t.setVisibility(View.VISIBLE);
			tv_o1_c.setVisibility(View.VISIBLE);
			tv_o1_t.setText("People Commented :");
			tv_o1_c.setText(""+opinion.getTotalReplyCount());
		}
		
		tv_total_appraised.setText(opinion.getTotalReplyCount()+" out of "+opinion.getTagContactsCount() + " replied");
	}

	public class AnswerItemView extends LinearLayout {
		private ViewGroup viewGroup;

		public AnswerItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
				    setOrientation(LinearLayout.VERTICAL);
				    LayoutInflater inflater = (LayoutInflater) context
				        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				    viewGroup = (ViewGroup) inflater.inflate(R.layout.answer_item_layout, this, true);
		}

	}
}
