package com.baolinetworktechnology.shejiquan.net;

import java.util.List;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;

/*
 * 
 */
public abstract class BaseNet {
	/**
	 * 分页索引
	 */
	public static String PageIndex = "currentPage";
	/**
	 * 分页大小
	 */
	public static String PageSize = "pageSize";
	public static String MarkStatus = "MarkStatus";
	public static String ExpectDesigner = "ExpectDesigner";
	public boolean isLoading = false;

	public interface OnCallBackList<T extends Object> {
		public void onNetStart();

		/**
		 * 发布成功
		 * 
		 * @param data
		 */
		public void onNetSuccess(List<T> data);

		/**
		 * 发布失败
		 * 
		 * @param mesa
		 */
		public void onNetFailure(String mesa);

		/**
		 * 请求错误
		 * 
		 * @param arg0
		 * @param mesa
		 */
		public void onNetError(HttpException arg0, String mesa);

		/**
		 * 解析错误
		 * 
		 * @param json
		 */
		public void onNetError(String json);
	}

	// abstract void setParams(String... param);

	protected RequestParams getParams(String url, String[] paramString) {
		RequestParams params = getParams(url);
		for (int i = 0; i < paramString.length; i += 2) {
			params.addBodyParameter(paramString[i], paramString[i + 1]);
			LogUtils.i(paramString[i], paramString[i + 1]);
		}
		return params;
	}

	/**
	 * GET 请求
	 * 
	 * @param paramString
	 * @return
	 */
	protected String getUrl(String[] paramString) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < paramString.length; i += 2) {
			if (i != 0) {
				sb.append("&");
			}
			sb.append(paramString[i] + "=");
			sb.append(paramString[i + 1]);
		}

		return sb.toString();
	}

	/**
	 * 默认请求超时8秒
	 * 
	 * @return HttpUtils
	 */
	public HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		return httpUtil;

	}

	/**
	 * 
	 * @param CacheExpiry
	 *            请求缓存时间
	 * @return HttpUtils
	 */
	public HttpUtils getHttpUtils(int CacheExpiry) {
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		httpUtil.configDefaultHttpCacheExpiry(CacheExpiry);
		return httpUtil;

	}

	/**
	 * 
	 * @param url
	 *            请求URL,不包含域名
	 * @return RequestParams
	 */
	public RequestParams getParams(String url) {

		RequestParams params = new RequestParams();
		if (SJQApp.user != null) {
			params.setHeader("Token", SJQApp.user.Token);
		} else {
			params.setHeader("Token", null);
		}
		params.setHeader("Version", ApiUrl.VERSION);
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		params.setHeader("Client", AppTag.CLIENT);
		params.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
		return params;

	}

}
