package com.dotd.asumaps;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PointsArrayAdapter extends ArrayAdapter<PointData> {
	private List<PointData> points;

	public PointsArrayAdapter(Context context, int textViewResourceId,
			List<PointData> points) {
		super(context, textViewResourceId, points);

		this.points = points;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = (TextView) super.getView(position, convertView, parent);
		view.setText(points.get(position).getName());
		return view;
	}
};
