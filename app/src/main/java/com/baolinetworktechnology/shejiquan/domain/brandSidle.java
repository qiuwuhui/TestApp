package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class brandSidle {
	public brandSidle() {
	}
	private int id;
	private String image;
	private String description;
	private int width;
	private int height;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
