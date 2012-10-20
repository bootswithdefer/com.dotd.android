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

import android.location.Location;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.MGRSRef;

public class MGRSLocation extends Location {
	MGRSRef mgrsref; // MGRS coordinates class from Jcoord library
	
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
