package com.baolinetworktechnology.shejiquan.adapter;

import android.text.TextUtils;

public class UpFile {

	public UpFile() {
	}

	public UpFile(int resId) {
		this.resId = resId;
	}

	public UpFile(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String absolutePath;
	public int resId;
	public String webUrl;
	public int ID;
	public String GUID;
	public String CreateTime;
	public String UpdateTime;
	public int FileID;
	public String OrderGUID;
	public String Title;
	public String Path;

	public String getAbsolutePath() {
		if (TextUtils.isEmpty(absolutePath)) {
			if (!TextUtils.isEmpty(Path))
				return Path;
			return "";
		}
		return absolutePath;
	}
}
