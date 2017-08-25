package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

public class Casezx {
	//商家ID
	public int id;
	//商家GUID
	public String guid;
	//商家名称
	public String enterpriseName;
	//商家logo
	public String logo;

	public int isPay;
	//口碑值
	public int numPraise;
	//案例数
	public int numCase;
	//查阅次数
	public int numView;
	//维度
	public String gisLat;
	//经度
	public String gisLng;
	
	public String SmallImages;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGUID() {
		return guid;
	}
	public void setGUID(String gUID) {
		guid = gUID;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public int getIsPay() {
		return isPay;
	}
	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}
	public int getNumPraise() {
		return numPraise;
	}
	public void setNumPraise(int numPraise) {
		this.numPraise = numPraise;
	}
	public int getNumCase() {
		return numCase;
	}
	public void setNumCase(int numCase) {
		this.numCase = numCase;
	}
	public int getNumView() {
		return numView;
	}
	public void setNumView(int numView) {
		this.numView = numView;
	}
	public String getGisLat() {
		return gisLat;
	}
	public void setGisLat(String gisLat) {
		this.gisLat = gisLat;
	}
	public String getGisLng() {
		return gisLng;
	}
	public void setGisLng(String gisLng) {
		this.gisLng = gisLng;
	}
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
}
