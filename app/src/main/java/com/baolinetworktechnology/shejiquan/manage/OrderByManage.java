package com.baolinetworktechnology.shejiquan.manage;

import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.domain.CaseClass;

public class OrderByManage {

	public OrderByManage() {
	}

	public List<CaseClass> getListData() {
		CaseClass class1 = new CaseClass(0, "默认");
		CaseClass class2 = new CaseClass(11, "热门降序");
		CaseClass class3 = new CaseClass(12, "创建时间降序");
		CaseClass class4 = new CaseClass(13, "点击数降序");
		CaseClass class5 = new CaseClass(14, "评论数降序");
		CaseClass class6 = new CaseClass(15, "收藏数降序");
		CaseClass class7 = new CaseClass(16, "好评数降序");
		CaseClass class8 = new CaseClass(21, "排序号升序");
		List<CaseClass> paiXuData = new ArrayList<CaseClass>();
		paiXuData.add(class1);
		paiXuData.add(class2);
		paiXuData.add(class3);
		paiXuData.add(class4);
		paiXuData.add(class5);
		paiXuData.add(class6);
		paiXuData.add(class7);
		paiXuData.add(class8);
		return paiXuData;
	}

}
