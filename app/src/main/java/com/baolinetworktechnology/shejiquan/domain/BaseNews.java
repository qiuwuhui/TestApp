package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

public class BaseNews {
	public int ID;// 资讯_ID
	public String Title, SubTitle, UserGUID;// 标题,小标题
	public int UserID;// 用户 ID

	public String CreateTime;// 创建 时间
	public int ClassID;// 分类 ID
	public String ClassTitle, MarkName;// 分类名称
	public String Descriptions;// 简介
	public String UserLogo, UserNickName, UserCityTitle;// 用户头像、昵称
	public float NumGood;// 平均分
	public int CountScore;// 评分人数
	public boolean IsFavorite;// 是否收藏
	public String GUID;
	public String WebUrl;// 微名片链接
	public int Hits, NumComment, NumDigg, NumBury, NumFavorite;// 查看 评论 推荐 反对数量
	public String Images;
	private String SmallImages;
	public String FromatDate;// 创建 时间 自定义格式

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
		if (Images != null && Images.length() > 5) {
			String temp = Images.substring(Images.length() - 6);
			String[] tems = temp.split("\\.");
			if (tems != null && tems.length > 1) {
				String suffix = tems[1];

				return Images.replace("." + suffix, clip) + suffix;

			} else {
				return Images;
			}
		} else {
			return Images;
		}

	}

	public void setSmallImages(String smallImages) {
		SmallImages = smallImages;
	}

	public int getHits() {
		if (Hits < 0)
			Hits = 0;
		return Hits;
	}

	public int getNumComment() {
		if (NumComment < 0)
			NumComment = 0;
		return NumComment;
	}

	public int getNumFavorite() {
		if (NumFavorite < 0)
			NumFavorite = 0;
		return NumFavorite;
	}
}
