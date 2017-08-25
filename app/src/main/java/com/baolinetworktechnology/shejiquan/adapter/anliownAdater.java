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
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.GongSianliBean;
import com.baolinetworktechnology.shejiquan.domain.anliBean;
import com.lidroid.xutils.BitmapUtils;

public class anliownAdater extends BaseAdapter{
	private List<anliBean> anliList=new ArrayList<anliBean>();
	BitmapUtils mImageUtil;
	
	public anliownAdater(Context context) {
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
		
	}
	@Override
	public int getCount() {
		return anliList.size();
//		return messgaelist.size();
	}
	@Override
	public anliBean getItem(int position) {
		if (position < 0 || position >= anliList.size())
			return null;
		return anliList.get(position);
	}
	public void addDate(GongSianliBean bean) {
		if(bean==null)
		return;
		anliList.addAll(bean.listData);
		notifyDataSetChanged();
	}
	public void setDate(GongSianliBean bean) {
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
		Holder vh = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.anli_layout, null);
			vh = new Holder();
			vh.al_userLogo=(ImageView) convertView.findViewById(R.id.al_userLogo);
			vh.al_name=(TextView) convertView.findViewById(R.id.al_name);
			vh.al_zhajia=(TextView) convertView.findViewById(R.id.al_zhajia);
			vh.al_type=(TextView) convertView.findViewById(R.id.al_type);
			convertView.setTag(vh);
		} else {
			vh = (Holder) convertView.getTag();
		}
		anliBean anlibean=anliList.get(position);
		vh.al_name.setText(anlibean.getTitle());
		mImageUtil.display(vh.al_userLogo,anlibean.getSmallImages());
		if(anlibean.getClassID()==5){
			vh.al_zhajia.setText(anlibean.getMcost());			
		}else{
			vh.al_zhajia.setText(anlibean.getNumItem()+"图");
		}
		vh.al_type.setVisibility(View.INVISIBLE);
		return convertView;
	}
	
}

class Holder {
	ImageView al_userLogo;
	TextView al_name,al_zhajia,al_type;
}	
