package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;

import android.text.TextUtils;

public class CaseClass {
	public int id;
	public String title;// 标题
	public String GUID;
	public String ClassName;// 类别名称
	public String Images;
	public int parentID;// 所属上级ID
	public int ParentLevels;// 父 等级
	public int orderNum;
	public boolean isCheck;
	public String StringId;
	public List<ClassChildren> Children;
	public String SmallImages;

	public class ClassChildren {
		public int ID;
		public String GUID;
		public String Title;// 标题
		public String ClassName;// 类别名称
		public String Images,Descriptions;
		public int ParentID;// 所属上级ID
		public int ParentLevels;// 父 等级
	}

	public CaseClass() {
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean check) {
		isCheck = check;
	}
	public CaseClass(int ID, String Title, String StringId) {
		this.id = ID;
		this.title = Title;
		this.ClassName = Title;
		this.StringId = StringId;
	}

	public CaseClass(int ID, String Title) {
		this.id = ID;
		this.StringId = ID + "";
		this.title = Title;
		this.ClassName = Title;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title;
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
}
