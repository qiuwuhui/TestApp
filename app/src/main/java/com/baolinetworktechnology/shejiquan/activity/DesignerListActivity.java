package com.baolinetworktechnology.shejiquan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.OwnerAdapter;
import com.baolinetworktechnology.shejiquan.adapter.PersonerAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.DataBen;
import com.baolinetworktechnology.shejiquan.domain.DesignerBean;
import com.baolinetworktechnology.shejiquan.domain.DesignerInfo;
import com.baolinetworktechnology.shejiquan.domain.DesignerItemInfo;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.view.KoyTab;
import com.baolinetworktechnology.shejiquan.view.KoyTab.OnTabChangeListener;
import com.baolinetworktechnology.shejiquan.view.MyListView;
import com.baolinetworktechnology.shejiquan.view.PulldownFooter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设计师列表(粉丝，关注)
 * 
 * @author JiSheng.Guo
 * 
 */
public class DesignerListActivity extends BaseActivity implements
		OnTabChangeListener {
	public static final String ISFANS = "ISFANS";
	public static final String ISOWNER = "ISOWNER";
	private MyListView mMyListView;
	private PersonerAdapter mAdapter;
	OwnerAdapter mOwnerAdapter;
	// private CollectDesignerAdapter mAdapter1;
	private PulldownFooter mPulldownFooter;// ListView 底部视图
	private String mUserGuid = "";
	private int mPageIndex = 1;
	private boolean mIsDivPage;// 如果等到该分页（is_divPage =
	private boolean isFans;
	String Cacekey;
	// true）的时候，并且滑动停止（这个时候手已经离开了屏幕），自动加载更多。

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_designer_list);
		isFans = getIntent().getBooleanExtra(ISFANS, false);
		isOwner = getIntent().getBooleanExtra(ISOWNER, false);
		setTitle(getIntent().getStringExtra("TITLE"));
		mUserGuid = getIntent().getStringExtra("GUID");
		Cacekey=isFans+mUserGuid;
		initView();
		SJQApp.isRrefresh = true;

	}

	@Override
	protected void setUpViewAndData() {

	}
	@Override
	protected void restartApp() {
		startActivity(new Intent(this, SplashActivity.class));
		finish();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_BACK_TO_HOME);
		switch (action) {
			case AppStatusConstant.ACTION_RESTART_APP:
				restartApp();
				break;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (SJQApp.isRrefresh) {
			mMyListView.setOnRefreshing();
			SJQApp.isRrefresh = false;
		}
	}

	private void initView() {
		if (isFans) {
			KoyTab tab = (KoyTab) findViewById(R.id.tab);
			tab.setVisibility(View.VISIBLE);
			tab.setTabText(0, "设计师");
			tab.setTabText(1, "业主");
			tab.setOnTabChangeListener(this);
			tab.setShow2();
		}
		mMyListView = (MyListView) findViewById(R.id.myListView);
		// 设置下拉刷新 上拉加载更多
		mMyListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

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

				refreshData();

			}

		});

		/**
		 * listview的监听事件
		 */
		mMyListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view1, int scrollState) {
				/**
				 * 如果等到该分页（is_divPage = true）的时候，并且滑动停止（这个时候手已经离开了屏幕），自动加载更多。
				 */
				if (mIsDivPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					onLastItemVisible();
					mPulldownFooter.setState(PulldownFooter.STATE_LOGING);

				} else if (!mIsDivPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					mPulldownFooter.setState(PulldownFooter.STATE_FREE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mIsDivPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});
		// 设置条目点击事件
		mMyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (isPerson) {
					return;
				}
				DesignerItemInfo item = null;
				if (isOwner) {
					item = mOwnerAdapter.getDesignerInfo(position - 1);
				} else {
					item = mAdapter.getDesignerInfo(position - 1);
					// String item = mAdapter.getGuId(position - 1);
				}
				if (item != null) {
					Intent intent = new Intent(getActivity(),
							WeiShopWebActivity.class);
					intent.putExtra(AppTag.TAG_GUID, item.guid);
					intent.putExtra(AppTag.TAG_ID, item.id + "");
					intent.putExtra(AppTag.TAG_JSON, item.toString());
					getActivity().startActivity(intent);
				}
			}
		});

		if (!isFans) {
			mMyListView.getRefreshableView().setOnItemLongClickListener(
					new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								final View view, final int position, long id) {

							View dialogView = View.inflate(getActivity(),
									R.layout.dialog_collect, null);
							final AlertDialog ad = new AlertDialog.Builder(
									getActivity()).setView(dialogView).show();
							dialogView.findViewById(R.id.dialog_cancel)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {

											ad.cancel();
										}

									});
							dialogView.findViewById(R.id.dialog_ok)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {

											delect(position - 1);
											ad.cancel();
										}
									});

							return true;
						}
					});
		}
		// 设置提示消息
		ILoadingLayout endLabels = mMyListView.getLoadingLayoutProxy(false,
				true);
		endLabels.setPullLabel("上拉加载更多");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入中...");// 刷新时
		endLabels.setReleaseLabel("放开加载更多");// 下来达到一定距离时，显示的提示
		// 设置适配器
		DesignerBean desgnerBean = CommonUtils.getDomain(
				CacheUtils.getStringData(getActivity(), "List", Cacekey, ""),
				DesignerBean.class);
		if (isOwner) {
			mOwnerAdapter = new OwnerAdapter(getActivity());
			mMyListView.setAdapter(mOwnerAdapter);
			if (desgnerBean != null)
				mOwnerAdapter.setData(desgnerBean.listData);
		} else {
			mAdapter = new PersonerAdapter(getActivity());
			mMyListView.setAdapter(mAdapter);
			if (desgnerBean != null)
				mAdapter.setData(desgnerBean.listData);
		}

		View view = View.inflate(this, R.layout.pulldown_footer, null);
		mPulldownFooter = new PulldownFooter(getActivity(), view);
		view.setOnClickListener(this);
		mMyListView.getRefreshableView().addFooterView(view);

	}

	boolean isOwner = false;

	// 下拉刷新
	private void refreshData() {
		mPageIndex = 1;
		loadData(false);
	}

	// 加载更多
	private void onLastItemVisible() {
		mPageIndex++;
		loadData(true);

	}

	private void delect(final int index) {
		String fromGuid = null;
		if (isOwner) {
			fromGuid = mOwnerAdapter.getGuId(index);
		} else {
			fromGuid = mAdapter.getGuId(index);
		}

		dialog.show("取消中...");
		String url = ApiUrl.FAVORITE_CANCEL;
		RequestParams params = getParams(url);
		params.addBodyParameter("ClassType", "5");
		params.addBodyParameter("UserGUID", SJQApp.user.guid);
		params.addBodyParameter("FromGUID", fromGuid);
		params.addBodyParameter("Type", "1");
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (mMyListView == null)
					return;
				if (dialog != null) {
					dialog.dismiss();
				}

				LogUtils.i("设计师详情头部", arg0.result);
				DataBen data = CommonUtils.getDomain(arg0, DataBen.class);
				if (data != null) {
					if (data.success) {
						if (isOwner) {
							if (SJQApp.userData != null) {
								SJQApp.userData.numConcer--;
							}
							mOwnerAdapter.delete(index);
							mOwnerAdapter.notifyDataSetChanged();
						} else {
							if (SJQApp.userData != null) {
								SJQApp.userData.numConcer--;
							}

							mAdapter.delete(index);
							mAdapter.notifyDataSetChanged();
						}

					} else {
						toastShow("取消失败");
					}
				} else {
				}
			}

			@Override
			public void onFailure(HttpException error, String arg1) {
				if (mMyListView == null)
					return;
				if (dialog != null) {
					dialog.dismiss();
				}
				LogUtils.i("erro:设计师详情头部", arg1);
				toastShow(getResources().getString(R.string.network_error));
				AppErrorLogUtil.getErrorLog(getActivity()).postError(
						error.getExceptionCode() + "", "POST",
						ApiUrl.FAVORITE_CANCEL);
			}
		};
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);

	}

	boolean isPerson = false;

	// 请求数据falg是否加载更多
	private void loadData(final boolean falg) {
		if (!falg) {
			mPageIndex = 1;
			mMyListView.setRefreshing(true);

		} else {
			mPulldownFooter.setState(PulldownFooter.STATE_LOGING);
		}
		String url = "";
		if (getIntent().getBooleanExtra(ISFANS, false)) {

			if (isPerson) {

				url = ApiUrl.GET_USER_FANSLIST + "&UserGuid=" + mUserGuid
						+ "&PageIndex=" + mPageIndex + "&PageSize=10";
			} else {
				url = ApiUrl.GET_FANSLIST + "&UserGuid=" + mUserGuid
						+ "&PageIndex=" + mPageIndex + "&PageSize=10";
			}
		} else {
			url = ApiUrl.GET_DESIGNER_LIST + "&UserGuid=" + mUserGuid
					+ "&PageIndex=" + mPageIndex + "&PageSize=10";
		}
		url = url + "&r=" + System.currentTimeMillis();
		final String urls = url;
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						if (mMyListView == null)
							return;
						mMyListView.onRefreshComplete();
						if (falg) {
							mPageIndex--;

						}
						mPulldownFooter.setText("网络请求错误");
						AppErrorLogUtil.getErrorLog(getApplicationContext())
								.postError(error.getExceptionCode() + "",
										"GET", urls);
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						if (mMyListView == null)
							return;

						if (mPageIndex == 1) {
							CacheUtils.cacheStringData(
									DesignerListActivity.this, "List",
									Cacekey, n.result);
						}

						mPulldownFooter.setState(PulldownFooter.STATE_COMPLETE);
						JSONObject json;
						DesignerBean desgnerBean=null;
						try {
							json = new JSONObject(n.result);
							String result1=json.getString("result");
							Gson gson = new Gson();
							desgnerBean=gson.fromJson(result1, DesignerBean.class);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (desgnerBean != null && desgnerBean.listData != null) {

							if (!falg) {
								if (isOwner) {
									mOwnerAdapter.setData(desgnerBean.listData);
								} else {
									mAdapter.setData(desgnerBean.listData);
								}
								if (desgnerBean.listData.size() == 0) {
									if (!isFans) {
										mPulldownFooter
												.setState(
														PulldownFooter.STATE_COMPLETE_NULL_DATA,
														"您还没有关注设计师哦");
									} else {
										mPulldownFooter
												.setState(
														PulldownFooter.STATE_COMPLETE_NULL_DATA,
														"您还没有任何粉丝哦");
									}
								}
							} else {
								if (desgnerBean.listData.size() == 0) {
									mPulldownFooter
											.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
									mPulldownFooter.setText("已经全部加载完毕");
								}
								if (isOwner) {
									mOwnerAdapter.addData(desgnerBean.listData);
								} else {

									mAdapter.addData(desgnerBean.listData);
								}
							}

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
						mMyListView.onRefreshComplete();

					}
				});

	}


	@Override
	public void onTabChange(RadioGroup group, int index) {
		switch (index) {
		case 0:
			isPerson = false;
			break;

		default:
			isPerson = true;
			break;
		}
		if (isOwner) {

			mOwnerAdapter.setIsPersoner(isPerson);
		} else {
			mAdapter.setIsPersoner(isPerson);
		}

		loadData(false);
	}

	@Override
	public void finish() {
		super.finish();
		mMyListView = null;
	}
}
