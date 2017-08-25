package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 下拉刷新
 * 
 * @author gjs
 * 
 */
public class MyListView extends PullToRefreshListView {
	// private OnScrollStateListener mOnScrollStateListener;
	// private float mStartY;

	// public interface OnScrollStateListener {
	// public void onScrollStateListener(boolean isUpScroll);
	// }

	// public void setOnScrollStateListener(
	// OnScrollStateListener onScrollStateListener) {
	// this.mOnScrollStateListener = onScrollStateListener;
	// }

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// if (onScrollStateListener != null)
		// getRefreshableView().setOnTouchListener(this);

	}

	public MyListView(Context context) {
		super(context);
	}

	public void setOnRefreshing() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				setRefreshing(true);
			}
		}, 110);
	}

	// @Override
	// public boolean onTouch(View v, MotionEvent ev) {
	// if (mOnScrollStateListener == null) {
	// return false;
	// }
	// int action = ev.getAction();
	// switch (action) {
	// case MotionEvent.ACTION_DOWN:
	// // 记录按下时的Y值
	// mStartY = ev.getY();
	// break;
	// case MotionEvent.ACTION_UP:
	// float nowY = ev.getY();
	// int deltaY = (int) (nowY - mStartY);
	// if (deltaY >= 50) {
	// if (mOnScrollStateListener != null) {
	// mOnScrollStateListener.onScrollStateListener(false);
	// }
	// } else {
	// if (deltaY != 0) {
	// if (mOnScrollStateListener != null) {
	// mOnScrollStateListener.onScrollStateListener(true);
	// }
	// }
	// }
	// break;
	// case MotionEvent.ACTION_MOVE:
	//
	// break;
	//
	// }
	// return false;
	// }

}
