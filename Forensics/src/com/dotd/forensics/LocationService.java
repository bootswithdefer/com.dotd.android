package com.dotd.forensics;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class LocationService extends Service implements LocationListener {

	// Called when the GPS provider gets a new location
	public void onLocationChanged(Location location) {
		Intent intent = new Intent("ForensicsLocation");
		intent.putExtra("coordinates", String.format("%.6f", location.getLatitude()) + ", " + String.format("%.6f", location.getLongitude()));
		intent.putExtra("latitude", location.getLatitude());
		intent.putExtra("longitude",  location.getLongitude());
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	// On service start, start the GPS provider
	public int onStartCommand(Intent intent, int flags, int startId) {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
