package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

import java.util.List;

public class HouseCase{
	private List<HouseItem> casesItemList;
	private int id;
	private String guid;
	private String addTime;
	public String title;
	private String cityID;
	private String images;
	private int numItem;
	private int hits;
	private int numFavorite;
	private String attrStyle;
	private String attrPattern;
	private String attrHouseType;
	private int attrArea;

	//这个是面积拼接字段
	private String tips;

	public List<HouseItem> getCasesItemList() {
		return casesItemList;
	}

	public void setCasesItemList(List<HouseItem> casesItemList) {
		this.casesItemList = casesItemList;
	}
	public int getAttrArea() {
		return attrArea;
	}

	public void setAttrArea(int attrArea) {
		this.attrArea = attrArea;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public int getNumItem() {
		return numItem;
	}

	public void setNumItem(int numItem) {
		this.numItem = numItem;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getNumFavorite() {
		return numFavorite;
	}

	public void setNumFavorite(int numFavorite) {
		this.numFavorite = numFavorite;
	}

	public String getAttrStyle() {
		return attrStyle;
	}

	public void setAttrStyle(String attrStyle) {
		this.attrStyle = attrStyle;
	}

	public String getAttrPattern() {
		return attrPattern;
	}

	public void setAttrPattern(String attrPattern) {
		this.attrPattern = attrPattern;
	}

	public String getAttrHouseType() {
		return attrHouseType;
	}

	public void setAttrHouseType(String attrHouseType) {
		this.attrHouseType = attrHouseType;
	}
	public String getTips() {
		if (TextUtils.isEmpty(tips)) {
			StringBuffer sb = new StringBuffer();
			if (!TextUtils.isEmpty(attrStyle)) {
				sb.append(attrStyle + " | ");
			}

			if (!TextUtils.isEmpty(attrHouseType)) {
				sb.append(attrHouseType);
			}
			tips = sb.toString();
		}
		return tips;
	}
}
