package com.cybase.android.mhowdy.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.activities.NotificationActivity;
import com.cybase.android.mhowdy.data.ImageLoader;

public class NotificationItemAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<HashMap<String, String>> placesList;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy hh:mm a");;
	private SimpleDateFormat dateFormatDB = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public NotificationItemAdapter(Context context) {
		mContext = context;
	}

	public NotificationItemAdapter(Context context,
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
		view = inflater.inflate(R.layout.listitem_notificationitem, null);

		TextView placeName = (TextView) view.findViewById(R.id.not_name);
		TextView placeDesc = (TextView) view.findViewById(R.id.not_desc);
		TextView placeUnRead = (TextView) view.findViewById(R.id.not_unread);
		TextView PlaceNotifDate = (TextView) view.findViewById(R.id.not_date);
		RatingBar placeRating = (RatingBar) view.findViewById(R.id.not_rating);
		ImageView placeImage = (ImageView) view.findViewById(R.id.not_image);
		ImageView editImage = (ImageView) view.findViewById(R.id.not_arrows);
		// add values to view

		HashMap<String, String> place = placesList.get(position);
		placeName.setText(place.get(NotificationActivity.TAG_SPOT_NAME));
		placeDesc.setText(place.get(NotificationActivity.TAG_DESC));
		String unreadVal = place
				.get(NotificationActivity.TAG_UNREAD_NOTIFICATION);
		if (unreadVal != null && unreadVal.length() > 0
				&& !"0".equalsIgnoreCase(unreadVal)) {
			placeUnRead.setText(place
					.get(NotificationActivity.TAG_UNREAD_NOTIFICATION));
		}
		String dateCreated = place
				.get(NotificationActivity.TAG_LAST_NOTIFICATION_DATE);
		if (dateCreated != null && dateCreated.length() > 0) {
			try {
				PlaceNotifDate.setText(dateFormat.format(dateFormatDB
						.parse(dateCreated)));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		String rating = place.get(NotificationActivity.TAG_RATING);
		if (rating != null && rating.length() > 0) {
			placeRating.setRating(Float.valueOf(rating));
		}
		imageLoader.DisplayImage(place.get(NotificationActivity.TAG_IMAGE),
				placeImage);
		if (NotificationActivity.bEditClicked) {
			editImage.setImageResource(R.drawable.delete);
		}
		convertView = view;
		return convertView;
	}
}
