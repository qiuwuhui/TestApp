package com.baolinetworktechnology.shejiquan.adapter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.view.PinnedHeaderListView;
import com.baolinetworktechnology.shejiquan.view.PinnedHeaderListView.PinnedHeaderAdapter;

/**
 * 城市列表适配器
 * 
 * @author Administrator
 * 
 */
public class CityAdapter extends BaseAdapter implements SectionIndexer,
		PinnedHeaderAdapter, OnScrollListener {
	// 首字母集
	private List<City> mCities; // 城市集合
	private Map<String, List<City>> mMap; // 根据首字母存放城市集合
	private List<String> mSections; // 首字母集
	private List<Integer> mPositions; // 字母所在集合位置
	private LayoutInflater mInflater;

	public CityAdapter(Context context, List<City> cities,
			Map<String, List<City>> map, List<String> sections,
			List<Integer> positions) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		mCities = cities;
		mMap = map;
		mSections = sections;
		mPositions = positions;

	}

	// 条目数
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCities.size();
	}

	@Override
	public City getItem(int position) {
		// TODO Auto-generated method stub
		int section = getSectionForPosition(position);
		return mMap.get(mSections.get(section)).get(
				position - getPositionForSection(section));
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// View的样式
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int section = getSectionForPosition(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.select_city_item, null);
		}
		TextView group = (TextView) convertView.findViewById(R.id.group_title);
		TextView city = (TextView) convertView.findViewById(R.id.column_title);
		if (getPositionForSection(section) == position) {
			group.setVisibility(View.VISIBLE);
			group.setText(mSections.get(section));

		} else {
			group.setVisibility(View.GONE);
		}
		City item = mMap.get(mSections.get(section)).get(
				position - getPositionForSection(section));
		city.setText(item.Title);
		return convertView;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	// 滑动监听
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}

	}

	// 首条状态
	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0 || position >= getCount()) {
			return PINNED_HEADER_GONE;
		}
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);// 下个字母所在的条目位置
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {// 下个字母的上一条等于当前头的位置
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	// 设置首字母
	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		// TODO Auto-generated method stub
		int realPosition = position;
		int section = getSectionForPosition(realPosition);
		String title = (String) getSections()[section];
		((TextView) header.findViewById(R.id.group_title)).setText(title);
	}

	// 获取首字母字符数组
	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return mSections.toArray();
	}

	// 获取字母分类所在位置
	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		if (section < 0 || section >= mPositions.size()) {
			return -1;
		}
		return mPositions.get(section);
	}

	// 根据集合位置获取首字母集的位置
	@Override
	public int getSectionForPosition(int position) {
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(mPositions.toArray(), position); // 查找插入点
		return index >= 0 ? index : -index - 2;
	}
}
