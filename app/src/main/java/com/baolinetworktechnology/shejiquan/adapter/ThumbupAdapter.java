package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.MesageBean;
import com.baolinetworktechnology.shejiquan.domain.ThumbBean;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的消息 适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class ThumbupAdapter extends BaseAdapter {
	List<ThumbBean> data = new ArrayList<ThumbBean>();
	private BitmapUtils mImageUtil;
	public ThumbupAdapter(Context context) {
		mImageUtil = new BitmapUtils(context);
		//加载图片类
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public void setData(List<ThumbBean> data1) {
		this.data.clear();
		if (data1 != null)
			data.addAll(data1);
	}

	public void addData(List<ThumbBean> data1) {
		if (data1 != null)
			data.addAll(data1);
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder vh = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_thumbup, null);
			vh = new Holder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (Holder) convertView.getTag();
		}
		vh.setData(data.get(position));
		return convertView;
	}

	class Holder {
		CircleImg circle_image;
		TextView tv_title,tv_time,tv_text,tie_name;
		ImageView is_zuozhe;

		Holder(View view) {
			circle_image = (CircleImg) view.findViewById(R.id.circle_image);
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_text = (TextView) view.findViewById(R.id.tv_text);
			tie_name = (TextView) view.findViewById(R.id.tie_name);
			is_zuozhe = (ImageView) view.findViewById(R.id.is_zuozhe);
		}

		public void setData(ThumbBean bean) {

			mImageUtil.display(circle_image,bean.getSendUserLogo());
			tv_title.setText(bean.getSendUserName());
			tv_time.setText(bean.getFromatDate());
			tie_name.setVisibility(View.GONE);
			if(bean.getMarkName()==1){
				tie_name.setVisibility(View.VISIBLE);
				String Title="《"+bean.getPostsTitle()+"》";
				tie_name.setText(Title);
			}else{
				tv_text.setText("赞了您的朋友圈");
			}

//			tv_text.setText(Html.fromHtml("<font font color='#333333'>赞了您的帖子 </font>"
//					+ Title));

			is_zuozhe.setVisibility(View.GONE);
			if(bean.getIsRead()==0){
				is_zuozhe.setVisibility(View.VISIBLE);
			}else{
				is_zuozhe.setVisibility(View.GONE);
			}
		}

	}


}
