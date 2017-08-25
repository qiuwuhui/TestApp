package com.baolinetworktechnology.shejiquan.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.PostDetailsActivity;
import com.baolinetworktechnology.shejiquan.adapter.MainPageAdapter;

/**
 * 首页-发现
 * 
 * @author JiSheng.Guo
 * 
 */
public class MainCaseFragment extends Fragment implements
		ViewPager.OnPageChangeListener,View.OnClickListener,OnCheckedChangeListener {
	private ViewPager mViewPage;
	private CricleFragment circleFragment;//朋友圈
	private PeriodFragment periodFragment;//社区
	private RadioGroup mRadioGroup;
	private View cirle_lay;
	private View perio_lay;
	private MyBroadcastReciver mybroad;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_main_case, container,
				false);
		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("CommentSuCCESS");//社区列表评论成功
//		intentFilter.addAction("CollectionOn");//社区列表收藏成功
//		intentFilter.addAction("CollectionOff");//社区列表收藏取消
		intentFilter.addAction("FeelFabulous");
		intentFilter.addAction("DETAILSUPDATA");//帖子详情页返回
		mybroad=new MyBroadcastReciver();
		getActivity().registerReceiver(mybroad, intentFilter);
		initView(view);
		return view;
	}

	private void initView(View view) {
		view.findViewById(R.id.casego).setOnClickListener(this);
		mViewPage = (ViewPager) view.findViewById(R.id.viewPager);
		mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
		mRadioGroup.setOnCheckedChangeListener(this);
		cirle_lay = view.findViewById(R.id.cirle_lay);
		perio_lay = view.findViewById(R.id.perio_lay);
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		circleFragment=new CricleFragment();
		periodFragment = new PeriodFragment();
		fragments.add(circleFragment);
		fragments.add(periodFragment);
		mViewPage.setAdapter(new MainPageAdapter(getChildFragmentManager(),
				fragments));
		mViewPage.setOnPageChangeListener(this);
		mViewPage.setCurrentItem(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.casego:
			setTabClick();
			break;
			
		  default:
			  break;			
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}


	@Override
	public void onPageSelected(int arg0) {
		int id = 0;
		switch (arg0) {
			case 0:
				id=R.id.rbCircle;
				cirle_lay.setVisibility(View.VISIBLE);
				perio_lay.setVisibility(View.INVISIBLE);
				break;
			case 1:
				id=R.id.rbPeriod;
				cirle_lay.setVisibility(View.INVISIBLE);
				perio_lay.setVisibility(View.VISIBLE);
				break;
		}
		mRadioGroup.check(id);
	}
	public void setTabClick() {
		if (mViewPage != null && mViewPage.getCurrentItem() == 0) {
			circleFragment.setTabClick();
		} else {
			periodFragment.setTabClick();
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		switch (checkedId) {
			case R.id.rbCircle:
				if (mViewPage.getCurrentItem() != 0) {
					mViewPage.setCurrentItem(0);
					cirle_lay.setVisibility(View.VISIBLE);
					perio_lay.setVisibility(View.INVISIBLE);

				}
				break;
			case R.id.rbPeriod:
				if (mViewPage.getCurrentItem() != 1) {
					mViewPage.setCurrentItem(1);
					cirle_lay.setVisibility(View.INVISIBLE);
					perio_lay.setVisibility(View.VISIBLE);
				}
				break;
		}
	}
	public void showPenyou(){
		circleFragment.showPenyou();
	}
	public void refreshSQ(){
		periodFragment.refresh();
	}

	private class MyBroadcastReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();//
			if (action.equals("CommentSuCCESS")) {//帖子详情 评论成功
				int position = intent.getIntExtra("POISTION",-1);
				int commentNum = intent.getIntExtra("NUMBER",0);
				periodFragment.updateitem(position,commentNum);
			}else if (action.equals("CollectionOn")){//帖子详情 收藏成功
				int position = intent.getIntExtra("POISTION",-1);
				int CollectionNum = intent.getIntExtra("NUMBER",0);
				periodFragment.updeitemCollection(position,CollectionNum,true);
			}else if (action.equals("CollectionOff")){//帖子详情 取消收藏
				int position = intent.getIntExtra("POISTION",-1);
				int CollectionNum = intent.getIntExtra("NUMBER",0);
				periodFragment.updeitemCollection(position,CollectionNum,false);
			}else if (action.equals("FeelFabulous")){//帖子详情 点赞
				int position = intent.getIntExtra("POISTION",-1);
				int FeelFabulousNumber = intent.getIntExtra("NUMBER",0);
//				periodFragment.updeitemFobulows(position,FeelFabulousNumber);
			}else if (action.equals("DETAILSUPDATA")){//帖子详情返回
				int position = intent.getIntExtra(PostDetailsActivity.POSITION,-1);
				String string = intent.getStringExtra(PostDetailsActivity.SWCASESTRING);
				periodFragment.updeitemFobulows(position,string);
			}
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
