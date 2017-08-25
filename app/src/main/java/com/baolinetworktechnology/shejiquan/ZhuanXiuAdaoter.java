package com.baolinetworktechnology.shejiquan;
import com.baolinetworktechnology.shejiquan.adapter.CommonAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewHolder;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.domain.Casezx;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import android.content.Context;
import android.text.Html;
public class ZhuanXiuAdaoter extends CommonAdapter<Casezx>{
	public ZhuanXiuAdaoter(Context context) {
		super(context, R.layout.zhuanxiu_layout);
		setDefaultLoadingImage(R.drawable.defualt_trans);
	}
	@Override
	public void convert(ViewHolder holder, Casezx item) {
		holder.setText(R.id.tvTitle, item.enterpriseName);
		setImageUrl(holder.getView(R.id.userLogo), item.getSmallImages("_100_100."));
		holder.setText1(R.id.anli,Html.fromHtml("<font font color='#000000'>设计方案： </font>"
				+ item.numCase+" "));
		holder.setText1(R.id.chayue, Html.fromHtml("<font font color='#000000'>咨询人数： </font>"
				+ item.numView+" "));
		if(item.gisLat!=null && item.gisLng!=null){
			if(item.gisLat.equals("") || item.gisLat.equals(""))
			 return;
			double lat_a = Double.valueOf(item.gisLat);
			double lng_a = Double.valueOf(item.gisLng);			
			if(lat_a>0 && lng_a>0 && SJQApp.location.geoLat>0 && SJQApp.location.geoLng>0){
				holder.setText(R.id.juli_tv,CommonUtils.gps2m(lat_a, lng_a, 
						SJQApp.location.geoLat, SJQApp.location.geoLng)+"㎞");
			}
		}
	}
}
