package com.baolinetworktechnology.shejiquan.domain;

import java.util.ArrayList;

public class GongSiBean extends Bean{
    //商家ID 
	int id;
	//商家名称
	String enterpriseName;
	//商家logo
	String logo;
	//省ID
	int provinceID;
	//市ID 
	int cityID;
	//公司简介
	String introduce;
	//案例数 
	int numCase;
	//
	
	//访客数 
	int numVisit;
	//地理纬度 
	String gisLat;
	//地理经度 
	String gisLng;	
	//是否认证(0=未认证，1=认证)
	int isCertification;
	//服务地区
	String areaRange;
	//公司地址
	String address;
	//聊天问候语
	String greetings;
	//公司案例列表
	ArrayList<caselist> caseList;
	//资质证书径数组 
	ArrayList <credentialList> credentialList;
	//成立日期 
	String establishTime;
	//注册资金 
	String registerFund;
	//登记机关
	String registerAuthority;
	
	String loginName;
	String nickName;
	String userLogo;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserLogo() {
		return userLogo;
	}
	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public int getProvinceID() {
		return provinceID;
	}
	public void setProvinceID(int provinceID) {
		this.provinceID = provinceID;
	}
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public int getNumCase() {
		return numCase;
	}
	public void setNumCase(int numCase) {
		this.numCase = numCase;
	}
	public int getNumVisit() {
		return numVisit;
	}
	public void setNumVisit(int numVisit) {
		this.numVisit = numVisit;
	}
	public String getGisLat() {
		return gisLat;
	}
	public void setGisLat(String gisLat) {
		this.gisLat = gisLat;
	}
	public String getGisLng() {
		return gisLng;
	}
	public void setGisLng(String gisLng) {
		this.gisLng = gisLng;
	}
	public int getIsCertification() {
		return isCertification;
	}
	public void setIsCertification(int isCertification) {
		this.isCertification = isCertification;
	}
	public String getAreaRange() {
		return areaRange;
	}
	public void setAreaRange(String areaRange) {
		this.areaRange = areaRange;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGreetings() {
		return greetings;
	}
	public void setGreetings(String greetings) {
		this.greetings = greetings;
	}
	public ArrayList<caselist> getCaseList() {
		return caseList;
	}
	public void setCaseList(ArrayList<caselist> caseList) {
		this.caseList = caseList;
	}
	public ArrayList<credentialList> getCredentialList() {
		return credentialList;
	}
	public void setCredentialList(ArrayList<credentialList> credentialList) {
		this.credentialList = credentialList;
	}
	public String getEstablishTime() {
		return establishTime;
	}
	public void setEstablishTime(String establishTime) {
		this.establishTime = establishTime;
	}
	public String getRegisterFund() {
		return registerFund;
	}
	public void setRegisterFund(String registerFund) {
		this.registerFund = registerFund;
	}
	public String getRegisterAuthority() {
		return registerAuthority;
	}
	public void setRegisterAuthority(String registerAuthority) {
		this.registerAuthority = registerAuthority;
	}
}
