package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.net.GetClassList;

public class Items {
	public Items() {
	}

	public String designerGuid;
	public int ID;
	public String path;
	public String title,CaseNum;
	public int numCase;
	public String parameter;
	public String url;
	public String designerId;
	public String fromGuid;
	public boolean isWatch;

	public String getDesignerID() {
		if (TextUtils.isEmpty(url)) {
			return "";
		}
		if (!TextUtils.isEmpty(designerId)) {
			return designerId;
		}
		if (url.indexOf("shop/des") != -1) {
			Pattern p = Pattern.compile("[1-9][0-9]{5,}");
			Matcher m = p.matcher(url);
			if (m.find()) {
				designerId = m.group();
				return designerId;
			}

		}
		return designerId;
	}
	public String getDesignerGuid() {
		return designerGuid;
	}

	public void setDesignerGuid(String designerGuid) {
		this.designerGuid = designerGuid;
	}
	public boolean isWatch() {
		return isWatch;
	}

	public void setWatch(boolean watch) {
		isWatch = watch;
	}
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getFromGuid() {
		return fromGuid;
	}

	public void setFromGuid(String fromGuid) {
		this.fromGuid = fromGuid;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String SmallImages;
	/**
	 * _500_250.
	 *
	 * @param clip
	 * @return
	 */
	public String getSmallImages(String clip) {
		if (TextUtils.isEmpty(SmallImages)) {
			SmallImages = getSuffix(clip);
			return SmallImages;
		}
		return SmallImages;
	}

	private String getSuffix(String clip) {
		if (path != null && path.length() > 5) {
			String temp = path.substring(path.length() - 6);
			String[] tems = temp.split("\\.");
			if (tems != null && tems.length > 1) {
				String suffix = tems[1];

				return path.replace("." + suffix, clip) + suffix;

			} else {
				return path;
			}
		} else {
			return path;
		}

	}
}
