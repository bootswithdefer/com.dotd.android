package com.dotd.asumaps;

import java.util.ArrayList;

public class PlacesManager extends ArrayList<PointData> {
	private static final long serialVersionUID = -6707330925765518167L;

	public PlacesManager() {
		super();
		createPoints();
	}

	private void createPoints() {
		add(new PointData("Port of Subs", "Restaurant", 33.422935, -111.934788, R.drawable.restaurant));
		add(new PointData("Munchies", "Restaurant", 33.424195, -111.939662, R.drawable.restaurantgreek));
		add(new PointData("ECA", "Work", 33.418587, -111.931905, R.drawable.workoffice));
		add(new PointData("Lot 59E", "Parking", 33.424967, -111.925978, R.drawable.parking));
		add(new PointData("Desert Arboretum", "Place", 33.425526, -111.930003, R.drawable.garden));
		add(new PointData("Pyramid", "Place", 33.418758, -111.938715, R.drawable.pyramidegypt));
	}
}
