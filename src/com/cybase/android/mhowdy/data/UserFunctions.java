package com.cybase.android.mhowdy.data;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.util.Log;

public class UserFunctions {
	public static final String LOG_TAG = UserFunctions.class.getName();

	private static final String ADD_CONTACT_URL = "http://23.23.212.96/api/v1/Action/add_contact.json?";
	private static final String EDIT_MOBILE_NUMBER_URL = "http://23.23.212.96/api/v1/Action/edit_mobile.json?";
	private static final String REGISTER_URL = "http://23.23.212.96/api/v1/Action/register.json?";
	private static final String VERIFY_URL = "http://23.23.212.96/api/v1/Action/verify.json?";
	private static final String RESEND_VERIFICATION_CODE = "http://23.23.212.96/api/v1/Action/resend_verification.json?";

	// favourites
	private static final String GET_FAVOURITES_URL = "http://23.23.212.96/api/v1/Action/get_favourites.json?";
	private static final String REMOVE_FAVOURITES_URL = "http://23.23.212.96/api/v1/Action/remove_favourite.json?";
	private static final String ADD_FAVOURITES_URL = "http://23.23.212.96/api/v1/Action/add_favourite.json?";

	// categories
	private static final String GET_CATEGORY_URL = "http://23.23.212.96/api/v1/Action/get_categories.json?";
	private static final String GET_CATEGORY_ITEM_URL = "http://23.23.212.96/api/v1/Action/get_items_by_category.json?";
	private static final String GET_CATEGORY_DETAIL_URL = "http://23.23.212.96/api/v1/Action/get_item_details.json?";

	// reviews
	private static final String GET_REVIEWS_URL = "http://23.23.212.96/api/v1/Action/get_reviews.json?";
	private static final String WRITE_REVIEW_URL = "http://23.23.212.96/api/v1/Action/write_review.json?";

	// Search
	private static final String GET_SEARCH_URL = "http://23.23.212.96/api/v1/Action/get_items_by_keyword.json?";

	// Add place
	private static final String ADD_PLACE_URL = "http://23.23.212.96/api/v1/Action/add_item.json?";

	// Notifications
	private static final String ADD_REGISTRATION_ID = "http://23.23.212.96/api/v1/Action/add_registration_id.json?";
	private static final String GET_NOTIFICATIONS = "http://23.23.212.96/api/v1/Action/get_all_notifications.json?";
	private static final String GET_SPOT_NOTIFICATIONS = "http://23.23.212.96/api/v1/Action/get_item_notifications.json?";
	private static final String DELETE_NOTIFICATIONS = "http://23.23.212.96/api/v1/Action/delete_notification.json?";
	private static final String GET_FAV_NOTIFICATIONS = "http://23.23.212.96/api/v1/Action/get_fav_notifications.json?";

	// Settings
	private static final String GET_HELP = "http://23.23.212.96/api/v1/Action/get_help.json";
	private static final String GET_ABOUT_US = "http://23.23.212.96/api/v1/Action/about_us.json";

	private static String TAG_DEVICE_ID = "device_id";
	private static String TAG_DEVICE_TYPE = "mobile_type";
	private static String TAG_ITEM_ID = "item_id";
	private static String TAG_NOTIFICATION_ID = "notification_id";
	private static String TAG_ITEM_NAME = "item_name";
	private static String TAG_DESCRIPTION = "description";
	private static String TAG_ADDRESS = "address";
	private static String TAG_NAME = "name";
	private static String TAG_FIRST_NAME = "first_name";
	private static String TAG_LAST_NAME = "last_name";
	private static String TAG_GENDER = "gender";
	private static String TAG_DOB = "date_of_birth";
	private static String TAG_EMAIL = "email";
	private static String TAG_NICK_NAME = "nick_name";
	private static String TAG_MOBILE_NUMBER = "mobile_number";
	private static String TAG_VERIFICATION_CODE = "verification_code";
	private static String TAG_RADIUS = "radius";
	private static String TAG_CATEGORY_ID = "category_id";
	private static String TAG_LATITUDE = "latitude";
	private static String TAG_LONGITUDE = "longitude";
	private static String TAG_KEYWORD = "keyword";
	private static String TAG_REVIEW = "review";
	private static String TAG_REVIEWER_NAME = "reviewer_name";
	private static String TAG_REVIEWER_STATE = "is_anonymous";
	private static String TAG_REVIEWER_RATING = "rating";
	private static String TAG_REVIEWER_TITLE = "title";
	private static String TAG_REGISTRATION_ID = "registration_id";

	private FetchData fetchData;

	// constructor
	public UserFunctions() {
		fetchData = new FetchData();
	}

