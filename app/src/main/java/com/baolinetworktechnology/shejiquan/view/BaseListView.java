package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;

public abstract class BaseListView extends PullToRefreshListView {
	int mPageIndex = 1, mPageSize = 15;
	boolean mIsRecord = true;// 是否要记录
	protected PulldownFooter mPulldownFooter;

	public PulldownFooter getPulldownFooter() {
		// TODO Auto-generated method stub
		return mPulldownFooter;
	}

	// 点击底部View
	public abstract void clickFooterView();

	// 点刷新数据
	public void onRefreshData() {
		initTips();
		loadData(true);
	};

	/**
	 * 加载数据
	 * 
	 * @param isRefresh
	 *            是否刷新
	 */
	public abstract void loadData(boolean isRefresh);

	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public BaseListView(Context context) {
		super(context);
		initView();
	}

	void initView() {
		View view = View.inflate(getContext(), R.layout.pulldown_footer, null);
		mPulldownFooter = new PulldownFooter(getContext(), view);
		// 下拉刷新
		setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				onRefreshData();
			}

		});

		/**
		 * 加载更多
		 */
		setOnScrollListener(new OnScrollListener() {

			private boolean is_divPage;

			@Override
			public void onScrollStateChanged(AbsListView view1, int scrollState) {
				/**
				 * 如果等到该分页（is_divPage = true）的时候，并且滑动停止（这个时候手已经离开了屏幕），自动加载更多。
				 */
				if (is_divPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					mPulldownFooter.setState(PulldownFooter.STATE_LOGING);
					loadData(false);
				} else if (!is_divPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 空闲状态
					mPulldownFooter.setState(PulldownFooter.STATE_FREE);
				}
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					onScrollStop();
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});
		getRefreshableView().addFooterView(view);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clickFooterView();
			}
		});
		initTips();
	}

	/**
	 * 刷新数据
	 */
	void doRefresh() {
		mPageIndex = 1;
		initTips();

		loadData(true);

	}

	protected void onScrollStop() {

	}

	public void initTips() {
		String label = DateUtils.formatDateTime(getContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		// 显示最后更新的时间
		getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + label);
		/*
		 * getRefreshableView().setDivider(
		 * getResources().getDrawable(R.color.acyivity_bg));
		 */

		// 设置提示消息
		ILoadingLayout endLabels = getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("上拉加载更多");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入中...");// 刷新时
		endLabels.setReleaseLabel("放开加载更多");// 下来达到一定距离时，显示的提示

	}

	HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		httpUtil.configDefaultHttpCacheExpiry(1000 * 1);
		return httpUtil;

	}

	RequestParams getParams(String url) {
		RequestParams params = new RequestParams();
		if (SJQApp.user == null) {
			params.setHeader("Token", null);
		} else {
//			params.setHeader("Token", SJQApp.user.Token);
		}
		params.setHeader("Version", ApiUrl.VERSION);
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		params.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));

		return params;

	}

	/**
	 * 设置刷新数据
	 */
	public void setOnRefreshing() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				setRefreshing(true);
			}
		}, 220);

	}

	/**
	 * 设置刷新数据
	 */
	public void setOnRefreshing(int time) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				setRefreshing(true);

			}
		}, time);

	}

}
