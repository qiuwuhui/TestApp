package com.baolinetworktechnology.shejiquan.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.baolinetworktechnology.shejiquan.activity.PhotoActivity;
/***
 * web嵌入js通信
 * @author JiSheng.Guo
 *
 */
public class WebJavascriptInterface {
	private Context context;
	ArrayList<String> mImageList;

	public WebJavascriptInterface(Context context) {
		this.context = context;
	}

	@JavascriptInterface
	public void addImage(String img) {
		if (mImageList == null) {
			mImageList = new ArrayList<String>();
		}
		mImageList.add(img);

	}

	@JavascriptInterface
	public void openImage(String img) {
		String[] imgs = img.split(",");//分割
		if (imgs != null && imgs.length > 0) {//有数据
			Intent intent = new Intent(context, PhotoActivity.class);
			if (mImageList != null) {//有图片集
				intent.putExtra(PhotoActivity.IMAGE_URLS, mImageList);
				for (int i = 0; i < mImageList.size(); i++) {
					if (img.equals(mImageList.get(i))) {//查找当前位置
						intent.putExtra(PhotoActivity.INDEX, i);
						break;
					}
				}
			} else {
				intent.putExtra(PhotoActivity.IMAGE_URL, imgs[0]);
			}
			context.startActivity(intent);
		} else {
			Intent intent = new Intent(context, PhotoActivity.class);
			intent.putExtra(PhotoActivity.IMAGE_URL, img);
			context.startActivity(intent);
		}
	}
}
