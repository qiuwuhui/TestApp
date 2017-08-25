package com.baolinetworktechnology.shejiquan.net;

import com.lidroid.xutils.exception.HttpException;

public interface OnCallBackBean<T> {
	void onNetStart();

	/**
	 * 发布成功
	 * 
	 * @param mesa
	 */
	void onSuccess(T bean);

	/**
	 * 发布失败
	 * 
	 * @param mesa
	 */
	void onFailure(String mesa);

	/**
	 * 请求错误
	 * 
	 * @param arg0
	 * @param mesa
	 */
	void onError(HttpException arg0, String mesa);

	/**
	 * 解析错误
	 * 
	 * @param json
	 */
	void onError(String json);
}
