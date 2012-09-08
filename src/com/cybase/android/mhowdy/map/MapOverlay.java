package com.cybase.android.mhowdy.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();

	public MapOverlay(Context context, Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
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
	
}