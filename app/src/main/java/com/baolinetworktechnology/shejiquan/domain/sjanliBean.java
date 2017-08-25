package com.baolinetworktechnology.shejiquan.domain;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import android.text.TextUtils;

public class sjanliBean {
	public int id;
	public String GUID;
	public String images;
	public String size;
	public String budget;
	public String attrStyleName;
	public String attrHouseTypeName;
	public String title;
	public int classID;//5是家装,6是公装
	public int NumItem;//图片上
	String  Mcost;
	
	public String getMcost() {
		if (TextUtils.isEmpty(Mcost)) {
			StringBuffer sb = new StringBuffer();
			if (!TextUtils.isEmpty(attrStyleName)) {
				sb.append(attrStyleName+ " | ");
			}

			if (!TextUtils.isEmpty(attrHouseTypeName)) {
				sb.append(attrHouseTypeName + " | ");
			}

			if (!TextUtils.isEmpty(size)) {
				sb.append(size + "㎡  | ");
			}

			if (TextUtils.isEmpty(budget)||"面议".equals(budget)) {
				budget = "面议";
				sb.append(budget);
			}else{
				sb.append(budget + "万元");
			}
			Mcost = sb.toString();
		}
		return Mcost;
	}
//	public String getAttrStyle() {
//		List<CaseClass> list = SJQApp.getClassMap().getList("风格");;
//		if (list == null)
//			return "未填写";
//		if (TextUtils.isEmpty(AttrStyle)) {
//			return "未填写";
//		} else {
//			String[] s = AttrStyle.split(",");
//			StringBuffer buffer = new StringBuffer();
//			for (int j = 0; j < s.length; j++) {
//				for (int i = 0; i < list.size(); i++) {
//					Pattern pattern = Pattern.compile("[0-9]*");
//					Matcher isNum = pattern.matcher(s[j]);
//					if(!isNum.matches()){
//						buffer.append(s[j]);
//						break;
//					}else{
//						if (Integer.valueOf(s[j]).intValue() == list.get(i).id) {
//							buffer.append(list.get(i).title).append(" ");
//							break;
//						}
//					}
//				}
//			}
//			return buffer.toString();
//		 }
//	  }
//
//	public String getAttrHouseType() {
//		List<CaseClass> list = SJQApp.getClassMap().getList("户型");;
//		if (list == null)
//			return "未填写";
//		if (TextUtils.isEmpty(AttrHouseType)) {
//			return "未填写";
//		} else {
//			String[] s = AttrHouseType.split(",");
//			StringBuffer buffer = new StringBuffer();
//			for (int j = 0; j < s.length; j++) {
//				for (int i = 0; i < list.size(); i++) {
//					if (Integer.valueOf(s[j]).intValue() == list.get(i).id) {
//						buffer.append(list.get(i).title).append(" ");
//						break;
//					}
//				}
//			}
//			return buffer.toString();
//		 }
//	  }
}
