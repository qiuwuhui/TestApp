package com.baolinetworktechnology.shejiquan;
import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.YouLikeList;
import com.baolinetworktechnology.shejiquan.domain.YouLikemode;
import com.baolinetworktechnology.shejiquan.fragment.MainHomeFragment;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class YouLikeAdapter extends BaseAdapter{
	List<YouLikemode> mData = new ArrayList<YouLikemode>();
	BitmapUtils mImageUtil;
	Context context;
	boolean ISshow;
	public YouLikeAdapter(Context context, boolean isShowLine) {
		this.context = context;
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
		// 设置图片压缩类型		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		YouLikemode item=mData.get(position);
		if(item.recommendType==0){
			return 0;
		}else{
			if(item.recomendData.getTypeID()==10){
				return 1;
			}else{
				return 2;
			}
		}
	}

	@Override
	public YouLikemode getItem(int position) {
		if (position < 0 || position >= mData.size())
			return null;
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public void setData(List<YouLikemode> data) {
		if(data.size()<3){
			return;
		}
		if (this.mData != null) {
			this.mData.clear();
		}
		if (data!=null && data.size()!=0 ) {
			this.mData.addAll(data);
//			if(SJQApp.user == null){
//			YouLikeList youLikeList=new YouLikeList();
//			youLikeList.type="3";
//				this.mData.add(2, youLikeList);
//			}
			notifyDataSetChanged();
		}
	}
//	public void addDenlu() {
//		if(this.mData.size()==0){
//			return;
//		}
//		YouLikeList youLikeList=new YouLikeList();
//		youLikeList.type="3";
//		this.mData.add(2, youLikeList);
//		notifyDataSetChanged();
//	}
//	public void DeletaDenlu() {
//		if(this.mData.size()==0){
//			return;
//		}
//		this.mData.remove(2);
//		notifyDataSetChanged();
//	}
	public void addData(List<YouLikemode> data) {
		if (data != null)
			this.mData.addAll(data);
		notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		     if(position>=6){
				 if(!ISshow){
					 ISshow=true;
					 Intent intent = new Intent();
					 intent.setAction("dingshow");
					 context.sendBroadcast(intent);
				 }
			 }
		Holder holder = null;
		Holder1 holder1 = null;
		Holder2 holder2 = null;
		YouLikemode item=mData.get(position);
		int ViewType =getItemViewType(position);
		if(convertView ==null){
			switch (ViewType){
				case 0:
					holder =new Holder();
					convertView = View.inflate(parent.getContext(),
							R.layout.anli_layout, null);
					holder.al_userLogo=(ImageView) convertView.findViewById(R.id.al_userLogo);
					holder.al_name=(TextView) convertView.findViewById(R.id.al_name);
					holder.al_zhajia=(TextView) convertView.findViewById(R.id.al_zhajia);
					holder.al_type=(TextView) convertView.findViewById(R.id.al_type);
					mImageUtil.display(holder.al_userLogo, item.recomendData.getSmallImages());
					holder.al_name.setText(item.recomendData.getTitle());
					holder.al_zhajia.setText(item.recomendData.getMcost());
					holder.al_type.setText("收藏 "+item.recomendData.getNumFavorite());
					convertView.setTag(holder);
					break;
				case 1:
					holder1 =new Holder1();
					convertView = View.inflate(parent.getContext(),
							R.layout.home_duo_image, null);
					holder1.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
					holder1.iv_images = (ImageView) convertView.findViewById(R.id.iv_images);
					holder1.iv_imagess = (ImageView) convertView.findViewById(R.id.iv_imagess);
					holder1.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
					holder1.tvText = (TextView) convertView.findViewById(R.id.tvText);
					holder1.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
					holder1.tv_title.setText(item.recomendData.getTitle());
					holder1.tv_leixin.setBackgroundResource(R.color.white);
					if(TextUtils.isEmpty(item.recomendData.getLinkUrl())){
						holder1.tv_leixin.setText(item.recomendData.getClassName());
					}else{
						holder1.tv_leixin.setText("");
						holder1.tv_leixin.setBackgroundResource(R.drawable.new_guangao);
					}
					holder1.tvText.setText("阅读 "+item.recomendData.getHits());
					if(item.recomendData.getNewsItemInfo() != null){
						if(item.recomendData.getNewsItemInfo().size() == 1){
							holder1.iv_image.setVisibility(View.VISIBLE);
							mImageUtil.display(holder1.iv_image, item.recomendData.getNewsItemInfo().get(0).getImages());
						}else if(item.recomendData.getNewsItemInfo().size() == 2){
							holder1.iv_image.setVisibility(View.VISIBLE);
							holder1.iv_images.setVisibility(View.VISIBLE);
							mImageUtil.display(holder1.iv_image,  item.recomendData.getNewsItemInfo().get(0).getImages());
							mImageUtil.display(holder1.iv_images, item.recomendData.getNewsItemInfo().get(1).getImages());
						}else if(item.recomendData.getNewsItemInfo().size() == 3){
							holder1.iv_image.setVisibility(View.VISIBLE);
							holder1.iv_images.setVisibility(View.VISIBLE);
							holder1.iv_imagess.setVisibility(View.VISIBLE);
							mImageUtil.display(holder1.iv_image,  item.recomendData.getNewsItemInfo().get(0).getImages());
							mImageUtil.display(holder1.iv_images, item.recomendData.getNewsItemInfo().get(1).getImages());
							mImageUtil.display(holder1.iv_imagess, item.recomendData.getNewsItemInfo().get(2).getImages());
						}
					}
					convertView.setTag(holder1);
					break;
				case 2:
					holder2 =new Holder2();
					convertView = View.inflate(parent.getContext(),
							R.layout.home_small_image, null);
					holder2.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
					holder2.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
					holder2.tvText = (TextView) convertView.findViewById(R.id.tvText);
					holder2.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
					holder2.tv_title.setText(item.recomendData.getTitle());
					holder2.tv_leixin.setBackgroundResource(R.color.white);
					if(TextUtils.isEmpty(item.recomendData.getLinkUrl())){
						holder2.tv_leixin.setText(item.recomendData.getClassName());
					}else{
						holder2.tv_leixin.setText("");
						holder2.tv_leixin.setBackgroundResource(R.drawable.new_guangao);
					}
					holder2.tvText.setText("阅读 "+item.recomendData.getHits());
					mImageUtil.display(holder2.iv_image, item.recomendData.getImages());
					convertView.setTag(holder2);
					break;
			   }
		}else{
			switch (ViewType){
				case 0:
					holder = (Holder) convertView.getTag();
					holder.al_userLogo=(ImageView) convertView.findViewById(R.id.al_userLogo);
					holder.al_name=(TextView) convertView.findViewById(R.id.al_name);
					holder.al_zhajia=(TextView) convertView.findViewById(R.id.al_zhajia);
					holder.al_type=(TextView) convertView.findViewById(R.id.al_type);
					mImageUtil.display(holder.al_userLogo, item.recomendData.getSmallImages());
					holder.al_name.setText(item.recomendData.getTitle());
					holder.al_zhajia.setText(item.recomendData.getMcost());
					holder.al_type.setText("收藏 "+item.recomendData.getNumFavorite());
					break;
				case 1:
					holder1 = (Holder1) convertView.getTag();
					holder1.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
					holder1.iv_images = (ImageView) convertView.findViewById(R.id.iv_images);
					holder1.iv_imagess = (ImageView) convertView.findViewById(R.id.iv_imagess);
					holder1.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
					holder1.tvText = (TextView) convertView.findViewById(R.id.tvText);
					holder1.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
					holder1.tv_title.setText(item.recomendData.getTitle());
					holder1.tv_leixin.setBackgroundResource(R.color.white);
					if(TextUtils.isEmpty(item.recomendData.getLinkUrl())){
						holder1.tv_leixin.setText(item.recomendData.getClassName());
					}else{
						holder1.tv_leixin.setText("");
						holder1.tv_leixin.setBackgroundResource(R.drawable.new_guangao);
					}
					holder1.tvText.setText("阅读 "+item.recomendData.getHits());
					if(item.recomendData.getNewsItemInfo() != null){
						if(item.recomendData.getNewsItemInfo().size() == 1){
							holder1.iv_image.setVisibility(View.VISIBLE);
							mImageUtil.display(holder1.iv_image, item.recomendData.getNewsItemInfo().get(0).getImages());
						}else if(item.recomendData.getNewsItemInfo().size() == 2){
							holder1.iv_image.setVisibility(View.VISIBLE);
							holder1.iv_images.setVisibility(View.VISIBLE);
							mImageUtil.display(holder1.iv_image,  item.recomendData.getNewsItemInfo().get(0).getImages());
							mImageUtil.display(holder1.iv_images, item.recomendData.getNewsItemInfo().get(1).getImages());
						}else if(item.recomendData.getNewsItemInfo().size() == 3){
							holder1.iv_image.setVisibility(View.VISIBLE);
							holder1.iv_images.setVisibility(View.VISIBLE);
							holder1.iv_imagess.setVisibility(View.VISIBLE);
							mImageUtil.display(holder1.iv_image,  item.recomendData.getNewsItemInfo().get(0).getImages());
							mImageUtil.display(holder1.iv_images, item.recomendData.getNewsItemInfo().get(1).getImages());
							mImageUtil.display(holder1.iv_imagess, item.recomendData.getNewsItemInfo().get(2).getImages());
						}
					}
					break;
				case 2:
					holder2 = (Holder2) convertView.getTag();
					holder2.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
					holder2.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
					holder2.tvText = (TextView) convertView.findViewById(R.id.tvText);
					holder2.tv_leixin	= (TextView) convertView.findViewById(R.id.tv_leixin);
					holder2.tv_title.setText(item.recomendData.getTitle());
					holder2.tv_leixin.setBackgroundResource(R.color.white);
					if(TextUtils.isEmpty(item.recomendData.getLinkUrl())){
						holder2.tv_leixin.setText(item.recomendData.getClassName());
					}else{
						holder2.tv_leixin.setText("");
						holder2.tv_leixin.setBackgroundResource(R.drawable.new_guangao);
					}
					holder2.tvText.setText("阅读 "+item.recomendData.getHits());
					mImageUtil.display(holder2.iv_image, item.recomendData.getSmallImages("_300_150."));
					break;
			}
		}
		return convertView;
	}
	class Holder {
		ImageView al_userLogo;
		TextView al_name,al_zhajia,al_type,gonglue_name;
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
	public void setISshow(){
		ISshow =false;
	}
}
