package com.baolinetworktechnology.shejiquan.manage;

import java.util.ArrayList;

import android.content.Context;

import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.google.gson.Gson;
import com.guojisheng.koyview.domain.ChannelItem;

/**
 * 我的频道 管理者
 * 
 * @author JiSheng.Guo
 * 
 */
public class ChanneManage {
	/** 当前选中的栏目 */
	public ArrayList<ChannelItem> mOtherChannelList = new ArrayList<ChannelItem>();
	public ArrayList<ChannelItem> mUserChannelList = new ArrayList<ChannelItem>();
	// 注意，这里没有final
	private static ChanneManage mChanneManage;

	// 私有的默认构造子
	private ChanneManage(Context context) {

		initChannelList(context);
	}

	// 静态工厂方法
	public static ChanneManage getInstance(Context context) {
		if (mChanneManage == null) {
			mChanneManage = CommonUtils.getDomain(
					CacheUtils.getStringData(context, "channeManage2", null),
					ChanneManage.class);
			if (mChanneManage == null)
				mChanneManage = new ChanneManage(context);
		}
		return mChanneManage;
	}

	public void saveData(Context context) {
		Gson gson = new Gson();
		CacheUtils.cacheStringData(context, "channeManage2",
				gson.toJson(mChanneManage));
		LogUtils.i("频道管理数据保存", "ok");

	}

	/**
	 * 初始化Column栏目项
	 * 
	 * @return
	 * */
	public void initChannelList(Context context) {
		ChannelItem tab1 = new ChannelItem(143, "行业动态", false);
		ChannelItem tab2 = new ChannelItem(148, "设计艺术", true);
		ChannelItem tab3 = new ChannelItem(155, "家居生活", true);
		ChannelItem tab4 = new ChannelItem(162, "风水星座", true);
//		ChannelItem tab1 = new ChannelItem(20, "房产", false);
//		ChannelItem tab2 = new ChannelItem(143, "收房", true);
//		ChannelItem tab3 = new ChannelItem(155, "设计", true);
//		ChannelItem tab4 = new ChannelItem(162, "预算", true);
//		ChannelItem tab5 = new ChannelItem(148, "合同", true);
//		ChannelItem tab6 = new ChannelItem(448, "材料", true);
//		ChannelItem tab7 = new ChannelItem(143, "拆改", true);
//		ChannelItem tab8 = new ChannelItem(155, "水电", true);
//		ChannelItem tab9 = new ChannelItem(162, "防水", true);
//		ChannelItem tab10 = new ChannelItem(148, "泥瓦", true);
//		ChannelItem tab11 = new ChannelItem(448, "木工", true);
//		ChannelItem tab12 = new ChannelItem(143, "油漆", true);
//		ChannelItem tab13 = new ChannelItem(155, "收尾", true);
//		ChannelItem tab14 = new ChannelItem(162, "软装", true);
//		ChannelItem tab15 = new ChannelItem(148, "入驻", true);
//
		mUserChannelList.add(tab1);
		mUserChannelList.add(tab2);
		mUserChannelList.add(tab3);
		mUserChannelList.add(tab4);
//		mUserChannelList.add(tab5);
//		mUserChannelList.add(tab6);
//		mUserChannelList.add(tab7);
//		mUserChannelList.add(tab8);
//		mUserChannelList.add(tab9);
//		mUserChannelList.add(tab10);
//		mUserChannelList.add(tab11);
//		mUserChannelList.add(tab12);
//		mUserChannelList.add(tab13);
//		mUserChannelList.add(tab14);
//		mUserChannelList.add(tab15);
	}
}
