package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;

import com.baolinetworktechnology.shejiquan.adapter.UpFile;

public class OrderDesignerFileList {
	//时间
	String Time;
	List<UpFile> List;
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public List<UpFile> getList() {
		return List;
	}
	public void setList(List<UpFile> list) {
		List = list;
	}
}
