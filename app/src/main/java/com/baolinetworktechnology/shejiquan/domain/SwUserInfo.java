package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;

/**
 * 用户信息model
 * 
 * @author Administrator
 * 
 */

public class SwUserInfo {
	public int id;
	public String guid;
	public String markName;
	public String nickName;
	public boolean isBindEmail,isBindWeiXin,isBindQQ,isBindWeibo,isBindMobile;
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this).toString();
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

}
