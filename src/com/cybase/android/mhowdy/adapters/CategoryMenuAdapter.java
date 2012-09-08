package com.cybase.android.mhowdy.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.activities.CategoriesHomeActivity;

public class CategoryMenuAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<HashMap<String, String>> catTypes = new ArrayList<HashMap<String, String>>();


	public CategoryMenuAdapter(Context context) {
		mContext = context;
	}

	public CategoryMenuAdapter(Context context, ArrayList<HashMap<String, String>> categoryList) {
		mContext = context;
		this.catTypes = categoryList;
	}

	public int getCount() {
		return catTypes.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		// We inflate the xml which gives us a view
		view = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.item_grid, null);
		ImageView image = (ImageView) view.findViewById(R.id.grid_item_image);
		//image.setImageResource(get)
		TextView label = (TextView) view.findViewById(R.id.grid_item_label);
		label.setText(catTypes.get(position).get(CategoriesHomeActivity.TAG_SPOT_NAME));
		convertView = view;
		return convertView;
	}

}
