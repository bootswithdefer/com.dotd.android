package com.dotd.asumaps;

import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.os.Bundle;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.Menu;

public class MapsActivityPortrait extends MapActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
		}

		setContentView(R.layout.activity_map);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		addOverlays();

		PointData point = (PointData) getIntent().getSerializableExtra("point");

		ASUMapUtil.centerOn(mapView, point);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void addOverlays() {
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		// Add overlay for points
		List<Overlay> mapOverlays = mapView.getOverlays();
		for (PointData point : new PlacesManager()) {
			Drawable drawable = this.getResources().getDrawable(
					point.getDrawableId());
			ASUItemizedOverlay itemizedoverlay = new ASUItemizedOverlay(
					drawable, this);
			itemizedoverlay.addOverlay(point.getOverlayItem());
			mapOverlays.add(itemizedoverlay);
		}
	}
}
