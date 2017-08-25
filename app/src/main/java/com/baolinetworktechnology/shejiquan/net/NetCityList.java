package com.baolinetworktechnology.shejiquan.net;

import android.content.Context;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.CityBean;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 获取已开通城市
 * 
 * @author JiSheng.Guo
 * 
 */
public class NetCityList extends BaseNet {

	/**
	 * 获取已开通城市
	 */
	public void getHotCityList(final OnCallBackList<City> onCallBack,
			final Context context) {
		// 缓存处理

		long preTime = CacheUtils.getLongData(context, "getHotCityListTime");
		long currTie = System.currentTimeMillis();

		long between = currTie - preTime;
		boolean isRefres = false;
		if (between > 60 * 60 * 24 * 7) {
			isRefres = true;// 需要刷新
		}
		String json = CacheUtils.getStringData(context, "getHotCityList", "");
		CityBean bean = CommonUtils.getDomain(json, CityBean.class);
		if (bean != null && bean.data != null && bean.data.size() != 0) {
			if (onCallBack != null)
				onCallBack.onNetSuccess(bean.data);
			if (!isRefres)
				return;
		}

		String url = ApiUrl.CITY_GET_HOT;
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				CacheUtils.cacheLongData(context, "getHotCityListTime",
						System.currentTimeMillis());
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				CityBean bean = CommonUtils.getDomain(arg0, CityBean.class);
				if (onCallBack != null && context != null) {
					if (bean != null) {
						onCallBack.onNetSuccess(bean.data);
						if (context != null)
							CacheUtils.cacheStringData(context,
									"getHotCityList", arg0.result);
					} else {
						onCallBack.onNetError(arg0.result);
					}
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (onCallBack != null)
					onCallBack.onNetFailure(arg1);

			}
		};
		getHttpUtils(1000*600).send(HttpMethod.GET, ApiUrl.API + url,
				getParams(url), callBack);
	}
}
