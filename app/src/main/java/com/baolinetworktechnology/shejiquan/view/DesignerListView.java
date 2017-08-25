package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baolinetworktechnology.shejiquan.CollectsjFragment;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.WeiActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.adapter.CollectDesignerAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.DesignerBean;
import com.baolinetworktechnology.shejiquan.domain.DesignerInfo;
import com.baolinetworktechnology.shejiquan.domain.DesignerItemInfo;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.google.gson.Gson;
import com.guojisheng.koyview.ExplosionField;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 资讯ListView
 * 
 * @author gjs
 * 
 */
public class DesignerListView extends PullToRefreshListView {
	private HttpUtils mHttpUtils;
	// private RequestCallBack<String> callBack;
	private CollectDesignerAdapter mCollAdapter;
	private MyDialog dialog;
	// private boolean flag = false;
	private int mPageIndex = 1;
	private RequestParams mParams;
	private String mUserGuid;
	private boolean mIsDivPage;
	String caceKey = "";
	Context mContext;
	public DesignerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
		
		initView();
	}

	boolean isCollect = false;
	private PulldownFooter pulldownFooter;

	public void setUserGuid(String UserGuid, boolean isCollect) {
		this.isCollect = isCollect;
		this.mUserGuid = UserGuid;
		mPageIndex = 1;
		setRefreshing();

	}
	private void initView() {
		if (mHttpUtils == null) {
			mHttpUtils = new HttpUtils();
		}
		if (dialog == null) {
			dialog = new MyDialog(getContext());
		}
		mParams = new RequestParams();
		mParams.setHeader("Token", null);
		mParams.setHeader("Version", "1.0");
		mParams.setHeader("AppAgent", "ANDROID_SHEJIQUAN_Ver.1.0");

		setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
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
		// 设置条目点击事件
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DesignerItemInfo item = (DesignerItemInfo) mCollAdapter.getItem(position-1);
				if (item == null)
					return;
				Intent intent = new Intent(getActivity(),
						WeiShopActivity.class);// WeiActivity WeiShopWebActivity2.1版
				intent.putExtra("IS_MY", false);
				intent.putExtra(AppTag.TAG_GUID, item.guid);

				intent.putExtra(AppTag.TAG_ID, item.id + "");
				intent.putExtra(AppTag.TAG_JSON, item.toString());
				getActivity().startActivity(intent);
			}

		});
		View view = View.inflate(getContext(), R.layout.pulldown_footer, null);
		pulldownFooter = new PulldownFooter(getActivity(), view);
		getRefreshableView().addFooterView(view);
		view.setOnClickListener(null);

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
					pulldownFooter.setState(PulldownFooter.STATE_LOGING);
					onLastItemVisible();
					// view.setVisibility(View.VISIBLE);
					// footerTitle.setText("更多数据正在加载中");
					//
					// pulldown_footer_loading.setVisibility(View.VISIBLE);

				} else if (!mIsDivPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					pulldownFooter.setState(PulldownFooter.STATE_FREE);
					// pulldown_footer_loading.setVisibility(View.GONE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mIsDivPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});
		// 设置提示消息
		ILoadingLayout endLabels = getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("上拉加载更多");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入中...");// 刷新时
		endLabels.setReleaseLabel("放开加载更多");// 下来达到一定距离时，显示的提示
		mCollAdapter = new CollectDesignerAdapter(getActivity());
		setAdapter(mCollAdapter);
	}

	// 请求数据falg是否加载更多
	private void loadData(final boolean falg) {
		CollectsjFragment.gong();
		if (!falg) {
			mPageIndex = 1;
			// setRefreshing();

		} else {
			pulldownFooter.setState(PulldownFooter.STATE_LOGING);
		}
		String url = AppUrl.GET_DESIGNER_LIST+ SJQApp.user.guid
				+"&currentPage="+mPageIndex+"&pageSize=10";
		setHash(url);
		mHttpUtils.send(HttpMethod.GET, AppUrl.API + url, mParams,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						if (pulldownFooter == null)
							return;
						if (pulldownFooter != null) {
							pulldownFooter.setText("网络请求错误");
						}
						onRefreshComplete();
						if (falg) {
							mPageIndex--;
						}

						// dialog.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						if (pulldownFooter == null)
							return;
						pulldownFooter.setState(PulldownFooter.STATE_COMPLETE);
						JSONObject json;
						DesignerBean newsBean=null;
						try {
							json = new JSONObject(n.result);
							String result1=json.getString("result");
							Gson gson = new Gson();
							newsBean=gson.fromJson(result1, DesignerBean.class);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (newsBean != null && newsBean.listData != null) {
							if (!falg) {
								if (!TextUtils.isEmpty(caceKey))
									CacheUtils.cacheStringData(getActivity(),
											"List", caceKey, n.result);
								mCollAdapter.setData(newsBean.listData);
							} else {
								mCollAdapter.addData(newsBean.listData);
							}

						} else {
							if (!falg) {
								setAdapter(null);
								CollectsjFragment.vienin();
							}else{
								pulldownFooter
										.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
							}
							mPageIndex--;
						}
						onRefreshComplete();
					}

				});

	}

	// 下拉刷新
	public void refreshData() {
		mPageIndex = 1;
		loadData(false);
	}

	// 加载更多
	private void onLastItemVisible() {
		mPageIndex++;
		loadData(true);

	}

	// View 销毁
	@Override
	protected void onDetachedFromWindow() {
		pulldownFooter = null;
		super.onDetachedFromWindow();

	}

	private Context getActivity() {
		// TODO Auto-generated method stub
		return getContext();
	}

	public void setHash(String url) {
		mParams.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
	}

	public void setChangData(boolean is) {
		mCollAdapter.setChangData(is);

	}
//
	public void setChangData(boolean is, ExplosionField mExplosionField) {
		if (mCollAdapter != null)
			mCollAdapter.setChangData(is, mExplosionField);
	}
	public void delete(int position, View view) {
		mCollAdapter.doCollect(position, view);
	}
}
