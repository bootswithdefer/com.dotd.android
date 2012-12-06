package com.dotd.forensics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
			item.put("id", Long.toString(photo.getId()));
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

	public static final int MENU_PHOTO = Menu.FIRST;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_PHOTO, Menu.NONE, "Take Photos");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_PHOTO:
			Intent intent = new Intent(this, PhotoActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Object o = this.getListAdapter().getItem(position);

		@SuppressWarnings("unchecked")
		HashMap<String, String> item = (HashMap<String, String>) o;

		Intent intent = new Intent(this, PhotoDetailActivity.class);
		intent.putExtra("id", Long.valueOf(item.get("id")));
		startActivity(intent);

		/*
		 * String filename = item.get("filename"); Toast.makeText(this,
		 * filename, Toast.LENGTH_LONG).show();
		 */
	}

}
