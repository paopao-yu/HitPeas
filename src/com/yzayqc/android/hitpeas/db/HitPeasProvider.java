package com.yzayqc.android.hitpeas.db;

import com.yzayqc.android.hitpeas.db.ProviderContants.ScoreColumns;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class HitPeasProvider extends ContentProvider
{
	private static final String TAG = "HitPeasProvider";

	private static final String DATABASE_NAME = "peas.db";

	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_SCORE = "score";

	/**
	 * This class helps open, create, and upgrade the database file.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		private static final String TAG = "DatebaseHelper";

		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			try
			{
				db.execSQL("create table " + TABLE_SCORE + " ("
						+ ScoreColumns._ID
						+ " integer primary key autoincrement , "
						+ ScoreColumns.SCORE + " int);");
			}
			catch (SQLException e)
			{
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			onCreate(db);
		}
	}

	private static final int SCORE = 1;

	private static final UriMatcher uriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static
	{
		uriMatcher.addURI(ProviderContants.AUTHORITY, null, SCORE);
	}

	private DatabaseHelper mOpenHelper;

	@Override
	public boolean onCreate()
	{
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder)
	{
		Cursor cursor = null;
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		int match = uriMatcher.match(uri);
		switch (match)
		{
			case SCORE:
			{
				cursor = db.query(TABLE_SCORE, projection, selection,
						selectionArgs, null, null, sortOrder);
				break;
			}
			default:
			{
				break;
			}
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri)
	{
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = 0;
		int match = uriMatcher.match(uri);
		switch (match)
		{
			case SCORE:
			{
				rowId = db.insert(TABLE_SCORE, "", values);
				break;
			}
			default:
			{
				Log.e(TAG, "Invalid request: " + uri);
				break;
			}
		}

		Uri res = null;
		if (rowId > 0)
		{
			res = Uri.parse(ProviderContants.AUTHORITY + "/" + rowId);
		}
		return res;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs)
	{
		return 0;
	}

}
