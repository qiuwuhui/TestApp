package com.baolinetworktechnology.shejiquan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.baolinetworktechnology.shejiquan.domain.Dailylise;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面切换fragment适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class MyPageAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Fragment> mFragmentList;

	public MyPageAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.mFragmentList = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragmentList.get(arg0);
	}
	@Override
	public int getItemPosition(Object object) {
// TODO Auto-generated method stub
		return PagerAdapter.POSITION_NONE;
	}
	@Override
	public int getCount() {
		return mFragmentList.size();
	}
	public void setData(ArrayList<Fragment> fragments){
		this.mFragmentList = fragments;
		notifyDataSetChanged();
	}
}
