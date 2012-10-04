package com.dotd.mgrs.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class MGRSLocationListener extends Service implements LocationListener {
	private final IBinder mBinder = new MyBinder();
	private MGRSLocation current;

	MGRSLocationListener() {
		current = new MGRSLocation("MGRS");
	}

	public void onLocationChanged(Location location) {
		current = new MGRSLocation(location);
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public String getMGRSString() {
		return current.toMGRSString();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

		return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	public class MyBinder extends Binder {
		MGRSLocationListener getService() {
			return MGRSLocationListener.this;
		}
	}
}
