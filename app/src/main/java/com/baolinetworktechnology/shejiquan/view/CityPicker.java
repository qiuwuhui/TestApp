package com.baolinetworktechnology.shejiquan.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.db.CityDB;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.view.ScrollerNumberPicker.OnSelectListener;

/**
 * 城市Picker
 * 
 * @author JiSheng.Guo
 * 
 */
public class CityPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempProvinceIndex = -1;
	private int temCityIndex = -1;
	private List<City> province_list = new ArrayList<City>();
	private ArrayList<String> province_list_name = new ArrayList<String>();
	private HashMap<Integer, List<City>> city_map = new HashMap<Integer, List<City>>();
	private int city_code;
	private String city_string;
	private CityDB cityService;
	private int mDefault;
	private static ArrayList<Integer> province_list_code = new ArrayList<Integer>();

	public CityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		cityService = CityService.getInstance(context).getCityDB();
		getaddressinfo();

	}

	public CityPicker(Context context) {
		super(context);
		// this.context = context;
		getaddressinfo();
	}

	/**
	 * 设置默认显示第几个
	 * 
	 * @param defau
	 */
	public void setDefault(int defau) {
		mDefault = defau;
		if (mDefault < 0 || mDefault >= province_list_name.size()) {
			mDefault = 0;
		}
		if (provincePicker != null)
			provincePicker.setDefault(mDefault, true);
	}

	/**
	 * 获取当前默认显示第几个
	 * 
	 * @return
	 */
	public int getDefault() {
		return mDefault;
	}

	// 获取城市信息
	private void getaddressinfo() {
		// 读取城市信息string
		if (cityService != null) {
			province_list = cityService.getAllProvince();
			province_list_name = getNameList(province_list);
			province_list_code = getProvinceCodeList(province_list);
			city_map = getCityMap(province_list);
		}
	}

	// 获取城市名称ArrayList
	private ArrayList<String> getNameList(List<City> province_list2) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < province_list2.size(); i++) {
			arrayList.add(province_list2.get(i).Title);
		}
		return arrayList;
	}

	// 获取城市编码ArrayList
	private ArrayList<Integer> getProvinceCodeList(List<City> province_list2) {
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		for (int i = 0; i < province_list2.size(); i++) {
			arrayList.add(province_list2.get(i).CityID);
		}
		return arrayList;
	}

	// 获取城市HashMap
	private HashMap<Integer, List<City>> getCityMap(List<City> province_list2) {
		HashMap<Integer, List<City>> hashMap = new HashMap<Integer, List<City>>();
		if (province_list2 != null) {
			for (int i = 0; i < province_list2.size(); i++) {
				int cityID = province_list2.get(i).CityID;
				List<City> area = cityService.getArea(cityID);
				if (area.size() != 0) { // 台湾跟澳门添加自己
					hashMap.put(cityID, area);
				} else {
					area = new ArrayList<City>();
					area.add(province_list2.get(i));
					hashMap.put(cityID, area);
				}

			}
		}
		return hashMap;
	}

	int select = 0;

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);

		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
		provincePicker.setData(province_list_name);
		provincePicker.setDefault(0);
		ArrayList<String> city = getCity(city_map, province_list_code.get(0));
		cityPicker.setData(city);
		cityPicker.setDefault(0);
		city_code = getCityCodeList(city_map, province_list_code.get(0)).get(0);
		provincePicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null)
					return;
				if (tempProvinceIndex != id) {
					String selectDay = cityPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					// 城市数组
					cityPicker.setData(getCity(city_map,
							province_list_code.get(id)));

					// provices_code = getCityCodeList(city_map,
					// province_list_code.get(id)).get(0);

					cityPicker.setDefault(0);
					city_code = getCityCodeList(city_map,
							province_list_code.get(id)).get(0);
					select = id;
					mDefault = id;
					// int lastDay =
					// Integer.valueOf(provincePicker.getListSize());
					// if (id > lastDay) {
					// provincePicker.setDefault(lastDay - 1);
					// }
				}
				tempProvinceIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
			}
		});
		cityPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null)
					return;
				if (temCityIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					int lastDay = Integer.valueOf(cityPicker.getListSize());
					if (id > lastDay) {
						cityPicker.setDefault(lastDay - 1);
					}

					city_code = getCityCodeList(city_map,
							province_list_code.get(select)).get(id);
				}
				temCityIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {

			}
		});
	}

	private ArrayList<String> getCity(HashMap<Integer, List<City>> city_map2,
			Integer integer) {
		List<City> list = city_map2.get(integer);
		return getNameList(list);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	// private int provices_code;
	private ArrayList<Integer> getCityCodeList(
			HashMap<Integer, List<City>> city_map2, Integer integer) {
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		List<City> list = city_map2.get(integer);
		for (int i = 0; i < list.size(); i++) {
			arrayList.add(list.get(i).CityID);

		}
		return arrayList;
	}

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public int getCity_code_string() {
		return city_code;
	}

	public int getProvinces_code() {
		City cit = cityService.getCity(cityPicker.getSelectedText());
		if (cit != null) {
			return cit.ParentID;
		}
		return 0;
	}

	public String getCity_string() {
		city_string = provincePicker.getSelectedText() + "-"
				+ cityPicker.getSelectedText();// +
												// counyPicker.getSelectedText()
		return city_string;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}
