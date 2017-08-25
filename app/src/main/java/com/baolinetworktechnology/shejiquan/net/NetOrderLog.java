package com.baolinetworktechnology.shejiquan.net;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.OrderLog;
import com.baolinetworktechnology.shejiquan.domain.OrderLogBean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 获取设计师/业主未读订单
 * 
 * @author JiSheng.Guo
 * 
 */
public class NetOrderLog extends BaseNet {
	
	public static String UserGuid="UserGuid";
	/**
	 * ReadType==Designer读的是设计师的未读订单信息，ReadType==Person读的是业主的订单信息
	 */
	public static String ReadType="ReadType";
	/**
	 * 设计师（值）
	 */
	public static String Designer="DESIGNER";
	/**
	 * 业主（值）
	 */
	public static String Person="PERSONAL";

	public void getHouseOrderLogList(final OnCallBackList<OrderLog> oncallBack,
			String... paramString) {

		String url = ApiUrl.GetLoggingList + getUrl(paramString);
		RequestParams param = getParams(url);
		LogUtils.i("getHouseOrderLogList", url);
		RequestCallBack<String> callBack = null;
		if (oncallBack != null) {
			callBack = new RequestCallBack<String>() {
				@Override
				public void onStart() {
					super.onStart();
					oncallBack.onNetStart();
				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					OrderLogBean bean = CommonUtils.getDomain(arg0,
							OrderLogBean.class);
					if (bean != null) {
						if (bean.success) {
							oncallBack.onNetSuccess(bean.data);
						} else {
							oncallBack.onNetFailure(bean.message);
						}
					} else {
						oncallBack.onNetError(arg0.result);
					}

				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					oncallBack.onNetError(arg0, arg1);
				}
			};
		}
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, param, callBack);

	}

	/**
	 * 更改系统未读信息为已读
	 */
	public void updateMessageRead(final OnCallBack onCallBack, String userGUID) {
		String url = ApiUrl.UpdateMessageRead + "UserGUID=" + userGUID;
		RequestCallBack<String> callBack = null;
		if (onCallBack != null) {
			callBack = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					Bean bean = CommonUtils.getDomain(arg0, Bean.class);
					if (bean != null) {
						if (bean.success) {
							onCallBack.onSuccess(bean.message);
						} else {
							onCallBack.onFailure(bean.message);
						}
					} else {
						onCallBack.onError(arg0.result);
					}
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {

				}
			};
		}
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);
	}
}
