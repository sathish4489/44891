package com.cybase.android.mhowdy.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class LocationOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private int latitude;
	private int longitude;

	public LocationOverlay(Context context, Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		populate();
	}

	public OverlayItem getOverlayItem(int i) {
		return overlays.get(i);
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlays.get(i);
	}

	@Override
	public int size() {
		return overlays.size();

	}

	public void addOverlay(OverlayItem overlay) {
		overlays.add(overlay);
		populate();
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		//boolean flow = super.onTap(p, mapView);
		overlays.clear();
		// mapView.getOverlays().clear();
		// mapView.getOverlays().add(this);
		// if (!flow) {
		overlays.clear();
		latitude = (int) (p.getLatitudeE6());
		longitude = (int) (p.getLongitudeE6());
		GeoPoint point = new GeoPoint(latitude, longitude);
		OverlayItem item = new OverlayItem(point, "", "");
		this.addOverlay(item);
		// mapView.getOverlays().clear();
		// mapView.getOverlays().add(this);
		// }
		populate();
		return true;
	}

	@Override
	protected boolean onTap(int index) {
		// if (index >= 0 && overlays.size() != 0) {
		// return true;
		// } else {
		return false;
		// }
	};
}