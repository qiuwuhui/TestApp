package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.content.Intent;
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
import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.adapter.NewOpusHomenAdapter;
import com.baolinetworktechnology.shejiquan.adapter.OpusHomenAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.CaseBean;
import com.baolinetworktechnology.shejiquan.domain.CasePageList;
import com.baolinetworktechnology.shejiquan.domain.GongSiMyanliBean;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设计师
 *
 * @author JiSheng.Guo
 */
public class ListHomeCase extends MyListView implements OnClickListener {
    private HttpHandler<String> mHandler;
    private NewOpusHomenAdapter mAdapter;
    private ListHomeCase listHomeCase;
    private String CACE_KEY = "";
    private int PageIndex = 1;
    private boolean mIsCace = false;// 是否缓存过（只缓存一次）
    private boolean mIsDivPage;
    private int OrderBy = 0, ItemID = 0, ClassID = 5;
    private String attrStyle = "";
    private String attrHouseType = "";
    private String size = "";
    private String MarkName = AppTag.MARKNAME_DESIGNER;
    String Budget = "";
    private Context Mcontext;
    private int firstVisiblePosition;

    public ListHomeCase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        this.Mcontext = context;
    }

    public ListHomeCase(Context context) {
        super(context);
        this.Mcontext = context;
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
        CasePageList data = CommonUtils.getDomain(cace, CasePageList.class);
        if (data != null && data.listData != null) {
            mAdapter.setData(data.listData);
        }
    }

    /**
     * 以下方法 勿修改
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
        mAdapter = new NewOpusHomenAdapter(getActivity());
        setAdapter(mAdapter);
//		setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				SwCase item = null;
//				item = mAdapter.getItem(position - 1);
//				if (item == null)
//					return;
//				Intent intent = new Intent(getActivity(), WebOpusActivity.class);
//				String url = AppUrl.DETAIL_CASE2 + item.id+"&r="+System.currentTimeMillis();
//				intent.putExtra("WEB_URL", url);
//				intent.putExtra("classTitle", "");
//				intent.putExtra(WebDetailActivity.GUID, item.guid);
//				intent.putExtra(AppTag.TAG_ID, item.id);
//				intent.putExtra(WebDetailActivity.ISCASE, true);
//				intent.putExtra(AppTag.TAG_JSON, item.toString());
//				intent.putExtra("POSITION", position);//
//				intent.putExtra("SUMBER", item.getNumFavorite());//收藏人数
//				getContext().startActivity(intent);
//			}
//
//		});

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
                if (firstVisibleItem >= 5) {
                    Intent intent = new Intent();
                    intent.setAction("HomeCaseshow");
                    Mcontext.sendBroadcast(intent);
                    firstVisiblePosition = firstVisibleItem;
                }
                mIsDivPage = (firstVisibleItem + visibleItemCount == totalItemCount);
            }
        });

    }

    boolean isLoading = false;
    String desc = "抱歉，暂时没有找到相关的案例";

    public void setNullDataTips(String nullDataTips) {
        this.desc = nullDataTips;
    }

    // 请求数据falg是否加载更多
    public void loadData(final boolean falg) {
        if (!falg) {
            // footerTitle.setText("加载中...");
            pulldownFooter.setState(PulldownFooter.STATE_COMPLETE);
            // 不是加载更多
            PageIndex = 1;
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
            PageIndex++;
            if (onStateListener != null) {
                onStateListener.onStateListener(true);
            }
            pulldownFooter.setState(PulldownFooter.STATE_LOGING);
        }
        String url = "";

        if (KeyWord != null) {
            url = AppUrl.CASELISET + "ClassID=" + ClassID + "&attrStyle=" + attrStyle
                    + "&attrHouseType=" + attrHouseType + "&size=" + size + "&orders=12"
                    + "&ItemID=" + ItemID + "&currentPage=" + PageIndex + "&pageSize=10" +
                    "&MarkName=" + MarkName + "&Budget=" + Budget;
            if (SJQApp.user != null) {
                url = url + "&selectUserGuid=" + SJQApp.user.getGuid();
            }
        } else {
            url = AppUrl.CASELISET + "GlassID=" + ClassID + "&attrStyle=" + attrStyle
                    + "&attrHouseType=" + attrHouseType + "&size=" + size + "&orders=12"
                    + "&ItemID=" + ItemID + "&currentPage=" + PageIndex + "&pageSize=10" +
                    "&MarkName=" + MarkName + "&Budget=" + Budget;
            if (SJQApp.user != null) {
                url = url + "&selectUserGuid=" + SJQApp.user.getGuid();
            }
        }
        LogUtils.i("designer", url);
        final String urls = url;
        mHandler = getHttpUtils().send(HttpMethod.GET, AppUrl.API + url,
                getParams(url), new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        onRefreshComplete();
                        if (onStateListener != null) {
                            onStateListener.onStateListener(false);
                        }
                        if (falg) {
                            PageIndex--;
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
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        pulldownFooter.setState(PulldownFooter.STATE_COMPLETE);
                        isLoading = false;
                        JSONObject json;
                        CasePageList newsBean = null;
                        try {
                            json = new JSONObject(responseInfo.result);
                            String result1 = json.getString("result");
                            Gson gson = new Gson();
                            newsBean = gson.fromJson(result1, CasePageList.class);
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
                                                responseInfo.result);
                                        mIsCace = true;
                                    }
                                }
                            } else {
                                if (newsBean.listData.size() == 0) {
                                    pulldownFooter
                                            .setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
                                }
                                mAdapter.addData(newsBean.listData);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            if (falg) {
                                PageIndex--;
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

    String KeyWord = null;
    private OnStateListener onStateListener;

    public void setTags(String tags) {
        // TODO Auto-generated method stub
        this.attrStyle = tags;
        setRefreshing();
    }

    public void setattrHouseType(String attrHouseType) {
        // TODO Auto-generated method stub
        this.attrHouseType = attrHouseType;
        setRefreshing();
    }

    public void setSize(String size) {
        // TODO Auto-generated method stub
        this.size = size;
        setRefreshing();
    }

    public void setBudget(String Budget) {
        this.Budget = Budget;
    }

    public void setClassID(int classID) {
        ClassID = classID;
    }

    public void setonStateListener(OnStateListener mOnStateListener) {
        this.onStateListener = mOnStateListener;

    }

    public NewOpusHomenAdapter ObjectList() {
        if (mAdapter != null) {
            return mAdapter;
        }
        return null;
    }

    public int getFirstVisiblePosition() {
        return firstVisiblePosition;
    }
}
