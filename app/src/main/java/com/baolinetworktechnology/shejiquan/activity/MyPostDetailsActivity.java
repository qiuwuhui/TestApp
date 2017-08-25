package com.baolinetworktechnology.shejiquan.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.PeriodListAdapter;
import com.baolinetworktechnology.shejiquan.adapter.myPostAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.GongSiMyanliBean;
import com.baolinetworktechnology.shejiquan.domain.NineGridTestModel;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.domain.PostMainBean;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPostDetailsActivity extends BaseActivity implements View.OnClickListener,RefreshListView.ILoadData {
    private String TAG = "MyPostDetails";
    private myPostAdapter mAdapter;
    private RefreshListView mCaseListView;
    private TextView mChangeState,mDelete;
    private boolean mIsDeleteMode;//是否进入删除模式
    private int nullDdrawable = R.drawable.icon_my_post;
    private RelativeLayout deleteLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_post_details);
        initView();
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
    private void initView() {
        mCaseListView = (RefreshListView)findViewById(R.id.MypostList);
        mChangeState = (TextView) findViewById(R.id.changeState);
        deleteLayout = (RelativeLayout) findViewById(R.id.deleteLayout);
        mDelete = (TextView) findViewById(R.id.tv_delete);
        mDelete.setOnClickListener(this);
        mCaseListView.getPulldownFooter().isShowBottom(true);
        mCaseListView.setOnLoadListener(this);
        mAdapter = new myPostAdapter(this);
        mIsDeleteMode = false;
        mCaseListView.setAdapter(mAdapter);
//        mCaseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//                                    long arg3) {
//                if (mIsDeleteMode){//删除模式，点击选中
//
//                }else {
//                    PostBean bean= mAdapter.getItem(position-1);
//                    Intent intent = new Intent(getActivity(), PostDetailsActivity.class);
//                    intent.putExtra(AppTag.TAG_GUID,bean.getGuid());
//                    startActivity(intent);
//                }
//            }
//        });
        mChangeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsDeleteMode){
                    mIsDeleteMode = false;
                }else {
                    mIsDeleteMode = true;
                }
                if (mIsDeleteMode){//进入删除
                    deleteLayout.setVisibility(View.VISIBLE);
                    mChangeState.setText("保存");
                    mAdapter.isIsdeletemode(true);
                    mAdapter.notifyDataSetChanged();
                    mDelete.setVisibility(View.VISIBLE);
                }else {
                    deleteLayout.setVisibility(View.GONE);
                    mChangeState.setText("编辑");
                    mAdapter.isIsdeletemode(false);
                    mAdapter.emptyMap();
                    mAdapter.notifyDataSetChanged();
                    mDelete.setVisibility(View.GONE);
                }
            }
        });
        mCaseListView.setOnRefreshing();
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.share_show).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.share_show:
                    if(SJQApp.user.isBindMobile){
                        Intent intent = new Intent(getActivity(),ReleaseActivity.class);
                        startActivity(intent);
                    }else{
                        toastShow("绑定手机号后才能发帖");
                    }
                    break;
                case R.id.tv_delete://批量删除
//                    toastShow("批量删除");
//                    if (mAdapter.Detele()== null){
//                        toastShow("请选择一条动态");
//                    }else{
                        mAdapter.MoreDeletePost();
//                    }
                    break;
                default:
                    break;
            }
    }

    private int PageIndex=1;
    @Override
    public void loadData(boolean isRefresh) {
        if(isRefresh){
            PageIndex=1;
        }else{
            PageIndex++;
        }
        loadata();
    }
    private void loadata() {
        String url = AppUrl.GETPAGELIST+"pageSize=10"+"&currentPage="+PageIndex+"&userGUID="+ SJQApp.user.getGuid()+"&IsRefresh=true";
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String message) {
                mCaseListView.setOnComplete();
                toastShow("网络连接错误，请检查网络");
            }
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                mCaseListView.setOnComplete();
                JSONObject json;
                PostMainBean bean=null;
                try {
                    json = new JSONObject(responseInfo.result);
                    String result1=json.getString("result");
                    Gson gson = new Gson();
                    bean=gson.fromJson(result1, PostMainBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (bean == null || bean.listData == null) {
                    if(PageIndex==1){
                        mCaseListView.setOnNullData("还没有发布帖子!");
                        mCaseListView.setNullData(nullDdrawable);
                    }else{
                        mCaseListView.setOnNullNewsData();
                    }
                }
                if (bean!= null) {
                    if(PageIndex==1){
                        mAdapter.setData(bean.listData);
                    }else{
                        mAdapter.addData(bean.listData);
                    }
                }
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);
    }
}
