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

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.PostDetailsActivity;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.adapter.SwCollectOwnerOpusAdapter;
import com.baolinetworktechnology.shejiquan.adapter.SwOwnerPotAdapter;
import com.baolinetworktechnology.shejiquan.adapter.SwOwnerPotAdapter2;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.domain.PostMainBean;
import com.baolinetworktechnology.shejiquan.fragment.CollectPostFragment;
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
 */
public class PostListView extends PullToRefreshListView {
    private HttpUtils mHttpUtils;
    // private RequestCallBack<String> callBack;
    private SwOwnerPotAdapter2 mCollAdapter;
    private MyDialog dialog;
    // private boolean flag = false;
    private int mPageIndex = 1;
    private RequestParams mParams;
    private String mUserGuid;
    private boolean mIsDivPage;
    String desc = "你还没有帖子资讯哦!";
    private int nullDdrawable = R.drawable.icon_wdsctb;
    String caceKey = "";
    Context mContext;
    private boolean isDeleteMode = false;

    public PostListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    boolean isCollect = false;
    private PulldownFooter pulldownFooter;

    public void setUserGuid(String UserGuid, boolean isCollect) {
        this.isCollect = isCollect;
        this.mUserGuid = UserGuid;

        if (isCollect) {
            caceKey = "collect" + mUserGuid;
            getCace();
        }
        // 设置适配器
        // dialog.show();
        mPageIndex = 1;
        setRefreshing();

    }

    private void getCace() {
        PostMainBean newsBean = CommonUtils.getDomain(
                CacheUtils.getStringData(getActivity(), "List", caceKey, ""),
                PostMainBean.class);
        if (newsBean != null && newsBean.listData != null) {
            if (newsBean.listData.size() > 0) {
                if (mCollAdapter != null)
                    mCollAdapter.setData(newsBean.listData);
            }
        }
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
        // getRefreshableView().setDivider(
        // getResources().getDrawable(R.drawable.list_divider));
        // getRefreshableView().setDividerHeight(32);
        // setMode(Mode.BOTH);
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
//                if (isDeleteMode) {
//
//                } else {
                    PostBean news = (PostBean) mCollAdapter.getItem(position - 1);
                    if (news == null)
                        return;
                    Intent intent = new Intent(mContext, PostDetailsActivity.class);
                    intent.putExtra(AppTag.TAG_GUID, news.getGuid());
                    intent.putExtra("IsPost", "0");
                    mContext.startActivity(intent);
//                }

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
        mCollAdapter = new SwOwnerPotAdapter2(getActivity());
        setAdapter(mCollAdapter);
    }

    // 请求数据falg是否加载更多
    private void loadData(final boolean falg) {
        if (!falg) {
            mPageIndex = 1;
            // setRefreshing();

        } else {
            pulldownFooter.setState(PulldownFooter.STATE_LOGING);
        }

        String url = AppUrl.GETCOLLECTPOSTSLIST + "&userGuid=" + mUserGuid
                + "&currentPage=" + mPageIndex + "&pageSize=10";
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
                        PostMainBean bean = null;
                        try {
                            json = new JSONObject(n.result);
                            String result1 = json.getString("result");
                            Gson gson = new Gson();
                            bean = gson.fromJson(result1, PostMainBean.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (bean != null && bean.listData != null) {

                            if (!falg) {
                                if (!TextUtils.isEmpty(caceKey))
                                    CacheUtils.cacheStringData(getActivity(),
                                            "List", caceKey, n.result);
                                mCollAdapter.setData(bean.listData);

                            } else {
                                if (bean.listData.size() == 0) {
                                    pulldownFooter
                                            .setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA);
                                }
                                mCollAdapter.addData(bean.listData);
                            }

                        } else {
                            if (!falg) {
                                pulldownFooter
                                        .setState(
                                                PulldownFooter.STATE_COMPLETE_NULL_DATA,
                                                desc);
                                pulldownFooter.setNullData(nullDdrawable);
                                setAdapter(null);
                            } else {
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
        mCollAdapter = null;
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

    public void setChangData(boolean is, ExplosionField mExplosionField) {
        if (mCollAdapter != null)
            mCollAdapter.setChangData(is, mExplosionField);

    }

    public void delete(int position, View view) {
    }
    //批量删除
    public void bitchdelete(){
        mCollAdapter.doBitchCollect();
    }

    public void setDeleteMode(boolean ismode) {
        isDeleteMode = ismode;
        mCollAdapter.showDelete(isDeleteMode);
    }
}
