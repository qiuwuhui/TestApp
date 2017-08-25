package com.guojisheng.koyview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class GListViewHeader {
	private RelativeLayout mContainer;
	private ImageView adViewPager;
	private int mState = STATE_NORMAL;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;
	public final static int STATE_STOP = -1;

	public GListViewHeader(Context context, RelativeLayout view, int ImageId) {
		// super(context);
		initView(context, view, ImageId);
	}

	private void initView(Context context, RelativeLayout v, int ImageId) {

		mContainer = v;
		adViewPager = (ImageView) mContainer.findViewById(ImageId);
		// addView(mContainer);

	}

	public void setState(int state) {
		if (state == mState)
			return;

		switch (state) {
		case STATE_NORMAL:
			break;
		case STATE_READY:
			break;
		case STATE_REFRESHING:
			break;
		case STATE_STOP:
			break;
		default:
		}

		mState = state;
	}

	public void setVisiableHeight(int height) {

		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		//mContainer.getHeight();
		return adViewPager.getHeight();
	}

	public ImageView getContent() {
		return adViewPager;
	}

	public int getMeasuredHeight() {
		return mContainer.getMeasuredHeight();
	}

}
