package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

/**
 * 业主
 * 
 * @author JiSheng.Guo
 * 
 */
public class SwOwnerDetail extends SwaBean {
	private String guid;
	private String name;
	private String logo;
	private int sex=1;
	private int cityID;
	private int provinceID;
	private String qq;
	private String mobile;
	private String loveStyle;
	private String decorState;
	private int nProvinceID;
	private int nCityID;
	private String houseArea;
	private String budget;
	private String houseType;
	private String FromCity;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getQq() {
		if (TextUtils.isEmpty(qq)) {
			qq="";
		}
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMobile() {
		if (TextUtils.isEmpty(mobile)) {
			mobile="";
		}
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLoveStyle() {
		if (TextUtils.isEmpty(loveStyle)) {
			loveStyle="";
		}
		return loveStyle;
	}

	public void setLoveStyle(String loveStyle) {
		this.loveStyle = loveStyle;
	}

	public String getDecorState() {
		if (TextUtils.isEmpty(decorState)) {
			decorState="";
		}
		return decorState;
	}

	public void setDecorState(String decorState) {
		this.decorState = decorState;
	}

	public int getnProvinceID() {
		return nProvinceID;
	}

	public void setnProvinceID(int nProvinceID) {
		this.nProvinceID = nProvinceID;
	}

	public int getnCityID() {
		return nCityID;
	}

	public void setnCityID(int nCityID) {
		this.nCityID = nCityID;
	}

	public String getHouseArea() {
		if (TextUtils.isEmpty(houseArea)) {
			houseArea="";
		}
		return houseArea;
	}

	public void setHouseArea(String houseArea) {
		this.houseArea = houseArea;
	}

	public String getBudget() {
		if (TextUtils.isEmpty(budget)) {
			budget="";
		}
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getHouseType() {
		if (TextUtils.isEmpty(houseType)) {
			houseType="";
		}
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}
	public String getFromCity() {
		return FromCity;
	}

	public void setFromCity(String fromCity) {
		FromCity = fromCity;
	}
	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public int getProvinceID() {
		return provinceID;
	}
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
	public void setProvinceID(int provinceID) {
		this.provinceID = provinceID;
	}
	public String getSexTitle() {
		if (0 == sex) {
			return "女";
		} else {
			return "男";
		}

	}
}
