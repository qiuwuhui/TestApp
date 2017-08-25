package com.baolinetworktechnology.shejiquan.domain;

import java.util.ArrayList;

import com.google.gson.Gson;

public class WeiShopGv {
	public String title;
	public int id;
	public String imageUrl;
	public boolean isClick;
	public int imageId;
	public ArrayList<Integer> preImagesView;

	public WeiShopGv() {
	};

	public WeiShopGv(String title, int imageId, int... previewId) {
		preImagesView = new ArrayList<Integer>();
		this.title = title;
		this.imageId = imageId;
		for (int i = 0; i < previewId.length; i++) {
			preImagesView.add(previewId[i]);
		}
	};

	@Override
	public String toString() {
		Gson gson = new Gson();

		return gson.toJson(this);
	}
}
