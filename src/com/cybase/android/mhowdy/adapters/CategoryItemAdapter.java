package com.cybase.android.mhowdy.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.activities.CategoriesHomeActivity;
import com.cybase.android.mhowdy.data.ImageLoader;

public class CategoryItemAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<HashMap<String, String>> placesList;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public CategoryItemAdapter(Context context) {
		mContext = context;
	}

	public CategoryItemAdapter(Context context,
			ArrayList<HashMap<String, String>> placesList) {
		mContext = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.placesList = placesList;
		imageLoader = new ImageLoader(context.getApplicationContext());
	}

	public int getCount() {
		return placesList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		view = inflater.inflate(R.layout.listitem_categoryitem, null);

		TextView placeName = (TextView) view.findViewById(R.id.cat_name);
		TextView placeDesc = (TextView) view.findViewById(R.id.cat_desc);
		TextView PlaceDistance = (TextView) view
				.findViewById(R.id.cat_distance);
		RatingBar placeRating = (RatingBar) view.findViewById(R.id.cat_rating);
		ImageView placeImage = (ImageView) view.findViewById(R.id.cat_image);
		// add values to view

		HashMap<String, String> place = placesList.get(position);
		placeName.setText(place.get(CategoriesHomeActivity.TAG_SPOT_NAME));
		placeDesc.setText(place.get(CategoriesHomeActivity.TAG_DESC));
		PlaceDistance.setText(place.get(CategoriesHomeActivity.TAG_DISTANCE));
		String rating = place.get(CategoriesHomeActivity.TAG_RATING).length() > 0 ? place
				.get(CategoriesHomeActivity.TAG_RATING) : "0";
		placeRating.setRating(Float.valueOf(rating));
		Log.d("icon", place.get(CategoriesHomeActivity.TAG_IMAGE));
		imageLoader.DisplayImage(place.get(CategoriesHomeActivity.TAG_IMAGE),
				placeImage);
		convertView = view;
		return convertView;
	}
}
