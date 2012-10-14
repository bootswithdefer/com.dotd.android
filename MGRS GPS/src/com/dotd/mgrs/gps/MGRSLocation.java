package com.dotd.mgrs.gps;

import android.location.Location;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.MGRSRef;

public class MGRSLocation extends Location {
	MGRSRef mgrsref;
	
	public MGRSLocation(String provider) {
		super(provider);
		// TODO Auto-generated constructor stub
	}

	public MGRSLocation(Location l) {
		super(l);

		LatLng latlng = new LatLng(l.getLatitude(), l.getLongitude());
		mgrsref = latlng.toMGRSRef();
	}

	public String getGZD() {
		return String.format("%d%c", mgrsref.getUtmZoneNumber(), mgrsref.getUtmZoneChar());
	}
	
	public String getSquare() {
		return String.format("%c%c", mgrsref.getEastingID(), mgrsref.getNorthingID());
	}
	
	public int getNorthing() {
		return mgrsref.getNorthing();
	}
	
	public int getEasting() {
		return mgrsref.getEasting();
	}

	public String toString() {
		return String.format("%s %s %05d %05d", getGZD(), getSquare(), getEasting(), getNorthing());
	}
}
