package com.guojisheng.koyview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guojisheng.koyview.GScrollView.ScrollListener;
import com.guojisheng.koyview.domain.ChannelItem;
import com.guojisheng.koyview.utls.WindowsUtil;

public class ChanneView extends RelativeLayout {
	OnChanneClickListener onChanneClickListener;
	public int post=0;
	public interface OnChanneClickListener {
		public void onChanneClickListener(View v, int index);
	}

	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	/** 左阴影部分 */
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 用户选择的新闻分类列表 */
	private ArrayList<ChannelItem> userChannelList;
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	private ColumnHorizontalScrollView mColumnHorizontalScrollView1;
	private LinearLayout ll_more_columns;
	private RelativeLayout rl_column;
	private LinearLayout mRadioGroup_content;
	private LinearLayout mRadioGroup_content1;
	public void setOnChanneClickListener(
			OnChanneClickListener onChanneClickListener) {
		this.onChanneClickListener = onChanneClickListener;
	}

	public ChanneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView();
	}

	public void setData(Activity ac, ArrayList<ChannelItem> userChannelList) {
		this.userChannelList = userChannelList;
		initTabColumn(ac);
	}
	public int getPOst() {		
		return post;
	}
	private void iniView() {
		// 初始化控件
		View view = View.inflate(getContext(), R.layout.view_koy, null);
		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) view
				.findViewById(R.id.mColumnHorizontalScrollView);
		mColumnHorizontalScrollView1 = (ColumnHorizontalScrollView) view
				.findViewById(R.id.mColumnHorizontalScrollView1);
		mColumnHorizontalScrollView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				return false;
			}
		});
		mColumnHorizontalScrollView1.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				return true;
			}
		});
		mColumnHorizontalScrollView1.setVisibility(View.GONE);
		mRadioGroup_content = (LinearLayout) view
				.findViewById(R.id.mRadioGroup_content);
		mRadioGroup_content1 = (LinearLayout) view
				.findViewById(R.id.mRadioGroup_content1);
		shade_left = (ImageView) view.findViewById(R.id.shade_left);
		shade_right = (ImageView) view.findViewById(R.id.shade_right);
		ll_more_columns = (LinearLayout) view
				.findViewById(R.id.ll_more_columns);
		rl_column = (RelativeLayout) view.findViewById(R.id.rl_column);
		ll_more_columns.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onChanneClickListener != null)
					onChanneClickListener.onChanneClickListener(v, -1);
			}
		});

		addView(view);
//		mColumnHorizontalScrollView1.setScrollListener(new ScrollListener() {
//
//			@Override
//			public void scrollOritention(int l, int t, int oldl, int oldt) {
//			    .scrollTo(l, t);
//
//			}
//		});
//		.setScrollListener(new ScrollListener() {
//
//			@Override
//			public void scrollOritention(int l, int t, int oldl, int oldt) {
//				1.scrollTo(l, t);
//
//			}
//		});
	}

	private void initTabColumn(Activity ac) {
		mRadioGroup_content1.removeAllViews();
		mRadioGroup_content.removeAllViews();
		mScreenWidth = WindowsUtil.getWindowsWidth(ac);
		mItemWidth = mScreenWidth / 5;// 一个Item宽度为屏幕的1/7
		int count = userChannelList.size();

		mColumnHorizontalScrollView.setParam(ac, mScreenWidth,
				mRadioGroup_content, shade_left, shade_right, ll_more_columns,
				rl_column);
		mColumnHorizontalScrollView1.setParam(ac, mScreenWidth,
				mRadioGroup_content1, shade_left, shade_right, ll_more_columns,
				rl_column);
		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 20;
			params.rightMargin = 20;
			TextView columnTextView = new TextView(ac);
			columnTextView.setTextSize(16);
			columnTextView.setGravity(CENTER_HORIZONTAL);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(0, 5, 0, 5);
			columnTextView.setId(userChannelList.get(i).getId());
			columnTextView.setText(userChannelList.get(i).getName());
			columnTextView.setTag(userChannelList.get(i).isClassId);
			columnTextView.setTextColor(getResources().getColorStateList(
					R.color.selector_btn_text_soll));

			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
					mItemWidth, LayoutParams.WRAP_CONTENT);
//			params1.leftMargin = 0;
//			params1.rightMargin = 0;
			TextView columnTextView1 = new TextView(ac);
			columnTextView1.setTextAppearance(ac,
					R.style.top_category_scroll_view_item_text);
			columnTextView1.setBackgroundResource(R.drawable.textview_pass);
			columnTextView1.setGravity(Gravity.CENTER);
			columnTextView.setPadding(0, 5, 0, 5);
			columnTextView1.setText(userChannelList.get(i).getName());
			columnTextView1.setTextColor(0xffffffff);
			
			if (columnSelectIndex == i) {
				columnTextView1.setSelected(true);
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
						View localView = mRadioGroup_content.getChildAt(i);
						View localView1 = mRadioGroup_content1.getChildAt(i);
						TextView textsize = (TextView) mRadioGroup_content.getChildAt(i);
						if (localView != v){
							localView.setSelected(false);
						    localView1.setSelected(false);
							textsize.setTextSize(16);
						}
						else {
							localView.setSelected(true);
							localView1.setSelected(true);
							textsize.setTextSize(17);
							selectTab(i);
							post=i;
						}
					}
					if (onChanneClickListener != null)
						onChanneClickListener.onChanneClickListener(v,
								v.getId());
				}
			});
			mRadioGroup_content.addView(columnTextView, i, params);
			mRadioGroup_content1.addView(columnTextView1, i, params1);
		}

	}

	private int columnSelectIndex = 0;

	/**
	 * 选择的Column里面的Tab
	 * */
	public void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			
			
			mColumnHorizontalScrollView1.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
		// 判断是否选中
		for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			View checkView1 = mRadioGroup_content1.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
			checkView1.setSelected(ischeck);
		}
	}

	public void netChanne() {
		if (columnSelectIndex != mRadioGroup_content.getChildCount() - 1) {
			selectTab(columnSelectIndex + 1);
			if (onChanneClickListener != null) {
				View v = mRadioGroup_content.getChildAt(columnSelectIndex);
				onChanneClickListener.onChanneClickListener(v, v.getId());
			}
		}

	}

	public void preChanne() {

		if (columnSelectIndex != 0) {
			selectTab(columnSelectIndex - 1);
			if (onChanneClickListener != null) {
				View v = mRadioGroup_content.getChildAt(columnSelectIndex);
				onChanneClickListener.onChanneClickListener(v, v.getId());
			}
		}
	}
}
