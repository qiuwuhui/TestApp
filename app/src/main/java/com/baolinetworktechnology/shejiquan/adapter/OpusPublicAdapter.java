package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.view.CircleImg;

/**
 * 公装适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class OpusPublicAdapter extends CommonAdapter<SwCase> {

	public OpusPublicAdapter(Context context) {
		super(context, R.layout.item_case_public);
		setDefaultLoadingImage(R.drawable.defualt_trans);
	}

	@Override
	public void convert(ViewHolder holder, SwCase item) {
		holder.setText(R.id.tvTitle, item.title);// 标题
		holder.setText(R.id.tvTips, item.getTipsPublic());// 描述

		holder.setText(R.id.tv_hist, item.getHits() + "");// 查看数
		holder.setText(R.id.tv_comment, item.getNumComment() + "");// 评论数
		holder.setText(R.id.tv_favorat, "收藏 "+item.getNumFavorite());// 收藏数
		setImageViewUrl((ImageView)holder.getView(R.id.iv_image), item.getSmallImages());// 缩略图
	}

}
