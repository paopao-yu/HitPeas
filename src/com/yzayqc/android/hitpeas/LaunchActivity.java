package com.yzayqc.android.hitpeas;

import com.yzayqc.android.hitpeas.util.FusionField;
import com.yzayqc.android.hitpeas.util.IConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Start game UI Activity
 * 
 * @author yuqingchen
 * 
 */
public class LaunchActivity extends Activity
{
	private final static String TAG = "LaunchActivity";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViewById(R.id.begin_btn).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(LaunchActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		initData();
	}

	private void initData()
	{
		// Get screen size
		Display defaultDisplay = getWindowManager().getDefaultDisplay();
		FusionField.SCREEN_HEIGHT = defaultDisplay.getHeight();
		FusionField.SCREEN_WIDTH = defaultDisplay.getWidth();
		
		// Calculate the peas number
		FusionField.PEA_WIDTH = getResources().getDrawable(R.drawable.box_blue)
				.getMinimumWidth();
		Log.i(TAG, "PEA_WIDTH" + FusionField.PEA_WIDTH);
		FusionField.PEA_X_NUM = (int) ((FusionField.SCREEN_WIDTH - FusionField.GAME_PADDING_WIDTH * 2) / FusionField.PEA_WIDTH);
		FusionField.PEA_Y_NUM = (int) ((FusionField.SCREEN_HEIGHT - IConstants.SCORE_HEIGHT) / FusionField.PEA_WIDTH);
		Log.i(TAG, "^_^ X:Y " + FusionField.PEA_X_NUM + ":"
				+ FusionField.PEA_Y_NUM);

		FusionField.GAME_PADDING_WIDTH = (int) ((FusionField.SCREEN_WIDTH - FusionField.PEA_X_NUM
				* FusionField.PEA_WIDTH) / 2);

	}
}
