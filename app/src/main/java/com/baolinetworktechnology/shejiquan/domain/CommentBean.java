package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;

/**
 * 评论
 * 
 * @author Administrator
 * 
 */
public class CommentBean extends SwaBean{



	public List<Comment> listData;
	public int recordCount;
	public List<Comment> getListData() {
		return listData;
	}
	public int getRecordCount() {
		return recordCount;
	}
}
