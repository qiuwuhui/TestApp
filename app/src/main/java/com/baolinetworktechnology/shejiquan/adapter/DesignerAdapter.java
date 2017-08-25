package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.DesignerInfo;
import com.baolinetworktechnology.shejiquan.domain.DesignerItemInfo;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.utils.DesignerHonorUtils;
import com.guojisheng.koyview.ExplosionField;

/**
 * 设计师列表适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class DesignerAdapter extends CommonAdapter<DesignerItemInfo> {
	public DesignerAdapter(Context contex) {
		super(contex, R.layout.item_designer);
		setDefaultLoadingImage(R.drawable.icon_morentxy);
		setDefaultLoadingImage1(R.drawable.zixun_tu);
	}

	@Override
	public void convert(ViewHolder holder, DesignerItemInfo item) {
		holder.setText(R.id.tvName, item.name);

		holder.setText(R.id.tvFromCity, item.cityName+"  |");
		if(item.getCost().equals("面议")){
			holder.setText(R.id.tvCost, "  "+item.getCost());
		}else{
			holder.setText(R.id.tvCost,"  "+"¥ "+item.getCost());
		}
		holder.setText(R.id.NumCase_tv, "共"+item.numCase+"套作品");
		holder.setText(R.id.tvNumOrder,"擅长风格：" +item.desStyleName);
		setImageUrl(holder.getView(R.id.designer_iv_head),
				item.getSmallImages("_300_300."));
		holder.getView(R.id.iv_layout).setVisibility(View.VISIBLE);
		holder.getView(R.id.iv1).setVisibility(View.VISIBLE);
		holder.getView(R.id.iv2).setVisibility(View.VISIBLE);
		holder.getView(R.id.item_Num).setVisibility(View.VISIBLE);
		if(item.caseInfoList==null || item.caseInfoList.size()==0){
			holder.getView(R.id.iv_layout).setVisibility(View.GONE);
		}else if(item.caseInfoList.size()==1){
			setImageUrl1(holder.getView(R.id.iv1), item.caseInfoList.get(0).getSmallImages("_280_140."));
			holder.getView(R.id.iv2).setVisibility(View.INVISIBLE);	
			holder.getView(R.id.item_Num).setVisibility(View.INVISIBLE);
		}else if(item.caseInfoList.size()==2){
			setImageUrl1(holder.getView(R.id.iv1), item.caseInfoList.get(0).getSmallImages("_280_140."));
			setImageUrl1(holder.getView(R.id.iv2), item.caseInfoList.get(1).getSmallImages("_280_140."));
			holder.getView(R.id.item_Num).setVisibility(View.INVISIBLE);
		}else if(item.caseInfoList.size()==3){
			setImageUrl1(holder.getView(R.id.iv1), item.caseInfoList.get(0).getSmallImages("_280_140."));
			setImageUrl1(holder.getView(R.id.iv2), item.caseInfoList.get(1).getSmallImages("_280_140."));
			setImageUrl1(holder.getView(R.id.iv3), item.caseInfoList.get(2).getSmallImages("_280_140."));
		}
	}
}
