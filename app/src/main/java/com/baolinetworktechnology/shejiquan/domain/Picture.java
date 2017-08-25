package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

public class Picture {
	int ID;
	int RandHeight;
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getCaseTitle() {
		return CaseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		CaseTitle = caseTitle;
	}

	public String getPath() {
		return Path;
	}

	public void setPath(String path) {
		Path = path;
	}

	String GUID, Title, CaseTitle, Path;
	String SmallImages;

	public String getPath(String clip) {
		if (TextUtils.isEmpty(SmallImages)) {
			SmallImages = getSuffix(clip);
			return SmallImages;
		}
		return SmallImages;
	}
	
	
	private String getSuffix(String clip) {
		if (Path != null && Path.length() > 5) {
			String temp = Path.substring(Path.length() - 6);
			String[] tems = temp.split("\\.");
			if (tems != null && tems.length > 1) {
				String suffix = tems[1];

				return Path.replace("." + suffix, clip) + suffix;

			} else {
				return Path;
			}
		} else {
			return Path;
		}

	}

	public String getSmallImages() {
		// TODO Auto-generated method stub
		return getPath("_250_300.");
	}
}
