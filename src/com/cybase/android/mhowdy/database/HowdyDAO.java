package com.cybase.android.mhowdy.database;

import java.util.ArrayList;

import com.cybase.android.mhowdy.activities.NotificationActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HowdyDAO {
	public static final String LOG_TAG = HowdyDAO.class.getName();
	private SQLiteDatabase database;
	private HowdyDatabase howdyDatabase;
	private String[] allColumns = { HowdyDatabase.COLUMN_ID,
			HowdyDatabase.COLUMN_SPOT_ID, HowdyDatabase.COLUMN_NOTIF_ID,
			HowdyDatabase.COLUMN_MESSAGE, HowdyDatabase.COLUMN_DATE };

	public HowdyDAO(Context context) {
		howdyDatabase = new HowdyDatabase(context);
	}

	public void open() {
		database = howdyDatabase.getWritableDatabase();
	}

	public void close() {
		howdyDatabase.close();
	}

	public ArrayList<MessageVO> addMesage(MessageVO message) {
		ContentValues values = new ContentValues();
		ArrayList<MessageVO> messageList = new ArrayList<MessageVO>();
		long insertId = 0;
		values.put(HowdyDatabase.COLUMN_SPOT_ID, message.getSpotID());
		values.put(HowdyDatabase.COLUMN_NOTIF_ID, message.getNotificationID());
		values.put(HowdyDatabase.COLUMN_MESSAGE, message.getMessage());
		values.put(HowdyDatabase.COLUMN_DATE, message.getCreatedDate());
		insertId = database.insert(HowdyDatabase.TABLE_NAME, null, values);
		if (insertId > 0) {
			messageList = getAllMesages(message.getSpotID());
		}
		return messageList;

	}

	public boolean deleteMessage(String spotID, String notificationID) {
		long insertId = 0;
		insertId = database.delete(HowdyDatabase.TABLE_NAME,
				HowdyDatabase.COLUMN_SPOT_ID + " = " + spotID + " and "
						+ HowdyDatabase.COLUMN_NOTIF_ID + " = "
						+ notificationID, null);
		Log.w(LOG_TAG, "Deleted :insert ID --> " + insertId);
		if (insertId > 0) {
			return true;
		}
		return false;
	}

	public boolean deleteMessages(String spotID, String[] messageIDs) {
		return false;

	}

	public ArrayList<MessageVO> getAllMesages(int spotID) {
		ArrayList<MessageVO> messageList = new ArrayList<MessageVO>();
		Cursor cursor = database.query(HowdyDatabase.TABLE_NAME, allColumns,
				HowdyDatabase.COLUMN_SPOT_ID + " = " + spotID, null, null,
				null, HowdyDatabase.COLUMN_ID + " desc");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MessageVO message = new MessageVO();
			message.setID(cursor.getInt(0));
			message.setSpotID(cursor.getInt(1));
			message.setNotificationID(cursor.getInt(2));
			message.setMessage(cursor.getString(3));
			message.setCreatedDate(cursor.getString(4));
			messageList.add(message);
			cursor.moveToNext();
		}
		cursor.close();
		return messageList;

	}
}
