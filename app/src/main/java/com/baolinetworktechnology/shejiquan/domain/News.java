package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;

import com.google.gson.Gson;
/**
 * 资讯类
 * @author JiSheng.Guo
 *
 */
public class News extends BaseNews{
	
	public String Contents;// 文章内容
	public String EnterpriseLabel;// 企业称号
	public int UserCityID;// 用户城市ID
	public String Tags, Flags;
	public int CountBeWatch;
	public int TypeID;// 0.无，1.单图，2.多图，3.大图，4.专题
	public int ItemID;// 项目ID，10.头条，20.热点，30.经验，40.视频

	public int ClassParentID;// 父级分类ID
	
	public List<NewsItem> ItemList;
	public class NewsItem {
		public String CreateTime;// 创建时间
		public int ID;// 编号_ID
		public String GUID;// 编号 GUID
		public String Title;// 标题
		public String Path;// 路径
		public String Url;// 地址

	}
	
	@Override
	public String toString() {
		Gson gson=new Gson();
		return gson.toJson(this);
	}



}