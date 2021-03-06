/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baolinetworktechnology.shejiquan.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.camera.CameraManager;
import com.google.zxing.ResultPoint;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {
	/**
	 * 刷新界面的时间
	 */
	private static final long ANIMATION_DELAY = 40L;
	private static final int OPAQUE = 0xFF;
	// private static final int MAX_RESULT_POINTS = 5;
	/**
	 * 四个绿色边角对应的长度
	 */
	private int mScreenRate;

	/**
	 * 四个绿色边角对应的宽度
	 */
	private static final int CORNER_WIDTH = 10;
	/**
	 * 扫描框中的中间线的宽度
	 */
	// private static final int MIDDLE_LINE_WIDTH = 6;

	/**
	 * 扫描框中的中间线的与扫描框左右的间隙
	 */
	// private static final int MIDDLE_LINE_PADDING = 5;

	/**
	 * 中间那条线每次刷新移动的距离
	 */
	private static final int SPEEN_DISTANCE = 10;

	/**
	 * 手机的屏幕密度
	 */
	private static float density;
	/**
	 * 字体大小
	 */
	// private static final int TEXT_SIZE = 16;
	/**
	 * 字体距离扫描框下面的距离
	 */
	// private static final int TEXT_PADDING_TOP = 30;

	/**
	 * 画笔对象的引用
	 */
	private Paint mPaint;

	/**
	 * 中间滑动线的最顶端位置
	 */
	private int mSlideTop;

	/**
	 * 中间滑动线的最底端位置
	 */
	// private int slideBottom;

	/**
	 * 将扫描的二维码拍下来，这里没有这个功能，暂时不考虑
	 */
	private Bitmap mResultBitmap;
	private final int mMaskColor;
	private final int mResultColor;

	private Collection<ResultPoint> mPossibleResultPoints;
	private CameraManager mCameraManager;
	private boolean mIsFirst;
	private Collection<ResultPoint> lastPossibleResultPoints;
	private int angleColor=0xffFD0210;//

	public ViewfinderView(Context context) {
		this(context, null);
	}

	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mMaskColor = 0x60000000;
		mResultColor = 0xb0000000;
		mPossibleResultPoints = new ArrayList<ResultPoint>(5);
		density = context.getResources().getDisplayMetrics().density;
		// 将像素转换成dp
		mScreenRate = (int) (20 * density);
	
	}

	public void setCameraManager(CameraManager cameraManager) {
		this.mCameraManager = cameraManager;
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (mCameraManager == null)
			return;
		// 中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
		Rect frame = mCameraManager.getFramingRect();
		if (frame == null) {
			return;
		}
		// 初始化中间线滑动的最上边和最下边
		if (!mIsFirst) {
			mIsFirst = true;
			mSlideTop = frame.top;
			// slideBottom = frame.bottom;
		}

		// 获取屏幕的宽和高
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		if (mPaint == null) {
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		}
		mPaint.setColor(mResultBitmap != null ? mResultColor : mMaskColor);

		// 画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
		// 扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
		canvas.drawRect(0, 0, width, frame.top, mPaint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, mPaint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				mPaint);
		canvas.drawRect(0, frame.bottom + 1, width, height, mPaint);

		if (mResultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			mPaint.setAlpha(OPAQUE);
			canvas.drawBitmap(mResultBitmap, frame.left, frame.top, mPaint);
		} else {

			// Draw a two pixel solid black border inside the framing rect
			mPaint.setColor(0xffffffff);
			canvas.drawRect(frame.left, frame.top, frame.right + 1,
					frame.top + 2, mPaint);
			canvas.drawRect(frame.left, frame.top + 2, frame.left + 2,
					frame.bottom - 1, mPaint);
			canvas.drawRect(frame.right - 1, frame.top, frame.right + 1,
					frame.bottom - 1, mPaint);
			canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1,
					frame.bottom + 1, mPaint);

			// 这里画取景框四个角落的夹角
			mPaint.setColor(angleColor);
			mPaint.setAntiAlias(true);
			canvas.drawRect(frame.left - CORNER_WIDTH + 2, frame.top
					- CORNER_WIDTH + 2, frame.left + mScreenRate - CORNER_WIDTH
					+ 2, frame.top + 2, mPaint);
			canvas.drawRect(frame.left - CORNER_WIDTH + 2, frame.top
					- CORNER_WIDTH + 2, frame.left + 2, frame.top + mScreenRate
					- CORNER_WIDTH + 2, mPaint);
			canvas.drawRect(frame.right - mScreenRate + CORNER_WIDTH - 2,
					frame.top - CORNER_WIDTH + 2, frame.right + CORNER_WIDTH
							- 2, frame.top + 2, mPaint);
			canvas.drawRect(frame.right - 2, frame.top - CORNER_WIDTH + 2,
					frame.right + CORNER_WIDTH - 2, frame.top + mScreenRate
							- CORNER_WIDTH + 2, mPaint);

			canvas.drawRect(frame.left - CORNER_WIDTH + 2, frame.bottom - 2,
					frame.left + mScreenRate - CORNER_WIDTH + 2, frame.bottom
							+ CORNER_WIDTH - 2, mPaint);
			canvas.drawRect(frame.left - CORNER_WIDTH + 2, frame.bottom
					- mScreenRate + CORNER_WIDTH - 2, frame.left + 2,
					frame.bottom + CORNER_WIDTH - 2, mPaint);
			canvas.drawRect(frame.right - mScreenRate + CORNER_WIDTH - 2,
					frame.bottom - 2, frame.right + CORNER_WIDTH - 2,
					frame.bottom + CORNER_WIDTH - 2, mPaint);
			canvas.drawRect(frame.right - 2, frame.bottom - mScreenRate
					+ CORNER_WIDTH - 2, frame.right + CORNER_WIDTH - 2,
					frame.bottom + CORNER_WIDTH - 2, mPaint);

			// 绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
			mSlideTop += SPEEN_DISTANCE;
			if (mSlideTop >= frame.bottom) {
				mSlideTop = frame.top;
			}

			Rect lineRect = new Rect();
			lineRect.left = frame.left;
			lineRect.right = frame.right;
			lineRect.top = mSlideTop;
			lineRect.bottom = mSlideTop + 18;
			canvas.drawBitmap(((BitmapDrawable) (getResources()
					.getDrawable(R.drawable.qr_scan_line))).getBitmap(), null,
					lineRect, mPaint);
			
			//****
			Collection<ResultPoint> currentPossible = mPossibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				mPossibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				mPaint.setAlpha(OPAQUE);
				mPaint.setColor(0xffFF575F);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 6.0f, mPaint);
				}
			}
			if (currentLast != null) {
				mPaint.setAlpha(OPAQUE / 2);
				mPaint.setColor(0xffFF575F);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 3.0f, mPaint);
				}
			}
			// 显示关键点
			

			// 只刷新扫描框的内容，其他地方不刷新
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);

		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	public void drawViewfinder() {
		Bitmap resultBitmap = this.mResultBitmap;
		this.mResultBitmap = null;
		if (resultBitmap != null) {
			resultBitmap.recycle();
		}
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		mResultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		mPossibleResultPoints.add(point);
	}
}
