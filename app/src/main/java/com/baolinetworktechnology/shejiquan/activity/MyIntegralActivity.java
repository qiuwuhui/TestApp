package com.baolinetworktechnology.shejiquan.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.IntegralBean;
import com.baolinetworktechnology.shejiquan.domain.MyIntegral;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyIntegralActivity extends BaseActivity implements ILoadData {
    private RefreshListView mCaseListView;
    private List<MyIntegral> IntegraList=new ArrayList<MyIntegral>();
    private IntegraAdater apter;
    private TextView TotalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_integral);
        inview();
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
    private void inview() {
        findViewById(R.id.back).setOnClickListener(this);
        mCaseListView = (RefreshListView)findViewById(R.id.listViewIntegra);
        mCaseListView.getPulldownFooter().isShowBottom(true);
        View view_integra = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_integra, null);
//        View view_integra=View.inflate(this,R.layout.view_integra, null);
        mCaseListView.getRefreshableView().addHeaderView(view_integra);
        apter=new IntegraAdater();
        mCaseListView.setAdapter(apter);
        mCaseListView.setOnLoadListener(this);
        TotalScore= (TextView) view_integra.findViewById(R.id.TotalScore);
        mCaseListView.setOnRefreshing();
    }
    private class IntegraAdater extends BaseAdapter {

        @Override
        public int getCount() {
            return IntegraList.size();
        }
        @Override
        public Object getItem(int position) {
            if (position < 0 || position >= IntegraList.size())
                return null;
            return IntegraList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder vh = null;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(),
                        R.layout.my_integral_layout, null);
                vh = new Holder();
                vh.Time_text= (TextView) convertView.findViewById(R.id.Time_text);
                vh.Score_text= (TextView) convertView.findViewById(R.id.Score_text);
                convertView.setTag(vh);
            } else {
                vh = (Holder) convertView.getTag();
            }
            MyIntegral bean=IntegraList.get(position);
            vh.Time_text.setText(bean.getTimeData());
            vh.Score_text.setText("+ "+bean.signinScore);
            return convertView;
        }
    }
    class Holder {
        TextView Time_text,Score_text;
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
        String url= AppUrl.GETSIGNINList+"userGuid="+ SJQApp.user.guid
                    +"&pageSize=10"+"&currentPage="+PageIndex;
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String message) {
                mCaseListView.setOnComplete();
                toastShow(message);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                mCaseListView.setOnComplete();
                JSONObject json;
                IntegralBean bean=null;
                try {
                    json = new JSONObject(responseInfo.result);
                    if(!json.getString("result").equals("null")){
                        String result1=json.getString("result");
                        Gson gson = new Gson();
                        bean=gson.fromJson(result1, IntegralBean.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (bean == null || bean.listData == null) {
                    if(PageIndex==1){
                    }else{
                        mCaseListView.setOnNullNewsData();
                    }
                }
                if (bean!= null && bean.listData !=null) {
                    if(PageIndex==1){
                        setDate(bean);
                    }else{
                        addDate(bean);
                    }
                }
            }


        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);

    }

    private void addDate(IntegralBean bean) {
        IntegraList.addAll(bean.listData);
        apter.notifyDataSetChanged();
    }
    private void setDate(IntegralBean bean) {
        if(bean.listData.size()!=0){
            TotalScore.setText(bean.listData.get(0).totalScore+"");
        }
        IntegraList.clear();
        IntegraList.addAll(bean.listData);
        apter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MyIntegralActivity");
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MyIntegralActivity");
    }
}
