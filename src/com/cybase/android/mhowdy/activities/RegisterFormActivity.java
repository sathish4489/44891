package com.cybase.android.mhowdy.activities;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.data.UserFunctions;

public class RegisterFormActivity extends FragmentActivity implements
		OnClickListener {

	public static final String LOG_TAG = RegisterFormActivity.class.getName();

	// JSON Node name
	private static final String TAG_CODE = "code";
	private static final String TAG_DATA = "data";
	private static final String TAG_ERRORS = "errors";

	private TextView butNext;
	private EditText edFirstName;
	private EditText edLastName;
	private EditText edEmail;
	private String strMobileNumber;
	private static EditText edDOB;
	private static EditText edNickName;
	private RadioGroup rdGender;
	static final int DATE_DIALOG_ID = 0;
	private static int year;
	private static int month;
	private static int day;
	private StringBuilder validatBuilder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_form);

		strMobileNumber = getIntent().getExtras().getString("mobileNumber");
		butNext = (TextView) findViewById(R.id.butNext);
		edFirstName = (EditText) findViewById(R.id.edFirstName);
		edLastName = (EditText) findViewById(R.id.edLastName);
		edEmail = (EditText) findViewById(R.id.edEmail);
		edDOB = (EditText) findViewById(R.id.imgButtonCalendar);
		edNickName = (EditText) findViewById(R.id.edNickname);
		butNext.setOnClickListener(this);
		edDOB.setOnClickListener(this);
		rdGender = (RadioGroup) findViewById(R.id.rdGender);
		findViewById(R.id.backbtn).setOnClickListener(this);
		// hide keyboard
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edFirstName.getWindowToken(), 0);

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.butNext:
			String strFirstName = URLEncoder.encode(edFirstName.getText()
					.toString().trim());
			String strEmail = edEmail.getText().toString().replace("&", "")
					.replace(" ", "").trim();
			String strLastName = URLEncoder.encode(edLastName.getText()
					.toString().trim());
			String strDOB = edDOB.getText().toString().trim();
			String strgender = ((RadioButton) findViewById(rdGender
					.getCheckedRadioButtonId())).getText().toString().trim();
			System.out.println(strgender);
			String strNickName = edNickName.getText().toString().trim();
			boolean bValidFields = validateRegisterFormFields(strFirstName,
					strLastName, strgender, strDOB, strEmail, strNickName);
			if (bValidFields) {
				SharedPreferences settings = getSharedPreferences(
						SplashScreenActivity.PREFS_NAME, 0);
				String strDeviceId = settings.getString(
						SplashScreenActivity.TAG_DEVICE_ID, "n/a");
				String[] contactParams = { strDeviceId, strFirstName,
						strLastName, strgender, strDOB, strEmail, strNickName };
				new AddContact().execute(contactParams);
			}
			break;
		case R.id.imgButtonCalendar:
			DialogFragment newFragment = new DatePickerFragment();
			newFragment.show(getSupportFragmentManager(), "datePicker");
			break;
		case R.id.backbtn:
			finish();
			break;
		}
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			// set date picker as current date
			year = 1990;
			month = 0;
			day = 1;
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			Calendar calendar = Calendar.getInstance();
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			int cYear = calendar.get(Calendar.YEAR);

			if (selectedYear <= 1900 || selectedYear >= cYear - 10) {
				Toast.makeText(getActivity(),
						"Year Limit 1900 - " + String.valueOf(cYear - 11),
						Toast.LENGTH_SHORT).show();
			} else {
				updateDisplay();
			}
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

	// display current date
	private static void updateDisplay() {
		// set current date into textview
		edDOB.setText(new StringBuilder()
				.append(day < 10 ? "0" + String.valueOf(day) : day)
				.append("-")
				.append((month + 1) < 10 ? "0" + String.valueOf(month + 1)
						: (month + 1)).append("-") // Month is 0 based, just add
													// 1
				.append(year).append(" "));
		edNickName.requestFocus();
	}

	private boolean validateRegisterFormFields(String strFirstName,
			String strLastName, String strgender, String strDOB,
			String strEmail, String strNickName) {
		validatBuilder = new StringBuilder();
		boolean bValidFields = true;
		if (strFirstName.length() == 0) {
			validatBuilder.append("Please enter first name\n");
			bValidFields = false;
		}
		if (strLastName.length() == 0) {
			validatBuilder.append("Please enter last name\n");
			bValidFields = false;
		}
		if (strNickName.length() == 0) {
			validatBuilder.append("Please enter Nickname\n");
			bValidFields = false;
		}
		if (strEmail.length() > 0) {
			bValidFields = validateEmailId(strEmail);
		} else {
			validatBuilder.append("Please enter email id\n");
			bValidFields = false;
		}
		if (strDOB.length() == 0) {
			validatBuilder.append("Please enter Date of Birth\n");
			bValidFields = false;
		}

		if (!bValidFields) {
			Toast.makeText(getApplicationContext(), validatBuilder.toString(),
					Toast.LENGTH_LONG).show();
		}
		return bValidFields;
	}

	private boolean validateEmailId(String strEmail) {

		return true;
	}

	private class AddContact extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-addContact", " pre execute async");
			mDialog = ProgressDialog.show(RegisterFormActivity.this, "",
					"Adding Contact info. Please wait...", true);

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			try {
				JSONObject json = userFunction.addContact(params[0], params[1],
						params[2], params[3], params[4], params[5], params[6]);

				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					result = json.getString(TAG_DATA);
				} else if (strCode.equalsIgnoreCase("404")
						&& json.getString(TAG_ERRORS).contains(
								"Contact has been already added")) {
					result = "already";
				} else {
					result = "false";
				}

			} catch (IOException e) {
				result = SplashScreenActivity.IO_EXCEPTION;
			} catch (Exception e) {
				e.printStackTrace();
			}
			// remove this
			result = "already";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-addContact", " post execute async");
			mDialog.dismiss();
			if ("true".equalsIgnoreCase(result)
					|| "already".equalsIgnoreCase(result)) {
				if ("already".equalsIgnoreCase(result)) {
					Toast.makeText(getApplicationContext(),
							"Contact info already added!!", Toast.LENGTH_SHORT)
							.show();
				}

				Intent intent = new Intent(RegisterFormActivity.this,
						RegisterConfirmActivity.class);
				intent.putExtra("mobileNumber", strMobileNumber);
				startActivity(intent);
			} else if (SplashScreenActivity.IO_EXCEPTION.equals(result)) {
				Intent intent = new Intent(RegisterFormActivity.this,
						HowdyExceptionActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						"Error Adding contact info", Toast.LENGTH_SHORT).show();
			}

		}
	}
}