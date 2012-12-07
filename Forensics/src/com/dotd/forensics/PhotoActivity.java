package com.dotd.forensics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoActivity extends Activity {
	private SurfaceView preview = null;
	private SurfaceHolder previewHolder = null;
	private Camera camera = null;
	private boolean inPreview = false;
	private boolean cameraConfigured = false;

	private TextView textView_timestamp;
	private TextView textView_coordinates;
	private Handler time_handler;

	private double latitude = 0.0;
	private double longitude = 0.0;
	private String currentDate;
	private String coordinates;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_photo);

		preview = (SurfaceView) findViewById(R.id.preview);
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);

		Button takePhoto = (Button) findViewById(R.id.button_takePhoto);
		takePhoto.setOnClickListener(takePhotoHandler);

		textView_timestamp = (TextView) findViewById(R.id.textView_timestamp);
		textView_coordinates = (TextView) findViewById(R.id.textView_coordinates);

		LocalBroadcastManager.getInstance(this).registerReceiver(
				locationReceiver, new IntentFilter("ForensicsLocation"));

		time_handler = new Handler();
	}

	@Override
	public void onResume() {
		super.onResume();

		startService(new Intent(this, LocationService.class));
		m_updateCounter.run();

		camera = Camera.open();
		if (camera == null)
			camera = Camera.open(Camera.getNumberOfCameras() - 1);
	}

	public static final int MENU_LIST = Menu.FIRST;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_LIST, Menu.NONE, "List Photos");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_LIST:
			Intent intent = new Intent(this, PhotoListActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		if (inPreview) {
			camera.stopPreview();
		}

		if (camera != null) {
			camera.release();
		}
		camera = null;
		inPreview = false;

		stopService(new Intent(this, LocationService.class));
		time_handler.removeCallbacks(m_updateCounter);

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(this, LocationService.class));
		time_handler.removeCallbacks(m_updateCounter);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				locationReceiver);

		super.onDestroy();
	}

	private Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (result == null) {
				result = size;
			} else {
				int resultArea = result.width * result.height;
				int newArea = size.width * size.height;

				if (newArea > resultArea) {
					result = size;
				}
			}
		}

		return (result);
	}

	private void initPreview(int width, int height) {
		if (camera != null && previewHolder.getSurface() != null) {
			try {
				camera.setPreviewDisplay(previewHolder);
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback",
						"Exception in setPreviewDisplay()", t);
				Toast.makeText(PhotoActivity.this, t.getMessage(),
						Toast.LENGTH_LONG).show();
			}

			if (!cameraConfigured) {
				Camera.Parameters parameters = camera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height, parameters);

				if (size != null) {
					parameters.setPreviewSize(size.width, size.height);
					camera.setParameters(parameters);
					cameraConfigured = true;
				}
			}
		}
	}

	private void startPreview() {
		if (cameraConfigured && camera != null) {
			camera.startPreview();
			inPreview = true;
		}
	}

	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
			// no-op -- wait until surfaceChanged()
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			initPreview(width, height);
			startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// no-op
		}
	};

	View.OnClickListener takePhotoHandler = new View.OnClickListener() {
		public void onClick(View v) {
			Button button;
			button = (Button) findViewById(R.id.button_takePhoto);

			if (button.getId() == ((Button) v).getId()) {
				camera.takePicture(null, null, pictureCallback);
			}
		}
	};

	Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] imageData, Camera c) {
			File pictureFileDir = getDir();

			if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

				Toast.makeText(getApplicationContext(),
						"Can't create: " + getDir(), Toast.LENGTH_LONG).show();
				camera.startPreview();
				return;

			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = dateFormat.format(new Date());

			String photoFile = "ForensicPhoto_" + date + ".jpg";
			String filename = pictureFileDir.getPath() + File.separator
					+ photoFile;
			File pictureFile = new File(filename);

			// save original image
			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(imageData);
				fos.close();
			} catch (Exception error) {
				Toast.makeText(getApplicationContext(),
						"Image could not be saved: " + error.toString(),
						Toast.LENGTH_LONG).show();
				return;
			}

			photoFile = "TForensicPhoto_" + date + ".jpg";
			String thumbname = pictureFileDir.getPath() + File.separator
					+ photoFile;

			// save 10% size thumbnail
			try {
				Bitmap src = BitmapFactory.decodeFile(filename);

				pictureFile = new File(thumbname);

				int width = (int) (src.getWidth() * 0.1f + 0.5f);
				int height = (int) (src.getHeight() * 0.1f + 0.5f);
				Bitmap dst = Bitmap
						.createScaledBitmap(src, width, height, true);

				FileOutputStream fos = new FileOutputStream(pictureFile);
				dst.compress(CompressFormat.JPEG, 100, fos);
				fos.close();
			} catch (Exception error) {
				Toast.makeText(getApplicationContext(),
						"Thumbnail could not be saved: " + error.toString(),
						Toast.LENGTH_LONG).show();
				return;
			}

			StringBuffer hexString = new StringBuffer();
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				digest.update(imageData);
				byte messageDigest[] = digest.digest();

				for (int i = 0; i < messageDigest.length; i++) {
					String h = Integer.toHexString(0xFF & messageDigest[i]);
					while (h.length() < 2)
						h = "0" + h;
					hexString.append(h);
				}
			} catch (NoSuchAlgorithmException e) {
				Toast.makeText(getApplicationContext(), "No MD5 Algorithm!",
						Toast.LENGTH_LONG).show();
				return;
			}

			new HTTPRequestTask()
					.execute("http://dotd.com/forensics/submit.php?hashtype=md5&hash="
							+ hexString
							+ "&timestamp="
							+ date
							+ "&latitude="
							+ String.format("%.6f", latitude)
							+ "&longitude="
							+ String.format("%.6f", longitude));
			// TODO handle response

			PhotoDataSource datasource = new PhotoDataSource(
					getApplicationContext());
			datasource.open();
			datasource.createPhoto(filename, thumbname, "MD5",
					hexString.toString(), coordinates, currentDate,
					PhotoSQLiteHelper.PHOTO_UNSUBMITTED);
			datasource.close();

			camera.startPreview();
		}

		private File getDir() {
			File sdDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			return new File(sdDir, "Forensics");
		}
	};

	Runnable m_updateCounter = new Runnable() {
		public void run() {
			currentDate = DateFormat.getDateTimeInstance().format(new Date());
			textView_timestamp.setText(currentDate);

			coordinates = String.format("%.6f, %.6f", latitude, longitude);

			time_handler.postDelayed(m_updateCounter, 1000);
		}
	};

	private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String loc = intent.getStringExtra("coordinates");
			textView_coordinates.setText(loc);
			latitude = intent.getDoubleExtra("latitude", 0.0);
			longitude = intent.getDoubleExtra("longitude", 0.0);
		}
	};

	private class HTTPRequestTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				Toast.makeText(getApplicationContext(), "Protocol Exception!",
						Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "IO Exception!",
						Toast.LENGTH_LONG).show();
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			String[] temp;
			temp = result.split(" ");

			if (!temp[0].equals("SUCCESS")) {
				Toast.makeText(getApplicationContext(),
						"Failed to submit photo!", Toast.LENGTH_LONG).show();
				return;
			}

			PhotoDataSource datasource = new PhotoDataSource(
					getApplicationContext());
			datasource.open();

			if (!datasource.markSubmitted(temp[1], temp[2])) {
				Toast.makeText(getApplicationContext(),
						"Failed to mark photo as submitted!", Toast.LENGTH_LONG)
						.show();
			}

			datasource.close();
		}
	}

}
