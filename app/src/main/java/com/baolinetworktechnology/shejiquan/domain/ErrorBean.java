package com.baolinetworktechnology.shejiquan.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
/**
 * Api 错误记录
 * @author JiSheng.Guo
 *
 */
public class ErrorBean {
	String HttpMethod, HttpCode, NetworkType, Host, Client, IMEI, CreateTime,
			Device, PathAndQuery, MacAddress;

	public ErrorBean(String HttpCode, String HttpMethods, String PathAndQuery,
			String mIMEI, String mNetworkType, String mDevice,
			String mMacAddress) {
		this.HttpCode = HttpCode;
		this.HttpMethod = HttpMethods;
		this.PathAndQuery = PathAndQuery;
		this.Host = ApiUrl.API;
		this.CreateTime = getNowDate();
		this.IMEI = mIMEI;
		this.NetworkType = mNetworkType;
		this.Device = mDevice;
		this.MacAddress = mMacAddress;
		this.Client = "2004";

	}

	@SuppressLint("SimpleDateFormat")
	private String getNowDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	
		return df.format(new Date());
	}
}