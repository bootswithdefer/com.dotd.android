package com.dotd.asumaps;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PointsArrayAdapter extends ArrayAdapter<PointData> {
	public PointsArrayAdapter(Context context, int textViewResourceId,
			List<PointData> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = (TextView) super.getView(position, convertView, parent);
		PointData point = getItem(position);
		view.setText(point.getName());
		return view;
	}
};
