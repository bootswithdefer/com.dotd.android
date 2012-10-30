package com.dotd.asumaps;

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
		mapView.getController().setZoom(18);
	}

	public static void centerOn(MapView mapView, PointData point) {
		if (point == null) {
			centerOnASU(mapView);
			return;
		}
		centerOn(mapView, point.getPoint());
	}
}
