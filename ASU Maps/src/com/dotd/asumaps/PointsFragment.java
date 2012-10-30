package com.dotd.asumaps;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PointsFragment extends ListFragment {
	OnPointSelectedListener mCallback;

	public interface OnPointSelectedListener {
		public void onPointSelected(PointData point);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnPointSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnPointSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);

		ArrayAdapter<PointData> adapter = new PointsArrayAdapter(getActivity(),
				android.R.layout.simple_list_item_1, new PlacesManager());
		setListAdapter(adapter);

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		PointData point = (PointData) getListView().getItemAtPosition(position);
		mCallback.onPointSelected(point);
	}
}
