package com.baolinetworktechnology.shejiquan;
import com.baolinetworktechnology.shejiquan.adapter.CommonAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewHolder;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.Casezx;
import com.baolinetworktechnology.shejiquan.domain.CasezxGz;

import android.content.Context;
import android.widget.ImageView;
public class ZhuanXiuGzAdaoter extends CommonAdapter<CasezxGz>{
	public ZhuanXiuGzAdaoter(Context context) {
		super(context, R.layout.zhuanxiu_layout);
		setDefaultLoadingImage(R.drawable.defualt_trans);
	}
	@Override
	public void convert(ViewHolder holder, CasezxGz item) {
		holder.setText(R.id.tvTitle, item.EnterpriseName);
		setImageUrl(holder.getView(R.id.userLogo), item.Logo);
		holder.setText(R.id.anli, "设计方案："+item.NumCase);
		holder.setText(R.id.chayue, "查阅数："+item.NumView);
	}
}
