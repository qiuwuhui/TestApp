package com.baolinetworktechnology.shejiquan.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.SortAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.DataBean;
import com.baolinetworktechnology.shejiquan.domain.DatalistBean;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CharacterParser;
import com.baolinetworktechnology.shejiquan.utils.PinyinSelect;
import com.baolinetworktechnology.shejiquan.view.IconCenterEditText;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectDsCityActivity extends BaseActivity {
    private ListView sortListView;
    private List<DataBean> contactList;
    private SortAdapter adapter;
    private PinyinSelect pinyinComparator;
    private IconCenterEditText mClearEditText;
    private CharacterParser characterParser;
    private TextView dingwei_cg;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ds_city);
        initView();
        getCity();
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
        dingwei_cg=(TextView) findViewById(R.id.dingwei_cg);
        TextView dingwei_sb=(TextView) findViewById(R.id.dingwei_sb);
        dingwei_cg.setOnClickListener(this);
        title = CacheUtils.getStringData(SelectDsCityActivity.this, "dingweics", "");
        if (!title.equals("")) {
            dingwei_cg.setText(title);
            dingwei_sb.setVisibility(View.GONE);
        }else{
            dingwei_cg.setText("未开启定位服务");
            dingwei_sb.setVisibility(View.VISIBLE);
        }
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinSelect();
        sortListView = (ListView) findViewById(R.id.sortlist);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DataBean databean = (DataBean) adapter.getItem(position);
                City city=new City(databean.getId(), 0, 0, "", "", databean.getTitle());
                SJQApp.setCity(city);
                Intent data = new Intent();
                data.putExtra(AppTag.TAG_TITLE, databean.getTitle());
                data.putExtra(AppTag.TAG_ID, databean.getId());
                setResult(AppTag.RESULT_OK, data);
                finish();
            }
        });
        mClearEditText = (IconCenterEditText) findViewById(R.id.filter_edit);
        mClearEditText.setHintTextColor(getResources().getColor(R.color.shouqi));
        mClearEditText.setOnSearchClickListener(new IconCenterEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {

            }
        });
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
    }
    private void getCity() {
        String url = AppUrl.GETRECOMMENDCITY;
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String message) {
                toastShow(message);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject json;
                DatalistBean bean=null;
//                try {
//                    json = new JSONObject(responseInfo.result);
//                    String result1=json.getString("result");
                    Gson gson = new Gson();
                    bean=gson.fromJson(responseInfo.result, DatalistBean.class);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                if(bean!=null && bean.result!=null){
                    showData(bean);
                }
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url,
                callBack);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.dingwei_cg:
                if(dingwei_cg.getText().toString().equals("未开启定位服务")){
                    Toast.makeText(this,"当前定位失败...",Toast.LENGTH_SHORT).show();
                    return;
                }
                CacheUtils.cacheStringData(SelectDsCityActivity.this, "getAddress",title);
                CityService mApplication=CityService.getInstance(SelectDsCityActivity.this);
                int dingweiID=mApplication.getCityDB().getTJCityID(title);
                mApplication.getCityDB().getAllCityQU();
                Intent data1 = new Intent();
                data1.putExtra(AppTag.TAG_TITLE,title);
                data1.putExtra(AppTag.TAG_ID, dingweiID);
                City city=new City(dingweiID, 0, 0, "", "", title);
                SJQApp.setCity(city);
                setResult(AppTag.RESULT_OK, data1);
                finish();
                break;
        }
    }
    private void showData(DatalistBean bean){
        contactList =bean.result;
        Collections.sort(contactList, pinyinComparator);
        adapter = new SortAdapter(this, contactList);
        sortListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void filterData(String filterStr) {
        List<DataBean> filterDateList = new ArrayList<DataBean>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = contactList;
            // 根据a-z进行排序
            Collections.sort(filterDateList, pinyinComparator);
            adapter.updateListView(filterDateList);
        } else {
            filterDateList.clear();
            for (DataBean databean : contactList) {
                String name = databean.getTitle();

                    if (name.indexOf(filterStr.toString()) != -1
                            || characterParser.getSelling(name).startsWith(
                            filterStr.toString())) {
                        filterDateList.add(databean);
                    }

            }
            // 根据a-z进行排序
            Collections.sort(filterDateList, pinyinComparator);
            adapter.updateListView(filterDateList);
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
}
