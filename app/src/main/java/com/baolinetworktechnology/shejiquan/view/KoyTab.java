package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.utils.WindowsUtil;

/**
 * 滑动TAB
 * 
 * @author gjs
 * 
 */
public class KoyTab extends RelativeLayout {
	private RadioButton mRb1, mRb2, mRb3, mRb4;
	private View mView;
	private OnTabChangeListener mOnTabChangeListener;
	private View viewTab;

	public KoyTab(Context context) {
		super(context);
		initView();
	}

	public KoyTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	void setWeightSum(int sum) {
		LinearLayout llayout = (LinearLayout) findViewById(R.id.divLinear);
		llayout.setWeightSum(sum);
		invalidate();
	}

	public void setShow2() {
		mRb3.setVisibility(View.GONE);
		setWeightSum(2);
	}

	/**
	 * 设置选择第几个TAB
	 * 
	 * @param index
	 *            下标 从0开始
	 */
	public void setTab(final int index) {
		this.mIndex = index;
		switch (index) {
		case 0:
			mRb1.setChecked(true);
			break;
		case 1:
			mRb2.setChecked(true);
			break;
		case 2:
			mRb3.setChecked(true);
			break;
		case 3:
			mRb4.setChecked(true);
			break;
		default:
			mRb1.setChecked(false);
			mRb2.setChecked(false);
			mRb3.setChecked(false);
			mRb4.setChecked(false);
			// mView.setVisibility(View.GONE);
			// return;
		}
		// if(mView.getVisibility()!=View.VISIBLE)
		// mView.setVisibility(View.VISIBLE);
		Runnable run = new Runnable() {

			@Override
			public void run() {
				KoyTab.this.mIndex = 0;
				anim(index, 300);
				KoyTab.this.mIndex = index;
			}
		};
		Handler handler = new Handler();
		handler.postDelayed(run, 200);

	}

	/**
	 * 设置Tab 显示的名称
	 * 
	 * @param index
	 *            下标
	 * @param text
	 *            名称
	 */
	public void setTabText(int index, String text) {
		switch (index) {
		case 0:
			mRb1.setText(text);
			break;

		case 1:
			mRb2.setText(text);
			break;
		case 2:
			mRb3.setText(text);
			break;
		case 3:
			mRb4.setVisibility(View.VISIBLE);
			mRb4.setText(text);
			setWeightSum(4);
			break;
		}
	}

	public void setTabText(String... text) {
		for (int i = 0; i < text.length; i++) {
			setTabText(i, text[i]);
		}
	}

	/**
	 * 设置Tab字体大小
	 * 
	 * @param size
	 */
	public void setTabTextSize(float size) {
		mRb1.setTextSize(size);
		mRb2.setTextSize(size);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

	}

	// 初始化
	private void initView() {
		viewTab = View.inflate(getContext(), R.layout.tab, null);
		RadioGroup rGroup = (RadioGroup) viewTab.findViewById(R.id.rGrop);
		if (rGroup == null)
			return;
		mRb1 = (RadioButton) rGroup.findViewById(R.id.rb1);
		mRb2 = (RadioButton) rGroup.findViewById(R.id.rb2);
		mRb3 = (RadioButton) rGroup.findViewById(R.id.rb3);
		mRb4 = (RadioButton) rGroup.findViewById(R.id.rb4);
		mView = viewTab.findViewById(R.id.view_div1);
		OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				onTabChange(group, checkedId);
			}
		};
		rGroup.setOnCheckedChangeListener(checkedChangeListener);// RadioGroup选中监听
		addView(viewTab);
	}

	int mIndex = 0;

	private void onTabChange(RadioGroup group, int checkedId) {
		int index = 0;
		switch (checkedId) {
		case R.id.rb1:
			index = 0;
			mView.setVisibility(View.VISIBLE);
			break;
		case R.id.rb2:
			index = 1;
			mView.setVisibility(View.INVISIBLE);
			break;
		case R.id.rb3:
			index = 2;
			mView.setVisibility(View.INVISIBLE);
			break;
		case R.id.rb4:
			index = 3;
			mView.setVisibility(View.INVISIBLE);
			break;
		}

		anim(index, 100);
		this.mIndex = index;
		if (mOnTabChangeListener != null) {
			mOnTabChangeListener.onTabChange(group, index);
		}

	}

	public interface OnTabChangeListener {
		public void onTabChange(RadioGroup group, int index);
	}

	// 0开始
	public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
		this.mOnTabChangeListener = onTabChangeListener;
	}

	// 开始动画
	boolean flag = true;

	public void anim(int id, int time) {
		int cursorWidth = mView.getWidth();
		Animation animation = new TranslateAnimation(cursorWidth * mIndex,
				cursorWidth * id, 0, 0);
		animation.setDuration(time);
		animation.setFillAfter(true);
		mView.startAnimation(animation);

	}

	// 获取radiobutton
	public RadioButton getTab(int index) {
		switch (index) {
		case 0:
			return mRb1;
		case 1:
			return mRb2;
		case 2:
			return mRb3;
		case 3:
			return mRb4;
		}
		return mRb1;
	}

	public int getCurrtent() {

		return mIndex;

	}

	public void setShowDivid() {
//		viewTab.findViewById(R.id.divid1).setVisibility(View.VISIBLE);
//		viewTab.findViewById(R.id.divid2).setVisibility(View.VISIBLE);
//		if (mRb4.getVisibility() == View.VISIBLE)
//			viewTab.findViewById(R.id.divid3).setVisibility(View.VISIBLE);
	}
}
