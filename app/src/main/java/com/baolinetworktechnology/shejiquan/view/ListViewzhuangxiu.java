package com.baolinetworktechnology.shejiquan.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.baolinetworktechnology.shejiquan.ZhuanXiuAdaoter;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopWebActivity;
import com.baolinetworktechnology.shejiquan.activity.WeizxActivity;
import com.baolinetworktechnology.shejiquan.adapter.DesignerAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseZhuanxiu;
import com.baolinetworktechnology.shejiquan.domain.Casezx;
import com.baolinetworktechnology.shejiquan.domain.DesignerBean;
import com.baolinetworktechnology.shejiquan.domain.DesignerInfo;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 装修公司搜索
 * 
 * @author JiSheng.Guo
 * 
 */
public class ListViewzhuangxiu extends MyListView implements OnClickListener {
	private HttpHandler<String> mHandler;
	private ZhuanXiuAdaoter mAdapter;
	private String CACE_KEY = "";
	private int mPageIndex = 1;
	// private int mOrderBy = 0;
	private int mCityID = 0;
	private boolean mIsCace = false;// 是否缓存过（只缓存一次）
	private boolean mIsDivPage;

	private boolean mIsMultiple = false;

	public ListViewzhuangxiu(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ListViewzhuangxiu(Context context) {
		super(context);

		getRefreshableView().setDivider(
				getResources().getDrawable(R.color.acyivity_bg));
		getRefreshableView().setDividerHeight(32);

		// 设置提示消息
		// ILoadingLayout endLabels = getLoadingLayoutProxy(false, true);
		// endLabels.setPullLabel("上拉加载更多");// 刚下拉时，显示的提示
		// endLabels.setRefreshingLabel("正在载入中...");// 刷新时
		// endLabels.setReleaseLabel("放开加载更多");// 下来达到一定距离时，显示的提示
		String label = DateUtils.formatDateTime(getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		// 显示最后更新的时间
		getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + label);
		initView();
	}

	public void setCity(int CityID) {
		this.mCityID = CityID;
		// loadData(false);
		setRefreshing();
	}

	// public void setUserGuid(String userGuid) {
	// this.userGuid = userGuid;
	// loadData(false);
	// }
	//
	// public void setDev(int dev) {
	// this.dev = dev;
	// }

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
//		DesignerBean data = CommonUtils.getDomain(cace, DesignerBean.class);
//		if (data != null && data.data != null) {
//			mAdapter.setData(data.data);
//		}

	}

	/**
	 * 以下方法 勿修改
	 * 
	 * 
	 */
	PulldownFooter pulldownFooter;

