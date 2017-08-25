package com.baolinetworktechnology.shejiquan.domain;

import java.io.Serializable;

/**
 * 城市信息
 * @author JiSheng.Guo
 *
 */
public class City implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 城市domain
	 */
	public int ID,CityID, ParentID, ParentLevels; // 城市id , 父类id, 父类等级

	public String Initial, Letter, Title; // 首字母, 拼音字母 , 中文名称

	@Override
	public String toString() {
		return "City{" +
				"ID=" + ID +
				", CityID=" + CityID +
				", ParentID=" + ParentID +
				", ParentLevels=" + ParentLevels +
				", Initial='" + Initial + '\'' +
				", Letter='" + Letter + '\'' +
				", Title='" + Title + '\'' +
				'}';
	}

	public City(int cityID, int parentID, int parentLevels, String initial,
				String letter, String title) {
		super();
		CityID = cityID;
		ParentID = parentID;
		ParentLevels = parentLevels;
		Initial = initial;
		Letter = letter;
		Title = title;
	}

}
