package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.net.GetClassList;

public class CaseDesigner {
	String GUID;
	String DesignerName;
	String Logo;
	String AttrPattern;
	String AttrType;
	String AttrZxStyle;
	String AttrZxType;
	String NumCase;
	String NumFans;
	String Cost;
	String Mobile;
	String QQ;
	int ProvinceID;
	int CityID;
	int IsCertification;
	String ServiceStatus;
	String DesStyle;
	String Designing;
	public int ServiceLevel;
	public  String LoginName;//联系人标识
	public  String NickName;//联系人名字
	public  String UserLogo;//联系人头像
	public String getGUID() {
		return GUID;
	}
	public void setGUID(String gUID) {
		GUID = gUID;
	}
	public String getDesignerName() {
		return DesignerName;
	}
	public void setDesignerName(String designerName) {
		DesignerName = designerName;
	}
	public String getLogo() {
		return Logo;
	}
	public void setLogo(String logo) {
		Logo = logo;
	}
	public String getAttrPattern() {
		return AttrPattern;
	}
	public void setAttrPattern(String attrPattern) {
		AttrPattern = attrPattern;
	}
	public String getAttrType() {
		return AttrType;
	}
	public void setAttrType(String attrType) {
		AttrType = attrType;
	}
	public String getAttrZxStyle() {
		return AttrZxStyle;
	}
	public void setAttrZxStyle(String attrZxStyle) {
		AttrZxStyle = attrZxStyle;
	}
	public String getAttrZxType() {
		return AttrZxType;
	}
	public void setAttrZxType(String attrZxType) {
		AttrZxType = attrZxType;
	}
	public String getNumCase() {
		return NumCase;
	}
	public void setNumCase(String numCase) {
		NumCase = numCase;
	}
	public String getNumFans() {
		return NumFans;
	}
	public void setNumFans(String numFans) {
		NumFans = numFans;
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
	public int getProvinceID() {
		return ProvinceID;
	}
	public void setProvinceID(int provinceID) {
		ProvinceID = provinceID;
	}
	public int getCityID() {
		return CityID;
	}
	public void setCityID(int cityID) {
		CityID = cityID;
	}
	public int getIsCertification() {
		return IsCertification;
	}
	public void setIsCertification(int isCertification) {
		IsCertification = isCertification;
	}
	public String getServiceStatus() {
		return ServiceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		ServiceStatus = serviceStatus;
	}
	public String getDesStyle() {
		return DesStyle;
	}
	public void setDesStyle(String desStyle) {
		DesStyle = desStyle;
	}
	public String getDesigning() {
		return Designing;
	}
	public void setDesigning(String designing) {
		Designing = designing;
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
