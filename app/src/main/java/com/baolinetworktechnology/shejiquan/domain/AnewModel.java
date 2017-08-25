package com.baolinetworktechnology.shejiquan.domain;

public class AnewModel {
    private int id;
	private String guid;
	private String easeGuid;
	private String name;
	private int isHint;
	private String logo;

	//搜索好友
	private String markName;
	private String nickName;


	//1.未关注 //2.已关注
	private Boolean isWatch;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMarkName() {
		return markName;
	}

	public void setMarkName(String markName) {
		this.markName = markName;
	}
	public Boolean getWatch() {
		return isWatch;
	}

	public void setWatch(Boolean watch) {
		isWatch = watch;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getEaseGuid() {
		return easeGuid;
	}

	public void setEaseGuid(String easeGuid) {
		this.easeGuid = easeGuid;
	}

	public int getIsHint() {
		return isHint;
	}

	public void setIsHint(int isHint) {
		this.isHint = isHint;
	}
}
