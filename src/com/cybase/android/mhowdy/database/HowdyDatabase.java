package com.cybase.android.mhowdy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HowdyDatabase extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "howdy.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "notifications";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SPOT_ID = "spot_id";
	public static final String COLUMN_NOTIF_ID = "notif_id";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_DATE = "create_date";
	public static final String CREATE_STMT = "create table " + TABLE_NAME + "("
			+ COLUMN_ID + " integer primary key autoincrement ,"
			+ COLUMN_SPOT_ID + " integer," + COLUMN_NOTIF_ID + " integer,"
			+ COLUMN_MESSAGE + " text," + COLUMN_DATE + " text);";

	public HowdyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_STMT);

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(HowdyDatabase.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);

	}

}
