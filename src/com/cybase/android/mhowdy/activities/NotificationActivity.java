package com.cybase.android.mhowdy.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.adapters.NotificationItemAdapter;
import com.cybase.android.mhowdy.adapters.NotificationMessageAdapter;
import com.cybase.android.mhowdy.data.LazyAdapter;
import com.cybase.android.mhowdy.data.UserFunctions;
import com.cybase.android.mhowdy.database.HowdyDAO;
import com.cybase.android.mhowdy.database.MessageVO;
import com.cybase.android.mhowdy.widgets.SegmentedRadioGroup;

public class NotificationActivity extends Activity implements OnClickListener {

	public static final String LOG_TAG = NotificationActivity.class.getName();
	public static final String TAG_CODE = "code";
	public static final String TAG_ERRORS = "errors";
	public static final String TAG_DATA = "data";
	public static final String TAG_SPOT_ID = "SpotID";
	public static final String TAG_SPOT_NAME = "SpotName";
	public static final String TAG_IMAGE = "Image";
	public static final String TAG_DESC = "Description";
	public static final String TAG_RATING = "Rating";
	public static final String TAG_UNREAD_NOTIFICATION = "UnreadNotifications";
	public static final String TAG_LAST_NOTIFICATION_DATE = "LastNotificationDate";
	public static final String TAG_IS_FAVOURITE = "IsFavourite";
	public static final String TAG_PHONE = "PhoneNo";
	public static final String TAG_NOTIFICATION_ID = "NotificationID";
	public static final String TAG_MESSAGE = "Message";
	public static final String TAG_CREATE_DATE = "CreatedDate";
	public static final String TAG_IS_READ = "IsRead";

	public static boolean bEditClicked = false;
	public static boolean bMessageEditClicked = false;
	String deviceId = null;
	String spotId = null;
	String spotName = null;
	String notifId = null;
	SharedPreferences settings = null;
	ApplicationHomeActivity parent = null;
	ListView listNotifications;
	ListView notificationMessage;
	EditText edSearch;
	TextView butEdit;
	TextView butMessageEdit;
	TextView tvNoNotification;
	TextView notificationName;
	ImageView imgView;
	LazyAdapter adapter;
	SegmentedRadioGroup radioGroup;
	ViewFlipper notificationLayouts;
	TextView tvNotDetailTitle;
	Button butCall;
	Button butInfo;
	RadioButton butAll;

	int textlength = 0;
	boolean dbAccess = false;

	ArrayList<HashMap<String, String>> tempNotificationsList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> tempFavNotificationsList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> notificationsList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> favNotificationsList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> selectedPlaceResult = new HashMap<String, String>();

