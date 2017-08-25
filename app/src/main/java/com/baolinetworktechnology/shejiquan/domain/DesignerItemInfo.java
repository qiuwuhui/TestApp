package com.baolinetworktechnology.shejiquan.domain;

import android.content.Context;
import android.text.TextUtils;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.net.GetClassList;

import java.util.ArrayList;
import java.util.List;

public class DesignerItemInfo {
	public int id;// 设计师_ID
	public String guid;// 设计师GUID
	public String logo;// 头像
	public String name;// 设计师名称
	public String cityName;// 城市名
	public String desStyle; //擅长风格
	public String desStyleName; //擅长风格
	public String desArea;
	public int numComment;// 评论统计数量
	public String cost;
	public int numCase;// 案例数
	public int numOrder;
	public int isCertification;// 0未认证
	public int serviceLevel;
	public String designing;
	public ArrayList<DesignerCaseList> caseInfoList;


	public String getCost() {
		if (TextUtils.isEmpty(cost) || "不限".equals(cost))
			cost = "面议";
		String str=cost.substring(cost.length() - 2);
		if(str.equals("m2")|| str.equals("m²")){
			StringBuffer buffer=new StringBuffer();
			buffer.append(cost.substring(0,cost.length() - 2));
			buffer.append("㎡");
			cost=buffer.toString();
		}
		return cost;
	}
	private String SmallImages;
	/**
	 * _500_250.
	 *
	 * @param clip
	 * @return
	 */
	public String getSmallImages(String clip) {
		if (TextUtils.isEmpty(SmallImages)) {
			SmallImages = getSuffix(clip);
			return SmallImages;
		}
		return SmallImages;
	}

	private String getSuffix(String clip) {
		if (logo != null && logo.length() > 5) {
			String temp = logo.substring(logo.length() - 6);
			String[] tems = temp.split("\\.");
			if (tems != null && tems.length > 1) {
				String suffix = tems[1];

				return logo.replace("." + suffix, clip) + suffix;

			} else {
				return logo;
			}
		} else {
			return logo;
		}

	}
	public String getStrStyle(Context mContex) {
		List<CaseClass> list;
		if (SJQApp.getClassMap() != null) {
			list = SJQApp.getClassMap().getList("风格");
		} else {
			list = new GetClassList(mContex).getList("风格");
		}
		if (list == null)
			return "未填写";
		if (TextUtils.isEmpty(desStyle)) {
			return "未填写";
		} else {
			String[] s = desStyle.split(",");
			StringBuffer buffer = new StringBuffer();
			for (int j = 0; j < s.length; j++) {
				for (int i = 0; i < list.size(); i++) {
					if (Integer.valueOf(s[j]).intValue() == list.get(i).id) {
						if(j==s.length-1){
							buffer.append(list.get(i).title);
						}else{
							buffer.append(list.get(i).title).append("、");
						}
						break;
					}
				}
			}
			return buffer.toString();
		}
	}
}
