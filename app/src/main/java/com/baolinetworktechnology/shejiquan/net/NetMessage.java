package com.baolinetworktechnology.shejiquan.net;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.GongSiMyanliBean;
import com.baolinetworktechnology.shejiquan.domain.MesageBean;
import com.baolinetworktechnology.shejiquan.domain.MesageList;
import com.baolinetworktechnology.shejiquan.domain.ReadMessageBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class NetMessage extends BaseNet {
	/**
	 * 来源信息GUID
	 */
	public static String UserGuid = "userGuid";
	/**
	 * 订单GUID
	 */
	public static String OrderGuid = "OrderGuid";
	/**
	 * 消息类型
	 * 
	 * @see MessageType
	 */
	public static String MessageType = "messageType";

	/**
	 * 获取系统未读消息
	 */
	public void Search(final OnCallBackList<MesageBean> onCallBack,
			String... paramString) {
		String url = AppUrl.ORDER_GetMessageList + getUrl(paramString);
		LogUtils.i("NetMessage", url);
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
					MesageList bean = CommonUtils.getDomain(arg0,
							MesageList.class);
					System.out.println("-->>" + arg0.result);
					if (bean != null) {
						if (bean.success) {
							onCallBack.onNetSuccess(bean.listData);
						} else {
							onCallBack.onNetFailure(bean.message);
						}
					} else {
						onCallBack.onNetError(arg0.result);
					}

				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					System.out.println("-->>onFailure "
							+ arg0.getExceptionCode() + arg1);
					onCallBack.onNetError(arg0, arg1);
					LogUtils.i("NetMessage", arg1);
				}
			};
		}
		getHttpUtils().send(HttpMethod.GET, AppUrl.API + url, getParams(url),
				callBack);
	}

	/**
	 * 发送站内消息和发送短信
	 * 
	 * @param onCallBack
	 * @param userGuid
	 *            、消息来源、消息类型
	 */
	public void SendUserMessage(final String userGuid,
			final OnCallBack onCallBack, String... paramString) {
		String url = ApiUrl.SendUserMessage;
		RequestParams params = getParams(url, paramString);

		LogUtils.i("NetMessage", url);
		RequestCallBack<String> callBack = null;
		if (onCallBack != null) {

			callBack = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					onCallBack.onNetStart();
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					LogUtils.i("NetMessage", arg0.result);
					Bean bean = CommonUtils.getDomain(arg0, Bean.class);
					if (bean != null) {
						if (bean.success) {

							XinGe.senMessage("设计师已完成您的委托，请确认", userGuid);
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
					onCallBack.onError(arg0, arg1);
					LogUtils.i("NetMessage", arg1);
				}
			};
		}
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);
	}

	public static String order = "order";
	public static String message = "message";
	public static String MessageGUID = "MessageGUID";

	/**
	 * 更改，清空（订单、消息）的状态为删除状态 MessageGUID信息GUID UserGUID用户GUID（清空用）
	 * MessageType消息类型（order--订单，message--消息）
	 */
	public void updateMessageStatus(final OnCallBack onCallBack,
			String... paramString) {

		String url = ApiUrl.UpdateMessageStatus;
		RequestParams params = getParams(url, paramString);

		LogUtils.i("NetMessage", url);
		RequestCallBack<String> callBack = null;
		if (onCallBack != null) {
			callBack = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					onCallBack.onNetStart();
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					LogUtils.i("NetMessage", arg0.result);

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
					onCallBack.onError(arg0, arg1);
					LogUtils.i("NetMessage", arg1);
				}
			};
		}
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);

	}

	/**
	 * 新获取设计师/业主订单信息
	 * 
	 * @param onCall
	 * @param userGUid
	 */
	private void GetIsLoggerList(final OnCallBackBean<ReadMessageBean> onCall,
			String userGUid) {
		String url = ApiUrl.GetHouseOrderLoggerList + "UserGuid=" + userGUid
				+ "&r=" + System.currentTimeMillis();
		RequestParams params = getParams(url);
		RequestCallBack<String> callBack = null;
		if (onCall != null) {
			callBack = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					onCall.onNetStart();
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					ReadMessageBean bean = CommonUtils.getDomain(responseInfo,
							ReadMessageBean.class);
					if (bean != null) {
						if (bean.success) {
							onCall.onSuccess(bean);
						} else {
							onCall.onFailure(bean.message);
						}
					} else {
						onCall.onError(responseInfo.result);
					}
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					onCall.onError(arg0, arg1);
				}
			};
		}

		getHttpUtils(1000 * 10).send(HttpMethod.GET, ApiUrl.API + url, params,
				callBack);
	}

	/**
	 * 获取是否有未读消息/订单消息
	 * 
	 * @param onCall
	 * @param userGUid
	 */
	public void GetIsNoReadMessage(
			final OnCallBackBean<ReadMessageBean> onCall, String userGUid) {
		String url = AppUrl.GetIsNoReadMessage + "userGuid=" + userGUid;
		RequestParams params = getParams(url);
		RequestCallBack<String> callBack = null;
		if (onCall != null) {
			callBack = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					onCall.onNetStart();
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					ReadMessageBean bean = CommonUtils.getDomain(responseInfo,
							ReadMessageBean.class);
					if (bean != null) {
						if (bean.success) {
							onCall.onSuccess(bean);
						} else {
							onCall.onFailure(bean.message);
						}
					} else {
						onCall.onError(responseInfo.result);
					}
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					onCall.onError(arg0, arg1);
				}
			};
		}

		getHttpUtils(0).send(HttpMethod.GET, AppUrl.API + url, params,
				callBack);
	}

	/**
	 * 订单消息
	 */
	public static int IsOrder = 1;
	/**
	 * 系统消息
	 */
	public static int IsSystem = 2;
	/**
	 * 用户类型
	 */
	public static String UpdateType = "UpdateType";

	/**
	 * 
	 * @param onCallBack
	 * @param type
	 *            IsOrder、IsSystem订单消息或者系统消息
	 * @param paramString
	 *            UserGUID 设计师/业主的GUID、UpdateType
	 *            更改设计师的未读订单（UpdateType==Designer），更改业主的未读订单（UpdateType==Person）
	 */
	public void ChangeMeaseIsRead(final OnCallBack onCallBack, int type,
			String... paramString) {

		RequestCallBack<String> callBack = null;
		if (onCallBack != null) {
			callBack = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					onCallBack.onNetStart();
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					LogUtils.i("NetMessage", arg0.result);
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
					onCallBack.onError(arg0, arg1);
					LogUtils.i("NetMessage", arg1);
				}
			};
		}
		String url = null;
		if (type == IsOrder) {// POST
			url = ApiUrl.ChangeOrderIsRead;
			RequestParams params = getParams(url, paramString);
			getHttpUtils().send(HttpMethod.POST, ApiUrl.API + url, params,
					callBack);
		} else {// GET
			url = ApiUrl.UpdateMessageRead + getUrl(paramString);
			getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url,
					getParams(url), null);
		}

		LogUtils.i("NetMessage", url);

	}

	/**
	 * 更改所有为已读
	 * 
	 * @param onCallBack
	 * @param userGuid
	 */
	public void updateAllOrderIsRead(final OnCallBack onCallBack,
			String userGuid) {
		String url = ApiUrl.UpdateAllOrderIsRead + userGuid;
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				null);

	}
	/**
	 * 获取是否有未读消息/订单消息
	 *
	 * @param onCall
	 * @param userGUid
	 */
	public void GetSAVECONSULT(
			final OnCallBackBean<ReadMessageBean> onCall, String DesignGUid, String userGUid) {
		String url = ApiUrl.SAVECONSULT + "DesignerGuid=" + DesignGUid +"&UserGuid="+ userGUid;
		RequestParams params = getParams(url);
		RequestCallBack<String> callBack = null;
		if (onCall != null) {
			callBack = new RequestCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					onCall.onNetStart();
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {

					ReadMessageBean bean = null;
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					onCall.onError(arg0, arg1);
				}
			};
		}

		getHttpUtils(1000 * 10).send(HttpMethod.GET, ApiUrl.API + url, params,
				callBack);
	}
}
