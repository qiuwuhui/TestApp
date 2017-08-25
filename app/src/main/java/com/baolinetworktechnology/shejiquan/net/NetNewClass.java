package com.baolinetworktechnology.shejiquan.net;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.NewClass;
import com.baolinetworktechnology.shejiquan.domain.NewClassBean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 获取推荐的资讯类别
 * 
 * @author JiSheng.Guo
 * 
 */
public class NetNewClass extends BaseNet {

	public void getNewsClass(final OnCallBackList<NewClass> oncallBack) {
		String url = ApiUrl.NEWS_GET_CLASS;
		LogUtils.i("NetNewClass", url);
		RequestParams params = getParams(url);
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
					NewClassBean bean = CommonUtils.getDomain(arg0,
							NewClassBean.class);
					if (bean != null) {
						oncallBack.onNetSuccess(bean.data);
						oncallBack.onNetError(arg0.result);
					} else {
						//oncallBack.onNetError(arg0.result);
					}

				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					oncallBack.onNetError(arg0, arg1);

				}
			};
		}

		getHttpUtils().configDefaultHttpCacheExpiry(21600).send(HttpMethod.GET,
				ApiUrl.API + url, params, callBack);

	}
}
