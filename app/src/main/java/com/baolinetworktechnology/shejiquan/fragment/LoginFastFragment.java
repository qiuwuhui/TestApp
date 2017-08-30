package com.baolinetworktechnology.shejiquan.fragment;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.FindPasswordActivity;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.activity.OAuth1Activity;
import com.baolinetworktechnology.shejiquan.activity.SelectiDentityActivity;
import com.baolinetworktechnology.shejiquan.activity.WebActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Login;
import com.baolinetworktechnology.shejiquan.domain.ReadMessageBean;
import com.baolinetworktechnology.shejiquan.domain.SwUserInfo;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.UserInfo;
import com.baolinetworktechnology.shejiquan.interfaces.OnOAuthListener;
import com.baolinetworktechnology.shejiquan.net.NetMessage;
import com.baolinetworktechnology.shejiquan.net.OnCallBackBean;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.utils.OAuthHelper;
import com.baolinetworktechnology.shejiquan.view.Conde;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.google.gson.Gson;
import com.guojisheng.koyview.MyEditText;
import com.guojisheng.koyview.utls.LogUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.exception.SocializeException;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登陆-验证码登陆
 * 
 * @author JiSheng.Guo
 * 
 */
public class LoginFastFragment extends BaseFragment implements OnOAuthListener{

