package com.dotd.forensics;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PhotoListActivity extends ListActivity {
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;
		for (int i = 0; i < 8; i++) {
			item = new HashMap<String, String>();
			item.put("filename", "aaaaaaaaa");
			item.put("timestamp", "bbbbbbbbbb");
			item.put("coordinates", "ccccccccc");
			list.add(item);
		}

		adapter = new SimpleAdapter(this, list, R.layout.multi_line,
				new String[] { "filename", "timestamp", "coordinates" },
				new int[] { R.id.line_1, R.id.line_2, R.id.line_3 });

		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}

}
