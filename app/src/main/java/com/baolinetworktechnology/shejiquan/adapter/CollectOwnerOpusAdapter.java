package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.utils.WindowsUtil;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.guojisheng.koyview.ExplosionField;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 收藏资讯适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class CollectOwnerOpusAdapter extends BaseAdapter {
	private boolean mIsShowLine;

	List<Case> mData = new ArrayList<Case>();
	BitmapUtils mImageUtil;
	MyDialog mDialog;
	int whith;

	public CollectOwnerOpusAdapter(Context context) {
		this(context, false);
	}

	public CollectOwnerOpusAdapter(Context context, boolean isShowLine) {
		mIsShowLine = isShowLine;
		mImageUtil = new BitmapUtils(context);
		mDialog = new MyDialog(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
		// 设置图片压缩类型
		whith = WindowsUtil.dip2px(context, 85);
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
		return mData.size();
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

		Case news = mData.get(position);
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_case_collect, null);
			if (mIsShowLine) {
				View line = convertView.findViewById(R.id.line);
				line.setVisibility(View.VISIBLE);
				if (lineColor != 0) {// 分割线颜色
					line.setBackgroundColor(lineColor);
				}
			}
			holder1 = new Holder(convertView);
			convertView.setTag(holder1);

		} else {
			holder1 = (Holder) convertView.getTag();
		}

		if (convertView.getAlpha() != 1) {
			convertView.setAlpha(1);
			convertView.setScaleX(1);
			convertView.setScaleY(1);
			convertView.setTranslationX(0);
			convertView.setEnabled(true);

		}
		holder1.setData(news);
		return convertView;
	}

	public void doCollect(final int position, View view) {
		if (SJQApp.user != null) {
			Case news = mData.get(position);
			String FromGUID = news.GUID;
			String url = ApiUrl.FAVORITE_CANCEL;
			RequestParams params = getParams(url);
			// case
			params.addBodyParameter("ClassType", "1");
			params.addBodyParameter("UserGUID", SJQApp.user.guid);
			params.addBodyParameter("FromGUID", FromGUID);
			getHttpUtils()
					.send(HttpMethod.POST, ApiUrl.API + url, params, null);
			mExplosionField.clear();
			mExplosionField.explode(view);
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					mData.remove(position);
					notifyDataSetChanged();
				}
			};
			Handler handler = new Handler();
			handler.postDelayed(runnable, 800);
		}

	}

	RequestParams getParams(String url) {

		RequestParams params = new RequestParams();
		if (SJQApp.user != null) {
			params.setHeader("Token", SJQApp.user.Token);
		} else {
			params.setHeader("Token", null);
		}

		params.setHeader("Version", "1.0");
		params.setHeader("AppAgent", "ANDROID_SHEJIQUAN_Ver.1.0");
		params.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
		return params;

	}

	HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(6 * 1000);
		return httpUtil;

	}

	class Holder {
		TextView tv_title, tv_tips,tv_favorat, tvCost;
		ImageView iv_image;
		View llayout;

		Holder(View view) {
			tv_title = (TextView) view.findViewById(R.id.tvTitle);
			tv_tips = (TextView) view.findViewById(R.id.tvTips);
			tv_favorat = (TextView) view.findViewById(R.id.tv_favorat);
			tvCost = (TextView) view.findViewById(R.id.tvCost);
			llayout = view.findViewById(R.id.llayout);
			iv_image = (ImageView) view.findViewById(R.id.iv_image);

		}

		public void setData(Case data) {
//			if (data.ClassID == 5) {// 家装
				if (llayout.getVisibility() != View.VISIBLE) {
					llayout.setVisibility(View.GONE);
				}
//				if (TextUtils.isEmpty(data.Budget)) {
//					tvCost.setText("面议");
//				} else {
//					tvCost.setText(data.getMyBudget());
//				}
				tv_favorat.setText("收藏　"+data.getNumFavorite());
//				tv_tips.setText(data.getTipsPublic());
				tv_tips.setText(data.getTips());
//			} else {

//				if (llayout.getVisibility() != View.GONE) {
//					llayout.setVisibility(View.GONE);
//				}
//			}
			tv_title.setText(data.Title);
			if (iv_image.getTag() == null
					|| !iv_image.getTag().toString()
							.equals(data.getSmallImages())) {
				mImageUtil.display(iv_image, data.getSmallImages());
				iv_image.setTag(data.getSmallImages());
			}
		}

	}

	public void setChangData(boolean isChange) {

	}

	ExplosionField mExplosionField;

	public void setChangData(boolean is, ExplosionField mExplosionField) {
		this.mExplosionField = mExplosionField;
		// notifyDataSetChanged();
	}

	int lineColor = 0;

	/**
	 * 设置分割线颜色
	 * 
	 * @param color
	 */
	public void setDLineColor(int color) {
		lineColor = color;

	}

	
	
}
