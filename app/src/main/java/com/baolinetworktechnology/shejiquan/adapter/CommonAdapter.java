package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.baolinetworktechnology.shejiquan.R;
import com.lidroid.xutils.BitmapUtils;

/**
 * 万能适配器
 * 
 * @author JiSheng.Guo
 * 
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	protected int layoutId;
	protected BitmapUtils mImageUtil;
	protected BitmapUtils mImageUtil1;
	public CommonAdapter(Context context, int layoutId) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.layoutId = layoutId;
		this.mDatas = new ArrayList<T>();
		initBuitls();
	}

	public void setData(List<T> data) {
		if (mDatas != null) {
			mDatas.clear();
			if (data != null) {
				mDatas.addAll(data);
			}
		} else {
			mDatas = data;
		}

	}
	public void setData1(List<T> data) {
	    if (data != null) {
			mDatas.clear();
			mDatas.addAll(data);
			}
	}
	public void addData(List<T> data) {
		if (mDatas != null) {
			if (data != null) {
				mDatas.addAll(data);
			}
		} else {
			mDatas = data;
		}
	}

	public void addData(T data) {
		if (mDatas != null) {
			if (data != null) {
				mDatas.add(data);
			}
		}
	}

	public CommonAdapter(Context context, List<T> datas, int layoutId) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.layoutId = layoutId;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		if (position < 0 || position >= mDatas.size())
			return null;
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
				layoutId, position);
		convert(holder, getItem(position));
		return holder.getConvertView();
	}

	public void setImageUrl(View view, String imageUrl) {
		// if (view.getTag() == null || !view.getTag().equals(imageUrl)) {
		
		String imageUrl1=toUtf8String(imageUrl);
		mImageUtil.display(view, imageUrl1);
		//view.setTag(imageUrl);
		// }
	}
	public void setImageUrl1(View view, String imageUrl) {
		// if (view.getTag() == null || !view.getTag().equals(imageUrl)) {
		
		String imageUrl1=toUtf8String(imageUrl);
		mImageUtil1.display(view, imageUrl1);
		//view.setTag(imageUrl);
		// }
	}
	public void setImageViewUrl(ImageView view, String imageUrl) {
		// if (view.getTag() == null || !view.getTag().equals(imageUrl)) {
		String imageUrl1=toUtf8String(imageUrl);
		mImageUtil.display(view, imageUrl1);
		view.setTag(imageUrl1);
		// }
	}

	public void setDefaultLoadingImage(int drawableId) {
		mImageUtil.configDefaultLoadingImage(drawableId);
		mImageUtil.configDefaultLoadFailedImage(drawableId);
	}
	public void setDefaultLoadingImage1(int drawableId) {
		mImageUtil1.configDefaultLoadingImage(drawableId);
		mImageUtil1.configDefaultLoadFailedImage(drawableId);
	}
	public abstract void convert(ViewHolder holder, T item);

	private void initBuitls() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String diskCachePath = Environment.getExternalStorageDirectory()
					+ "/SheJiQuan/CacheImage";
			mImageUtil = new BitmapUtils(mContext, diskCachePath);
			mImageUtil1= new BitmapUtils(mContext, diskCachePath);
		} else {
			mImageUtil = new BitmapUtils(mContext);
			mImageUtil1 = new BitmapUtils(mContext);
		}
		mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		
		mImageUtil1.configDefaultLoadingImage(R.drawable.zixun_tu);
		mImageUtil1.configDefaultLoadFailedImage(R.drawable.zixun_tu);
		mImageUtil1.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
	}

	public List<T> getDatas() {
		return mDatas;
	}
	public static String toUtf8String(String s) {
	       StringBuffer sb = new StringBuffer();
	       for (int i = 0; i < s.length(); i++) {
	           char c = s.charAt(i);
	           if (c >= 0 && c <= 255) {
	               sb.append(c);
	           } else {
	               byte[] b;
	               try {
	                   b = String.valueOf(c).getBytes("utf-8");
	               } catch (Exception ex) {
	                   System.out.println(ex);
	                   b = new byte[0];
	               }
	               for (int j = 0; j < b.length; j++) {
	                   int k = b[j];
	                   if (k < 0)
	                       k += 256;
	                   sb.append("%" + Integer.toHexString(k).toUpperCase());
	               }
	           }
	       }
	       return sb.toString();
	   }
	public void SetItem(){}
}
