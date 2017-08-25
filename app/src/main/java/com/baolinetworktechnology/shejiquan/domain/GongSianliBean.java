package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;

public class GongSianliBean extends Bean{
	
	public List<anliBean> listData;
	//总条数
	int recordCount; 
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
}
