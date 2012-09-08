package com.cybase.android.mhowdy.activities;

import java.io.IOException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.data.UserFunctions;

public class RegisterConfirmActivity extends Activity implements
		OnClickListener {

	public static final String LOG_TAG = RegisterConfirmActivity.class
			.getName();

	// JSON Node name
	private static final String TAG_CODE = "code";
	private static final String TAG_DATA = "data";

	private LinearLayout butDone;
	private EditText butResendNumber;
	private LinearLayout butResendVerificationCode;
	private EditText edVerificationCode;
	private SharedPreferences settings;
	private String strDeviceId;
	private String strMobileNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_verification);
		// Fonts setting
		Typeface fontHelveticalBold = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaBlk_.ttf");
		Typeface fontHelveticalNarrow = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNw_.ttf");
		Typeface fontAllerDisplay = Typeface.createFromAsset(getAssets(),
				"fonts/AllerDisplay.ttf");

		((TextView) findViewById(R.id.str_verification_resend))
				.setTypeface(fontAllerDisplay);
		((TextView) findViewById(R.id.str_verification_send))
				.setTypeface(fontAllerDisplay);
		// Restore preferences
		settings = getSharedPreferences(SplashScreenActivity.PREFS_NAME, 0);
		strDeviceId = settings.getString(SplashScreenActivity.TAG_DEVICE_ID,
				"n/a");
		strMobileNumber = getIntent().getExtras().getString("mobileNumber");
		edVerificationCode = (EditText) findViewById(R.id.edVerificationCode);
		edVerificationCode.setImeOptions(EditorInfo.IME_ACTION_DONE);
		edVerificationCode
				.setOnEditorActionListener(new OnEditorActionListener() {

					public boolean onEditorAction(TextView textView,
							int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							onClick(butDone);
						}
						return true;
					}
				});
		butDone = (LinearLayout) findViewById(R.id.butDone);
		butResendVerificationCode = (LinearLayout) findViewById(R.id.butResendVerificationCode);
		butResendNumber = (EditText) findViewById(R.id.edEditNumber);
		butResendNumber.setText(strMobileNumber);
		butResendNumber.setImeOptions(EditorInfo.IME_ACTION_SEND);
		butResendNumber.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView textView, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					onClick(butResendVerificationCode);
				}
				return true;
			}
		});
		butDone.setOnClickListener(this);
		butResendVerificationCode.setOnClickListener(this);
		// hide keyboard
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edVerificationCode.getWindowToken(), 0);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.butDone:
			String strVerificationCode = edVerificationCode.getText()
					.toString();
			InputMethodManager inputMethodManager = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
					edVerificationCode.getWindowToken(), 0);
			if (strVerificationCode == null || strVerificationCode == ""
					|| strVerificationCode.length() == 0) {
				Toast.makeText(getApplicationContext(),
						"Please enter verification code!", Toast.LENGTH_SHORT)
						.show();
			} else {
				String[] registerParams = { strDeviceId, strVerificationCode };
				new FinishRegister().execute(registerParams);
			}
			break;
		case R.id.butResendVerificationCode:
			String strMobileNo = butResendNumber.getText().toString();
			InputMethodManager inputMethodManager1 = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager1.hideSoftInputFromWindow(
					butResendNumber.getWindowToken(), 0);
			if (null == strMobileNo || strMobileNo == ""
					|| strMobileNo.length() == 0) {
				Toast.makeText(getApplicationContext(),
						"Please enter Mobile number!", Toast.LENGTH_SHORT)
						.show();
			} else {
				new ResendVerification().execute();
			}

			break;
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
			finish();
		}
	}

	private void finishRegisterActivity() {
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		mPrefs.edit().putBoolean("CLOSE_REGISTER_ACTIVITY", true).commit();
		finish();
	}

	private class FinishRegister extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-finishRegister", " pre execute async");
			mDialog = ProgressDialog.show(RegisterConfirmActivity.this, "",
					"Verifying. Please wait...", true);

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			try {
				JSONObject json = userFunction.verify(params[0], params[1]);

				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					result = json.getString(TAG_DATA);
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
			Log.d("progress-finishRegister", " post execute async");
			mDialog.dismiss();
			if ("true".equalsIgnoreCase(result)) {
				finishRegisterActivity();
				Intent intent = new Intent(RegisterConfirmActivity.this,
						ApplicationHomeActivity.class);
				startActivity(intent);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("registered", true);
				// Commit the edits!
				editor.commit();
			} else if (result.equals(SplashScreenActivity.IO_EXCEPTION)) {
				Intent intent = new Intent(RegisterConfirmActivity.this,
						HowdyExceptionActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						"Invalid Verfication code", Toast.LENGTH_SHORT).show();
			}

		}
	}

	private class ResendVerification extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			String result = "false";
			try {
				UserFunctions userFunction = new UserFunctions();
				JSONObject json = userFunction.resendVerifyCode(strDeviceId,
						strMobileNumber);

				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					result = "true";
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
			if (result.equals("true")) {
				Toast.makeText(getApplicationContext(),
						"Resend Verification Code Sent!", Toast.LENGTH_SHORT)
						.show();
			} else if (result.equals(SplashScreenActivity.IO_EXCEPTION)) {
				Intent intent = new Intent(RegisterConfirmActivity.this,
						HowdyExceptionActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						"Resend Verification Code Failed!", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}
}