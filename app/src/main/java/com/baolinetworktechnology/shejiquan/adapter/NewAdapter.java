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
import com.baolinetworktechnology.shejiquan.YouLikeAdapter;
import com.baolinetworktechnology.shejiquan.domain.GongLuelist;
import com.baolinetworktechnology.shejiquan.domain.News;
import com.baolinetworktechnology.shejiquan.domain.YouLikemode;
import com.baolinetworktechnology.shejiquan.domain.ZiXunList;
import com.lidroid.xutils.BitmapUtils;

public class NewAdapter extends BaseAdapter {

	public List<ZiXunList> mData;
	BitmapUtils mImageUtil;

	public NewAdapter(Context context) {
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.default_icon);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.default_icon);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		this.mData = new ArrayList<ZiXunList>();
	}

	public void setData(List<ZiXunList> data) {
		if (this.mData != null) {
			this.mData.clear();
		}
		if (data != null) {
			this.mData.addAll(data);
			notifyDataSetChanged();
		}
	}
	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		ZiXunList item=mData.get(position);
			if(item.typeID==10){
				return 1;
			}else{
				return 2;
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
	public Object getItem(int position) {
		if (mData == null)
			return null;
		if (position >= mData.size())
			return null;
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mData.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		     ZiXunList news = mData.get(position);
	         Holder1 holder1 = null;
		     Holder2 holder2 = null;
		    int ViewType =getItemViewType(position);
	     	if(convertView ==null){
		    	switch (ViewType) {
			    	case 1:
				 //多张图片
				 holder1 =new Holder1();
				 convertView = View.inflate(parent.getContext(),
						 R.layout.item_inspi_duo_image, null);
				 holder1.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
				 holder1.iv_images = (ImageView) convertView.findViewById(R.id.iv_images);
				 holder1.iv_imagess = (ImageView) convertView.findViewById(R.id.iv_imagess);
				 holder1.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				 holder1.tvText = (TextView) convertView.findViewById(R.id.tvText);
				 holder1.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
				 holder1.tv_title.setText(news.title);
				 holder1.tv_leixin.setBackgroundResource(R.color.white);
				 if(TextUtils.isEmpty(news.getLinkUrl())){
					 holder1.tv_leixin.setText(news.className);
				 }else{
					 holder1.tv_leixin.setText("");
					 holder1.tv_leixin.setBackgroundResource(R.drawable.new_guangao);
				 }
				 holder1.tvText.setText("阅读　"+news.hits);
				 holder1.iv_image.setVisibility(View.GONE);
				 holder1.iv_images.setVisibility(View.GONE);
				 holder1.iv_imagess.setVisibility(View.GONE);
				 if(news.getNewItemInfoList() != null){
					 if(news.getNewItemInfoList().size() == 1){
						 holder1.iv_image.setVisibility(View.VISIBLE);
						 mImageUtil.display(holder1.iv_image,news.getNewItemInfoList().get(0).getSmallImages());
					 }else if(news.getNewItemInfoList().size() == 2){
						 holder1.iv_image.setVisibility(View.VISIBLE);
						 holder1.iv_images.setVisibility(View.VISIBLE);
						 mImageUtil.display(holder1.iv_image,news.getNewItemInfoList().get(0).getSmallImages());
						 mImageUtil.display(holder1.iv_images, news.getNewItemInfoList().get(1).getSmallImages());
					 }else if(news.getNewItemInfoList().size() == 3){
						 holder1.iv_image.setVisibility(View.VISIBLE);
						 holder1.iv_images.setVisibility(View.VISIBLE);
						 holder1.iv_imagess.setVisibility(View.VISIBLE);
						 mImageUtil.display(holder1.iv_image, news.getNewItemInfoList().get(0).getSmallImages());
						 mImageUtil.display(holder1.iv_images,  news.getNewItemInfoList().get(1).getSmallImages());
						 mImageUtil.display(holder1.iv_imagess, news.getNewItemInfoList().get(2).getSmallImages());
					 }
				 }
					convertView.setTag(holder1);
					break;
					case 2:
						holder2 =new Holder2();
					     convertView = View.inflate(parent.getContext(),R.layout.item_inspi_small_image, null);
						holder2.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
						holder2.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
						holder2.tvText = (TextView) convertView.findViewById(R.id.tvText);
						holder2.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
						holder2.tv_title.setText(news.title);
						holder2.tv_leixin.setBackgroundResource(R.color.white);
						if(news.getLinkUrl().equals("")){
							holder2.tv_leixin.setText(news.className);
				          }else{
							holder2.tv_leixin.setText("");
							holder2.tv_leixin.setBackgroundResource(R.drawable.new_guangao);
				          }
						holder2.tvText.setText("阅读　"+news.hits);
				        mImageUtil.display(holder2.iv_image, news.getSmallImages());
						convertView.setTag(holder2);
					break;
			   }
		    }else{
				switch (ViewType) {
					case 1:
						holder1 = (Holder1) convertView.getTag();
						holder1.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
						holder1.iv_images = (ImageView) convertView.findViewById(R.id.iv_images);
						holder1.iv_imagess = (ImageView) convertView.findViewById(R.id.iv_imagess);
						holder1.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
						holder1.tvText = (TextView) convertView.findViewById(R.id.tvText);
						holder1.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
						holder1.tv_title.setText(news.title);
						holder1.tv_leixin.setBackgroundResource(R.color.white);
						if(TextUtils.isEmpty(news.getLinkUrl())){
							holder1.tv_leixin.setText(news.className);
						}else{
							holder1.tv_leixin.setText("");
							holder1.tv_leixin.setBackgroundResource(R.drawable.new_guangao);
						}
						holder1.tvText.setText("阅读　"+news.hits);
						holder1.iv_image.setVisibility(View.GONE);
						holder1.iv_images.setVisibility(View.GONE);
						holder1.iv_imagess.setVisibility(View.GONE);
						if(news.getNewItemInfoList() != null){
							if(news.getNewItemInfoList().size() == 1){
								holder1.iv_image.setVisibility(View.VISIBLE);
								mImageUtil.display(holder1.iv_image, news.getNewItemInfoList().get(0).getSmallImages());
							}else if(news.getNewItemInfoList().size() == 2){
								holder1.iv_image.setVisibility(View.VISIBLE);
								holder1.iv_images.setVisibility(View.VISIBLE);
								mImageUtil.display(holder1.iv_image, news.getNewItemInfoList().get(0).getSmallImages());
								mImageUtil.display(holder1.iv_images, news.getNewItemInfoList().get(1).getSmallImages());
							}else if(news.getNewItemInfoList().size() == 3){
								holder1.iv_image.setVisibility(View.VISIBLE);
								holder1.iv_images.setVisibility(View.VISIBLE);
								holder1.iv_imagess.setVisibility(View.VISIBLE);
								mImageUtil.display(holder1.iv_image, news.getNewItemInfoList().get(0).getSmallImages());
								mImageUtil.display(holder1.iv_images, news.getNewItemInfoList().get(1).getSmallImages());
								mImageUtil.display(holder1.iv_imagess, news.getNewItemInfoList().get(2).getSmallImages());
							}
						}
					break;
					case 2:
						holder2 = (Holder2) convertView.getTag();
						holder2.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
						holder2.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
						holder2.tvText = (TextView) convertView.findViewById(R.id.tvText);
						holder2.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
						holder2.tv_title.setText(news.title);
						holder2.tv_leixin.setBackgroundResource(R.color.white);
						if(news.getLinkUrl().equals("")){
							holder2.tv_leixin.setText(news.className);
						}else{
							holder2.tv_leixin.setText("");
							holder2.tv_leixin.setBackgroundResource(R.drawable.new_guangao);
						}
						holder2.tvText.setText("阅读　"+news.hits);
						mImageUtil.display(holder2.iv_image, news.getSmallImages());
						convertView.setTag(holder2);
						break;
				}
			}
		return convertView;
	}


	class Holder1{
		//资讯
		ImageView iv_image,iv_images,iv_imagess;
		TextView tv_title,tvText,tv_leixin;
	}
	class Holder2{
		//资讯
		ImageView iv_image;
		TextView tv_title,tvText,tv_leixin;
	}

	public BitmapUtils getBitmapUtils() {
		return mImageUtil;
	}
}
