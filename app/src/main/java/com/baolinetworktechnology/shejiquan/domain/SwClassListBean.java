package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 案例分类列表
 * 
 * @author JiSheng.Guo
 * 
 */
public class SwClassListBean extends Bean {
	public HashMap<String, List<CaseClass>> listHashMap;

	public HashMap<String, List<CaseClass>> getListHashMap() {
		return listHashMap;
	}

	public void setListHashMap(HashMap<String, List<CaseClass>> listHashMap) {
		this.listHashMap = listHashMap;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
