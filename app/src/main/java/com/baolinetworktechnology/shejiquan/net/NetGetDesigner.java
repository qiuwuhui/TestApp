package com.baolinetworktechnology.shejiquan.net;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetailBean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.lidroid.xutils.exception.HttpException;
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
public class NetGetDesigner extends BaseNet {

	public static String UserGUID = "UserGUID";
	public static String DesignerID = "DesignerID";

	/**
	 * 获取设计师
	 * 
	 * @param onCallBack
	 * @param param
	 */
	public NetGetDesigner(final OnCallBackBean<DesignerDetail> onCallBack,
			String... param) {
		String url = ApiUrl.GET_DESIGNER + getUrl(param)+"&R="+System.currentTimeMillis();
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
					onCallBack.onError(error, msg);
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					DesignerDetailBean data = CommonUtils.getDomain(
							responseInfo, DesignerDetailBean.class);
					if (data != null) {
						if (data.success) {
							onCallBack.onSuccess(data.data);
						} else {
							onCallBack.onFailure(data.message);
						}

					} else {
						onCallBack.onError(responseInfo.result);
					}
				}

			};
		}
		getHttpUtils(1000*60*10).send(HttpMethod.GET, ApiUrl.API + url, params,
				requestCall);
	}
}
