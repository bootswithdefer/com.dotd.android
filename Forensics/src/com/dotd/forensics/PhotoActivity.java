package com.dotd.forensics;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
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

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
			String date = dateFormat.format(new Date());
			String photoFile = "ForensicPhoto_" + date + ".jpg";

			String filename = pictureFileDir.getPath() + File.separator
					+ photoFile;

			File pictureFile = new File(filename);

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
					.execute("http://dotd.com/forensics/submit?md5="
							+ hexString + "&latitude="
							+ String.format("%.6f", latitude) + "&longitude="
							+ String.format("%.6f", longitude));
			// TODO handle response
			
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
			textView_timestamp.setText("test");
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
}
