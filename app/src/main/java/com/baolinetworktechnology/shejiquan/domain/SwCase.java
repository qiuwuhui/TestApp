package com.baolinetworktechnology.shejiquan.domain;

import android.content.Context;
import android.text.TextUtils;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.net.GetClassList;
import com.google.gson.Gson;

import java.util.List;

public class SwCase extends  BaseBean{
	public int id;
	public String guid;
	private String createTime;
	private String userGUID;
	public String title;
	private String descriptions;
	private String images;

	public int getNumGood() {
		return numGood;
	}

	public void setNumGood(int numGood) {
		this.numGood = numGood;
	}

	private int numFavorite;
	private int numComment;//评论数
	private int numGood;//点赞数
	private String size;
	private String attrHouseTypeName;

	public void setNumComment(int numComment) {
		this.numComment = numComment;
	}

	private String budget;
	private int hits;
	private String  attrStyleName;
	//这个是面积拼接字段
	private String tips;
	//图片剪切
	private String SmallImages;
	private boolean isFavorite;
	private boolean isGood;
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
	public void setNumFavorite(int numFavorite) {
		this.numFavorite = numFavorite;
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

		return getSmallImages("_700_600.");
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isGood() {
		return isGood;
	}

	public void setGood(boolean good) {
		isGood = good;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean favorite) {
		isFavorite = favorite;
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
	public String getImages() {
		return images;
	}
}