	/**
	 * function make addContact request
	 * 
	 * @param device_id
	 * @param firstName
	 * @param lastName
	 * @param gender
	 * @param dob
	 * @param email
	 * @throws Exception
	 * */
	public JSONObject addContact(String device_id, String firstName,
			String lastName, String gender, String dob, String email,
			String nickName) throws Exception {
		StringBuilder sbAddContactUrl = new StringBuilder();
		sbAddContactUrl.append(ADD_CONTACT_URL);
		sbAddContactUrl.append(TAG_DEVICE_ID).append("=").append(device_id);
		sbAddContactUrl.append("&").append(TAG_FIRST_NAME).append("=")
				.append(firstName);
		sbAddContactUrl.append("&").append(TAG_LAST_NAME).append("=")
				.append(lastName);
		sbAddContactUrl.append("&").append(TAG_GENDER).append("=")
				.append(gender);
		sbAddContactUrl.append("&").append(TAG_DOB).append("=").append(dob);
		sbAddContactUrl.append("&").append(TAG_EMAIL).append("=").append(email);
		sbAddContactUrl.append("&").append(TAG_NICK_NAME).append("=")
				.append(nickName);
		Log.v(LOG_TAG, "addContact: URL" + sbAddContactUrl);
		return fetchData.getJSONFromUrl(sbAddContactUrl.toString());
	}

	/**
	 * function make editMobileNumber request
	 * 
	 * @param device_id
	 * @param mobile_number
	 * @throws Exception
	 * */
	public JSONObject editMobileNumber(String device_id, String mobile_number)
			throws Exception {
		StringBuilder sbEditMobileNumberUrl = new StringBuilder();
		sbEditMobileNumberUrl.append(EDIT_MOBILE_NUMBER_URL);
		sbEditMobileNumberUrl.append(TAG_DEVICE_ID).append("=")
				.append(device_id);
		sbEditMobileNumberUrl.append("&").append(TAG_MOBILE_NUMBER).append("=")
				.append(mobile_number);
		Log.v(LOG_TAG, "editMobileNumber: URL" + sbEditMobileNumberUrl);
		return fetchData.getJSONFromUrl(sbEditMobileNumberUrl.toString());
	}

	/**
	 * function make register request
	 * 
	 * @param device_id
	 * @param mobile_number
	 * @throws Exception
	 * */
	public JSONObject register(String device_id, String mobile_number)
			throws Exception {
		StringBuilder sbRegisterUrl = new StringBuilder();
		sbRegisterUrl.append(REGISTER_URL);
		sbRegisterUrl.append(TAG_DEVICE_ID).append("=").append(device_id);
		sbRegisterUrl.append("&").append(TAG_MOBILE_NUMBER).append("=")
				.append(mobile_number);
		sbRegisterUrl.append("&").append(TAG_DEVICE_TYPE).append("=")
				.append("android");
		Log.v(LOG_TAG, "register: URL" + sbRegisterUrl);
		return fetchData.getJSONFromUrl(sbRegisterUrl.toString());
	}

	/**
	 * function resend verification code
	 * 
	 * @param device_id
	 * @param mobile_number
	 * @throws Exception
	 * */
	public JSONObject resendVerifyCode(String device_id, String mobile_number)
			throws Exception {
		StringBuilder sbResendVerifyCode = new StringBuilder();
		sbResendVerifyCode.append(RESEND_VERIFICATION_CODE);
		sbResendVerifyCode.append(TAG_DEVICE_ID).append("=").append(device_id);
		sbResendVerifyCode.append("&").append(TAG_MOBILE_NUMBER).append("=")
				.append(mobile_number);
		Log.v(LOG_TAG, "resend verify code: URL" + sbResendVerifyCode);
		return fetchData.getJSONFromUrl(sbResendVerifyCode.toString());
	}

	/**
	 * function make verify request
	 * 
	 * @param device_id
	 * @param verification_code
	 * @throws Exception
	 * */
	public JSONObject verify(String device_id, String verification_code)
			throws Exception {
		StringBuilder sbVerifyUrl = new StringBuilder();
		sbVerifyUrl.append(VERIFY_URL);
		sbVerifyUrl.append(TAG_DEVICE_ID).append("=").append(device_id);
		sbVerifyUrl.append("&").append(TAG_VERIFICATION_CODE).append("=")
				.append(verification_code);
		Log.v(LOG_TAG, "verify: URL" + sbVerifyUrl);
		return fetchData.getJSONFromUrl(sbVerifyUrl.toString());
	}

