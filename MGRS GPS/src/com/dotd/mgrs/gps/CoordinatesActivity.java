/* MGRS GPS App for Android
 * Copyright (C) 2012. Jesse DeFer
 */

/* This program uses the Jcoord library Copyright (C) Jonathan Stott 
 * For more information see: http://www.jstott.me.uk/jcoord/
 */

/* See assets/LICENSE for full text of the GPL Version 2 under which
 * this program is licensed.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
	private Handler age_handler; // increments the age counter once per second
	private int age; // age in seconds since last GPS update
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coordinates);

		// Listen for GPS updates from the MGRSLocationListener Service
		LocalBroadcastManager.getInstance(this).registerReceiver(
				locationReceiver, new IntentFilter("MGRSlocation"));
		
		age_handler = new Handler();
		age = 0;
	}
	
	@Override
	protected void onPause() {
		// Stop GPS service when application is paused/stopped
		stopService(new Intent(CoordinatesActivity.this, MGRSLocationListener.class));
		age_handler.removeCallbacks(m_updateCounter);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// Start GPS service when application starts/resumes
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
		age_handler.removeCallbacks(m_updateCounter);
		super.onDestroy();
	}

	// Update a text view in the Activity by name
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

	// Receives location updates from GPS service and updates Activity
	private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getStringExtra("location");
			updateTextView("location", action);
			action = intent.getStringExtra("accuracy");
			updateTextView("accuracy", action);
			action = intent.getStringExtra("altitude");
			updateTextView("altitude", action);
			action = intent.getStringExtra("bearing");
			updateTextView("bearing", action);
			action = intent.getStringExtra("message");
			updateTextView("message", action);
			
			age = 0;
		}
	};

	// Runs once a second to update the age counter
	Runnable m_updateCounter = new Runnable()
	{
		public void run() {
			updateTextView("age", Integer.toString(age) + "s");
			age++;
			age_handler.postDelayed(m_updateCounter, 1000);
		}
	};
}
