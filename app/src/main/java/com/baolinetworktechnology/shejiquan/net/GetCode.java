package com.baolinetworktechnology.shejiquan.net;

import android.os.SystemClock;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 获取短信验证码
 * 
 * @author JiSheng.Guo
 * 
 */
public class GetCode extends BaseNet {

	/**
	 * 注册
	 */
	public static final String TEMPLE_SheJiQuanRegister = "SheJiQuanRegister";
	/**
	 * 登录
	 */
	public static final String TEMPLE_MLOGIN = "MLOGIN";
	/**
	 * 验证
	 */
	public static final String TEMPLE_SheJiQuanCode = "SheJiQuanCode";
	OnCallBack mOnCallBack;

	/**
	 * 获取短信验证码
	 * 
	 * @param onCallBack
	 * @param phone
	 * @param temple
	 */
	public GetCode(OnCallBack onCallBack, String phone, String temple) {
		mOnCallBack = onCallBack;
		String url = ApiUrl.SEND_SMS_CODE + "&template=" + temple + "&mobile="
				+ phone + "&r=" + SystemClock.currentThreadTimeMillis();
		System.out.println("-->>url=" + url);
		RequestCallBack<String> request = null;
		if (mOnCallBack != null) {
			request = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					mOnCallBack.onNetStart();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					mOnCallBack.onError(error, msg);
				}

				@Override
				public void onSuccess(ResponseInfo<String> n) {
					Bean bean = CommonUtils.getDomain(n, Bean.class);
					if (bean != null) {
						if (bean.success) {
							mOnCallBack.onSuccess("验证码已下发成功，注意查收");
						} else {
							mOnCallBack.onFailure(bean.message);
						}
					} else {
						mOnCallBack.onError(n.result);
					}
				}

			};
			new HttpUtils(15 * 1000).configRequestRetryCount(0).send(HttpMethod.GET, ApiUrl.API + url,
					getParams(url), request);
		}
	}
}
