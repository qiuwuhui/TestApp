package com.baolinetworktechnology.shejiquan.net;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 评论
 * 
 * @author JiSheng.Guo
 * 
 */
public class NetComment extends BaseNet {
	/**
	 * 评价对象类型的枚举
	 * 
	 * @author JiSheng.Guo
	 * 
	 */
	public interface ClassTypeEnum {
		/**
		 * 案例
		 */
		int CASE = 0;
		/**
		 * 博文
		 */
		int NEWS = 1;
		/**
		 * 产品
		 */
		int PRODUCTS = 2;
		/**
		 * 个人
		 */
		int PERSONAL = 4;
		/**
		 * 设计师
		 */
		int DESIGNER = 5;

	}

	public static String Title = "Title";
	public static String Contents = "Contents";
	public static String FromID = "FromID";
	public static String FromGuid = "FromGuid";
	public static String UserGuid = "UserGuid";
	public static String UserName = "UserName";
	public static String UserLogo = "UserLogo";
	public static String Star = "Star";
	public static String OrderGUID = "OrderGUID";
	/**
	 * 评价
	 */
	public static String Evaluate = "Evaluate";
	/**
	 * 评价类型
	 * 
	 * @see ClassTypeEnum
	 */
	public static String ClassType = "ClassType";

	/**
	 * 发表评价
	 */
	public void Send(final OnCallBack onCallBack, String... paramString) {

		String url = ApiUrl.COMMENT_SEND;
		RequestParams param = getParams(url, paramString);
		RequestCallBack<String> callBack = null;
		if (onCallBack != null) {
			callBack = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					onCallBack.onNetStart();
				}

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
				public void onFailure(HttpException arg0, String mesa) {
					onCallBack.onError(arg0, mesa);

				}
			};
		}
		getHttpUtils().send(HttpMethod.POST, ApiUrl.API + url, param, callBack);

	}
}
