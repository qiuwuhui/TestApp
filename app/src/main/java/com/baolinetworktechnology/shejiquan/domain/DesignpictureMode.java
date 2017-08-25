package com.baolinetworktechnology.shejiquan.domain;

import java.io.Serializable;

public class DesignpictureMode implements Serializable{
	
		String Title;
		String Path;
		String CreateTime;
		public String getCreateTime() {
			return CreateTime;
		}
		public void setCreateTime(String createTime) {
			CreateTime = createTime;
		}
		public String getTitle() {
			return Title;
		}
		public void setTitle(String title) {
			Title = title;
		}
		public String getPath() {
			return Path;
		}
		public void setPath(String path) {
			Path = path;
		}
}
