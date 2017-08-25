package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.baolinetworktechnology.shejiquan.R;
/**
 * 
 * @author JiSheng.Guo
 *
 */
public class LoadingImageView extends ImageView {
	private AnimationDrawable mAnimationDrawable;

	public LoadingImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundResource(R.anim.loading);
		mAnimationDrawable = (AnimationDrawable) getBackground();
		mAnimationDrawable.start();
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {

		super.onVisibilityChanged(changedView, visibility);
		if (visibility == View.GONE) {
			mAnimationDrawable.stop();
		}
	}
}
