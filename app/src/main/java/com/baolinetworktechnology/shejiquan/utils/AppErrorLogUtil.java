package com.baolinetworktechnology.shejiquan.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.ErrorBean;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class AppErrorLogUtil {
	public static AppErrorLogUtil INSTANCE;
	private String mNetworkType = "";
	private String mMacAddress = "";
	private String mIMEI = "";
	private String mDevice = "";

	private AppErrorLogUtil() {
	};

	private AppErrorLogUtil(Context context) {

		try {
			NetworkInfoUtils networkInfoUtils = new NetworkInfoUtils();
			mNetworkType = networkInfoUtils.getCurrentNetType(context);
			mMacAddress = networkInfoUtils
					.getLocalMacAddressFromWifiInfo(context);
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			mIMEI = telephonyManager.getDeviceId();
			mDevice = getHandSetInfo();
		} catch (Exception e) {
			// TODO: handle exception
		}

	};

	public static AppErrorLogUtil getErrorLog(Context context) {

		if (INSTANCE == null) {
			if (context == null) {
				return INSTANCE;
			}
			INSTANCE = new AppErrorLogUtil(context.getApplicationContext());
		}

		return INSTANCE;
	}

	public void postError(String HttpCode, String HttpMethods,
			String PathAndQuery) {
		String url = ApiUrl.ERROR_LOG;
		ErrorBean e1 = new ErrorBean(HttpCode, HttpMethods, PathAndQuery,
				mIMEI, mNetworkType, mDevice, mMacAddress);
		ArrayList<ErrorBean> list = new ArrayList<ErrorBean>();
		list.add(e1);

		Gson gs = new Gson();
		String js = gs.toJson(list);

		RequestParams params = getParams(url);
		HttpUtils httpUtils = new HttpUtils();

		try {
			params.setBodyEntity(new StringEntity(js, "utf-8"));
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		httpUtils.send(HttpMethod.POST, ApiUrl.API + ApiUrl.ERROR_LOG, params,
				null);
	}

	private String getHandSetInfo() {
		@SuppressWarnings("deprecation")
		String handSetInfo = "手机型号:" + android.os.Build.MODEL + ",SDK版本:"
				+ android.os.Build.VERSION.SDK + ",系统版本:"
				+ android.os.Build.VERSION.RELEASE;
		return handSetInfo;
	}

	/**
	 * 
	 * @param url
	 *            请求URL,不包含域名
	 * @return RequestParams
	 */
	public RequestParams getParams(String url) {
		RequestParams params = new RequestParams();
		params.setHeader("Token", null);
		params.setHeader("Version", "1.0");
		params.setHeader("AppAgent", "ANDROID_SHEJIQUAN_Ver.1.0");
		params.addHeader("Content-Type", "application/json");
		params.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
		return params;

	}

}
