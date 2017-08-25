package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.SwCase;

/**
 * 家装适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class OpusHomenAdapter extends CommonAdapter<SwCase> {
	public OpusHomenAdapter(Context context) {
		super(context, R.layout.item_case_home);
		setDefaultLoadingImage(R.drawable.zixun_tu);
	}

	@Override
	public void convert(ViewHolder holder, SwCase item) {
		holder.setText(R.id.tvTitle, item.title);
		holder.setText(R.id.tvTips, item.getTips());
		holder.setText(R.id.tvCost, "收藏　" + item.getNumFavorite());
		setImageViewUrl((ImageView) holder.getView(R.id.iv_image), item.getSmallImages());
	}

}
