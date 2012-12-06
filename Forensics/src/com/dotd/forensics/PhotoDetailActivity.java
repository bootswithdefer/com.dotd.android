package com.dotd.forensics;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PhotoDetailActivity extends Activity {
	private PhotoData photo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_detail);

		Bundle extras = getIntent().getExtras();
		Long id = 0L;
		if (extras != null)
			id = extras.getLong("id");

		if (id > 0) {
			PhotoDataSource datasource = new PhotoDataSource(this);

			datasource.open();
			photo = datasource.getPhoto(id);
			datasource.close();
		}

		if (photo != null) {
			TextView tv = (TextView) findViewById(R.id.textView_timestamp);
			tv.setText(photo.getTimestamp());
			tv = (TextView) findViewById(R.id.textView_coordinates);
			tv.setText(photo.getCoordinates());
		}
	}

	public static final int MENU_LIST = Menu.FIRST;
	public static final int MENU_PHOTO = MENU_LIST + 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_LIST, Menu.NONE, "List Photos");
		menu.add(Menu.NONE, MENU_PHOTO, Menu.NONE, "Take Photo");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case MENU_LIST:
			intent = new Intent(this, PhotoListActivity.class);
			startActivity(intent);
			return true;
		case MENU_PHOTO:
			intent = new Intent(this, PhotoActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
