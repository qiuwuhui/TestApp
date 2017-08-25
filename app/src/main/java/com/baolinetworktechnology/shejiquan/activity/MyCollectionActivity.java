package com.baolinetworktechnology.shejiquan.activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.MainPageAdapter;
import com.baolinetworktechnology.shejiquan.fragment.CollectCaseFragment;
import com.baolinetworktechnology.shejiquan.fragment.CollectNewsFragment;
import com.baolinetworktechnology.shejiquan.fragment.CollectPostFragment;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;

import java.util.ArrayList;

/**
 * 我的收藏
 */
public class MyCollectionActivity extends BaseFragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {
    private String TAG = "MyCollection";
    private RadioButton mRB1;
    private RadioButton mRB2;
    private RadioButton mRB3;
    private TextView mTvDelete;
    private View tuei_view;
    private View tuei_view1;
    private View tuei_view2;
    private ViewPager mViewPage;
    private CollectCaseFragment mCollectCaseFragment;
    private CollectNewsFragment mCollectNewsFragment;
    private CollectPostFragment mCollectPostFragment;
    private MyBroadcastReciver mybroad;
    private boolean mIsDeleteMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        mViewPage = (ViewPager) findViewById(R.id.viewPager);
        mIsDeleteMode = false;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Caseshow");
        intentFilter.addAction("Newshow");
        intentFilter.addAction("Postshow");
        mybroad=new MyBroadcastReciver();
        registerReceiver(mybroad, intentFilter);
        initview();
    }

    private void initview() {
        findViewById(R.id.back).setOnClickListener(this);
        mRB1 = (RadioButton) findViewById(R.id.rbCass);
        mRB2 = (RadioButton) findViewById(R.id.rbNew);
        mRB3 = (RadioButton) findViewById(R.id.rbTie);
        mTvDelete = (TextView) findViewById(R.id.tv_delete);
        mTvDelete.setOnClickListener(this);
        mRB1.setOnClickListener(this);
        mRB2.setOnClickListener(this);
        mRB3.setOnClickListener(this);
        tuei_view  =findViewById(R.id.tuei_view);
        tuei_view1 =findViewById(R.id.tuei_view1);
        tuei_view2 =findViewById(R.id.tuei_view2);
        tuei_view1.setVisibility(View.INVISIBLE);
        tuei_view2.setVisibility(View.INVISIBLE);

         mCollectCaseFragment = new CollectCaseFragment();//案例收藏
		 mCollectNewsFragment = new CollectNewsFragment();//资讯收藏
         mCollectPostFragment = new CollectPostFragment();//帖子收藏

        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(mCollectCaseFragment);
        fragments.add(mCollectNewsFragment);
        fragments.add(mCollectPostFragment);
        mViewPage.setAdapter(new MainPageAdapter(getSupportFragmentManager(),
                fragments));
        mViewPage.setOnPageChangeListener(this);
        mViewPage.setOffscreenPageLimit(3);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int page) {
        switch (page) {
            case 0:
                mRB1.setChecked(true);
                mViewPage.setCurrentItem(0);
                mIsDeleteMode = false;
                tuei_view.setVisibility(View.VISIBLE);
                tuei_view1.setVisibility(View.INVISIBLE);
                tuei_view2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                mRB2.setChecked(true);
                mIsDeleteMode = false;
                mViewPage.setCurrentItem(1);
                tuei_view.setVisibility(View.INVISIBLE);
                tuei_view1.setVisibility(View.VISIBLE);
                tuei_view2.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mRB3.setChecked(true);
                mIsDeleteMode = false;
                mViewPage.setCurrentItem(2);
                tuei_view.setVisibility(View.INVISIBLE);
                tuei_view1.setVisibility(View.INVISIBLE);
                tuei_view2.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rbCass:
                mViewPage.setCurrentItem(0);
                tuei_view.setVisibility(View.VISIBLE);
                tuei_view1.setVisibility(View.INVISIBLE);
                tuei_view2.setVisibility(View.INVISIBLE);
                mIsDeleteMode = false;
                break;
            case R.id.rbNew:
                mViewPage.setCurrentItem(1);
                tuei_view.setVisibility(View.INVISIBLE);
                tuei_view1.setVisibility(View.VISIBLE);
                tuei_view2.setVisibility(View.INVISIBLE);
                mIsDeleteMode = false;
                break;
            case R.id.rbTie:
                mViewPage.setCurrentItem(2);
                tuei_view.setVisibility(View.INVISIBLE);
                tuei_view1.setVisibility(View.INVISIBLE);
                tuei_view2.setVisibility(View.VISIBLE);
                mIsDeleteMode = false;
                break;
            case R.id.tv_delete:
                if (mIsDeleteMode){
                    mIsDeleteMode = false;
                }else {
                    mIsDeleteMode = true;
                }
                if (mViewPage.getCurrentItem() == 0){
                }else if (mViewPage.getCurrentItem() == 1){
                }else if (mViewPage.getCurrentItem() == 2){
                    mCollectPostFragment.DeleteMode(mIsDeleteMode);
                }
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mybroad);
    }
    private class MyBroadcastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("Caseshow")){
                mCollectCaseFragment.shuaxin();
            }
            if(action.equals("Newshow")){
                mCollectNewsFragment.shuaxin();
            }
            if(action.equals("Postshow")){
                mCollectPostFragment.shuaxin();
            }
        }
    }
}
