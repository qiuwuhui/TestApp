package com.baolinetworktechnology.shejiquan.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.MainPageAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.fragment.CharFragment;
import com.baolinetworktechnology.shejiquan.fragment.WeiShopFragment;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
/**
 * 设计师详情页
 *
 * @author JiSheng.Guo
 *
 */

public class WeiActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener,RadioGroup.OnCheckedChangeListener {
    private boolean mIsPrepared;
    private ViewPager mViewPage;
    private RadioGroup mRadioGroup;
    private WeiShopFragment weiShopFragment;
    private CharFragment charfragment;
    private View view_layout;
    private View view_layout1;
    private TextView tv_chent;
    private MyBroadcastReciver addbroad;
    private RelativeLayout yingdao;
    private String appTad="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("addSuccess");
        appTad= getIntent().getStringExtra(AppTag.TAG_ID);
        addbroad=new MyBroadcastReciver();
        registerReceiver(addbroad, intentFilter);
        mIsPrepared = true;
        initView();
    }
    private void initView() {
        yingdao = (RelativeLayout)findViewById(R.id.yingdao);
        yingdao.setOnClickListener(this);
        boolean isFirst = CacheUtils.getBooleanData(WeiActivity.this, "Weishop", true);
        if(isFirst){
            yingdao.setVisibility(View.VISIBLE);
        }
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        tv_chent= (TextView) findViewById(R.id.tv_chent);
        tv_chent.setText("设计师详情");
        mRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        mRadioGroup.setOnCheckedChangeListener(this);
        view_layout = findViewById(R.id.Meitu_layout);
        view_layout1 = findViewById(R.id.Opus_layout);
        view_layout1.setVisibility(View.GONE);
        mViewPage = (ViewPager) findViewById(R.id.viewPager);
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        weiShopFragment = new WeiShopFragment();
        charfragment =new CharFragment();
        fragments.add(weiShopFragment);
        fragments.add(charfragment);
        mViewPage.setAdapter(new MainPageAdapter(getSupportFragmentManager(),
                fragments));
        mViewPage.setOnPageChangeListener(this);
        mViewPage.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(SJQApp.user==null){
                    return true;
                }else{
                    if(weiShopFragment.Certification!=3){
                        Toast.makeText(WeiActivity.this,"该设计师尚未开通此服务",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }

            }
        });
        if(SJQApp.user!=null){
            tv_chent.setVisibility(View.GONE);
            mRadioGroup.setVisibility(View.VISIBLE);
            view_layout.setVisibility(View.VISIBLE);
        }else{
            tv_chent.setVisibility(View.VISIBLE);
            mRadioGroup.setVisibility(View.GONE);
            view_layout.setVisibility(View.GONE);
        }
        if(appTad.equals("1")){
            setMessamg();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.share:
                MobclickAgent.onEvent(WeiActivity.this," kControlDesignerHomePageShare");// 设计师个人主页分享点击次数
                weiShopFragment.doShare();
                break;
            case R.id.yingdao:
                yingdao.setVisibility(View.GONE);
                CacheUtils.cacheBooleanData(WeiActivity.this, "Weishop",
                        false);
                break;
        }
    }

    // 设计师、聊天
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (isPageSelected) {
            isPageSelected = false;
            return;
        }
        if(!appTad.equals("1")){
            if(weiShopFragment.Certification!=3){
                Toast.makeText(this,"该设计师尚未开通此服务",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        switch (checkedId) {
            case R.id.rbMeitu:
                if (mViewPage.getCurrentItem() != 0) {
                    mViewPage.setCurrentItem(0);
                    isPageSelected = false;
                    view_layout.setVisibility(View.VISIBLE);
                    view_layout1.setVisibility(View.GONE);
                    findViewById(R.id.share).setVisibility(View.VISIBLE);
                }
                break;

            case R.id.rbOpus:
                MobclickAgent.onEvent(WeiActivity.this,"kControlDesignerHomePageConslution","liaotian");//设计师主页聊天界面调用次数
                if (mViewPage.getCurrentItem() != 1) {
                    mViewPage.setCurrentItem(1);
                    isPageSelected = false;
                    view_layout.setVisibility(View.GONE);
                    view_layout1.setVisibility(View.VISIBLE);
                    findViewById(R.id.share).setVisibility(View.GONE);
                }
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    boolean isPageSelected = false;// 是否选择

    @Override
    public void onPageSelected(int arg0) {
        isPageSelected = true;
        switch (arg0) {
            case 0:
                mRadioGroup.check(R.id.rbMeitu);
                view_layout.setVisibility(View.VISIBLE);
                view_layout1.setVisibility(View.GONE);
                break;

            default:
                MobclickAgent.onEvent(WeiActivity.this,"kControlDesignerHomePageConslution","huadong");//设计师主页聊天界面调用次数
                mRadioGroup.check(R.id.rbOpus);
                view_layout.setVisibility(View.GONE);
                view_layout1.setVisibility(View.VISIBLE);
                break;
        }
    }
    public void setMessamg(){
        MobclickAgent.onEvent(WeiActivity.this,"kControlDesignerHomePageConslution","liaotian");//设计师主页聊天界面调用次数
        if (mViewPage.getCurrentItem() != 1) {
            mViewPage.setCurrentItem(1);
            isPageSelected = false;
            view_layout.setVisibility(View.GONE);
            view_layout1.setVisibility(View.VISIBLE);
            findViewById(R.id.share).setVisibility(View.GONE);
        }
    }
    private class MyBroadcastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("addSuccess")) {
                if(SJQApp.user==null){
                    tv_chent.setVisibility(View.VISIBLE);
                    mRadioGroup.setVisibility(View.GONE);
                    view_layout.setVisibility(View.GONE);
                }else{
                    tv_chent.setVisibility(View.GONE);
                    mRadioGroup.setVisibility(View.VISIBLE);
                    view_layout.setVisibility(View.VISIBLE);
                    charfragment.GetDesigner();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
           weiShopFragment.addDesign();
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WeiActivity");
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WeiActivity");
    }
}
