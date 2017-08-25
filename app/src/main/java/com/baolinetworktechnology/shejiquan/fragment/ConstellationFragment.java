package com.baolinetworktechnology.shejiquan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.SkipActivity;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
import com.baolinetworktechnology.shejiquan.view.ListViewNews;
import com.baolinetworktechnology.shejiquan.view.ListViewNewsCons;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;

/**
 * Created by Administrator on 2017/3/20.
 */

public class ConstellationFragment extends BaseFragment implements RefreshListView.ILoadData {
    private ListViewNewsCons mListViewNews;
    private OnStateListener onStateListener;
    private CountDownTimer time;
    private ImageView ding_bu;
    private View layout;
    public void setonStateListener(OnStateListener onStateListener) {
        this.onStateListener = onStateListener;
        if (mListViewNews != null)
            mListViewNews.setonStateListener(onStateListener);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (layout == null){
            layout=inflater.inflate(R.layout.zi_xuncons_fragment, container, false);
            ding_bu = (ImageView) layout.findViewById(R.id.ding_bu);
            ding_bu.setOnClickListener(this);
            mListViewNews = (ListViewNewsCons) layout.findViewById(R.id.conslistView);
            mListViewNews.setonStateListener(onStateListener);
            time = new CountDownTimer(800, 800) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    mListViewNews.setCaceKey("ConstellationFragment");
                    mListViewNews.setDev(1);
                    mListViewNews.setClassID(162,0);
                }
            };
            time.start();
        }
        return layout;
    }
    private int PageIndex=1;
    @Override
    public void loadData(boolean isRefresh) {
        if(isRefresh){
            PageIndex=1;
        }else{
            PageIndex++;
        }
    }
    public void setTabClick() {
        if (getActivity() == null)
            return;
        if (mListViewNews == null
                || mListViewNews.getRefreshableView() == null) {
            go2Splash();
            return;
        }
        int position = mListViewNews.getRefreshableView()
                .getFirstVisiblePosition();
        if (position == 0) {
            mListViewNews.setOnRefreshing();
        } else {
            if (position > 5) {
                mListViewNews.getRefreshableView().setSelection(5);
            }
            mListViewNews.getRefreshableView().smoothScrollToPosition(0);
        }

    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ding_bu:
                ding_bu.setVisibility(View.GONE);
                setTabClick();
                break;
        }
    }
    void go2Splash() {
        toastShow("抱歉程序出现问题，即将重启动");
        startActivity(new Intent(getActivity(), SkipActivity.class));
        getActivity().finish();
    }
    public void setDinBu(){
        ding_bu.setVisibility(View.VISIBLE);
    }
}
