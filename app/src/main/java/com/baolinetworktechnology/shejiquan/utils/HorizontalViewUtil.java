package com.baolinetworktechnology.shejiquan.utils;

import java.util.ArrayList;

import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 横向滚动条辅助工具（实现点击rb 进行滚动）
 * 
 * @author JiSheng.Guo
 * 
 */

public class HorizontalViewUtil implements OnCheckedChangeListener {
	private HorizontalSelectedListener mHorizontalSelectedId;// 选中
	private ArrayList<RadioButton> mRadioButtonList = new ArrayList<RadioButton>();// RadioButton集合

	/**
	 * 设置监听器，回调0开始的position
	 * 
	 * @param selectedListener
	 */
	public void setOnHorizontalSelectedListener(
			HorizontalSelectedListener selectedListener) {
		this.mHorizontalSelectedId = selectedListener;
	}

	/**
	 * 添加RadioButton
	 * 
	 * @param radioButton
	 *            选项
	 */
	public void addRadioButton(RadioButton radioButton) {
		mRadioButtonList.add(radioButton);

	}

	/**
	 * 
	 * @param radioGroup
	 * @param horizontalView
	 */
	public void setHorizontalView(RadioGroup radioGroup,
			HorizontalScrollView horizontalView) {
		horizontalScrollView = horizontalView;
		radioGroup.setOnCheckedChangeListener(this);
		// 初始化选中第一个

		mRadioButtonList.get(0).setChecked(true);
	}

	private HorizontalScrollView horizontalScrollView;

	// 进行滚动
	private void scrollTo() {
		int size = mRadioButtonList.size();
		for (int i = 0; i < size; i++) {
			RadioButton rb = mRadioButtonList.get(i);
			if (rb.isChecked()) {

				int let = 0;
				let = ((i - 2) * rb.getWidth());
				rb.setTextSize(18);
				if (mHorizontalSelectedId != null)
					mHorizontalSelectedId.selectedId(i, rb);
				horizontalScrollView.smoothScrollTo(let, 0);

			} else {
				// 没有选中的 设置小号字体
				rb.setTextSize(16);
			}
		}

	}

	// 监听器
	public interface HorizontalSelectedListener {
		public void selectedId(int position, RadioButton rb);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		scrollTo();
	}

	public void removeAllViews() {
		// TODO Auto-generated method stub
		mRadioButtonList.clear();
	}

}
