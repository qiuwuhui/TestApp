package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.utils.WindowsUtil;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.guojisheng.koyview.ExplosionField;
import com.lidroid.xutils.BitmapUtils;

public class CollectOpusAdapter extends BaseAdapter {
	// final int BIG_IMAGE = 0;// 有图
	// final int NULL_IMAGE = 1;// 无图
	ExplosionField mExplosionField;
	List<Case> mData;
	BitmapUtils mImageUtil;
	MyDialog mDialog;
	int whith;

	public CollectOpusAdapter(Context context) {
		mImageUtil = new BitmapUtils(context);
		mDialog = new MyDialog(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.default_icon);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.default_icon);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		whith = WindowsUtil.dip2px(context, 85);
	}

	public void setData(List<Case> data) {
		if (this.mData != null) {
			this.mData.clear();
		} else {
			mData = new ArrayList<Case>();
		}
		if (data != null)
			this.mData.addAll(data);
		notifyDataSetChanged();
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
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		if (position < 0 || mData == null || position >= mData.size())
			return null;
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mData.get(position).ID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder1 holder1 = null;

		Case news = mData.get(position);
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_case_collect, null);
			holder1 = new Holder1(convertView);
			convertView.setTag(holder1);
		} else {
			holder1 = (Holder1) convertView.getTag();
		}
		holder1.setData(news);
		return convertView;
	}

	class Holder1 {
		TextView tv_title, tv_tips, tv_hist, tv_comment, tv_favorat, tvCost;
		ImageView iv_image;
		CircleImg userLogo;
		View llayout;

		Holder1(View view) {
			tv_title = (TextView) view.findViewById(R.id.tvTitle);
			tv_tips = (TextView) view.findViewById(R.id.tvTips);
			tv_hist = (TextView) view.findViewById(R.id.tv_hist);
			tv_comment = (TextView) view.findViewById(R.id.tv_comment);
			tv_favorat = (TextView) view.findViewById(R.id.tv_favorat);
			tvCost = (TextView) view.findViewById(R.id.tvCost);
			llayout = view.findViewById(R.id.llayout);
			iv_image = (ImageView) view.findViewById(R.id.iv_image);
			userLogo = (CircleImg) view.findViewById(R.id.userLogo);

		}

		public void setData(Case data) {
			if (data.ClassID == 5) {// 家装
				if (llayout.getVisibility() != View.VISIBLE) {
					llayout.setVisibility(View.VISIBLE);

					tv_hist.setVisibility(View.GONE);
					tv_favorat.setVisibility(View.GONE);
					tv_comment.setVisibility(View.GONE);
				}
				if (TextUtils.isEmpty(data.Budget)) {
					tvCost.setText("面议");
				} else {
					tvCost.setText(data.getMyBudget());
				}

				tv_tips.setText(data.getTips());
			} else {
				if (llayout.getVisibility() != View.GONE) {
					llayout.setVisibility(View.GONE);
					tv_hist.setVisibility(View.VISIBLE);
					tv_favorat.setVisibility(View.VISIBLE);
					tv_comment.setVisibility(View.VISIBLE);
				}
				tv_hist.setText(data.getHits() + "");
				tv_comment.setText(data.getNumComment() + "");
				tv_favorat.setText(data.getNumFavorite() + "");
				tv_tips.setText(data.getTipsPublic());
			}

			tv_title.setText(data.Title);

			if (iv_image.getTag() == null
					|| !iv_image.getTag().toString()
							.equals(data.getSmallImages())) {
				mImageUtil.display(iv_image, data.getSmallImages());
				iv_image.setTag(data.getSmallImages());
			}
			if (userLogo.getTag() == null
					|| !userLogo.getTag().toString().equals(data.UserLogo)) {
				mImageUtil.display(userLogo, data.UserLogo);
				userLogo.setTag(data.UserLogo);
			}

		}

	}

	public void setChangData(boolean isChange) {
		notifyDataSetChanged();

	}

	public void setIsMy(boolean isMy) {
		// TODO Auto-generated method stub
		
	}

}
