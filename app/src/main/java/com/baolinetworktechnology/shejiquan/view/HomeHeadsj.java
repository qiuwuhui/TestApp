package com.baolinetworktechnology.shejiquan.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.MyFragmentActivity;
import com.baolinetworktechnology.shejiquan.activity.WebActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopWebActivity;
import com.baolinetworktechnology.shejiquan.adapter.CommonAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewHolder;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.HouOrderList;
import com.baolinetworktechnology.shejiquan.domain.Items;
import com.baolinetworktechnology.shejiquan.domain.LunBoBean;
import com.baolinetworktechnology.shejiquan.net.BaseNet.OnCallBackList;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 设计师个人 ListView 头部
 * 
 * @author JiSheng.Guo
 * 
 */
public class HomeHeadsj extends FrameLayout{
	private String NEWS_CLASS = "NEWS_CLASS";
//	private CommonAdapter<Items> hzAdapter;

	public HomeHeadsj(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HomeHeadsj(Context context) {
		super(context);
		initView();
	}
	boolean isMeasure = false;
//	private TextView tvNumber;


	private void initView() {
		View view = View.inflate(getContext(), R.layout.sj_home_head, null);
//		tvNumber = (TextView) view.findViewById(R.id.tvNumber);
		addView(view);
	
	}

	
}
