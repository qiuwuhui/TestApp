package com.baolinetworktechnology.shejiquan.adapter;

import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.City;

/**
 * 订单大厅-城市适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class OlderNavAdapter extends BaseAdapter {
	
	public OlderNavAdapter(){
	}

	List<City> data = null;// A B C D E F G H I J K L M N O P Q R S T U V W X Y
							// Z
	public static final String PROVINCES[] = { "全国", "A 安徽", "A 澳门", "B 北京",
			"C 重庆", "F 福建", "G 广西", "G 广东", "G 甘肃", "G 贵州", "H 河北", "H 湖北",
			"H 河南", "H 海南", "H 湖南", "H 黑龙江", "J 江苏", "J 吉林", "J 江西", "L 辽宁",
			"N 宁夏", "N 内蒙古", "Q 青海", "S 山西", "S 陕西", "S 上海", "S 山东", "S 四川",
			"T 天津", "T 台湾", "X 西藏", "X 新疆", "X 香港", "Y 云南", "Z 浙江" };
	int PreCityID = 0;
	public static final int PROVINCES_ID[] = { -1, 340000, 820000, 110000,
			500000, 350000, 450000, 440000, 620000, 520000, 130000, 420000,
			410000, 460000, 430000, 230000, 320000, 220000, 360000, 210000,
			640000, 150000, 630000, 140000, 610000, 310000, 370000, 510000,
			120000, 710000, 540000, 650000, 810000, 530000, 330000 };

	// 北京 110000 ，天jin 120000 ，河北130000，山西140000，
	// 内蒙15，辽宁21，吉林22，黑龙江23 ，上海31，江苏32，浙江33，安徽34
	// 福建35，江西36，山东37，河南41，湖北42，湖南43.广东44
	// 广西45.海南46，重庆50，四川51，贵州52，云南53.西藏54，陕西61.甘肃62，青海63.宁夏64， 新疆65
	// 台湾71.香港81 澳门82
	public void setData(List<City> data) {
		this.data = data;
	}


	@Override
	public int getCount() {
		if (data == null) {
			return PROVINCES.length;
		} else {
			return data.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (data == null)
			return PROVINCES[position];
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		if (position < 0)
			position = 0;
		if (data == null)
			return PROVINCES_ID[position];
		return data.get(position).CityID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_older_nav_city, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
			if (data == null) {
				holder.setBig(true);
				holder.setData(PROVINCES[position]);
			} else {
				holder.setBig(false);
				holder.setData(data.get(position).Title);
			}
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tvTitle.setTag(position);
		if (data == null) {// 省份
			holder.setData(PROVINCES[position]);
			if (preId == position) {
				holder.tvTitle.setBackgroundColor(0xFFFBFBFB);
			} else {
				holder.tvTitle.setBackgroundColor(0x00000000);
			}
		} else {// 城市
			City item = data.get(position);
			holder.setData(item.Title);

			if (item.CityID == PreCityID) {
				holder.tvTitle.setTextColor(Color.RED);
			} else {
				holder.tvTitle.setTextColor(Color.GRAY);
			}

		}

		return convertView;
	}

	int preId = -1;

	class Holder implements OnClickListener {
		TextView tvTitle;

		public Holder(View v) {
			tvTitle = (TextView) v.findViewById(R.id.tvTitle);
			tvTitle.setOnClickListener(this);
		}

		public void setBig(boolean b) {
			if (b) {
				tvTitle.setHeight((int) tvTitle.getContext().getResources()
						.getDimension(R.dimen.text_city_big_h));
			} else {
				tvTitle.setHeight((int) tvTitle.getContext().getResources()
						.getDimension(R.dimen.text_city_small_h));
			}

		}

		public void setData(String nav) {
			tvTitle.setText(nav);

		}

		@Override
		public void onClick(View v) {
			if (onItemClickListener != null) {
				int position = (Integer) v.getTag();
				if (data == null) {
					if (preId == position) {
						position = -1;
					}
				}
				preId = position;
				notifyDataSetChanged();
				PreCityID = (int) getItemId(position);
				onItemClickListener.onItemClick(null, v, position, PreCityID);
			}

		}
	}

	OnItemClickListener onItemClickListener;

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;

	}

}
