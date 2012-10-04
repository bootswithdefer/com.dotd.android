package com.dotd.mgrs.gps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class CoordinatesActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinates);
        
//        startService(new Intent(CoordinatesActivity.this,CoordinatesActivity.class));     
//        updateTextView(locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_coordinates, menu);
        return true;
    }

    public void updateTextView(String coords) {
        TextView textViewCoords = (TextView)findViewById(R.id.textViewCoords);
        textViewCoords.setText(coords);
    }
}
