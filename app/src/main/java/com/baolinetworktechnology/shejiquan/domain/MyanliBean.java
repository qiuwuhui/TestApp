package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

public class MyanliBean {
	public int id;
	public String guid;
	public String images;
	public String title;
	//ClassID  5代表家装，6代表公装
	public int classID;
	//MarkStatus  案例状态 
	public int markStatus;
	// 案例状态名称 
	public String markStatusName;
	public String attrSize;
	public String attrBudget;
	public String attrStyle;
	public String attrHouseType;
	public String attrRoom;
	public int numItem;
	public int numShare;
	public int hits;
	public int numFavorite;
	public int numComment;
	public String Mcost;
	public String SmallImages;
	public String getMcost() {
		if (TextUtils.isEmpty(Mcost)) {
			StringBuffer sb = new StringBuffer();
			if (!TextUtils.isEmpty(attrStyle)) {
				sb.append(attrStyle + " | ");
			}

			if (!TextUtils.isEmpty(attrHouseType)) {
				sb.append(attrHouseType + " | ");
			}

			if (!TextUtils.isEmpty(attrSize)) {
				sb.append(attrSize + "㎡  | ");
			}

			if (TextUtils.isEmpty(attrBudget)||"面议".equals(attrBudget)) {
				attrBudget = "面议";
				sb.append(attrBudget);
			}else{
				sb.append(attrBudget + "万元");
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

		return getSmallImages("_600_600.");
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
	public int getNumComment() {
		return numComment;
	}

	public void setNumComment(int numComment) {
		this.numComment = numComment;
	}

	public int getNumFavorite() {
		return numFavorite;
	}

	public void setNumFavorite(int numFavorite) {
		this.numFavorite = numFavorite;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getNumShare() {
		return numShare;
	}

	public void setNumShare(int numShare) {
		numShare = numShare;
	}
	public String getImages() {
		return images;
	}
}
