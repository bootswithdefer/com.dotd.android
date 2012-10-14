package com.dotd.mgrs.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class MGRSLocationListener extends Service implements LocationListener {
	private void sendLocationBroadcast(MGRSLocation mgrs) {
		Intent intent = new Intent("MGRSlocation");
		intent.putExtra("location", mgrs.toString());
		intent.putExtra("accuracy", Float.toString(mgrs.getAccuracy()));
		intent.putExtra("altitude", String.format("%.1f", mgrs.getAltitude()));
		intent.putExtra("bearing", Float.toString(mgrs.getBearing()));
		intent.putExtra("message", R.string.location_updated);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void sendMessageBroadcast(String message) {
		Intent intent = new Intent("MGRSlocation");
		intent.putExtra("message",  message);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
	
	public void onLocationChanged(Location location) {
		sendLocationBroadcast(new MGRSLocation(location));
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		sendMessageBroadcast(getString(R.string.gps_provider_disabled));
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		sendMessageBroadcast(getString(R.string.gps_provider_enabled_acquiring));
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		sendMessageBroadcast(getString(R.string.gps_status_changed));
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		sendMessageBroadcast(getString(R.string.acquiring));

		return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
