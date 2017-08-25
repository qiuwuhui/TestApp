package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;

/**
 * 设计师个人 ListView 头部
 * 
 * @author JiSheng.Guo
 * 
 */
public class MyIntegView extends FrameLayout{
	private String NEWS_CLASS = "NEWS_CLASS";
//	private CommonAdapter<Items> hzAdapter;

	public MyIntegView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public MyIntegView(Context context) {
		super(context);
		initView();
	}
	boolean isMeasure = false;
	private TextView tvNumber;


	private void initView() {
		View view = View.inflate(getContext(), R.layout.view_integra, null);
	
	}

	
}
