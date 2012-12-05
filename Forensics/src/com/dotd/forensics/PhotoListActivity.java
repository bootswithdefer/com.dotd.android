package com.dotd.forensics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PhotoListActivity extends ListActivity {
	private SimpleAdapter adapter;
	public PhotoDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		datasource = new PhotoDataSource(this);
		datasource.open();

		List<PhotoData> photos = datasource.getAllPhotos();

		datasource.close();

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;
		for (int i = 0; i < photos.size(); i++) {
			PhotoData photo = photos.get(i);
			item = new HashMap<String, String>();
			item.put("filename", photo.getFilename());
			item.put("timestamp", photo.getTimestamp());
			item.put("coordinates", photo.getCoordinates());
			list.add(item);
		}

		adapter = new SimpleAdapter(this, list, R.layout.multi_line,
				new String[] { "filename", "timestamp", "coordinates" },
				new int[] { R.id.line_1, R.id.line_2, R.id.line_3 });

		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Object o = this.getListAdapter().getItem(position);

		@SuppressWarnings("unchecked")
		HashMap<String, String> item = (HashMap<String, String>) o;

		String filename = item.get("filename");

		Toast.makeText(this, filename, Toast.LENGTH_LONG).show();
	}

}
