package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 委托
 * 
 * @author JiSheng.Guo
 * 
 */
public class ListViewEntrust extends MyListView implements OnClickListener {

	/*private EntrustAdapter mAdapter;
	private HttpHandler<String> mHandler;
	private TextView mFooterTitle;
	private View mPulldownFooterLoading;
	private View mIvNullData;
	private HttpUtils mHttpUtil;
	private int mPageIndex = 1, mPageSize = 10;
	private int mDev = 2;// 点击偏移
	private boolean mIsCace = false;// 是否缓存过（只缓存一次）
	private boolean mIsDivPage;
	private String mUserGuid = "";
	private String CACE_KEY = "";*/

	public ListViewEntrust(Context context, AttributeSet attrs) {
		super(context, attrs);
		//initView();
	}

	public ListViewEntrust(Context context) {
		super(context);

	/*	getRefreshableView().setDivider(
				getResources().getDrawable(R.color.acyivity_bg));
		getRefreshableView().setDividerHeight(32);

		// // 设置提示消息
		// ILoadingLayout endLabels = getLoadingLayoutProxy(false, true);
		// endLabels.setPullLabel("上拉加载更多");// 刚下拉时，显示的提示
		// endLabels.setRefreshingLabel("正在载入中...");// 刷新时
		// endLabels.setReleaseLabel("放开加载更多");// 下来达到一定距离时，显示的提示
		// // 下拉刷新
		String label = DateUtils.formatDateTime(getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		// 显示最后更新的时间
		getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + label);
		initView();*/
	}

/*	public void setUserGuid(String userGuid) {
		this.mUserGuid = userGuid;
		mPulldownFooterLoading.setVisibility(View.VISIBLE);
		mFooterTitle.setText("加载中");
		loadData(false);
	}

	public void setDev(int dev) {
		this.mDev = dev;
	}

	// 设置是否缓存
	public void setCaceKey(String CaceKey) {
		CACE_KEY = CaceKey;
		// 缓存
		String cace = CacheUtils.getStringData(getActivity(), CACE_KEY, null);
		if (cace != null) {
			showZT(cace);
		}
	}

	private void showZT(String cace) {
		EntrustBean data = CommonUtils.getDomain(cace, EntrustBean.class);
		if (data != null && data.data != null) {
			mAdapter.setData(data.data);
		}

	}

	*//**
	 * 以下方法 勿修改
	 * 
	 * 
	 *//*
	private void initView() {
		mHttpUtil = getHttpUtils();
		setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// // 下拉刷新
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				// 显示最后更新的时间
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
						"更新于：" + label);
				loadData(false);

			}
		});
		// 设置适配器
		mAdapter = new EntrustAdapter(getActivity());
		setAdapter(mAdapter);
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Entrust data = (Entrust) mAdapter.getItem(position - mDev);
				if (data == null)
					return;

				Intent intent = new Intent(getActivity(),
						EntustDetailActivity.class);

				intent.putExtra(AppTag.TAG_GUID, data.GUID);
				getContext().startActivity(intent);

			}

		});

		final View view = View.inflate(getContext(), R.layout.pulldown_footer,
				null);
		mFooterTitle = (TextView) view.findViewById(R.id.pulldown_footer_text);
		mPulldownFooterLoading = view
				.findViewById(R.id.pulldown_footer_loading);
		mIvNullData = view.findViewById(R.id.ivNullData);
		getRefreshableView().addFooterView(view);
		view.setOnClickListener(this);
		// view.setVisibility(View.GONE);

		*//**
		 * listview的监听事件
		 *//*
		setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view1, int scrollState) {
				*//**
				 * 如果等到该分页（is_divPage = true）的时候，并且滑动停止（这个时候手已经离开了屏幕），自动加载更多。
				 *//*
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

	}

	boolean isLoading = false;

	// 请求数据falg是否加载更多
	public void loadData(final boolean falg) {
		if (mIvNullData.getVisibility() != View.GONE)
			mIvNullData.setVisibility(View.GONE);
		if (!falg) {
			// 不是加载更多
			mPageIndex = 1;
			isLoading = false;
			setRefreshing();
			// 如果之前的线程没有完成
			if (mHandler != null
					&& mHandler.getState() != HttpHandler.State.FAILURE
					&& mHandler.getState() != HttpHandler.State.SUCCESS
					&& mHandler.getState() != HttpHandler.State.CANCELLED) {
				// 关闭handler后 onStart()和onLoading()还是会执行
				mHandler.cancel();
			}
		} else {
			if (isLoading) {
				return;
			}
			isLoading = true;
			mPageIndex++;
		}
		final String url = ApiUrl.GET_ENTRUST_LIST + mUserGuid + "&PageIndex="
				+ mPageIndex + "&PageSize=" + mPageSize;

		// System.out.println("-->>entrust+" + url);
		mHandler = mHttpUtil.send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						onRefreshComplete();
						if (falg) {
							mPageIndex--;
							mFooterTitle.setText("已经全部加载完毕");
							
						}
						mFooterTitle.setText("网络请求错误");
						// dialog.dismiss();
						isLoading = false;
						mPulldownFooterLoading.setVisibility(View.GONE);
						AppErrorLogUtil.getErrorLog(getContext()).postError(
								error.getExceptionCode() + "", "GET", url);
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
					
						isLoading = false;
						EntrustBean newsBean = CommonUtils.getDomain(n,
								EntrustBean.class);
						mFooterTitle.setText("");
						if (newsBean != null && newsBean.data != null) {

							if (!falg) {
								mAdapter.setData(newsBean.data);
								if (newsBean.data.size() == 0) {
									mIvNullData.setVisibility(View.VISIBLE);
									mFooterTitle.setText(getResources()
											.getString(R.string.NULL_DATA));
								}

								// 进行缓存
								if (!"".equals(CACE_KEY)) {
									if (!mIsCace) {
										CacheUtils.cacheStringData(
												getContext(), CACE_KEY,
												n.result);
										mIsCace = true;
									}
								}
							} else {
								if (newsBean.data.size() == 0) {
									mFooterTitle.setText("已经全部加载完毕");
								}

								mAdapter.addData(newsBean.data);
							}

						} else {
							if (falg) {
								mPageIndex--;
								mFooterTitle.setText("已经全部加载完毕");
							} else {
								mIvNullData.setVisibility(View.VISIBLE);
								mFooterTitle.setText(getResources().getString(
										R.string.NULL_DATA));
							}

						}
						onRefreshComplete();
						mPulldownFooterLoading.setVisibility(View.GONE);
					}
				});
	}

	private HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(5 * 1000);
		httpUtil.configDefaultHttpCacheExpiry(1000 * 3);
		return httpUtil;

	}

	private RequestParams getParams(String url) {

		RequestParams params = new RequestParams();
		if (SJQApp.user == null) {
			params.setHeader("Token", null);
		} else {
			params.setHeader("Token", SJQApp.user.Token);
		}
		params.setHeader("Version", "1.0");
		params.setHeader("AppAgent", "ANDROID_SHEJIQUAN_Ver.1.0");
		params.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
		return params;

	}*/

	private Context getActivity() {
		// TODO Auto-generated method stub
		return getContext();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
