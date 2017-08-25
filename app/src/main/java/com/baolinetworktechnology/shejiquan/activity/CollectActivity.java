package com.baolinetworktechnology.shejiquan.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.MainPageAdapter;
import com.baolinetworktechnology.shejiquan.fragment.CollectCaseFragment;
import com.baolinetworktechnology.shejiquan.fragment.CollectNewsFragment;
import com.baolinetworktechnology.shejiquan.view.HomeViewPager;

/**
 * 首页-我的- 收藏
 * 
 * @author JiSheng.Guo
 * 
 */
public class CollectActivity extends FragmentActivity implements
		OnCheckedChangeListener, OnPageChangeListener, OnClickListener {
	private RadioGroup mRradioGroup;// 案例和资讯
	private HomeViewPager mViewPage;
	private CollectCaseFragment mCollectCaseFragment;
	private CollectNewsFragment mCollectNewsFragment;
	private TextView mTvTitle;// 右边标题
	private RadioButton mRBCass, mRBNew;
	///private ExplosionField mExplosionField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collecy);
		initView();

		ArrayList<Fragment> fragments = new ArrayList<Fragment>();

		mCollectCaseFragment = new CollectCaseFragment();
		mCollectNewsFragment = new CollectNewsFragment();

		fragments.add(mCollectCaseFragment);
		fragments.add(mCollectNewsFragment);

		mViewPage.setAdapter(new MainPageAdapter(getSupportFragmentManager(),
				fragments));
		//mExplosionField = ExplosionField.attach2Window(this);
		
	}

	// 初始化View
	private void initView() {
		mRradioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		mRradioGroup.setOnCheckedChangeListener(this);
		mViewPage = (HomeViewPager) findViewById(R.id.mainViewPage);
		mViewPage.setOnPageChangeListener(this);
		mRBCass = (RadioButton) findViewById(R.id.rbCass);
		mRBNew = (RadioButton) findViewById(R.id.rbNew);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		// mTvTitle.setOnClickListener(this);
		mTvTitle.setVisibility(View.GONE);
		findViewById(R.id.back).setOnClickListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
	
		switch (checkedId) {
		case R.id.rbCass:// 案例收藏
			mViewPage.setCurrentItem(0);
			break;
		case R.id.rbNew:// 文章收藏
			mViewPage.setCurrentItem(1);
			break;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		if (arg0 == 0) {
			mRBCass.setChecked(true);

		} else {
			mRBNew.setChecked(true);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

//		case R.id.tv_title:
//			if (mTvTitle.getText().toString().equals("编辑")) {
//				mTvTitle.setText("完成");
//
//				mCollectCaseFragment.setChangData(true, mExplosionField);// 设置是否编辑
//				mCollectNewsFragment.setChangData(true, mExplosionField);
//			} else {
//				mTvTitle.setText("编辑");
//				mCollectCaseFragment.setChangData(false);
//				mCollectNewsFragment.setChangData(false);
//				mExplosionField.clear();
//			}
//			mTvTitle.setAnimation(AnimationUtils.loadAnimation(this,
//					R.anim.umeng_socialize_fade_in));
//			break;
		}

	}
}
