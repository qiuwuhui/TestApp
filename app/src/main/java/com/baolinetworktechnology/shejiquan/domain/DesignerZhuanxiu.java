package com.baolinetworktechnology.shejiquan.domain;

public class DesignerZhuanxiu{
	int ID;
	String GUID;
	//经度
	String GisLat;
	//纬度
	String GisLng;
	//绑定电话
	String LinkPhone;
	
	String LinkMobile;
	//公司地址
	String Address;
	//是否认证  1是认证  0未认证 2待审核
	int IsCertification;
	//省ID
	int ProvinceID;
	//市ID
	int CityID;
	//区ID
	int AreaID;
	//分享数
	int NumShare;
	//访问数
	int NumVisit;
	//关注数
	int NumRecommend;
	//装修公司名字
	String EnterpriseName;
	//装修公司图标
	String Logo;
	//服务区域名称
	String AreaRange;
	//服务区域ID
	String AreaIds;
	// 认证是否通过  0是未通过  1是通过 2待审核
	int CertificationStatus;
	//认证状态
	String renzheng;
	//问候语
	String Greetings;
	//粉丝数
	int FollowersCount;
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
	public String getGisLat() {
		return GisLat;
	}
	public void setGisLat(String gisLat) {
		GisLat = gisLat;
	}
	public String getGisLng() {
		return GisLng;
	}
	public void setGisLng(String gisLng) {
		GisLng = gisLng;
	}
	public String getLinkPhone() {
		return LinkPhone;
	}
	public void setLinkPhone(String linkPhone) {
		LinkPhone = linkPhone;
	}
	public String getLinkMobile() {
		return LinkMobile;
	}
	public void setLinkMobile(String linkMobile) {
		LinkMobile = linkMobile;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public int getIsCertification() {
		return IsCertification;
	}
	public void setIsCertification(int isCertification) {
		IsCertification = isCertification;
	}
	public int getProvinceID() {
		return ProvinceID;
	}
	public void setProvinceID(int provinceID) {
		ProvinceID = provinceID;
	}
	public int getCityID() {
		return CityID;
	}
	public void setCityID(int cityID) {
		CityID = cityID;
	}
	public int getAreaID() {
		return AreaID;
	}
	public void setAreaID(int areaID) {
		AreaID = areaID;
	}
	public int getNumShare() {
		return NumShare;
	}
	public void setNumShare(int numShare) {
		NumShare = numShare;
	}
	public int getNumVisit() {
		return NumVisit;
	}
	public void setNumVisit(int numVisit) {
		NumVisit = numVisit;
	}
	public int getNumRecommend() {
		return NumRecommend;
	}
	public void setNumRecommend(int numRecommend) {
		NumRecommend = numRecommend;
	}
	public String getEnterpriseName() {
		return EnterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		EnterpriseName = enterpriseName;
	}
	public String getLogo() {
		return Logo;
	}
	public void setLogo(String logo) {
		Logo = logo;
	}
	public String getAreaRange() {
		return AreaRange;
	}
	public void setAreaRange(String areaRange) {
		AreaRange = areaRange;
	}
	public String getAreaIds() {
		return AreaIds;
	}
	public void setAreaIds(String areaIds) {
		AreaIds = areaIds;
	}
	public int getCertificationStatus() {
		return CertificationStatus;
	}
	public void setCertificationStatus(int certificationStatus) {
		CertificationStatus = certificationStatus;
	}
	public String getRenzheng() {
		if(CertificationStatus==0){
			return renzheng="未认证";
		}else if(CertificationStatus==1){
			return renzheng="未认证";
		}else if(CertificationStatus==2){
			return renzheng="审核中";
		}else if(CertificationStatus==3){
			return renzheng="已认证";
		}
		return renzheng;
	}
	public String getGreetings() {
		return Greetings;
	}
	public void setGreetings(String greetings) {
		Greetings = greetings;
	}
	public int getFollowersCount() {
		return FollowersCount;
	}
	public void setFollowersCount(int followersCount) {
		FollowersCount = followersCount;
	}
}
