package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;

import com.google.gson.Gson;

public class CaseClassList {
	public List<CaseClass> List;// 字典列表

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
