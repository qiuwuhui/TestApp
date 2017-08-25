package com.baolinetworktechnology.shejiquan.domain;

public class CasezxGz {
	//商家ID
	public int ID;
	//商家GUID
	public String GUID;
	//商家名称
	public String EnterpriseName;
	//商家logo
	public String Logo;

	public int IsPay;
	//口碑值
	public int NumPraise;
	//案例数
	public int NumCase;
	//查阅次数
	public int NumView;
	//维度
	public String Gislat;
	//经度
	public String GisLng;
	
	public int getId() {
		return ID;
	}
	public void setId(int id) {
		this.ID = id;
	}
	public String getGUID() {
		return GUID;
	}
	public void setGUID(String gUID) {
		GUID = gUID;
	}
	public String getEnterpriseName() {
		return EnterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.EnterpriseName = enterpriseName;
	}
	public String getLogo() {
		return Logo;
	}
	public void setLogo(String logo) {
		this.Logo = logo;
	}
	public int getIsPay() {
		return IsPay;
	}
	public void setIsPay(int isPay) {
		this.IsPay = isPay;
	}
	public int getNumPraise() {
		return NumPraise;
	}
	public void setNumPraise(int numPraise) {
		this.NumPraise = numPraise;
	}
	public int getNumCase() {
		return NumCase;
	}
	public void setNumCase(int numCase) {
		this.NumCase = numCase;
	}
	public int getNumView() {
		return NumView;
	}
	public void setNumView(int numView) {
		this.NumView = numView;
	}
	public String getGisLat() {
		return Gislat;
	}
	public void setGisLat(String gisLat) {
		this.Gislat = gisLat;
	}
	public String getGisLng() {
		return GisLng;
	}
	public void setGisLng(String gisLng) {
		this.GisLng = gisLng;
	}
}
