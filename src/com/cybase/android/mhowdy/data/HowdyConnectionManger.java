package com.cybase.android.mhowdy.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HowdyConnectionManger {

	public static boolean isOnline(Context context) {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			isConnected = true;
		}
		return isConnected;
	}
}
