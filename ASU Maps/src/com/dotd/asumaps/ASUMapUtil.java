package com.dotd.asumaps;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public final class ASUMapUtil {
	public static void centerOnASU(MapView mapView) {
		double lat = 33.421907;
		double lng = -111.933181;

		centerOn(mapView, lat, lng);
	}

	public static void centerOn(MapView mapView, double lat, double lng) {
		GeoPoint center = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		centerOn(mapView, center);
	}

	public static void centerOn(MapView mapView, GeoPoint center) {
		mapView.getController().setCenter(center);
		mapView.getController().setZoom(16);
	}

	public static void centerOn(MapView mapView, PointData point) {
		centerOn(mapView, point.getLat(), point.getLng());		
	}
	
	public static List<PointData> createPoints() {
		List<PointData> list = new ArrayList<PointData>();

		list.add(new PointData("ASU", 33.421907, -111.933181));
		list.add(new PointData("ASU 2", 33.420407, -111.932481));
		list.add(new PointData("ASU 3", 33.410407, -111.922481));

		return list;
	}
}
