package com.baolinetworktechnology.shejiquan.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.CaseBean;
import com.baolinetworktechnology.shejiquan.domain.CasePageList;
import com.baolinetworktechnology.shejiquan.domain.CaseZhuanxiu;
import com.baolinetworktechnology.shejiquan.domain.Casezx;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 获取案例
 * 
 * @author JiSheng.Guo
 * 
 */
public class NetCaseSearch extends BaseNet {
	private HttpUtils httpUtl;
	private HttpHandler<String> handler;

	public NetCaseSearch() {
		// 缓存2小时60*60*1000*2
		httpUtl = getHttpUtils();
	}

	public void SearchCase(final OnCallBackList<SwCase> callBack, String... strs) {
		cancel();
		String url = ApiUrl.CASELISET + getUrl(strs);
		RequestCallBack<String> onCallBack = null;
		RequestParams pams = getParams(url);
		if (callBack != null) {
			onCallBack = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					callBack.onNetStart();
					isLoading = true;
				}

				@Override
				public void onSuccess(ResponseInfo<String> n) {
					isLoading = false;
					CasePageList data = CommonUtils.getDomain(n, CasePageList.class);
					if (data == null) {
						if (callBack != null) {
							callBack.onNetError(n.result);
						}
						return;
					}
					if (!data.success) {
						if (callBack != null) {
							callBack.onNetFailure(data.error);
						}
					}

					if (callBack != null) {
						callBack.onNetSuccess(data.listData);
					}

				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					isLoading = false;
					if (callBack != null) {
						callBack.onNetFailure(arg1);
					}
				}
			};

		}

		handler = httpUtl.send(HttpMethod.GET, ApiUrl.API + url, pams,
				onCallBack);
	}
	public void SearchCase1(final OnCallBackList<Casezx> callBack, String... strs) {
		cancel();
		String url = ApiUrl.BUSINESS + getUrl(strs);
		RequestCallBack<String> onCallBack = null;
		RequestParams pams = getParams(url);
		if (callBack != null) {
			onCallBack = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					callBack.onNetStart();
					isLoading = true;
				}

				@Override
				public void onSuccess(ResponseInfo<String> n) {
					isLoading = false;
					String result=n.result;
					CaseZhuanxiu data=null;
					try {
						JSONObject json=new JSONObject(result);
						String result1=json.getString("result");
						Gson gson = new Gson();
						data = gson.fromJson(result1, CaseZhuanxiu.class);
					} catch (JSONException e) {
						e.printStackTrace();
					}
//					CaseZhuanxiu data = CommonUtils.getDomain(n, CaseZhuanxiu.class);
					if (data == null) {
						if (callBack != null) {
							callBack.onNetError(n.result);
						}
						return;
					}
					if (!data.success) {
						if (callBack != null) {
							callBack.onNetFailure(data.message);
						}
					}

					if (callBack != null) {
						callBack.onNetSuccess(data.listData);
					}

				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					isLoading = false;
					if (callBack != null) {
						callBack.onNetFailure(arg1);
					}
				}
			};

		}

		handler = httpUtl.send(HttpMethod.GET, ApiUrl.API + url, pams,
				onCallBack);

	}
	public boolean cancel() {
		isLoading = false;
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
