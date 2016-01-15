package com.yzayqc.android.hitpeas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yzayqc.android.hitpeas.bean.PeaView;
import com.yzayqc.android.hitpeas.util.FusionField;
import com.yzayqc.android.hitpeas.util.IConstants;
import com.yzayqc.android.hitpeas.util.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Screen
 * 
 * @author yuqc
 * 
 */
public class GameScreen extends SurfaceView implements SurfaceHolder.Callback
{
	private final static String TAG = "GameScreen";

	private final static int SLEEP_SPAN = 30;

	private Context mContext;

	private Handler mHandler;

	/**
	 * An array fills in not fall down peas
	 */
	private PeaView[][] mPeaArray = new PeaView[FusionField.PEA_X_NUM][FusionField.PEA_Y_NUM];

	/**
	 * All falling down's peas are put into this
	 */
	private List<PeaView> mDisappearPeasList = new ArrayList<PeaView>();

	private boolean mFlag = false;

	public GameScreen(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context;
		getHolder().addCallback(this);
	}

	private void initPea()
	{
		for (int x = 0; x < FusionField.PEA_X_NUM; x++)
		{
			for (int y = 0; y < FusionField.PEA_Y_NUM; y++)
			{
				int rondomColor = Utils.getRondomColor();
				if (0 != rondomColor)
				{
					PeaView peaView = new PeaView(mContext, x, y, rondomColor);
					mPeaArray[x][y] = peaView;
				}
			}
		}
		Log.i(TAG, "End init peas ~(^o^)~");
	}

	public void surfaceCreated(SurfaceHolder holder)
	{
		initPea();

		// Draw initial peas
		Canvas canvas = holder.lockCanvas(null);
		drawBG(canvas);
		for (int i = 0; i < FusionField.PEA_X_NUM; i++)
		{
			for (int j = 0; j < FusionField.PEA_Y_NUM; j++)
			{
				PeaView peaView = mPeaArray[i][j];
				if (null != peaView)
				{
					peaView.drawSelf(canvas);
				}
			}
		}
		holder.unlockCanvasAndPost(canvas);
		Log.i(TAG, "End init draw");

		mHandler.sendEmptyMessage(MainActivity.HANDLER_BEGIN_TIMER);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		Log.i(TAG, "surfaceChanged");
	}

	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Log.i(TAG, "surfaceDestroyed");

		// Release thread
		mFlag = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x = event.getX() - FusionField.GAME_PADDING_WIDTH;
		float y = event.getY();

		int i = (int) (x / FusionField.PEA_WIDTH);
		int j = (int) (y / FusionField.PEA_WIDTH);

		// Prevent out of boundary
		if (i > FusionField.PEA_X_NUM - 1)
		{
			i = FusionField.PEA_X_NUM - 1;
		}
		if (j > FusionField.PEA_Y_NUM - 1)
		{
			j = FusionField.PEA_Y_NUM - 1;
		}
		hitPea(i, j);
		Log.i(TAG, "You hit " + i + " " + j);

