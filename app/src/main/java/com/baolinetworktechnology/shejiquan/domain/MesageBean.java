package com.baolinetworktechnology.shejiquan.domain;

public class MesageBean {
	private String guid;
	private String title;
	private int state;
	private String time;
	private String isRead;
	private String ID;
	private String content,FromatDate;
	private String num,CreateTime,GUID;
	private String FromGuid;//来源GUID
	private String SkipType;//  来源类型 ｛CASE：案例，NEW：文章，VERIFY：身份审核｝

	public String getSkipType() {
		return SkipType;
	}

	public void setSkipType(String skipType) {
		SkipType = skipType;
	}

	public String getFromGuid() {
		return FromGuid;
	}

	public void setFromGuid(String fromGuid) {
		FromGuid = fromGuid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		content = content;
	}
	public String getFromatDate() {
		return FromatDate;
	}
	public void setFromatDate(String fromatDate) {
		FromatDate = fromatDate;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		isRead = isRead;
	}
	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public CharSequence getCreateTime() {
		// TODO Auto-generated method stub
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getGUID() {
		// TODO Auto-generated method stub
		return GUID;
	}
	
	

}
