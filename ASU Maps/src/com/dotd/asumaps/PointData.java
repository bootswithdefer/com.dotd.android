package com.dotd.asumaps;
public class PointData {
	private double lat;
	private double lng;
	private String name;
	private boolean selected;

	public PointData(String name, double lat, double lng) {
		this.setName(name);
		this.setLat(lat);
		this.setLng(lng);
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
}
