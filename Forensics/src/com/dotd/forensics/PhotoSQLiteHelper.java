package com.dotd.forensics;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PhotoSQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_PHOTOS = "photos";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FILENAME = "filename";
	public static final String COLUMN_MD5 = "md5";
	public static final String COLUMN_SHA1 = "sha1";
	public static final String COLUMN_SHA256 = "sha256";
	public static final String COLUMN_COORDINATES = "coordinates";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_SUBMITTED = "submitted";

	private static final String DATABASE_NAME = "ForensicPhotos.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_PHOTOS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_FILENAME
			+ " text not null, " + COLUMN_MD5 + " text, " + COLUMN_SHA1
			+ " text, " + COLUMN_SHA256 + " text, " + COLUMN_COORDINATES
			+ " text not null, " + COLUMN_TIMESTAMP + " text not null, "
			+ COLUMN_SUBMITTED + " integer)";

	public PhotoSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVer, int newVer) {
		Log.w(PhotoSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVer + " to " + newVer
						+ ".");
		// handle database upgrades
	}

}
