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

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.adapter.CollectOwnerNewsAdapter;
import com.baolinetworktechnology.shejiquan.adapter.CollectOwnerNewsAdapter2;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.News;
import com.baolinetworktechnology.shejiquan.domain.NewsBean;
import com.baolinetworktechnology.shejiquan.domain.ZiXunBean;
import com.baolinetworktechnology.shejiquan.domain.ZiXunList;
import com.baolinetworktechnology.shejiquan.fragment.CollectCaseFragment;
import com.baolinetworktechnology.shejiquan.fragment.CollectNewsFragment;
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
 * 收藏资讯ListView
 * 
 * @author gjs
 * 
 */
public class NewsListView extends PullToRefreshListView {
	private HttpUtils mHttpUtils;
	private PulldownFooter mPulldownFooter;
	private CollectOwnerNewsAdapter2 mAdapter;
	private RequestParams mParams;
	private String mUserGuid;
	private boolean mIsDivPage;
	private boolean mIsCase = false;
	private int mPageIndex = 1;
	String desc = "你还没有收藏资讯哦!";
	private int nullDdrawable = R.drawable.icon_wdsctb;
	public NewsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public void setUserGuid(String UserGuid, boolean isCollect) {
		this.mIsCase = isCollect;
		this.mUserGuid = UserGuid;

		//mDialog.show();
		refreshData();
	}

	private void initView() {
		if (mHttpUtils == null) {
			mHttpUtils = new HttpUtils();
		}
		mParams = new RequestParams();
		mParams.setHeader("Token", null);
		mParams.setHeader("Version", ApiUrl.VERSION);
		mParams.setHeader("Client", AppTag.CLIENT);
		mParams.setHeader("AppAgent", ApiUrl.APP_AGENT);
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

				refreshData();

			}

		});
		// 设置条目点击事件
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ZiXunList news = (ZiXunList) mAdapter.getItem(position - 1);
				if(news !=null){
					Intent intent = new Intent(getActivity(), WebDetailActivity.class);
					intent.putExtra(WebDetailActivity.GUID, news.guid);
					intent.putExtra(WebDetailActivity.ID, news.id+"");
					getActivity().startActivity(intent);
				}
			}

		});
		View view = View.inflate(getContext(), R.layout.pulldown_footer, null);
		mPulldownFooter = new PulldownFooter(getActivity(), view);
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
					mPulldownFooter.setState(PulldownFooter.STATE_LOGING);
					onLastItemVisible();
					// view.setVisibility(View.VISIBLE);
					// footerTitle.setText("更多数据正在加载中");
					//
					// pulldown_footer_loading.setVisibility(View.VISIBLE);
				} else if (!mIsDivPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					mPulldownFooter.setState(PulldownFooter.STATE_FREE);
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
		// 设置适配器
		mAdapter = new CollectOwnerNewsAdapter2(getActivity());
		setAdapter(mAdapter);
	}

	// 请求数据falg是否加载更多
	private void loadData(final boolean falg) {
		if (!falg) {
			mPageIndex = 1;
			setRefreshing();

		}else{
			mPulldownFooter.setState(PulldownFooter.STATE_LOGING);
		}
		String url = AppUrl.GET_NEWS_LIST + "&userGUID=" + mUserGuid
				+ "&currentPage=" + mPageIndex + "&pageSize=10";
		setHash(url);
		mHttpUtils.send(HttpMethod.GET, AppUrl.API + url, mParams,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						onRefreshComplete();
						mPulldownFooter.setText("网络请求错误");
						if (falg) {
							mPageIndex--;
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						//mDialog.dismiss();
						if (mAdapter == null)
							return;
						mPulldownFooter.setState(PulldownFooter.STATE_COMPLETE);

						JSONObject json;
						ZiXunBean newsBean=null;
						try {
							json = new JSONObject(n.result);
							String result1=json.getString("result");
							Gson gson = new Gson();
							newsBean=gson.fromJson(result1, ZiXunBean.class);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (newsBean != null && newsBean.listData != null) {

							if (!falg) {
								if (newsBean.listData.size() == 0) {
								}
								mAdapter.setData(newsBean.listData);

							} else {
								if (newsBean.listData.size() == 0) {
									mPulldownFooter
											.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
								}
								mAdapter.addData(newsBean.listData);
							}

						} else {
							if (!falg) {
								mPulldownFooter
										.setState(
												PulldownFooter.STATE_COMPLETE_NULL_DATA,
												desc);
								mPulldownFooter.setNullData(nullDdrawable);
								setAdapter(null);
							}else{
								mPulldownFooter
										.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
							}
							mPageIndex--;

						}
						onRefreshComplete();
					}
				});

	}

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

	// View 销毁
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mPulldownFooter = null;
		mAdapter = null;
		// flag = false;
	}

	private Context getActivity() {
		// TODO Auto-generated method stub
		return getContext();
	}

	public void setHash(String url) {
		mParams.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
	}

	// 编辑
	public void setChangData(boolean is) {
		mAdapter.setChangData(is);

	}
	
	public void delete(int position, View view){
		mAdapter.doCollect(position, view);
	}
	
	

	public void setChangData(boolean is, ExplosionField mExplosionField) {
		// TODO Auto-generated method stub
		mAdapter.setChangData(is,mExplosionField);
	}
	private boolean isDeleteMode = false;
	//批量删除
	public void bitchdelete(){
		mAdapter.doBitchCollect();
	}

	public void setDeleteMode(boolean ismode) {
		isDeleteMode = ismode;
		mAdapter.showDelete(isDeleteMode);
	}
}
