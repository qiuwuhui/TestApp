package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.lidroid.xutils.BitmapUtils;

/**
 * 收藏资讯适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class HomeOpusAdapter extends BaseAdapter implements OnClickListener {

	List<Case> mData = new ArrayList<Case>();
	BitmapUtils mImageUtil;
	Context context;

	public HomeOpusAdapter(Context context) {
		this(context, false);
	}

	public HomeOpusAdapter(Context context, boolean isShowLine) {
		this.context = context;
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
		// 设置图片压缩类型
	}

	public void setData(List<Case> data) {
		if (this.mData != null) {
			this.mData.clear();
		}
		if (data != null) {
			this.mData.addAll(data);
			notifyDataSetChanged();
		}
	}

	public void addData(List<Case> data) {
		if (data != null)
			this.mData.addAll(data);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mData == null)
			return 0;
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
		return mData.get(position).ID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder1 = null;

		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_home_opus, null);
			holder1 = new Holder(convertView);
			convertView.setTag(holder1);

		} else {
			holder1 = (Holder) convertView.getTag();
		}

		// position计算 两列的情况下：position* 2 +1 多列依次类推
		// 第一列
		Case news = mData.get(position * 2);
		// 第二列
		Case news2 = null;
		if ((position * 2 + 1) < mData.size()) {
			news2 = mData.get(position * 2 + 1);
		}

		holder1.setData(news, news2);
		return convertView;
	}

	int height = 0;

	class Holder {
		TextView tv_title, tv_title2;
		ImageView iv_image, iv_image2;
		View item1, item2;

		Holder(View view) {
			tv_title = (TextView) view.findViewById(R.id.tvTitle);
			iv_image = (ImageView) view.findViewById(R.id.ivImage);
			tv_title2 = (TextView) view.findViewById(R.id.tvTitle2);
			iv_image2 = (ImageView) view.findViewById(R.id.ivImage2);
			item1 = view.findViewById(R.id.item1);
			item2 = view.findViewById(R.id.item2);
			iv_image2.setTag("");
			iv_image.setTag("");

			if (height != 0) {
				initHeight(iv_image, iv_image2);
			} else {
				iv_image.postDelayed(new Runnable() {

					@Override
					public void run() {
						initHeight(iv_image, iv_image2);
					}

				}, 200);
			}

			item1.setOnClickListener(HomeOpusAdapter.this);
			item2.setOnClickListener(HomeOpusAdapter.this);
		}

		private void initHeight(ImageView iv1, ImageView iv2) {
			if (iv1 == null)
				return;

			if (height > 0) {
				LayoutParams pa = iv1.getLayoutParams();
				pa.height = height;
				iv1.setLayoutParams(pa);
				LayoutParams pa2 = iv2.getLayoutParams();
				pa2.height = height;
				iv2.setLayoutParams(pa2);
			} else {
				int with = iv1.getWidth();
				height = with * 3 / 4;
				if (height <= 0)
					return;
				LayoutParams pa = iv1.getLayoutParams();
				pa.height = height;
				iv1.setLayoutParams(pa);
				LayoutParams pa2 = iv2.getLayoutParams();
				pa2.height = height;
				iv2.setLayoutParams(pa2);
			}

		}

		public void setData(Case data, Case data2) {

			// Item1:
			item1.setTag(data);
			tv_title.setText(data.Title);
			if (!iv_image.getTag().toString().equals(data.getSmallImages())) {
				mImageUtil.display(iv_image, data.getSmallImages());
				iv_image.setTag(data.getSmallImages());
			}

			// Item2:
			item2.setTag(data2);
			if (data2 == null) {
				item2.setVisibility(View.INVISIBLE);
				return;
			}
			if (item2.getVisibility() != View.VISIBLE)
				item2.setVisibility(View.VISIBLE);

			tv_title2.setText(data2.Title);
			if (!iv_image2.getTag().toString().equals(data2.getSmallImages())) {
				mImageUtil.display(iv_image2, data2.getSmallImages());
				iv_image2.setTag(data2.getSmallImages());
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

	@Override
	public void onClick(View v) {
		Case item = (Case) v.getTag();
		if (item == null)
			return;
		Intent intent = new Intent(context, WebOpusActivity.class);
		String url = ApiUrl.DETAIL_CASE2 + item.ID;
		intent.putExtra("WEB_URL", url);
		intent.putExtra(AppTag.TAG_ID, item.ID);
		intent.putExtra(WebDetailActivity.GUID, item.GUID);
		intent.putExtra(WebDetailActivity.ISCASE, true);
		intent.putExtra(AppTag.TAG_JSON, item.toString());
		context.startActivity(intent);

	}
}
