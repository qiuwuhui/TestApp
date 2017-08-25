package com.baolinetworktechnology.shejiquan.fragment;

import java.util.Map;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.FindPasswordActivity;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.activity.OAuth2Activity;
import com.baolinetworktechnology.shejiquan.activity.SelectiDentityActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Login;
import com.baolinetworktechnology.shejiquan.interfaces.OnOAuthListener;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.OAuthHelper;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.guojisheng.koyview.MyEditText;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.exception.SocializeException;

/**
 * 登陆-账号密码登陆
 * 
 * @author JiSheng.Guo
 * 
 */
public class LoginFragment extends BaseFragment implements OnOAuthListener {

	private MyEditText mEtPhone;
	private MyEditText mEtPassword;
	private MyDialog mDialog;
	private OAuthHelper mOAuthHelper;
	private int erroNum;
	private CheckBox cbPawd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_login, null);
		initView(view);
		mOAuthHelper = new OAuthHelper(getActivity());
		mOAuthHelper.setOnOAuthListener(this);
		return view;
	}

	@Override
	public void onResume() {
		// MobclickAgent.onPageStart("LoginFragment"); // 统计页面
		super.onResume();
	}

	public void onPause() {
		super.onPause();
		// MobclickAgent.onPageEnd("LoginFragment");
	}

	private void initView(View view) {
		btnlogin = (Button) view.findViewById(R.id.btn_login);
		Button btn_zuce= (Button) view.findViewById(R.id.btnRegist);
		btn_zuce.setOnClickListener(this);
		btnlogin.setOnClickListener(this);
		mEtPhone = (MyEditText) view.findViewById(R.id.et_phone);
		mEtPhone.addTextChangedListener(watcher);
		mEtPhone.SetButton(btnlogin);
		btnlogin.setClickable(false);
		btnlogin.setBackgroundResource(R.drawable.anniu_shape1);
		view.findViewById(R.id.tv_forgetpassword).setOnClickListener(this);
		mEtPassword = (MyEditText) view.findViewById(R.id.et_password);
		mEtPassword.addTextChangedListener(watcher);
		mDialog = ((LoginActivity) getActivity()).mDialog;
		mEtPhone.setText(CacheUtils.getStringData(getActivity(), "phone", ""));
		view.findViewById(R.id.oAuthQQ).setOnClickListener(this);
		view.findViewById(R.id.oAuthWX).setOnClickListener(this);
		view.findViewById(R.id.oAuthSina).setOnClickListener(this);
		view.findViewById(R.id.tvFastLogin).setOnClickListener(this);

		cbPawd = (CheckBox) view.findViewById(R.id.cbPawd);
		cbPawd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {// //显示密码
					mEtPassword
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else { // 隐藏密码
					mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				mEtPassword.setSelection(mEtPassword.getText().toString()
						.length());
			}
		});

	}
	private String strphone="";
	private String strPassword="";
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
			strPassword = mEtPassword.getText().toString();
			if(!strphone.equals("") && !strPassword.equals("")){
				btnlogin.setClickable(true);
				btnlogin.setBackgroundResource(R.drawable.anniu_shape);
			}
			if(strphone.equals("") || strPassword.equals("")){
				btnlogin.setClickable(false);
				btnlogin.setBackgroundResource(R.drawable.anniu_shape1);
			}
		}	    
	};
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_login:

			doLogin();
			break;

		case R.id.tvFastLogin:
			((LoginActivity) getActivity()).startFastFragment1();
			break;
		case R.id.btnRegist:
			((LoginActivity) getActivity()).startRegisterActivity();
			break;
		case R.id.tv_forgetpassword:
			go2FindPaswd();
			break;
		case R.id.oAuthQQ:
			mOAuthHelper.doOauthQQ();
			mDialog.setCancelable(false);
			break;
		case R.id.oAuthSina:
			mOAuthHelper.doOauthSina();
			mDialog.setCancelable(false);
			break;
		case R.id.oAuthWX:
			mOAuthHelper.doOauthWeChat();
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

	/**
	 * 设置登录相关参数
	 * 
	 * @param phone
	 *            用户名
	 * @param pawsd
	 *            密码
	 */
	public void setLoginData(String phone, String pawsd) {
		if (mEtPhone == null)
			return;
		mEtPhone.setText(phone.trim());
		mEtPassword.setText(pawsd.trim());
		doLogin();
	}

	/**
	 * 进行登录
	 */
	private void doLogin() {
		sPhone = mEtPhone.getText().toString().trim();
		sPassword = mEtPassword.getText().toString().trim();
		if (!CommonUtils.checkNumber(sPhone)) {
			mEtPhone.setError("请输入正确的手机号码");
			return;
		}
		CacheUtils.cacheStringData(getActivity(), "phone", sPhone);
		if (sPassword.length() < 6) {
			mEtPassword.setError("请输入正确的密码");
			return;
		}
		hideInput();
		RequestParams params = getParams(ApiUrl.LOGING);
		params.addBodyParameter("UserName", sPhone);
		params.addBodyParameter("Password", sPassword);
		params.addBodyParameter("Client", ApiUrl.CLIENT);

		getHttpUtils(10).send(HttpMethod.POST, ApiUrl.API + ApiUrl.LOGING,
				params, new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
						if (mDialog != null) {
							mDialog.setCancelable(false);
							mDialog.show();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						toastShow("网络请求失败");
						if (mDialog != null)
							mDialog.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (mDialog != null)
							mDialog.dismiss();
						Login info = CommonUtils.getDomain(responseInfo,
								Login.class);
						if (info == null) {
							toastShow("服务器繁忙");
							return;
						}

						if (info.success) {

							if (info.userInfo != null) {
								erroNum = 0;// 错误次数
								doGetUserInfo(info);
								toastShow(info.operatMessage);
							} else {
								if (TextUtils.isEmpty(info.operatMessage)) {
									info.operatMessage = "账户信息读取失败";
								}
								toastShow(info.operatMessage);
							}

						} else {
							if (TextUtils.isEmpty(info.operatMessage)) {
								info.operatMessage = "账户或者密码错误";
							}
//							cbPawd.setChecked(true);
							toastShow(info.operatMessage);
							erroNum++;
							if (erroNum >= 3) {
								showFindPawd();
							}
						}

					}

				});
		

	}

	// 显示找回密码对话框
	private void showFindPawd() {
		new MyAlertDialog(getActivity()).setTitle("是否找回密码？").setBtnCancel("取消")
				.setBtnOk("找回密码").setBtnOnListener(new DialogOnListener() {

					@Override
					public void onClickListener(boolean isOk) {
						if (isOk) {
							go2FindPaswd();
						}
						erroNum = 0;
					}
				}).show();
	}

	@Override
	public void onStart(SHARE_MEDIA platform) {
		if (mDialog != null) {
			mDialog.show("请稍后...");
		}
	}

	@Override
	public void onComplete(Bundle value, SHARE_MEDIA platform) {

		doOAuth(value, platform);

	}

	String platformName = "1";
	String openId;

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
	void go2Au1(String openId, String markName, String nickName2, String logo2) {
		if (getActivity() == null)
			return;
		Intent intent = new Intent(getActivity(), SelectiDentityActivity.class);
		intent.putExtra(SelectiDentityActivity.IS_OAUTH, true);
		intent.putExtra(SelectiDentityActivity.OPEN_ID, openId);
		intent.putExtra(SelectiDentityActivity.MARK_NAME, markName);
		
		intent.putExtra(SelectiDentityActivity.NICK_NAME, nickName2);
		intent.putExtra(SelectiDentityActivity.HEAD_LOGO, logo2);
		startActivityForResult(intent, 0x8);
	}

	// void go2Au1(String openId, String markName) {
	// if (getActivity() == null)
	// return;
	// Intent intent = new Intent(getActivity(), OAuth1Activity.class);
	// intent.putExtra(OAuth1Activity.OPEN_ID, openId);
	// intent.putExtra(OAuth2Activity.MARK_NAME, markName);
	// startActivityForResult(intent, 0x8);
	// }
	String nickName, logo;
	private String sPhone;
	private String sPassword;
	private Button btnlogin;

	@Override
	public void onComplete(int status, Map<String, Object> info) {

		if (mDialog != null)
			mDialog.show("正在登录中");

		nickName = (String) info.get("nickname");
		if (TextUtils.isEmpty(nickName))
			nickName = (String) info.get("screen_name");

//		logo = (String) info.get("profile_image_url");
//		if (TextUtils.isEmpty(logo))
			logo = (String) info.get("headimgurl");
		platformName = status + "";

		openId = (String) info.get("openid");
		if (TextUtils.isEmpty(openId)) {
			openId = (String) info.get("uid");
		}
		String url = ApiUrl.ThirdLogin;// +"="+openId+"&MarkName="+MarkName+"&Client="+AppTag.CLIENT;
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (mDialog != null)
					mDialog.dismiss();
				Login user = CommonUtils.getDomain(arg0, Login.class);

				if (user != null && user.userInfo != null) {
					if (!user.success) {// 未绑定
						go2Au1(openId, platformName, nickName, logo);
					} else {// 已经绑定
						doGetUserInfo(user, true);
					}
				} else {// 未绑定
					go2Au1(openId, platformName, nickName, logo);
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (mDialog != null)
					mDialog.dismiss();
				toastShow("网络连接错误");
			}
		};
		RequestParams params = getParams(url);
		params.addBodyParameter("OpenID", openId);
		params.addBodyParameter("MarkName", platformName);
		params.addBodyParameter("Client", AppTag.CLIENT);
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);
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

	/**
	 * 获取用户资料
	 * 
	 * @param info
	 */
	private void doGetUserInfo(Login info) {
		doGetUserInfo(info, false);
	}

	private void doGetUserInfo(final Login info, boolean isAuto) {

		if (getActivity() == null || info == null || info.userInfo == null)
			return;

		// 用户-选择角色
		if ("NONE".equals(info.userInfo.markName)) {
			Intent intent = new Intent(getActivity(),
					SelectiDentityActivity.class);
			intent.putExtra(AppTag.TAG_GUID, info.userInfo.guid);
			if (isAuto) {
				startActivityForResult(intent, 0x8);
			} else {
				startActivityForResult(intent, 0x14);
			}
			return;
		}		
		// 将用户信息保存本地
		SJQApp app = (SJQApp) getActivity().getApplication();
		app.caceUserInfo(info.userInfo);
		// 刷新数据-》登录
		app.refresh();
		String name=CommonUtils.removeAllSpace(info.userInfo.getGuid());
		setdenlu(name);
		((LoginActivity) getActivity()).finish(true);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0x8) {
			if (resultCode == OAuth2Activity.BIN_OK) {
				// doFinsh();
				if (mOAuthHelper == null) {
					mOAuthHelper = new OAuthHelper(getActivity());
				}
				if (platformName.equals("1")) {
					mOAuthHelper.doOauthQQ();
				} else if (platformName.equals("2")) {
					mOAuthHelper.doOauthWeChat();
				} else {
					mOAuthHelper.doOauthSina();
				}
			}
		} else if (requestCode == 0x14) {
			if (resultCode == SelectiDentityActivity.OK_CODE) {
				doLogin();
			}
		}
	}

	@Override
	public void onDestroy() {
		mDialog = null;
		super.onDestroy();

	}
	private void setdenlu(String name){
		Intent intent = new Intent();
		intent.setAction("denglu");
		intent.putExtra("name",name);
		getActivity().sendBroadcast(intent);
	}
}