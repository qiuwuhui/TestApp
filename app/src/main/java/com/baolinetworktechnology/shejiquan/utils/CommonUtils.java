package com.baolinetworktechnology.shejiquan.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Hashtable;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lidroid.xutils.http.ResponseInfo;

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
		if (TextUtils.isEmpty(jsonString)) {
			return t;
		}
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			Log.e("", "解析错误");
		}
		return t;
	}

	// 使用泛型获取javaBean
	public static <T> T getDomain(ResponseInfo<String> responseInfo,
			Class<T> cls) {
		if (responseInfo == null) {
			return null;
		}
		return getDomain(responseInfo.result, cls);
	}
	// 使用泛型获取javaBean
	public static <T> T getDomain1(String responseInfo,
								  Class<T> cls) {
		if (responseInfo == null) {
			return null;
		}
		return getDomain(responseInfo, cls);
	}
	// 根据key获取字段
	public static String getString(ResponseInfo<String> responseInfo, String key) {
		String t = "";
		try {
			JSONObject js = new JSONObject(responseInfo.result);
			t = js.getString(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	// 根据key获取字段
	public static String getString(String result, String key) {
		String t = "";
		if (TextUtils.isEmpty(result)) {
			return t;
		}
		try {
			JSONObject js = new JSONObject(result);
			t = js.getString(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	// 根据key获取字段
	public static double getDouble(String result, String key) {
		double t = 0;
		if (TextUtils.isEmpty(result)) {
			return t;
		}
		try {

			JSONObject js = new JSONObject(result);
			t = js.getDouble(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	/**
	 * 判断是不是手机号
	 * 
	 * @param s
	 * @return`
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
		return s.matches("^[a-zA-Z0-9_]{6,18}$");
	}

	public static File saveMyBitmap(Bitmap bit, String bitName) {
		File f = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File path = new File(Environment.getExternalStorageDirectory()
					+ "/SheJiQuan/Image");
			if (!path.exists()) {
				path.mkdirs();
			}
			f = new File(path + "/" + bitName + ".png");

			FileOutputStream fOut = null;
			try {
				f.createNewFile();
				fOut = new FileOutputStream(f);
				if (bit != null)
					bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				fOut.flush();
				fOut.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return f;
	}

	public static String gps2m(double lat_a, double lng_a, double lat_b,
			double lng_b) {
		final double EARTH_RADIUS = 6378137.0;
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) /10000;
		if(s>100){
			s=s/1000;			
		}else{
			s=0.1;
		}
		DecimalFormat df=new java.text.DecimalFormat("#.#");
		String ss=df.format(s);		
		return ss;
	}

	public static String getJson(Object data) {
		Gson gson = new Gson();
		return gson.toJson(data);

	}
	//小米顶部字体颜色
	public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
		  Class<? extends Window> clazz = activity.getWindow().getClass();
		  try {
		    int darkModeFlag = 0;
		    Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
		    Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
		    darkModeFlag = field.getInt(layoutParams);
		    Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
		    extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
		    return true;
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		  return false;
		}
	//魅族顶部状态栏字体颜色
	public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
		Window window = activity.getWindow();
		boolean result = false;
		if (activity != null) {
			try {
				WindowManager.LayoutParams lp = window.getAttributes();
				Field darkFlag = WindowManager.LayoutParams.class
						.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
				Field meizuFlags = WindowManager.LayoutParams.class
						.getDeclaredField("meizuFlags");
				darkFlag.setAccessible(true);
				meizuFlags.setAccessible(true);
				int bit = darkFlag.getInt(null);
				int value = meizuFlags.getInt(lp);
				if (dark) {
					value |= bit;
				} else {
					value &= ~bit;
				}
				meizuFlags.setInt(lp, value);
				window.setAttributes(lp);
				result = true;
			} catch (Exception e) {
			}
		}
		return result;
	}

	public static String removeAllSpace(String str)
	{
		String tmpstr=str.replace("-","");
		return tmpstr;
	}
}
