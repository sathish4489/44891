package com.cybase.android.mhowdy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.data.HowdyConnectionManger;

public class HowdyExceptionActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_exception);
		findViewById(R.id.exception_ok).setOnClickListener(this);
		findViewById(R.id.exception_cancel).setOnClickListener(this);

	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.exception_ok:
			startActivity(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			break;
		case R.id.exception_cancel:
			finish();
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (HowdyConnectionManger.isOnline(this)) {
			finish();
		}
	}

}
