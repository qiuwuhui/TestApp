package com.baolinetworktechnology.shejiquan.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.activity.PostDetailsActivity;
import com.baolinetworktechnology.shejiquan.activity.ReleaseActivity;
import com.baolinetworktechnology.shejiquan.activity.ReleaseCricleActivity;
import com.baolinetworktechnology.shejiquan.adapter.CricleListAdapter;
import com.baolinetworktechnology.shejiquan.adapter.PeriodListAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.baolinetworktechnology.shejiquan.domain.CricleBean;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.domain.PostCricleBean;
import com.baolinetworktechnology.shejiquan.domain.PostMainBean;
import com.baolinetworktechnology.shejiquan.domain.ReplayUser;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.postcommentList;
import com.baolinetworktechnology.shejiquan.view.MyRelativeLayout;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 朋友圈
 */
public class CricleFragment extends BaseFragment implements OnClickListener,RefreshListView.ILoadData {
    public CricleFragment() {
    }
    private List<CricleBean> mList = new ArrayList<>();
    private View view;
    private CricleListAdapter mAdapter;
    private RefreshListView mCaseListView;
    private CountDownTimer time;
    private View item_pingluen,item_wei;
    private EditText pingluen_text;
    private TextView fasong_btn;
    private MyRelativeLayout mRoot1;
    private InputMethodManager imm;
    private List<CricleBean> mData =new ArrayList<>();
    private MyBroadcastReciver mybroad;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_circle, container, false);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("criclePin");
            intentFilter.addAction("criclehui");
            intentFilter.addAction("setRefresh");
            mybroad=new MyBroadcastReciver();
            getActivity().registerReceiver(mybroad, intentFilter);
            initView(view);
        }
        return view;
    }
    private void initView(View view) {
        ImageButton btn_release= (ImageButton) view.findViewById(R.id.btn_release);
        btn_release.setOnClickListener(this);
        item_wei= view.findViewById(R.id.item_wei);
        view.findViewById(R.id.btnOK).setOnClickListener(this);
        if(SJQApp.user!=null){
            item_wei.setVisibility(View.GONE);
        }
        item_pingluen = view.findViewById(R.id.item_pingluen);
        pingluen_text = (EditText) view.findViewById(R.id.pingluen_text);
        pingluen_text.clearFocus();
        pingluen_text.addTextChangedListener(watcher);
        fasong_btn = (TextView) view.findViewById(R.id.fasong_btn);
        fasong_btn.setOnClickListener(this);
        imm = (InputMethodManager) pingluen_text.getContext().getSystemService(
                getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);
        mRoot1 = (MyRelativeLayout) view.findViewById(R.id.cricle_rela);
        mRoot1.setFitsSystemWindows(true);
        mCaseListView = (RefreshListView)view.findViewById(R.id.periodView);
        mCaseListView.getPulldownFooter().isShowBottom(true);
        mAdapter = new CricleListAdapter(getActivity());
        mCaseListView.setAdapter(mAdapter);
        mCaseListView.setOnLoadListener(this);
        time = new CountDownTimer(700, 7000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                if(SJQApp.user !=null){
                    mCaseListView.setOnRefreshing();
                }
            }
        };
        time.start();
        mCaseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                if (item_pingluen.getVisibility() == View.VISIBLE) {
                    FromID="0";
                    Fromguid ="";
                    position = -1;
                    replayUserName="";
                    ownerGUID="";
                    pingluen_text.setHint("");
                    pingluen_text.setText("");
                    item_pingluen.setVisibility(View.GONE);
                    imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法
                }
            }
        });
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
                    Intent intent = new Intent(getActivity(),ReleaseCricleActivity.class);
                    startActivity(intent);
                }else{
                    toastShow("绑定手机号后才能发动态");
                }
                break;
            case R.id.btnOK:
                ((SJQApp) getActivity().getApplication()).exitAccount();
                startActivity(new Intent(getActivity(),
                        LoginActivity.class));
                break;
            case R.id.fasong_btn:
                if(fasong_btn.getText().toString().equals("发送")){
                    if(SJQApp.user.isBindMobile){
                       doSubmit();
                    }else{
                        toastShow("绑定手机号后才能发评论");
                    }
                }else{
                    if (item_pingluen.getVisibility() == View.VISIBLE) {
                        FromID="0";
                        Fromguid ="";
                        replayUserName="";
                        ownerGUID="";
                        position = -1;
                        pingluen_text.setHint("");
                        pingluen_text.setText("");
                        item_pingluen.setVisibility(View.GONE);
                        imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法
                    }
                }
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
        if(SJQApp.user != null){
            loadata();
        }
    }
    private void loadata() {
        String url = AppUrl.GETCRICLE+"userGuid="+SJQApp.user.getGuid()+"&pageSize=10"+"&currentPage="+PageIndex+"&IsRefresh=true";
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
                PostCricleBean bean=null;
                try {
                    json = new JSONObject(responseInfo.result);
                    String result1=json.getString("result");
                    Gson gson = new Gson();
                    bean=gson.fromJson(result1, PostCricleBean.class);
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
                        mData.clear();
                        mData.addAll(bean.listData);
                        mAdapter.setData(mData);
                    }else{
                        mData.addAll(bean.listData);
                        mAdapter.addData(bean.listData);
                    }
                }
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);

    }
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            String str = pingluen_text.getText().toString();
            if(str.equals("")){
                fasong_btn.setText("取消");
            }else{
                fasong_btn.setText("发送");
            }

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }
    };
    private void doSubmit() {
        String Contents=pingluen_text.getText().toString();
        if(Contents.trim().length() < 1){
            toastShow("输入内容不能为空");
            return;
        }
        String url = AppUrl.COMMENT_SEND;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type","application/json");
        try {
            JSONObject param  = new JSONObject();
            param.put("contents",Contents);
            param.put("fromGUID", Fromguid);
            param.put("markName", "8");
            param.put("fromID", FromID);
            param.put("userGUID", SJQApp.user.guid);
            param.put("anonymous", false);
            if (SJQApp.userData != null) {
                param.put("userName",SJQApp.userData.name);
                param.put("userLogo", SJQApp.userData.logo);
            } else if (SJQApp.ownerData != null) {
                param.put("userName",SJQApp.ownerData.getName());
                param.put("userLogo", SJQApp.ownerData.getLogo());
            }
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        }catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onStart() {
                super.onStart();
                // dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if (mAdapter == null)
                    return;
                Gson gson = new Gson();
                SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
                if (bean != null) {
                    if (bean.result.operatResult) {
                        addComment();
                    }else{
                        toastShow(bean.result.operatMessage);
                    }
                }
            }
            @Override
            public void onFailure(HttpException error, String arg1) {
                toastShow("网络请求发生错误");

            }
        };

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params, callBack);
    }
    private void addComment() {
        if (pingluen_text != null) {
            postcommentList postcommentList= new postcommentList();
            if (SJQApp.userData != null) {
                postcommentList.setOwnerName(SJQApp.userData.getName());
            } else if (SJQApp.ownerData != null) {
                postcommentList.setOwnerName(SJQApp.ownerData.getName());
            } else{
                postcommentList.setOwnerName(SJQApp.user.nickName);
            }
            postcommentList.setOwnerGUID(SJQApp.user.getGuid());
            postcommentList.setReplayUserName(replayUserName);
            postcommentList.setCommentContent(pingluen_text.getText().toString());
            List<postcommentList> postList= mData.get(position).getComment();
            postList.add(postcommentList);
            mAdapter.setCommtlist(postList,position);
            pingluen_text.setHint("");
            pingluen_text.setText("");
            FromID="0";
            Fromguid="";
            replayUserName="";
            ownerGUID="";
            position = -1;
            item_pingluen.setVisibility(View.GONE);
            imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法
        }
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
    int position = -1;
    private String FromID="0";
    private String Fromguid="";
    private String replayUserName="";
    private String ownerGUID="";
    private class MyBroadcastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("criclePin")) {
                position = intent.getIntExtra("position",-1);
                if(position == -1){
                    toastShow("数据出错");
                    return;
                }
                CricleBean cricleBean= mData.get(position);
                Fromguid = cricleBean.getGuid();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
                item_pingluen.setVisibility(View.VISIBLE);
                pingluen_text.setFocusable(true);
                pingluen_text.setFocusableInTouchMode(true);
                pingluen_text.requestFocus();
                pingluen_text.setHint("");
                pingluen_text.setText("");
            }
            if (action.equals("criclehui")) {
                int plpost =intent.getIntExtra("plpost",-1);
                position = intent.getIntExtra("position",-1);
                if(position == -1 || plpost == -1){
                    toastShow("数据出错");
                    return;
                }
                CricleBean cricleBean= mData.get(position);
                ownerGUID=cricleBean.getComment().get(plpost).getOwnerGUID();
                if(ownerGUID.equals(SJQApp.user.getGuid())){
                    toastShow("自己不能回复自己");
                    return;
                }
                Fromguid = cricleBean.getGuid();
                FromID =cricleBean.getComment().get(plpost).getCommnentID()+"";
                replayUserName =cricleBean.getComment().get(plpost).getOwnerName();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
                item_pingluen.setVisibility(View.VISIBLE);
                pingluen_text.setFocusable(true);
                pingluen_text.setFocusableInTouchMode(true);
                pingluen_text.requestFocus();
                pingluen_text.setText("");
                pingluen_text.setHint("回复"+replayUserName+"：");
            }
            if(action.equals("setRefresh")){
                mCaseListView.setOnRefreshing();
            }
            if(action.equals("denglu")){
                String name = intent.getStringExtra("name");
            }

        }
    }
    public void showPenyou(){
        if(SJQApp.user != null){
            item_wei.setVisibility(View.GONE);
            mCaseListView.setOnRefreshing();
        }else{
            item_wei.setVisibility(View.VISIBLE);
            mData.clear();
            mAdapter.setData(mData);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mybroad!=null){
            getActivity().unregisterReceiver(mybroad);
        }
    }
}