	/**
	 * function get Favourites
	 * 
	 * @param deviceId
	 * @throws Exception
	 * */
	public JSONObject getFavourites(String deviceId) throws Exception {
		StringBuilder sbGetFavourites = new StringBuilder();
		sbGetFavourites.append(GET_FAVOURITES_URL);
		sbGetFavourites.append(TAG_DEVICE_ID).append("=").append(deviceId);
		Log.v(LOG_TAG, "get favourites : URL" + sbGetFavourites);
		return fetchData.getJSONFromUrl(sbGetFavourites.toString());
	}

	/**
	 * function remove Favourite
	 * 
	 * @param deviceId
	 * @throws Exception
	 * */
	public JSONObject removeFavourite(String deviceId, String itemId)
			throws Exception {
		StringBuilder sbRemoveFavourite = new StringBuilder();
		sbRemoveFavourite.append(REMOVE_FAVOURITES_URL);
		sbRemoveFavourite.append(TAG_DEVICE_ID).append("=").append(deviceId)
				.append("&");
		sbRemoveFavourite.append(TAG_ITEM_ID).append("=").append(itemId);
		Log.v(LOG_TAG, "remove favourite : URL" + sbRemoveFavourite);
		return fetchData.getJSONFromUrl(sbRemoveFavourite.toString());
	}

	/**
	 * function remove Favourite
	 * 
	 * @param deviceId
	 * @throws Exception
	 * */
	public JSONObject addFavourite(String deviceId, String itemId)
			throws Exception {
		StringBuilder sbAddFavourite = new StringBuilder();
		sbAddFavourite.append(ADD_FAVOURITES_URL);
		sbAddFavourite.append(TAG_DEVICE_ID).append("=").append(deviceId)
				.append("&");
		sbAddFavourite.append(TAG_ITEM_ID).append("=").append(itemId);
		Log.v(LOG_TAG, "add Favourite : URL" + sbAddFavourite);
		return fetchData.getJSONFromUrl(sbAddFavourite.toString());
	}

	/**
	 * function get category menu
	 * 
	 * @param deviceId
	 * @throws Exception
	 * */
	public JSONObject getCategory(String device_id) throws Exception {
		StringBuilder sbGetCategory = new StringBuilder();
		sbGetCategory.append(GET_CATEGORY_URL);
		sbGetCategory.append(TAG_DEVICE_ID).append("=").append(device_id);
		Log.v(LOG_TAG, "get category : URL" + sbGetCategory);
		return fetchData.getJSONFromUrl(sbGetCategory.toString());

	}

	/**
	 * function get category Items
	 * 
	 * @param deviceId
	 * @param catId
	 * @param radius
	 * @param latitude
	 * @param longitude
	 * @throws Exception
	 * */
	public JSONObject getCategoryItems(String deviceId, String catId,
			String radius, String latitude, String longitude) throws Exception {
		StringBuilder sbGetCategoryItem = new StringBuilder();
		sbGetCategoryItem.append(GET_CATEGORY_ITEM_URL);
		sbGetCategoryItem.append(TAG_DEVICE_ID).append("=").append(deviceId)
				.append("&");
		sbGetCategoryItem.append(TAG_CATEGORY_ID).append("=").append(catId)
				.append("&");
		sbGetCategoryItem.append(TAG_RADIUS).append("=").append(radius)
				.append("&");
		sbGetCategoryItem.append(TAG_LATITUDE).append("=").append(latitude)
				.append("&");
		sbGetCategoryItem.append(TAG_LONGITUDE).append("=").append(longitude);
		Log.v(LOG_TAG, "get category item : URL" + sbGetCategoryItem);
		return fetchData.getJSONFromUrl(sbGetCategoryItem.toString());
	}

	/**
	 * function getReviews
	 * 
	 * @param deviceId
	 * @param itemId
	 * @throws Exception
	 * */
	public JSONObject getReviews(String deviceId, String itemId)
			throws Exception {
		StringBuilder sbGetReviews = new StringBuilder();
		sbGetReviews.append(GET_REVIEWS_URL);
		sbGetReviews.append(TAG_DEVICE_ID).append("=").append(deviceId)
				.append("&");
		sbGetReviews.append(TAG_ITEM_ID).append("=").append(itemId);
		Log.v(LOG_TAG, "get Reviews : URL" + sbGetReviews);
		return fetchData.getJSONFromUrl(sbGetReviews.toString());
	}

