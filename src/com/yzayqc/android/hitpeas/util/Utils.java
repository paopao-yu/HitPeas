package com.yzayqc.android.hitpeas.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.yzayqc.android.hitpeas.bean.PeaView;
import com.yzayqc.android.hitpeas.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Utils
{
	private final static String TAG = "Utils";

	/**
	 * Get peas' bitmap from color
	 * 
	 * @param context
	 * @param color
	 * @return
	 */
	public static Bitmap getBitmapByColor(Context context, int color)
	{
		switch (color)
		{
			case IConstants.PEA_COLOR_BLUE:
			{
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.box_blue);
			}
			case IConstants.PEA_COLOR_GREEN:
			{
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.box_green);
			}
			case IConstants.PEA_COLOR_PINK:
			{
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.box_pink);
			}
			case IConstants.PEA_COLOR_RED:
			{
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.box_red);
			}
			case IConstants.PEA_COLOR_TURQUOISE:
			{
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.box_turquoise);
			}
			case IConstants.PEA_COLOR_YELLOW:
			{
				return BitmapFactory.decodeResource(context.getResources(),
						R.drawable.box_yellow);
			}
			default:
			{
				return null;
			}
		}

	}

	/**
	 * Generate random color
	 * 
	 * @return
	 */
	public static int getRondomColor()
	{
		Random r = new Random();
		int nextInt = r.nextInt(IConstants.PEA_COLOR_SUM);
		return nextInt;
	}

	/**
	 * Get close peas from array
	 * 
	 * @param x
	 * @param y
	 * @param peaArray
	 * @return
	 */
	public static List<PeaView> getCLosePeas(int x, int y, PeaView[][] peaArray)
	{
		List<PeaView> closePeas = new ArrayList<PeaView>();

		// X
		if (x > 0)
		{
			for (int i = x - 1; i > -1; i--)
			{
				if (null != peaArray[i][y])
				{
					closePeas.add(peaArray[i][y]);
					Log.i(TAG, "Left: " + i + "/" + y);
					break;
				}
			}
		}
		if (x < FusionField.PEA_X_NUM - 1)
		{
			for (int i = x + 1; i < FusionField.PEA_X_NUM; i++)
			{
				if (null != peaArray[i][y])
				{
					closePeas.add(peaArray[i][y]);
					Log.i(TAG, "Right: " + i + "/" + y);
					break;
				}
			}
		}

		// Y
		if (y > 0)
		{
			for (int i = y - 1; i > -1; i--)
			{
				if (null != peaArray[x][i])
				{
					closePeas.add(peaArray[x][i]);
					Log.i(TAG, "Up: " + x + "/" + i);
					break;
				}
			}
		}
		if (y < FusionField.PEA_Y_NUM - 1)
		{
			for (int i = y + 1; i < FusionField.PEA_Y_NUM; i++)
			{
				if (null != peaArray[x][i])
				{
					closePeas.add(peaArray[x][i]);
					Log.i(TAG, "Down: " + x + "/" + i);
					break;
				}
			}
		}

		return closePeas;
	}

	/**
	 * Judge whether the given PeaView is out of screen bound.
	 * 
	 * @param peaView
	 * @return
	 */
	public static boolean judgeOutOfBoundary(PeaView peaView)
	{
		boolean flag = false;
		Log.i(TAG,
				"Judge disappear: " + peaView.getmXNum() + "/"
						+ peaView.getmYNum() + " , Ypos: " + peaView.getmYPos());
		if (peaView.getmYPos() > FusionField.SCREEN_HEIGHT)
		{
			flag = true;
		}

		return flag;
	}

	public static boolean hasPeaIn(int x, int y, PeaView[][] peaArray)
	{
		if (x <= FusionField.PEA_X_NUM && y <= FusionField.PEA_Y_NUM
				&& null != peaArray[x][y]) { return true; }
		
		return false; 
	}
}
