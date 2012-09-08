package com.cybase.android.mhowdy.activities;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.cybase.android.mhowdy.R;

public class ApplicationHomeActivity extends TabActivity {
	public static final String LOG_TAG = ApplicationHomeActivity.class
			.getName();
	public static final String CATEGORY_TAG = "category";
	public static final String NOTIFICATION_TAG = "notification";
	public static final String SETTINGS_TAG = "settings";
	public static final String PREFS_NAME = "AppLaunchPrefFile";
	TabHost tabHost;
	private int tabId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			Log.e(LOG_TAG, "TabID" + bundle.getString("tabid"));
			tabId = Integer.parseInt(bundle.getString("tabid"));
		}
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent = null; // Reusable Intent for each tab

		intent = new Intent().setClass(this, CategoriesHomeActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec(CATEGORY_TAG).setContent(intent);
		spec.setIndicator(customView("Categories",
				R.drawable.tab_image_category_select));
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, NotificationActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = tabHost.newTabSpec(NOTIFICATION_TAG).setContent(intent);
		spec.setIndicator(customView("Notifications",
				R.drawable.tab_image_notification_select));
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, SettingsActivity.class);
		spec = tabHost.newTabSpec(SETTINGS_TAG).setContent(intent);
		spec.setIndicator(customView("Settings",
				R.drawable.tab_image_setting_select));
		tabHost.addTab(spec);

		tabHost.setCurrentTab(tabId);
		// Tab refresh
		tabHost.getTabWidget().getChildAt(0)
				.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						tabHost.setCurrentTab(2);
						tabHost.setCurrentTab(0);
					}
				});
		tabHost.getTabWidget().getChildAt(1)
				.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						tabHost.setCurrentTab(2);
						tabHost.setCurrentTab(1);
					}
				});

	}

	public void showItemDetails(String itemId) {
		tabHost.setCurrentTab(0);
		CategoriesHomeActivity categoriesHomeActivity = (CategoriesHomeActivity) this
				.getCurrentActivity();
		categoriesHomeActivity.getItemDetail(itemId);
	}

	public void goBacktoNotification(String itemId) {
		tabHost.setCurrentTab(1);
		NotificationActivity notificationActivity = (NotificationActivity) this
				.getCurrentActivity();
		notificationActivity.callFromParent(itemId, "parent");
	}

	private void showGpsOptions() {

		Intent gpsOptionsIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(gpsOptionsIntent);

		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		// if (!provider.contains("gps")) {
		// final Intent poke = new Intent();
		// poke.setClassName("com.android.settings",
		// "com.android.settings.widget.SettingsAppWidgetProvider");
		// poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		// poke.setData(Uri.parse("3"));
		// this.sendBroadcast(poke);
		// }

	}

	@Override
	protected void onResume() {
		// Share Location
		// Restore preferences
		super.onResume();
		final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean NWenabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		boolean GPSenabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		Editor edit = settings.edit();
		if (!NWenabled && !GPSenabled) {
			edit.putBoolean("locationAvailable", false);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Your Location is required to determine the best results. Please enable your Network or GPS location service !!")
					.setCancelable(true)
					.setPositiveButton("Share Location",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									showGpsOptions();
								}
							}).setOnCancelListener(new OnCancelListener() {

						public void onCancel(DialogInterface dialog) {
							finish();
						}
					});

			AlertDialog alert = builder.create();
			alert.show();

		} else {

			edit.putBoolean("locationAvailable", true);
		}
		edit.commit();

	}

	private View customView(String text, int imageSrc) {
		View view = LayoutInflater.from(this).inflate(R.layout.sam, null);
		TextView title = (TextView) view.findViewById(R.id.tabsText);
		title.setText(text);
		ImageView image = (ImageView) view.findViewById(R.id.tabsImage);
		image.setBackgroundResource(imageSrc);
		return view;

	}

	/*
	 * private void finishRegisterActivity() { SharedPreferences mPrefs =
	 * PreferenceManager.getDefaultSharedPreferences(this);
	 * mPrefs.edit().putBoolean("CLOSE_REGISTER_ACTIVITY", true).commit(); //
	 * finish(); }
	 */
}