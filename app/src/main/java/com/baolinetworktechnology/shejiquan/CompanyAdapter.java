package com.baolinetworktechnology.shejiquan;
import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.domain.CaseZhuanxiu;
import com.baolinetworktechnology.shejiquan.domain.Casezx;
import com.baolinetworktechnology.shejiquan.domain.GongSianliBean;
import com.baolinetworktechnology.shejiquan.domain.anliBean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CompanyAdapter extends BaseAdapter{
	private List<Casezx> zhuanxiuList=new ArrayList<Casezx>();
	BitmapUtils mImageUtil;
	
	public CompanyAdapter(Context context) {
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.icon_morentxs);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.icon_morentxs);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return zhuanxiuList.size();
	}
	public void addDate(CaseZhuanxiu bean) {
		if(bean.listData==null)
		return;
		zhuanxiuList.addAll(bean.listData);
		notifyDataSetChanged();
	}
	public void setDate(CaseZhuanxiu bean) {
		if(bean.listData==null)
			return;
		zhuanxiuList.clear();
		zhuanxiuList.addAll(bean.listData);
		notifyDataSetChanged();
	}
	@Override
	public Casezx getItem(int position) {
		if (position < 0 || position >= zhuanxiuList.size())
			return null;
		return zhuanxiuList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh=new ViewHolder();
			convertView = View.inflate(parent.getContext(),
					R.layout.company_layout, null);
			vh.userLogo= (ImageView) convertView.findViewById(R.id.userLogo);
			vh.tvTitle=(TextView) convertView.findViewById(R.id.tvTitle);
			vh.tv_numCase=(TextView) convertView.findViewById(R.id.tv_numCase);
			vh.tv_numView=(TextView) convertView.findViewById(R.id.tv_numView);
			vh.juli_tv=(TextView) convertView.findViewById(R.id.juli_tv);
			convertView.setTag(vh);
		}else{
			vh =(ViewHolder) convertView.getTag();
		}
		Casezx item=zhuanxiuList.get(position);
		
		mImageUtil.display(vh.userLogo, item.getSmallImages("_100_100."));
		vh.tvTitle.setText(item.enterpriseName);
		Html.fromHtml("<font font color='#000000'>设计方案： </font>"
				+ item.numCase);
		vh.tv_numCase.setText(Html.fromHtml("<font font color='#000000'>设计方案： </font>"
				+ item.numCase+" "));		
		vh.tv_numView.setText(Html.fromHtml("<font font color='#000000'>咨询人数： </font>"
				+ item.numView+" "));
		if(item.gisLat!=null && item.gisLng!=null){
			if(!item.gisLat.equals("") || !item.gisLat.equals("")){
				double lat_a = Double.valueOf(item.gisLat);
				double lng_a = Double.valueOf(item.gisLng);			
				if(lat_a>0 && lng_a>0 && SJQApp.location.geoLat>0 && SJQApp.location.geoLng>0){
					vh.juli_tv.setText(CommonUtils.gps2m(lat_a, lng_a, SJQApp.location.geoLat, SJQApp.location.geoLng)+"㎞");			
				}						
			}
		}
		return convertView;
	}
	class ViewHolder {
	 ImageView userLogo;
	 TextView tvTitle,tv_numCase,tv_numView,juli_tv;
	}
}
