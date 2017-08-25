package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

public class anliBean {
	 //案例ID 
     int id;  
     //案例标题 ;
     String title;
     //案例缩略图 
     String images;
     //区域ID
     int areaID;
     //预算 
     String budget;
     //面积
     String size;
     //风格 
     String attrStyle;
     //户型
     String attrHouseType;
     
 	 String  Mcost;
 	 
 	 String SmallImages;
 	 
 	 int numItem;
 	 
 	 int classID;
 	
     public int getClassID() {
		return classID;
	}
	public void setClassID(int classID) {
		this.classID = classID;
	}
	public int getNumItem() {
		return numItem;
	}
	public void setNumItem(int numItem) {
		this.numItem = numItem;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public int getAreaID() {
		return areaID;
	}
	public void setAreaID(int areaID) {
		this.areaID = areaID;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getAttrStyle() {
		return attrStyle;
	}
	public void setAttrStyle(String attrStyle) {
		this.attrStyle = attrStyle;
	}
	public String getAttrHouseType() {
		return attrHouseType;
	}
	public void setAttrHouseType(String attrHouseType) {
		this.attrHouseType = attrHouseType;
	}
	public String getMcost() {
		if (TextUtils.isEmpty(Mcost)) {
			StringBuffer sb = new StringBuffer();
			if (!TextUtils.isEmpty(attrStyle)) {
				sb.append(attrStyle + " | ");
			}

			if (!TextUtils.isEmpty(attrHouseType)) {
				sb.append(attrHouseType + " | ");
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
	/**
	 * _500_250.
	 * 
	 * @return
	 */
	public String getSmallImages() {

		return getSmallImages("_500_250.");
	}
	public String getSmallImages(String clip) {
		if (TextUtils.isEmpty(SmallImages)) {
			SmallImages = getSuffix(clip);
			return SmallImages;
		}
		return SmallImages;
	}
	private String getSuffix(String clip) {
		if (images != null && images.length() > 5) {
			String temp = images.substring(images.length() - 6);
			String[] tems = temp.split("\\.");
			if (tems != null && tems.length > 1) {
				String suffix = tems[1];

				return images.replace("." + suffix, clip) + suffix;

			} else {
				return images;
			}
		} else {
			return images;
		}
	}
}
