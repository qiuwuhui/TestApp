package com.baolinetworktechnology.shejiquan.domain;

public class DataBean {
	private int id;
	private String title;
	private int parentID;
	private int parentLevels;
	private String value;
	private String initial;

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getParentID() {
		return parentID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public int getParentLevels() {
		return parentLevels;
	}

	public void setParentLevels(int parentLevels) {
		this.parentLevels = parentLevels;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
