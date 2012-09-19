package com.dotd.mgrs.gps;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CoordinatesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinates);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_coordinates, menu);
        return true;
    }
}
