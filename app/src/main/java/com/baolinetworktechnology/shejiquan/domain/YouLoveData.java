package com.baolinetworktechnology.shejiquan.domain;


import android.text.TextUtils;

import java.util.List;

public class YouLoveData {
	private int id;
	private String createTime;
	private String title;
	private String images;
	private String attrHouseTypeName;
	private String attrStyleName;
	private String size;
	private int numFavorite;


	//设计资讯  typeID =10取值 newsItemInfo
	private int typeID;
	private int hits;
	public String className;
	private String linkUrl;

	private List<newsItemInfo>  newsItemInfo;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getAttrHouseType() {
		return attrHouseTypeName;
	}

	public void setAttrHouseType(String attrHouseType) {
		this.attrHouseTypeName = attrHouseType;
	}

	public String getStyle() {
		return attrStyleName;
	}

	public void setStyle(String style) {
		this.attrStyleName = style;
	}

	public int getNumFavorite() {
		return numFavorite;
	}

	public void setNumFavorite(int numFavorite) {
		this.numFavorite = numFavorite;
	}

	public int getTypeID() {
		return typeID;
	}

	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public  List<newsItemInfo> getNewsItemInfo() {
		return this.newsItemInfo;
	}

	public String getMcost() {
		StringBuffer sb = new StringBuffer();
		if (!TextUtils.isEmpty(attrStyleName)) {
			sb.append(attrStyleName + " | ");
		}

		if (!TextUtils.isEmpty(attrHouseTypeName)) {
			sb.append(attrHouseTypeName + " | ");
		}

		if (!TextUtils.isEmpty(size)) {
			sb.append(size + "㎡  ");
		}
		if(TextUtils.isEmpty(sb.toString())){
			sb.append("暂无信息");
		}
		return sb.toString();
	}
	private String SmallImages;
	public String getSmallImages() {

		return getSmallImages("_700_600.");
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
	public	class  newsItemInfo{
		private String images;
		public String getImages() {
			return images;
		}
	}
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}