package com.baolinetworktechnology.shejiquan.adapter;

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
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.fragment.CollectCaseFragment;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.utils.WindowsUtil;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.google.gson.Gson;
import com.guojisheng.koyview.ExplosionField;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏资讯适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class SwCollectOwnerOpusAdapter extends BaseAdapter {
	private boolean mIsShowLine;

	List<SwCase> mData = new ArrayList<SwCase>();
	BitmapUtils mImageUtil;
	Context mContex;
	int whith;

	public SwCollectOwnerOpusAdapter(Context context) {
		this(context, false);
	}

	public SwCollectOwnerOpusAdapter(Context context, boolean isShowLine) {
		mIsShowLine = isShowLine;
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
		// 设置图片压缩类型
		whith = WindowsUtil.dip2px(context, 85);
		mContex =context;
	}

	public void setData(List<SwCase> data) {
		if (this.mData != null) {
			this.mData.clear();
		}
		if (data != null) {
			this.mData.addAll(data);
			notifyDataSetChanged();
		}
	}

	public void addData(List<SwCase> data) {
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
		return mData.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder1 = null;

		SwCase news = mData.get(position);
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
			SwCase news = mData.get(position);
			String url = AppUrl.FAVORITE_ADD;
			String FromGUID = news.guid;
			RequestParams params = new RequestParams();
			params.setHeader("Content-Type","application/json");
			try {
				JSONObject param  = new JSONObject();
				param.put("classType","2");
				param.put("userGUID", SJQApp.user.guid);
				param.put("fromGUID", FromGUID);
				param.put("favoriteMark", "0");
				param.put("operate", "0");
				StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
				params.setBodyEntity(sEntity);
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			getHttpUtils().send(HttpMethod.POST, AppUrl.API + url, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException error, String msg) {

						}

						@Override
						public void onSuccess(ResponseInfo<String> n) {
							Gson gson = new Gson();
							SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
							if (bean != null) {
								if (bean.result.operatResult) {
								}else{
								}
							}

						}
					});
			mExplosionField.clear();
			mExplosionField.explode(view);
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					mData.remove(position);
					notifyDataSetChanged();
					if(mData.size()==0){
						Intent intent = new Intent();
						intent.setAction("Caseshow");
						mContex.sendBroadcast(intent);
					}
				}
			};
			Handler handler = new Handler();
			handler.postDelayed(runnable, 800);
		}

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

		public void setData(SwCase data) {
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
			tv_title.setText(data.title);
			if (iv_image.getTag() == null
					|| !iv_image.getTag().toString()
							.equals(data.getImages())) {
				mImageUtil.display(iv_image, data.getImages());
				iv_image.setTag(data.getImages());
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