		return super.onTouchEvent(event);
	}

	private void disappear()
	{
		// Time stamp
		long fatherTime = System.currentTimeMillis();
		while (mFlag)
		{
			long beginTime = System.currentTimeMillis();
			drawCanvas(fatherTime);
			long castTime = System.currentTimeMillis() - beginTime;

			// Control the frame
			if (castTime < SLEEP_SPAN)
			{
				try
				{
					Thread.sleep(SLEEP_SPAN - castTime);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

		}
	}

	private void drawBG(Canvas canvas)
	{
		canvas.drawRGB(255, 255, 255);

		Paint bgPaint = new Paint();
		bgPaint.setColor(getResources().getColor(android.R.color.darker_gray));

		// X
		for (int i = 0; i <= FusionField.PEA_Y_NUM; i++)
		{
			canvas.drawLine(FusionField.GAME_PADDING_WIDTH, i
					* FusionField.PEA_WIDTH, FusionField.SCREEN_WIDTH
					- FusionField.GAME_PADDING_WIDTH,
					i * FusionField.PEA_WIDTH, bgPaint);
		}

		// Y
		for (int m = 0; m <= FusionField.PEA_X_NUM; m++)
		{
			canvas.drawLine(m * FusionField.PEA_WIDTH
					+ FusionField.GAME_PADDING_WIDTH, 0, m
					* FusionField.PEA_WIDTH + FusionField.GAME_PADDING_WIDTH,
					FusionField.PEA_Y_NUM * FusionField.PEA_WIDTH, bgPaint);
		}
	}

	private void drawCanvas(long fatherTime)
	{
		Canvas canvas = null;
		try
		{
			// Clear screen
			canvas = getHolder().lockCanvas();
			drawBG(canvas);

			// Refresh others peas
			synchronized (mPeaArray)
			{
				for (int i = 0; i < FusionField.PEA_X_NUM; i++)
				{
					for (int j = 0; j < FusionField.PEA_Y_NUM; j++)
					{
						PeaView peaView = mPeaArray[i][j];
						if (null != peaView)
						{
							peaView.drawSelf(canvas);
						}
					}
				}
			}

			synchronized (mDisappearPeasList)
			{
				for (int m = 0; m < mDisappearPeasList.size(); m++)
				{
					PeaView view = mDisappearPeasList.get(m);
					if (null == view)
					{
						Log.e(TAG, "A empty view in mDisappearPeasList");
						continue;
					}

					float timeSpan = (float) (System.currentTimeMillis() - view
							.getmFatherTime()) / 1000;

					// X
					float xSpan = view.mXSpeed * timeSpan + timeSpan * timeSpan
							* IConstants.A_X / 2;
					view.addXPos(xSpan);

					// Y
					float ySpan = view.mYSpeed * timeSpan + timeSpan * timeSpan
							* IConstants.G / 2;
					view.addYPos(ySpan);

					// Refresh disappearing pea
					view.drawSelf(canvas);
				}

				// Judge whether all peas out of bounds
				for (int i = 0; i < mDisappearPeasList.size(); i++)
				{
					PeaView peaView = mDisappearPeasList.get(i);
					if (Utils.judgeOutOfBoundary(peaView))
					{
						mDisappearPeasList.remove(peaView);
					}
				}

				// All peas are fall down from the screen
				if (mDisappearPeasList.size() == 0)
				{
					mFlag = false;
					Log.i(TAG, "Disappear thread stop...");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
		finally
		{

			if (null != canvas)
			{
				getHolder().unlockCanvasAndPost(canvas);
			}
		}
	}

	private void hitPea(int x, int y)
	{
		// If PeaView[x][y] has pea
		if (Utils.hasPeaIn(x, y, mPeaArray))
		{
			handlerError();
			return;
		}

		// First get all close peas 0-4
		List<PeaView> cLosePeas = Utils.getCLosePeas(x, y, mPeaArray);
		Log.i(TAG, "Close peas number: " + cLosePeas.size());
		if (null == cLosePeas || cLosePeas.size() < 1)
		{
			handlerError();
			return;
		}

		// Find out the same color of this close peas
		// boolean flag = false;
		Set<PeaView> sameColorSet = new HashSet<PeaView>();
		for (int i = 0; i < cLosePeas.size(); i++)
		{
			for (int j = cLosePeas.size() - 1; j > i; j--)
			{

				PeaView peaViewI = cLosePeas.get(i);
				PeaView peaViewJ = cLosePeas.get(j);
				if (peaViewI.getmColor() == peaViewJ.getmColor())
				{
					sameColorSet.add(peaViewI);
					sameColorSet.add(peaViewJ);
				}
			}
		}

		if (sameColorSet.size() < 1)
		{
			handlerError();
			return;
		}
		else
		{
			Message message = new Message();
			message.what = MainActivity.HANDLER_ADD_SCORE;
			message.arg1 = sameColorSet.size();
			mHandler.sendMessage(message);

			for (PeaView peaViewI : sameColorSet)
			{
				if (null == peaViewI)
				{
					continue;
				}

				// Initial time
				peaViewI.setmFatherTime(System.currentTimeMillis());

				synchronized (mDisappearPeasList)
				{
					// Add to list
					mDisappearPeasList.add(peaViewI);
				}
				synchronized (mPeaArray)
				{
					// Remove from array
					mPeaArray[peaViewI.getmXNum()][peaViewI.getmYNum()] = null;
				}
			}

			// If thread not start
			if (!mFlag)
			{
				new Thread(new Runnable()
				{
					public void run()
					{
						Log.i(TAG,
								"Begin thread..." + mDisappearPeasList.size());
						mFlag = true;
						disappear();
					}
				}).start();
			}
		}
	}

	public void setHandler(Handler handler)
	{
		this.mHandler = handler;
	}

	private void handlerError()
	{
		// Toast.makeText(mContext, mContext.getString(R.string.error),
		// Toast.LENGTH_SHORT).show();

		// TODO Play a music

		// Vibration
		Vibrator vibrator = (Vibrator) mContext
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 300, 300 };
		vibrator.vibrate(pattern, -1);

		mHandler.sendEmptyMessage(MainActivity.HANDLER_MINUS_SCORE);
	}
}
