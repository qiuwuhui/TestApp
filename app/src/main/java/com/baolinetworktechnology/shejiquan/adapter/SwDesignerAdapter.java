package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.DesignerItemInfo;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.guojisheng.koyview.ExplosionField;

/**
 * 我关注设计师列表适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class SwDesignerAdapter extends CommonAdapter<DesignerItemInfo> {
	private ExplosionField mExplosionField;
//	private DesignerHonorUtils honorUtils;
	private Context mContex;
	public SwDesignerAdapter(Context contex) {
		super(contex, R.layout.sw_item_designer);
		this.mContex=contex;
		setDefaultLoadingImage(R.drawable.icon_morentxy);
		setDefaultLoadingImage1(R.drawable.zixun_tu);
//		honorUtils = new DesignerHonorUtils();
	}

	@Override
	public void convert(ViewHolder holder, DesignerItemInfo item) {
		holder.setText(R.id.tvName, item.name);
		holder.setText(R.id.tvFromCity, item.cityName);
		holder.setText(R.id.tvCost, item.getCost());
		holder.setText(R.id.tvNumOrder,"擅长风格：" +item.desStyleName);
		holder.setText(R.id.tvdesigning,item.designing);
		setImageUrl(holder.getView(R.id.designer_iv_head),
				item.getSmallImages("_80_80."));
	}
	public void setChangData(boolean isChange) {
	}
	public void setChangData(boolean is, ExplosionField mExplosionField) {
		this.mExplosionField = mExplosionField;
	}
	public void doCollect(View view){
//		mExplosionField.clear();
//		mExplosionField.explode(view);
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				notifyDataSetChanged();
			}
		};
		Handler handler = new Handler();
		handler.postDelayed(runnable, 800);
	}
	//获取设计师所在地区
	private String getfromCity(int CityID){
		CityService cityService = CityService.getInstance(mContex);
		if (cityService == null)
			return null;
		String shi=cityService.fromCityID(CityID);
		return shi;
	}
}
