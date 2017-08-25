package com.baolinetworktechnology.shejiquan.domain;

public class GongSirz {
	int id;
	String guid;
	String logo;
	String enterpriseName;
	//认证时间 
	String certTime;
	//提交认证时间 
	String subCertificationTime;
	//认证状态
	int certificationStatus;
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
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getCertTime() {
		return certTime;
	}
	public void setCertTime(String certTime) {
		this.certTime = certTime;
	}
	public String getSubCertificationTime() {
		return subCertificationTime;
	}
	public void setSubCertificationTime(String subCertificationTime) {
		this.subCertificationTime = subCertificationTime;
	}
	public int getCertificationStatus() {
		return certificationStatus;
	}
	public void setCertificationStatus(int certificationStatus) {
		this.certificationStatus = certificationStatus;
	}
}
