package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.NineGridTest2Adapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;

/**
 * 设计师个人 ListView 头部
 * 
 * @author JiSheng.Guo
 * 
 */
public class HomePost extends FrameLayout{
	private String NEWS_CLASS = "NEWS_CLASS";
	public HomePost(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HomePost(Context context) {
		super(context);
		initView();
	}
	private void initView() {
		View view = View.inflate(getContext(), R.layout.post_home_head, null);
		addView(view);
	}
}
