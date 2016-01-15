package com.yzayqc.android.hitpeas.bean;


import com.yzayqc.android.hitpeas.util.FusionField;
import com.yzayqc.android.hitpeas.util.IConstants;
import com.yzayqc.android.hitpeas.util.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;

/**
 * Every pea
 * 
 * @author yuqingchen
 * 
 */
public class PeaView
{
	/**
	 * Position on X
	 */
	private float mXPos;

	/**
	 * Position on Y
	 */
	private float mYPos;

	/**
	 * Position on X in number
	 */
	private int mXNum;

	/**
	 * Position on Y in number
	 */
	private int mYNum;

	/**
	 * Speed on X
	 */
	public float mXSpeed;

	/**
	 * Speed on Y
	 */
	public float mYSpeed;

	/**
	 * Pea's color
	 */
	private int mColor;

	/**
	 * Bitmap
	 */
	private Bitmap mBitmap;

	/**
	 * Bitmap
	 */
	private long mFatherTime;

	public PeaView(Context context, int x, int y, int color)
	{
		this.mXNum = x;
		this.mYNum = y;
		this.mXPos = x * FusionField.PEA_WIDTH;
		this.mYPos = y * FusionField.PEA_WIDTH;
		this.mColor = color;
		Bitmap bitmapByColor = Utils.getBitmapByColor(context, mColor);
		this.mBitmap = bitmapByColor;
		this.mXSpeed = IConstants.V_X;
		this.mYSpeed = IConstants.V_Y;

	}

	public void drawSelf(Canvas canvas)
	{
		if (null != mBitmap)
		{
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0,
					Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
			canvas.setDrawFilter(pfd);
			canvas.drawBitmap(mBitmap, mXPos + FusionField.GAME_PADDING_WIDTH,
					mYPos, paint);
		}

	}

	public void addXPos(float x)
	{
		this.mXPos = mXNum * FusionField.PEA_WIDTH + x;
	}

	public void addYPos(float y)
	{
		this.mYPos = mYNum * FusionField.PEA_WIDTH + y;
	}

	public float getmXPos()
	{
		return mXPos;
	}

	public float getmYPos()
	{
		return mYPos;
	}

	public int getmXNum()
	{
		return mXNum;
	}

	public int getmYNum()
	{
		return mYNum;
	}

	public int getmColor()
	{
		return mColor;
	}

	public long getmFatherTime()
	{
		return mFatherTime;
	}

	public void setmFatherTime(long mFatherTime)
	{
		this.mFatherTime = mFatherTime;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof PeaView)
		{
			PeaView peaView = (PeaView) o;
			if (this.mXNum == peaView.mXNum && this.mYNum == peaView.mYNum) { return true; }
		}
		return false;
	}

}
