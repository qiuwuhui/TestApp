package com.guojisheng.koyview.utls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

import com.google.gson.Gson;

public class CommonUtils {

	/**
	 * 安装apk 激活系统里面已经存在的一个组件
	 * 
	 * @param context
	 * @param file
	 * @param mimetype
	 */
	public static void installApk(Context context, File file) {
		/*
		 * <intent-filter> <action android:name="android.intent.action.VIEW" />
		 * <category android:name="android.intent.category.DEFAULT" /> <data
		 * android:scheme="content" /> <data android:scheme="file" /> <data
		 * android:mimeType="application/vnd.android.package-archive" />
		 * </intent-filter>
		 */
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 添加标记位
		context.startActivity(intent);
	}

	// 判断sdcard是否可用
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断手机是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetWorkInfo(Context context) {
		// 获取连接管理器
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 需要access_net_state权限
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {// 2g
																				// 3g
			return true;
		} else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {// wifi
			return true;
		}
		return false;
	}

	// 使用泛型获取javaBean
	public static <T> T getDomain(String jsonString, Class<T> cls) {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	// 使用泛型获取javaBean
//	public static <T> T getDomain(ResponseInfo<String> responseInfo,
//			Class<T> cls) {
//		if(responseInfo==null){
//			return null;
//		}
//		T t = null;
//		try {
//			Gson gson = new Gson();
//			String jsonString = responseInfo.result;
//			t = gson.fromJson(jsonString, cls);
//		} catch (Exception e) {
//			LogUtils.i("gson","Json解析错误");
//		}
//		return t;
//	}
//
//	// 根据key获取字段
//	public static String getString(ResponseInfo<String> responseInfo, String key) {
//		String t = "";
//		try {
//			JSONObject js = new JSONObject(responseInfo.result);
//			t = js.getString(key);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return t;
//	}

	// 根据key获取字段
	public static String getString(String result, String key) {
		String t = "";
		try {
			JSONObject js = new JSONObject(result);
			t = js.getString(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	/**
	 * 判断是不是手机号
	 * 
	 * @param s
	 * @return
	 */
	public static boolean checkNumber(String s) {
		return s.matches("1[3|5|7|8|][0-9]{9}");
	}

	/**
	 * 判断是不是密码
	 * 
	 * @param s
	 * @return
	 */
	public static boolean checkPassword(String s) {
		return s.matches("^[a-zA-Z0-9]{6,14}$");
	}

	public static void saveMyBitmap(Bitmap bit,String bitName) throws IOException {
		File f = new File("/sdcard/BZW/" + bitName + ".png");
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
