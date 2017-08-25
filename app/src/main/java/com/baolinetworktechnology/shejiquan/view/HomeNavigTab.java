package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import com.baolinetworktechnology.shejiquan.R;
/**
 * 首页底部导航栏
 * 
 * @author JiSheng.Guo
 * 
 */
public class HomeNavigTab extends RelativeLayout implements
		OnCheckedChangeListener, OnClickListener {
	RadioButton mRbInsp, mRbWorks, mRbDesigner, mRbMe;
	RadioGroup mRgAll;
	NavigTabListener mNavigTabListener;
	boolean isCheckedChanged = false;// 选中监听

	/**
	 * 设置监听器
	 *
	 * @param navigTabListener
	 */
	public void setOnNavigTabListener(NavigTabListener navigTabListener) {
		this.mNavigTabListener = navigTabListener;
	}

	public HomeNavigTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		View view = View.inflate(getContext(), R.layout.navi_tab_news, null);
		mRgAll = (RadioGroup) view.findViewById(R.id.ma_rg_all);
		mRbInsp = (RadioButton) view.findViewById(R.id.ma_rb_insp);
		mRbWorks = (RadioButton) view.findViewById(R.id.ma_rb_works);
		mRbDesigner = (RadioButton) view.findViewById(R.id.ma_rb_designer);
		mRbMe = (RadioButton) view.findViewById(R.id.ma_rb_me);
		mRgAll.setOnCheckedChangeListener(this);

		mRbInsp.setOnClickListener(this);
		mRbWorks.setOnClickListener(this);
		mRbDesigner.setOnClickListener(this);
		mRbMe.setOnClickListener(this);

		addView(view);

	}

	/**
	 * 监听器
	 *
	 * @author JiSheng.Guo
	 */
	public interface NavigTabListener {

		public void onTabCheckedChangeListener(RadioGroup group, int checkedId);

		public void onTabClickListener(int position);
	}

	/**
	 * 设置选中Tab
	 *
	 * @param index （0到4个）
	 * @return
	 */
	public boolean setCheckedTab(int index) {
		boolean fage = false;
		index = index + 1;
		switch (index) {
			case 1:// 首页
				mRbInsp.setChecked(true);
				fage = true;
				break;

			case 2://图库
				mRbWorks.setChecked(true);
				fage = true;
				break;

//	    	case 3:// 发现
//			    mRbDesigner.setChecked(true);
//			    fage = true;
//			   break;

			case 3:// 我的
				mRbMe.setChecked(true);
				fage = true;
				break;
			case 7:// 我的
				mRbDesigner.setChecked(true);
				fage = true;
				break;
			default:
				break;
		}

		return fage;

	}

	/**
	 * 导航RadioGroup改变监听
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		isCheckedChanged = true;
		if (mNavigTabListener != null) {
			int checkedIndex = 0;
			switch (checkedId) {
				case R.id.ma_rb_insp:// 首页
					checkedIndex = 0;
					break;
				case R.id.ma_rb_works:// 作品
					checkedIndex = 1;
					break;
				case R.id.ma_rb_designer:// 订单
					checkedIndex = 2;
					break;
				case R.id.ma_rb_me:// 设计师
					checkedIndex = 3;
					break;
//			case R.id.ma_rb_me:// 我的
//				checkedIndex = 4;
//				break;
			}
			mNavigTabListener.onTabCheckedChangeListener(group, checkedIndex);
		}

	}

	@Override
	public void onClick(View v) {
		if (mNavigTabListener != null) {
			int checkedIndex = 0;
			switch (v.getId()) {
				case R.id.ma_rb_insp:// 首页
					checkedIndex = 0;
					break;
				case R.id.ma_rb_works:// 作品
					checkedIndex = 1;
					break;
				case R.id.ma_rb_designer:// 订单
					checkedIndex = 2;
					break;
				case R.id.ma_rb_me:// 设计师
					checkedIndex = 3;
					break;
//			case R.id.ma_rb_me:// 我的
//				checkedIndex = 4;
//				break;
			}

			if (!isCheckedChanged) {
				mNavigTabListener.onTabClickListener(checkedIndex);
			} else {
				isCheckedChanged = false;
			}
		}
	}
}
