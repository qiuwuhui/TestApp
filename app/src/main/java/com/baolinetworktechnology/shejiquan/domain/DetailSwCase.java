package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.List;

public class DetailSwCase extends  BaseBean{
	public int id;
	public String guid;
	public String userGUID;
	private String createTime;
	private String forMatCreateTime;
	public String title;
	private String images;
	public int classID;
	public String descriptions;
	private String attrHouseType;
	private String budget;
	private String size;
	private int hits;
	private String attrRoom;
	private int numGood;
	private String attrStyle;
	public int numFavorite;
	public int numComment;
	private String attrStyleName;
	private String attrHouseTypeName;
	public SwDesignerDetail designerInfo;
	public List<CaseItem> caseItemInfoList;

	//这个是面积拼接字段
	private String tips;
	//图片剪切
	private String SmallImages;
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public String getTips() {
		if (TextUtils.isEmpty(tips)) {
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
			tips = sb.toString();
		}
		return tips;
	}
	public int getNumFavorite() {
		if (numFavorite < 0)
			numFavorite = 0;
		return numFavorite;
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
	/**
	 * _500_250.
	 *
	 * @return
	 */
	public String getSmallImages() {

		return getSmallImages("_500_250.");
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
		if (numComment < 0)
			numComment = 0;
		return numComment;
	}
	public int getHits() {
		if (hits < 0)
			hits = 0;
		return hits;
	}
	public String getTipsPublic() {
//		if (TextUtils.isEmpty(tips)) {
//			StringBuffer sb = new StringBuffer();
//			if (!TextUtils.isEmpty(AttrRoomText)) {
//				sb.append(AttrRoomText + " / ");
//			}
//
//			sb.append(NumItem + "图");
//			tips = sb.toString();
//		}
		return tips;
	}
}
