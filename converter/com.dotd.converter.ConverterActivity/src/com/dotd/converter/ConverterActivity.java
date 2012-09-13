// Jesse DeFer
// CSE494
// 9/4/2012

package com.dotd.converter;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

public class ConverterActivity extends Activity implements OnKeyListener {

	private EditText editTextCups;
	private TextView textViewResult;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        
        editTextCups = (EditText)findViewById(R.id.editTextCups);
        editTextCups.setOnKeyListener(this);

        textViewResult = (TextView)findViewById(R.id.textViewEquals);
        textViewResult.setText("0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_converter, menu);
        return true;
    }
    
  
    private void update_results(String input) {
    	float cups;
    	try {
    		cups = Float.valueOf(input);
    	}
    	catch (NumberFormatException e) {
    		return;
    	}
    	
    	float tbsp = Conversions.tablespoonsForCups(cups);
    	
    	textViewResult.setText(Float.toString(tbsp));
    }

	public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode==KeyEvent.KEYCODE_ENTER)) {
            update_results(editTextCups.getText().toString());
            return true;
        }
        return false;
	}
}
