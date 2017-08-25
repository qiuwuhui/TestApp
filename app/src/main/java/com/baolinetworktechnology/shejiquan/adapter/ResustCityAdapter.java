package com.baolinetworktechnology.shejiquan.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.City;

/**
 * 城市列表适配器
 * 
 * @author Administrator
 * 
 */
public class ResustCityAdapter extends BaseAdapter {
	// 首字母集
	private List<City> mCities; // 城市集合

	public void setData(List<City> cities) {
		if (mCities != null) {
			mCities.clear();
		}
		mCities = cities;
	}

	// 条目数
	@Override
	public int getCount() {
		if (mCities == null)
			return 0;
		return mCities.size();
	}

	@Override
	public City getItem(int position) {
		// TODO Auto-generated method stub
		if (mCities == null)
			return null;
		return mCities.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// View的样式
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder=null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.select_city_item, null);
			holder=new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder=(Holder) convertView.getTag();
		}
		City item = mCities.get(position);
		holder.setData(item);
		return convertView;
	}

	class Holder {
		TextView city;

		public Holder(View convertView) {
			TextView group = (TextView) convertView
					.findViewById(R.id.group_title);
			group.setVisibility(View.GONE);
			city = (TextView) convertView.findViewById(R.id.column_title);
		}

		public void setData(City item) {

			if (item != null) {
				city.setText(item.Title);
			}
		}
	}

}
