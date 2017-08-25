package com.baolinetworktechnology.shejiquan.net;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.CaseBean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 获取设计师
 * 
 * @author JiSheng.Guo
 * 
 */
public class NetGetWeek extends BaseNet {

	public NetGetWeek() {
		httpUtl = getHttpUtils(60*60*1000*2);
	}

	/**
	 * 获取推荐到主页的每周精选
	 * @param onCallBack
	 * @param PageIndex
	 */
	public void getWeekNews(final OnCallBackList<Case> onCallBack,
			int PageIndex) {
		String url = ApiUrl.CASE_GET_WEEKLY + "&PageIndex=" + PageIndex+"&MarkName=DESIGNER";
		RequestParams params = getParams(url);
		RequestCallBack<String> requestCall = null;
		if (onCallBack != null) {
			requestCall = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					onCallBack.onNetStart();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					onCallBack.onNetError(error, msg);
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {

					CaseBean data = CommonUtils.getDomain(responseInfo,
							CaseBean.class);
					if (data != null) {
						if (data.success) {
							onCallBack.onNetSuccess(data.data);
						} else {
							onCallBack.onNetFailure(data.message);
						}

					} else {
						onCallBack.onNetError(responseInfo.result);
					}
				}

			};
		}
		handler = httpUtl.send(HttpMethod.GET, ApiUrl.API + url, params,
				requestCall);
	}

	private HttpHandler<String> handler;
	private HttpUtils httpUtl;

	public boolean cancel() {
		// 如果之前的线程没有完成
		if (handler != null && handler.getState() != HttpHandler.State.FAILURE
				&& handler.getState() != HttpHandler.State.SUCCESS
				&& handler.getState() != HttpHandler.State.CANCELLED) {
			// 关闭handler后 onStart()和onLoading()还是会执行
			handler.cancel();

			return true;
		} else {
			return false;
		}
	}
}
