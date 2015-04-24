package com.greatapp.qpinion;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.greatapp.qpinion.constants.DB;
import com.greatapp.qpinion.data.Appraise;
import com.greatapp.qpinion.data.AppraiseCursorAdapter;
import com.greatapp.qpinion.data.DataManager;
import com.greatapp.qpinion.data.Opinion;
import com.greatapp.qpinion.data.OpinionCursorAdapter;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceHolderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

//This is the Adapter being used to display the list's data.
OpinionCursorAdapter mOpinionAdapter;
AppraiseCursorAdapter mAppraiseAdapter;
//If non-null, this is the current filter the user has provided.
String mCurFilter;
private ArrayList<Opinion> mOpinions;

@Override 
public void onActivityCreated(Bundle savedInstanceState) {
	   Log.d(TAG,"OnActivityCreated");
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

}




@Override
public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	if(id == OPINION_LODER_ID) {
		Log.d(TAG,"onCreateLoader opinion loader");
	       return new CursorLoader(getActivity(), DB.OPINION_TABLE_URI,
	    	        DB.OPINION_TABLE_PROJECTION, null, null,
	    	        "data DESC");
	}
	else if(id == APPRAISE_LODER_ID) {
		Log.d(TAG,"onCreateLoader appraise loader");
	       return new CursorLoader(getActivity(), DB.APPRAISE_TABLE_URI,
	    	        DB.APPRAISE_TABLE_PROJECTION, null, null,
	    	        "data DESC");
	}
	return null;

}