	private HowdyDAO datasource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_flipper);
		datasource = new HowdyDAO(this);

		parent = (ApplicationHomeActivity) this.getParent();
		bEditClicked = false;
		bMessageEditClicked = false;
		butEdit = (TextView) findViewById(R.id.tvEdit);
		butMessageEdit = (TextView) findViewById(R.id.butEdit);
		butCall = (Button) findViewById(R.id.butCall);
		butInfo = (Button) findViewById(R.id.butInfo);
		edSearch = (EditText) findViewById(R.id.edSearch);
		radioGroup = (SegmentedRadioGroup) findViewById(R.id.segment_text);
		notificationName = (TextView) findViewById(R.id.tvNotDetailTitle);
		listNotifications = (ListView) findViewById(R.id.list_notifications);
		notificationMessage = (ListView) findViewById(R.id.list_not_message);
		notificationLayouts = (ViewFlipper) findViewById(R.id.notification_flipper);
		tvNoNotification = (TextView) findViewById(R.id.notification_empty);
		notificationLayouts.setDisplayedChild(0);
		findViewById(R.id.tvBack).setOnClickListener(this);
		findViewById(R.id.tvInfoBack).setOnClickListener(this);
		findViewById(R.id.butAll).setOnClickListener(this);
		findViewById(R.id.butFavorites).setOnClickListener(this);
		findViewById(R.id.butEdit).setOnClickListener(this);
		findViewById(R.id.butInfo).setOnClickListener(this);
		butEdit.setOnClickListener(this);
		// delete item notification/navigation
		listNotifications.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long pos) {
				String unread = null;
				if (R.id.butAll == radioGroup.getCheckedRadioButtonId()) {
					spotId = notificationsList.get(position).get(TAG_SPOT_ID);
					spotName = notificationsList.get(position).get(
							TAG_SPOT_NAME);
					unread = notificationsList.get(position).get(
							TAG_UNREAD_NOTIFICATION);
					if (unread != null && Integer.valueOf(unread) > 0) {
						dbAccess = true;
					} else {
						dbAccess = false;
					}
				} else {
					spotId = favNotificationsList.get(position)
							.get(TAG_SPOT_ID);
					spotName = favNotificationsList.get(position).get(
							TAG_SPOT_NAME);
					unread = favNotificationsList.get(position).get(
							TAG_UNREAD_NOTIFICATION);
					if (unread != null && Integer.valueOf(unread) > 0) {
						dbAccess = true;
					} else {
						dbAccess = false;
					}

				}
				Log.w(LOG_TAG, "dbAccess  " + dbAccess);
				if (bEditClicked) {
					// new DeleteNotification().execute();
				} else {
					notificationName.setText(spotName);
					new GetSpotNotification().execute();
					notificationLayouts.setDisplayedChild(1);
				}

			}
		});
		// delete single notification
		notificationMessage.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long pos) {
				if (bMessageEditClicked) {
					spotId = selectedPlaceResult.get(TAG_SPOT_ID);
					spotName = selectedPlaceResult.get(TAG_SPOT_NAME);
					notifId = messageList.get(position)
							.get(TAG_NOTIFICATION_ID);
					notificationName.setText(spotName);
					new DeleteSpotNotification().execute();
				}
			}
		});
		// get device id
		settings = getSharedPreferences(SplashScreenActivity.PREFS_NAME, 0);
		deviceId = settings
				.getString(SplashScreenActivity.TAG_DEVICE_ID, "n/a");
		callFromParent(null, null);

	}

	public void callFromParent(String itemId, String call) {
		Log.e(LOG_TAG, "itemId  " + itemId + "call  " + call);
		if (null == itemId && null == call) {
			new GetNotifications().execute();
		} else if (call.equals("parent")) {
			spotId = itemId;
			new GetSpotNotification().execute();
			notificationLayouts.setDisplayedChild(1);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tvEdit:
			if (butEdit.getText().equals("Edit")) {
				bEditClicked = true;
				butEdit.setText(R.string.StrDone);
			} else {
				bEditClicked = false;
				butEdit.setText(R.string.strEdit);
			}
			int selectedId = radioGroup.getCheckedRadioButtonId();
			if (R.id.butAll == selectedId) {
				listNotifications.setAdapter(new NotificationItemAdapter(
						getApplicationContext(), notificationsList));
			} else {
				listNotifications.setAdapter(new NotificationItemAdapter(
						getApplicationContext(), favNotificationsList));
			}
			break;
		case R.id.butEdit:
			if (butMessageEdit.getText().equals("Edit")) {
				bMessageEditClicked = true;
				butMessageEdit.setText(R.string.StrDone);

			} else {
				bMessageEditClicked = false;
				butMessageEdit.setText(R.string.strEdit);
			}
			notificationMessage.setAdapter(new NotificationMessageAdapter(
					getApplicationContext(), messageList));
			break;

		case R.id.tvBack:
			bMessageEditClicked = false;
			butMessageEdit.setText(R.string.strEdit);
			new GetNotifications().execute();
			notificationLayouts.setDisplayedChild(0);
			break;
		case R.id.tvInfoBack:
			notificationLayouts.setDisplayedChild(1);
			break;
		case R.id.butAll:
			listNotifications.setAdapter(new NotificationItemAdapter(
					getApplicationContext(), notificationsList));
			break;
		case R.id.butFavorites:
			listNotifications.setAdapter(new NotificationItemAdapter(
					getApplicationContext(), favNotificationsList));
			break;
		case R.id.butInfo:
			if (null == parent) {
				parent = (ApplicationHomeActivity) this.getParent();
			}
			Log.e(LOG_TAG, "spotId " + spotId);
			if (spotId != null) {
				parent.showItemDetails(spotId);
			}

			break;
		}
	}

	private void call(String uriString) {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse(uriString));
			startActivity(callIntent);
		} catch (ActivityNotFoundException e) {
			Log.e(LOG_TAG, "Call failed:" + e);
		}
	}

	private class GetNotifications extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-getNotifications", " pre execute async");
			mDialog = ProgressDialog.show(NotificationActivity.this, "",
					"Retrieving Notifications.. Please wait...", true);
			favNotificationsList = new ArrayList<HashMap<String, String>>();
			notificationsList = new ArrayList<HashMap<String, String>>();

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			try {
				Float fLatitude = (float) (Float.valueOf(settings.getInt(
						"latitude", (int) (13.0741 * 1E6))) / 1E6);
				Float fLongitude = (float) (Float.valueOf(settings.getInt(
						"longitude", (int) (80.2424 * 1E6))) / 1E6);
				JSONObject json = userFunction.getNotifications(deviceId, "10",
						String.valueOf(fLatitude), String.valueOf(fLongitude),
						null);
				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					JSONArray jsonArray = json.getJSONArray(TAG_DATA);
					for (int count = 0; count < jsonArray.length(); count++) {
						HashMap<String, String> notifItem = new HashMap<String, String>();
						JSONObject jsonObject = jsonArray.getJSONObject(count);
						notifItem.put(TAG_SPOT_ID,
								jsonObject.getString(TAG_SPOT_ID));
						notifItem.put(TAG_SPOT_NAME,
								jsonObject.getString(TAG_SPOT_NAME));
						notifItem.put(TAG_DESC, jsonObject.getString(TAG_DESC));
						notifItem.put(TAG_IMAGE,
								jsonObject.getString(TAG_IMAGE));
						notifItem.put(TAG_LAST_NOTIFICATION_DATE, jsonObject
								.getString(TAG_LAST_NOTIFICATION_DATE));
						notifItem.put(TAG_IS_FAVOURITE,
								jsonObject.getString(TAG_IS_FAVOURITE));
						notifItem.put(TAG_RATING,
								jsonObject.getString(TAG_RATING));
						notificationsList.add(notifItem);
					}
					result = "true";

				} else if (strCode.equalsIgnoreCase("404")) {
					result = "false";
				}
				json = userFunction.getFavNotifications(deviceId, null);
				strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					JSONArray jsonArray = json.getJSONArray(TAG_DATA);
					for (int count = 0; count < jsonArray.length(); count++) {
						HashMap<String, String> notifItem = new HashMap<String, String>();
						JSONObject jsonObject = jsonArray.getJSONObject(count);
						notifItem.put(TAG_SPOT_ID,
								jsonObject.getString(TAG_SPOT_ID));
						notifItem.put(TAG_SPOT_NAME,
								jsonObject.getString(TAG_SPOT_NAME));
						notifItem.put(TAG_DESC, jsonObject.getString(TAG_DESC));
						notifItem.put(TAG_IMAGE,
								jsonObject.getString(TAG_IMAGE));
						notifItem.put(TAG_LAST_NOTIFICATION_DATE, jsonObject
								.getString(TAG_LAST_NOTIFICATION_DATE));
						notifItem.put(TAG_UNREAD_NOTIFICATION,
								jsonObject.getString(TAG_UNREAD_NOTIFICATION));
						favNotificationsList.add(notifItem);
					}
					result = "true";
				} else if (strCode.equalsIgnoreCase("404")) {
					result = "false";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-getNotifications", " post execute async " + result);
			mDialog.dismiss();
			if ("true".equalsIgnoreCase(result)) {
				tempFavNotificationsList = favNotificationsList;
				tempNotificationsList = notificationsList;
				int selectedId = radioGroup.getCheckedRadioButtonId();
				if (R.id.butAll == selectedId) {
					listNotifications.setAdapter(new NotificationItemAdapter(
							getApplicationContext(), notificationsList));
				} else {
					listNotifications.setAdapter(new NotificationItemAdapter(
							getApplicationContext(), favNotificationsList));
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Error retrieving notifications", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

	private class DeleteSpotNotification extends
			AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-deleteNotification", " pre execute async");
			mDialog = ProgressDialog.show(NotificationActivity.this, "",
					"Removing Notifications.. Please wait...", true);

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";

			try {
				datasource.open();
				datasource.deleteMessage(spotId, notifId);
				ArrayList<MessageVO> messageVOList = datasource
						.getAllMesages(Integer.valueOf(spotId));
				datasource.close();
				messageList = new ArrayList<HashMap<String, String>>();
				for (MessageVO messageVO : messageVOList) {
					HashMap<String, String> messageItem = new HashMap<String, String>();
					messageItem.put(TAG_NOTIFICATION_ID,
							String.valueOf(messageVO.getNotificationID()));
					messageItem.put(TAG_MESSAGE, messageVO.getMessage());
					messageItem
							.put(TAG_CREATE_DATE, messageVO.getCreatedDate());
					messageItem.put(TAG_IS_READ, String.valueOf(true));
					messageList.add(messageItem);
				}
				if (messageList.size() == 0) {
					result = "empty";
				} else {
					result = "true";
				}

				// *********Web service replaced with DB****start******
				// JSONObject json = userFunction.deleteNotifications(deviceId,
				// spotId, notifId);
				// String strCode = json.getString(TAG_CODE);
				// if (strCode.equalsIgnoreCase("200")) {
				// result = json.getString(TAG_DATA);
				// } else if (strCode.equalsIgnoreCase("404")) {
				// result = "false";
				// }
				// } catch (JSONException e) {
				// e.printStackTrace();
				// *********Web service replaced with DB****end******
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-deleteNotification", " post execute async");
			mDialog.dismiss();
			// *********DB****remove******
			if ("true".equalsIgnoreCase(result)) {
				notificationMessage.setAdapter(new NotificationMessageAdapter(
						NotificationActivity.this, messageList));
				notificationMessage.setVisibility(View.VISIBLE);
				tvNoNotification.setVisibility(View.GONE);
			} else if ("empty".equalsIgnoreCase(result)) {
				tvNoNotification.setVisibility(View.VISIBLE);
				notificationMessage.setVisibility(View.GONE);
			} else {
				Toast.makeText(getApplicationContext(),
						"Error retrieving notifications", Toast.LENGTH_SHORT)
						.show();
			}

			// *********DB****remove******
			// *********Web service replaced with DB****start******
			// if ("true".equalsIgnoreCase(result)) {
			// new GetSpotNotification().execute();
			// } else {
			// Toast.makeText(getApplicationContext(),
			// "Error removing notifications", Toast.LENGTH_SHORT)
			// .show();
			// }
			// *********Web service replaced with DB****end******
		}
	}

	private class GetSpotNotification extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-GetSpotNotification", " pre execute async");
			mDialog = ProgressDialog.show(NotificationActivity.this, "",
					"Retrieving Notifications.. Please wait...", true);
			tvNoNotification.setVisibility(View.VISIBLE);
			notificationMessage.setVisibility(View.GONE);
			notificationMessage.setAdapter(new NotificationMessageAdapter(
					NotificationActivity.this,
					new ArrayList<HashMap<String, String>>()));
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			try {
				JSONObject json = userFunction.getCategoryDetail(deviceId,
						spotId);
				String strCode = json.getString(TAG_CODE);

				if (strCode.equalsIgnoreCase("200")) {
					result = "true";

					JSONObject PlaceResult = json.getJSONObject(TAG_DATA);
					selectedPlaceResult = new HashMap<String, String>();
					selectedPlaceResult.put(TAG_SPOT_ID, spotId);
					selectedPlaceResult.put(TAG_SPOT_NAME,
							PlaceResult.getString(TAG_SPOT_NAME));
					selectedPlaceResult.put(TAG_IMAGE,
							PlaceResult.getString(TAG_IMAGE));
					selectedPlaceResult.put(TAG_DESC,
							PlaceResult.getString(TAG_DESC));
					selectedPlaceResult.put(TAG_IS_FAVOURITE,
							PlaceResult.getString(TAG_IS_FAVOURITE));
					selectedPlaceResult.put(TAG_PHONE,
							PlaceResult.getString(TAG_PHONE));
				} else if (strCode.equalsIgnoreCase("404")) {
					result = "false";
				}

				datasource.open();
				ArrayList<MessageVO> messageVOList = datasource
						.getAllMesages(Integer.valueOf(spotId));
				messageList = new ArrayList<HashMap<String, String>>();
				if (dbAccess) {
					json = userFunction.getSpotNotifications(deviceId, spotId,
							null);
					strCode = json.getString(TAG_CODE);
					if (strCode.equalsIgnoreCase("200")) {
						result = "true";
						JSONArray jsonArray = json.getJSONArray(TAG_DATA);
						if (jsonArray.length() == 0) {
							result = "empty";
						}
						for (int count = 0; count < jsonArray.length(); count++) {
							HashMap<String, String> messageItem = new HashMap<String, String>();
							JSONObject jsonObject = jsonArray
									.getJSONObject(count);
							messageItem.put(TAG_NOTIFICATION_ID,
									jsonObject.getString(TAG_NOTIFICATION_ID));
							messageItem.put(TAG_MESSAGE,
									jsonObject.getString(TAG_MESSAGE));
							messageItem.put(TAG_CREATE_DATE,
									jsonObject.getString(TAG_CREATE_DATE));
							messageItem.put(TAG_IS_READ,
									jsonObject.getString(TAG_IS_READ));

							messageList.add(messageItem);
						}
						for (int count = jsonArray.length() - 1; count >= 0; count--) {
							JSONObject jsonObject = jsonArray
									.getJSONObject(count);
							MessageVO messageVO = new MessageVO();
							messageVO.setSpotID(Integer.valueOf(spotId));
							messageVO.setNotificationID(Integer
									.valueOf(jsonObject
											.getString(TAG_NOTIFICATION_ID)));
							messageVO.setMessage(jsonObject
									.getString(TAG_MESSAGE));
							messageVO.setCreatedDate(jsonObject
									.getString(TAG_CREATE_DATE));
							datasource.addMesage(messageVO);
						}

					} else if (strCode.equalsIgnoreCase("404")) {
						result = "false";
					}
				}
				datasource.close();
				for (MessageVO messageVO : messageVOList) {
					HashMap<String, String> messageItem = new HashMap<String, String>();
					messageItem.put(TAG_NOTIFICATION_ID,
							String.valueOf(messageVO.getNotificationID()));
					messageItem.put(TAG_MESSAGE, messageVO.getMessage());
					messageItem
							.put(TAG_CREATE_DATE, messageVO.getCreatedDate());
					messageItem.put(TAG_IS_READ, String.valueOf(true));
					messageList.add(messageItem);
				}
				if (messageList.size() == 0) {
					result = "empty";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-GetSpotNotification", " post execute async "
					+ result);
			mDialog.dismiss();
			if ("true".equalsIgnoreCase(result)) {
				notificationMessage.setAdapter(new NotificationMessageAdapter(
						NotificationActivity.this, messageList));
				notificationMessage.setVisibility(View.VISIBLE);

				tvNoNotification.setVisibility(View.GONE);
			} else if ("empty".equalsIgnoreCase(result)) {
				tvNoNotification.setVisibility(View.VISIBLE);
				notificationMessage.setVisibility(View.GONE);
			} else {
				Toast.makeText(getApplicationContext(),
						"Error retrieving notifications", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

}