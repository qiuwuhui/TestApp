package com.baolinetworktechnology.shejiquan.manage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.baolinetworktechnology.shejiquan.db.CityDB;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;

/**
 * 城市管理
 * 
 * @author JiSheng.Guo
 * 
 */
public class CityService {
	public static final String CITY_DB_NAME = "sjqcity.db";
	private static CityService mCityService;
	private static Context mContext;
	// 城市列表
	private List<City> mCityList;
	//城市区域列表
//	private List<City> mCityListqu;
	// 首字母集
	private List<String> mSections;
	// 根据首字母存放数据
	private Map<String, List<City>> mMap;

	// 首字母位置集
	private List<Integer> mPositions;
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;
	private CityDB mCityDB;
	private boolean mIsCityListComplite = false;// 是否加载完成
	private String Citytitle;
	// private static SQLiteDatabase db;

	private CityService() {
		new MyAsk().execute();
	}

	public static CityService getInstance(Context context1) {
		mContext = context1.getApplicationContext();
		if (mCityService == null) {
			mCityService = new CityService();
		}
		return mCityService;
	}

	class MyAsk extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			initCityList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mIsCityListComplite = true;
			super.onPostExecute(result);
		}

	}

	// 初始化 城市数据
	private void initCityList() {
		mCityList = new ArrayList<City>();
		mSections = new ArrayList<String>();
		mMap = new HashMap<String, List<City>>();
		mPositions = new ArrayList<Integer>();
		mIndexer = new HashMap<String, Integer>();
		mCityDB = openCityDB();// 这个必须最先复制完,所以我放在单线程中处理
		prepareCityList();
	}

	private static final String TAG = "CityService";

	private CityDB openCityDB() {
		if (mContext == null) {
			mCityService = null;
			return null;
		}
		LogUtils.i(TAG, "--------------------");
		String path = "/data"
				+ Environment.getDataDirectory().getAbsolutePath()
				+ File.separator + "com.baolinetworktechnology.shejiquan"
				+ File.separator + CityDB.CITY_DB_NAME;
//		String path =mContext.getClass().getClassLoader().getResourceAsStream("assets/"+CityDB.CITY_DB_NAME);
		File db = new File(path);
		if (!db.exists()) {
			// L.i("db is not exists");
			try {
				InputStream is = mContext.getAssets().open(CITY_DB_NAME);
				FileOutputStream fos = new FileOutputStream(db);
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
				}
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				// System.exit(0);
			}
		}
		return new CityDB(mContext, path);
	}

	private final String FORMAT = "^[a-z,A-Z].*$";

	// 按字母排序
	private boolean prepareCityList() {
		if (mCityDB == null) {
			return false;
		}
//		mCityListqu = mCityDB.getAllCityQU();//获取全部区域
		mCityList = mCityDB.getAllCity();// 获取数据库中所有城市
		for (City city : mCityList) {
			String firstName = city.Initial;// 第一个字拼音的第一个字母
			if (firstName.matches(FORMAT)) {
				if (mSections.contains(firstName)) {
					mMap.get(firstName).add(city);
				} else {
					mSections.add(firstName);
					List<City> list = new ArrayList<City>();
					list.add(city);
					mMap.put(firstName, list);
				}
			} else {
				if (mSections.contains("#")) {
					mMap.get("#").add(city);
				} else {
					mSections.add("#");
					List<City> list = new ArrayList<City>();
					list.add(city);
					mMap.put("#", list);
				}
			}
		}
		Collections.sort(mSections);// 按照字母重新排序
		// String temp = mSections.get(mSections.size() - 1);
		// mSections.remove(mSections.size() - 1);
		// mSections.add(0, temp);
		int position = 0;
		for (int i = 0; i < mSections.size(); i++) {
			mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
			mPositions.add(position);// 首字母在listview中位置，存入list中
			position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
		}
		return true;
	}

	/**
	 * 获取地区
	 * 
	 * @param ParentID
	 * @return
	 */

	public List<City> getArea(int ParentID) {
		if (mCityDB != null)
			return mCityDB.getArea(ParentID);
		return null;
	}
	//获取装修公司所在地区省市
	public String fromCity(int ProvinceID,int CityID) {
		if (mCityDB != null)
			return mCityDB.fromCity(ProvinceID,CityID);
		return null;
	}
	//获取设计师所在市
	public String fromCityID(int CityID) {
		if (mCityDB != null)
			return mCityDB.fromCityID(CityID);
		return null;
	}
	public List<City> getResultCityList(String keyword) {
		if (mCityDB != null)
			return mCityDB.getResultCityList(keyword);
		return null;
	}

	public List<City> getAllProvince() {
		if (mCityDB == null)
			return null;
		return mCityDB.getAllProvince();
	}

	public CityDB getCityDB() {
		// TODO Auto-generated method stub
		return mCityDB;
	}

	public boolean isCityListComplite() {
		return mIsCityListComplite;
	}

	public List<City> getCityList() {

		return mCityList;
	}
	
	public List<City> getCityListqu() {
		return mCityDB.getAllCityQU();
	}
	//服务区域
	public List<City> getfuwuCity(int CityID) {

		return mCityDB.getfuwuCity(CityID);
	}
	
	public List<String> getSections() {
		// TODO Auto-generated method stub
		return mSections;
	}

	public Map<String, List<City>> getMap() {
		// TODO Auto-generated method stub
		return mMap;
	}

	public List<Integer> getPositions() {
		// TODO Auto-generated method stub
		return mPositions;
	}

	public Map<String, Integer> getIndexer() {
		// TODO Auto-generated method stub
		return mIndexer;
	}
	public void SteCitytitle(String CityTitle) {
		Citytitle=CityTitle;		
	}
	public int getTJCityID(String Title) {
		return mCityDB.getTJCityID(Title);		
	}
}
