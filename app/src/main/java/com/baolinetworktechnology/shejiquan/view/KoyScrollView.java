package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;

/**
 * 自定义ScrollView 实现 滑动时头部图片伸缩
 * 
 * @author JiSheng.Guo
 * 
 */
public class KoyScrollView extends ScrollView {

	private View mInner;// 孩子View

	private ImageView mImageView;// 背景图控件.

	private float mDeltaY;// Y轴滑动的距离

	private float mInitTouchY;// 首次点击的Y坐标

	private boolean mIsMoveing = false;// 是否开始移动.

	private int mInitImageHeight = 0;// 图片初始高度

	private float mCurrenImageHeight;

	private int mInnerMoveH;

	private int maxHeight = 300;// 滑动最大距离

	boolean isScroll = true;

	// 默认状态
	private State mState = State.NOMAL;

	private float mPreTouchY;

	// 状态：上部，下部，默认
	private enum State {
		UP, DOWN, NOMAL
	};

	/***
	 * 构造方法
	 * 
	 * @param context
	 * @param attrs
	 */
	public KoyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/***
	 * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
	 * 方法，也应该调用父类的方法，使该方法得以执行.
	 */
	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			mInner = getChildAt(0);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (mImageView != null && mInner != null) {
			if (commOnTouchEvent(ev)) {
				return true;
			}
		}
		if (isScroll) {

			return super.dispatchTouchEvent(ev);
		} else {
			return true;
		}
	}

	/***
	 * 触摸事件
	 * 
	 * @param ev
	 */

	public boolean commOnTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (mInitImageHeight == 0) {
				mInitImageHeight = mImageView.getHeight();
			}
			mPreTouchY = mInitTouchY = ev.getY();

			break;

		/***
		 * 排除出第一次移动计算，因为第一次无法得知deltaY的高度， 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0.
		 * 之后记录准确了就正常执行.
		 */
		case MotionEvent.ACTION_MOVE:

			float touchY = ev.getY();
			if (getScrollY() == 0) {
				mDeltaY = touchY - mInitTouchY;// 滑动距离
			} else {
				mDeltaY = 0;
				mInitTouchY = touchY;
			}
			float direction = touchY - mPreTouchY;
			mPreTouchY = touchY;
			/** 对于首次Touch操作要判断方位：UP OR DOWN **/
			if (direction < 0) {
				mState = State.UP;
			} else if (direction > 0) {
				mState = State.DOWN;
			} else {
				mState = State.NOMAL;
			}

			if (mState == State.UP) {
				if (mCurrenImageHeight <= mInitImageHeight) {
					isScroll = true;
					mIsMoveing = false;
				}

			} else if (mState == State.DOWN) {
				if (getScrollY() == 0 && 0 < mDeltaY) {
					isScroll = false;
					mIsMoveing = true;
				} else {
					isScroll = true;
					mIsMoveing = false;
				}
				mDeltaY = mDeltaY < 0 ? 0 : mDeltaY;
			}

			if (mIsMoveing) {
				// 移动布局(手势移动的1/3)
				mInnerMoveH = (int) (mDeltaY / 5);
				mInner.scrollTo(0, -mInnerMoveH);
				/** image_bg **/
				// 图片高度(移动布局的1/1)
				float image_move_H = mInnerMoveH;
				android.view.ViewGroup.LayoutParams params = mImageView
						.getLayoutParams();
				mCurrenImageHeight = mInitImageHeight + image_move_H;
				params.height = (int) (mCurrenImageHeight);
				mImageView.setLayoutParams(params);
			}
			break;
		case MotionEvent.ACTION_UP:
			isScroll = true;
			if (getScrollY() == 0) {
				mState = State.NOMAL;
			}
			/** 回缩动画 **/
			if (isNeedAnimation()) {
				animation();
				mIsMoveing = false;
				return true;

			}

			mIsMoveing = false;
			break;
		default:
			break;

		}
		return false;
	}

	/***
	 * 回缩动画
	 */
	public void animation() {
		// 开启图片动画
		ScaleAnimation image_Anim = new ScaleAnimation(1, 1, mCurrenImageHeight
				/ mInitImageHeight, 1);
		image_Anim.setDuration(200);
		mImageView.startAnimation(image_Anim);
		android.view.ViewGroup.LayoutParams params = mImageView
				.getLayoutParams();
		params.height = (int) (mInitImageHeight);
		mImageView.setLayoutParams(params);
		// 开启移动动画
		TranslateAnimation inner_Anim = new TranslateAnimation(0, 0,
				mInnerMoveH, 0);
		inner_Anim.setDuration(200);
		mInner.startAnimation(inner_Anim);
		mInner.scrollTo(0, 0);

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mImageView == null)
			return;
		if (Math.abs(getScrollY()) < maxHeight * 1.1) {
			if (!mIsMoveing) {
				int i = (int) (0.4 * (getScrollY()));
				mImageView.scrollTo(0, i);

			}
		}
	}

	/** 是否需要开启动画 **/
	public boolean isNeedAnimation() {
		return mIsMoveing;
	}

	/***
	 * 执行翻转
	 * 
	 * @author jia
	 * 
	 */
	public interface onTurnListener {

		/** 必须达到一定程度才执行 **/
		void onTurn();
	}

	// 注入背景图
	public void setImageView(ImageView imageView) {
		this.mImageView = imageView;
	}

	public void setMaxHeight(int height) {

		this.maxHeight = dip2px(getContext(), height);
	}

	public int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

}
