package com.greatapp.qpinion.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greatapp.qpinion.DetailOpinionActivity;
import com.greatapp.qpinion.R;
import com.greatapp.qpinion.Tools;
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.data.Appraise;
import com.greatapp.qpinion.data.DataManager;
import com.greatapp.qpinion.data.Opinion;

public class OpinionView extends LinearLayout{





	private static final String TAG = "OPINION_VIEW";
	private Opinion mOpinion;
	private Button b_view_point;
	private ViewGroup viewGroup;
	private TextView opinion_body;
	private LinearLayout ll_statics;
	private TextView last_replied_by;
	private TextView last_reply;
	private LinearLayout ll_opinion_frame;

	public OpinionView(Context context, AttributeSet attrs ) {
		
		super(context, attrs);
		Log.d(TAG,"called super");
			    setOrientation(LinearLayout.VERTICAL);
			   // setGravity(Gravity.CENTER_VERTICAL);

			    LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    viewGroup = (ViewGroup) inflater.inflate(R.layout.opinion_layout, this, true);
			    opinion_body = (TextView)viewGroup.findViewById(R.id.tv_body);
			    last_replied_by = (TextView)viewGroup.findViewById(R.id.tv_last__reply_by);
			    last_reply = (TextView)viewGroup.findViewById(R.id.tv_last_reply);
			    ll_statics = (LinearLayout)viewGroup.findViewById(R.id.ll_statics);
			    ll_opinion_frame = (LinearLayout)viewGroup.findViewById(R.id.ll_opinion_frame);
			    
			    ImageView deleteView =(ImageView)viewGroup.findViewById(R.id.imageView_delete);
			    ImageView iv_share =(ImageView)viewGroup.findViewById(R.id.iv_share);
			    b_view_point = (Button)viewGroup.findViewById(R.id.b_view_point);
			    b_view_point.setText("VIEW"+Tools.getEmijoByUnicode(Tools.e_magifier)+"POINT");

			    b_view_point.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
                       onClickViewPoint();
					}
				});
			    deleteView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						handleDeleteButtonClicked();					
					}
				});
			    iv_share.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						handleShareViewClick();						
					}
				});

			     
	}

	protected void onClickViewPoint() {
        Intent i = new Intent(getContext(),DetailOpinionActivity.class);
        i.putExtra("id", mOpinion.getId());
        getContext().startActivity(i);
	}

	private static void setStaticsView(LinearLayout ll_statics, Opinion opinion) {
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

		}

		tv_total_appraised.setText(opinion.getTotalReplyCount()+" out of "+opinion.getTagContactsCount() + " replied");
	}

	protected void handleDeleteButtonClicked() {
        DataManager dm = new DataManager(getContext());
        dm.deleteOpinion(getContext(), mOpinion);
        viewGroup.setVisibility(View.GONE);
	}

	public void initView(Opinion opinion) {
		viewGroup.setVisibility(View.VISIBLE);
		ll_opinion_frame.setBackgroundResource(R.drawable.opinion_item_bg);
		mOpinion = opinion;
		opinion_body.setText(opinion.getBody());
		last_reply.setVisibility(View.GONE);
		last_replied_by.setVisibility(View.GONE);
		if(!opinion.getLastReply().equals(V.NA)) {//last reply available
			last_reply.setVisibility(View.VISIBLE);
			last_replied_by.setVisibility(View.VISIBLE);
			last_reply.setText("ANSWER : "+opinion.getLastReply());
			last_replied_by.setText("LAST REPLIED BY : "+opinion.getLastRepliedBy().getName());
		}
		if(!opinion.isLastUpdateSeen()) {
			ll_opinion_frame.setBackgroundResource(R.drawable.opinion_unseen_item_bg);
		}
        setStaticsView(ll_statics,opinion);
	}
	
	protected void handleShareViewClick() {
		 Intent intent = new Intent(Intent.ACTION_SEND);
		    intent.setType("text/plain");		    
		    String msg = "";
		    msg = msg + "Hey, : \n";
		    msg = msg + mOpinion.getBody()+"\n\n";
		    msg = msg + "Help me out by giving your opinion on Qpinion Andoid Application!.)";
		  //  waIntent.setPackage("com.whatsapp");
		    intent.putExtra(Intent.EXTRA_TEXT,msg);
		    getContext().startActivity(Intent.createChooser(intent, "Ask Opinion via"));
		    getContext().startActivity(intent);
	}




}