	public PulldownFooter getPulldownFooter() {
		return pulldownFooter;
	}

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
		mAdapter = new ZhuanXiuAdaoter(getActivity());
		setAdapter(mAdapter);
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Casezx casezx=mAdapter.getItem(position-1);
				if (casezx == null)
					return;
				Intent intent = new Intent(getActivity(),WeizxActivity.class);
				intent.putExtra("businessID", casezx.getId()+"");
				intent.putExtra("GUID", casezx.getGUID()+"");
				getActivity().startActivity(intent);
			}

		});

		final View view = View.inflate(getContext(), R.layout.pulldown_footer,
				null);
		pulldownFooter = new PulldownFooter(getActivity(), view);
		getRefreshableView().addFooterView(view);
		view.setOnClickListener(this);
		// view.setVisibility(View.GONE);

		/**
		 * listview的监听事件
		 */
		setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view1, int scrollState) {
				/**
				 * 如果等到该分页（is_divPage = true）的时候，并且滑动停止（这个时候手已经离开了屏幕），自动加载更多。
				 */
				if (mIsDivPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					loadData(true);
					// view.setVisibility(View.VISIBLE);
					// footerTitle.setText("更多数据正在加载中");
					pulldownFooter.setState(PulldownFooter.STATE_LOGING);
					// pulldown_footer_loading.setVisibility(View.VISIBLE);

				} else if (!mIsDivPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					pulldownFooter.setState(PulldownFooter.STATE_FREE);
					// pulldown_footer_loading.setVisibility(View.GONE);
				}
				// if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				// if (onStateListener != null) {
				// onStateListener.onStateListener(false);
				// }
				// }
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mIsDivPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});

	}

	boolean isLoading = false;
	private int OrderBy = 0;
	String desc = "抱歉，暂时没有找到相关的设计师";

	public void setNullDataTips(String nullDataTips) {
		this.desc = nullDataTips;
	}

	// 请求数据falg是否加载更多
	public void loadData(final boolean falg) {

		if (!falg) {
			// footerTitle.setText("加载中...");
			pulldownFooter.setState(PulldownFooter.STATE_COMPLETE);
			// 不是加载更多
			mPageIndex = 1;
			isLoading = false;
			// setRefreshing();
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
			if (onStateListener != null) {
				onStateListener.onStateListener(true);
			}
			pulldownFooter.setState(PulldownFooter.STATE_LOGING);
		}
		String url = ApiUrl.BUSINESSSEARCH + KeyWord+"&PageIndex=" + mPageIndex
						+ "&PageSize=10"+"&IsRefresh=true";		
		final String urls = url;
		mHandler = getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url,
				getParams(url), new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						onRefreshComplete();
						if (onStateListener != null) {
							onStateListener.onStateListener(false);
						}
						if (falg) {
							mPageIndex--;
							// footerTitle.setText("已经全部加载完毕");
							pulldownFooter.setText("已经全部加载完毕");

						}
						pulldownFooter.setText("网络请求错误");
						// footerTitle.setText("网络请求错误");
						// pulldown_footer_loading.setVisibility(View.GONE);
						isLoading = false;

						AppErrorLogUtil.getErrorLog(getContext()).postError(
								error.getExceptionCode() + "", "GET", urls);
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						pulldownFooter.setState(PulldownFooter.STATE_COMPLETE);
						isLoading = false;
						String result=n.result;
						CaseZhuanxiu newsBean=null;
						try {
							JSONObject json=new JSONObject(result);
							String result1=json.getString("result");
							Gson gson = new Gson();
							newsBean = gson.fromJson(result1, CaseZhuanxiu.class);
						} catch (JSONException e) {
							e.printStackTrace();
						}

						if (newsBean != null && newsBean.listData != null) {

							if (!falg) {
								mAdapter.setData(newsBean.listData);
								if (newsBean.listData.size() == 0) {
									pulldownFooter
											.setState(
													PulldownFooter.STATE_COMPLETE_NULL_DATA,
													desc);
									// footerTitle.setText("没有发现数据");
									// pulldown_footer_loading
									// .setVisibility(View.GONE);
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
								if (newsBean.listData.size() == 0) {
									pulldownFooter
											.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
									// footerTitle.setText("已经全部加载完毕");
									// pulldown_footer_loading
									// .setVisibility(View.GONE);
								}

								mAdapter.addData(newsBean.listData);
							}
							mAdapter.notifyDataSetChanged();
						} else {
							if (falg) {
								mPageIndex--;
								pulldownFooter
										.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
								// footerTitle.setText("已经全部加载完毕");
							} else {
								pulldownFooter
										.setState(PulldownFooter.STATE_COMPLETE_NULL_DATA);
								// footerTitle.setText("暂时没有数据");
							}

						}
						if (onStateListener != null) {
							onStateListener.onStateListener(false);
						}
						// dialog.dismiss();
						onRefreshComplete();
					}
				});
	}

	private HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(5 * 1000);
		// httpUtil.configDefaultHttpCacheExpiry(1000 * 60*1);
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
		params.setHeader("AppAgent", "ANDROID_SHEJIQUAN_Ver.1.0");
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

	// private boolean isRecord = true;
	String KeyWord = null;

	public void setRecord(boolean b) {
		// TODO Auto-generated method stub
		// isRecord = b;
	}

	boolean isRefresh = true;

	// 设置搜索关键词
	public void setUrlParams(String KeyWord) {
		this.KeyWord = KeyWord;
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

	String tags = "", cost = "", workYear = "0", sex = "", WorkCompany = "";
	private OnStateListener onStateListener;

	public void setUrlParam(String name, String tags, String cost,
			String workYear, String sex, String WorkCompany, int CityID) {
		this.KeyWord = name;
		this.mCityID = CityID;
		this.tags = tags;
		if (cost.startsWith("㎡", cost.length() - 1)) {
			cost = cost.split("/")[0] + "/m2";
		}

		this.cost = cost;
		this.sex = sex;
		if (workYear.trim().equals("")) {
			workYear = "0";
		}
		this.workYear = workYear;
		this.WorkCompany = WorkCompany;
		setOnRefreshing();
		// loadData(false);
	}

	public void setTags(String tags) {
		// TODO Auto-generated method stub
		this.tags = tags;
		setRefreshing();
		// loadData(false);
	}

	public void setCost(String cost) {
		if (cost.startsWith("㎡", cost.length() - 1)) {
			cost = cost.split("/")[0] + "/m2";
		}

		LogUtils.i("cost", cost);
		this.cost = cost;
		// loadData(false);
		setRefreshing();
	}

	public void setWorkYear(String workYear) {
		// TODO Auto-generated method stub
		this.workYear = workYear;
		// loadData(false);
		setRefreshing();
	}

	public void setOrderBy(int OrderBy) {
		// TODO Auto-generated method stub
		this.OrderBy = OrderBy;
		// loadData(false);
		setRefreshing();
	}

	public void setUrlParams(String keyWord2, boolean b) {
		// TODO Auto-generated method stub

	}

	public void setIsMultiple(boolean IsMultiple) {
		this.mIsMultiple = IsMultiple;

	}

	public void setonStateListener(OnStateListener mOnStateListener) {
		this.onStateListener = mOnStateListener;

	}

	public void setCity2(int cityID) {
		// TODO Auto-generated method stub
		this.mCityID = cityID;
	}

}
