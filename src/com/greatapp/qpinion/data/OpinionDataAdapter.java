package com.greatapp.qpinion.data;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class OpinionDataAdapter extends ArrayAdapter<Opinion> {


			private static final String TAG = "OPINIUMADAPTER";
			// declaring our ArrayList of items
			private ArrayList<Opinion> objects;

			/* here we must override the constructor for ArrayAdapter
			* the only variable we care about now is ArrayList<Item> objects,
			* because it is the list of objects we want to display.
			*/
			public OpinionDataAdapter(Context context, int textViewResourceId, ArrayList<Opinion> objects) {
				
				super(context, textViewResourceId, objects);
				Log.d(TAG,"creating ContentAdapter :");
				this.objects = objects;
			}

			/*
			 * we are overriding the getView method here - this is what defines how each
			 * list item will look.
			 */
			@Override
			public View getView(int position, View convertView, ViewGroup parent){
				Log.d(TAG," ContentAdapter getView :");
				// assign the view we are converting to a local variable
				View v = convertView;
	            View view = null;
				// first check to see if the view is null. if so, we have to inflate it.
				// to inflate it basically means to render, or show, the view.
				Opinion a = objects.get(position);
                //view = new OpinionView(getContext(), null, a);

				/*
				 * Recall that the variable position is sent in as an argument to this method.
				 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
				 * iterates through the list we sent it)
				 * 
				 * Therefore, i refers to the current Item object.
				 */
				


				// the view must be returned to our activity
				return null;

			}

	}


