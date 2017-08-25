package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;

/**
 * 用户信息model
 * 
 * @author Administrator
 * 
 */

public class UserInfo {
	public int id;
	public String guid;
	public String logo;//头像
	public String nickName;//名称
	public int userLevel;
	public int userScore;
	//PERSONAL 普通人 //NONE未选择  //DESIGNER设计师
	public String markName;//身份标识
	public String QQ, WeiXin, Mobile, Email;
	public String Token;
	public String UserName;
	public boolean isBindEmail,isBindWeiXin,isBindQQ,isBindWeibo,isBindMobile;
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this).toString();
	}

	public String getMobile() {
		// TODO Auto-generated method stub
		return Mobile;
	}
	public boolean isBindEmail() {
		return isBindEmail;
	}

	public void setBindEmail(boolean bindEmail) {
		isBindEmail = bindEmail;
	}

	public boolean isBindWeiXin() {
		return isBindWeiXin;
	}

	public void setBindWeiXin(boolean bindWeiXin) {
		isBindWeiXin = bindWeiXin;
	}

	public boolean isBindQQ() {
		return isBindQQ;
	}

	public void setBindQQ(boolean bindQQ) {
		isBindQQ = bindQQ;
	}

	public boolean isBindWeibo() {
		return isBindWeibo;
	}

	public void setBindWeibo(boolean bindWeibo) {
		isBindWeibo = bindWeibo;
	}

	public boolean isBindMobile() {
		return isBindMobile;
	}

	public void setBindMobile(boolean bindMobile) {
		isBindMobile = bindMobile;
	}
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
