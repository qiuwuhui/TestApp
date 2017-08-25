package com.baolinetworktechnology.shejiquan.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.net.GetClassList;
import com.google.gson.Gson;

public class DesignerInfo {
	public String Name;// 设计师名称
	public String Logo;// 头像
	public int ID;// 设计师_ID
	public String Sex;
	public String Cost, Mobile;
	public String GUID;// 设计师GUID
	private String Profession;// 职业身份
	public String DesStyle;
	public String StrStyle;
	public int ProvinceID, ServiceLevel;// 所在省份
	public int CityID;// 城市_ID
	public String FromCity;// 城市归属
	public String Distance;// 距离
	public String WorkCompany;// 就职公司
	public int NumCase, NumNew; // 案例数
	public int NumView;// 查阅次数
	public int NumVisit;// 访客
	public int NumFans; // 粉丝统计数量
	public int NumShare;//分享数
	public String Greetings;//问候语
	public ArrayList<DesignerCaseList> CaseList;
	public int NumComment;// 评论统计数量
	public int NumConcer; // 关注数
	public String NumFavorite;// 收藏数量

	public String GisLng;// 经度
	public String GisLat;// 纬度

	public float StarLevel;// 星级
	public int IsCertification;// 0未认证
	private int NumOrder = 0;

	public int getNumCase() {
		if (NumCase < 0)
			NumCase = 0;
		return NumCase;
	}

	public int getNumComment() {
		if (NumComment < 0)
			NumComment = 0;
		return NumComment;
	}
	public int getNumFans() {
		if (NumFans < 0)
			NumFans = 0;
		return NumFans;
	}
	public int getNumConcer() {
		if (NumConcer < 0)
			NumConcer = 0;
		return NumConcer;
	}

	public Integer getNumOrder() {
		if (NumOrder < 0)
			NumOrder = 0;

		return NumOrder;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this).toString();
	}

	public String getCost() {
		if (TextUtils.isEmpty(Cost) || "不限".equals(Cost))
			Cost = "面议";
		String str=Cost.substring(Cost.length() - 2);
		if(str.equals("m2")|| str.equals("m²")){
			StringBuffer buffer=new StringBuffer();
			buffer.append(Cost.substring(0,Cost.length() - 2));
			buffer.append("㎡");
			Cost=buffer.toString();
		}
		return Cost;
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

	/**
	 * _500_250.
	 *
	 * @return
	 */
	public String getSmallImages() {

		return getSmallImages("_500_250.");
	}

	private String getSuffix(String clip) {
		if (Logo != null && Logo.length() > 5) {
			String temp = Logo.substring(Logo.length() - 6);
			String[] tems = temp.split("\\.");
			if (tems != null && tems.length > 1) {
				String suffix = tems[1];

				return Logo.replace("." + suffix, clip) + suffix;

			} else {
				return Logo;
			}
		} else {
			return Logo;
		}

	}

	public String getProfession() {
		return Profession;
	}

	public void setProfession(String profession) {
		Profession = profession;
	}

	public int getServiceStatus() {
		return ServiceLevel;
	}

	public void setServiceStatus(int serviceStatus) {
		ServiceLevel = serviceStatus;
	}
	//	public String getStrStyle(Context context) {
//		List<CaseClass> list;
//		if (SJQApp.getClassMap() != null) {
//			list = SJQApp.getClassMap().getList("风格");
//		} else {
//			list = new GetClassList(context).getList("风格");
//		}
//		if (list == null)
//			return "未填写";
//		if (TextUtils.isEmpty(DesStyle)) {
//			return "未填写";
//		} else {
//			String[] s = DesStyle.split(",");
//			StringBuffer buffer = new StringBuffer();
//			for (int j = 0; j < s.length; j++) {
//				for (int i = 0; i < list.size(); i++) {
//					if (Integer.valueOf(s[j]).intValue() == list.get(i).ID) {
//						buffer.append(list.get(i).Title).append(" ");
//						break;
//					}
//				}
//			}
//			return buffer.toString();
//		}
//	}
	public void setServiceStatus(String serviceStatus) {
		if (!TextUtils.isEmpty(serviceStatus)) {
			try {
				ServiceLevel = Integer.parseInt(serviceStatus);
			} catch (Exception e) {
				ServiceLevel = 0;
			}

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
