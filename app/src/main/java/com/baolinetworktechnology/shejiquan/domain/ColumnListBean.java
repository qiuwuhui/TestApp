package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;
import com.guojisheng.koyview.domain.ChannelItem;

import java.util.List;

public class ColumnListBean {
	public List<ChannelItem> List;// 字典列表

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
