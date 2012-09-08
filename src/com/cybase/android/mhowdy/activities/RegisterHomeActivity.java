package com.cybase.android.mhowdy.activities;

import java.io.IOException;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.data.UserFunctions;

public class RegisterHomeActivity extends FragmentActivity implements OnClickListener {

	public static final String LOG_TAG = RegisterHomeActivity.class.getName();
	public final static String AUTH = "authentication";
	private static final String TAG_CODE = "code";
	private static final String TAG_DATA = "data";
	EditText edMobileNumber;
	private String mobileNum;
	LinearLayout butSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_home);
		Typeface fontHelveticalBold = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaBlk_.ttf");
		Typeface fontHelveticalNarrow = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNw_.ttf");
		Typeface fontAllerDisplay = Typeface.createFromAsset(getAssets(),
				"fonts/AllerDisplay.ttf");
		// Fonts setting
		// ((TextView) findViewById(R.id.str_registration_mobile_number))
		// .setTypeface(fontHelveticalBold);
		// ((TextView) findViewById(R.id.str_registration_title))
		// .setTypeface(fontHelveticalNarrow);
		((TextView) findViewById(R.id.str_registration_get_verification))
				.setTypeface(fontAllerDisplay);

		edMobileNumber = (EditText) findViewById(R.id.edMobileNumber);
		butSubmit = (LinearLayout) findViewById(R.id.butSubmit);

		butSubmit.setOnClickListener(this);
		TelephonyManager tMgr = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
		String mcountry = tMgr.getSimCountryIso();
		edMobileNumber.setText(mPhoneNumber);
		edMobileNumber.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		edMobileNumber.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView textView, int actionId,
					KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					onClick(textView);
				}
				return true;
			}
		});
	}

	public void onClick(View v) {

		String strMobileNumber = edMobileNumber.getText().toString().trim();
		Log.e(LOG_TAG, "StrMobileNumber::" + strMobileNumber + ":");

		if (strMobileNumber == null || strMobileNumber == ""
				|| strMobileNumber.length() == 0) {
			Toast.makeText(getApplicationContext(),
					"Please enter your mobile number!", Toast.LENGTH_SHORT)
					.show();
		} else {
			SharedPreferences settings = getSharedPreferences(
					SplashScreenActivity.PREFS_NAME, 0);
			String strDeviceId = settings.getString(
					SplashScreenActivity.TAG_DEVICE_ID, "n/a");
			InputMethodManager imm = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edMobileNumber.getWindowToken(), 0);
			mobileNum = strMobileNumber;
			String[] registerParams = { strDeviceId, strMobileNumber };
			new RegisterNewDevice().execute(registerParams);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		// Retrieve the message
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean bIShouldClose = mPrefs.getBoolean("CLOSE_REGISTER_ACTIVITY",
				false);

		if (bIShouldClose) {
			mPrefs.edit().remove("CLOSE_REGISTER_ACTIVITY").commit();
			finish();
		}
	}

	private class RegisterNewDevice extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-registerNewDevice", " pre execute async");
			mDialog = ProgressDialog.show(RegisterHomeActivity.this, "",
					"Registering. Please wait...", true);

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			try {
				JSONObject json = userFunction.register(params[0], params[1]);

				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					result = json.getString(TAG_DATA);
					if (String.valueOf(
							"DeviceID is registered again successfully")
							.equalsIgnoreCase(result)) {
						userFunction.resendVerifyCode(params[0], params[1]);
					}

				} else if (strCode.equalsIgnoreCase("404")) {
					result = "error";
					userFunction.resendVerifyCode(params[0], params[1]);
				}
			} catch (IOException e) {
				result = SplashScreenActivity.IO_EXCEPTION;
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.v(LOG_TAG, "result : " + result);
			result = "DeviceID is registered for first time successfully";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-registerNewDevice", " post execute async");
			mDialog.dismiss();
			if (String.valueOf(
					"DeviceID is registered for first time successfully")
					.equalsIgnoreCase(result)) {
				Intent intent = new Intent(RegisterHomeActivity.this,
						RegisterFormActivity.class);
				intent.putExtra("mobileNumber", mobileNum);
				startActivity(intent);
			} else if (String.valueOf(
					"DeviceID is registered again successfully")
					.equalsIgnoreCase(result)) {
				Toast.makeText(getApplicationContext(),
						"Device already registered", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(RegisterHomeActivity.this,
						RegisterConfirmActivity.class);
				intent.putExtra("mobileNumber", mobileNum);
				startActivity(intent);
			} else if (SplashScreenActivity.IO_EXCEPTION.equals(result)) {
				Intent intent = new Intent(RegisterHomeActivity.this,
						HowdyExceptionActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "Error Registering",
						Toast.LENGTH_SHORT).show();

			}

		}
	}
}