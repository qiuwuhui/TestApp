package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.net.GetClassList;
import com.google.gson.Gson;

public class Case extends BaseNews {
	@ID
	public int _id;
	public int TypeID;
	public String Contents;// 文章内容
	public int NumItem;
	public String WorkCompany,ServiceStatus;// 公司
	public int ServiceLevel;
	// public List<NewsItem> ItemList;
	public String ArrtTitle, HouseTitle, AttrType, AttrZxType, Size, Budget,
			AttrHouseType, AttrStyle, AttrRoomText,DesStyle;// 设计类型AttrRoomText空间类型
	public int NumCase, NumFans;
	private String tips;
	private String Cost;
	private String QQ, Mobile;
	public int CityID,ProvinceID,IsCertification;
	public CaseDesigner Designer;
	
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public String getTips() {
		if (TextUtils.isEmpty(tips)) {
			StringBuffer sb = new StringBuffer();
			if (!TextUtils.isEmpty(AttrStyle)) {
				sb.append(AttrStyle + " | ");
			}

			if (!TextUtils.isEmpty(AttrHouseType)) {
				sb.append(AttrHouseType + " | ");
			}

			if (!TextUtils.isEmpty(Size)) {
				sb.append(Size + "㎡  ");
			}

//			if (TextUtils.isEmpty(Budget)||"面议".equals(Budget)) {
//				Budget = "面议";
//				sb.append(Budget);
//			}else{
//				sb.append(Budget + "万元");
//			}
			tips = sb.toString();
		}
		return tips;
	}

	public String getTipsPublic() {
		if (TextUtils.isEmpty(tips)) {
			StringBuffer sb = new StringBuffer();
			if (!TextUtils.isEmpty(AttrRoomText)) {
				sb.append(AttrRoomText + " / ");
			}

			sb.append(NumItem + "图");
			tips = sb.toString();
		}
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getMyBudget() {
		if (TextUtils.isEmpty(Budget)||"面议".equals(Budget)) {
			Budget = "收藏 "+"0";
			return Budget;
		}
		//StringBuffer sb=new StringBuffer();
	//	sb.append(Budget);
		//sb.append();
		return "收藏 "+Budget;
	}

	public String getCost() {
		if (TextUtils.isEmpty(Cost) || "不限".equals(Cost))
			Cost = "面议";
		return Cost;
	}

	public void setCost(String cost) {
		Cost = cost;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
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
		if (TextUtils.isEmpty(DesStyle)) {
			return "未填写";
		} else {
			String[] s = DesStyle.split(",");
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
