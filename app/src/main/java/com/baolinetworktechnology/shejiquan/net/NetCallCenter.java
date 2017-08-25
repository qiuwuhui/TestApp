package com.baolinetworktechnology.shejiquan.net;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 拨打电话
 * 
 * @author JiSheng.Guo
 * 
 */
public class NetCallCenter extends BaseNet {
	/**
	 * 打电话的GUID
	 */
	public static String CallerGUID = "CallerGUID";
	/**
	 * 被呼叫的GUID
	 */
	public static String CalledGUID = "CalledGUID";
	/**
	 * 付费人的GUID（设计师的），不管是业主呼叫设计师还是设计师呼叫业主 付费的都是设计师
	 */
	public static String PayerGUID = "PayerGUID";

	/**
	 * 业主的联系电话
	 */
	public static String Mobile = "Mobile";

	/**
	 * 拨打电话
	 * @param mOnCallBack 回调器
	 * @param pas参数（必须双数）
	 */
	public NetCallCenter(final OnCallBack mOnCallBack, String... pas) {
		String url = ApiUrl.MakeCallCenter;
		RequestParams params = getParams(url, pas);
		
		RequestCallBack<String> callBack = null;
		if (mOnCallBack != null) {

			callBack = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					mOnCallBack.onNetStart();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					System.out.println("-->>"+msg);
					mOnCallBack.onError(error, msg);
					LogUtils.i("NetCallCenter", msg);
				}

				@Override
				public void onSuccess(ResponseInfo<String> n) {
					LogUtils.i("NetCallCenter", n.result);
					Bean bean = CommonUtils.getDomain(n, Bean.class);
					if (bean != null) {
						if (bean.success) {
							mOnCallBack.onSuccess(bean.message);
						} else {
							mOnCallBack.onFailure(bean.message);
						}
					} else {
						mOnCallBack.onError(n.result);
					}
				}
			};
		}
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);

	}
}
