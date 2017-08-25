package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;

public class SortModel implements Serializable {
	private String friendGUID;
	private int fromID;
	private String logo;
	private String name;
	private int markStatus;
	private String markName;
	private String nickName;
	private String[] mobile;
	private boolean isConcern;  //1是ture 0是falsh
	private String remarkName;
	private String initial;//显示数据拼音的首字母

	public String getName() {
	  return name;
	}
	public String getshowName() {
		if(!TextUtils.isEmpty(remarkName)){
			return remarkName;
		}else if(!TextUtils.isEmpty(name)){
			return name;
		}else{
			return nickName;
		}
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return initial;
	}
	public void setSortLetters(String sortLetters) {
		this.initial = sortLetters;
	}
	public String getFriendGUID() {
		return friendGUID;
	}

	public void setFriendGUID(String friendGUID) {
		this.friendGUID = friendGUID;
	}

	public int getFromID() {
		return fromID;
	}

	public void setFromID(int fromID) {
		this.fromID = fromID;
	}

	public String getLogo() {
		return logo;
	}
	private String SmallImages;
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

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getMarkStatus() {
		return markStatus;
	}

	public void setMarkStatus(int markStatus) {
		this.markStatus = markStatus;
	}

	public String getMarkName() {
		return markName;
	}

	public void setMarkName(String markName) {
		this.markName = markName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public boolean isConcern() {
		return isConcern;
	}

	public void setConcern(boolean concern) {
		isConcern = concern;
	}
	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}
	public String[] getMobile() {
		return mobile;
	}

	public void setMobile(String[] mobile) {
		this.mobile = mobile;
	}
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this).toString();
	}
}
