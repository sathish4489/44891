package com.cybase.android.mhowdy.activities;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.data.HowdyConnectionManger;

public class SplashScreenActivity extends FragmentActivity {
	public static final String LOG_TAG = SplashScreenActivity.class.getName();
	public static final String PREFS_NAME = "AppLaunchPrefFile";
	public static final String TAG_DEVICE_ID = "device_id";
	public static final String IO_EXCEPTION = "ioexception";
	public static final String SOCKET_EXCEPTION = "socketexception";
	private Thread mSplashThread;
	boolean notifiEnabled;
	boolean bRegistered;
	SharedPreferences settings;
	private int tabId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			tabId = Integer.parseInt(bundle.getString("tabid"));
		}
		// running process
		// ActivityManager activityManager = (ActivityManager) this
		// .getSystemService(ACTIVITY_SERVICE);
		//
		// List<RunningAppProcessInfo> procInfos = activityManager
		// .getRunningAppProcesses();
		// for (int i = 0; i < procInfos.size(); i++) {
		// Log.e("CES", "processName " + procInfos.get(i).processName);
		// }
		// Restore preferences
		settings = getSharedPreferences(PREFS_NAME, 0);
		bRegistered = settings.getBoolean("registered", false);
		notifiEnabled = settings.getBoolean("notificationsEnabled", true);
		Log.v(LOG_TAG, "notificationsEnabled " + notifiEnabled);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// The thread to wait for splash screen events
		mSplashThread = new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if (HowdyConnectionManger.isOnline(getApplicationContext())) {
					try {
						synchronized (this) {
							// Wait given period of time or exit on touch
							wait(2000);
						}
					} catch (InterruptedException ex) {
						Log.v(LOG_TAG, "Thread InterruptedException");
					}

					finish();
					if (notifiEnabled) {
						registerForNotification();
					}
					if (bRegistered) {
						Log.e(LOG_TAG, "Already Registered");
						Intent intent = new Intent(SplashScreenActivity.this,
								ApplicationHomeActivity.class);
						intent.putExtra("tabid", String.valueOf(tabId));
						startActivity(intent);
					} else {
						// Run next activity
						String deviceId = Secure.getString(
								getContentResolver(), Secure.ANDROID_ID);
						// deviceId = "devs3";
						SharedPreferences.Editor editor = settings.edit();
						editor.putString(TAG_DEVICE_ID, deviceId);
						editor.commit();
						Log.e(LOG_TAG, "Device_ID:" + deviceId);
						Intent intent = new Intent(SplashScreenActivity.this,
								RegisterHomeActivity.class);
						startActivity(intent);

					}
				} else {
					AlertDialog.Builder notificationAlert = new AlertDialog.Builder(
							SplashScreenActivity.this);
					notificationAlert
							.setTitle(getString(R.string.app_nw))
							.setMessage(getString(R.string.app_nw_msg))
							.setPositiveButton("Okay",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											startActivity(new Intent(
													android.provider.Settings.ACTION_WIRELESS_SETTINGS));
										}
									})
							.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											finish();
											dialog.cancel();
										}
									});
					notificationAlert.create().show();
				}
				Looper.loop();
			}
		};
		mSplashThread.start();
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

}