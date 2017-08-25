package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
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
import com.baolinetworktechnology.shejiquan.activity.WebActivity;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.adapter.NewAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.ZiXunBean;
import com.baolinetworktechnology.shejiquan.domain.ZiXunList;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
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

public class ListViewNewsDesig extends MyListView implements OnClickListener {
	private HttpUtils mHttpUtil;
	private NewAdapter mAdapter;
	private String mKeyWord = "";
	private String CACE_KEY = "";
	private int mOrderBy = 0, mPageIndex = 1, mPageSize = 20;
	private int mItemID = 0;
	private int mDev = 1;
	// private boolean mIsRecord = false;// 是否要记录
	private boolean mIsRefing = false;//
	private boolean mIsCace = false;// 是否缓存过（只缓存一次）
	private String nullDataTips = "抱歉，没有找到您要的结果";
	private Context mContext;
	public void setNullDataTips(String nullDataTips) {
		this.nullDataTips = nullDataTips;
	}

	public ListViewNewsDesig(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	public ListViewNewsDesig(Context context) {
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

	// 设置 项目Id
	public void setClassID(int ClassID, int ItemID) {
		this.ClassID = ClassID;
		this.mItemID = ItemID;
		onRefreshComplete();
		setRefreshing();
		if (!mIsRefing) {
			loadData(false);
		}
	}

	// 设置 项目Id
	public void setClassID2(int ClassID, int ItemID) {
		this.ClassID = ClassID;
		this.mItemID = ItemID;
		onRefreshComplete();
		setOnRefreshing();
	}

	// 设置 子项目Id
	public void setItemID(int ItemID) {
		this.ClassID =143;
		this.mItemID = ItemID;
		onRefreshComplete();
		setRefreshing();
	}

	public void first() {
		this.ClassID =143;
		mItemID = 20;
		loadData(false);
	}

	public void setUserGuid(String UserGUID, boolean isMy) {
		this.UserGUID = UserGUID;
		this.isMy = isMy;
		if (mAdapter != null)
			// mAdapter.setIsMy();
			onRefreshComplete();
		setRefreshing();

	}

	public void setDev(int dev) {
		this.mDev = dev;
	}

	public void setNull() {
		getRefreshableView().setSelection(0);
		mAdapter.setData(null);
		mAdapter.notifyDataSetChanged();
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

	// 设置搜索关键词
	public void setUrlParams(String KeyWord, boolean isShow) {
		this.mKeyWord = KeyWord;
		this.isShow = isShow;
		onRefreshComplete();
		setRefreshing();
	}

	boolean isShow = false;// 是否显示logo

	// 设置 排序
	public void setOrderBy(int OrderBy) {
		this.mOrderBy = OrderBy;
		loadData(false);
	}

	// 设置添加到足迹中(默认假)
	public void setRecord(boolean isRecord) {
		// this.mIsRecord = isRecord;

	}

	public void setCaceKey2(String CaceKey) {
		CACE_KEY = CaceKey;

	}

	// 设置是否缓存
	public void setCaceKey(String CaceKey) {
		CACE_KEY = CaceKey;
		// 缓存
		if(!CommonUtils.hasNetWorkInfo(mContext)){
			String cace = CacheUtils.getStringData(getActivity(), CACE_KEY, null);
			if (cace != null) {
				showZT(cace);
			}
		}
	}

	private void showZT(String cace) {
		Gson gson = new Gson();
		ZiXunBean newsBean=gson.fromJson(cace, ZiXunBean.class);
		if (newsBean != null && newsBean.listData != null) {
			mAdapter.setData(newsBean.listData);
		}
	}

	/**
	 * 以下方法 勿修改
	 * 
	 * 
	 */
	private void initView() {
		setRefreshLabe();
		mHttpUtil = getHttpUtils();
		setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// // 下拉刷新
				setRefreshLabe();
				mIsRefing = true;
				loadData(false);

			}

		});
		// 设置适配器
		mAdapter = new NewAdapter(getActivity());
		setAdapter(mAdapter);
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// if (mIsOnClick) {
				// mIsOnClick = false;
				// //return;
				// }

				final ZiXunList news = (ZiXunList) mAdapter.getItem(position - mDev);
				if (isMy) {

					new ActionSheetDialog(getContext())
							.builder()
							.setCancelable(true)
							.setCanceledOnTouchOutside(true)

							.addSheetItem("查看", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											listOnItemClick(news);
										}
									})
							// .addSheetItem("编辑", SheetItemColor.Blue,
							// new OnSheetItemClickListener() {
							// @Override
							// public void onClick(int which) {
							// editorItem(news);
							// }
							//
							// })
							.addSheetItem("删除", SheetItemColor.Red,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											deleteItem(news);
										}

									}).show();
				} else {

					listOnItemClick(news);
				}

			}

		});

		View view = View.inflate(getContext(), R.layout.pulldown_footer, null);
		pulldownFooter = new PulldownFooter(getActivity(), view);
		// footerTitle = (TextView)
		// view.findViewById(R.id.pulldown_footer_text);
		// pulldown_footer_loading =
		// view.findViewById(R.id.pulldown_footer_loading);
		getRefreshableView().addFooterView(view);
		view.setOnClickListener(this);
		// view.setVisibility(View.INVISIBLE);

		/**
		 * listview的监听事件
		 */
		setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view1, int scrollState) {
				// if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				// if (onStateListener != null) {
				// onStateListener.onStateListener(false);
				// }
				// }
				/**
				 * 如果等到该分页（is_divPage = true）的时候，并且滑动停止（这个时候手已经离开了屏幕），自动加载更多。
				 */
				if (is_divPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					pulldownFooter.setState(PulldownFooter.STATE_LOGING);
					loadData(true);

					// view.setVisibility(View.VISIBLE);
					// footerTitle.setText("更多数据正在加载中");
					//
					// pulldown_footer_loading.setVisibility(View.VISIBLE);

				} else if (!is_divPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					pulldownFooter.setState(PulldownFooter.STATE_FREE);
					// pulldown_footer_loading.setVisibility(View.GONE);
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem>=15){
					Intent intent = new Intent();
					intent.setAction("DesignArtshow");
					mContext.sendBroadcast(intent);
				}
				is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});

	}

	private void deleteItem(ZiXunList news) {
		if(news==null)
			return;
		final MyDialog dialog = new MyDialog(getActivity());
//		dialog.show();
//		if (isMy && SJQApp.user != null) {
//			String url = ApiUrl.REMOVE;
//			RequestParams params = getParams(url);
//			params.addBodyParameter("UserGUID", SJQApp.user.UserGUID);
//			params.addBodyParameter("InfoID", news.ID + "");
//			params.addBodyParameter("OperateEnum", OperateEnum.OperateEnumNews);
//
//			RequestCallBack<String> callBack = new RequestCallBack<String>() {
//
//				@Override
//				public void onSuccess(ResponseInfo<String> arg0) {
//					dialog.dismiss();
//					loadData(false);
//
//				}
//
//				@Override
//				public void onFailure(HttpException error, String arg1) {
//					dialog.dismiss();
//					AppErrorLogUtil.getErrorLog(getContext()).postError(
//							error.getExceptionCode() + "", "POST",
//							ApiUrl.REMOVE);
//				}
//			};
//			getHttpUtils().send(HttpMethod.POST, ApiUrl.API + url, params,
//					callBack);
//
//		}
	}

	// private void editorItem(News news) {
	// // TODO Auto-generated method stub
	// Intent intent = new Intent(getContext(), AddNewsActivity.class);
	// intent.putExtra(AppTag.TAG_JSON, news.toString());
	// getContext().startActivity(intent);
	// }

	private void listOnItemClick(ZiXunList news) {
		// News news = (News) adapter.getItem(position - dev);
		if (news == null)
			return;
		if(TextUtils.isEmpty(news.getLinkUrl())){
			Intent intent = new Intent(getActivity(), WebDetailActivity.class);
			intent.putExtra(WebDetailActivity.GUID, "0");
			intent.putExtra(WebDetailActivity.ID, news.id+"");
			getActivity().startActivity(intent);
		}else{
			Intent intent = new Intent(getActivity(), WebActivity.class);
			String url=news.getLinkUrl();
			intent.putExtra(WebActivity.URL,url);
			getActivity().startActivity(intent);
		}

		// if (mIsRecord) {
		// Serch item = new Serch(SerchBean.TYPE_NEWS, news.GUID, news.Title,
		// news.ID);
		// SerchBean.getInstance(getContext()).addData(item);
		// }
	}

	private boolean is_divPage;
	private HttpHandler<String> handler;
	private int ClassID =143;

	// 请求数据falg是否加载更多
	boolean isLoading = false;
	private boolean isMy = false;
	private String UserGUID = "";

	private void setRefreshLabe() {
		String label = DateUtils.formatDateTime(getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		// 显示最后更新的时间
		getLoadingLayoutProxy().setLastUpdatedLabel("更新于：" + label);

	}

	PulldownFooter pulldownFooter;

	public void loadData(final boolean falg) {
		if (!falg) {
			// 不是加载更多
			pulldownFooter.setState(PulldownFooter.STATE_COMPLETE);
			mPageIndex = 1;
			isLoading = false;
			setRefreshLabe();
			if (isShow) {
				// onRefreshComplete();
				setRefreshing();

			}
			// 如果之前的线程没有完成
			if (handler != null
					&& handler.getState() != HttpHandler.State.FAILURE
					&& handler.getState() != HttpHandler.State.SUCCESS
					&& handler.getState() != HttpHandler.State.CANCELLED) {
				// 关闭handler后 onStart()和onLoading()还是会执行
				handler.cancel();
			}
		} else {
			// pulldown_footer_loading.setVisibility(View.VISIBLE);
			mPageIndex++;
			if (onStateListener != null) {
				onStateListener.onStateListener(true);
			}
			pulldownFooter.setState(PulldownFooter.STATE_LOGING);
		}
		String url = AppUrl.DECORATIONGTLIST+ClassID+"&orders=12"+"&markName=0"+"&PageSize=15"+"&currentPage="+mPageIndex;
		LogUtils.i("news", url);
		handler = mHttpUtil.send(HttpMethod.GET, AppUrl.API + url,
				getParams(url), new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						onRefreshComplete();
						mIsRefing = false;
						if (falg) {
							mPageIndex--;
							pulldownFooter
									.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
							// footerTitle.setText("已经全部加载完毕");
							// pulldown_footer_loading.setVisibility(View.GONE);
						} else {
							pulldownFooter.setText("网络请求错误");
						}
						// footerTitle.setText("网络请求错误");
						// dialog.dismiss();

						isLoading = false;
						if (onStateListener != null) {
							onStateListener.onStateListener(false);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						isLoading = false;
						JSONObject json;
						String result1="";
						ZiXunBean newsBean=null;
						try {
							json = new JSONObject(n.result);
							 result1=json.getString("result");
							Gson gson = new Gson();
							newsBean=gson.fromJson(result1, ZiXunBean.class);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						pulldownFooter.setState(PulldownFooter.STATE_COMPLETE);
						if (newsBean != null && newsBean.listData != null) {
							if (isMy) {
								if (SJQApp.userData != null) {
									SJQApp.userData.setNumNew(newsBean.totalsize);
								}
							}
							// 进行缓存
							if (!"".equals(CACE_KEY)) {
								if (!mIsCace) {
									CacheUtils.cacheStringData(
											getContext(), CACE_KEY,
											result1);
									mIsCace = true;
								}
							}
							if (!falg) {
								mAdapter.setData(newsBean.listData);
								getRefreshableView().setSelection(0);
								if (newsBean.listData.size() == 0) {
									// footerTitle.setText("加载完毕");
									pulldownFooter
											.setState(
													PulldownFooter.STATE_COMPLETE_NULL_DATA,
													nullDataTips);
								}
							} else {
								if (newsBean.listData.size() == 0) {
									pulldownFooter
											.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
									// footerTitle.setText("已经全部加载完毕");
									// pulldown_footer_loading.setVisibility(View.GONE);
								}
								mAdapter.addData(newsBean.listData);
							}

						} else {

							if (falg) {
								mPageIndex--;
								pulldownFooter
										.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
							} else {
								pulldownFooter
										.setState(PulldownFooter.STATE_COMPLETE_NULL_DATA);
							}

							// footerTitle.setText("没有发现数据");
							// pulldown_footer_loading.setVisibility(View.GONE);
						}
						// dialog.dismiss();
						onRefreshComplete();
						if (onStateListener != null) {
							onStateListener.onStateListener(false);
						}
					}
				});
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

	private Context getActivity() {
		// TODO Auto-generated method stub
		return getContext();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void refreshData() {
		loadData(false);

	}

	public void setSmallImage(boolean is) {
		if (is) {
			// mAdapter.setIsSmall(is);
		}
	}

	// boolean mIsOnClick = false;

	public void setNoClick() {
		// TODO Auto-generated method stub
		// mIsOnClick = true;
	}

	private OnStateListener onStateListener;

	public void setonStateListener(OnStateListener onStateListener) {
		this.onStateListener = onStateListener;

	}

	public NewAdapter getAdapter() {
		// TODO Auto-generated method stub
		return mAdapter;
	}

}