	private MyEditText mEtPhone, mEtCode;
	private MyDialog mDialog;
	private Conde mGetCode;
	private int SELECT_DENT_CODE = 50;
	private UserInfo userInfo;
	private OAuthHelper mOAuthHelper;
	private String strphone="";
	private String strcode="";
	String channel = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = View.inflate(getActivity(), R.layout.fragment_login_fast,
				null);
		initView(view);
		mOAuthHelper = new OAuthHelper(getActivity());
		mOAuthHelper.setOnOAuthListener(this);
		getQuDao();
		return view;
	}

	private void initView(View view) {
		mDialog = ((LoginActivity) getActivity()).mDialog;
		mEtPhone = (MyEditText) view.findViewById(R.id.et_phone);
		mEtPhone.setHintTextColor(getResources().getColor(R.color.shouqi));
		mEtCode = (MyEditText) view.findViewById(R.id.etCode);
		mEtCode.setHintTextColor(getResources().getColor(R.color.shouqi));
		TextView text= (TextView) view.findViewById(R.id.text);
		text.setOnClickListener(this);
		text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  //添加下划线
		mGetCode = (Conde) view.findViewById(R.id.btn_getCode);
		mGetCode.setConde();
		mGetCode.setOnClickListener(this);
		btnLogin = (Button) view.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		btnLogin.setClickable(false);
		btnLogin.setBackgroundResource(R.drawable.anniu_shape1);
		Button btnRegist= (Button) view.findViewById(R.id.btnRegist);
		btnRegist.setOnClickListener(this);
		view.findViewById(R.id.tvFastLogin).setOnClickListener(this);
		view.findViewById(R.id.oAuthQQ).setOnClickListener(this);
		view.findViewById(R.id.oAuthWX).setOnClickListener(this);
		view.findViewById(R.id.oAuthSina).setOnClickListener(this);
		view.findViewById(R.id.tv_forgetpassword).setOnClickListener(this);
		mEtPhone.SetButton(mGetCode);
		mEtPhone.addTextChangedListener(watcher);
		mEtCode.addTextChangedListener(watcher);
	}
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable arg0) {			
		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {		
		}
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {		
			strphone = mEtPhone.getText().toString();
			strcode = mEtCode.getText().toString();
			if(!strphone.equals("") && !strcode.equals("")){
				btnLogin.setClickable(true);
				btnLogin.setBackgroundResource(R.drawable.anniu_shape);
			}
			if(strphone.equals("") || strcode.equals("")){
				btnLogin.setClickable(false);
				btnLogin.setBackgroundResource(R.drawable.anniu_shape1);
			}
		}	    
	};
	@Override
	public void onResume() {
		super.onResume();
		// MobclickAgent.onPageStart("LoginFastFragment"); // 统计页面
		if (mEtPhone.getText().length() < 5)
			mEtPhone.setText(CacheUtils.getStringData(getActivity(), "phone",
					""));
		mGetCode.notifyCode();
	}

	public void onPause() {
		super.onPause();
		// MobclickAgent.onPageEnd("LoginFastFragment");
		mGetCode.waitCode();
	}

	@Override
	public void onClick(View v) {
		SJQApp app = (SJQApp) getActivity().getApplication();
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_getCode:
			doGetCode();
			break;
		case R.id.btnLogin:
			doCheckCode();				
			break;
		case R.id.text:
			Intent intent = new Intent(getActivity(), WebActivity.class);
			intent.putExtra(WebActivity.URL,"http://www.sjq315.com/themes/scheme/mobile.html");
			getActivity().startActivity(intent);
			break;
		case R.id.btnRegist:
			((LoginActivity) getActivity()).startRegisterActivity();
			break;
		case R.id.tvFastLogin:
			((LoginActivity) getActivity()).startFastFragment();
			break;
		case R.id.tv_forgetpassword:
			go2FindPaswd();
			break;
		case R.id.oAuthQQ:
			mOAuthHelper.doOauthQQ();
			fromWay=7;
			app.setFromWay(7);
			mDialog.setCancelable(false);
			break;
		case R.id.oAuthSina:
			mOAuthHelper.doOauthSina();
			fromWay=6;
			app.setFromWay(6);
			mDialog.setCancelable(false);
			break;
		case R.id.oAuthWX:
			mOAuthHelper.doOauthWeChat();
			fromWay=5;
			app.setFromWay(5);
			mDialog.setCancelable(true);
			break;
		}
	}
	/**
	 * 关闭输入法弹窗
	 */
	public void hideInput() {
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getActivity().getCurrentFocus() != null)
			inputMethodManager.hideSoftInputFromWindow(getActivity()
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

	}
	private void go2FindPaswd() {
		Intent intent = new Intent(getActivity(), FindPasswordActivity.class);
		getActivity().startActivity(intent);
	}

	private void doCheckCode() {

		String sPhone = mEtPhone.getText().toString();
		String code = mEtCode.getText().toString();
		if (!CommonUtils.checkNumber(sPhone)) {
			toastShow("请输入正确的手机号码");
//			mEtPhone.setError("请输入正确的手机号码");
			return;
		}
		if (code.length() < 4) {
			toastShow("请输入正确的验证码");
//			mEtCode.setError("请输入正确的验证码");
			return;
		}
		doCheckCode(sPhone, code);

	}

	private void doGetCode() {
		final String sPhone = mEtPhone.getText().toString().trim();
		if (!CommonUtils.checkNumber(sPhone)) {
			toastShow("请输入正确的手机号码");
//			mEtPhone.setError("请输入正确的手机号码");
			return;
		}
		if (TextUtils.isEmpty(sPhone)) {
			toastShow("请输入手机号码");
//			mEtPhone.setError("请输入手机号码");
			return;
		}
		CacheUtils.cacheStringData(getActivity(), "phone", sPhone);
		mDialog.show();
		mGetCode.start();
		long time = new Date().getTime();
		int  time1 = (int) (time/1000);
		String verify=AppTag.YAN_ZHEN+sPhone+time1;
		verify=MD5Util.getmd5String(verify);
		String url = AppUrl.SEND_SMS_CODE_MLOGIN+sPhone+"&verify="+verify+"&TimesTamp="+time1;
		getHttpUtils().send(HttpMethod.GET, AppUrl.API + url,getParams(url),
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						mDialog.dismiss();
						mGetCode.stop();
						toastShow("发送验证码失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						mDialog.dismiss();
						Gson gson = new Gson();
						SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
						if (bean != null) {
							if (bean.result.operatResult) {
						        toastShow(bean.result.operatMessage);
							}else{
								toastShow(bean.result.operatMessage);
								mGetCode.stop();
							}
						}
					}
				});
	}

	// 检查验证码是否正确
	private void doCheckCode(final String Mobile, String Captcha) {
		mDialog.show("登录中");
		hideInput();
		String url = AppUrl.MOBILE_LOGIN;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("mobile",Mobile);
			param.put("captcha", Captcha);
			String channel="android{"+android.os.Build.MODEL+"}";
			param.put("channel", channel);
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> n) {
				com.baolinetworktechnology.shejiquan.utils.LogUtils.i("login",
						n.result);
				mDialog.dismiss();
				JSONObject json;
				Login info=null;
				try {
					json = new JSONObject(n.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					info=gson.fromJson(result1, Login.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (info == null) {
					toastShow("服务器繁忙");
					return;
				}

				if (info.operatResult) {
					if (info.userInfo != null) {
						userInfo = info.userInfo;
						if (info.userInfo.markName.equals("NONE")) {// 表示
							// 该用户未注册过；进行身份设置
							Intent intent = new Intent(getActivity(),
									SelectiDentityActivity.class);
							intent.putExtra(AppTag.TAG_GUID, info.userInfo.guid);
							startActivityForResult(intent, SELECT_DENT_CODE);
							return;
						}
						toastShow(info.operatMessage);
						saveData();
					}

				} else {
					toastShow(info.operatMessage);
//					 showCodeDialog(Mobile);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtils.i("快速登录 登录错误", msg);
				mDialog.dismiss();
				toastShow("服务器繁忙，请稍后");
				AppErrorLogUtil.getErrorLog(getActivity()).postError(
						error.getExceptionCode() + "", "POST",
						ApiUrl.MOBILE_LOGIN);

			}
		};
		getHttpUtils()
				.send(HttpMethod.POST, AppUrl.API + url, params, callBack);

	}

	protected void saveData() {
		if (userInfo == null)
			return;
		// 将用户信息保存本地
		((SJQApp) getActivity().getApplication()).caceUserInfo(userInfo);
		// 刷新数据
		((SJQApp) getActivity().getApplication()).refresh();
	     String name=CommonUtils.removeAllSpace(userInfo.getGuid());
		 setdenlu(name);
		((LoginActivity) getActivity()).finish(true);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_DENT_CODE) {
			if (resultCode == SelectiDentityActivity.OK_CODE) {
				String makName = data.getStringExtra(AppTag.TAG_TITLE);
				if (!TextUtils.isEmpty(makName)) {
					userInfo.markName = makName;
					saveData();
				}
			}
		}
	}
	//第三方登录保存信息
	private void doGetUserInfo1(final Login info) {
		mDialog.show();
		if (getActivity() == null || info == null || info.userInfo == null)
			return;
		String url = AppUrl.GET_USER_INFO + info.userInfo.guid;
		getHttpUtils().configRequestRetryCount(0).send(HttpMethod.GET, AppUrl.API + url, getParams(url),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException error, String msg) {
						mDialog.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						mDialog.dismiss();
						JSONObject json;
						SwUserInfo templ=null;
						try {
							json = new JSONObject(n.result);
							String result1=json.getString("result");
							Gson gson = new Gson();
							templ=gson.fromJson(result1, SwUserInfo.class);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if(templ!=null){
							if(info.userInfo.markName.equals("DESIGNER")){
								if(!templ.isBindMobile){
									Intent intent = new Intent(getActivity(),
											OAuth1Activity.class);
									intent.putExtra(SelectiDentityActivity.USER_TYPE, info.userInfo.markName);
									intent.putExtra(SelectiDentityActivity.NICK_NAME,info.userInfo.nickName);
									intent.putExtra(SelectiDentityActivity.HEAD_LOGO,info.userInfo.logo);
									intent.putExtra(AppTag.TAG_GUID,info.userInfo.guid);
									intent.putExtra(AppTag.TAG_ID,info.userInfo.id);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									getActivity().startActivity(intent);
									return;
								}else{
									// 将用户信息保存本地
									SJQApp app = (SJQApp) getActivity().getApplication();
									app.caceUserInfo(info.userInfo);
									// 刷新数据-》登录
									app.refresh();
									String name=CommonUtils.removeAllSpace(info.userInfo.getGuid());
									setdenlu(name);
									((LoginActivity) getActivity()).finish(true);
								}
							}else{
								// 将用户信息保存本地
								SJQApp app = (SJQApp) getActivity().getApplication();
								app.caceUserInfo(info.userInfo);
								// 刷新数据-》登录
								app.refresh();
								String name=CommonUtils.removeAllSpace(info.userInfo.getGuid());
								setdenlu(name);
								((LoginActivity) getActivity()).finish(true);
							}
						}
					}

				});
	}
	//第三方登录
		String unionId;
	    String openId;
		String nickName, logo;
	    int fromWay;
	private Button btnLogin;
	@Override
	public void onDestroy() {
		if(mDialog != null){
			mDialog = null;
		}
		super.onDestroy();
	}

	@Override
	public void onStart(SHARE_MEDIA platform) {
		if (mDialog != null) {
			mDialog.show("请稍后...");
		}
		mOAuthHelper.doDeleteWeiChatOauth();
		mOAuthHelper.doDeleteQQOauth();
		mOAuthHelper.doDeleteSinaOauth();
	}
	@Override
	public void onComplete(Bundle value, SHARE_MEDIA platform) {
		  doOAuth(value, platform);		
	}
	@Override
	public void onComplete(int status, Map<String, Object> info) {
		if (mDialog != null)
			mDialog.show("正在登录中");

		nickName = (String) info.get("nickname");
		if (TextUtils.isEmpty(nickName))
			nickName = (String) info.get("screen_name");
		logo = (String) info.get("profile_image_url");
		if (TextUtils.isEmpty(logo))
			logo = (String) info.get("headimgurl");
		if (fromWay ==5){
			unionId = (String) info.get("unionid");
			openId = (String) info.get("openid");
		}else{
			unionId = (String) info.get("openid");
		}
		if (unionId == null || unionId.equals("")) {
			unionId = (String) info.get("uid");
		}
		String url = AppUrl.ThirdLogin;// +"="+openId+"&MarkName="+MarkName+"&Client="+AppTag.CLIENT;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			if(fromWay ==5){
				param.put("unionId", unionId);
				param.put("openID", openId);
			}else{
				param.put("unionId", unionId);
			}
			String channel="android{"+android.os.Build.MODEL+"}";
			param.put("channel", channel);
			param.put("fromWay", fromWay+"");
			param.put("nickName", nickName);
			param.put("logo", logo);
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			System.out.println("-->>"+"第三方登录请求接口");
			System.out.println("-->>"+"第三方登录请求接口");
			System.out.println("-->>"+param.toString());
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (mDialog != null)
					mDialog.dismiss();
				System.out.println("-->>"+"第三方登录");
				System.out.println("-->>"+"第三方登录");
				System.out.println("-->>"+"第三方登录");
				System.out.println("-->>"+arg0.result);
				System.out.println("-->>"+"第三方登录");
				System.out.println("-->>"+"第三方登录");
				JSONObject json;
				Login user=null;
				try {
					json = new JSONObject(arg0.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					user=gson.fromJson(result1, Login.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(user.operatResult){
					if (user != null && user.userInfo != null) {
						toastShow(user.operatMessage);
						userInfo= user.userInfo;
						if (user.userInfo.markName.equals("NONE")) {// 未绑定
							go2Au1(unionId, fromWay, nickName, logo,user.userInfo.id,user.userInfo.guid);
						} else {// 已经绑定
							doGetUserInfo1(user);
						}
					} else {// 未绑定
						go2Au1(unionId, fromWay, nickName, logo,user.userInfo.id,user.userInfo.guid);
					}
				}else{
					toastShow(user.operatMessage);
				}
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (mDialog != null)
					mDialog.dismiss();
				toastShow("网络连接错误");
			}
		};
		getHttpUtils().send(HttpMethod.POST, AppUrl.API + url, params, callBack);
	}
	@Override
	public void onCancel(SHARE_MEDIA platform) {
		if (mDialog != null)
			mDialog.dismiss();	
		
	}
	@Override
	public void onError(SocializeException e, SHARE_MEDIA platform) {
		if (mDialog != null)
			mDialog.dismiss();
		if (e != null)
			toastShow(e.getMessage());
		
	}
	public void onResult(int requestCode, int resultCode, Intent data) {
		/** 使用SSO授权必须添加如下代码 */
		if (mOAuthHelper != null)
			mOAuthHelper.onActivityResult(requestCode, resultCode, data);
	}
	// 授权登录
	private void doOAuth(final Bundle value, final SHARE_MEDIA platform) {

	}
	/**
	 * 未绑定
	 * 
	 * @param openId
	 * @param markName
	 * @param logo2 
	 * @param nickName2 
	 */
	void go2Au1(String openId, int markName, String nickName2, String logo2,int id, String guid) {
		if (getActivity() == null)
			return;
		Intent intent = new Intent(getActivity(), SelectiDentityActivity.class);
		intent.putExtra(SelectiDentityActivity.IS_OAUTH, true);
		intent.putExtra(SelectiDentityActivity.OPEN_ID, openId);
		intent.putExtra(SelectiDentityActivity.MARK_NAME, markName);
		intent.putExtra(SelectiDentityActivity.NICK_NAME, nickName2);
		intent.putExtra(SelectiDentityActivity.HEAD_LOGO, logo2);
		intent.putExtra(AppTag.TAG_ID, id);
		intent.putExtra(AppTag.TAG_GUID, guid);
		startActivityForResult(intent, SELECT_DENT_CODE);
	}
	private void getQuDao(){
		if (TextUtils.isEmpty(channel)) {
			ApplicationInfo appInfo;
			try {
				appInfo = getActivity().getPackageManager().getApplicationInfo(
						getActivity().getPackageName(), PackageManager.GET_META_DATA);
				channel = appInfo.metaData.getString("UMENG_CHANNEL");
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
				channel = "other";
			}
		}
	}
	private void setdenlu(String name){
		Intent intent = new Intent();
		intent.setAction("denglu");
		intent.putExtra("name",name);
		getActivity().sendBroadcast(intent);
	}
}