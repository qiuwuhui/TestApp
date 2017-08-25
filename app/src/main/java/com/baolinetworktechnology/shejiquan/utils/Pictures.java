package com.baolinetworktechnology.shejiquan.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 会员认证---身份证图片对象
 * 
 * @author Tony.An
 * 
 */
public class Pictures {
	public static int max = 0;
	public static boolean act_bool = false;
	public static List<Bitmap> bmps = new ArrayList<Bitmap>(); // 压缩图片对象
	public static List<String> addrs = new ArrayList<String>(); // 图片sd地址
	public static List<String> cache_addrs = new ArrayList<String>(); // 压缩图片缓存sd地址

//	 图片压缩， 保存到临时文件夹图片压缩后小于100KB，失真度不明显
	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 650)
					&& (options.outHeight >> i <= 650)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(1.7D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

//	 图片压缩， 保存到临时文件夹图片压缩后小于100KB，失真度不明显
	public static Bitmap revitionImageSize(Bitmap bp) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bp.compress(Bitmap.CompressFormat.PNG, 50, baos);
		BufferedInputStream in = new BufferedInputStream(
				new ByteArrayInputStream(baos.toByteArray()));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 500)
					&& (options.outHeight >> i <= 500)) {
				in = new BufferedInputStream(new ByteArrayInputStream(
						baos.toByteArray()));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public static void clearCache() {
		max = 0;
		act_bool = false;
		bmps.clear();
		addrs.clear();
		cache_addrs.clear();
	}
}
