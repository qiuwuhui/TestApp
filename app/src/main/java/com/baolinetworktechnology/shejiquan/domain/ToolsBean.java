package com.baolinetworktechnology.shejiquan.domain;

public class ToolsBean {
	public String title;
	public String ImageUrl, ImageUrl2, webUrl;
	public int ImageID, ImageID2;
	public int ID;

	public ToolsBean() {
	}

	public ToolsBean(int ID, String title, int ImageID) {
		this(ID, title, ImageID, 0);
	}

	public ToolsBean(int ID, String title, int ImageID, String webUrl) {
		this(ID, title, ImageID, 0, webUrl);
	}

	public ToolsBean(int ID, String title, int ImageID, int ImageID2,
			String webUrl) {
		this.ID=ID;
		this.title = title;
		this.ImageID = ImageID;
		this.ImageID2 = ImageID2;
		this.webUrl = webUrl;
	}

	public ToolsBean(int ID, String title, int ImageID, int ImageID2) {
		this(ID, title, ImageID, 0, null);
	}
}
