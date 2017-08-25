package com.baolinetworktechnology.shejiquan.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {

	public List<View> mListView;

	public ViewPagerAdapter(List<View> list) {
		super();

		this.mListView = list;
	}

	@Override
	public int getCount() {
		if (mListView == null)
			return 0;
		return mListView.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mListView.get(position));
		return mListView.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
