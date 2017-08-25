package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.CasePageList;
import com.baolinetworktechnology.shejiquan.domain.GongSianliBean;
import com.baolinetworktechnology.shejiquan.domain.SheJiShianliBean;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.domain.anliBean;
import com.baolinetworktechnology.shejiquan.domain.sjanliBean;
import com.lidroid.xutils.BitmapUtils;

public class SJanliownAdater extends BaseAdapter{
	private List<SwCase> anliList=new ArrayList<SwCase>();
	BitmapUtils mImageUtil;
	
	public SJanliownAdater(Context context) {
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
		
	}
	@Override
	public int getCount() {
		return anliList.size();
	}
	@Override
	public SwCase getItem(int position) {
		if (position < 0 || position >= anliList.size())
			return null;
		return anliList.get(position);
	}
	public void addDate(CasePageList bean) {
		if(bean==null)
		return;
		anliList.addAll(bean.listData);
		notifyDataSetChanged();
	}
	public void setDate(CasePageList bean) {
		if(bean==null)
			return;
		anliList.clear();
		anliList.addAll(bean.listData);
		notifyDataSetChanged();
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holde vh = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.anli_layout, null);
			vh = new Holde();
			vh.al_userLogo=(ImageView) convertView.findViewById(R.id.al_userLogo);
			vh.al_name=(TextView) convertView.findViewById(R.id.al_name);
			vh.al_zhajia=(TextView) convertView.findViewById(R.id.al_zhajia);
			vh.al_type=(TextView) convertView.findViewById(R.id.al_type);
			convertView.setTag(vh);
		} else {
			vh = (Holde) convertView.getTag();
		}
		SwCase anlibean=anliList.get(position);
		vh.al_name.setText(anlibean.title);
		mImageUtil.display(vh.al_userLogo,anlibean.getSmallImages());
//		if(anlibean.classID==5){
			vh.al_zhajia.setText(anlibean.getTips());
//		}else if(anlibean.classID==6){
//			vh.al_zhajia.setText(anlibean.NumItem+"图");
//		}
		vh.al_type.setVisibility(View.INVISIBLE);
		return convertView;
	}
	
}

class Holde {
	ImageView al_userLogo;
	TextView al_name,al_zhajia,al_type;
}	
