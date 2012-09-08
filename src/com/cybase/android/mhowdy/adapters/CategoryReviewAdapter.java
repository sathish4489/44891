package com.cybase.android.mhowdy.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cybase.android.mhowdy.R;
import com.cybase.android.mhowdy.activities.CategoriesHomeActivity;

public class CategoryReviewAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<HashMap<String, String>> reviewList;
	private HashMap<String, String> review;

	public CategoryReviewAdapter(Context c) {
		mContext = c;
	}

	public CategoryReviewAdapter(Context context,
			ArrayList<HashMap<String, String>> placesList) {
		mContext = context;
		this.reviewList = placesList;
	}

	public int getCount() {
		return reviewList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		view = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.listitem_review, null);
		TextView reviewCount = (TextView) view.findViewById(R.id.review_count);
		TextView reviewTitle = (TextView) view.findViewById(R.id.review_title);
		RatingBar reviewRating = (RatingBar) view
				.findViewById(R.id.review_rating);
		TextView reviewContent = (TextView) view
				.findViewById(R.id.review_content);
		TextView reviewDate = (TextView) view.findViewById(R.id.review_date);
		TextView reviewNickName = (TextView) view
				.findViewById(R.id.review_nickname);
		review = reviewList.get(position);
		reviewCount.setText(String.valueOf(position + 1));
		reviewTitle.setText(review.get(CategoriesHomeActivity.TAG_TITLE));
		String rating = review.get(CategoriesHomeActivity.TAG_RATING).length() > 0 ? review
				.get(CategoriesHomeActivity.TAG_RATING) : "0";
		reviewRating.setRating(Float.valueOf(rating));
		reviewContent.setText(review.get(CategoriesHomeActivity.TAG_REVIEW));
		reviewDate.setText(review.get(CategoriesHomeActivity.TAG_REVIEW_DATE));
		reviewNickName.setText(review
				.get(CategoriesHomeActivity.TAG_REVIEWER_NAME));
		return view;
	}
}
