package com.greatapp.qpinion.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.greatapp.qpinion.R;
import com.greatapp.qpinion.Tools;
import com.greatapp.qpinion.TransactionManager;
import com.greatapp.qpinion.User;
import com.greatapp.qpinion.data.Appraise;
import com.greatapp.qpinion.data.DataManager;

public class AppraiseView extends LinearLayout{

	
	private static final String TAG = "AppraiseView";
	private TextView nameView;
	private TextView questionView;
	private int color;
	private Button appraiseButton;
	private View viewGroup;
	private Appraise mAppraise;
	private Paint paint;
	private ImageView contactImageView;
	private ImageView deleteView;
	
	private TextView yourAnswer;
	private LinearLayout answerView;
	private Button   ignore_button;
	private TextView tv_window_title;

	public AppraiseView(Context context, AttributeSet attrs ) {
		super(context, attrs);
			    setOrientation(LinearLayout.VERTICAL);
			    LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			   viewGroup = (ViewGroup) inflater.inflate(R.layout.appraise_layout, this, true);
			   yourAnswer = (TextView)viewGroup.findViewById(R.id.tv_your_answer);
			   questionView = (TextView)viewGroup.findViewById(R.id.textview_question_for_me);
			   contactImageView = (ImageView)viewGroup.findViewById(R.id.imageView_contact_image);
			   nameView = (TextView)viewGroup.findViewById(R.id.textview_name);
			   answerView = (LinearLayout)viewGroup.findViewById(R.id.linearlayout_answer_view);
			   ignore_button = (Button)viewGroup.findViewById(R.id.b_ignore);
			   appraiseButton = (Button)viewGroup.findViewById(R.id.b_appraise);
			   answerView = (LinearLayout)viewGroup.findViewById(R.id.linearlayout_answer_view);
			   tv_window_title = (TextView)viewGroup.findViewById(R.id.textView_opiniumn_window_title);

			   deleteView = (ImageView)viewGroup.findViewById(R.id.imageView_delete);
			   
			
			   deleteView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						handleDeleteViewClick();
						
					}
				});
			   

			     ignore_button = (Button)viewGroup.findViewById(R.id.b_ignore);
			     ignore_button.setText(""+Tools.getEmijoByUnicode(Tools.e_monkey));
			     appraiseButton = (Button)viewGroup.findViewById(R.id.b_appraise);
			     appraiseButton.setText(""+Tools.getEmijoByUnicode(Tools.e_pencil));
			     appraiseButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						View view = v.getRootView();
						onAppraiseClicked(v,view);
						
					}
				});
			 //    viewGroup.setBackgroundResource(R.drawable.custom_shape);
			  //   init();
			    
	}

	protected void handleDeleteViewClick() {
        DataManager dm = new DataManager(getContext());
        dm.deleteAppraise(getContext(), mAppraise);
        viewGroup.setVisibility(View.GONE);
	}

	private void setAnswerView(Appraise appraise) {
        if(appraise == null) return;
        //cause Adapter may return a recycled 
        //view in which this view is hidden
        answerView.setVisibility(View.VISIBLE);

		RadioGroup rg = (RadioGroup)answerView.findViewById(R.id.radioGroup_options);
		RatingBar rb = (RatingBar)answerView.findViewById(R.id.ratingBar_answer);
		EditText et = (EditText)answerView.findViewById(R.id.editText_answer);
		RadioButton r1 = (RadioButton)answerView.findViewById(R.id.radio_option1);
		RadioButton r2 = (RadioButton)answerView.findViewById(R.id.radio_option2);
		RadioButton r3 = (RadioButton)answerView.findViewById(R.id.radio_option3);
		RadioButton r4 = (RadioButton)answerView.findViewById(R.id.radio_option4);
		
		rg.setVisibility(View.GONE);
		rb.setVisibility(View.GONE);
		et.setVisibility(View.GONE);
		r1.setVisibility(View.GONE);
		r2.setVisibility(View.GONE);
		r3.setVisibility(View.GONE);
		r4.setVisibility(View.GONE);
		
		
		switch(appraise.getType()) {
	
		case Appraise.TYPE_OPTIONS:

			switch(appraise.getOptionCount()) {
			case 4:
				r4.setVisibility(View.VISIBLE);
				r4.setText(appraise.getOptions().get(3));
			case 3:
				r3.setVisibility(View.VISIBLE);
				r3.setText(appraise.getOptions().get(2));
			case 2:
				r2.setVisibility(View.VISIBLE);
				r2.setText(appraise.getOptions().get(1));
			case 1:
				r1.setVisibility(View.VISIBLE);
				r1.setText(appraise.getOptions().get(0));
			}

			rg.setVisibility(View.VISIBLE);
			break;
		case Appraise.TYPE_RATING :
			rb.setVisibility(View.VISIBLE);
			break;
		case Appraise.TYPE_COMMENTS :
			et.setVisibility(View.VISIBLE);		
		}
		
	}

	protected void handlePreviousQuestionViewClick() {

	}

	protected void handleNextQuestionViewClick() {

	}


	protected void onAppraiseClicked(View buttonView, View rootView) {
		//viewGroup.findViewById(R.id.linearLayout_question_for_you_root).setBackgroundColor(getResources().getColor(R.color.violet));

		
		
		//mAppraise.setContactName(RegistrationManager.getUserName(getContext()));
		int ansType = mAppraise.getType();
		switch(ansType) {
		case Appraise.TYPE_OPTIONS :
			RadioButton rb1 = (RadioButton)viewGroup.findViewById(R.id.radio_option1);
			RadioButton rb2 = (RadioButton)viewGroup.findViewById(R.id.radio_option2);
			RadioButton rb3 = (RadioButton)viewGroup.findViewById(R.id.radio_option3);
			RadioButton rb4 = (RadioButton)viewGroup.findViewById(R.id.radio_option4);
			if(rb1.isChecked()) mAppraise.setAnswer(Appraise.OPTION_1);
			else if(rb2.isChecked()) mAppraise.setAnswer(Appraise.OPTION_2);
			else if(rb3.isChecked()) mAppraise.setAnswer(Appraise.OPTION_3);
			else if(rb4.isChecked()) mAppraise.setAnswer(Appraise.OPTION_4);
			else{
				Toast.makeText(getContext(),"No Option Selected",Toast.LENGTH_SHORT).show();
			}
			break;
		case Appraise.TYPE_RATING:
			RatingBar rb = (RatingBar)viewGroup.findViewById(R.id.ratingBar_answer);
			mAppraise.setAnswer(""+((int)rb.getRating()));
			
			break;
		case Appraise.TYPE_COMMENTS :
			EditText et = (EditText)viewGroup.findViewById(R.id.editText_answer);
			 mAppraise.setAnswer(et.getText().toString());
			break;

		}
		Log.d(TAG,"Sending Appraise to Server");
		DataManager dm = new DataManager(getContext());
		dm.updateAppraise(getContext(),mAppraise);
		TransactionManager.sendAppraiseToQpinion(mAppraise,new User(getContext()));
		     ignore_button.setVisibility(View.GONE);
        	appraiseButton.setVisibility(View.GONE);
        	answerView.setVisibility(View.GONE);
        	yourAnswer.setVisibility(View.VISIBLE);
        	String ans = mAppraise.getAnswer();
        	
            if(mAppraise.getType() == Appraise.TYPE_OPTIONS) {
            	if(ans.equals(Appraise.OPTION_1)) {
            		ans = mAppraise.getOptions().get(0);
            	} else if(ans.equals(Appraise.OPTION_2)) {
            		ans = mAppraise.getOptions().get(1);
            	} else if(ans.equals(Appraise.OPTION_3)) {
            		ans = mAppraise.getOptions().get(2);
            	} else if(ans.equals(Appraise.OPTION_4)) {
            		ans = mAppraise.getOptions().get(3);
            	} 
            }
        	yourAnswer.setText(ans);
        	setAppraiseWindowTitle(true);

        		
	}

	public TextView getNameView() {
		return nameView;
	}

	public void setName(CharSequence name) {
		this.nameView.setText(name);
	}
	public void setAppraiseWindowTitle(boolean done) {

		if(done) {
			tv_window_title.setText(""+Tools.getEmijoByUnicode(Tools.e_check));
		} else {
			tv_window_title.setText(""+Tools.getEmijoByUnicode(Tools.e_ques_mark));
		}

	}
	
	public TextView getQuestionView() {
		return questionView;
	}

	public void setQuestion(CharSequence que) {
		this.questionView.setText(que);
	}

	public void setContactImage(int resId) {
		contactImageView.setImageResource(resId);
	}
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}


	public void initView(Appraise appraise) {

		   mAppraise = appraise;
		   nameView.setText(appraise.getOwnerName());
		   //-----default settings for view------//
		   viewGroup.setVisibility(View.VISIBLE);
		   questionView.setText(appraise.getBody());
       	   appraiseButton.setVisibility(View.VISIBLE);
       	   answerView.setVisibility(View.VISIBLE);
       	   ignore_button.setVisibility(View.VISIBLE);
       	   yourAnswer.setVisibility(View.GONE);
       	   //------------------------------------//
		   Log.d(TAG,"Setting appraise Body : +"+appraise.getBody());
		   ignore_button.setText(""+Tools.getEmijoByUnicode(Tools.e_monkey));
	        if(!appraise.getAnswer().equals((""+Appraise.NOT_ANSWERED))) {
	        	Log.d(TAG,"Answered :"+appraise.getAnswer());
	        	appraiseButton.setVisibility(View.GONE);
	        	answerView.setVisibility(View.GONE);
	        	ignore_button.setVisibility(View.GONE);
	        	yourAnswer.setVisibility(View.VISIBLE);
	        	yourAnswer.setText(appraise.getAnswer());
	        	setAppraiseWindowTitle(true);
	        } else {
	        	Log.d(TAG,"NOT ANSWERED :"+appraise.getAnswer());
	        	setAppraiseWindowTitle(false);
	 		    setAnswerView(appraise);
	        }

	}

	

}
