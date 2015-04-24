package com.greatapp.qpinion.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.greatapp.qpinion.R;
import com.greatapp.qpinion.constants.V;
import com.greatapp.qpinion.group.GroupManager;
import com.greatapp.qpinion.group.QpinionGroup;


public class GroupListItemView extends LinearLayout{

	private static final String TAG = "QPINION";
	private TextView tv_name;
	private ProgressBar pb_delete;
	private ImageView iv_remove_group;
	private ViewGroup viewGroup;
	private QpinionGroup mGroup;
	private TextView tv_contact_count;

	public GroupListItemView(Context context,AttributeSet attrs) {
		super(context, attrs);

		 LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 viewGroup = (ViewGroup) inflater.inflate(R.layout.group_list_item_view, this, true);
		 iv_remove_group = (ImageView)viewGroup.findViewById(R.id.iv_remove);
		 pb_delete = (ProgressBar)viewGroup.findViewById(R.id.pb_delete);
		 tv_contact_count = (TextView)viewGroup.findViewById(R.id.tv_contact_count);
		 pb_delete.setVisibility(View.GONE);
		  tv_name = (TextView)viewGroup.findViewById(R.id.tv_group_name);

		 iv_remove_group.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {

			onClickRemoveGroup();
			}
		});
	}
     public void initView(QpinionGroup group){
    	 mGroup = group;
    	 viewGroup.setVisibility(View.VISIBLE);
    	 iv_remove_group.setVisibility(View.VISIBLE);
		 pb_delete.setVisibility(View.GONE);
    	 Log.d(TAG,"initializing view for : "+group.getName());
		 tv_name.setText(group.getName());
		 if(group.getName().equals(V.DEFAULT_GROUP_NAME)) {
	    	 iv_remove_group.setVisibility(View.GONE);
		 }
		 tv_contact_count.setText(""+group.getContactCount()+" members");
     }
	protected void onClickRemoveGroup() {
		
		iv_remove_group.setVisibility(View.GONE);
		pb_delete.setVisibility(View.VISIBLE);
		String name = tv_name.getText().toString();
		GroupManager gm = new GroupManager(getContext());
		gm.getGroupByName(getContext(), name);
		viewGroup.setVisibility(View.GONE);
		gm.deleteGroup(gm.getGroupByName(getContext(), name));

	}

}