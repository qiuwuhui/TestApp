package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.ThumbupAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.GongSiMyanliBean;
import com.baolinetworktechnology.shejiquan.domain.MyanliBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.SwthumbBean;
import com.baolinetworktechnology.shejiquan.domain.ThumbBean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ThumbUpActivity extends BaseActivity implements View.OnClickListener,RefreshListView.ILoadData {
    private RefreshListView mCaseListView;
    private ThumbupAdapter mAdapter;
    private CountDownTimer time;
    private int nullDdrawable = R.drawable.icon_my_dianz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumb_up);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.relativeLayout1).setOnClickListener(this);
        //魅族
        CommonUtils.setMeizuStatusBarDarkIcon(ThumbUpActivity.this, true);
        //小米
        CommonUtils.setMiuiStatusBarDarkMode(ThumbUpActivity.this, true);

        mCaseListView = (RefreshListView)findViewById(R.id.listThumd);
        mCaseListView.getPulldownFooter().isShowBottom(true);
        mAdapter = new ThumbupAdapter(getApplicationContext());
        mCaseListView.setAdapter(mAdapter);
        mCaseListView.setOnLoadListener(this);
        mCaseListView.setOnRefreshing();
        mCaseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                ThumbBean bean= (ThumbBean) mAdapter.getItem(position-1);
                if(bean != null){
                    if(bean.getMarkName()==1){
                        Intent intent = new Intent(ThumbUpActivity.this, PostDetailsActivity.class);
                        intent.putExtra(AppTag.TAG_GUID,bean.getPostsId()+"");
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(ThumbUpActivity.this, MyContacActivity.class);
                        intent.putExtra(AppTag.TAG_GUID,bean.getPostsGuid());
                        startActivity(intent);
                    }
                }
            }
        });
        time = new CountDownTimer(500, 500) {

            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                if(SJQApp.messageBean!=null){
                    if(SJQApp.messageBean.getResult().getNumPostsGood()!=0){
                        allMesage();
                    }
                }
            }
        };
        time.start();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.relativeLayout1:
                setTabClick();
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
        String url = AppUrl.GETPOSTSGOOD+"userGUID="+ SJQApp.user.getGuid()
                +"&pageSize=10"+"&currentPage="+PageIndex;

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
                SwthumbBean bean=null;
                try {
                    json = new JSONObject(responseInfo.result);
                    String result1=json.getString("result");
                    Gson gson = new Gson();
                    bean=gson.fromJson(result1, SwthumbBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (bean == null || bean.getListData() == null) {
                    if(PageIndex==1){
                        mCaseListView.setOnNullData("还没有收到任何的赞");
                        mCaseListView.setNullData(nullDdrawable);
                    }else{
                        mCaseListView.setOnNullNewsData("已显示全部收到的赞");
                    }
                }
                if (bean!= null) {
                    if(PageIndex==1){
                        mAdapter.setData(bean.getListData());
                    }else{
                        mAdapter.addData(bean.getListData());
                    }
                }
            }


        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);

    }
    //阅读状态全部改变为已读
    protected void allMesage() {
        String url = AppUrl.UpdatePOSTSRead;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type","application/json");
        try {
            JSONObject param  = new JSONObject();
            param.put("userGuid", SJQApp.user.getGuid());
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        }catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String message) {
                toastShow(message);
            }

            @Override
            public void onSuccess(ResponseInfo<String> n) {
                Gson gson = new Gson();
                SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
                if (bean != null) {
                    if (bean.result.operatResult) {
                        SJQApp.messageBean.getResult().setNumPostsGood(0);
                        Intent intent = new Intent();
                        intent.setAction("ReadMag");
                        getActivity().sendBroadcast(intent);
                    }
                }
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.PUT, AppUrl.API+ url,params,
                callBack);
    }
    public void setTabClick() {
        int position = mCaseListView.getRefreshableView()
                .getFirstVisiblePosition();
        if (position == 0) {
            mCaseListView.setOnRefreshing();
        } else {
            if (position < 8) {
//                if (position > 5)
//                mCaseListView.getRefreshableView().setSelection(5);
                mCaseListView.getRefreshableView().setSelection(0);
            } else {
                mCaseListView.getRefreshableView().setSelection(0);
            }

        }
    }
}
