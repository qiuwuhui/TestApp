package com.baolinetworktechnology.shejiquan.utils;

/**
 * 文件管理类
 */
import java.io.File;
import java.text.DecimalFormat;

import com.baolinetworktechnology.shejiquan.domain.Case;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import android.content.Context;
import android.os.Environment;
import android.webkit.WebView;
import android.widget.Toast;

public class FilesManager {

	// 获取内部缓存(-1表示获取错误)
	public static long getInternalCache(Context context) {
		return getFileSize(context.getCacheDir());
	}

	// 获取外部缓存(-1表示获取错误)
	public static long getExternalCache(Context context) {
		return getFileSize(context.getExternalCacheDir());
	}

	// 获取所有的缓存大小
	public static String getAllCache(Context context) {
		return FormetFileSize(getFileSize(context.getCacheDir())
				+ getFileSize(context.getExternalCacheDir()));
	}

	// 清空 内部缓存
	public static boolean cleanInternalCache(Context context) {
		if (deleteFilesByDirectory(context.getCacheDir())) {
			return true;
		}
		return false;

	}

	// 清空 外部缓存
	public static boolean cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (deleteFilesByDirectory(context.getExternalCacheDir())) {
				return true;
			}
		} else {
			Toast.makeText(context, "外部存储卡不可用", Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	// 清空 所有缓存
	public static boolean cleanAllCache(Context context) {
		try {
			cleanExternalCache(context);
			cleanInternalCache(context);
			DbUtils db = DbUtils.create(context);
			db.deleteAll(Case.class);
			
			new WebView(context).clearCache(true);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// 删除文件夹
	private static boolean deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				if (item.isDirectory()) {
					deleteFilesByDirectory(item);
				} else {
					item.delete();
				}
			}
			return true;
		}

		return false;

	}

	// 递归 取得文件夹大小
	public static long getFileSize(File f) {
		long size = 0;
		try {
			File flist[] = f.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSize(flist[i]);
				} else {
					size = size + flist[i].length();
				}

			}

		} catch (Exception e) {
			size = -1;
			System.out.println("-->>" + e.getMessage());
		}

		return size;
	}

	// 转换文件大小
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("0.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
	// //清除数据库
	// public static void cleanDatabases(Context context) {
	// deleteFilesByDirectory(new File("/data/data/"
	// + context.getPackageName() + "/databases"));
	// }
	// //清除数共享参数
	// public static void cleanSharedPreference(Context context) {
	// deleteFilesByDirectory(new File("/data/data/"
	// + context.getPackageName() + "/shared_prefs"));
	// }
	//
	// public static void cleanDatabaseByName(Context context, String dbName) {
	// context.deleteDatabase(dbName);
	// }
	//
	// public static void cleanFiles(Context context) {
	// deleteFilesByDirectory(context.getFilesDir());
	// }
	// //清除自定义缓存
	// public static void cleanCustomCache(String filePath) {
	// deleteFilesByDirectory(new File(filePath));
	// }
	//
	// public static void cleanApplicationData(Context context, String...
	// filepath) {
	// cleanInternalCache(context);
	// cleanExternalCache(context);
	// cleanDatabases(context);
	// cleanSharedPreference(context);
	// cleanFiles(context);
	// for (String filePath : filepath) {
	// cleanCustomCache(filePath);
	// }
	// }
}
