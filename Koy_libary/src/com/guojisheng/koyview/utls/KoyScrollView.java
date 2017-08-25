package com.guojisheng.koyview.utls;

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

	private View inner;// 孩子View

	private float deltaY;// Y轴滑动的距离

	private float initTouchY;// 首次点击的Y坐标

	private boolean isMoveing = false;// 是否开始移动.

	private ImageView imageView;// 背景图控件.

	private int initImageHeight = 0;// 图片初始高度

	private float currenImageHeight;

	private int inner_move_H;

	private int maxHeight = 300;// 滑动最大距离

	// 状态：上部，下部，默认
	private enum State {
		UP, DOWN, NOMAL
	};

	// 默认状态
	private State state = State.NOMAL;

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
			inner = getChildAt(0);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (imageView != null && inner != null) {
			commOnTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	/***
	 * 触摸事件
	 * 
	 * @param ev
	 */
	public void commOnTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (initImageHeight == 0) {
				initImageHeight = imageView.getHeight();
			}
			initTouchY = ev.getY();

			break;

		/***
		 * 排除出第一次移动计算，因为第一次无法得知deltaY的高度， 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0.
		 * 之后记录准确了就正常执行.
		 */
		case MotionEvent.ACTION_MOVE:

			float touchY = ev.getY();
			deltaY = touchY - initTouchY;// 滑动距离

			/** 对于首次Touch操作要判断方位：UP OR DOWN **/
			if (deltaY < 0) {
				state = State.UP;
			} else if (deltaY > 0) {
				state = State.DOWN;
			}

			if (state == State.UP) {
				deltaY = deltaY < 0 ? deltaY : 0;
				isMoveing = false;

			} else if (state == state.DOWN) {
				if (getScrollY() <= deltaY) {
					isMoveing = true;

				}
				deltaY = deltaY < 0 ? 0 : deltaY;
			}

			if (isMoveing) {
				// 移动布局(手势移动的1/3)
				inner_move_H = (int) (deltaY / 5);
				inner.scrollTo(0, -inner_move_H);
				/** image_bg **/
				// 图片高度(移动布局的1/2)
				float image_move_H = inner_move_H / 2;
				android.view.ViewGroup.LayoutParams params = imageView
						.getLayoutParams();
				currenImageHeight = initImageHeight + image_move_H;
				params.height = (int) (currenImageHeight);
				imageView.setLayoutParams(params);

			}
			break;
		case MotionEvent.ACTION_UP:
			/** 回缩动画 **/
			if (isNeedAnimation()) {
				animation();
			}

			if (getScrollY() == 0) {
				state = State.NOMAL;
			}

			isMoveing = false;
			break;
		default:
			break;

		}
	}

	/***
	 * 回缩动画
	 */
	public void animation() {
		// 开启图片动画
		ScaleAnimation image_Anim = new ScaleAnimation(1, 1, currenImageHeight
				/ initImageHeight, 1);
		image_Anim.setDuration(200);
		imageView.startAnimation(image_Anim);
		android.view.ViewGroup.LayoutParams params = imageView
				.getLayoutParams();
		params.height = (int) (initImageHeight);
		imageView.setLayoutParams(params);
		// 开启移动动画
		TranslateAnimation inner_Anim = new TranslateAnimation(0, 0,
				inner_move_H, 0);
		inner_Anim.setDuration(200);
		inner.startAnimation(inner_Anim);
		inner.scrollTo(0, 0);

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (imageView == null)
			return;
		if (Math.abs(getScrollY()) < maxHeight) {
			if (!isMoveing) {
				int i = (int) (0.4 * (getScrollY()));
				imageView.scrollTo(0, i);
			}
		}
	}

	/** 是否需要开启动画 **/
	public boolean isNeedAnimation() {
		return isMoveing;
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
		this.imageView = imageView;
	}

}
