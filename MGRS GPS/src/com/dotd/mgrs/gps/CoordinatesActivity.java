package com.dotd.mgrs.gps;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.widget.TextView;

public class CoordinatesActivity extends Activity {
	private Handler m_handler;
	private int counter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coordinates);

		LocalBroadcastManager.getInstance(this).registerReceiver(
				locationReceiver, new IntentFilter("MGRSlocation"));
		
		m_handler = new Handler();
		resetCounter();
	}
	
	@Override
	protected void onPause() {
		stopService(new Intent(CoordinatesActivity.this, MGRSLocationListener.class));
		m_handler.removeCallbacks(m_updateCounter);
		super.onPause();
	}

	@Override
	protected void onResume() {
		startService(new Intent(CoordinatesActivity.this, MGRSLocationListener.class));
		m_updateCounter.run();
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
		m_handler.removeCallbacks(m_updateCounter);
		super.onDestroy();
	}

	public void updateTextView(String name, String coords) {
		TextView textView;
		
		if (name.equals("location"))
			textView = (TextView) findViewById(R.id.textViewCoords);
		else if (name.equals("message"))
			textView = (TextView) findViewById(R.id.textViewMessage);
		else if (name.equals("accuracy"))
			textView = (TextView) findViewById(R.id.textViewAccuracy);
		else if (name.equals("altitude"))
			textView = (TextView) findViewById(R.id.textViewAltitude);
		else if (name.equals("bearing"))
			textView = (TextView) findViewById(R.id.textViewBearing);
		else if (name.equals("age"))
			textView = (TextView) findViewById(R.id.textViewAge);
		else
			return;
		
		textView.setText(coords);
	}

	private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getStringExtra("location");
			updateTextView("location", action);
			action = intent.getStringExtra("accuracy");
			resetCounter();
			updateTextView("accuracy", action);
			action = intent.getStringExtra("altitude");
			updateTextView("altitude", action);
			action = intent.getStringExtra("bearing");
			updateTextView("bearing", action);
			action = intent.getStringExtra("message");
			updateTextView("message", action);
		}
	};
	
	private void resetCounter()
	{
		counter = 0;
	}
	
	Runnable m_updateCounter = new Runnable()
	{
		public void run() {
			updateTextView("age", Integer.toString(counter) + "s");
			counter++;
			m_handler.postDelayed(m_updateCounter, 1000);
		}
	};
}
