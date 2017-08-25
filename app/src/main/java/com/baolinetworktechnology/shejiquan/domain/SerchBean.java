package com.baolinetworktechnology.shejiquan.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;

public class SerchBean {
//	public static final int TYPE_DESIGNER = 0;
//	public static final int TYPE_CASE = 1;
//	public static final int TYPE_NEWS = 2;
//	private static SerchBean INSTANCE;
//	private List<Serch> data = new ArrayList<Serch>();
//	public List<Serch> getDataList(){
//		return data;
//		
//	}
//	public void addData(Serch serch) {
//		for (int i = 0; i < data.size(); i++) {
//			if (data.get(i).ID == serch.ID && data.get(i).Type == serch.Type) {
//				data.remove(i);
//				break;
//			}
//		}
//		data.add(0, serch);
//		if (data.size() >= 100) {
//			data.remove(99);
//		}
//	}
//
//	private SerchBean() {
//	}
//
//	public static SerchBean getInstance(Context context) {
//		if (INSTANCE == null) {
//			INSTANCE = CommonUtils.getDomain(
//					CacheUtils.getStringData(context, "Serch", ""),
//					SerchBean.class);
//			if (INSTANCE == null)
//				INSTANCE = new SerchBean();
//
//		}
//		return INSTANCE;
//	}
//
//	public void clean(Context context) {
//		if (INSTANCE != null) {
//			INSTANCE.data.clear();
//		}
//		CacheUtils.cacheStringData(context, "Serch", "");
//	}

}