	/**
	 * function writeReview
	 * 
	 * @param deviceId
	 * @param itemId
	 * @param reviewerName
	 * @param review
	 * @param rating
	 * @param title
	 * @throws Exception
	 * */
	public JSONObject writeReview(String deviceId, String itemId,
			String reviewerName, String review, String rating, String title)
			throws Exception {
		StringBuilder sbWriteReview = new StringBuilder();
		sbWriteReview.append(WRITE_REVIEW_URL);
		sbWriteReview.append(TAG_DEVICE_ID).append("=").append(deviceId)
				.append("&");
		sbWriteReview.append(TAG_ITEM_ID).append("=").append(itemId)
				.append("&");
		// sbWriteReview.append(TAG_REVIEWER_NAME).append("=").append(reviewerName).append("&");
		sbWriteReview.append(TAG_REVIEWER_STATE).append("=")
				.append(reviewerName).append("&");
		sbWriteReview.append(TAG_REVIEW).append("=").append(review).append("&");
		sbWriteReview.append(TAG_REVIEWER_RATING).append("=").append(rating);
		if (title != null) {
			sbWriteReview.append("&").append(TAG_REVIEWER_TITLE).append("=")
					.append(title);
		}
		Log.v(LOG_TAG, "write Review : URL" + sbWriteReview);
		return fetchData.getJSONFromUrl(sbWriteReview.toString());
	}

	/**
	 * function getCategoryDetail
	 * 
	 * @param deviceId
	 * @param itemId
	 * @throws Exception
	 * */
	public JSONObject getCategoryDetail(String deviceId, String itemId)
			throws Exception {
		StringBuilder sbGetCategoryDetail = new StringBuilder();
		sbGetCategoryDetail.append(GET_CATEGORY_DETAIL_URL);
		sbGetCategoryDetail.append(TAG_DEVICE_ID).append("=").append(deviceId)
				.append("&");
		sbGetCategoryDetail.append(TAG_ITEM_ID).append("=").append(itemId);
		Log.v(LOG_TAG, "get Category Detail : URL" + sbGetCategoryDetail);
		return fetchData.getJSONFromUrl(sbGetCategoryDetail.toString());
	}

	/**
	 * function get search items
	 * 
	 * @param deviceId
	 * @param keyword
	 * @throws Exception
	 * */
	public JSONObject getSearchItems(String deviceId, String keyword)
			throws Exception {
		keyword = URLEncoder.encode(keyword);
		StringBuilder sbGetSearchItem = new StringBuilder();
		sbGetSearchItem.append(GET_SEARCH_URL);
		sbGetSearchItem.append(TAG_DEVICE_ID).append("=").append(deviceId)
				.append("&");
		sbGetSearchItem.append(TAG_KEYWORD).append("=").append(keyword);
		Log.v(LOG_TAG, "get search item : URL" + sbGetSearchItem);
		return fetchData.getJSONFromUrl(sbGetSearchItem.toString());
	}

	/**
	 * function addPlace
	 * 
	 * @param deviceId
	 * @param catId
	 * @param itemName
	 * @param latitude
	 * @param longitude
	 * @param address
	 * @throws Exception
	 * */
	public JSONObject addPlace(String deviceId, String catId, String itemName,
			String latitude, String longitude, String address,
			String description) throws Exception {
		StringBuilder sbAddPlace = new StringBuilder();
		sbAddPlace.append(ADD_PLACE_URL);
		sbAddPlace.append(TAG_DEVICE_ID).append("=").append(deviceId)
				.append("&");
		sbAddPlace.append(TAG_CATEGORY_ID).append("=").append(catId)
				.append("&");
		sbAddPlace.append(TAG_ITEM_NAME).append("=").append(itemName)
				.append("&");
		sbAddPlace.append(TAG_DESCRIPTION).append("=").append(description);
		if (latitude != null)
			sbAddPlace.append("&").append(TAG_LATITUDE).append("=")
					.append(latitude);
		if (longitude != null)
			sbAddPlace.append("&").append(TAG_LONGITUDE).append("=")
					.append(longitude);
		if (address != null)
			sbAddPlace.append("&").append(TAG_ADDRESS).append("=")
					.append(address);
		Log.v(LOG_TAG, "Add Place : URL" + sbAddPlace);
		return fetchData.getJSONFromUrl(sbAddPlace.toString());
	}

	/**
	 * function RegisterForNotification
	 * 
	 * @param deviceId
	 * @param registrationId
	 * @throws Exception
	 * */
	public JSONObject registerForNotification(String deviceId,
			String registrationId) throws Exception {
		StringBuilder sbRegisterForNotification = new StringBuilder();
		sbRegisterForNotification.append(ADD_REGISTRATION_ID);
		sbRegisterForNotification.append(TAG_DEVICE_ID).append("=")
				.append(deviceId).append("&");
		sbRegisterForNotification.append(TAG_REGISTRATION_ID).append("=")
				.append(registrationId);
		Log.v(LOG_TAG, "Register For Notification : URL"
				+ sbRegisterForNotification);
		return fetchData.getJSONFromUrl(sbRegisterForNotification.toString());
	}

