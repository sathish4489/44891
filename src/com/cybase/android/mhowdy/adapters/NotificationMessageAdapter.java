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
import android.widget.TextView;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.activities.NotificationActivity;
import com.cybase.android.mhowdy.data.ImageLoader;

public class NotificationMessageAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<HashMap<String, String>> messageList;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy hh:mm a");;
	private SimpleDateFormat dateFormatDB = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public NotificationMessageAdapter(Context context) {
		mContext = context;
	}

	public NotificationMessageAdapter(Context context,
			ArrayList<HashMap<String, String>> messageList) {
		mContext = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.messageList = messageList;
		imageLoader = new ImageLoader(context.getApplicationContext());
	}

	public int getCount() {
		return messageList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		view = inflater.inflate(R.layout.listitem_notification_message, null);

		TextView message = (TextView) view.findViewById(R.id.not_message);
		TextView time = (TextView) view.findViewById(R.id.not_create_time);
		ImageView editImage = (ImageView) view.findViewById(R.id.not_arrow);
		// add values to view

		HashMap<String, String> messageItem = messageList.get(position);
		message.setText("Message : "+messageItem.get(NotificationActivity.TAG_MESSAGE));
		
		String dateCreated = messageItem.get(NotificationActivity.TAG_CREATE_DATE);
		if (dateCreated != null && dateCreated.length() > 0) {
			try {
				time.setText("Date : "+dateFormat.format(dateFormatDB
						.parse(dateCreated)));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		if (NotificationActivity.bMessageEditClicked) {
			editImage.setImageResource(R.drawable.delete);
		}
		convertView = view;
		return convertView;
	}
}
