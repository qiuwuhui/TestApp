package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	private float mDownX;
	private boolean mMove = false;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// return true; //返回true所有的事件自己处理
		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent

		switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = arg0.getX();

			break;
		case MotionEvent.ACTION_MOVE:

			if (!mMove) {
				mDownX = arg0.getX();
				
				mMove = true;
			}
			if (mDownX -  arg0.getX() >= 20|| arg0.getX()-   mDownX>= 20) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}

			break;
		case MotionEvent.ACTION_UP:
			mMove = false;
		}
		// if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
		// // 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
		// getParent().requestDisallowInterceptTouchEvent(true);
		// }

		return super.onInterceptTouchEvent(arg0);
	}

	// 无用回调代码
	// PointF downP = new PointF();
	// PointF curP = new PointF();
	OnSingleTouchListener onSingleTouchListener;

	public void onSingleTouch() {
		if (onSingleTouchListener != null) {

			onSingleTouchListener.onSingleTouch();
		}
	}

	public interface OnSingleTouchListener {
		public void onSingleTouch();
	}

	public void setOnSingleTouchListener(
			OnSingleTouchListener onSingleTouchListener) {
		this.onSingleTouchListener = onSingleTouchListener;
	}
}
