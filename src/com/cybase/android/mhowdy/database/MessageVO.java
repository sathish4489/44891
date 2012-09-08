package com.cybase.android.mhowdy.database;

import java.sql.Date;

public class MessageVO {
	private int ID;
	private int spotID;
	private int notificationID;
	private String message;
	private String createdDate;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getSpotID() {
		return spotID;
	}
	public void setSpotID(int spotID) {
		this.spotID = spotID;
	}
	public int getNotificationID() {
		return notificationID;
	}
	public void setNotificationID(int notificationID) {
		this.notificationID = notificationID;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	

}
