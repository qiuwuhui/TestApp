package com.baolinetworktechnology.shejiquan.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SheJiShirz {
	int id;
	String guid;
	String logo;
	String realName;
	String cardNo;
	//认证时间 
	String createTime;
	//提交认证时间 
	String updateTime;
	//认证状态
	int isCertification;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getName() {
		return realName;
	}
	public void setName(String name) {
		this.realName = name;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCertTime() {
		return timeData(createTime);
	}
	public void setCertTime(String certTime) {
		this.createTime = certTime;
	}
	public String getSubCertificationTime() {
		return timeData(updateTime);
	}



	public void setSubCertificationTime(String isCertification) {
		this.updateTime = updateTime;
	}
	public int getCertificationStatus() {
		return isCertification;
	}
	public void setCertificationStatus(int isCertification) {
		this.isCertification = isCertification;
	}
	public String timeData(String time){
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =null;
		String Time="";
		try {
			date =format.parse(time);
			Time= sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return Time;
	}
}
