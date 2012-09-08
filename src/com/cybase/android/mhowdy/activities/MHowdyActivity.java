package com.cybase.android.mhowdy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.cybase.android.mhowdy.R;

public class MHowdyActivity extends Activity {

	public static final String LOG_TAG = MHowdyActivity.class.getName();
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in
																		// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in
	private LocationManager locationManager;
	private int iLatitude;
	private int iLongitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_home);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!enabled) {
			Log.e(LOG_TAG, "GPS not enabled");
			createGpsDisabledAlert();
		}

		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());

		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {
			updatePreference((int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));
		}
	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			if (location != null) {
				iLatitude = (int) (location.getLatitude() * 1E6);
				iLongitude = (int) (location.getLongitude() * 1E6);
				updatePreference(iLatitude, iLongitude);
			}
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

	private void updatePreference(int iLatitude, int iLongitude) {
		Log.e(LOG_TAG, "Lat::" + iLatitude + "Long::" + iLongitude);
		SharedPreferences settings = getSharedPreferences(
				SplashScreenActivity.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putInt("latitude", iLatitude);
		editor.putInt("longitude", iLongitude);
		// Commit the edits!
		editor.commit();
	}

	private void createGpsDisabledAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your GPS is disabled! can you please enable it?")
				.setCancelable(false)
				.setPositiveButton("Enable GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								showGpsOptions();
							}
						});
		/*
		 * builder.setNegativeButton("Do nothing", new
		 * DialogInterface.OnClickListener(){ public void
		 * onClick(DialogInterface dialog, int id){ dialog.cancel(); } });
		 */
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showGpsOptions() {

		Intent gpsOptionsIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(gpsOptionsIntent);

		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (!provider.contains("gps")) {
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			this.sendBroadcast(poke);
		}

	}

	/*
	 * protected void onActivityResult(int requestCode, int resultCode, Intent
	 * data) { if (requestCode == 0) { if (resultCode == RESULT_OK) { finish();
	 * } } }
	 */

	/*
	 * @Override public void onPause() { super.onPause();
	 * finishRegisterActivity(); }
	 * 
	 * private void finishRegisterActivity() { SharedPreferences mPrefs =
	 * PreferenceManager.getDefaultSharedPreferences(this);
	 * mPrefs.edit().putBoolean("CLOSE_REGISTER_ACTIVITY", true).commit();
	 * finish(); }
	 */
}