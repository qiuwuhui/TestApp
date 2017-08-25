package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.utils.WindowsUtil;

/**
 * 字母索引栏
 * 
 * @author Administrator
 * 
 */
public class BladeView extends View {
	private OnItemClickListener mOnItemClickListener;
	String[] b = { "#", "☆","A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z" };//
	private int mChoose = -1;
	private Paint mPaint = new Paint();
	private boolean mShowBkg = false; // 是否弹框提示
	private PopupWindow mPopupWindow; // 弹出提示窗口
	private TextView mPopupText; // 弹窗文字
	private Handler mHandler = new Handler();

	public BladeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BladeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BladeView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mShowBkg) {
			canvas.drawColor(Color.parseColor("#00000000"));
		}

		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / b.length; // 单个字母的高度
		for (int i = 0; i < b.length; i++) { // 循环绘制每个字母
			mPaint.setColor(Color.parseColor("#545454"));
			mPaint.setTypeface(Typeface.DEFAULT);
			mPaint.setFakeBoldText(true);
			mPaint.setAntiAlias(true);

			mPaint.setTextSize(getResources().getDimension(
					R.dimen.blade_view_text));
			if (i == mChoose) {
				mPaint.setColor(Color.parseColor("#FF575F"));
			}
			float xPos = width / 2 - mPaint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(b[i], xPos, yPos, mPaint);
			mPaint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = mChoose;
		final int c = (int) (y / getHeight() * b.length);

		switch (action) {
		case MotionEvent.ACTION_DOWN: // 按下
			mShowBkg = true;
			if (oldChoose != c) {
				if (c > 0 && c < b.length) {
					performItemClicked(c); // 执行点击事件
					mChoose = c;
					invalidate(); // 刷新
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:// 移动
			if (oldChoose != c) {
				if (c > 0 && c < b.length) {
					performItemClicked(c);
					mChoose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP: // 放开
			mShowBkg = false;
			mChoose = -1;
			dismissPopup(); // 关闭弹窗口
			invalidate();
			break;
		}
		return true;
	}

	private void showPopup(int item) { // 显示弹窗
		if (mPopupWindow == null) {
			mHandler.removeCallbacks(dismissRunnable);
			mPopupText = new TextView(getContext());
			mPopupText.setBackgroundColor(0x9F888888);
			mPopupText.setTextColor(Color.WHITE);
			mPopupText.setTextSize(40);
			mPopupText.setGravity(Gravity.CENTER_HORIZONTAL
					| Gravity.CENTER_VERTICAL);
			int size = WindowsUtil.dip2px(getContext(), 60);
			mPopupWindow = new PopupWindow(mPopupText, size, size);
		}

		String text = b[item];
		// if (item == 0) {
		// text = "#";
		// } else {
		// text = Character.toString((char) ('A' + item - 1));
		// }
		mPopupText.setText(text);
		if (mPopupWindow.isShowing()) {
			mPopupWindow.update();
		} else {
			mPopupWindow.showAtLocation(getRootView(),
					Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
		}
	}

	private void dismissPopup() { // 关闭弹窗
		mHandler.postDelayed(dismissRunnable, 800);
	}

	Runnable dismissRunnable = new Runnable() {

		@Override
		public void run() {

			if (mPopupWindow != null) {
				if (!isDeta)
					mPopupWindow.dismiss();
			}
		}
	};

	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public void setOnItemClickListener(OnItemClickListener listener) { // 点击监听
		mOnItemClickListener = listener;
	}

	private void performItemClicked(int item) {
		if (mOnItemClickListener != null) {
			if (item == 1) {
				mOnItemClickListener.onItemClick("热门");
			} else {
				mOnItemClickListener.onItemClick(b[item]);
			}
			showPopup(item);
		}
	}

	public interface OnItemClickListener {
		void onItemClick(String s);
	}

	@Override
	protected void onDetachedFromWindow() {
		isDeta = true;
		super.onDetachedFromWindow();

	}

	boolean isDeta = false;
}
