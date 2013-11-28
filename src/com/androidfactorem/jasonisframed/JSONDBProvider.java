package com.androidfactorem.jasonisframed;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class JSONDBProvider extends ContentProvider {

	public static final Uri CONTENT_URI = Uri
			.parse("content://com.androidfactorem.jsondbprovider/jsonisframed");
	public static final Uri CONTENT_URI_CLEAR = Uri
			.parse("content://com.androidfactorem.jsondbprovider/clear");
	public static final Uri CONTENT_URI_UPDATE = Uri
			.parse("content://com.androidfactorem.jsondbprovider/updates");

	// Column Names
	public static final String KEY_ID = "_id";
	public static final String KEY_DATA_ID = "aid";
	public static final String KEY_TYPE = "atype";
	public static final String KEY_TEXT = "atext";
	public static final String KEY_IMAGE = "aimage";
	public static final String KEY_LINK = "link";
	public static final String KEY_VALID = "avalid";

	JasonDBContract dbHelper;

	@Override
	public boolean onCreate() {
		Context context = getContext();

		dbHelper = new JasonDBContract(context, JasonDBContract.DATABASE_NAME,
				null, JasonDBContract.DATABASE_VERSION);
		return true;
	}

	private static final int JASON = 1;
	private static final int JASON_ID = 2;
	private static final int JASON_CLEAR = 3;
	private static final int JASON_UPDATE = 4;

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("com.androidfactorem.jsondbprovider", "jasonisframed",
				JASON);
		uriMatcher.addURI("com.androidfactorem.jsondbprovider", "jasonisframed/#",
				JASON_ID);
		uriMatcher
				.addURI("com.androidfactorem.jsondbprovider", "clear", JASON_CLEAR);
		uriMatcher.addURI("com.androidfactorem.jsondbprovider", "updates",
				JASON_UPDATE);
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case JASON:
			return "vnd.android.cursor.dir/vnd.androidfactorem.jasonisframed";
		case JASON_ID:
			return "vnd.android.cursor.item/vnd.androidfactorem.jasonisframed";
		case JASON_CLEAR:
			return "vnd.android.cursor.item/vnd.androidfactorem.clear";
		case JASON_UPDATE:
			return "vnd.android.cursor.item/vnd.androidfactorem.updates";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {

		SQLiteDatabase database = dbHelper.getWritableDatabase();

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(JasonDBContract.JASON_TABLE);

		switch (uriMatcher.match(uri)) {
		case JASON_ID:
			qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			break;
		}

		Cursor c = qb.query(database, projection, selection, selectionArgs,
				null, null, null);

		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public Uri insert(Uri _uri, ContentValues _initialValues) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();

		long rowID = database.insert(JasonDBContract.JASON_TABLE, "quake",
				_initialValues);

		if (rowID > 0) {
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}

		throw new SQLException("Failed to insert row into " + _uri);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();

		int count;
		switch (uriMatcher.match(uri)) {
		case JASON:
			count = database.delete(JasonDBContract.JASON_TABLE, where,
					whereArgs);
			break;
		case JASON_ID:
			String segment = uri.getPathSegments().get(1);
			count = database.delete(JasonDBContract.JASON_TABLE,
					KEY_ID
							+ "= "
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case JASON_CLEAR:
			count = database.delete(JasonDBContract.JASON_TABLE, null, null);
			break;
		default:
			count = database.delete(JasonDBContract.JASON_TABLE, null, null);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();

		int count;
		switch (uriMatcher.match(uri)) {
		case JASON:
			count = database.update(JasonDBContract.JASON_TABLE, values, where,
					whereArgs);
			break;
		case JASON_ID:
			String segment = uri.getPathSegments().get(1);
			count = database.update(JasonDBContract.JASON_TABLE, values,
					KEY_ID
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case JASON_UPDATE:
			count = database.update(JasonDBContract.JASON_TABLE, values, where,
					whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private static class JasonDBContract extends SQLiteOpenHelper {

		private static final String TAG = "JSONDataProvider";

		private static final String DATABASE_NAME = "jason.db";
		private static final int DATABASE_VERSION = 1;
		private static final String JASON_TABLE = "jason";

		private static final String DATABASE_CREATE = "create table "
				+ JASON_TABLE + " (" + KEY_ID
				+ " integer primary key autoincrement, " + KEY_DATA_ID
				+ " TEXT, " + KEY_TYPE + " TEXT, " + KEY_TEXT + " TEXT, "
				+ KEY_IMAGE + " TEXT, " + KEY_LINK + " TEXT, " + KEY_VALID
				+ " TEXT);";

		private SQLiteDatabase jasonDB;

		public JasonDBContract(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS " + JASON_TABLE);
			onCreate(db);
		}

	}

}
