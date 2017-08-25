package com.baolinetworktechnology.shejiquan.utils;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.interfaces.OnOAuthListener;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 授权登录
 * 
 * @author JiSheng.Guo
 * 
 */
public class OAuthHelper {
	private UMSocialService mController;
	private Context mContext;
	private UMQQSsoHandler mQQSsoHandler;
	private UMWXHandler mWeChatHandler;
	private OnOAuthListener mOnOAuthListener;
	private String unionid = "";
	private String openId = "";
	private String userGuid;
	private MyDialog dialog;

	public void setOnOAuthListener(OnOAuthListener onOAuthListener) {
		mOnOAuthListener = onOAuthListener;
	}

	public OAuthHelper(Activity act) {
		mContext = act;
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
//		 mQQSsoHandler = new UMQQSsoHandler(act, "1104396630",
//		 "mPmKmjx8vgI3JVaJ");// 测试
		mQQSsoHandler = new UMQQSsoHandler(act, "1104738591",
				"WeGlBcFR2Ncsfuj7");// 正式
		mWeChatHandler = new UMWXHandler(act, AppTag.WeChatAppID,
				AppTag.WeChatAppSecret);

		mWeChatHandler.addToSocialSDK();
		mQQSsoHandler.addToSocialSDK();

	}

	public boolean isWeChatInstalled() {
		return mWeChatHandler.isClientInstalled();
	}

	public boolean isQQInstalled() {
		try {
			return mQQSsoHandler.isClientInstalled();
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * QQ登录
	 */
	public void doOauthQQ() {
//		if (!isQQInstalled()) {
//			toast("您的手机尚未安装最新版的手机QQ");
//			return;
//		}
		mController.doOauthVerify(mContext, SHARE_MEDIA.QQ,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {// 授权开始
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onStart(platform);
						}
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {// 授权错误
						toast("授权错误");
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onError(e, platform);
						}
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {// 授权完成
						openId = value.getString("openid");
						doBind("1");
						// 获取相关授权信息
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onComplete(value, platform);
						}

						mController.getPlatformInfo(mContext, SHARE_MEDIA.QQ,
								new UMDataListener() {
									@Override
									public void onStart() {
										// toast("获取平台数据开始...");
									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										if (mOnOAuthListener != null) {
											info.put("openid", openId);
											mOnOAuthListener.onComplete(1,
													info);
										}
									//parsingData(status, info);
									}

								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						toast("授权取消");
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onCancel(platform);
						}

					}
				});
	}

	/**
	 * 新浪登录
	 */
	public void doOauthSina() {
		mController.doOauthVerify(mContext, SHARE_MEDIA.SINA,
				new UMAuthListener() {
					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onError(e, platform);
						}
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						openId = value.getString("uid");
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onComplete(value, platform);
						}
						if (value != null
								&& !TextUtils.isEmpty(value.getString("uid"))) {
							// toast("授权成功");
							getUserData();

						} else {
							// toast("授权失败");
						}
					}

					private void getUserData() {
						mController.getPlatformInfo(mContext, SHARE_MEDIA.SINA,
								new UMDataListener() {
									@Override
									public void onStart() {
									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										if (mOnOAuthListener != null) {
											info.put("openid", openId);
											mOnOAuthListener.onComplete(3,
													info);
										}

									}
								});

					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onCancel(platform);
						}
					}

