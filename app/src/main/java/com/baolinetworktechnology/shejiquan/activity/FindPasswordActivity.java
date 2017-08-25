package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

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
import com.baolinetworktechnology.shejiquan.view.Conde;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.guojisheng.koyview.MyEditText;
import com.hyphenate.EMCallBack;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 重置密码
 * 
 * @author JiSheng.Guo
 * 
 */
public class FindPasswordActivity extends BaseActivity {
	public static String IS_SET_PAWSD = "IS_SET_PAWSD";
	private MyEditText mMyEtPhone, mMyEtpassword, mMyEtcode, myetOldPawd,
			myetNewPawd, myetNewPawd2;
	private Conde mGetCode;
	private Button btn_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().getBooleanExtra(IS_SET_PAWSD, false)) {
			setContentView(R.layout.activity_setpawd);
			setTitle("修改密码");
			initSetPaswdUI();
		} else {
			setContentView(R.layout.activity_register);
			setTitle("找回密码");
			initView();
		}

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
	// 初始化修改密码界面
	private void initSetPaswdUI() {
		myetOldPawd = (MyEditText) findViewById(R.id.myetOldPawd);// 旧密码
		myetNewPawd = (MyEditText) findViewById(R.id.myetNewPawd);// 新密码
		myetNewPawd2 = (MyEditText) findViewById(R.id.myetNewPawd2);// 确认密码
	}

	private void initView() {
		mMyEtPhone = (MyEditText) findViewById(R.id.et_phone);
		mMyEtPhone.addTextChangedListener(watcher);
		mMyEtpassword = (MyEditText) findViewById(R.id.et_password);
		mMyEtpassword.setHint("输入新密码(至少6位数)");
		mMyEtpassword.addTextChangedListener(watcher);
		mGetCode = (Conde) findViewById(R.id.btn_getCode);
		mMyEtcode = (MyEditText) findViewById(R.id.et_code);
		mMyEtcode.addTextChangedListener(watcher);
		findViewById(R.id.back).setOnClickListener(this);
		mMyEtPhone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.length() == 11) {
					if (findViewById(R.id.tv_pre).getVisibility() != View.GONE) {
						findViewById(R.id.tv_pre).setVisibility(View.GONE);
					}
					CheckPhone();

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		findViewById(R.id.tv_pre).setOnClickListener(this);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setText("确认");
		btn_ok.setBackgroundResource(R.drawable.anniu_shape1);
		btn_ok.setClickable(false);
		// mGetCode.setEnabled(false);
		mGetCode.setOnClickListener(this);

		CheckBox cbPawd = (CheckBox) findViewById(R.id.cbPawd);
		cbPawd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {// //显示密码
					mMyEtpassword
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else { // 隐藏密码
					mMyEtpassword.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				mMyEtpassword.setSelection(mMyEtpassword.getText().toString()
						.length());
			}
		});

		mMyEtPhone.SetButton(mGetCode);
		mMyEtPhone.setText(CacheUtils.getStringData(this, "phone", ""));
	}
	
	private String strphone="";
	private String strcode="";
	private String strpass="";
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
			strphone = mMyEtPhone.getText().toString();
			strcode =  mMyEtcode.getText().toString();
			strpass =  mMyEtpassword.getText().toString();
			if(!strphone.equals("") && !strcode.equals("") && !strpass.equals("")){
				btn_ok.setClickable(true);
				btn_ok.setBackgroundResource(R.drawable.anniu_shape);
			}
			if(strphone.equals("") || strcode.equals("") || strpass.equals("")){
				btn_ok.setClickable(false);
				btn_ok.setBackgroundResource(R.drawable.anniu_shape1);
			}
		}	    
	};
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_getCode:
			if (mMyEtpassword.getVisibility() == View.VISIBLE) {
				doGetCode();
			} else {
				CheckPhone();
			}

			break;
		case R.id.tv_pre:
			// initView();
			break;
		case R.id.btn_ok:

			doCheckCode(mMyEtcode.getText().toString());
			break;
		case R.id.btnSetPawd:
			setPawd();
			break;
		case R.id.back:
			finish();
			break;
		}

	}

	// 设置密码
	private void setPawd() {

		if (SJQApp.user == null) {
			startActivity(new Intent(this, FindPasswordActivity.class));
			finish();
			return;
		}
		String oldPaswd = myetOldPawd.getText().toString();
		String newPawd = myetNewPawd.getText().toString();
		String newPawd2 = myetNewPawd2.getText().toString();

		if (!CommonUtils.checkPassword(oldPaswd)) {
			toastShow("旧密码不正确,请重新输入");
			return;
		}
		if (newPawd.length() < 6) {
			toastShow("密码至少需要6位数,请重新输入");
			return;
		}
		if (!newPawd.equals(newPawd2)) {
			toastShow("两次密码输入不一致,请重新输入");
			return;
		}
		if (oldPaswd.equals(newPawd)) {
			toastShow("旧密码和新密码一致,请重新输入");
			return;
		}

		String url = ApiUrl.SET_PASSWORD;
		RequestParams param = getParams(url);

		param.addBodyParameter("Mobile", SJQApp.user.Mobile);
		param.addBodyParameter("OldPassword", oldPaswd);
		param.addBodyParameter("NewPassword", newPawd2);

		getHttpUtils().send(HttpMethod.POST, ApiUrl.API + url, param,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
						dialog.show("修改密码中，请稍候");
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						toastShow(getResources().getString(
								R.string.network_error));
						if (dialog != null)
							dialog.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (dialog != null)
							dialog.dismiss();
						Login login = CommonUtils.getDomain(arg0, Login.class);
						if (login != null) {
							toastShow(login.operatMessage);
							if (login.success) {
								DemoHelper.getInstance().logout(false,new EMCallBack() {

									@Override
									public void onSuccess() {
										// TODO Auto-generated method stub

									}

									@Override
									public void onProgress(int arg0, String arg1) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onError(int arg0, String arg1) {
										// TODO Auto-generated method stub

									}
								});
								SJQApp.fragmentIndex = 2;
								((SJQApp) getApplication()).exitAccount();
//							    发送 一个无序广播
								Intent intent = new Intent();
								intent.setAction("addSuccess");
								sendBroadcast(intent);
								Intent i = new Intent(FindPasswordActivity.this,
										SkipActivity.class);
								startActivity(i);
								finish();
							}
						} else {
							toastShow("暂时无法修改密码");
//							startActivity(new Intent(FindPasswordActivity.this,
//									FindPasswordActivity.class));
//							finish();
						}

					}
				});

	}

	// 验证手机号是否可用
	private void CheckPhone() {

		final String sPhone = mMyEtPhone.getText().toString().trim();
		if (!CommonUtils.checkNumber(sPhone)) {
			mMyEtPhone.setError("请输入正确的手机号码");
			return;
		}
		mGetCode.setEnabled(true);
		mGetCode.setText("获取验证码");
		CacheUtils.cacheStringData(this, "phone", sPhone);

	}

	// 获取验证码
	private void doGetCode() {
		final String sPhone = mMyEtPhone.getText().toString().trim();

		if (!CommonUtils.checkNumber(sPhone)) {
			mMyEtPhone.setError("请输入正确的手机号码");
			return;
		}

		dialog.show();
		mGetCode.start();
		final String url = ApiUrl.SEND_SMS_RESET_PASSWORD + sPhone;

		getHttpUtils().configRequestRetryCount(0).send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						dialog.dismiss();
						mGetCode.stop();
						AppErrorLogUtil.getErrorLog(getApplicationContext())
								.postError(error.getExceptionCode() + "",
										"GET", url);
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						dialog.dismiss();
						// btn_getCode.stop();
						Bean bean = CommonUtils.getDomain(n, Bean.class);
						if (bean != null) {
							if (bean.success) {
								toastShow("验证码已下发成功，注意查收");
								// showCodeDialog(sPhone, sPassword);
							} else {
								toastShow(bean.message);
								mGetCode.stop();
							}
						}
					}
				});
	}

	// 检查验证码是否正确
	private void doCheckCode(String Captcha) {

		final String Mobile = mMyEtPhone.getText().toString().trim();
		final String sPassword = mMyEtpassword.getText().toString().trim();
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

		hideInput();
		dialog.show();
		String url = ApiUrl.RESET_PASSWORD;
		RequestParams params = getParams(url);
		params.addBodyParameter("Mobile", mMyEtPhone.getText().toString());
		params.addBodyParameter("Password", mMyEtpassword.getText().toString());
		params.addBodyParameter("Captcha", mMyEtcode.getText().toString());
		params.addBodyParameter("Client", AppTag.CLIENT);

		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (dialog == null)
					return;
				dialog.dismiss();
				Bean data = CommonUtils.getDomain(responseInfo, Bean.class);
				if (data != null) {

					if (data.success) {
						new MyAlertDialog(FindPasswordActivity.this)
								.setTitle("密码修改成功").setContent("请重新输入登录")
								.setBtnOk("好的")
								.setBtnOnListener(new DialogOnListener() {

									@Override
									public void onClickListener(boolean isOk) {
										finish();

									}
								}).show();

					} else {
						toastShow(data.message);
					}
				}
			

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				if (dialog == null)
					return;
				dialog.dismiss();
				toastShow("网络请求错误，请稍后重试");
			}
		};
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);

	}

	@Override
	protected void onDestroy() {
		dialog = null;
		super.onDestroy();
	}
}
