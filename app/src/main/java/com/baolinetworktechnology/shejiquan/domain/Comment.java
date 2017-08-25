package com.baolinetworktechnology.shejiquan.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.ParseException;
import android.text.TextUtils;

/**
 * 评论
 * 
 * @author Administrator
 * 
 */
public class Comment {
	public int id;// 评论ID
	public int fromID;
	public String userGUID;
	public String Title;// 标题
	public String contents;// 详情
	public String userName;// 用户名
	public String userLogo;// 用户头像
	public String UserIP;// 用户IP地址
	public String createTime; // 评论时间
	private String markName;
	public int Star;// 评分
	public String Evaluate;// 评价
	private String FromatDate;// 格式化时间
	public int ClassType;//类型
	public String fromGUID;
	public boolean isAuthor;//是否是作者
	public ReplayUser replayUser;
	public RelatedClass replayInfo;
	private int isRead;
	
	public boolean isAuthor() {
		return isAuthor;
	}

	public void setAuthor(boolean isAuthor) {
		this.isAuthor = isAuthor;
	}

	public String getFromatDate() {
		if (TextUtils.isEmpty(FromatDate)) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			if (createTime == null) {
				createTime = str;
			}
			dateDiff(createTime, str, "yyyy-MM-dd HH:mm:ss");
		}
		return FromatDate;
	}
	public String timeData(String time){
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
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
	public String timeData1(String time){
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日");
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
						if (sec < 60) {
							sec = 0;
							FromatDate = "刚刚";
						}
//                        else {
//                            FromatDate = sec + "秒前";
//                        }
						return;
					}

					FromatDate = min + "分钟前";
					return;
				}
				FromatDate = hour + "小时以前";
			} else {
				if(day >=365){
					FromatDate = timeData(createTime);
				}else{
					FromatDate = timeData1(createTime);
				}

			}

		} catch (ParseException e) {
			e.printStackTrace();
			FromatDate = timeData(createTime);
		} catch (java.text.ParseException e) {
			FromatDate = timeData(createTime);
			e.printStackTrace();
		}
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFromID() {
		return fromID;
	}

	public void setFromID(int fromID) {
		this.fromID = fromID;
	}

	public String getUserGUID() {
		return userGUID;
	}

	public void setUserGUID(String userGUID) {
		this.userGUID = userGUID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
		if (userLogo != null && userLogo.length() > 5) {
			String temp = userLogo.substring(userLogo.length() - 6);
			String[] tems = temp.split("\\.");
			if (tems != null && tems.length > 1) {
				String suffix = tems[1];

				return userLogo.replace("." + suffix, clip) + suffix;

			} else {
				return userLogo;
			}
		} else {
			return userLogo;
		}

	}

	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}

	public String getUserIP() {
		return UserIP;
	}

	public void setUserIP(String userIP) {
		UserIP = userIP;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getStar() {
		return Star;
	}

	public void setStar(int star) {
		Star = star;
	}

	public String getEvaluate() {
		return Evaluate;
	}

	public void setEvaluate(String evaluate) {
		Evaluate = evaluate;
	}

	public int getClassType() {
		return ClassType;
	}

	public void setClassType(int classType) {
		ClassType = classType;
	}

	public ReplayUser getReplayUser() {
		return replayUser;
	}

	public void setReplayUser(ReplayUser replayUser) {
		this.replayUser = replayUser;
	}

	public RelatedClass getReplayInfo() {
		return replayInfo;
	}

	public void setReplayInfo(RelatedClass replayInfo) {
		this.replayInfo = replayInfo;
	}
	public String getMarkName() {
		return markName;
	}

	public void setMarkName(String markName) {
		this.markName = markName;
	}
	public String getFromGUID() {
		return fromGUID;
	}
	public void setFromGUID(String fromGUID) {
		this.fromGUID = fromGUID;
	}
	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
}
