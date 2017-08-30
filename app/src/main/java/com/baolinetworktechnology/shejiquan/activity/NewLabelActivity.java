package com.baolinetworktechnology.shejiquan.activity;

/**
 * 标签列表
 */
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.NewLabelBean;
import com.baolinetworktechnology.shejiquan.domain.NewLabelList;
import com.baolinetworktechnology.shejiquan.itemRecycler.NewLableAdapter;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.List;

public class NewLabelActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private NewLableAdapter mAdapter;
    private MyBroadcastReciver mybroad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_label);
        initView();
        initData();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("showlable");
        intentFilter.addAction("deletelable");
        mybroad=new MyBroadcastReciver();
        NewLabelActivity.this.registerReceiver(mybroad, intentFilter);
    }
    private void showData( List<NewLabelBean> result) {
        mAdapter.setList(result);
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        recyclerView =(RecyclerView) findViewById(R.id.lableRecycler);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewLableAdapter(this);
        recyclerView.setAdapter(mAdapter);
        findViewById(R.id.add_lable).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }
    private void initData() {
        String lablestr=CacheUtils.getStringData(getActivity(), "lablelist", null);
        if (lablestr != null) {
            Gson gson = new Gson();
            NewLabelList bean = gson.fromJson(lablestr, NewLabelList.class);
            if (bean != null) {
                showData(bean.result);
            }
        }
        loadata();
    }
    private void loadata() {
        String url= AppUrl.GETEASELABLE+ SJQApp.user.getGuid();
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String message) {
                toastShow(message);
            }
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                NewLabelList bean=gson.fromJson(responseInfo.result, NewLabelList.class);
                if(bean!=null){
                    CacheUtils.cacheStringData( NewLabelActivity.this, "lablelist",responseInfo.result);
                    showData(bean.result);
                }

            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);
    }
    private void deletedata() {
        String url= AppUrl.GETEASELABLE+ SJQApp.user.getGuid();
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String message) {
                toastShow(message);
            }
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                NewLabelList bean=gson.fromJson(responseInfo.result, NewLabelList.class);
                if(bean!=null){
                    CacheUtils.cacheStringData( NewLabelActivity.this, "lablelist",responseInfo.result);
                }

            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);
    }
    private class MyBroadcastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            刷新数据
            if (action.equals("showlable")) {
                loadata();
            }
            if (action.equals("deletelable")) {
                deletedata();
            }

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add_lable:
                Intent intent = new Intent(this, MembersActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(recyclerView !=null){
            recyclerView.setAdapter(null);
            unregisterReceiver(mybroad);
        }
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
}
