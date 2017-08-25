package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.Picture;
import com.lidroid.xutils.BitmapUtils;

/**
 * 收藏资讯适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class PictureAdapter extends BaseAdapter implements OnClickListener {

	List<Picture> mData = new ArrayList<Picture>();
	BitmapUtils mImageUtil;

	public PictureAdapter(Context context) {
		this(context, false);
	}

	public PictureAdapter(Context context, boolean isShowLine) {

		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.home_con_icon_jztpz_default);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.home_con_icon_jztpz_default);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
		// AlphaAnimation animation = new AlphaAnimation(0, 1);
		// animation.setDuration(500);
		// /mImageUtil.configDefaultImageLoadAnimation(animation);
		// 设置图片压缩类型
	}

	public void setData(List<Picture> data) {
		if (this.mData != null) {
			this.mData.clear();
		}
		if (data != null) {
			this.mData.addAll(data);
			notifyDataSetChanged();
		}
	}

	public void addData(List<Picture> data) {
		if (data != null)
			this.mData.addAll(data);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mData == null)
			return 0;
		// 每列两项
		if (mData.size() % 2 == 0) {
			return mData.size() / 2;
		}

		return mData.size() / 2 + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position < 0 || position >= mData.size())
			return null;
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mData.get(position).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder1 = null;
		if (convertView == null) {

			convertView = View.inflate(parent.getContext(),
					R.layout.item_main_picture, null);

			holder1 = new Holder(convertView);
			convertView.setTag(holder1);
		} else {
			holder1 = (Holder) convertView.getTag();
		}

		// position计算 两列的情况下：position* 2 +1 多列依次类推
		// 第1列
		int c1 = position * 2;
		// 第2列
		int c2 = position * 2 + 1;
		holder1.setData(c1, c2);
		return convertView;
	}

	class Holder {
		ImageView iv1, iv2;

		Holder(View view) {
			iv1 = (ImageView) view.findViewById(R.id.iv1);
			iv2 = (ImageView) view.findViewById(R.id.iv2);
			iv1.setOnClickListener(PictureAdapter.this);
			iv2.setOnClickListener(PictureAdapter.this);
			iv1.setTag(0);
			iv2.setTag(0);
		}

		public void setData(int c1, int c2) {

			// position计算 两列的情况下：position* 2 +1 多列依次类推
			// 第1列
			if (c1 < mData.size()) {

				Picture p1 = mData.get(c1);
				mImageUtil.display(iv1, p1.getSmallImages());
				iv1.setTag(c1);
			}
			// 第2列
			if (c2 < mData.size()) {
				if (iv2.getVisibility() != View.VISIBLE)
					iv2.setVisibility(View.VISIBLE);
				Picture p2 = mData.get(c2);
				mImageUtil.display(iv2, p2.getSmallImages());
				iv2.setTag(c2);

			} else {
				iv2.setVisibility(View.GONE);
			}

		}

	}

	int lineColor = 0;

	/**
	 * 设置分割线颜色
	 * 
	 * @param color
	 */
	public void setDLineColor(int color) {

	}

	public List<Picture> getDatas() {
		// TODO Auto-generated method stub
		return mData;
	}

	@Override
	public void onClick(View v) {
		if (mOnItemClickListener != null)
			mOnItemClickListener.onItemClickListener((Integer) v.getTag());

	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}

	OnItemClickListener mOnItemClickListener;

	public interface OnItemClickListener {
		void onItemClickListener(int position);
	}

}
