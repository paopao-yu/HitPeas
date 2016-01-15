package com.yzayqc.android.hitpeas;

import java.util.Timer;
import java.util.TimerTask;

import com.yzayqc.android.hitpeas.db.HitPeasProviderHelper;
import com.yzayqc.android.hitpeas.util.IConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Hit peas UI Activity
 * 
 * @author yuqingchen
 * 
 */
public class MainActivity extends Activity
{
	private TextView mScoreTextView;
	private GameScreen mGameScreen;
	private ProgressBar mProgressBar;
	private Timer timer;
	private int mScore = 0;

	public static final int HANDLER_ADD_SCORE = 1;
	public static final int HANDLER_MINUS_SCORE = 2;
	public static final int HANDLER_BEGIN_TIMER = 3;
	public static final int HANDLER_END_GAME = 4;

	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case HANDLER_ADD_SCORE:
				{
					int addScore = msg.arg1;
					mScore += addScore;
					mScoreTextView.setText(String.valueOf(mScore));
					break;
				}
				case HANDLER_MINUS_SCORE:
				{
					mScore -= IConstants.ERROR_MINUS_SCORE;
					mScoreTextView.setText(String.valueOf(mScore));

					mProgressBar.incrementProgressBy(-5);
					break;
				}
				case HANDLER_BEGIN_TIMER:
				{
					beginTimer();
					break;
				}
				case HANDLER_END_GAME:
				{
					HitPeasProviderHelper helper = new HitPeasProviderHelper(
							MainActivity.this.getContentResolver());
					helper.insertScore(mScore);

					// End the game, toast a dialog to show the score
					showScoreDialog(helper.queryHighestScore());
					break;
				}
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_main);
		init();
	}

	private void init()
	{
		mScoreTextView = (TextView) findViewById(R.id.score_tv);
		mScoreTextView.setText("0");

		mGameScreen = (GameScreen) findViewById(R.id.game_screen);
		mGameScreen.setHandler(mHandler);

		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
	}

	private void beginTimer()
	{
		PeaTimerTask timerTask = new PeaTimerTask();
		timer = new Timer();
		timer.schedule(timerTask, 10, 1000);
	}

	class PeaTimerTask extends TimerTask
	{

		@Override
		public void run()
		{
			mProgressBar.incrementProgressBy(-1);

			if (mProgressBar.getProgress() < 1)
			{
				if (null != timer)
				{
					timer.cancel();
					Log.i("yqc", "End..." + System.currentTimeMillis());

					mHandler.sendEmptyMessage(HANDLER_END_GAME);
				}
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		if (null != timer)
		{
			timer.cancel();
		}
	}

	private void showScoreDialog(int highScore)
	{
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("Your score: "  + mScore + "\n"
				+ "Highest score: " + highScore);
		builder.setTitle(getString(R.string.Game_over_label));
		builder.setPositiveButton(getString(R.string.ok), new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				MainActivity.this.finish();
			}
		});
		builder.create().show();

	}

}
