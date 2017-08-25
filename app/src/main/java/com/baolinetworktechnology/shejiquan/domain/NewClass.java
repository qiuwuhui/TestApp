package com.baolinetworktechnology.shejiquan.domain;

public class NewClass {
	String Name;// "

	/**
	 * 
	 * @return 计设资讯标题
	 */
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	/**
	 * 计设资讯ClassId
	 * 
	 * @return int
	 */
	public int getValue() {
		return Value;
	}

	public void setValue(int value) {
		Value = value;
	}

	/**
	 * 计设资讯Icon Url
	 * 
	 * @return
	 */
	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}

	int Value;// （ClassID）
	String Text;// url 图标）

}
