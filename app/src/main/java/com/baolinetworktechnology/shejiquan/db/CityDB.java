package com.baolinetworktechnology.shejiquan.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;

/**
 * 数据库操作类
 * 
 * @author Administrator
 * 
 */

public class CityDB {
	public static final String CITY_DB_NAME = "sjqcity.db";// 数据库名称
	private static final String CITY_TABLE_NAME = "cities";// 表名
	private SQLiteDatabase mDB;
	private Context mContext;
	private int parentID;
	public CityDB(Context context, String path) {
		if (context != null)
			mContext=context;
			mDB = context
					.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
	}
	/**
	 * 获取全部区
	 * 
	 * @return
	 */
	public List<City> getAllCityQU() {
		List<City> list = new ArrayList<City>();
		list.clear();
		String CityTitle=CacheUtils.getStringData(mContext, 
				"getAddress", "");
		if(CityTitle.equals("")){
			 City city1 = SJQApp.getCity();
			 if(city1 !=null && !TextUtils.isEmpty(city1.Title)){			
				 String Title=city1.Title;
					 Cursor c1= mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
							 + " where Title= ?", new String[] {city1.Title});
					 while (c1.moveToNext()) {
						 parentID = c1.getInt(c1.getColumnIndex("ID"));
					 }	
					City city=new City(parentID,0, 0, "", "", Title);
					SJQApp.setCity(city);
//				CacheUtils.cacheIntData(mContext, "cityID",parentID);
					 Cursor c = mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
							 + " where ParentID= ?"+"and MarkStatus!= ?", new String[] { parentID+"","-1"});
					 while (c.moveToNext()) {
						 int ID = c.getInt(c.getColumnIndex("ID"));
						 int parentID = c.getInt(c.getColumnIndex("ParentID"));
						 int parentLevels = c.getInt(c.getColumnIndex("ParentLevels"));
						 String initial = c.getString(c.getColumnIndex("Initial"));
						 String letter = c.getString(c.getColumnIndex("Letter"));
						 String title = c.getString(c.getColumnIndex("Title"));
						 
						 City item = new City(ID, parentID, parentLevels, initial, letter,
								 title);
						 list.add(item);
					}			
			 }
		}else{
			if(!TextUtils.isEmpty(CityTitle)){
				Cursor c1= mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
						+ " where Title= ?", new String[] { CityTitle,});
				while (c1.moveToNext()) {
					parentID = c1.getInt(c1.getColumnIndex("ID"));
				}	
//				CacheUtils.cacheIntData(mContext, "cityID",parentID);
				Cursor c = mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
						+ " where ParentID= ?"+"and MarkStatus!= ?", new String[] { parentID+"","-1"});
				while (c.moveToNext()) {
					int ID = c.getInt(c.getColumnIndex("ID"));
					int parentID = c.getInt(c.getColumnIndex("ParentID"));
					int parentLevels = c.getInt(c.getColumnIndex("ParentLevels"));
					String initial = c.getString(c.getColumnIndex("Initial"));
					String letter = c.getString(c.getColumnIndex("Letter"));
					String title = c.getString(c.getColumnIndex("Title"));
					
					City item = new City(ID, parentID, parentLevels, initial, letter,
							title);
					list.add(item);
				}			
			}
		}
		City city=new City(parentID, 0, 0, "", "", "全城");
		 list.add(0, city);
		return list;		
	}
	/**
	 * 获取服务区域
	 * 
	 * @return
	 */
	public List<City> getfuwuCity(int CityID) {
		List<City> list = new ArrayList<City>();
			Cursor c = mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
					+ " where ParentID= ?"+"and MarkStatus!= ?", new String[] { CityID+"","-1"});
			while (c.moveToNext()) {
				int ID = c.getInt(c.getColumnIndex("ID"));
				int parentID = c.getInt(c.getColumnIndex("ParentID"));
				int parentLevels = c.getInt(c.getColumnIndex("ParentLevels"));
				String initial = c.getString(c.getColumnIndex("Initial"));
				String letter = c.getString(c.getColumnIndex("Letter"));
				String title = c.getString(c.getColumnIndex("Title"));				
				City item = new City(ID, parentID, parentLevels, initial, letter,
						title);
				if (!"县".equals(item.Title))// news 新增过滤 县
					list.add(item);		
		}
		return list;
	}
	/**
	 * 获取全部城市
	 * 
	 * @return
	 */
	public List<City> getAllCity() {
		List<City> list = new ArrayList<City>();
		Cursor c = mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
				+ " where ParentLevels= ?", new String[] { "3"});
		while (c.moveToNext()) {
			int ID = c.getInt(c.getColumnIndex("ID"));
			int parentID = c.getInt(c.getColumnIndex("ParentID"));
			int parentLevels = c.getInt(c.getColumnIndex("ParentLevels"));
			String initial = c.getString(c.getColumnIndex("Initial"));
			String letter = c.getString(c.getColumnIndex("Letter"));
			String title = c.getString(c.getColumnIndex("Title"));

			City item = new City(ID, parentID, parentLevels, initial, letter,
					title);
			if (!"县".equals(item.Title))// news 新增过滤 县
				list.add(item);
		}
		// City item = getCity("厦门市");
		// item.Initial = "热门";
		// list.add(item);
		//
		// City item2 = getCityInfo("北京市");
		// item2.Initial = "热门";
		// list.add(item2);
		//
		// City item3 = getCityInfo("上海市");
		// item3.Initial = "热门";
		// list.add(item3);
		//
		// City item4 = getCity("深圳市");
		// item4.Initial = "热门";
		// list.add(item4);
		//
		// City item5 = getCity("南京市");
		// item5.Initial = "热门";
		// list.add(item5);

		return list;
	}
	/**
	 * 装修公司获取省
	 * 
	 * @param ParentID
	 * @return
	 */
	public String fromCity(int ProvinceID,int CityID) {
		String shen="";
		String shi="";
		Cursor c1= mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
				 + " where ID= ?", new String[] {ProvinceID+""});
		 while (c1.moveToNext()) {
			 shen= c1.getString(c1.getColumnIndex("Title"));
		 }
		 Cursor c2= mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
				 + " where ID= ?", new String[] {CityID+""});
		 while (c2.moveToNext()) {
			 shi= c2.getString(c2.getColumnIndex("Title"));
		 }		
		return shen+"-"+shi;
	}
	/**
	 * 装修公司获取省
	 *
	 * @param ParentID
	 * @return
	 */
	public String fromCityID(int CityID) {
		String shi="";
		Cursor c2= mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
				+ " where ID= ?", new String[] {CityID+""});
		while (c2.moveToNext()) {
			shi= c2.getString(c2.getColumnIndex("Title"));
		}
		return shi;
	}
	/**
	 * 获取推荐地区城市ID
	 * 
	 * @param ParentID
	 * @return
	 */
	public int getTJCityID(String Title) {
		int shen=0;
		Cursor c1= mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
				 + " where Title= ?", new String[] {Title});
		 while (c1.moveToNext()) {
			 shen= c1.getInt(c1.getColumnIndex("ID"));
		 }
		return shen;	
	}
	/**
	 * 获取地区
	 * 
	 * @param ParentID
	 * @return
	 */
	
	public List<City> getArea(int ParentID) {
		List<City> list = null;
		Cursor c = null;
		try {
			list = new ArrayList<City>();
			c = mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
					+ " where ParentID=?", new String[] { "" + ParentID });
			while (c.moveToNext()) {
				int ID = c.getInt(c.getColumnIndex("ID"));
				int parentID = c.getInt(c.getColumnIndex("ParentID"));
				int parentLevels = c.getInt(c.getColumnIndex("ParentLevels"));
				String initial = c.getString(c.getColumnIndex("Initial"));
				String letter = c.getString(c.getColumnIndex("Letter"));
				String title = c.getString(c.getColumnIndex("Title"));

				City item = new City(ID, parentID, parentLevels, initial,
						letter, title);
				if (!"县".equals(item.Title))// news 新增过滤 县
					list.add(item);
			}
			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return list;
	}

	/**
	 * 获取城市
	 * 
	 * @param city
	 * @return
	 */
	public City getCity(String city) {
		if (TextUtils.isEmpty(city))
			return null;
		City item = getCityInfo(parseName(city));
		if (item == null) {
			item = getCityInfo(city);
		}
		return item;
	}

	/**
	 * 去掉市或县搜索
	 * 
	 * @param city
	 * @return
	 */
	private String parseName(String city) {
		if (city.contains("市")) {// 如果为空就去掉市字再试试
			String subStr[] = city.split("市");
			city = subStr[0];
		} else if (city.contains("县")) {// 或者去掉县字再试试
			String subStr[] = city.split("县");
			city = subStr[0];
		}
		return city;
	}

	/**
	 * 获取城市信息
	 * 
	 * @param city
	 *            城市名称
	 * @return
	 */
	public City getCityID(String city) {
		Cursor c = mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
				+ " where ID=?", new String[] { city });
		if (c.moveToFirst()) {
			int ID = c.getInt(c.getColumnIndex("ID"));
			int parentID = c.getInt(c.getColumnIndex("ParentID"));
			int parentLevels = c.getInt(c.getColumnIndex("ParentLevels"));
			String initial = c.getString(c.getColumnIndex("Initial"));
			String letter = c.getString(c.getColumnIndex("Letter"));
			String title = c.getString(c.getColumnIndex("Title"));

			City item = new City(ID, parentID, parentLevels, initial, letter,
					title);
			return item;
		}
		return null;
	}

	private City getCityInfo(String cityID) {
		Cursor c = mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
				+ " where Title=?", new String[] { cityID });
		if (c.moveToFirst()) {
			int ID = c.getInt(c.getColumnIndex("ID"));
			int parentID = c.getInt(c.getColumnIndex("ParentID"));
			int parentLevels = c.getInt(c.getColumnIndex("ParentLevels"));
			String initial = c.getString(c.getColumnIndex("Initial"));
			String letter = c.getString(c.getColumnIndex("Letter"));
			String title = c.getString(c.getColumnIndex("Title"));

			City item = new City(ID, parentID, parentLevels, initial, letter,
					title);
			return item;
		}
		return null;
	}

	// // 获取所有省份
	public List<City> getAllProvince() {
		List<City> list = null;
		Cursor c = null;
		try {
			list = new ArrayList<City>();
			c = mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
					+ " where ParentID=?", new String[] { "" + 1 });
			while (c.moveToNext()) {
				int ID = c.getInt(c.getColumnIndex("ID"));
				int parentID = c.getInt(c.getColumnIndex("ParentID"));
				int parentLevels = c.getInt(c.getColumnIndex("ParentLevels"));
				String initial = c.getString(c.getColumnIndex("Initial"));
				String letter = c.getString(c.getColumnIndex("Letter"));
				String title = c.getString(c.getColumnIndex("Title"));
				City item = new City(ID, parentID, parentLevels, initial,
						letter, title);
				list.add(item);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return list;
	}

	public List<City> getResultCityList(String keyword) {
		List<City> list = null;
		Cursor c = null;
		try {

			list = new ArrayList<City>();
			c = mDB.rawQuery("SELECT * from " + CITY_TABLE_NAME
					+ " where ParentLevels = 3 and" + "( Title like \"%"
					+ keyword + "%\" or Letter like \"%" + keyword
					+ "%\" or Value like \"%" + keyword + "%\")", null);
			while (c.moveToNext()) {
				int ID = c.getInt(c.getColumnIndex("ID"));
				int parentID = c.getInt(c.getColumnIndex("ParentID"));
				int parentLevels = c.getInt(c.getColumnIndex("ParentLevels"));
				String initial = c.getString(c.getColumnIndex("Initial"));
				String letter = c.getString(c.getColumnIndex("Letter"));
				String title = c.getString(c.getColumnIndex("Title"));
				City item = new City(ID, parentID, parentLevels, initial,
						letter, title);
				if (!"县".equals(item.Title))// news 新增过滤 县
					list.add(item);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return list;

	}
}
