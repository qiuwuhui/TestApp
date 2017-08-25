package com.baolinetworktechnology.shejiquan.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.AnewModel;
import com.baolinetworktechnology.shejiquan.domain.AnewfrienList;
import com.baolinetworktechnology.shejiquan.itemRecycler.SwipeAdapter;
import com.baolinetworktechnology.shejiquan.utils.CharacterParser;
import com.baolinetworktechnology.shejiquan.view.IconCenterEditText;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public class AnewfriendActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private SwipeAdapter mAdapter;
    private IconCenterEditText mClearEditText;
    private CharacterParser characterParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anewfriend);
        mClearEditText = (IconCenterEditText) findViewById(R.id.filter_edit);
        mClearEditText.setHintTextColor(getResources().getColor(R.color.shouqi));
        characterParser = CharacterParser.getInstance();
        recyclerView =(RecyclerView) findViewById(R.id.MyRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SwipeAdapter(this);
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
        loadata();
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
    private void loadata() {
        String url= AppUrl.ADVISEEASEFRI+ SJQApp.user.getGuid();
        RequestCallBack<String> callBack = new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String message) {
                toastShow(message);
            }
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                AnewfrienList bean=gson.fromJson(responseInfo.result, AnewfrienList.class);
                   if(bean !=null && bean.result!=null){
                       shuaxinData(bean);
                    }
                }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);
    }
    private List<AnewModel> mList =new ArrayList<>();
    private void shuaxinData(AnewfrienList bean) {
        mList=bean.result;
        mAdapter.setList(mList);
        mAdapter.notifyDataSetChanged();
        List<AnewModel> anewModelList =new ArrayList<>();
        for(AnewModel anewModel:mList){
            if(anewModel.getIsHint() == 1){
                anewModelList.add(anewModel);
            }
        }
        if(anewModelList.size()!=0){
            putTongXun(anewModelList);
        }
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
        List<AnewModel> filterDateList = new ArrayList<AnewModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mList;
        } else {
            filterDateList.clear();
            for (AnewModel anewModel : mList) {
                String name = anewModel.getName();
                if(!TextUtils.isEmpty(name)) {
                    if (name.indexOf(filterStr.toString()) != -1
                            || characterParser.getSelling(name).startsWith(
                            filterStr.toString())) {
                        filterDateList.add(anewModel);
                    }
                }
            }
        }
        mAdapter.setList(filterDateList);
        mAdapter.notifyDataSetChanged();
    }
    // 进行设置身份
    private void putTongXun(List<AnewModel> anewModels) {
        String url = AppUrl.EDITHINFRIEND;
        RequestParams params = getParams(url);
        params.addBodyParameter("enumHint","2");
        for (int c = 0; c < anewModels.size(); c++){
            params.addBodyParameter("guidList[]",anewModels.get(c).getEaseGuid());
        }
        RequestCallBack<String> callBack = new RequestCallBack<String>() {
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(ResponseInfo<String> n) {

            }
            @Override
            public void onFailure(HttpException error, String msg) {
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.PUT, AppUrl.API + url, params, callBack);

    }
    protected void onDestroy() {
        super.onDestroy();
        if(recyclerView != null){
            recyclerView.setAdapter(null);
        }
    }
}
