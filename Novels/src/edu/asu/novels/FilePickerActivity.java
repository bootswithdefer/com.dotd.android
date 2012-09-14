package edu.asu.novels;

import java.io.IOException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FilePickerActivity extends Activity implements OnItemClickListener {

	public static final int NOVELS_REQUEST_CODE = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        
        ListView listViewFilePicker = (ListView) findViewById(R.id.listViewFilePicker);
        
        AssetManager am = this.getAssets();
        try {
        	String files[] = am.list("books");
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, files);
        	listViewFilePicker.setAdapter(adapter);
        	listViewFilePicker.setOnItemClickListener(this);
        } catch (IOException e) {
        	Log.v("Novels", "IO Exception getting list of books");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_file_picker, menu);
        return true;
    }
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	String file = ((TextView)view).getText().toString();
    	
		Intent intent = new Intent(this, NovelsActivity.class);
		intent.putExtra("file", "books/" + file);
		startActivityForResult(intent, NOVELS_REQUEST_CODE);
    }
}
