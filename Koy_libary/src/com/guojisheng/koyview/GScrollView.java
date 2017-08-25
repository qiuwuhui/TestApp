package com.guojisheng.koyview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * 自定义ScrollView 实现 滑动时头部图片伸缩
 * 
 * @author JiSheng.Guo
 * 
 */
public class GScrollView extends ScrollView implements OnTouchListener {
	private IRefresh mIRefresh;
	public interface IRefresh {
		void onRefresh(int mInnerMoveH);
	}

	public void setOnRefreshListener(IRefresh iRefresh) {
		mIRefresh = iRefresh;
	}
	private ScrollListener mListener;
	public static interface ScrollListener {//声明接口，用于传递数据  
        public void scrollOritention(int l, int t, int oldl, int oldt);  
    }  
	private View mInner;// 孩子View

	private ImageView mImageView;// 背景图控件.

	private float mDeltaY;// Y轴滑动的距离

	private float mInitTouchY;// 首次点击的Y坐标

	private boolean mIsMoveing = false;// 是否开始移动.

	private int mInitImageHeight = 0;// 图片初始高度

	private float mCurrenImageHeight;

	private int mInnerMoveH;

	// private int maxHeight = 300;// 滑动最大距离

	boolean isScroll = true;

	// 默认状态
	private State mState = State.NOMAL;

	private float mPreTouchY;

	private Scroller mScroller;

	private float DAMPING = 3f;// 阻尼

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
	public GScrollView(Context context, AttributeSet attrs) {
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
		mScroller = new Scroller(getContext(), new DecelerateInterpolator());
		setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent ev) {
		if (mInner != null) {
			if (commOnTouchEvent(ev)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/*
	 * @Override public boolean dispatchTouchEvent(MotionEvent ev) { if (mInner
	 * != null) { if (commOnTouchEvent(ev)) { System.out.println("-->>false");
	 * return true; }else{ System.out.println("-->>super"); return
	 * super.dispatchTouchEvent(ev); }
	 * 
	 * } return super.dispatchTouchEvent(ev); }
	 */

	/***
	 * 触摸事件
	 * 
	 * @param ev
	 */
	// 手指按下时记录是否可以继续下拉
	// private boolean mCanPullDown = false;

	public boolean commOnTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:

			mPreTouchY = mInitTouchY = ev.getY();
			mScrollBack = -1;
			// mCanPullDown = isCanPullDown();
			break;

		/***
		 * 排除出第一次移动计算，因为第一次无法得知deltaY的高度， 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0.
		 * 之后记录准确了就正常执行.
		 */
		case MotionEvent.ACTION_MOVE:
			float touchY = ev.getY();
			if (getScrollY() == 0) {
				if (mInitTouchY == 0) {
					// 被其他控件拦截了，没有获取到事件
					mInitTouchY = touchY;
				}
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

			move();
			return mIsMoveing;
		case MotionEvent.ACTION_UP:
			mInitTouchY = 0;
			isScroll = true;
			if (getScrollY() == 0) {
				mState = State.NOMAL;
			}
			/** 回缩动画 **/
			if (isNeedAnimation()) {

				animation();
				mIsMoveing = false;
				return false;

			}

			mIsMoveing = false;
			break;
		default:
			break;

		}
		return false;
	}

	/**
	 * 判断是否滚动到顶部
	 */
	// private boolean isCanPullDown() {
	// return getScrollY() == 0
	// || mInner.getHeight() < getHeight() + getScrollY();
	// }

	private void move() {
		if (mIsMoveing) {
			// System.out.println("-->>mDeltaY"+mDeltaY);
			mInnerMoveH = (int) (mDeltaY / DAMPING);
			// mInner.scrollTo(0, -mInnerMoveH);
			mInner.setPadding(0, mInnerMoveH, 0, 0);
			if (mImageView != null) {
				if (mInitImageHeight == 0) {
					if (mImageView != null) {
						mInitImageHeight = mImageView.getHeight();
					}
				}
				android.view.ViewGroup.LayoutParams params = mImageView
						.getLayoutParams();
				mCurrenImageHeight = mInitImageHeight + mInnerMoveH;
				params.height = (int) (mCurrenImageHeight);
				mImageView.setLayoutParams(params);
			}
		}

	}

	protected final static int SCROLL_DURATION = 400; // 滚动回时间

	/***
	 * 回缩动画
	 */
	public void animation() {
		if (mIRefresh != null) {
			mIRefresh.onRefresh(mInnerMoveH);
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, mInnerMoveH, 0, -mInnerMoveH, SCROLL_DURATION);// 大
																				// 开始距离顶部距离大
		postInvalidate();

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mImageView == null)
			return;
		if (mListener != null) {  
            mListener.scrollOritention(l, t, oldl, oldt);  
        }  
		if (!mIsMoveing) {
			if (mInitImageHeight == 0) {
				mInitImageHeight = mImageView.getHeight();
			}
			if (Math.abs(getScrollY()) < mInitImageHeight) {
				int i = (int) (getScrollY() / DAMPING);
				mImageView.scrollTo(0, i);

			}
		}
	}
	public void setScrollListener(ScrollListener l) {  
        this.mListener = l;  
    }  
	/** 是否需要开启动画 **/
	public boolean isNeedAnimation() {
		return mIsMoveing;
	}

	/**
	 * 下拉回掉
	 * 
	 * @author Administrator
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

	/**
	 * 判断是否滚动到底部
	 */
	// private boolean isCanPullUp() {
	// return mInner.getHeight() <= getHeight() + getScrollY();
	// }

	protected int mScrollBack;// mScroller,滚动页眉或页脚。
	protected final static int SCROLLBACK_HEADER = 0;

	@Override
	public void computeScroll() {
		super.computeScroll();

		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				if (mImageView != null) {
					android.view.ViewGroup.LayoutParams params = mImageView
							.getLayoutParams();
					params.height = mInitImageHeight + mScroller.getCurrY();
					mImageView.setLayoutParams(params);
				}
				mInner.setPadding(0, mScroller.getCurrY(), 0, 0);
				// mInner.scrollTo(0, -mScroller.getCurrY());
				postInvalidate();

			}
		}
	}

	/**
	 * 设置最大滑动距离
	 * 
	 * @param height
	 */
	public void setMaxHeight(int height) {

		// this.maxHeight = dip2px(getContext(), height);
	}

	/*
	 * int dip2px(Context context, float dipValue) { final float scale =
	 * context.getResources().getDisplayMetrics().density; return (int)
	 * (dipValue * scale + 0.5f); }
	 */

}
