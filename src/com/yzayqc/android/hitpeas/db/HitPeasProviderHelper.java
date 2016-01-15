package com.yzayqc.android.hitpeas.db;

import com.yzayqc.android.hitpeas.db.ProviderContants.ScoreColumns;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class HitPeasProviderHelper
{
	private static final Uri UriBase = Uri.parse("content://"
			+ ProviderContants.AUTHORITY);

	private ContentResolver mResolver = null;

	public HitPeasProviderHelper(ContentResolver resolver)
	{
		this.mResolver = resolver;
	}

	public void insertScore(int score)
	{
		ContentValues values = new ContentValues();
		values.put(ScoreColumns.SCORE, score);
		mResolver.insert(UriBase, values);
	}

	public int queryHighestScore()
	{
		int socre = -1;
		Cursor cursor = mResolver.query(UriBase, null, null, null,
				ScoreColumns.SCORE + " desc");
		if (null != cursor && cursor.moveToFirst())
		{
			socre = cursor.getInt(cursor.getColumnIndex(ScoreColumns.SCORE));
		}
		return socre;
	}
}
