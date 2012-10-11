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
	
	private void sendBroadcast(String message) {
		Intent intent = new Intent("location");
		intent.putExtra("MGRS",  message);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
	
	public void onLocationChanged(Location location) {
		MGRSLocation mgrs = new MGRSLocation(location);
		
		sendBroadcast(mgrs.toMGRSString());
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		sendBroadcast("providerDisabled");

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		sendBroadcast("providerEnabled");

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		sendBroadcast("statusChanged");

	}

	public int onStartCommand(Intent intent, int flags, int startId) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		sendBroadcast("Acquiring...");

		return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
