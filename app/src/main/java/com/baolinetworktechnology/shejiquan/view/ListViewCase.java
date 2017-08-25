package com.baolinetworktechnology.shejiquan.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.adapter.CollectOpusAdapter;
import com.baolinetworktechnology.shejiquan.adapter.CollectOwnerOpusAdapter;
import com.baolinetworktechnology.shejiquan.adapter.OpusHomenAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.constant.OperateEnum;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.CaseBean;
import com.baolinetworktechnology.shejiquan.domain.CasePageList;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.google.gson.Gson;
import com.guojisheng.koyview.ActionSheetDialog;
import com.guojisheng.koyview.ActionSheetDialog.OnSheetItemClickListener;
import com.guojisheng.koyview.ActionSheetDialog.SheetItemColor;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

public class ListViewCase extends MyListView implements OnClickListener {
	private PulldownFooter mPulldownFooter;
	private OpusHomenAdapter mAdapter;
	private HttpHandler<String> mHandler;

	private String mKeyWord = "";


	private int mPageIndex = 1, mPageSize = 10;


	private int mDev = 1;


	private boolean mIsCace = false;// 是否缓存过（只缓存一次）

	private boolean mIsDivPage;

	private boolean mIsRecord = true;// 是否要记录

	// private TextView footerTitle;
	// private View pulldown_footer_loading;

	public ListViewCase(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ListViewCase(Context context) {
		super(context);

		getRefreshableView().setDivider(
				getResources().getDrawable(R.color.acyivity_bg));
		getRefreshableView().setDividerHeight(32);

		// 设置提示消息
		ILoadingLayout endLabels = getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("上拉加载更多");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入中...");// 刷新时
		endLabels.setReleaseLabel("放开加载更多");// 下来达到一定距离时，显示的提示
		initView();

	}

	public void notifyDataSetInvalidated() {
		mAdapter.notifyDataSetInvalidated();
	}



	public void setDev(int dev) {
		this.mDev = dev;
	}

	boolean isRefresh = true;

	// 设置搜索关键词
	public void setUrlParams(String KeyWord) {
		this.mKeyWord = KeyWord;
		onRefreshComplete();

		if (isRefresh) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					setRefreshing();
					isRefresh = false;
				}
			}, 300);

		} else {
			setRefreshing();
		}
	}

	/**
	 * 以下方法 勿修改
	 * 
	 * 
	 */
	private void initView() {
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
		mAdapter = new OpusHomenAdapter(getActivity());
		setAdapter(mAdapter);
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

					if (mAdapter.getItem(position - mDev) != null) {
						SwCase news = (SwCase) mAdapter.getItem(position - mDev);
						listOnItemClick(news);
				}
			}

		});

		View view = View.inflate(getContext(), R.layout.pulldown_footer, null);
		mPulldownFooter = new PulldownFooter(getActivity(), view);
		getRefreshableView().addFooterView(view);
		view.setOnClickListener(this);

		/**
		 * listview的监听事件
		 */
		setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view1, int scrollState) {
				/**
				 * 如果等到该分页（is_divPage = true）的时候，并且滑动停止（这个时候手已经离开了屏幕），自动加载更多。
				 */
				// if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				// if (onStateListener != null) {
				// onStateListener.onStateListener(false);
				// }
				// }

				if (mIsDivPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {

					mPulldownFooter.setState(PulldownFooter.STATE_LOGING);
					loadData(true);
					// view.setVisibility(View.VISIBLE);
					// footerTitle.setText("更多数据正在加载中");
					// pulldown_footer_loading.setVisibility(View.VISIBLE);

				} else if (!mIsDivPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					mPulldownFooter.setState(PulldownFooter.STATE_FREE);
					// pulldown_footer_loading.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mIsDivPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});

	}
	private void listOnItemClick(SwCase news) {
		if (news == null)
			return;
		Intent intent = new Intent(getActivity(), WebOpusActivity.class);
		String url = AppUrl.DETAIL_CASE2 + news.id;
		intent.putExtra("WEB_URL", url);
		intent.putExtra(AppTag.TAG_ID, news.id);
		intent.putExtra(WebDetailActivity.GUID, news.guid);
		intent.putExtra(WebDetailActivity.ISCASE, true);
		getActivity().startActivity(intent);
	}

	boolean isLoading = false;
	private String UserGUID;

	private void refresing() {
		setRefreshing();
	}

	// 请求数据falg是否加载更多
	public void loadData(final boolean falg) {

		if (!falg) {
			// 不是加载更多
			mPageIndex = 1;
			isLoading = false;
			setRefreshing();
			mPulldownFooter.setState(PulldownFooter.STATE_COMPLETE);
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
			if (onStateListener != null) {
				onStateListener.onStateListener(true);
			}
			isLoading = true;
			mPageIndex++;
			mPulldownFooter.setState(PulldownFooter.STATE_LOGING);
		}
		String url = "";
			try {
			 url = AppUrl.CASELISET + "currentPage=" + mPageIndex+"&orders=12"
					+ "&pageSize=15&keyWord="+ URLEncoder.encode(mKeyWord, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		LogUtils.i("case url", url);
		mHandler = getHttpUtils().send(HttpMethod.GET, AppUrl.API + url,
				getParams(url), new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						if (onStateListener != null) {
							onStateListener.onStateListener(false);
						}
						onRefreshComplete();
						if (falg) {
							mPageIndex--;
							mPulldownFooter
									.setState(
											PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA,
											nullDataTips);

						} else {
							mPulldownFooter.setText("网络请求错误");
						}

						isLoading = false;
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						
						isLoading = false;
						JSONObject json;
						CasePageList newsBean=null;
						try {
							json = new JSONObject(n.result);
							String result1=json.getString("result");
							Gson gson = new Gson();
							newsBean=gson.fromJson(result1, CasePageList.class);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						mPulldownFooter.setState(PulldownFooter.STATE_COMPLETE);
						if (newsBean != null && newsBean.listData != null) {

							if (!falg) {
								mAdapter.setData(newsBean.listData);
								if (newsBean.listData.size() == 0) {
									mPulldownFooter
											.setState(
													PulldownFooter.STATE_COMPLETE_NULL_DATA,
													nullDataTips);
									mPulldownFooter.setNullData(nullDdrawable);
								}
							} else {
								if (newsBean.listData.size() == 0) {

									mPulldownFooter
											.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
								}
								mAdapter.addData(newsBean.listData);
							}
							mAdapter.notifyDataSetChanged();
						} else {
							if (falg) {
								mPageIndex--;

								mPulldownFooter
										.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
							} else {
								mPulldownFooter
										.setState(PulldownFooter.STATE_COMPLETE_NULL_DATA);
							}

						}

						onRefreshComplete();
						if (onStateListener != null) {
							onStateListener.onStateListener(false);
						}
					}

				});
	}

	private HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils();
		// httpUtil.configDefaultHttpCacheExpiry(1000 * 1);
		return httpUtil;

	}

	private OnStateListener onStateListener;

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

	private Context getActivity() {
		// TODO Auto-generated method stub
		return getContext();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	public void setonStateListener(OnStateListener onStateListener) {
		this.onStateListener = onStateListener;

	}

	private String nullDataTips = "抱歉，没有找到您要的结果";
	private int nullDdrawable = R.drawable.ic_null_comment;
	public void setNullDataTips(String nullDataTips) {
		this.nullDataTips = nullDataTips;

	}
	public void setNullDdrawable(int nullDdrawable) {
		this.nullDdrawable = nullDdrawable;

	}
}
