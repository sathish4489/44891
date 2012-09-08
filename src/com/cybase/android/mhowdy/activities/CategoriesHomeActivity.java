package com.cybase.android.mhowdy.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.adapters.CategoryGalleyAdapter;
import com.cybase.android.mhowdy.adapters.CategoryItemAdapter;
import com.cybase.android.mhowdy.adapters.CategoryMenuAdapter;
import com.cybase.android.mhowdy.adapters.CategoryReviewAdapter;
import com.cybase.android.mhowdy.adapters.FavouriteItemAdapter;
import com.cybase.android.mhowdy.adapters.SearchItemAdapter;
import com.cybase.android.mhowdy.animations.SlideAnimation;
import com.cybase.android.mhowdy.data.ImageLoader;
import com.cybase.android.mhowdy.data.UserFunctions;
import com.cybase.android.mhowdy.database.HowdyDAO;
import com.cybase.android.mhowdy.map.LocationOverlay;
import com.cybase.android.mhowdy.map.MapOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class CategoriesHomeActivity extends MapActivity implements
		OnClickListener {
	public static final String LOG_TAG = CategoriesHomeActivity.class.getName();
	ViewFlipper catLayouts;
	ApplicationHomeActivity parent = null;
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat dateFormatDB;
	private RatingBar reviewRatingBar;
	private ListView reviewItems;
	private CheckBox reviewAnonymous;
	private TextView categoryName;
	private TextView reviewTitle;
	private TextView reviewDetail;
	private TextView placeName;
	private TextView currentDate;
	private TextView placeName2;
	private TextView placeName3;
	private ImageView placeImage;
	private TextView placeAddress;
	private TextView placeLocation;
	private TextView placeWebsite;
	private TextView placePhoneNumber;
	private TextView placeCusine;
	private TextView placeMealPrice;
	private TextView placeVegetarian;
	private TextView placeAlcohol;
	private TextView placeDrinkprice;
	private TextView placeUnisex;
	private TextView placeFollowers;
	private TextView placeReviewsCount;
	private TextView placeWriteReviewsText;
	private TextView placeWriteReviewsText2;
	private TextView placeWriteReviewsText3;
	private TextView favouriteAddBtn;
	private EditText searchText;
	private EditText addplaceName;
	private EditText addplaceAddress;
	private EditText addplaceDescription;
	private TextView searchTextCategory;
	private EditText searchTextPlaces;
	private LinearLayout placeRating;
	private LinearLayout placeRating2;
	private LinearLayout placeRating3;
	private LinearLayout placeRating4;
	private LinearLayout addPlaceStrip;
	private ImageLoader imageLoader;
	private int iLatitude;
	private int iLongitude;
	private MapOverlay itemizedoverlay;
	private LocationOverlay locOverlay;
	private MapController mapController;
	private MapView mapView;
	private GridView gridview;
	private ListView catItems;
	private ListView searchItems;
	private ListView favItems;
	private Gallery detailsGallery;
	private Spinner spinner;
	private String spinnerSelectedItemId;
	private String detailsNavCheck = null;
	private String contributeNavCheck = null;
	private String ratingNavCheck = null;
	private GeoPoint selectedGeoPoint;
	private AlertDialog.Builder builder;
	private ProgressBar searchProgressBar;
	private Dialog ratingbarDialog;
	private float writeReviewRating;
	private ArrayList<String> spinnerCategory;
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10;
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000;
	public static final String KEY_GEOMETRY = "geometry";
	public static final String KEY_ICON = "icon";
	public static final String KEY_RATING = "rating";
	public static final String KEY_DESCRIPTION = "desc";
	public static final String KEY_ITEM_ID = "itemId";
	public static final String KEY_ITEM_NAME = "itemName";
	public static final String TAG_CODE = "code";
	public static final String TAG_ERRORS = "errors";
	public static final String TAG_DATA = "data";
	public static final String TAG_SPOT_ID = "SpotID";
	public static final String TAG_SPOT_NAME = "SpotName";
	public static final String TAG_IMAGE = "Image";
	public static final String TAG_DESC = "Description";
	public static final String TAG_DISTANCE = "Distance";
	public static final String TAG_RATING = "Rating";
	public static final String TAG_FAVOURITES = "Favourites";
	public static final String TAG_REVIEW_COUNT = "ReviewsCount";
	public static final String TAG_IS_FAVOURITE = "IsFavourite";
	public static final String TAG_LOCATION = "Location";
	public static final String TAG_WEBSITE = "Website";
	public static final String TAG_PHONE = "PhoneNo";
	public static final String TAG_CUSINE = "cuisine";
	public static final String TAG_MEAL_PRICE = "Meal Price for 2";
	public static final String TAG_VEGETARIAN = "Vegetarian";
	public static final String TAG_ALCOHOL = "Alcohol";
	public static final String TAG_DRINK_PRICE = "Average Drink Cost";
	public static final String TAG_UNISEX = "Unisex";
	public static final String TAG_TITLE = "Title";
	public static final String TAG_REVIEWER_NAME = "ReviewerName";
	public static final String TAG_REVIEW = "Review";
	public static final String TAG_REVIEW_DATE = "ReviewDate";
	public String ProgressTitle = "Please wait...";
	public String ProgressContent = "Retriving Data..";
	LocationManager locationManager;
	ArrayList<HashMap<String, String>> categoryList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> favouritesList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> searchList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> reviewList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> tempCategoryList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> tempPlacesList = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> selectedPlace = null;
	String selectedItemId = null;
	String selectedSpotId = null;
	String deviceId = null;
	HashMap<String, String> selectedPlaceResult = null;
	JSONObject placesObject = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_flipper);

		parent = (ApplicationHomeActivity) this.getParent();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormatDB = new SimpleDateFormat("yyyy-MM-dd");
		catLayouts = (ViewFlipper) findViewById(R.id.cat_flipper);
		gridview = (GridView) findViewById(R.id.cat_grid);
		catItems = (ListView) findViewById(R.id.cat_listitem);
		searchItems = (ListView) findViewById(R.id.search_listitems);
		favItems = (ListView) findViewById(R.id.fav_listitem);
		detailsGallery = (Gallery) findViewById(R.id.cat_detailsgallery);
		reviewItems = (ListView) findViewById(R.id.cat_reviewitem);
		placeName = (TextView) findViewById(R.id.cat_details_name);
		placeName2 = (TextView) findViewById(R.id.cat_details_name2);
		placeName3 = (TextView) findViewById(R.id.cat_details_name3);
		reviewTitle = (TextView) findViewById(R.id.write_review_title);
		reviewDetail = (TextView) findViewById(R.id.write_review_text);
		placeImage = (ImageView) findViewById(R.id.cat_detail_image);
		placeAddress = (TextView) findViewById(R.id.cat_detail_address);
		placeLocation = (TextView) findViewById(R.id.cat_detail_location);
		placeWebsite = (TextView) findViewById(R.id.cat_detail_website);
		placePhoneNumber = (TextView) findViewById(R.id.cat_detail_phone);
		placeMealPrice = (TextView) findViewById(R.id.cat_detail_meal_price);
		placeCusine = (TextView) findViewById(R.id.cat_detail_cuisine);
		placeAlcohol = (TextView) findViewById(R.id.cat_detail_alcohol);
		placeVegetarian = (TextView) findViewById(R.id.cat_detail_vegetarian);
		placeDrinkprice = (TextView) findViewById(R.id.cat_detail_drink_price);
		placeUnisex = (TextView) findViewById(R.id.cat_detail_unisex);
		placeFollowers = (TextView) findViewById(R.id.cat_detail_followers);
		placeReviewsCount = (TextView) findViewById(R.id.cat_detail_reviewscount);
		placeWriteReviewsText = (TextView) findViewById(R.id.cat_read_review_text);
		placeWriteReviewsText2 = (TextView) findViewById(R.id.cat_read_review_text2);
		placeWriteReviewsText3 = (TextView) findViewById(R.id.cat_read_review_text3);
		searchText = (EditText) findViewById(R.id.addplace_searchtext);
		addplaceName = (EditText) findViewById(R.id.addplace_name);
		addplaceAddress = (EditText) findViewById(R.id.addplace_address);
		addplaceDescription = (EditText) findViewById(R.id.addplace_description);
		searchTextCategory = (TextView) findViewById(R.id.searchtext_category_home);
		searchTextPlaces = (EditText) findViewById(R.id.searchtext_category_item);
		placeRating = (LinearLayout) findViewById(R.id.cat_detail_rating);
		placeRating2 = (LinearLayout) findViewById(R.id.cat_detail_rating2);
		placeRating3 = (LinearLayout) findViewById(R.id.cat_detail_rating3);
		placeRating4 = (LinearLayout) findViewById(R.id.review_ratingbar);
		addPlaceStrip = (LinearLayout) findViewById(R.id.cat_contibute2);
		currentDate = (TextView) findViewById(R.id.review_current_date);
		favouriteAddBtn = (TextView) findViewById(R.id.cat_fav_addbtn);
		reviewAnonymous = (CheckBox) findViewById(R.id.chkAnonymous);
		imageLoader = new ImageLoader(getApplicationContext());
		ratingbarDialog = new Dialog(this);
		ratingbarDialog.setContentView(R.layout.custom_rating);
		reviewRatingBar = (RatingBar) ratingbarDialog
				.findViewById(R.id.review_custom_rating);
		reviewRatingBar.setMinimumWidth(reviewRatingBar.getWidth());
		searchProgressBar = (ProgressBar) findViewById(R.id.search_progress);
		// Location components
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new GeoUpdateHandler());
		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {
			updatePreference((int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));
		}
		selectedGeoPoint = null;
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapView.setStreetView(true);
		mapController = mapView.getController();
		mapController.setZoom(15);

		// get device id
		SharedPreferences settings = getSharedPreferences(
				SplashScreenActivity.PREFS_NAME, 0);
		deviceId = settings
				.getString(SplashScreenActivity.TAG_DEVICE_ID, "n/a");
		// Favourite remove alert box
		builder = new AlertDialog.Builder(this);
		// load categories
		String[] params = { "fetchCategories", "" };
		new FetchCategory().execute(params);

		hideKeyboard();
		catLayouts.setDisplayedChild(0);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				catLayouts.setInAnimation(SlideAnimation.inFromRightAnimation());
				catLayouts.setOutAnimation(SlideAnimation.outToLeftAnimation());
				String typeName = (String) ((TextView) view
						.findViewById(R.id.grid_item_label)).getText();
				categoryName = (TextView) CategoriesHomeActivity.this
						.findViewById(R.id.cat_category_name);
				categoryName.setText(typeName.trim().replace("_", " "));
				// Adych task to load items
				selectedSpotId = tempCategoryList.get(position)
						.get(TAG_SPOT_ID);
				String[] params = { "fetchTypes", selectedSpotId };
				new FetchCategory().execute(params);

				hideKeyboard();
				catLayouts.setDisplayedChild(1);

			}

		});
		catItems.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				catLayouts.setInAnimation(SlideAnimation.inFromRightAnimation());
				catLayouts.setOutAnimation(SlideAnimation.outToLeftAnimation());
				selectedPlace = tempPlacesList.get(position);
				selectedItemId = selectedPlace.get(TAG_SPOT_ID);
				detailsNavCheck = "fromCategory";
				String[] params = { selectedItemId };
				new categoryDetail().execute(params);
				hideKeyboard();
				catLayouts.setDisplayedChild(3);

			}

		});
		favItems.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				catLayouts.setInAnimation(SlideAnimation.inFromRightAnimation());
				catLayouts.setOutAnimation(SlideAnimation.outToLeftAnimation());
				selectedPlace = favouritesList.get(position);
				selectedItemId = selectedPlace.get(TAG_SPOT_ID);
				detailsNavCheck = "fromFavorites";
				String[] params = { selectedItemId };
				new categoryDetail().execute(params);
				hideKeyboard();
				catLayouts.setDisplayedChild(3);

			}

		});
		favItems.setLongClickable(true);
		favItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long point) {
				AlertDialog dialogBox = favouriteRemoveAlertBox(
						favouritesList.get(position), "false");
				dialogBox.show();
				return true;
			}
		});
		searchItems.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				catLayouts.setInAnimation(SlideAnimation.inFromRightAnimation());
				catLayouts.setOutAnimation(SlideAnimation.outToLeftAnimation());
				selectedPlace = searchList.get(position);
				selectedItemId = selectedPlace.get(TAG_SPOT_ID);
				detailsNavCheck = "fromSearch";
				String[] params = { selectedItemId };
				new categoryDetail().execute(params);
				hideKeyboard();
				catLayouts.setDisplayedChild(3);
			}
		});
		searchText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable searchVal) {

			}
		});

		searchTextPlaces.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable searchVal) {
				tempPlacesList = new ArrayList<HashMap<String, String>>();
				for (HashMap<String, String> category : placesList) {
					if (category.get(TAG_SPOT_NAME).toLowerCase()
							.contains(searchVal.toString().toLowerCase())) {
						tempPlacesList.add(category);
					}
				}
				catItems.setAdapter(new CategoryItemAdapter(
						getApplicationContext(), tempPlacesList));
			}
		});
		findViewById(R.id.review_ratingbar).setOnClickListener(this);
		findViewById(R.id.favourite_link).setOnClickListener(this);
		findViewById(R.id.cat_nav_favback).setOnClickListener(this);
		findViewById(R.id.cat_nav_catback).setOnClickListener(this);
		findViewById(R.id.cat_nav_detailback).setOnClickListener(this);
		findViewById(R.id.cat_nav_reviewback).setOnClickListener(this);
		findViewById(R.id.cat_read_review).setOnClickListener(this);
		findViewById(R.id.cat_write_a_review).setOnClickListener(this);
		findViewById(R.id.cat_nav_writereviewback).setOnClickListener(this);
		findViewById(R.id.cat_nav_writereviewsend).setOnClickListener(this);
		findViewById(R.id.cat_contibute).setOnClickListener(this);
		findViewById(R.id.cat_nav_addplaceback).setOnClickListener(this);
		findViewById(R.id.cat_contibute2).setOnClickListener(this);
		findViewById(R.id.cat_nav_addplace2back).setOnClickListener(this);
		findViewById(R.id.cat_addplace_add).setOnClickListener(this);
		findViewById(R.id.cat_locateonmap).setOnClickListener(this);
		findViewById(R.id.cat_nav_mapback).setOnClickListener(this);
		findViewById(R.id.searchtext_category_home).setOnClickListener(this);
		findViewById(R.id.cat_tell_a_friend).setOnClickListener(this);
		findViewById(R.id.addplace_searchtext_btn).setOnClickListener(this);
		// flow v1.3 update
		// findViewById(R.id.cat_nav_mapdone).setOnClickListener(this);
		ratingbarDialog.findViewById(R.id.ratingbar_ok)
				.setOnClickListener(this);
		ratingbarDialog.findViewById(R.id.ratingbar_cancel).setOnClickListener(
				this);
		favouriteAddBtn.setOnClickListener(this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		locationManager.removeUpdates(new GeoUpdateHandler());
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(catLayouts.getWindowToken(), 0);
	}

	private AlertDialog favouriteRemoveAlertBox(
			final HashMap<String, String> selectedFavItem,
			final String fromDetail) {
		// Favourite remove alert box
		builder.setMessage(
				"Do you want to remove "
						+ selectedFavItem.get(TAG_SPOT_NAME).toString()
						+ " from your Favourites ?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								String[] params = {
										selectedFavItem.get(TAG_SPOT_ID)
												.toString(), fromDetail };
								ProgressTitle = "Please wait...";
								ProgressContent = "Removing "
										+ selectedFavItem.get(TAG_SPOT_NAME)
												.toString()
										+ " from Favourites";
								new removeFavourite().execute(params);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		return alert;
	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.favourite_link:// Displays favourite view
			catLayouts.setInAnimation(SlideAnimation.inFromRightAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToLeftAnimation());
			new getFavourites().execute();
			break;
		case R.id.cat_nav_favback:
			catLayouts.setInAnimation(SlideAnimation.inFromLeftAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToRightAnimation());
			hideKeyboard();
			catLayouts.setDisplayedChild(0);// Displays category view
			break;
		case R.id.cat_nav_catback:
			catLayouts.setInAnimation(SlideAnimation.inFromLeftAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToRightAnimation());
			hideKeyboard();
			catLayouts.setDisplayedChild(0);// Displays category view
			break;
		case R.id.cat_nav_detailback:// Displays list view
			catLayouts.setInAnimation(SlideAnimation.inFromLeftAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToRightAnimation());
			hideKeyboard();
			if (null == detailsNavCheck) {
				catLayouts.setDisplayedChild(0);
			} else if ("fromCategory".equalsIgnoreCase(detailsNavCheck)) {
				catLayouts.setDisplayedChild(1);
				String[] params = { "fetchTypes", selectedSpotId };
				new FetchCategory().execute(params);
			} else if ("fromFavorites".equalsIgnoreCase(detailsNavCheck)) {
				new getFavourites().execute();
			} else if ("fromSearch".equalsIgnoreCase(detailsNavCheck)) {
				catLayouts.setDisplayedChild(6);
			} else if ("fromNotif".equalsIgnoreCase(detailsNavCheck)) {
				goBacktoNotification(selectedItemId);
			}
			break;
		case R.id.cat_nav_reviewback:
			catLayouts.setInAnimation(SlideAnimation.inFromLeftAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToRightAnimation());
			hideKeyboard();
			catLayouts.setDisplayedChild(3);// Displays details view
			break;
		case R.id.cat_read_review:// Displays read Review
			currentDate.setText("as on "
					+ dateFormat.format(Calendar.getInstance().getTime()));
			if (selectedItemId != null) {
				String[] getReviewsParams = { selectedItemId, null };
				new getReviews().execute(getReviewsParams);
			}
			break;
		case R.id.cat_write_a_review:// Displays write a review
			writeReviewRating = 0f;
			reviewAnonymous.setChecked(false);
			ratingLoader(writeReviewRating, placeRating4);
			catLayouts.setInAnimation(SlideAnimation.inFromRightAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToLeftAnimation());
			hideKeyboard();
			catLayouts.setDisplayedChild(5);
			break;
		case R.id.cat_nav_writereviewback:// Displays details view
			reviewTitle.setText(null);
			reviewDetail.setText(null);
			writeReviewRating = 0f;
			catLayouts.setInAnimation(SlideAnimation.inFromLeftAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToRightAnimation());
			hideKeyboard();
			catLayouts.setDisplayedChild(4);
			break;
		case R.id.cat_nav_writereviewsend:// Displays details view
			StringBuilder reviewErr = new StringBuilder();
			String anonymous = "0";
			if (reviewAnonymous.isChecked()) {
				anonymous = "0";
			} else {
				anonymous = "1";
			}
			if (reviewTitle.getText().length() == 0) {
				reviewErr.append("Enter Review Title\n");
			}
			if (reviewDetail.getText().length() == 0) {
				reviewErr.append("Enter Review (1000 Characters)");
			}
			if (reviewErr.length() == 0) {
				String[] writeReviewParams = { selectedItemId,
						URLEncoder.encode(anonymous),
						URLEncoder.encode(reviewDetail.getText().toString()),
						String.valueOf(writeReviewRating),
						URLEncoder.encode(reviewTitle.getText().toString()) };
				new writeReview().execute(writeReviewParams);
			} else {

				Toast.makeText(getApplicationContext(), reviewErr.toString(),
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.cat_contibute:// Displays add place page 2 view
			catLayouts.setInAnimation(SlideAnimation.inFromRightAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToLeftAnimation());
			contributeNavCheck = "category";
			selectedGeoPoint = null;
			hideKeyboard();
			catLayouts.setDisplayedChild(7);
			break;
		case R.id.cat_nav_addplaceback:// Displays category list view
			catLayouts.setInAnimation(SlideAnimation.inFromLeftAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToRightAnimation());
			hideKeyboard();
			catLayouts.setDisplayedChild(0);
			break;
		case R.id.cat_contibute2:// Displays add place 2 page
			catLayouts.setInAnimation(SlideAnimation.inFromRightAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToLeftAnimation());
			contributeNavCheck = "search";
			selectedGeoPoint = null;
			addplaceName.setText(searchText.getText());
			hideKeyboard();
			catLayouts.setDisplayedChild(7);
			break;
		case R.id.cat_nav_addplace2back:// Displays item list view
			addplaceName.setText(null);
			catLayouts.setInAnimation(SlideAnimation.inFromLeftAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToRightAnimation());
			hideKeyboard();
			if (contributeNavCheck != null
					&& "category".equalsIgnoreCase(contributeNavCheck)) {
				catLayouts.setDisplayedChild(1);
				String[] params = { "fetchTypes", selectedSpotId };
				new FetchCategory().execute(params);
			} else if (contributeNavCheck != null
					&& "search".equalsIgnoreCase(contributeNavCheck)) {
				catLayouts.setDisplayedChild(6);
			} else {
				catLayouts.setDisplayedChild(0);
			}

			break;
		case R.id.cat_addplace_add:// Displays add place page 1 view
			// flow v1.3 update start
			Log.e(" locOverlay overlay size ",
					String.valueOf(itemizedoverlay.size()));
			if (locOverlay.size() == 1) {
				selectedGeoPoint = locOverlay.getItem(0).getPoint();
			} else {
				selectedGeoPoint = null;
			}
			// flow v1.3 update end
			String placeName = addplaceName.getText().toString();
			String placeAddress = addplaceAddress.getText().toString();
			String placeDescription = addplaceDescription.getText().toString();
			String selLatitude = null;
			String selLogitude = null;
			if (selectedGeoPoint != null) {
				selLatitude = String
						.valueOf(selectedGeoPoint.getLatitudeE6() / 1E6);
				selLogitude = String
						.valueOf(selectedGeoPoint.getLongitudeE6() / 1E6);
			}
			// validations
			if (placeName != null && placeName.length() > 0) {
				if (placeAddress != null && placeAddress.length() > 0) {
					placeAddress = URLEncoder.encode(placeAddress);
				} else {
					placeAddress = null;
				}
				if (placeDescription != null && placeDescription.length() > 0) {
					placeDescription = URLEncoder.encode(placeDescription);
					placeName = URLEncoder.encode(placeName);
					String[] addPlaceParams = { placeName,
							spinnerSelectedItemId, selLatitude, selLogitude,
							placeAddress, placeDescription };
					new addPlace().execute(addPlaceParams);
				} else {
					Toast.makeText(getApplicationContext(),
							"Place Description Required", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "Place Name Required",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.cat_locateonmap:
			if (null == selectedGeoPoint) {
				Drawable drawable = this.getResources().getDrawable(
						R.drawable.marker);
				Drawable drawableLocation = this.getResources().getDrawable(
						R.drawable.gpin);
				itemizedoverlay = new MapOverlay(this, drawable);
				locOverlay = new LocationOverlay(this, drawableLocation);
				SharedPreferences settings = getSharedPreferences(
						SplashScreenActivity.PREFS_NAME, 0);
				iLatitude = settings.getInt("latitude", (int) (13.0741 * 1E6));
				iLongitude = settings
						.getInt("longitude", (int) (80.2424 * 1E6));
				Log.e(LOG_TAG, "Lat::" + iLatitude + "Long::" + iLongitude);
				GeoPoint point = new GeoPoint(iLatitude, iLongitude);
				OverlayItem overlayitem = new OverlayItem(point, "Location",
						"snippet");
				itemizedoverlay.addOverlay(overlayitem);
				mapView.getOverlays().clear();
				mapView.getOverlays().add(itemizedoverlay);
				mapView.getOverlays().add(locOverlay);
				mapController.animateTo(point);
			}
			catLayouts.setInAnimation(SlideAnimation.inFromRightAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToLeftAnimation());
			hideKeyboard();
			catLayouts.setDisplayedChild(8);// Displays locate on map page
			break;
		case R.id.cat_nav_mapback:
			selectedGeoPoint = null;
			catLayouts.setInAnimation(SlideAnimation.inFromLeftAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToRightAnimation());
			hideKeyboard();
			catLayouts.setDisplayedChild(7);// Displays add place page 1 view
			break;
		// flow v1.3 update start
		// case R.id.cat_nav_mapdone:
		// Log.e(" locOverlay overlay size ",
		// String.valueOf(itemizedoverlay.size()));
		// if (locOverlay.size() == 1) {
		// selectedGeoPoint = locOverlay.getItem(0).getPoint();
		// } else {
		// selectedGeoPoint = null;
		// }
		// catLayouts.setInAnimation(SlideAnimation.inFromLeftAnimation());
		// catLayouts.setOutAnimation(SlideAnimation.outToRightAnimation());
		// catLayouts.setDisplayedChild(7);// Displays add place page 1 view
		// break;
		// flow v1.3 update end
		case R.id.review_ratingbar:
			reviewRatingBar.setRating(0f);
			ratingbarDialog.show();
			break;
		case R.id.ratingbar_ok:
			Log.v("Ratingbar", String.valueOf(reviewRatingBar.getRating()));
			ratingLoader(reviewRatingBar.getRating(), placeRating4);
			writeReviewRating = reviewRatingBar.getRating();
			ratingbarDialog.dismiss();
			break;
		case R.id.ratingbar_cancel:
			ratingbarDialog.dismiss();
			break;
		case R.id.cat_fav_addbtn:
			if (favouriteAddBtn.getText().toString()
					.equalsIgnoreCase(getString(R.string.cat_fav_add))) {
				String[] addFavParams = { null };
				new addFavourite().execute(addFavParams);
			} else {
				AlertDialog dialogBox = favouriteRemoveAlertBox(
						selectedPlaceResult, "true");
				dialogBox.show();
			}
			break;
		case R.id.searchtext_category_home:
			catLayouts.setInAnimation(SlideAnimation.inFromRightAnimation());
			catLayouts.setOutAnimation(SlideAnimation.outToLeftAnimation());
			searchList.clear();
			searchItems.setAdapter(new SearchItemAdapter(
					getApplicationContext(), searchList));
			addPlaceStrip.setVisibility(View.VISIBLE);
			hideKeyboard();
			catLayouts.setDisplayedChild(6);
			break;
		case R.id.cat_tell_a_friend:
			Intent sharingIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			StringBuilder shareBody = new StringBuilder();
			shareBody.append(selectedPlaceResult.get(TAG_SPOT_NAME));
			if (selectedPlaceResult.get(TAG_WEBSITE) != null
					&& selectedPlaceResult.get(TAG_WEBSITE).length() > 0) {
				shareBody.append(" : " + selectedPlaceResult.get(TAG_WEBSITE));
			}
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					shareBody.toString());
			startActivity(Intent.createChooser(sharingIntent, "Tell a Friend"));
			break;
		case R.id.addplace_searchtext_btn:
			String[] params = { "searchItems", searchText.getText().toString() };
			new searchProgress().execute(params);
			hideKeyboard();
			break;
		}

	}

	private void ratingLoader(float rating, LinearLayout item) {
		item.removeAllViews();
		int resourceName = item.getId();
		int wholeVal = (int) rating;
		int decimalVal = (int) ((rating - wholeVal) * 1E2);
		boolean halfSet = false;
		if (decimalVal < 60 && decimalVal > 0) {
			halfSet = true;
		}
		int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
						.getDisplayMetrics());
		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				20, getResources().getDisplayMetrics());

		if (R.id.cat_detail_rating == resourceName) {
			size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
					20, getResources().getDisplayMetrics());

		} else if (R.id.review_ratingbar == resourceName) {
			size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
					25, getResources().getDisplayMetrics());
		} else if (R.id.cat_detail_rating2 == resourceName
				|| R.id.cat_detail_rating3 == resourceName) {
			size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
					15, getResources().getDisplayMetrics());
		}

		Log.d("raiting ", "wholeVal " + wholeVal + "decimalVal" + decimalVal
				+ "halfSet" + halfSet + "size" + size + "resourceName "
				+ resourceName);

		for (int count = 1; count <= 5; count++) {
			LayoutParams params = new LayoutParams(size, size);
			params.setMargins(margin, margin, margin, margin);
			ImageView view = new ImageView(getApplicationContext());
			view.setLayoutParams(params);
			if (count <= wholeVal) {
				view.setBackgroundResource(R.drawable.star);
			} else if (count == wholeVal + 1 && halfSet) {
				view.setBackgroundResource(R.drawable.starhalf);
			} else {
				view.setBackgroundResource(R.drawable.staroff);
			}

			item.addView(view);
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void updatePreference(int iLatitude, int iLongitude) {
		SharedPreferences settings = getSharedPreferences(
				SplashScreenActivity.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		Log.e(LOG_TAG, "Lat::" + iLatitude + "Long::" + iLongitude);
		editor.putInt("latitude", iLatitude);
		editor.putInt("longitude", iLongitude);
		// Commit the edits!
		editor.commit();
	}

	private class GeoUpdateHandler implements LocationListener {

		public void onLocationChanged(Location location) {
			if (location != null) {
				iLatitude = (int) (location.getLatitude() * 1E6);
				iLongitude = (int) (location.getLongitude() * 1E6);
				updatePreference(iLatitude, iLongitude);
			}
			GeoPoint point = new GeoPoint(iLatitude, iLongitude);
			Drawable drawable = CategoriesHomeActivity.this.getResources()
					.getDrawable(R.drawable.marker);
			itemizedoverlay = new MapOverlay(CategoriesHomeActivity.this,
					drawable);
			OverlayItem overlayitem = new OverlayItem(point, "Location",
					"snippet");
			itemizedoverlay.addOverlay(overlayitem);
			mapView.getOverlays().clear();
			mapView.getOverlays().add(itemizedoverlay);
			mapView.getOverlays().add(locOverlay);
			// mapController.animateTo(point);

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

	private class FetchCategory extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress", " pre execute async");
			mDialog = ProgressDialog.show(CategoriesHomeActivity.this, "",
					"Fetching details.. Please wait...", true);
			placesList = new ArrayList<HashMap<String, String>>();
			catItems.setAdapter(new CategoryItemAdapter(
					getApplicationContext(), placesList));

		}

		@Override
		protected String doInBackground(String... params) {
			JSONObject json = null;
			JSONArray jsonArray = null;
			String method = params[0];
			if ("fetchCategories".equalsIgnoreCase(method)) {
				categoryList = new ArrayList<HashMap<String, String>>();
				try {
					json = userFunction.getCategory(deviceId);

					String strCode = json.getString(TAG_CODE);
					if (strCode.equalsIgnoreCase("200")) {
						jsonArray = null;
						jsonArray = json.getJSONArray(TAG_DATA);
						if (jsonArray != null) {
							for (int ObjCount = 0; ObjCount < jsonArray
									.length(); ObjCount++) {
								HashMap<String, String> categoryItem = new HashMap<String, String>();
								categoryItem.put(TAG_SPOT_ID,
										((JSONObject) jsonArray.get(ObjCount))
												.getString(TAG_SPOT_ID));
								categoryItem.put(TAG_SPOT_NAME,
										((JSONObject) jsonArray.get(ObjCount))
												.getString(TAG_SPOT_NAME));
								categoryList.add(categoryItem);

							}
						}
					} else if (strCode.equalsIgnoreCase("404")) {
						jsonArray = null;
						jsonArray = json.getJSONArray(TAG_ERRORS);
						method = jsonArray.getString(0).toString();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if ("fetchTypes".equalsIgnoreCase(method)) {
				// fetchTypes(params[1]);
				placesList = new ArrayList<HashMap<String, String>>();
				SharedPreferences settings = getSharedPreferences(
						SplashScreenActivity.PREFS_NAME, 0);
				Float fLatitude = (float) (Float.valueOf(settings.getInt(
						"latitude", (int) (13.0741 * 1E6))) / 1E6);
				Float fLongitude = (float) (Float.valueOf(settings.getInt(
						"longitude", (int) (80.2424 * 1E6))) / 1E6);
				try {
					json = userFunction.getCategoryItems(deviceId, params[1],
							"10", String.valueOf(fLatitude),
							String.valueOf(fLongitude));

					String strCode = json.getString(TAG_CODE);
					if (strCode.equalsIgnoreCase("200")) {
						jsonArray = null;
						jsonArray = json.getJSONArray(TAG_DATA);
						if (jsonArray != null) {
							for (int ObjCount = 0; ObjCount < jsonArray
									.length(); ObjCount++) {
								HashMap<String, String> placeItem = new HashMap<String, String>();
								placeItem.put(TAG_SPOT_ID,
										((JSONObject) jsonArray.get(ObjCount))
												.getString(TAG_SPOT_ID));
								placeItem.put(TAG_SPOT_NAME,
										((JSONObject) jsonArray.get(ObjCount))
												.getString(TAG_SPOT_NAME));
								placeItem.put(TAG_IMAGE,
										((JSONObject) jsonArray.get(ObjCount))
												.getString(TAG_IMAGE));
								placeItem.put(TAG_DESC, ((JSONObject) jsonArray
										.get(ObjCount)).getString(TAG_DESC));
								if (((JSONObject) jsonArray.get(ObjCount))
										.getString(TAG_DISTANCE).length() > 0) {
									String distance = ((JSONObject) jsonArray
											.get(ObjCount)).getString(
											TAG_DISTANCE).substring(0, 5);
									placeItem.put(TAG_DISTANCE,
											String.valueOf(distance) + " km");
								} else {
									placeItem.put(TAG_DISTANCE, "-- km");
								}

								if (((JSONObject) jsonArray.get(ObjCount))
										.getString(TAG_RATING).length() > 0) {
									placeItem.put(TAG_RATING,
											((JSONObject) jsonArray
													.get(ObjCount))
													.getString(TAG_RATING));
								} else {
									placeItem.put(TAG_RATING, "0");
								}

								placesList.add(placeItem);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return method;
		}

		protected void onPostExecute(String method) {
			if ("fetchCategories".equalsIgnoreCase(method)) {
				tempCategoryList = categoryList;
				gridview.setAdapter(new CategoryMenuAdapter(
						getApplicationContext(), categoryList));
				// add place spinner
				spinner = (Spinner) findViewById(R.id.addplace_spinner);
				spinnerCategory = new ArrayList<String>();
				for (HashMap<String, String> cats : categoryList) {
					spinnerCategory.add(cats.get(TAG_SPOT_NAME));
				}
				ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(
						CategoriesHomeActivity.this,
						android.R.layout.simple_spinner_item, spinnerCategory);
				adapterCategory
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapterCategory);
				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long item) {
						// TODO Auto-generated method stub
						spinnerSelectedItemId = categoryList.get(position)
								.get(TAG_SPOT_ID).trim();
					}

					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
					}
				});
			} else if ("fetchTypes".equalsIgnoreCase(method)) {
				searchTextPlaces.setText(null);
				tempPlacesList = placesList;
				catItems.setAdapter(new CategoryItemAdapter(
						getApplicationContext(), placesList));
			} else if (method.contains("Device id is not registered.")) {
				SharedPreferences mPrefs = PreferenceManager
						.getDefaultSharedPreferences(CategoriesHomeActivity.this);
				mPrefs.edit().putBoolean("CLOSE_REGISTER_ACTIVITY", false)
						.commit();
				finish();
				Intent intent = new Intent(CategoriesHomeActivity.this,
						RegisterHomeActivity.class);

				SharedPreferences settings = getSharedPreferences(
						SplashScreenActivity.PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("registered", false);
				// Commit the edits!
				editor.commit();
				startActivity(intent);
			}
			Log.d("progress", " post execute async");
			mDialog.dismiss();
		}

	}

	private class searchProgress extends AsyncTask<String, Void, String> {
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress", " pre execute async");
			searchProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				searchList.clear();
				JSONObject json = userFunction.getSearchItems(deviceId,
						params[1]);
				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					JSONArray jsonArray = null;
					jsonArray = json.getJSONArray(TAG_DATA);
					if (jsonArray != null) {
						searchList.clear();
						for (int ObjCount = 0; ObjCount < jsonArray.length(); ObjCount++) {
							HashMap<String, String> placeItem = new HashMap<String, String>();
							placeItem.put(TAG_SPOT_ID, ((JSONObject) jsonArray
									.get(ObjCount)).getString(TAG_SPOT_ID));
							placeItem.put(TAG_SPOT_NAME,
									((JSONObject) jsonArray.get(ObjCount))
											.getString(TAG_SPOT_NAME));
							placeItem.put(TAG_IMAGE, ((JSONObject) jsonArray
									.get(ObjCount)).getString(TAG_IMAGE));
							placeItem.put(TAG_DESC, ((JSONObject) jsonArray
									.get(ObjCount)).getString(TAG_DESC));
							if (((JSONObject) jsonArray.get(ObjCount))
									.getString(TAG_RATING).length() > 0) {
								placeItem.put(TAG_RATING,
										((JSONObject) jsonArray.get(ObjCount))
												.getString(TAG_RATING));
							} else {
								placeItem.put(TAG_RATING, "0");
							}

							searchList.add(placeItem);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress", " post execute async");
			if (searchList != null && searchList.size() > 0
					&& searchText.getText() != null
					&& searchText.getText().toString().length() > 0) {
				addPlaceStrip.setVisibility(View.GONE);
			} else {
				searchList.clear();
				addPlaceStrip.setVisibility(View.VISIBLE);
			}
			searchItems.setAdapter(new SearchItemAdapter(
					getApplicationContext(), searchList));
			searchProgressBar.setVisibility(View.INVISIBLE);
		}

	}

	private class addPlace extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-add place", " pre execute async");
			mDialog = ProgressDialog.show(CategoriesHomeActivity.this, "",
					"Adding Place.. Please wait...", true);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			try {
				JSONObject json = userFunction.addPlace(deviceId, params[1],
						params[0], params[2], params[3], params[4], params[5]);
				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					result = json.getString(TAG_DATA);
				} else if (strCode.equalsIgnoreCase("404")) {
					result = "false";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-add place", " post execute async");
			mDialog.dismiss();
			if ("true".equals(result)) {
				addplaceAddress.setText(null);
				addplaceName.setText(null);
				addplaceDescription.setText(null);
				selectedGeoPoint = null;
				catLayouts.setInAnimation(SlideAnimation.inFromLeftAnimation());
				catLayouts
						.setOutAnimation(SlideAnimation.outToRightAnimation());
				hideKeyboard();
				if (contributeNavCheck != null
						&& "category".equalsIgnoreCase(contributeNavCheck)) {
					catLayouts.setDisplayedChild(1);
					String[] params = { "fetchTypes", selectedSpotId };
					new FetchCategory().execute(params);
				} else if (contributeNavCheck != null
						&& "search".equalsIgnoreCase(contributeNavCheck)) {
					catLayouts.setDisplayedChild(6);
				} else {
					catLayouts.setDisplayedChild(0);
				}
				Toast.makeText(getApplicationContext(),
						"Place added successfully", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "Error adding Place",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	private class categoryDetail extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-add place", " pre execute async");
			mDialog = ProgressDialog.show(CategoriesHomeActivity.this, "",
					"Fetching details.. Please wait...", true);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			try {
				JSONObject json = userFunction.getCategoryDetail(deviceId,
						params[0]);
				String strCode = json.getString(TAG_CODE);

				if (strCode.equalsIgnoreCase("200")) {
					JSONObject PlaceResult = json.getJSONObject(TAG_DATA);
					selectedPlaceResult = new HashMap<String, String>();
					selectedPlaceResult.put(TAG_SPOT_ID, params[0]);
					selectedPlaceResult.put(TAG_SPOT_NAME,
							PlaceResult.getString(TAG_SPOT_NAME));
					selectedPlaceResult.put(TAG_IMAGE,
							PlaceResult.getString(TAG_IMAGE));
					selectedPlaceResult.put(TAG_DESC,
							PlaceResult.getString(TAG_DESC));
					selectedPlaceResult.put(TAG_RATING,
							PlaceResult.getString(TAG_RATING));
					selectedPlaceResult.put(TAG_FAVOURITES,
							PlaceResult.getString(TAG_FAVOURITES));
					selectedPlaceResult.put(TAG_REVIEW_COUNT,
							PlaceResult.getString(TAG_REVIEW_COUNT));
					selectedPlaceResult.put(TAG_IS_FAVOURITE,
							PlaceResult.getString(TAG_IS_FAVOURITE));
					// Individual detail
					try {
						selectedPlaceResult.put(TAG_LOCATION,
								PlaceResult.getString(TAG_LOCATION));
					} catch (Exception e) {
						// TAG_LOCATION exception
					}
					try {
						selectedPlaceResult.put(TAG_WEBSITE,
								PlaceResult.getString(TAG_WEBSITE));
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						selectedPlaceResult.put(TAG_PHONE,
								PlaceResult.getString(TAG_PHONE));
					} catch (Exception e) {
						// TODO: handle exception
					}

					try {
						selectedPlaceResult.put(TAG_CUSINE,
								PlaceResult.getString(TAG_CUSINE));
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						selectedPlaceResult.put(TAG_MEAL_PRICE,
								PlaceResult.getString(TAG_MEAL_PRICE));
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						selectedPlaceResult.put(TAG_VEGETARIAN,
								PlaceResult.getString(TAG_VEGETARIAN));
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						selectedPlaceResult.put(TAG_ALCOHOL,
								PlaceResult.getString(TAG_ALCOHOL));
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						selectedPlaceResult.put(TAG_DRINK_PRICE,
								PlaceResult.getString(TAG_DRINK_PRICE));
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						selectedPlaceResult.put(TAG_UNISEX,
								PlaceResult.getString(TAG_UNISEX));
					} catch (Exception e) {
						// TODO: handle exception
					}
					result = "true";
				} else if (strCode.equalsIgnoreCase("404")) {
					result = "false";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-add place", " post execute async");
			mDialog.dismiss();
			favouriteAddBtn.setText(R.string.cat_fav_add);
			if ("true".equals(result)) {
				if (selectedPlaceResult != null) {
					placeName.setText(selectedPlaceResult.get(TAG_SPOT_NAME));
					placeName2.setText(selectedPlaceResult.get(TAG_SPOT_NAME));
					placeName3.setText(selectedPlaceResult.get(TAG_SPOT_NAME));
					imageLoader.DisplayImage(
							selectedPlaceResult.get(TAG_IMAGE), placeImage);
					placeAddress.setText(selectedPlaceResult.get(TAG_DESC));
					String ratingStars = (selectedPlaceResult.get(TAG_RATING)
							.length() > 0 ? selectedPlaceResult.get(TAG_RATING)
							: "0");
					ratingLoader(Float.valueOf(ratingStars), placeRating);
					ratingLoader(Float.valueOf(ratingStars), placeRating2);
					ratingLoader(Float.valueOf(ratingStars), placeRating3);
					placeFollowers.setText(selectedPlaceResult
							.get(TAG_FAVOURITES) + " Follower(s)");
					placeReviewsCount.setText(selectedPlaceResult
							.get(TAG_REVIEW_COUNT) + " Review(s)");
					placeWriteReviewsText.setText(selectedPlaceResult
							.get(TAG_REVIEW_COUNT) + " Review(s)");
					placeWriteReviewsText2.setText(selectedPlaceResult
							.get(TAG_REVIEW_COUNT) + " Review(s)");
					placeWriteReviewsText3.setText("Average of "
							+ selectedPlaceResult.get(TAG_REVIEW_COUNT)
							+ " Review(s)");

				}
				if ("true".equalsIgnoreCase(selectedPlaceResult
						.get(TAG_IS_FAVOURITE))) {
					favouriteAddBtn.setText(R.string.cat_fav_added);
				} else {
					favouriteAddBtn.setText(R.string.cat_fav_add);
				}
				// Individual detail
				try {

					if (selectedPlaceResult.get(TAG_LOCATION).length() > 0) {
						placeLocation.setText("Location : "
								+ selectedPlaceResult.get(TAG_LOCATION));
						placeName.setText(selectedPlaceResult
								.get(TAG_SPOT_NAME)
								+ " - "
								+ selectedPlaceResult.get(TAG_LOCATION));
						placeName2.setText(selectedPlaceResult
								.get(TAG_SPOT_NAME)
								+ " - "
								+ selectedPlaceResult.get(TAG_LOCATION));
						placeName3.setText(selectedPlaceResult
								.get(TAG_SPOT_NAME)
								+ " - "
								+ selectedPlaceResult.get(TAG_LOCATION));
						placeLocation.setVisibility(View.VISIBLE);
					} else {
						placeLocation.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					placeLocation.setVisibility(View.GONE);
					e.printStackTrace();
				}
				try {
					if (selectedPlaceResult.get(TAG_WEBSITE).length() > 0) {
						placeWebsite.setText("Website : "
								+ selectedPlaceResult.get(TAG_WEBSITE));
						placeWebsite.setVisibility(View.VISIBLE);
					} else {
						placeWebsite.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					placeWebsite.setVisibility(View.GONE);
					e.printStackTrace();
				}
				try {
					if (selectedPlaceResult.get(TAG_PHONE).length() > 0) {
						placePhoneNumber.setText("Contact : "
								+ selectedPlaceResult.get(TAG_PHONE));
						placePhoneNumber.setVisibility(View.VISIBLE);
					} else {
						placePhoneNumber.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					placePhoneNumber.setVisibility(View.GONE);
					e.printStackTrace();
				}

				try {
					if (selectedPlaceResult.get(TAG_CUSINE).length() > 0) {
						placeCusine.setText("Cusine : "
								+ selectedPlaceResult.get(TAG_CUSINE));
						placeCusine.setVisibility(View.VISIBLE);
					} else {
						placeCusine.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					placeCusine.setVisibility(View.GONE);
					e.printStackTrace();
				}
				try {
					if (selectedPlaceResult.get(TAG_MEAL_PRICE).length() > 0) {
						placeMealPrice.setText("Meal Price for 2 : "
								+ selectedPlaceResult.get(TAG_MEAL_PRICE));
						placeMealPrice.setVisibility(View.VISIBLE);
					} else {
						placeMealPrice.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					placeMealPrice.setVisibility(View.GONE);
					e.printStackTrace();
				}
				try {
					if (selectedPlaceResult.get(TAG_VEGETARIAN).length() > 0) {
						if (selectedPlaceResult.get(TAG_VEGETARIAN).equals("0")) {
							placeVegetarian.setText("Vegetarian : NO ");
						} else {
							placeVegetarian.setText("Vegetarian : YES ");
						}
						placeVegetarian.setVisibility(View.VISIBLE);
					} else {
						placeVegetarian.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					placeVegetarian.setVisibility(View.GONE);
					e.printStackTrace();
				}
				try {
					if (selectedPlaceResult.get(TAG_ALCOHOL).length() > 0) {
						if (selectedPlaceResult.get(TAG_ALCOHOL).equals("0")) {
							placeAlcohol.setText("Alcohol : NO ");
						} else {
							placeAlcohol.setText("Alcohol : YES ");
						}
						placeAlcohol.setVisibility(View.VISIBLE);
					} else {
						placeAlcohol.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					placeAlcohol.setVisibility(View.GONE);
					e.printStackTrace();
				}
				try {
					if (selectedPlaceResult.get(TAG_DRINK_PRICE).length() > 0) {
						placeDrinkprice.setText("Drink Price for 2 : "
								+ selectedPlaceResult.get(TAG_DRINK_PRICE));
						placeDrinkprice.setVisibility(View.VISIBLE);
					} else {
						placeDrinkprice.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					placeDrinkprice.setVisibility(View.GONE);
					e.printStackTrace();
				}
				try {
					if (selectedPlaceResult.get(TAG_UNISEX).length() > 0) {
						placeUnisex
								.setText(selectedPlaceResult.get(TAG_UNISEX));
						placeUnisex.setVisibility(View.VISIBLE);
					} else {
						placeUnisex.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					placeUnisex.setVisibility(View.GONE);
					e.printStackTrace();
				}
				detailsGallery.setAdapter(new CategoryGalleyAdapter(
						CategoriesHomeActivity.this));
				// Gallery Adjsutment
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);

				MarginLayoutParams mlp = (MarginLayoutParams) detailsGallery
						.getLayoutParams();
				mlp.setMargins(
						Integer.valueOf((int) -(metrics.widthPixels / 2.35)),
						mlp.topMargin, mlp.rightMargin, mlp.bottomMargin);
			} else {
				Toast.makeText(getApplicationContext(),
						"Error getting details", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class getReviews extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-get review", " pre execute async");
			mDialog = ProgressDialog.show(CategoriesHomeActivity.this, "",
					"Fetching Reviews.. Please wait...", true);
			reviewTitle.setText(null);
			reviewDetail.setText(null);
			writeReviewRating = 0f;
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			reviewList = new ArrayList<HashMap<String, String>>();
			try {
				JSONObject json = userFunction.getReviews(deviceId, params[0]);
				String strCode = json.getString(TAG_CODE);

				if (strCode.equalsIgnoreCase("200")) {
					JSONArray jsonArray = json.getJSONArray(TAG_DATA);
					for (int count = 0; count < jsonArray.length(); count++) {
						HashMap<String, String> reviewItem = new HashMap<String, String>();
						JSONObject jsonObject = jsonArray.getJSONObject(count);
						reviewItem.put(TAG_TITLE,
								jsonObject.getString(TAG_TITLE));
						reviewItem.put(TAG_REVIEWER_NAME,
								jsonObject.getString(TAG_REVIEWER_NAME));
						try {
							reviewItem.put(TAG_REVIEW_DATE, dateFormat
									.format(dateFormatDB.parse(jsonObject
											.getString(TAG_REVIEW_DATE))));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						reviewItem.put(TAG_RATING,
								jsonObject.getString(TAG_RATING));
						reviewItem.put(TAG_REVIEW,
								jsonObject.getString(TAG_REVIEW));
						reviewList.add(reviewItem);
					}
					if ("posted".equals(params[1])) {
						result = "true-posted";
					} else {
						result = "true";
					}
				} else if (strCode.equalsIgnoreCase("404")) {
					result = "false";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-get review", " post execute async");
			mDialog.dismiss();
			if (result.contains("true")) {
				reviewItems.setAdapter(new CategoryReviewAdapter(
						CategoriesHomeActivity.this, reviewList));
				placeReviewsCount.setText(reviewList.size() + " Review(s)");
				placeWriteReviewsText.setText(reviewList.size() + " Review(s)");
				placeWriteReviewsText2
						.setText(reviewList.size() + " Review(s)");
				placeWriteReviewsText3.setText("Average of "
						+ reviewList.size() + " Review(s)");
				catLayouts
						.setInAnimation(SlideAnimation.inFromRightAnimation());
				catLayouts.setOutAnimation(SlideAnimation.outToLeftAnimation());
				if (result.contains("posted")) {
					String[] params = { selectedItemId };
					new categoryDetail().execute(params);
				}
				hideKeyboard();
				catLayouts.setDisplayedChild(4);
			} else {
				Toast.makeText(getApplicationContext(),
						"Error retrieving reviews", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class writeReview extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-get review", " pre execute async");
			mDialog = ProgressDialog.show(CategoriesHomeActivity.this, "",
					"Adding your Review.. Please wait...", true);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			reviewList = new ArrayList<HashMap<String, String>>();
			try {
				JSONObject json = userFunction.writeReview(deviceId, params[0],
						params[1], params[2], params[3], params[4]);
				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					result = json.getString(TAG_DATA);
				} else if (strCode.equalsIgnoreCase("404")) {
					result = "false";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-get review", " post execute async");
			mDialog.dismiss();
			if ("true".equals(result)) {
				currentDate.setText("as on "
						+ dateFormat.format(Calendar.getInstance().getTime()));
				if (selectedItemId != null) {
					String[] getReviewsParams = { selectedItemId, "posted" };
					new getReviews().execute(getReviewsParams);
				}
			} else {
				catLayouts.setInAnimation(SlideAnimation.inFromLeftAnimation());
				catLayouts
						.setOutAnimation(SlideAnimation.outToRightAnimation());
				hideKeyboard();
				catLayouts.setDisplayedChild(4);
				Toast.makeText(getApplicationContext(),
						"You have already reviewed", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class addFavourite extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-addFavourite", " pre execute async");
			mDialog = ProgressDialog.show(CategoriesHomeActivity.this, "",
					"Adding Favourite.. Please wait...", true);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			try {
				JSONObject json = userFunction.addFavourite(deviceId,
						selectedItemId);
				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					result = json.getString(TAG_DATA);
				} else if (strCode.equalsIgnoreCase("404")) {
					result = "false";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-addFavourite", " post execute async");
			mDialog.dismiss();
			if ("true".equals(result)) {
				favouriteAddBtn.setText(R.string.cat_fav_added);
				String favCount = selectedPlaceResult.get(TAG_FAVOURITES);
				if (favCount.length() > 0) {
					favCount = String.valueOf((Integer.valueOf(favCount) + 1));
				} else {
					favCount = String.valueOf(1);
				}
				placeFollowers.setText(favCount + " Follower(s)");
				Toast.makeText(getApplicationContext(), "Added to Favourites",
						Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(getApplicationContext(),
						"Error adding Favourite", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class getFavourites extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-getFavourites", " pre execute async");
			mDialog = ProgressDialog.show(CategoriesHomeActivity.this, "",
					"Retrieving Favourites.. Please wait...", true);

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			try {
				JSONObject json = userFunction.getFavourites(deviceId);
				favouritesList = new ArrayList<HashMap<String, String>>();
				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					JSONArray jsonArray = json.getJSONArray(TAG_DATA);
					for (int count = 0; count < jsonArray.length(); count++) {
						HashMap<String, String> favouriteItem = new HashMap<String, String>();
						JSONObject jsonObject = jsonArray.getJSONObject(count);
						favouriteItem.put(TAG_SPOT_ID,
								jsonObject.getString(TAG_SPOT_ID));
						favouriteItem.put(TAG_SPOT_NAME,
								jsonObject.getString(TAG_SPOT_NAME));
						favouriteItem.put(TAG_IMAGE,
								jsonObject.getString(TAG_IMAGE));
						favouriteItem.put(TAG_DESC,
								jsonObject.getString(TAG_DESC));
						favouriteItem.put(TAG_RATING,
								jsonObject.getString(TAG_RATING));
						favouritesList.add(favouriteItem);
					}
					result = "true";
				} else if (strCode.equalsIgnoreCase("404")) {
					if (json.getString(TAG_ERRORS).contains(
							String.valueOf("No favourites found in database."))) {
						result = "empty";
					} else {
						result = "false";
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-getFavourites", " post execute async");
			mDialog.dismiss();
			if ("true".equals(result) || "empty".equals(result)) {
				favItems.setAdapter(new FavouriteItemAdapter(
						getApplicationContext(), favouritesList));
				hideKeyboard();
				catLayouts.setDisplayedChild(2);
			} else {
				Toast.makeText(getApplicationContext(),
						"Error retrieving Favourite", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private class removeFavourite extends AsyncTask<String, Void, String> {

		private ProgressDialog mDialog;
		UserFunctions userFunction = new UserFunctions();

		protected void onPreExecute() {
			Log.d("progress-removeFavourite", " pre execute async");
			mDialog = ProgressDialog.show(CategoriesHomeActivity.this, "",
					"Removing Favourite.. Please wait...", true);

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "false";
			try {
				JSONObject json = userFunction.removeFavourite(deviceId,
						params[0]);
				String strCode = json.getString(TAG_CODE);
				if (strCode.equalsIgnoreCase("200")) {
					result = json.getString(TAG_DATA);
				} else if (strCode.equalsIgnoreCase("404")) {
					result = "false";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if ("true".equals(result) && "true".equals(params[1])) {
				result = "fromDetail";

			} else if ("true".equals(result) && "false".equals(params[1])) {
				result = "fromFav";
			}
			Log.e("params for fav remove", params[1] + " result " + result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("progress-removeFavourite", " post execute async");
			mDialog.dismiss();
			catLayouts.setInAnimation(null);
			catLayouts.setOutAnimation(null);
			if ("fromDetail".equals(result)) {
				String[] params = { selectedItemId };
				new categoryDetail().execute(params);
				favouriteAddBtn.setText(R.string.cat_fav_add);
				hideKeyboard();
				catLayouts.setDisplayedChild(3);
			} else if ("fromFav".equals(result)) {
				new getFavourites().execute();
			} else {
				Toast.makeText(getApplicationContext(),
						"Error removing Favourite", Toast.LENGTH_SHORT).show();
			}
		}

	}

	public void getItemDetail(String itemId) {
		detailsNavCheck = "fromNotif";
		selectedItemId = itemId;
		String[] params = { selectedItemId };
		new categoryDetail().execute(params);
		favouriteAddBtn.setText(R.string.cat_fav_add);
		hideKeyboard();
		catLayouts.setDisplayedChild(3);
	}

	public void goBacktoNotification(String itemId) {
		if (null == parent) {
			parent = (ApplicationHomeActivity) this.getParent();
		}
		parent.goBacktoNotification(selectedItemId);

	}
}
