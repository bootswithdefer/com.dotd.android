package com.dotd.asumaps;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MapsActivityLandscape extends MapActivity implements
		PointsFragment.OnPointSelectedListener {
	public List<PointData> points = new ArrayList<PointData>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		points = ASUMapUtil.createPoints();

		setContentView(R.layout.activity_maps);

		// Bundle extras = getIntent().getExtras();
		// if (extras != null) {
		// String s = extras.getString("text");
		// TextView view = (TextView) findViewById(R.id.mapText);
		// view.setText(s);
		// }
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
	public void onPointSelected(int point) {
		MapFragment fragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.mapview);

		if (fragment != null && fragment.isInLayout()) {
			MapView mapView = (MapView) findViewById(R.id.mapview);
			mapView.setBuiltInZoomControls(true);

			if (point >= points.size())
				ASUMapUtil.centerOnASU(mapView);
			else
				ASUMapUtil.centerOn(mapView, points.get(point));
		} else {
			Intent intent = new Intent(this, MapsActivityPortrait.class);
			intent.putExtra("point", point);
			startActivity(intent);
		}
	}

	public List<PointData> getPoints() {
		return points;
	}
}
