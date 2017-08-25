package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopWebActivity;
import com.baolinetworktechnology.shejiquan.adapter.RecordAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.RecordDesigner;
import com.baolinetworktechnology.shejiquan.domain.VisitBean;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ListViewRecord extends MyListView {
	private RecordAdapter mAdapter;
	private HttpUtils mHttpUtils;
	private TextView mFooterTitle;
	private TextView mTvDay;
	private TextView mTvAll;
	private View mPulldownFooterLoading;
	protected boolean mIsDivPage;

	public ListViewRecord(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView();
	}

	private void initView() {
		mHttpUtils = getHttpUtils();
		setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// // 下拉刷新
				setRefreshLabe();

				loadData(false);

			}

		});

		// 设置适配器
		mAdapter = new RecordAdapter(getContext());
		setAdapter(mAdapter);

		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				RecordDesigner item = (RecordDesigner) mAdapter
						.getItem(position - 1);
				if (item != null) {
					if (!item.isDesigner()) {
						return;
					}
					Intent intent = new Intent(getContext(),
							WeiShopActivity.class);
					intent.putExtra("IS_MY", false);
					// Intent intent = new Intent(getContext(),
					// MyFragmentActivity.class);
					// intent.putExtra(MyFragmentActivity.TYPE,
					// MyFragmentActivity.MICRO);
					if (IS_WHO_AT_LOOK) {
						// intent.putExtra(MyFragmentActivity.USER_GUID,item.VisitorGUID);
						intent.putExtra(AppTag.TAG_GUID, item.VisitorGUID);
					} else {
						// intent.putExtra(MyFragmentActivity.USER_GUID,
						// item.UserGUID);
						intent.putExtra(AppTag.TAG_GUID, item.UserGUID);
					}
                    intent.putExtra(AppTag.TAG_ID, item.ID+"");
					getContext().startActivity(intent);
				}

			}
		});

		final View view = View.inflate(getContext(), R.layout.pulldown_footer,
				null);
		mFooterTitle = (TextView) view.findViewById(R.id.pulldown_footer_text);
		mPulldownFooterLoading = view
				.findViewById(R.id.pulldown_footer_loading);
		getRefreshableView().addFooterView(view);
		view.setOnClickListener(null);
		setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view1, int scrollState) {
				/**
				 * 如果等到该分页（is_divPage = true）的时候，并且滑动停止（这个时候手已经离开了屏幕），自动加载更多。
				 */
				if (mIsDivPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					loadData(true);
					view.setVisibility(View.VISIBLE);
					mFooterTitle.setText("更多数据正在加载中");

					mPulldownFooterLoading.setVisibility(View.VISIBLE);

				} else if (!mIsDivPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {

					mPulldownFooterLoading.setVisibility(View.GONE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mIsDivPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});

		//mAdapter.setData(data2);

	}

	private void setRefreshLabe() {
		String label = DateUtils.formatDateTime(getContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		// 显示最后更新的时间
		getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + label);

	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
		setCaceKey("Record"+userGuid);
		setOnRefreshing();
	}

	public final static int LOOK_AT_WHO = 0;// 我看了谁
	public final static int WHO_AT_LOOK = 1;// 谁看了我
	boolean IS_WHO_AT_LOOK = true;

	public void setUrlType(int type) {
		switch (type) {
		case LOOK_AT_WHO:
			mUrl = ApiUrl.GET_VISIT_LIST;
			IS_WHO_AT_LOOK = true;
			break;
		default:
			mUrl = ApiUrl.GET_ME_VISIT;
			IS_WHO_AT_LOOK = false;
			break;
		}
		loadData(false);
	}

	private int PageIndex = 1;
	private boolean isLoading = false;
	private HttpHandler<String> handler;
	private boolean isCace;
	private String CACE_KEY = "";
	private String mUrl = null;
	private String userGuid;

	private void loadData(final boolean falg) {
		if (!falg) {
			// 不是加载更多
			PageIndex = 1;
			isLoading = false;
			setRefreshLabe();
			setRefreshing();
			mFooterTitle.setText("");
			// 如果之前的线程没有完成
			if (handler != null
					&& handler.getState() != HttpHandler.State.FAILURE
					&& handler.getState() != HttpHandler.State.SUCCESS
					&& handler.getState() != HttpHandler.State.CANCELLED) {
				// 关闭handler后 onStart()和onLoading()还是会执行
				handler.cancel();

			}
		} else {
			if (isLoading) {
				return;
			}
			mPulldownFooterLoading.setVisibility(View.VISIBLE);
			mFooterTitle.setText("数据加载中");
			PageIndex++;
		}

		if (mUrl == null) {
			mUrl = ApiUrl.GET_VISIT_LIST;
		}
		// +"&PageSize=30"
		final String url = mUrl + userGuid + "&PageIndex=" + PageIndex + "&r="
				+ System.currentTimeMillis();
		handler = mHttpUtils.send(HttpMethod.GET, ApiUrl.API + url,
				getParams(url), new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						onRefreshComplete();
						if (falg) {
							PageIndex--;
							// footerTitle.setText("网络请求错误");
							mPulldownFooterLoading.setVisibility(View.GONE);
						}
						mFooterTitle.setText("网络请求错误");
						isLoading = false;
						if (mTvDay != null) {
							mTvAll.setText("0");
							mTvDay.setText("0");
						}

						AppErrorLogUtil.getErrorLog(getContext()).postError(
								error.getExceptionCode() + "", "GET", url);
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						if (mFooterTitle == null)
							return;
						if (PageIndex == 1) {

						}

						isLoading = false;
						VisitBean data = CommonUtils.getDomain(n,
								VisitBean.class);
						mFooterTitle.setText("加载完毕");
						if (data != null && data.data != null) {
							if (mTvDay != null) {
								mTvAll.setText(data.totalsize + "");
								mTvDay.setText(data.todaysize + "");
							}
							if (!falg) {
								mAdapter.setData(data.data);

								if (data.data.size() == 0) {
									mFooterTitle.setText("加载完毕");
								}
								mPulldownFooterLoading.setVisibility(View.GONE);

								// 进行缓存
								if (!"".equals(CACE_KEY)) {
									if (!isCace) {
										CacheUtils.cacheStringData(
												getContext(), "List",CACE_KEY,
												n.result);
										isCace = true;
									}
								}
							} else {
								if (data.data.size() == 0) {
									mFooterTitle.setText("已经全部加载完毕");
									mPulldownFooterLoading
											.setVisibility(View.GONE);
								}
								mAdapter.addData(data.data);
							}

						} else {
							if (falg) {
								PageIndex--;
							}
							mFooterTitle.setText("没有发现数据");
							mPulldownFooterLoading.setVisibility(View.GONE);
						}
						// dialog.dismiss();
						onRefreshComplete();
					}
				});
	}

	// 设置是否缓存
	private void setCaceKey(String caceKey) {
		CACE_KEY=caceKey;
		// 缓存
		String cace = CacheUtils.getStringData(getContext(), "List",CACE_KEY, null);
		if (cace != null) {
			showZT(cace);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		mFooterTitle=null;
		super.onDetachedFromWindow();
	}
	
	private void showZT(String cace) {
		VisitBean newsBean = CommonUtils.getDomain(cace, VisitBean.class);
		if (newsBean != null && newsBean.data != null) {
			mAdapter.setData(newsBean.data);
		}

	}

	private HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		httpUtil.configDefaultHttpCacheExpiry(1000 * 1);
		return httpUtil;

	}

	private RequestParams getParams(String url) {
		RequestParams params = new RequestParams();
		if (SJQApp.user == null) {
			params.setHeader("Token", null);
		} else {
			params.setHeader("Token", SJQApp.user.Token);
		}
		params.setHeader("Version", ApiUrl.VERSION);
		params.setHeader("Client", AppTag.CLIENT);
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		params.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
		return params;

	}

	public void setTextView(TextView tvDay, TextView tvAll) {
		// TODO Auto-generated method stub
		this.mTvDay = tvDay;
		this.mTvAll = tvAll;
	}
}