	/**
	 * function GetNotifications
	 * 
	 * @param deviceId
	 * @param radius
	 * @param latitude
	 * @param longitude
	 * @param page
	 * @throws Exception
	 * */
	public JSONObject getNotifications(String deviceId, String radius,
			String latitude, String longitude, String page) throws Exception {
		StringBuilder sbGetNotifications = new StringBuilder();
		sbGetNotifications.append(GET_NOTIFICATIONS);
		sbGetNotifications.append(TAG_DEVICE_ID).append("=").append(deviceId)
				.append("&");
		sbGetNotifications.append(TAG_RADIUS).append("=").append(radius)
				.append("&");
		sbGetNotifications.append(TAG_LATITUDE).append("=").append(latitude)
				.append("&");
		sbGetNotifications.append("page").append("=").append("1")
		.append("&");
		sbGetNotifications.append(TAG_LONGITUDE).append("=").append(longitude);
		Log.v(LOG_TAG, "getNotifications : URL" + sbGetNotifications);
		return fetchData.getJSONFromUrl(sbGetNotifications.toString());
	}

	/**
	 * function GetFavNotifications
	 * 
	 * @param deviceId
	 * @param page
	 * @throws Exception
	 * */
	public JSONObject getFavNotifications(String deviceId, String page)
			throws Exception {
		StringBuilder sbGetFavNotifications = new StringBuilder();
		sbGetFavNotifications.append(GET_FAV_NOTIFICATIONS);
		sbGetFavNotifications.append(TAG_DEVICE_ID).append("=")
				.append(deviceId);
		Log.v(LOG_TAG, "getFavNotifications : URL" + sbGetFavNotifications);
		return fetchData.getJSONFromUrl(sbGetFavNotifications.toString());
	}

	/**
	 * function DeleteNotifications
	 * 
	 * @param deviceId
	 * @param itemId
	 * @param notificationId
	 * @throws Exception
	 * */
	public JSONObject deleteNotifications(String deviceId, String itemId,
			String notificationId) throws Exception {
		StringBuilder sbDeleteNotifications = new StringBuilder();
		sbDeleteNotifications.append(DELETE_NOTIFICATIONS);
		sbDeleteNotifications.append(TAG_DEVICE_ID).append("=")
				.append(deviceId).append("&");
		sbDeleteNotifications.append(TAG_ITEM_ID).append("=").append(itemId)
				.append("&");
		sbDeleteNotifications.append(TAG_NOTIFICATION_ID).append("=")
				.append(notificationId);
		Log.v(LOG_TAG, "deleteNotifications : URL" + sbDeleteNotifications);
		return fetchData.getJSONFromUrl(sbDeleteNotifications.toString());
	}

	/**
	 * function GetSpotNotifications
	 * 
	 * @param deviceId
	 * @param itemId
	 * @param page
	 * @throws Exception
	 * */
	public JSONObject getSpotNotifications(String deviceId, String itemId,
			String page) throws Exception {
		StringBuilder sbGetSpotNotifications = new StringBuilder();
		sbGetSpotNotifications.append(GET_SPOT_NOTIFICATIONS);
		sbGetSpotNotifications.append(TAG_DEVICE_ID).append("=")
				.append(deviceId).append("&");
		sbGetSpotNotifications.append(TAG_ITEM_ID).append("=").append(itemId);
		Log.v(LOG_TAG, "sbGetSpotNotifications : URL" + sbGetSpotNotifications);
		return fetchData.getJSONFromUrl(sbGetSpotNotifications.toString());
	}

	/**
	 * function GetHelp
	 * 
	 * @throws Exception
	 * */
	public JSONObject getHelp() throws Exception {
		StringBuilder sbGetHelp = new StringBuilder();
		sbGetHelp.append(GET_HELP);
		Log.v(LOG_TAG, "sbGetHelp : URL " + sbGetHelp);
		return fetchData.getJSONFromUrl(sbGetHelp.toString());
	}

	/**
	 * function GetAboutUs
	 * 
	 * @throws Exception
	 * */
	public JSONObject getAboutUs() throws Exception {
		StringBuilder sbGetAboutUs = new StringBuilder();
		sbGetAboutUs.append(GET_ABOUT_US);
		Log.v(LOG_TAG, "sbGetAboutUs : URL " + sbGetAboutUs);
		return fetchData.getJSONFromUrl(sbGetAboutUs.toString());
	}

}