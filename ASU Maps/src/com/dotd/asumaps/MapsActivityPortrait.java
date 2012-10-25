package com.dotd.asumaps;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.os.Bundle;
import android.content.res.Configuration;
import android.view.Menu;

public class MapsActivityPortrait extends MapActivity {
	public List<PointData> points = new ArrayList<PointData>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
		}

		points = ASUMapUtil.createPoints();

		setContentView(R.layout.activity_map);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int point = extras.getInt("point");

			if (point >= points.size())
				ASUMapUtil.centerOnASU(mapView);
			else
				ASUMapUtil.centerOn(mapView, points.get(point));
		} else {
			ASUMapUtil.centerOnASU(mapView);
		}
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
}
