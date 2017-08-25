package com.baolinetworktechnology.shejiquan.domain;

import com.baolinetworktechnology.shejiquan.constant.AppTag;

public class RecordDesigner {

	public Integer ID;

	public String UserGUID, CreateTime, UserName, VisitorGUID, Visitor;

	public int IsCertification;

	public String VisitorLogo;
	public String MarkName;

	public boolean isDesigner() {
		if (AppTag.MARKNAME_DESIGNER.equals(MarkName)) {
			return true;
		} else {
			return false;
		}

	}

	public RecordDesigner() {
	};

	public RecordDesigner(String name, String date) {
		this.UserName = name;
		this.CreateTime = date;

	};
}
