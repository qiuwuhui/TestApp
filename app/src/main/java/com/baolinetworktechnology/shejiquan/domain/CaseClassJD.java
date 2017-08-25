package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

public class CaseClassJD {
	public int classID;
	public String img;
	public String className;
	public String SmallImages;
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
		if (img != null && img.length() > 5) {
			String temp = img.substring(img.length() - 6);
			String[] tems = temp.split("\\.");
			if (tems != null && tems.length > 1) {
				String suffix = tems[1];

				return img.replace("." + suffix, clip) + suffix;

			} else {
				return img;
			}
		} else {
			return img;
		}

	}
}
