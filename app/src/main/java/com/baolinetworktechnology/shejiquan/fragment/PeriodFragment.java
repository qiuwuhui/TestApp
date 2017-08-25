package com.baolinetworktechnology.shejiquan.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.PostDetailsActivity;
import com.baolinetworktechnology.shejiquan.activity.ReleaseActivity;
import com.baolinetworktechnology.shejiquan.adapter.PeriodListAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.domain.PostMainBean;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.socialize.utils.Log;

import android.view.View.OnClickListener;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 社区
 */
public class PeriodFragment extends BaseFragment implements OnClickListener,RefreshListView.ILoadData {
    public PeriodFragment() {
    }
    private List<PostBean> mList = new ArrayList<>();
    private View view;
    private PeriodListAdapter mAdapter;
    private RefreshListView mCaseListView;
    private CountDownTimer time;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_period, container, false);
            initView(view);
        }
        return view;
    }


    private void initView(View view) {
        ImageButton btn_release= (ImageButton) view.findViewById(R.id.btn_release);
        btn_release.setOnClickListener(this);

        mCaseListView = (RefreshListView)view.findViewById(R.id.periodView);
        mCaseListView.getPulldownFooter().isShowBottom(true);
        mAdapter = new PeriodListAdapter(getActivity());
        mCaseListView.setAdapter(mAdapter);
        mCaseListView.setOnLoadListener(this);
        view.findViewById(R.id.relativeLayout1).setOnClickListener(this);
        time = new CountDownTimer(1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                mCaseListView.setOnRefreshing();
            }
        };
        time.start();
//        mCaseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//                                    long arg3) {
//                PostBean bean= mAdapter.getItem(position-1);
//                if(bean == null){
//                    toastShow("数据出错");
//                    return;
//                }
//                Intent intent = new Intent(getActivity(), PostDetailsActivity.class);
//                intent.putExtra(AppTag.TAG_GUID,bean.getGuid());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("POSITION",position-1);//position
//                intent.putExtra("COLLECTIONNUMBER",bean.getnumFavorite());//收藏数
//                intent.putExtra("FABULOUSNUMBER",bean.getNumGood());//点赞数
//                getActivity().getApplicationContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_release:
                if(SJQApp.user == null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if(SJQApp.user.isBindMobile){
                    Intent intent = new Intent(getActivity(),ReleaseActivity.class);
                    startActivity(intent);
                }else{
                    toastShow("绑定手机号后才能发帖");
                }
                break;
            case R.id.relativeLayout1:
                setTabClick();
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
    private void loadata() {//是否强制刷新
//        String url = "";
//        if (SJQApp.user != null){
//            String  url = AppUrl.GETPAGELIST+"pageSize=10"+"&currentPage="+PageIndex+"&IsRefresh="+isQS+ "&userGUID="+SJQApp.user.getGuid();
//        }else {
        String url = AppUrl.GETPAGELISTNew+"&pageSize=10"+"&currentPage="+PageIndex+"&IsRefresh=true";
        if (SJQApp.user != null){
            url = url+"&userGUID="+SJQApp.user.getGuid();
        }
//        }
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
                if (bean == null || bean.listData == null || bean.listData.size()==0) {
                    if(PageIndex==1){
//                        no_layout.setVisibility(View.VISIBLE);
                    }else{
                        mCaseListView.setOnNullNewsData();
                    }
                }
                if (bean!= null && bean.listData != null &&  bean.listData.size()!=0) {
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
    public void setTabClick() {
        int position = mCaseListView.getRefreshableView()
                .getFirstVisiblePosition();
        if (position == 0) {
            mCaseListView.setOnRefreshing();
        } else {
            if (position < 8) {
//                if (position > 5)
//                mCaseListView.getRefreshableView().setSelection(5);
                mCaseListView.getRefreshableView().smoothScrollToPosition(0);
            } else {
                mCaseListView.getRefreshableView().setSelection(0);
            }

        }
    }
    public void refresh(){
        loadata();
    }
    public void updateitem(int position,int commentNum){
        if (position != -1){
            mAdapter.updateItemView(position,commentNum);
        }
    }
    public void updeitemCollection(int position,int num,boolean isCollection){//更新
        if (position != -1){
            mAdapter.updateCollection(position,num,isCollection);
        }

    }
    public void updeitemFobulows(int position,String str){//更新
        if (position != -1){
            mAdapter.updateFobulous(position,str);
        }

    }
}
