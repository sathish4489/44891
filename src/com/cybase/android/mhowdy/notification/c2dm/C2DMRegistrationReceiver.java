package com.cybase.android.mhowdy.notification.c2dm;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;

import com.cybase.android.mhowdy.activities.RegisterHomeActivity;
import com.cybase.android.mhowdy.data.UserFunctions;

public class C2DMRegistrationReceiver extends BroadcastReceiver {
	UserFunctions userFunction = new UserFunctions();
	private static final String TAG_CODE = "code";
	private static final String TAG_DATA = "data";
	private String deviceId = null;
	private String registrationId = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.w("C2DM", "Registration Receiver called");
		if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
			registrationId = intent.getStringExtra("registration_id");
			String error = intent.getStringExtra("error");

			Log.d("C2DM", "dmControl: registrationId = " + registrationId
					+ ", error = " + error);

			deviceId = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
			if (deviceId != null && registrationId != null
					&& null == intent.getStringExtra("error")) {
				new RegistrationNotification().execute();
				// Also save it in the preference to be able to show it later
				saveRegistrationId(context, registrationId);
			}

		}
	}

	private void saveRegistrationId(Context context, String registrationId) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = prefs.edit();
		edit.putString(RegisterHomeActivity.AUTH, registrationId);
		edit.commit();
	}

	// Incorrect usage as the receiver may be canceled at any time
	// do this in an service and in an own thread
	private class RegistrationNotification extends
			AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			String result = "false";
			try {
				JSONObject json = userFunction.registerForNotification(
						deviceId, registrationId);
				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					result = json.getString(TAG_DATA);
				} else if (strCode.equalsIgnoreCase("404")) {
					result = "false";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("google registration id : ", registrationId + " result "
					+ result);
			return null;
		}

	}
}