package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * 阻尼效果的scrollview
 */

public class DampView extends ScrollView {
	private static final int LEN = 0xc8;
	private static final int DURATION = 500;
	private static final int MAX_DY = 200;
	private Scroller mScroller;
	private ImageView mImageView;
	private TouchTool mTool;
	private int mTop;
	private float mStartY,  mCurrentY;
	private int mImageViewH;
	// private float mStartX,mCurrentX
	// private int mRootW, mRootH,mLeft;

	private boolean mScrollerType;

	public DampView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public DampView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
	}

	public DampView(Context context) {
		super(context);

	}

	public void setImageView(ImageView imageView) {
		this.mImageView = imageView;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (!mScroller.isFinished()) {
			return super.onTouchEvent(event);
		}
	//	mCurrentX = event.getX();
		mCurrentY = event.getY();
		mImageView.getTop();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		//	mLeft = mImageView.getLeft();
			mTop = mImageView.getBottom();
		//	mRootW = getWidth();
		//	mRootH = getHeight();
			mImageViewH = mImageView.getHeight();
		//	mStartX = mCurrentX;
			mStartY = mCurrentY;
			mTool = new TouchTool(mImageView.getLeft(), mImageView.getBottom(),
					mImageView.getLeft(), mImageView.getBottom() + LEN);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mImageView.isShown() && mImageView.getTop() >= 0) {
				if (mTool != null) {
					int t = mTool.getScrollY(mCurrentY - mStartY);
					if (t >= mTop && t <= mImageView.getBottom() + LEN) {
						android.view.ViewGroup.LayoutParams params = mImageView
								.getLayoutParams();
						params.height = t;
						mImageView.setLayoutParams(params);
					}
				}
				mScrollerType = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			mScrollerType = true;
			mScroller.startScroll(mImageView.getLeft(), mImageView.getBottom(),
					0 - mImageView.getLeft(),
					mImageViewH - mImageView.getBottom(), DURATION);
			invalidate();
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			mImageView.layout(0, 0, x + mImageView.getWidth(), y);
			invalidate();
			if (!mScroller.isFinished() && mScrollerType && y > MAX_DY) {
				android.view.ViewGroup.LayoutParams params = mImageView
						.getLayoutParams();
				params.height = y;
				mImageView.setLayoutParams(params);
			}
		}
	}

	public class TouchTool {

		private int startX, startY;

		public TouchTool(int startX, int startY, int endX, int endY) {
			super();
			this.startX = startX;
			this.startY = startY;
		}

		public int getScrollX(float dx) {
			int xx = (int) (startX + dx / 2.5F);
			return xx;
		}

		public int getScrollY(float dy) {
			int yy = (int) (startY + dy / 2.5F);
			return yy;
		}
	}
}
