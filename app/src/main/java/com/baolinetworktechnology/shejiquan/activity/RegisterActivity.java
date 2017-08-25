package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.Login;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.view.Conde;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.guojisheng.koyview.MyEditText;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 注册
 * 
 * @author JiSheng.Guo
 * 
 */
public class RegisterActivity extends BaseActivity {
	public static final String MARK_NAME = "MARK_NAME";
	public static String OPEN_ID = "OPEN_ID";
	private MyEditText et_phone, et_password, et_code;
	private Conde btn_getCode;
	private String userGuid = "";
	String channel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setTitle("注册");
		et_phone = (MyEditText) findViewById(R.id.et_phone);
		et_password = (MyEditText) findViewById(R.id.et_password);
		btn_getCode = (Conde) findViewById(R.id.btn_getCode);
		et_code = (MyEditText) findViewById(R.id.et_code);
		et_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (findViewById(R.id.tv_pre).getVisibility() != View.GONE) {
					findViewById(R.id.tv_pre).setVisibility(View.GONE);
				}
				if (s.length() == 11) {
					CheckPhone();

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		Button btn_ok=(Button) findViewById(R.id.btn_ok);
		btn_ok.setClickable(true);
		findViewById(R.id.tv_pre).setOnClickListener(this);
		btn_getCode.setEnabled(false);
		et_phone.SetButton(btn_getCode);
		btn_getCode.setOnClickListener(this);
		dialog.setCancelable(false);
		CheckBox cbPawd = (CheckBox) findViewById(R.id.cbPawd);
		cbPawd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {// //显示密码
					et_password
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else { // 隐藏密码
					et_password.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				et_password.setSelection(et_password.getText().toString()
						.length());
			}
		});

