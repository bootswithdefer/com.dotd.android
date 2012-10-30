package com.dotd.asumaps;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class PointData implements Serializable {
	private static final long serialVersionUID = -3583209689093691463L;

	private double lat;
	private double lng;
	private String name;
	private String description;
	private int drawableId;
	private boolean selected;

	public PointData(String name, String description, double lat, double lng, int drawableId) {
		this.setName(name);
		this.setDescription(description);
		this.setLat(lat);
		this.setLng(lng);
		this.setDrawableId(drawableId);
		this.setSelected(false);
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public GeoPoint getPoint() {
		return new GeoPoint((int) (getLat() * 1E6), (int) (getLng() * 1E6));
	}

	public OverlayItem getOverlayItem() {
		return new OverlayItem(getPoint(), getName(), getDescription());
	}
	
	public int getDrawableId() {
		return drawableId;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}
}
