package com.baolinetworktechnology.shejiquan.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.db.CityDB;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.view.ScrollerNumberPicker.OnSelectListener;

/**
 * 城市Picker+县区
 * 
 * @author JiSheng.Guo
 * 
 */
public class CityPicker2 extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	private ScrollerNumberPicker countyPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	// private int tempProvinceIndex = -1;
	// private int temCityIndex = -1;
	private List<City> province_list = new ArrayList<City>();
	private ArrayList<String> province_list_name = new ArrayList<String>();

	City countyCity, provinceCity, city;
	private CityDB cityService;

	public CityPicker2(Context context, AttributeSet attrs) {
		super(context, attrs);
		cityService = CityService.getInstance(context).getCityDB();
		getaddressinfo();

	}

	public CityPicker2(Context context) {
		super(context);
		getaddressinfo();
	}

	// 获取城市信息
	private void getaddressinfo() {
		// 读取城市信息string
		province_list = cityService.getAllProvince();
		province_list_name = getNameList(province_list);

	}

	// 获取城市名称ArrayList
	private ArrayList<String> getNameList(List<City> province_list2) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < province_list2.size(); i++) {
			
			arrayList.add(province_list2.get(i).Title);
			if (mDefault == 0) {
				if (SJQApp.ownerData != null) {
					if (province_list2.get(i).CityID == SJQApp.ownerData
							.getProvinceID()) {
						mDefault = i;
					}
				}
			}
		}
		return arrayList;
	}

	int mDefault = 0;

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

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.city_picker2, this);
		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
		countyPicker = (ScrollerNumberPicker) findViewById(R.id.county);
		// 省份
		provincePicker.setData(province_list_name);
		if (mDefault < 0 || mDefault >= province_list_name.size()) {
			mDefault = 0;
		}
		provincePicker.setDefault(mDefault);
		provinceCity = province_list.get(mDefault);
		// 城市
		List<City> list = cityService.getArea(provinceCity.CityID);
		ArrayList<String> citys = getNameList(list);
		cityPicker.setData(citys);
		cityPicker.setDefault(0);
		city = list.get(0);

		// 县区
		List<City> list2 = cityService.getArea(city.CityID);
		countyPicker.setData(getNameList(list2));

		countyPicker.setDefault(0);
		if (list2.size() > 0)
			countyCity = list2.get(0);

		provincePicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null)
					return;
				mDefault = id;
				String selectDay = cityPicker.getSelectedText();
				if (selectDay == null || selectDay.equals(""))
					return;
				// 城市数组
				provinceCity = province_list.get(id);
				List<City> list = cityService.getArea(provinceCity.CityID);
				ArrayList<String> citys = getNameList(list);
				cityPicker.setData(citys);
				cityPicker.setDefault(0);
				city = list.get(0);

				// 县区
				List<City> list2 = cityService.getArea(city.CityID);
				ArrayList<String> countyList = getNameList(list2);
				countyPicker.setData(countyList);
				if (countyList.size() > 0) {
					countyPicker.setDefault(0);
				}

				if (list2.size() > 0) {
					countyCity = list2.get(0);
				}
				Message message = Message.obtain();
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

				String selectDay = provincePicker.getSelectedText();
				if (selectDay == null || selectDay.equals(""))
					return;

				List<City> list = cityService.getArea(provinceCity.CityID);
				if (id < list.size())
					city = list.get(id);

				// 县区
				List<City> list2 = cityService.getArea(city.CityID);
				if (list2.size() == 0) {
					ArrayList<String> countyList = new ArrayList<String>();
					countyList.add("");
					if (countyCity != null)
						countyCity = new City(countyCity.CityID,
								countyCity.CityID, countyCity.ParentLevels, "",
								"", "");
					countyPicker.setData(countyList);
				} else {
					ArrayList<String> countyList = getNameList(list2);
					countyPicker.setData(countyList);
					countyPicker.setDefault(0);
					countyCity = list2.get(0);

				}
				Message message = Message.obtain();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {

			}
		});

		countyPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {

				if (text.equals("") || text == null)
					return;
				// 县区
				List<City> list2 = cityService.getArea(city.CityID);
				if (list2.size() > id) {
					countyCity = list2.get(id);

					Message message = Message.obtain();
					message.what = REFRESH_VIEW;
					handler.sendMessage(message);
				}
			}

			@Override
			public void selecting(int id, String text) {

			}
		});
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

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public int getProvincesCode() {
		return provinceCity.CityID;
	}

	public int getCityCode() {
		return city.CityID;
	}

	public int getCountyCode() {
		if (countyCity == null)
			return 0;
		return countyCity.CityID;
	}

	public String getCity_string() {
		String city_string = provincePicker.getSelectedText() + " "
				+ cityPicker.getSelectedText() + " "
				+ countyPicker.getSelectedText();
		return city_string;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}

	public void setShowSub(boolean b) {
		provincePicker.setShowSub(b);
		cityPicker.setShowSub(b);
		countyPicker.setShowSub(b);
		provincePicker.setData(province_list_name);
		if (mDefault < 0 || mDefault >= province_list_name.size()) {
			mDefault = 0;
		}
		provincePicker.setDefault(mDefault);
	}

}
