package com.dotd.mgrs.gps;

import android.location.Location;

public class MGRSLocation extends Location {

	public MGRSLocation(String provider) {
		super(provider);
		// TODO Auto-generated constructor stub
	}

	public MGRSLocation(Location l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	public String getMGRSgzd() {
		return "xx";
	}
	
	public String getMGRSsquare() {
		return "xx";
	}
	
	public int getMGRSnorthing() {
		return 0;
	}
	
	public int getMGRSeasting() {
		return 0;
	}

	public String toMGRSString() {
		return String.format("%s %s %05d %05d", getMGRSgzd(), getMGRSsquare(), getMGRSnorthing(), getMGRSeasting());
	}
}
