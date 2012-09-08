package com.cybase.android.mhowdy.activities;

import java.io.IOException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.data.UserFunctions;

public class SettingsActivity extends Activity implements OnClickListener {
	private AlertDialog.Builder alertBuilder;
	private AlertDialog.Builder notificationAlert;
	private WebView webView;
	SharedPreferences settings;
	TextView notificationText = null;
	boolean notifiEnabled = false;
	// JSON Node name
	private static final String TAG_CODE = "code";
	private static final String TAG_DATA = "data";
	private static final String TAG_ERRORS = "errors";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_home);

		settings = getSharedPreferences(SplashScreenActivity.PREFS_NAME, 0);
		notifiEnabled = settings.getBoolean("notificationsEnabled", true);
		alertBuilder = new AlertDialog.Builder(this);
		notificationAlert = new AlertDialog.Builder(this);

		notificationText = (TextView) findViewById(R.id.setting_notification_text);

		if (notifiEnabled) {
			notificationText.setText(getString(R.string.setting_notification));
		} else {
			notificationText
					.setText(getString(R.string.setting_notification_on));
		}
		findViewById(R.id.setting_help).setOnClickListener(this);
		findViewById(R.id.setting_about).setOnClickListener(this);
		findViewById(R.id.setting_notification).setOnClickListener(this);
		findViewById(R.id.setting_nw_status).setOnClickListener(this);
		findViewById(R.id.setting_share).setOnClickListener(this);
		findViewById(R.id.setting_tell_friend).setOnClickListener(this);

	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.setting_help:
			new GetHelp().execute();
			break;
		case R.id.setting_about:
			new GetAboutUs().execute();
			break;
		case R.id.setting_notification:
			notificationAlert();
			break;
		case R.id.setting_nw_status:
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.setting_nw_status_enabled),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.setting_nw_status_disabled),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.setting_share:

			break;
		case R.id.setting_tell_friend:
			Intent sharingIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			StringBuilder shareBody = new StringBuilder();
			shareBody.append("Howdy : http://www.nonstopsms.com/");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					shareBody.toString());
			startActivity(Intent.createChooser(sharingIntent,
					getString(R.string.setting_tell_friend)));
			break;

		default:
			break;
		}

	}

	private void notificationAlert() {
		String notificationId = notificationText.getText().toString();
		final SharedPreferences.Editor editor = settings.edit();
		if (notificationId.equals(getString(R.string.setting_notification))) {

			notificationAlert
					.setTitle(getString(R.string.setting_notification))
					.setMessage(
							getString(R.string.setting_turn_off_notification_content))
					.setCancelable(true)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									editor.putBoolean("notificationsEnabled",
											false);
									notificationText
											.setText(getString(R.string.setting_notification_on));
									editor.commit();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			notificationAlert.create().show();
		} else if (notificationId
				.equals(getString(R.string.setting_notification_on))) {
			editor.putBoolean("notificationsEnabled", true);
			notificationText.setText(getString(R.string.setting_notification));
			Toast.makeText(getApplicationContext(),
					getString(R.string.setting_turn_on_notification_content),
					Toast.LENGTH_SHORT).show();
			registerForNotification();
		}
		editor.commit();
	}

	public void registerForNotification() {
		Log.w("C2DM", "start registration process");
		Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
		intent.putExtra("app",
				PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		// Sender currently not used
		intent.putExtra("sender", "daisymadhan@gmail.com");
		startService(intent);
	}

	private class GetHelp extends AsyncTask<String, Void, String> {
		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-get help", " pre execute async");
			mDialog = ProgressDialog.show(SettingsActivity.this, "",
					"Please wait...", true);

		}

		@Override
		protected String doInBackground(String... arg0) {
			String result = "false";
			try {
				JSONObject json = userFunction.getHelp();

				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					result = json.getString(TAG_DATA);
				} else {
					result = "false";
				}

			} catch (IOException e) {
				result = SplashScreenActivity.IO_EXCEPTION;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-get help", " post execute async");
			mDialog.dismiss();
			super.onPostExecute(result);
			if (SplashScreenActivity.IO_EXCEPTION.equals(result)) {
				Intent intent = new Intent(SettingsActivity.this,
						HowdyExceptionActivity.class);
				startActivity(intent);
			} else if ("false".equals(result)) {
				Toast.makeText(getApplicationContext(),
						"Unable to load Help Content", Toast.LENGTH_SHORT)
						.show();
			} else {
				webView = new WebView(SettingsActivity.this);
				webView.loadData(result, "text/html", null);
				alertBuilder.setTitle(R.string.setting_help)
						// .setMessage(getString(resId))
						.setCancelable(true)
						.setPositiveButton("Close",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								}).setView(webView);
				alertBuilder.create().show();

			}
		}

	}

	private class GetAboutUs extends AsyncTask<String, Void, String> {
		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-get About Us", " pre execute async");
			mDialog = ProgressDialog.show(SettingsActivity.this, "",
					"Please wait...", true);

		}

		@Override
		protected String doInBackground(String... arg0) {
			String result = "false";
			try {
				JSONObject json = userFunction.getAboutUs();

				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					result = json.getString(TAG_DATA);
				} else {
					result = "false";
				}

			} catch (IOException e) {
				result = SplashScreenActivity.IO_EXCEPTION;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-get About Us", " post execute async");
			mDialog.dismiss();
			super.onPostExecute(result);
			if (SplashScreenActivity.IO_EXCEPTION.equals(result)) {
				Intent intent = new Intent(SettingsActivity.this,
						HowdyExceptionActivity.class);
				startActivity(intent);
			} else if ("false".equals(result)) {
				Toast.makeText(getApplicationContext(),
						"Unable to load About Us Content", Toast.LENGTH_SHORT)
						.show();
			} else {
				webView = new WebView(SettingsActivity.this);
				webView.loadData(result, "text/html", null);
				alertBuilder.setTitle(R.string.setting_about)
						// .setMessage(getString(resId))
						.setCancelable(true)
						.setPositiveButton("Close",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								}).setView(webView);
				alertBuilder.create().show();

			}
		}

	}

}