@Override
public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
	if(loader.getId() == OPINION_LODER_ID) {
		Log.d(TAG,"onLoadFinished opinion loader");
        mOpinionAdapter.swapCursor(data);
        DataManager dm = new DataManager(getActivity());
        mOpinions = dm.getOpinionList(getActivity());
	}
	else if(loader.getId() == APPRAISE_LODER_ID) {
		Log.d(TAG,"onLoadFinished appraise loader");
		mAppraiseAdapter.swapCursor(data);
	}
       ((ActionBarActivity) getActivity()).setProgressBarIndeterminateVisibility(false);
       ((ActionBarActivity) getActivity()).invalidateOptionsMenu();       
}
@Override
public void onLoaderReset(Loader<Cursor> loader) {

	if(loader.getId() == OPINION_LODER_ID) {
		Log.d(TAG,"reseting opinion loader");
        mOpinionAdapter.swapCursor(null);
	}
	else if(loader.getId() == APPRAISE_LODER_ID) {
		Log.d(TAG,"reseting appraise loader");
		mAppraiseAdapter.swapCursor(null);
	}
} 
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */

	private static final String ARG_SECTION_NUMBER = "section_number";
	protected static final String TAG = "PlaceholderFragment";
	private static final int APPRAISE_LODER_ID = 1;
	private static final int OPINION_LODER_ID = 0;
    static int layoutId;
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static PlaceHolderFragment newInstance(int sectionNumber) {
		Log.d(TAG,"newInstance");
		PlaceHolderFragment fragment = new PlaceHolderFragment();
		layoutId = sectionNumber;
		if(layoutId == 1) {
			
		}else if (layoutId == 2) {
			
		}
		
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}


	public PlaceHolderFragment() {
		Log.d(TAG,"PlaceHolderFragment");
	}
    ListView contentListView;
	private ArrayList<Opinion> opinionList;
	private ArrayList<Appraise> appraiseList;
	private ViewGroup rootView;
	private ActionBar actionBar;
	protected int SCROLL_STATE;
	protected int firstVisibleItem;
	protected int visibleItemCount;
	protected int totalItemCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.d(TAG,"onCreateView");
		rootView = (ViewGroup)inflater.inflate(R.layout.fragment_qpinion_main,container, false);
		contentListView = (ListView)rootView.findViewById(R.id.lv_content_list);
		if(layoutId == 1) {
			Log.d(TAG,"Loading OpinionLoader");
	        mOpinionAdapter = new OpinionCursorAdapter(getActivity(), null, false);
            contentListView.setAdapter(mOpinionAdapter);
	        getLoaderManager().initLoader(OPINION_LODER_ID, null,this);
	   
		} else if(layoutId == 2){
			Log.d(TAG,"Loading OpinionLoader");
	        mAppraiseAdapter = new AppraiseCursorAdapter(getActivity(), null, false);
            contentListView.setAdapter(mAppraiseAdapter);
	        getLoaderManager().initLoader(APPRAISE_LODER_ID, null,this);
		}

		contentListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				Log.d(TAG,"SCROLL STATE : "+scrollState);
				if(scrollState == 0) {//stable-- no scroll

						if(layoutId == 1) {//for unseen opinions
							Log.d(TAG,"SCROLL firstVisibleItem : "+firstVisibleItem);
							Log.d(TAG,"SCROLL visibleItemCount : "+visibleItemCount);
							Log.d(TAG,"SCROLL totalItemCount : "+totalItemCount);
							updateVisitedViews(getActivity());
						} else if(layoutId == 2){//for unseen appraises
							
						}

				}
			}
			
			@Override
			public void onScroll(AbsListView view, int _firstVisibleItem,
					int _visibleItemCount, int _totalItemCount) {

				firstVisibleItem = _firstVisibleItem;
				visibleItemCount = _visibleItemCount;
				totalItemCount = _totalItemCount;
			}
		});
		return rootView;

	}
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG,"onAttach");
		((QpinionMainActivity) activity).onSectionAttached(getArguments()
				.getInt(ARG_SECTION_NUMBER));

	}
	
	@Override
	public void onDetach( ) {
		super.onDetach();
		Log.d(TAG,"onDetach");

	}

	protected void refreshView() {
		
		if(opinionList != null){
		//	opinionList = mDataManager.getOpinionList(getActivity());
			opinionList.notify();
		}
		if(appraiseList != null) {
			//appraiseList = mDataManager.getAppraiseList(getActivity());
			appraiseList.notify();
		}
	}
	
	protected void updateVisitedViews(final Context context) {

			int actual_first_visible_index = totalItemCount - (firstVisibleItem+1);
			int second_visible_index =  actual_first_visible_index -1;
			int third_visible_index =  actual_first_visible_index -2;
			int fourth_visible_index = actual_first_visible_index - 3;
			final DataManager dm = new DataManager(context);
            switch(visibleItemCount) {
            case 4:
            	final Opinion opinion4 = mOpinions.get(fourth_visible_index);
            	if(!opinion4.isLastUpdateSeen()) {
            		opinion4.setLastUpdateSeen(true);
            		 new Handler().postDelayed(new Runnable(){
            	           @Override
            	            public void run() {
            	        		Log.e(TAG,"Again Register");
                                dm.updateOpinion(context,opinion4);
            	            }
            	          }, 3000);
            	}
            case 3:
            	final Opinion opinion3 = mOpinions.get(third_visible_index);
            	if(!opinion3.isLastUpdateSeen()) {
            		opinion3.setLastUpdateSeen(true);
           		 new Handler().postDelayed(new Runnable(){
      	           @Override
      	            public void run() {
      	        		Log.e(TAG,"Again Register");
                        dm.updateOpinion(context,opinion3);
      	            }
      	          }, 3000);

            	}
            case 2:
            	final Opinion opinion2 = mOpinions.get(second_visible_index);
            	if(!opinion2.isLastUpdateSeen()) {
            		opinion2.setLastUpdateSeen(true);
              		 new Handler().postDelayed(new Runnable(){
            	           @Override
            	            public void run() {
            	        		Log.e(TAG,"Again Register");
                              dm.updateOpinion(context,opinion2);
            	            }
            	          }, 3000);

            	}
            case 1:
            	final Opinion opinion1 = mOpinions.get(actual_first_visible_index);
            	if(!opinion1.isLastUpdateSeen()) {
            		opinion1.setLastUpdateSeen(true);
              		 new Handler().postDelayed(new Runnable(){
            	           @Override
            	            public void run() {
            	        		Log.e(TAG,"Again Register");
                              dm.updateOpinion(context,opinion1);
            	            }
            	          }, 4000);
            	}
            }
			



	}
}

