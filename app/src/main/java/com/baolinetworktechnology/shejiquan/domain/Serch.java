package com.baolinetworktechnology.shejiquan.domain;

public class Serch {
	public Serch() {

	}

	public Serch(int type, String guid, String title,int id) {
		Type = type;
		Guid = guid;
		Title = title;
		ID=id;
	}

	public int Type;
	public String Guid;
	public String Title;
	public int ID;
}