		initAuto();
	}

	@Override
	protected void setUpViewAndData() {

	}
	@Override
	protected void restartApp() {
		startActivity(new Intent(this, SplashActivity.class));
		finish();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_BACK_TO_HOME);
		switch (action) {
			case AppStatusConstant.ACTION_RESTART_APP:
				restartApp();
				break;
		}
	}
	public static String IS_OAUTH = "IS_OAUTH";
	private boolean isOAuth = false;
	private String openId;
	private String MarkName;

	private void initAuto() {
		openId = getIntent().getStringExtra(OPEN_ID);
		MarkName = getIntent().getStringExtra(MARK_NAME);
		isOAuth = getIntent().getBooleanExtra(IS_OAUTH, false);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_getCode:
			if (et_password.getVisibility() == View.VISIBLE) {
				doGetCode();
			} else {
				CheckPhone();
			}

			break;

		case R.id.btn_ok:
			hideInput();
			doCheckCode(et_code.getText().toString());

			break;
		}

	}

	// 验证手机号是否可用
	private void CheckPhone() {

		final String sPhone = et_phone.getText().toString().trim();
		if (!CommonUtils.checkNumber(sPhone)) {
			et_phone.setError("请输入正确的手机号码");
			return;
		}
		CacheUtils.cacheStringData(this, "phone", sPhone);
		String url = ApiUrl.VERIFY_USERNAME + sPhone;
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						dialog.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						dialog.dismiss();
						Bean bean = CommonUtils.getDomain(n, Bean.class);
						if (bean != null) {
							if (bean.success) {

								btn_getCode.setEnabled(true);
								btn_getCode.setText("获取验证码");

							} else {
								findViewById(R.id.tv_pre).setVisibility(
										View.VISIBLE);
								btn_getCode.setEnabled(false);
								toastShow(bean.message);
							}
						}
					}
				});
	}
	/**
	 * 获取验证码
	 */
	private void doGetCode() {
		final String sPhone = et_phone.getText().toString().trim();

		if (!CommonUtils.checkNumber(sPhone)) {
			et_phone.setError("请输入正确的手机号码");
			return;
		}

		dialog.show();
		btn_getCode.start();
		final String url = ApiUrl.SEND_SMS_CODE_MREG + sPhone;

		getHttpUtils().configRequestRetryCount(0).send(HttpMethod.GET,
				ApiUrl.API + url, getParams(url),
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						dialog.dismiss();
						btn_getCode.stop();
						AppErrorLogUtil.getErrorLog(getApplicationContext())
								.postError(error.getExceptionCode() + "",
										"GET", url);
						toastShow("服务器繁忙");
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						dialog.dismiss();
						Bean bean = CommonUtils.getDomain(n, Bean.class);
						if (bean != null) {
							if (bean.success) {
								toastShow("验证码已下发成功，注意查收");
							} else {
								toastShow(bean.message);
								btn_getCode.stop();
							}
						}
					}
				});
	}

	// 检查验证码是否正确
	private void doCheckCode(String Captcha) {
		String userType = getIntent().getStringExtra(
				SelectiDentityActivity.USER_TYPE);
		String mMarkName = TextUtils.isEmpty(userType) ? "NONE" : userType;
		final String Mobile = et_phone.getText().toString().trim();
		final String sPassword = et_password.getText().toString().trim();
		if (!CommonUtils.checkNumber(Mobile)) {
			toastShow("您输入的手机号有误");
			return;
		}

		if (!CommonUtils.checkPassword(sPassword)) {
			toastShow("您输入的密码格式有误");
			return;
		}
		if (Captcha.length() < 4) {
			toastShow("验证码不正确");
			return;
		}

		dialog.show();
		if (TextUtils.isEmpty(channel)) {
			ApplicationInfo appInfo;
			try {
				appInfo = getPackageManager().getApplicationInfo(
						getPackageName(), PackageManager.GET_META_DATA);
				channel = appInfo.metaData.getString("UMENG_CHANNEL");
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				channel = "other";
			}
		}

		String url = ApiUrl.MOBILE_REGISTER;
		RequestParams params = getParams(url);
		params.addBodyParameter("Mobile", Mobile);
		params.addBodyParameter("Captcha", Captcha);
		params.addBodyParameter("Channel", channel);
		params.addBodyParameter("Password", sPassword);
		params.addBodyParameter("MarkName", mMarkName);
		params.addBodyParameter("Client", AppTag.CLIENT);
		// System.out.println("-->>params=" + params.);
		// 注册
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> n) {
				LogUtils.i("regist", n.result);
				dialog.dismiss();
				Login info = CommonUtils.getDomain(n, Login.class);
				if (info == null) {
					toastShow("请求失败");
					return;
				}

				if (info.success && info.userInfo != null) {
					userGuid = info.userInfo.UserName;
					if (isOAuth) {
						SJQApp.user = info.userInfo;
						doBind();
						return;
					}
					go2SelectDentity(Mobile, sPassword, info.userInfo.guid);
				} else {
					toastShow(info.operatMessage);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtils.i("手机注册错误", msg);
				dialog.dismiss();
				toastShow("服务器繁忙，请稍后");

			}
		};
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);

	}

	private void go2SelectDentity(final String Mobile, final String sPassword,
			String guid, boolean isAuto) {
		Intent intent = new Intent(RegisterActivity.this,
				SelectiDentityActivity.class);
		intent.putExtra(AppTag.TAG_GUID, guid);
		intent.putExtra("USER", Mobile);
		// intent.putExtra(SelectiDentityActivity.IS_OAUTH, isAuto);
		intent.putExtra("PASSWD", sPassword);
		startActivityForResult(intent, 200);
	}

	/**
	 * 到选择身份页面
	 * 
	 * @param Mobile
	 * @param sPassword
	 * @param guid
	 */
	private void go2SelectDentity(String Mobile, String sPassword, String guid) {
		go2SelectDentity(Mobile, sPassword, guid, false);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 200) {
			if (resultCode == 0x101) {

				Intent data1 = new Intent();
				data1.putExtra("USER", et_phone.getText().toString());
				data1.putExtra("PASSWD", et_password.getText().toString());
				setResult(100, data1);
			}

		}
		finish();
	}

	protected void doBind() {
		if (dialog == null) {
			dialog = new MyDialog(this);
			dialog.setCancelable(false);
		}
		dialog.show("正在绑定...");
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dialog.dismiss();
				Bean bean = CommonUtils.getDomain(arg0, Bean.class);
				if (bean != null) {

					if (bean.success) {
						SJQApp app = (SJQApp) getApplication();
						app.refresh();
						Intent intent = new Intent(RegisterActivity.this,
								SkipActivity.class);
						startActivity(intent);
						finish();
						// String Mobile = et_phone.getText().toString();
						// String sPassword = et_password.getText().toString();
						// go2SelectDentity(Mobile, sPassword, userGuid, true);
						// finish();
					} else {
						toastShow(bean.message);
						finish();
					}
				} else {
					toastShow("绑定失败");
					finish();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				toastShow("网络请求失败");
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

}
