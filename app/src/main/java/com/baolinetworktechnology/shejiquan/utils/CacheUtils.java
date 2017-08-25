package com.baolinetworktechnology.shejiquan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CacheUtils {

	public static final String CACHE_FILE_NAME = "SJQ315";

	/**
	 * 
	 * 使用SharedPreferences存储boolean类型数据.
	 * 
	 * @param context
	 * @param key
	 *            要存储的数据的key
	 * @param value
	 *            要存储的数据
	 */
	public static void cacheBooleanData(Context context, String key,
			boolean value) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				CACHE_FILE_NAME, Context.MODE_PRIVATE);
		Editor edit = mSharedPreferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	/**
	 * 取出缓存的boolean类型数据
	 * 
	 * @param context
	 * @param key
	 *            要取出的数据的key
	 * @param defValue
	 *            缺省值
	 * @return
	 */
	public static boolean getBooleanData(Context context, String key,
			boolean defValue) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				CACHE_FILE_NAME, Context.MODE_PRIVATE);
		return mSharedPreferences.getBoolean(key, defValue);
	}

	/**
	 * 缓存字符串数据
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void cacheStringData(Context context, String key, String value) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				CACHE_FILE_NAME, Context.MODE_PRIVATE);

		mSharedPreferences.edit().putString(key, value).commit();
	}

	public static void cacheStringData(Context context, String TAB_NAME,
			String key, String value) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				TAB_NAME, Context.MODE_PRIVATE);

		mSharedPreferences.edit().putString(key, value).commit();
	}

	public static String getStringData(Context context, String TAB_NAME,
			String key, String defValue) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				TAB_NAME, Context.MODE_PRIVATE);
		return mSharedPreferences.getString(key, defValue);
	}

	public static void cacheIntData(Context context, String key, int value) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				CACHE_FILE_NAME, Context.MODE_PRIVATE);

		mSharedPreferences.edit().putInt(key, value).commit();
	}

	public static void cacheLongData(Context context, String key, long value) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				CACHE_FILE_NAME, Context.MODE_PRIVATE);
		mSharedPreferences.edit().putLong(key, value).commit();
	}

	public static long getLongData(Context context, String key) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				CACHE_FILE_NAME, Context.MODE_PRIVATE);
		return mSharedPreferences.getLong(key, 0);
	}

	/**
	 * 根据key获取缓存的数据
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getStringData(Context context, String key,
			String defValue) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				CACHE_FILE_NAME, Context.MODE_PRIVATE);
		return mSharedPreferences.getString(key, defValue);
	}

	public static int getIntData(Context context, String key, int defValue) {
		if (context == null)
			return -1;
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				CACHE_FILE_NAME, Context.MODE_PRIVATE);
		return mSharedPreferences.getInt(key, defValue);
	}

	public static void clear(Context context, String tabName) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				tabName, Context.MODE_PRIVATE);
		Editor edit = mSharedPreferences.edit();
		edit.clear();
		edit.commit();

	}
}
