package com.yzayqc.android.hitpeas.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class ProviderContants
{
	public static final String AUTHORITY = "hitpeas";

	// This class cannot be instantiated
	private ProviderContants()
	{
	}

	/**
	 * Notes table
	 */
	public static final class ScoreColumns implements BaseColumns
	{
		// This class cannot be instantiated
		private ScoreColumns()
		{
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/score");

		public static final String SCORE = "score";
	}
}
