package com.baolinetworktechnology.shejiquan.adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.News;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.YouLikemode;
import com.baolinetworktechnology.shejiquan.domain.ZiXunList;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.utils.WindowsUtil;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.SwipeItemLayoutListview;
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

/**
 * 收藏资讯适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class CollectOwnerNewsAdapter2 extends BaseAdapter {
	private String TAG = "CollectOwnerNews2";
	List<ZiXunList> mData;
	List<String> deleleteList;
	BitmapUtils mImageUtil;
	int whith;
	Context mContex;
	private boolean isdeletemode;
	public CollectOwnerNewsAdapter2(Context context) {
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
		if (data != null && data.size() != 0) {
			this.mData=data;
			deletemun();
			notifyDataSetChanged();
		}
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
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		ZiXunList item=mData.get(position);
		if(item.typeID==10){
			return 0;
		}else{
			return 1;
		}
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		  ViewHolder holderduo = null;
		  ViewHolder1 holder = null;
		int ViewType =getItemViewType(position);
		ZiXunList news=mData.get(position);
		if(convertView ==null){
			//多张图片
			switch (ViewType){
				case 0:
				   holderduo =new ViewHolder();
				   convertView = View.inflate(parent.getContext(),
							R.layout.item_inspi_duo_image2, null);
					holderduo.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
					holderduo.iv_images = (ImageView) convertView.findViewById(R.id.iv_images);
					holderduo.iv_imagess = (ImageView) convertView.findViewById(R.id.iv_imagess);
					holderduo.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
					holderduo.tvText = (TextView) convertView.findViewById(R.id.tvText);
					holderduo.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
					holderduo.deleteLayout = (LinearLayout) convertView.findViewById(R.id.deleteLayout);
					holderduo.imgdelete = (ImageView) convertView.findViewById(R.id.img_delete);
					holderduo.swipeRoot = (SwipeItemLayoutListview) convertView.findViewById(R.id.con_item_lay);
					convertView.setTag(holderduo);
				break;
				case 1:
					holder =new ViewHolder1();
					convertView = View.inflate(parent.getContext(),
							R.layout.item_inspi_small_image2, null);
					holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
					holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
					holder.tvText = (TextView) convertView.findViewById(R.id.tvText);
					holder.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
					holder.deleteLayout = (LinearLayout) convertView.findViewById(R.id.deleteLayout);
					holder.imgdelete = (ImageView) convertView.findViewById(R.id.img_delete);
					holder.swipeRoot = (SwipeItemLayoutListview) convertView.findViewById(R.id.con_item_lay);
					convertView.setTag(holder);
					break;
			}
		}else{
			switch (ViewType){
				case 0:
					holderduo = (ViewHolder) convertView.getTag();
					break;
				case 1:
					holder = (ViewHolder1) convertView.getTag();
			}
		}
		switch (ViewType){
			case 0:
				holderduo.tv_title.setText(news.title);
				holderduo.tv_leixin.setText("阅读类型");
				holderduo.tvText.setText("阅读："+news.hits);
				if(news.getNewItemInfoList() != null){
					if(news.getNewItemInfoList().size() == 1){
						holderduo.iv_image.setVisibility(View.VISIBLE);
						mImageUtil.display(holderduo.iv_image, news.getNewItemInfoList().get(0).getImages());
					}else if(news.getNewItemInfoList().size() == 2){
						holderduo.iv_image.setVisibility(View.VISIBLE);
						holderduo.iv_images.setVisibility(View.VISIBLE);
						mImageUtil.display(holderduo.iv_image,  news.getNewItemInfoList().get(0).getImages());
						mImageUtil.display(holderduo.iv_images, news.getNewItemInfoList().get(1).getImages());
					}else if(news.getNewItemInfoList().size() == 3){
						holderduo.iv_image.setVisibility(View.VISIBLE);
						holderduo.iv_images.setVisibility(View.VISIBLE);
						holderduo.iv_imagess.setVisibility(View.VISIBLE);
						mImageUtil.display(holderduo.iv_image,  news.getNewItemInfoList().get(0).getImages());
						mImageUtil.display(holderduo.iv_images, news.getNewItemInfoList().get(1).getImages());
						mImageUtil.display(holderduo.iv_imagess,news.getNewItemInfoList().get(2).getImages());
					}
				}

				if (news.isDelete()) {
					holderduo.imgdelete.setBackgroundResource(R.drawable.add_men_check);
				} else {
					holderduo.imgdelete.setBackgroundResource(R.drawable.rb_bg_no);
				}
				holderduo.imgdelete.setTag(position);
				holderduo.imgdelete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						int podtitem = (Integer) view.getTag();
						if (mData.get(podtitem).isDelete()) {
							mData.get(podtitem).setDelete(false);
						} else {
							mData.get(podtitem).setDelete(true);
						}
						notifyDataSetChanged();
					}
				});
				holderduo.swipeRoot.setSwipeAble(false);
				if (isdeletemode) {
					holderduo.swipeRoot.openWithAnim();
				} else {
					holderduo.swipeRoot.closeWithAnim();
				}
				break;
			case 1:
				holder.tv_title.setText(news.title);
				holder.tv_leixin.setText(news.className);
				holder.tvText.setText("阅读："+news.hits);
				mImageUtil.display(holder.iv_image, news.getSmallImages("_300_150."));
				if (news.isDelete()) {
					holder.imgdelete.setBackgroundResource(R.drawable.add_men_check);
				} else {
					holder.imgdelete.setBackgroundResource(R.drawable.rb_bg_no);
				}
				holder.imgdelete.setTag(position);
				holder.imgdelete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						int podtitem = (Integer) view.getTag();
						if (mData.get(podtitem).isDelete()) {
							mData.get(podtitem).setDelete(false);
						} else {
							mData.get(podtitem).setDelete(true);
						}
						deletemun();
						notifyDataSetChanged();
					}
				});
				holder.swipeRoot.setSwipeAble(false);
				if (isdeletemode) {
					holder.swipeRoot.openWithAnim();
				} else {
					holder.swipeRoot.closeWithAnim();
				}
		}
		return convertView;
	}
	// 小图
	class ViewHolder {
		TextView tv_title,tvText,tv_leixin,tvTime;
		ImageView iv_image,iv_images,iv_imagess;
		LinearLayout deleteLayout;
		ImageView imgdelete;
		SwipeItemLayoutListview swipeRoot;
	}
	// 小图
	class ViewHolder1 {
		TextView tv_title,tvText,tv_leixin,tvTime;
		ImageView iv_image;
		LinearLayout deleteLayout;
		ImageView imgdelete;
		SwipeItemLayoutListview swipeRoot;
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
	public void doBitchCollect() {
		if(deleleteList == null){
			deleleteList= new ArrayList();
		}else{
			deleleteList.clear();
		}
		if (SJQApp.user != null) {
			for (int i = 0; i < mData.size(); i++) {
				if (mData.get(i).isDelete()) {
					if (mData.get(i).guid != null) {
						deleleteList.add(mData.get(i).guid);
					}
				}
			}
			if (deleleteList.size() == 0) {
				Toast.makeText(mContex, "请先选择要删除的资讯", Toast.LENGTH_SHORT).show();
				return;
			}
			String url = AppUrl.FAVORITE_CANCEL;
			RequestParams params = new RequestParams();
			params.addBodyParameter("userGUID",SJQApp.user.guid);
			for (int i = 0; i < deleleteList.size(); i++) {
				params.addBodyParameter("fromGUIDList[]", deleleteList.get(i));
			}
			params.addBodyParameter("classType","0");
			params.addBodyParameter("favoriteMark","0");
			getHttpUtils()
					.send(HttpMethod.PUT, AppUrl.API + url, params, new RequestCallBack<String>() {
						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							Gson gson = new Gson();
							SwresultBen bean = gson.fromJson(responseInfo.result, SwresultBen.class);
							Log.e("TAG2" + TAG, bean.toString());
							if (bean != null) {
								if (bean.result.operatResult) {
									for (int j = 0; j < deleleteList.size(); j++) {
											for (int i = 0 ; i<mData.size();i++){
													if(deleleteList.get(j).equals(mData.get(i).guid)){
														mData.remove(i);
													}
												}
											}
											if(mData.size()==0){
												Intent intent = new Intent();
												intent.setAction("Newshow");
												mContex.sendBroadcast(intent);
											}else{
												notifyDataSetChanged();
											}
									        deletemun();
								} else {
									Toast.makeText(mContex,bean.result.operatMessage,Toast.LENGTH_SHORT).show();
								}
							}
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Log.e("TAG1" + TAG, msg.toString());
						}
					});

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
	public void showDelete(boolean mode) {
		if(!mode){
			if(mData!=null){
				for (int i = 0 ; i<mData.size();i++){
					mData.get(i).setDelete(false);
				}
			}
		}
		isdeletemode = mode;
		notifyDataSetChanged();
	}
	private void deletemun(){
		int deletemun =0;
		for (int i = 0 ; i<mData.size();i++){
			if(mData.get(i).isDelete()){
				deletemun++;
			}
		}
		Intent deletent = new Intent();
		deletent.setAction("deletemun");
		deletent.putExtra("mun",deletemun);
		mContex.sendBroadcast(deletent);
	}

}
