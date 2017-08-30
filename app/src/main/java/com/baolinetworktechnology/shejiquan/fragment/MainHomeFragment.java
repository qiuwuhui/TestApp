package com.baolinetworktechnology.shejiquan.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.YouLikeAdapter;
import com.baolinetworktechnology.shejiquan.activity.CodeActivity;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.activity.WebActivity;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.activity.MyFragmentActivity.IBackPressed;
import com.baolinetworktechnology.shejiquan.activity.SerchActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.CaseClassJDlist;
import com.baolinetworktechnology.shejiquan.domain.CaseClassList;
import com.baolinetworktechnology.shejiquan.domain.YouLikeList;
import com.baolinetworktechnology.shejiquan.domain.YouLikebean;
import com.baolinetworktechnology.shejiquan.domain.YouLikemode;
import com.baolinetworktechnology.shejiquan.domain.ZiXunBean;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.view.HomeHead;
import com.baolinetworktechnology.shejiquan.view.KoySliding;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 首页
 *
 * @author JiSheng.Guo
 */
public class MainHomeFragment extends BaseMainFragment implements IBackPressed,
        ILoadData {
    private RefreshListView mListViewNews;
    //	private KoySliding koySliding;
    private YouLikeAdapter adapter;
    private ImageView ding_bu;
    private HomeHead head;
    private View view = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_main_home,
                    null);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        ding_bu = (ImageView) view.findViewById(R.id.ding_bu);
        ding_bu.setOnClickListener(this);
        view.findViewById(R.id.Gofor).setOnClickListener(this);
        view.findViewById(R.id.editTex_serch).setOnClickListener(this);
        mListViewNews = (RefreshListView) view.findViewById(R.id.listViewNews);
        mListViewNews.getPulldownFooter().isShowBottom(true);
        mListViewNews.setOnLoadListener(this);
        head = new HomeHead(getActivity());
//		koySliding = head.getKoySliding();
        mListViewNews.getRefreshableView().addHeaderView(head);
        adapter = new YouLikeAdapter(getActivity(), true);
        mListViewNews.setAdapter(adapter);
        mListViewNews.setOnRefreshing();
        mListViewNews.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                YouLikemode item = adapter.getItem(position - 2);
                if (item == null) {
                    return;
                }
                if (item.recommendType == 0) {
                    Intent intent = new Intent(getActivity(), WebOpusActivity.class);
                    String url = AppUrl.DETAIL_CASE2 + item.recomendData.getId();
                    intent.putExtra("WEB_URL", url);
                    intent.putExtra(AppTag.TAG_ID, item.recomendData.getId());
                    getActivity().startActivity(intent);
                } else if (item.recommendType == 1) {
                    if (TextUtils.isEmpty(item.recomendData.getLinkUrl())) {
                        Intent intent = new Intent(getActivity(), WebDetailActivity.class);
                        intent.putExtra(WebDetailActivity.GUID, "0");
                        intent.putExtra(WebDetailActivity.ID, item.recomendData.getId() + "");
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        String url = item.recomendData.getLinkUrl();
                        intent.putExtra(WebActivity.URL, url);
                        getActivity().startActivity(intent);
                    }
                }
            }
        });
        if (!CommonUtils.hasNetWorkInfo(getContext())) {
            String cace = CacheUtils.getStringData(getActivity(), "HomeList", null);
            if (cace != null) {
                showZT(cace);
            }
        } else {
            loadata();
        }
    }

    private void showZT(String cace) {
        Gson gson = new Gson();
        YouLikebean newsBean = gson.fromJson(cace, YouLikebean.class);
        if (newsBean != null && newsBean.listData != null) {
            adapter.setData(newsBean.listData);
        }
    }

    private int pageIndex = 1;
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Gofor:
                setTabClick();
                break;
            case R.id.ding_bu:
                adapter.setISshow();
                ding_bu.setVisibility(View.GONE);
                setTabClick();
                break;
            case R.id.editTex_serch:
                Intent intent2 = new Intent(getActivity(), SerchActivity.class);
                intent2.putExtra(AppTag.TAG_ID, 0);
                getActivity().startActivity(intent2);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
//	@Override
//	public void onStop() {
//		super.onStop();
//		if (koySliding != null) {
//			koySliding.cancelAutoPlay();
//		}
//	}
//
//	@Override
//	public void onStart() {
//		super.onStart();
//		if (koySliding != null) {
//			koySliding.restart();
//		}
//	}

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void backPressed() {

    }

    @Override
    public void loadData(boolean isRefresh) {
        if (isRefresh) {        // 刷新
            pageIndex = 1;
        } else {
            pageIndex++;
        }
        loadata();
    }

    private void loadata() {

        String url = AppUrl.CUESSYOULIKE + pageIndex;
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String message) {
                mListViewNews.setOnComplete();
                toastShow("当前网络不稳定，请检查网络连接...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (mListViewNews == null || adapter == null)
                    return;
                mListViewNews.setOnComplete();
//				YouLikebean bean = CommonUtils.getDomain(responseInfo, YouLikebean.class);
                JSONObject json;
                YouLikebean bean = null;
                try {
                    json = new JSONObject(responseInfo.result);
                    String result1 = json.getString("result");
                    if (!TextUtils.isEmpty(result1)) {
                        if (pageIndex == 1) {
                            CacheUtils.cacheStringData(
                                    getActivity(), "HomeList",
                                    result1);
                        }
                    }
                    Gson gson = new Gson();
                    bean = gson.fromJson(result1, YouLikebean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (bean == null || bean.listData == null || bean.listData.size() == 0) {
                    if (pageIndex == 1) {
                        mListViewNews.setOnNullData("抱歉，没有数据");
                    } else {
                        mListViewNews.setOnNullNewsData();
                    }
                }
                if (bean != null) {
                    if (pageIndex == 1) {
                        adapter.setData(bean.listData);
                    } else {
                        adapter.addData(bean.listData);
                    }
                }
            }
        };
        System.out.println(ApiUrl.API + url);
        getHttpUtils().send(HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);
    }

    public void setTabClick() {
        int position = mListViewNews.getRefreshableView()
                .getFirstVisiblePosition();
        if (position == 0) {
            mListViewNews.setOnRefreshing();
        } else {
            if (position < 8) {
                if (position > 5)
                    mListViewNews.getRefreshableView().setSelection(5);
                mListViewNews.getRefreshableView().smoothScrollToPosition(0);
            } else {
                mListViewNews.getRefreshableView().setSelection(0);
            }

        }
    }

    public void setDinBu() {
        ding_bu.setVisibility(View.VISIBLE);
    }

    public void go2GuideActivity() {
        head.DesignershowLunBo();
    }
}
