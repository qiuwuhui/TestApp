package com.baolinetworktechnology.shejiquan.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.AnewModel;
import com.baolinetworktechnology.shejiquan.domain.AnewfrienList;
import com.baolinetworktechnology.shejiquan.domain.GongSiMyanliBean;
import com.baolinetworktechnology.shejiquan.domain.SearchFriendList;
import com.baolinetworktechnology.shejiquan.itemRecycler.SearchFriendAdapter;
import com.baolinetworktechnology.shejiquan.itemRecycler.SwipeAdapter;
import com.baolinetworktechnology.shejiquan.utils.CharacterParser;
import com.baolinetworktechnology.shejiquan.view.IconCenterEditText;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendsActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private SearchFriendAdapter mAdapter;
    private IconCenterEditText mClearEditText;
    private TextView tishi_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        mClearEditText = (IconCenterEditText) findViewById(R.id.filter_edit);
        tishi_tv = (TextView) findViewById(R.id.tishi_tv);
        mClearEditText.setHintTextColor(getResources().getColor(R.color.shouqi));
        recyclerView =(RecyclerView) findViewById(R.id.MyRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.back).setOnClickListener(this);
        mAdapter = new SearchFriendAdapter(this);
        recyclerView.setAdapter(mAdapter);
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mClearEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    hideInput();
                    if(mClearEditText.getText().toString().trim().equals("")){
                        toastShow("请输入要搜索用户名");
                        return false;
                    }
                    loadata(mClearEditText.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
    }
    /**
     * 关闭输入法弹窗
     */
    public void hideInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (this.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

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
    private void loadata(String name) {
        String url= AppUrl.SEARCHEASE+name+"&userGUID="+SJQApp.user.getGuid();
        RequestCallBack<String> callBack = new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String message) {
                toastShow(message);
            }
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject json;
                SearchFriendList bean=null;
                try {
                    json = new JSONObject(responseInfo.result);
                    String result1=json.getString("result");
                    Gson gson = new Gson();
                    bean=gson.fromJson(result1, SearchFriendList.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                   if(bean !=null && bean.listData!=null && bean.listData.size() !=0){
                       shuaxinData(bean);
                    }else{
                       tishi_tv.setVisibility(View.VISIBLE);
                   }
                }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);
    }
    private List<AnewModel> mList =new ArrayList<>();
    private void shuaxinData(SearchFriendList bean) {
        mList=bean.listData;
        mAdapter.setList(mList);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void filterData(String filterStr) {
        tishi_tv.setVisibility(View.GONE);
    }
    protected void onDestroy() {
        super.onDestroy();
        if(recyclerView != null){
            recyclerView.setAdapter(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
}
