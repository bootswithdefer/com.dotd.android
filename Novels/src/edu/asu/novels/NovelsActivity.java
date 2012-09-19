// Jesse DeFer
// 9/18/2012

package edu.asu.novels;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class NovelsActivity extends Activity {

	private TextView txtBig; // full screen TextView
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novels);

        String file = null;
        
        // Get name of file passed from the FilePickerActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        	file = extras.getString("file");
        
        txtBig = (TextView) findViewById(R.id.textViewBig);
        
        loadText(file);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_novels, menu);
        return true;
    }
    
    // Load text from the file and display it in a TextView
    private void loadText(String path) {
    	txtBig.setMovementMethod(new ScrollingMovementMethod());
    	if (path != null)
    	  txtBig.setText(readStringFromAsset(this, path));
    }
    
    // Read a file and return it's contents as a String
    static public String readStringFromAsset(Context ctx, String path) {
	    StringBuilder contents = new StringBuilder();
	    String sep = System.getProperty("line.separator");
	    
	    try {			
	      InputStream is = ctx.getAssets().open(path);

	      BufferedReader input =  new BufferedReader(new InputStreamReader(is), 1024*8);
	      try {
	        String line = null; 
	        while (( line = input.readLine()) != null){
	          contents.append(line);
	          contents.append(sep);
	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (FileNotFoundException ex) {
	    	Log.e("Novels", "Couldn't find the file " + ex);
	    	return null;
	    }
	    catch (IOException ex){
	    	Log.e("Novels", "Error reading file "  + ex);
	    	return null;
	    }
	    
	    return contents.toString();
	  }
}
