package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;

import com.baolinetworktechnology.shejiquan.adapter.UpFile;

public class HouseOrderDetails extends HouseOrder{
	// 取消时间
	String CommentTime;
	
	// 平面布置图
	List<UpFile> ItemBuzhituList;
	// 效果图
	List<UpFile> ItemXiaoguotuList;
	// 施工图
	List<UpFile> ItemShigongtuList;
	// 设计师上传的文件初稿
	public List<UpFile> ItemChugaoList;

	public String getCommentTime() {
		return CommentTime;
	}

	public void setCommentTime(String commentTime) {
		CommentTime = commentTime;
	}

	public String getCommentRemark() {
		return CommentRemark;
	}

	public void setCommentRemark(String commentRemark) {
		CommentRemark = commentRemark;
	}
	public List<UpFile> getItemBuzhituLis() {
		return ItemBuzhituList;
	}

	public void setItemBuzhituLis(List<UpFile> itemBuzhituLis) {
		ItemBuzhituList = itemBuzhituLis;
	}

	public List<UpFile> getItemXiaoguotuList() {
		return ItemXiaoguotuList;
	}

	public void setItemXiaoguotuList(List<UpFile> itemXiaoguotuList) {
		ItemXiaoguotuList = itemXiaoguotuList;
	}

	public List<UpFile> getItemShigongtuList() {
		return ItemShigongtuList;
	}

	public void setItemShigongtuList(List<UpFile> itemShigongtuList) {
		ItemShigongtuList = itemShigongtuList;
	}

}
