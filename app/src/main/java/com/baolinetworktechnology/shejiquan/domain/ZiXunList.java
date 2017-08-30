package com.baolinetworktechnology.shejiquan.domain;

import android.net.ParseException;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ZiXunList {
	public resultBen result;
	public int id;
	public String guid;
	public String title;
	public int classID;
	public int typeID;
	public String className;
	public String author;
	public int hits;
	public int numComment;
	public int numGood;
	public int numFavorite;
	public String images;
	public String descriptions;
	public String contents;

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean delete) {
		isDelete = delete;
	}

	public String createTime;
	public String seoKeywords;
	public String seoDescription;
	private List<DesignerCaseList> newItemInfoList;
	private String linkUrl;
	private boolean isDelete;//是否选中删除
//	public DesignerCaseList newItemInfoList;

	private String FromatDate;// 格式化时间
	public String SmallImages;
	/**
	 * _500_250.
	 * 
	 * @param clip
	 * @return
	 */
	public String getSmallImages() {
		return getSmallImages("_500_300.");
	}
	public String getSmallImages(String clip) {
		if (TextUtils.isEmpty(SmallImages)) {
			SmallImages = getSuffix(clip);
			return SmallImages;
		}
		return SmallImages;
	}
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	private String getSuffix(String clip) {
		if (images != null && images.length() > 5) {
			String temp = images.substring(images.length() - 6);
			String[] tems = temp.split("\\.");
			if (tems != null && tems.length > 1) {
				String suffix = tems[1];

				return images.replace("." + suffix, clip) + suffix;

			} else {
				return images;
			}
		} else {
			return images;
		}

	}
	// 格式化时间
	public String getFromatDate() {
		if (TextUtils.isEmpty(FromatDate)) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			if (createTime == null) {
				createTime = str;
			}
			dateDiff(timeData(createTime), str, "yyyy-MM-dd HH:mm:ss");
		}
		return FromatDate;
	}
	public void dateDiff(String startTime, String endTime, String format) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long diff;
		try {
			// 获得两个时间的毫秒时间差异
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			long day = diff / nd;// 计算差多少天
			if (day == 0) {
				long hour = diff % nd / nh;// 计算差多少小时
				long min = diff % nd % nh / nm;// 计算差多少分钟
				if (hour < 0)
					hour = 1;
				if (min < 0)
					min = 1;
				if (hour == 0) {
					if (min == 0) {
						long sec = diff % nd % nh % nm / ns;// 计算差多少秒
						if (sec < 10) {
							sec = 0;
							FromatDate = "刚刚";
						} else {
							FromatDate = sec + "秒前";
						}
						return;
					}

					FromatDate = min + "分钟前";
					return;
				}
				FromatDate = hour + "小时" + min + "前";
			} else {
			   FromatDate = createTime;
//				FromatDate = createTime.substring(5);
			}

		} catch (ParseException e) {
			e.printStackTrace();
			FromatDate = createTime;
		} catch (java.text.ParseException e) {
			FromatDate = createTime;
			e.printStackTrace();
		}
	}
	public String timeData(String time){
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =null;
		String Time="";
		try {
			date =format.parse(time);
			Time= sdf.format(date);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return Time;
	}
	public List<DesignerCaseList> getNewItemInfoList() {
		return newItemInfoList;
	}

	public void setNewItemInfoList(List<DesignerCaseList> newItemInfoList) {
		this.newItemInfoList = newItemInfoList;
	}
}
