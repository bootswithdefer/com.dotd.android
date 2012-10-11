package com.dotd.mgrs.gps;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class CoordinatesActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coordinates);

		LocalBroadcastManager.getInstance(this).registerReceiver(
				locationReceiver, new IntentFilter("location"));
	}
	
	@Override
	protected void onPause() {
		stopService(new Intent(CoordinatesActivity.this, MGRSLocationListener.class));
	    //Toast.makeText(this, "paused", Toast.LENGTH_SHORT).show();
		super.onPause();
	}

	@Override
	protected void onResume() {
		startService(new Intent(CoordinatesActivity.this, MGRSLocationListener.class));
	    //Toast.makeText(this, "resumed", Toast.LENGTH_SHORT).show();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_coordinates, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(CoordinatesActivity.this, MGRSLocationListener.class));
	    //Toast.makeText(this, "destroyed", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	public void updateTextView(String coords) {
		TextView textViewCoords = (TextView) findViewById(R.id.textViewCoords);
		textViewCoords.setText(coords);
	}

	private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getStringExtra("MGRS");
			updateTextView(action);
		}
	};
}
