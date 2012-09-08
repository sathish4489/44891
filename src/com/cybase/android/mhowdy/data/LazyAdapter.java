package com.cybase.android.mhowdy.data;

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
import com.cybase.android.mhowdy.activities.NotificationActivity;

public class LazyAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	Context context;

	public LazyAdapter(Context ctx, ArrayList<HashMap<String, String>> d) {

		context = ctx;
		data = d;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(context.getApplicationContext());
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.notification_list_row, null);

		TextView title = (TextView) vi.findViewById(R.id.title); // title
		TextView artist = (TextView) vi.findViewById(R.id.artist); // artist name
																	
		TextView duration = (TextView) vi.findViewById(R.id.duration); // duration
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb image

		ImageView arrow_image = (ImageView)vi.findViewById(R.id.arrow);
		
		
		HashMap<String, String> song = new HashMap<String, String>();
		song = data.get(position);

		if(NotificationActivity.bEditClicked) {
			arrow_image.setImageResource(R.drawable.delete);
		}
		return vi;
	}
}