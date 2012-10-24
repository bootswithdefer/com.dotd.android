package com.dotd.asumaps;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PointsFragment extends ListFragment {
	String[] month = { "January", "February", "March", "April", "May" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);

		ListAdapter myListAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, month);
		setListAdapter(myListAdapter);

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String item = getListView().getItemAtPosition(position).toString();

		MapFragment fragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.mapFragment);

		if (fragment != null && fragment.isInLayout()) {
			fragment.setText(item);
		} else {
			Intent intent = new Intent(getActivity().getApplicationContext(),
					ASUMapActivity.class);
			intent.putExtra("text", item);
			startActivity(intent);
		}
	}
}
