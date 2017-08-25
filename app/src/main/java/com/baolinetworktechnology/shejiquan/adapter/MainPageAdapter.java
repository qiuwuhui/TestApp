package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * 主界面切换fragment适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class MainPageAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> mFragmentList;

	public MainPageAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.mFragmentList = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}
}
