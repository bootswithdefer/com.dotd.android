package com.dotd.asumaps;

import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;

public class MapsActivityLandscape extends MapActivity implements
		PointsFragment.OnPointSelectedListener {
	private boolean addedOverlays = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_maps);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_maps, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onPointSelected(PointData point) {
		MapFragment fragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.mapview);

		if (fragment != null && fragment.isInLayout()) {
			MapView mapView = (MapView) findViewById(R.id.mapview);
			mapView.setBuiltInZoomControls(true);

			if (!addedOverlays) {
				addOverlays();
				addedOverlays = true;
			}
			
			ASUMapUtil.centerOn(mapView, point);
		} else {
			Intent intent = new Intent(this, MapsActivityPortrait.class);
			intent.putExtra("point", point);
			startActivity(intent);
		}
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
