package com.dotd.forensics;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PhotoDataSource {
	// Database fields
	private SQLiteDatabase database;
	private PhotoSQLiteHelper dbHelper;
	private String[] allColumns = { PhotoSQLiteHelper.COLUMN_ID,
			PhotoSQLiteHelper.COLUMN_FILENAME,
			PhotoSQLiteHelper.COLUMN_THUMBNAIL, PhotoSQLiteHelper.COLUMN_MD5,
			PhotoSQLiteHelper.COLUMN_SHA1, PhotoSQLiteHelper.COLUMN_SHA256,
			PhotoSQLiteHelper.COLUMN_COORDINATES,
			PhotoSQLiteHelper.COLUMN_TIMESTAMP,
			PhotoSQLiteHelper.COLUMN_SUBMITTED };

	public PhotoDataSource(Context context) {
		dbHelper = new PhotoSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public PhotoData createPhoto(String filename, String thumbnail,
			String hashType, String hash, String coordinates, String timestamp,
			int submitted) {
		ContentValues values = new ContentValues();

		values.put(PhotoSQLiteHelper.COLUMN_FILENAME, filename);
		values.put(PhotoSQLiteHelper.COLUMN_THUMBNAIL, thumbnail);
		values.put(PhotoSQLiteHelper.COLUMN_COORDINATES, coordinates);
		values.put(PhotoSQLiteHelper.COLUMN_TIMESTAMP, timestamp);
		values.put(PhotoSQLiteHelper.COLUMN_SUBMITTED, 0);
		if (hashType.equalsIgnoreCase("MD5"))
			values.put(PhotoSQLiteHelper.COLUMN_MD5, hash);
		else if (hashType.equalsIgnoreCase("SHA1"))
			values.put(PhotoSQLiteHelper.COLUMN_SHA1, hash);
		else if (hashType.equalsIgnoreCase("SHA256"))
			values.put(PhotoSQLiteHelper.COLUMN_SHA256, hash);
		values.put(PhotoSQLiteHelper.COLUMN_SUBMITTED, submitted);

		long insertId = database.insert(PhotoSQLiteHelper.TABLE_PHOTOS, null,
				values);
		Cursor cursor = database.query(PhotoSQLiteHelper.TABLE_PHOTOS,
				allColumns, PhotoSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		PhotoData newPhoto = cursorToPhoto(cursor);
		cursor.close();
		return newPhoto;
	}

	public void deletePhoto(PhotoData photo) {
		long id = photo.getId();
		System.out.println("Photo deleted with id: " + id);
		database.delete(PhotoSQLiteHelper.TABLE_PHOTOS,
				PhotoSQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	public void deleteAllPhotos() {
		database.delete(PhotoSQLiteHelper.TABLE_PHOTOS, null, null);
	}

	public boolean markSubmitted(String hashtype, String hash) {
		String col;
		if (hashtype.equalsIgnoreCase("MD5"))
			col = PhotoSQLiteHelper.COLUMN_MD5;
		else if (hashtype.equalsIgnoreCase("SHA1"))
			col = PhotoSQLiteHelper.COLUMN_SHA1;
		else if (hashtype.equalsIgnoreCase("SHA256"))
			col = PhotoSQLiteHelper.COLUMN_SHA256;
		else
			return false;

		ContentValues args = new ContentValues();
		args.put(PhotoSQLiteHelper.COLUMN_SUBMITTED,
				PhotoSQLiteHelper.PHOTO_SUBMITTED);

		int rows = database.update(PhotoSQLiteHelper.TABLE_PHOTOS, args, col
				+ "=?", new String[] { hash });

		if (rows > 0)
			return true;
		return false;
	}

	public PhotoData getPhoto(long id) {
		String[] args = new String[] { Long.toString(id) };

		Cursor cursor = database.query(PhotoSQLiteHelper.TABLE_PHOTOS,
				allColumns, PhotoSQLiteHelper.COLUMN_ID + "=?", args, null,
				null, null);
		cursor.moveToFirst();

		PhotoData photo = null;
		while (!cursor.isAfterLast()) {
			photo = cursorToPhoto(cursor);
			cursor.moveToNext();
		}
		cursor.close();
		return photo;
	}

	public List<PhotoData> getAllPhotos() {
		List<PhotoData> photos = new ArrayList<PhotoData>();

		Cursor cursor = database.query(PhotoSQLiteHelper.TABLE_PHOTOS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PhotoData photo = cursorToPhoto(cursor);
			photos.add(photo);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return photos;
	}

	private PhotoData cursorToPhoto(Cursor cursor) {
		PhotoData photo = new PhotoData();
		photo.setId(cursor.getLong(0));
		photo.setFilename(cursor.getString(1));
		photo.setThumbnail(cursor.getString(2));
		photo.setMd5(cursor.getString(3));
		photo.setSha1(cursor.getString(4));
		photo.setSha256(cursor.getString(5));
		photo.setCoordinates(cursor.getString(6));
		photo.setTimestamp(cursor.getString(7));
		photo.setSubmitted(cursor.getInt(8));
		return photo;
	}

}