					@Override
					public void onStart(SHARE_MEDIA platform) {
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onStart(platform);
						}
					}
				});

	}
	/**
	 * 微信登录
	 */
	public void doOauthWeChat() {
		if (!isWeChatInstalled()) {
			toast("您的手机尚未安装最新版的微信");
			if (mOnOAuthListener != null) {
				mOnOAuthListener.onError(null, null);
			}
			return;
		}
		mController.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						// 授权开始
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onStart(platform);
						}
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						// "授权错误"
						toast("授权错误");
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onError(e, platform);
						}
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						unionid =value.getString("unionid");
						openId =value.getString("openid");
						// 授权完成
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onComplete(value, platform);
						}
						// 获取相关授权信息
						mController.getPlatformInfo(mContext,
								SHARE_MEDIA.WEIXIN, new UMDataListener() {
									@Override
									public void onStart() {
										// 获取平台数据开始...
									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										if (mOnOAuthListener != null) {
											info.put("unionid", unionid);
											info.put("openid", openId);
											mOnOAuthListener.onComplete(2,
													info);
										}
									}
								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						toast("授权取消");
						if (mOnOAuthListener != null) {
							mOnOAuthListener.onCancel(platform);
						}
					}
				});
	}

	/**
	 * 删除新浪登录
	 */
	public void doDeleteSinaOauth() {
		mController.deleteOauth(mContext, SHARE_MEDIA.SINA,
				new SocializeClientListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onComplete(int status, SocializeEntity entity) {
//						if (status == 200) {
//							Toast.makeText(mContext, "取消成功.",
//									Toast.LENGTH_SHORT).show();
//						} else {
//							Toast.makeText(mContext, "取消失败", Toast.LENGTH_SHORT)
//									.show();
//						}
					}
				});
	}

	/**
	 * 删除微信登录
	 */
	public void doDeleteWeiChatOauth() {
		mController.deleteOauth(mContext, SHARE_MEDIA.WEIXIN, null);
	}

	/**
	 * 删除QQ登录
	 */
	public void doDeleteQQOauth() {
		mController.deleteOauth(mContext, SHARE_MEDIA.QQ,
				new SocializeClientListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onComplete(int status, SocializeEntity entity) {
//						if (status == 200) {
//							Toast.makeText(mContext, "取消成功.",
//									Toast.LENGTH_SHORT).show();
//						} else {
//							Toast.makeText(mContext, "取消失败", Toast.LENGTH_SHORT)
//									.show();
//						}
					}
				});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private void toast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	// private void parsingData(int status, Map<String, Object> info) {
	// if (status == 200 && info != null) {
	// StringBuilder sb = new StringBuilder();
	// Set<String> keys = info.keySet();
	// for (String key : keys) {
	// sb.append(key + "=" + info.get(key).toString() + "\r\n");
	// }
	// Log.d("TestData", sb.toString());
	// } else {
	// Log.d("TestData", "发生错误：" + status);
	// }
	// }

	boolean isBind = false;

	private void doBind(String MarkName) {
		if (!isBind) {
			return;
		}
		if (dialog == null) {
			dialog = new MyDialog(mContext);
			dialog.setCancelable(false);
		}
		dialog.show("正在绑定...");
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dialog.dismiss();
				isBind = false;
				Bean bean = CommonUtils.getDomain(arg0, Bean.class);
				if (bean != null) {
					toastShow(bean.message);
					if (bean.success) {

					} else {
					}
				} else {
					toastShow("绑定失败");
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				toastShow("网络请求失败");
				isBind = false;
				dialog.dismiss();
			}
		};
		String url = ApiUrl.AddUserThirdInfo;
		RequestParams params = getParams(url);
		params.addBodyParameter("OpenID", openId);
		params.addBodyParameter("UserGUID", userGuid);
		params.addBodyParameter("MarkName", MarkName);
		params.addBodyParameter("Client", AppTag.CLIENT);
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);
	}

	public HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		return httpUtil;

	}

	public RequestParams getParams(String url) {

		RequestParams params = new RequestParams();
		params.setHeader("Token", null);
		params.setHeader("Version", "1.0");
		params.setHeader("AppAgent", "ANDROID_SHEJIQUAN_Ver.1.0");
		params.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
		return params;

	}

	public void toastShow(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	private void initOauthData(String userGuid) {
		openId = "";
		this.userGuid = userGuid;
		isBind = true;

	}

	public void addOauthQQ(String userGuid) {
		initOauthData(userGuid);
		doOauthQQ();

	}

	public void addOauthWeChat(String userGUID2) {
		initOauthData(userGuid);
		doOauthWeChat();

	}
}
