package com.baolinetworktechnology.shejiquan.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.News;
import com.baolinetworktechnology.shejiquan.domain.ZiXunList;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.utils.WindowsUtil;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.guojisheng.koyview.ExplosionField;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 收藏资讯适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class CollectOwnerNewsAdapter extends BaseAdapter {
	List<ZiXunList> mData;
	BitmapUtils mImageUtil;
	int whith;
	Context mContex;
	public CollectOwnerNewsAdapter(Context context) {
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
		// 设置图片压缩类型
		whith = WindowsUtil.dip2px(context, 85);
		mContex =context;
	}

	public void setData(List<ZiXunList> data) {
		if (this.mData != null) {
			this.mData.clear();
		}
		this.mData = data;
		notifyDataSetChanged();
	}

	public void addData(List<ZiXunList> data) {
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
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mData.get(position).id;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ZiXunList news = mData.get(position);
		Holder3 holder1 = new Holder3();
		if(news.typeID==10){
			//多张图片
			convertView = View.inflate(parent.getContext(),
					R.layout.item_inspi_duo_image, null);
			holder1.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			holder1.iv_images = (ImageView) convertView.findViewById(R.id.iv_images);
			holder1.iv_imagess = (ImageView) convertView.findViewById(R.id.iv_imagess);
			holder1.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder1.tvText = (TextView) convertView.findViewById(R.id.tvText);
			holder1.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
			holder1.tv_title.setText(news.title);
			holder1.tv_leixin.setText("阅读类型");
			holder1.tvText.setText("阅读："+news.hits);
			mImageUtil.display(holder1.iv_image, news.getSmallImages("_300_150."));
			mImageUtil.display(holder1.iv_images, news.getSmallImages("_300_150."));
			mImageUtil.display(holder1.iv_imagess, news.getSmallImages("_300_150."));
		}else{
			convertView = View.inflate(parent.getContext(),
					R.layout.item_inspi_small_image, null);
			holder1.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			holder1.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder1.tvText = (TextView) convertView.findViewById(R.id.tvText);
			holder1.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
			holder1.tv_title.setText(news.title);
			holder1.tv_leixin.setText(news.className);
			holder1.tvText.setText("阅读："+news.hits);
			mImageUtil.display(holder1.iv_image, news.getSmallImages("_300_150."));
		}
//		if (convertView.getAlpha() != 1) {
//			convertView.setAlpha(1);
//			convertView.setScaleX(1);
//			convertView.setScaleY(1);
//			convertView.setTranslationX(0);
//		}
		return convertView;
	}
	// 小图
	class Holder3 {
		TextView tv_title,tvText,tv_leixin,tvTime;
		ImageView iv_image,iv_images,iv_imagess;

	}

	public void doCollect(final int position, View view) {
		// mDialog.show();
		if (SJQApp.user != null) {
			ZiXunList news = mData.get(position);
			String FromGUID = news.guid;
			String url = AppUrl.FAVORITE_ADD;
			RequestParams params = new RequestParams();
			params.setHeader("Content-Type","application/json");
			try {
				JSONObject param  = new JSONObject();
				param.put("classType","0");
				param.put("userGUID", SJQApp.user.guid);
				param.put("fromGUID", FromGUID);
				param.put("operate", "0");
				StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
				params.setBodyEntity(sEntity);
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			getHttpUtils()
					.send(HttpMethod.POST, AppUrl.API + url, params, null);
			mExplosionField.clear();
			mExplosionField.explode(view);
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					// mDialog.dismiss();
					mData.remove(position);
					notifyDataSetChanged();
					if(mData.size()==0){
						Intent intent = new Intent();
						intent.setAction("Newshow");
						mContex.sendBroadcast(intent);
					}
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
		// httpUtil.configDefaultHttpCacheExpiry(1000 * 2);
		return httpUtil;

	}

	class Holder {
		TextView tv_title, tv_time, tv_numview, tv_text, tv_collect, tv_messge;
		ImageView iv_image;

		// View pview;
		Holder(View view) {
			// pview=view;
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_text = (TextView) view.findViewById(R.id.tv_text);
			iv_image = (ImageView) view.findViewById(R.id.iv_image);
			tv_numview = (TextView) view.findViewById(R.id.tv_numview);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_collect = (TextView) view.findViewById(R.id.tv_collect);
			tv_messge = (TextView) view.findViewById(R.id.tv_messge);
		}

		public void setData(News news) {
			tv_text.setText(news.Descriptions);
			tv_title.setText(news.Title);
			tv_time.setText(news.CreateTime);
			mImageUtil.display(iv_image, news.getSmallImages());
			setNumber(tv_numview, news.Hits);
			setNumber(tv_collect, news.NumFavorite);
			setNumber(tv_messge, news.NumComment);
		}

		void setNumber(TextView tv, int n) {
			if (tv != null) {
				tv.setText(n < 0 ? 0 + "" : n + "");
			}
		}

	}

	public void setChangData(boolean isChange) {

	}

	ExplosionField mExplosionField;

	public void setChangData(boolean is, ExplosionField mExplosionField) {
		this.mExplosionField = mExplosionField;
		notifyDataSetChanged();
	}
}
