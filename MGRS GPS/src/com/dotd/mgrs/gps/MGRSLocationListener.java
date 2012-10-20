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

	// Send the new location to the broadcast receiver for the service
	private void sendLocationBroadcast(MGRSLocation mgrs) {
		Intent intent = new Intent("MGRSlocation");
		intent.putExtra("location", mgrs.toString());
		intent.putExtra("accuracy", String.format("%.1fm", mgrs.getAccuracy()));
		intent.putExtra("altitude", String.format("%.1fm", mgrs.getAltitude()));
		intent.putExtra("bearing", String.format("%.1f", mgrs.getBearing()));
		intent.putExtra("message", R.string.location_updated);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	// Send a status message to the broadast receiver for the service
	private void sendMessageBroadcast(String message) {
		Intent intent = new Intent("MGRSlocation");
		intent.putExtra("message",  message);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
	
	// Called when the GPS provider gets a new location
	public void onLocationChanged(Location location) {
		sendLocationBroadcast(new MGRSLocation(location));
	}

	public void onProviderDisabled(String provider) {
		sendMessageBroadcast(getString(R.string.gps_provider_disabled));
	}

	public void onProviderEnabled(String provider) {
		sendMessageBroadcast(getString(R.string.gps_provider_enabled_acquiring));
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		sendMessageBroadcast(getString(R.string.gps_status_changed));
	}

	// On service start, start the GPS provider
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
