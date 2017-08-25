package com.baolinetworktechnology.shejiquan;

import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.RegisterActivity;
import com.baolinetworktechnology.shejiquan.activity.SelectiDentityActivity;
import com.baolinetworktechnology.shejiquan.activity.SkipActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.Login;
import com.baolinetworktechnology.shejiquan.fragment.BaseFragment;
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

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class RegisterFragment extends BaseFragment {
	public static final String MARK_NAME = "MARK_NAME";
	public static String OPEN_ID = "OPEN_ID";
	private MyEditText et_phone, et_password, et_code;
	private Conde btn_getCode;
	private String userGuid = "";
	String channel = null;
	private MyDialog dialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_register, null);
		((TextView) view.findViewById(R.id.zuce)).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		dialog = ((LoginActivity) getActivity()).mDialog;
		et_phone = (MyEditText) view.findViewById(R.id.et_phone);
		et_phone.addTextChangedListener(watcher);
		et_password = (MyEditText) view.findViewById(R.id.et_password);
		et_password.addTextChangedListener(watcher);
		btn_getCode = (Conde) view.findViewById(R.id.btn_getCode);
		btn_getCode.setConde();
		et_code = (MyEditText) view.findViewById(R.id.et_code);
		et_code.addTextChangedListener(watcher);
		et_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (view.findViewById(R.id.tv_pre).getVisibility() != View.GONE) {
					view.findViewById(R.id.tv_pre).setVisibility(View.GONE);
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
		view.findViewById(R.id.tv_pre).setOnClickListener(this);
		btn_getCode.setEnabled(false);
		et_phone.SetButton(btn_getCode);
		btn_getCode.setOnClickListener(this);
		dialog.setCancelable(false);
		btn_zhuce = (Button) view.findViewById(R.id.btn_zhuce);
		btn_zhuce.setOnClickListener(this);
		btn_zhuce.setBackgroundResource(R.drawable.anniu_shape1);
		btn_zhuce.setClickable(false);
		CheckBox cbPawd = (CheckBox) view.findViewById(R.id.cbPawd);
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
		return view;
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
			strphone = et_phone.getText().toString();
			strcode =  et_code.getText().toString();
			strpass =  et_password.getText().toString();
			if(!strphone.equals("") && !strcode.equals("") && !strpass.equals("")){
				btn_zhuce.setClickable(true);
				btn_zhuce.setBackgroundResource(R.drawable.anniu_shape);
			}
			if(strphone.equals("") || strcode.equals("") || strpass.equals("")){
				btn_zhuce.setClickable(false);
				btn_zhuce.setBackgroundResource(R.drawable.anniu_shape1);
			}
		}	    
	};
	public static String IS_OAUTH = "IS_OAUTH";
	private boolean isOAuth = false;
	private String openId;
	private String MarkName;
	private View view;
	private Button btn_zhuce;

	private void initAuto() {
		openId = getActivity().getIntent().getStringExtra(OPEN_ID);
		MarkName = getActivity().getIntent().getStringExtra(MARK_NAME);
		isOAuth = getActivity().getIntent().getBooleanExtra(IS_OAUTH, false);

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

		case R.id.btn_zhuce:
			hideInput();
			doCheckCode(et_code.getText().toString());

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
	// 验证手机号是否可用
	private void CheckPhone() {

		final String sPhone = et_phone.getText().toString().trim();
		if (!CommonUtils.checkNumber(sPhone)) {
			et_phone.setError("请输入正确的手机号码");
			return;
		}
		CacheUtils.cacheStringData(getActivity(), "phone", sPhone);
		String url = ApiUrl.VERIFY_USERNAME + sPhone+"&R="+System.currentTimeMillis();
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
								view.findViewById(R.id.tv_pre).setVisibility(
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
						AppErrorLogUtil.getErrorLog(getActivity().getApplicationContext())
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
		String userType = getActivity().getIntent().getStringExtra(
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
				appInfo = getActivity().getPackageManager().getApplicationInfo(
						getActivity().getPackageName(), PackageManager.GET_META_DATA);
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
					userGuid = info.userInfo.guid;
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
		Intent intent = new Intent(getActivity(),
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 200) {
			if (resultCode == 0x101) {

				Intent data1 = new Intent();
				data1.putExtra("USER", et_phone.getText().toString());
				data1.putExtra("PASSWD", et_password.getText().toString());
				getActivity().setResult(100, data1);
			}

		}
		getActivity().finish();
	}

	protected void doBind() {
		if (dialog != null) {
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
						SJQApp app = (SJQApp) getActivity().getApplication();
						app.refresh();
						Intent intent = new Intent(getActivity(),
								SkipActivity.class);
						startActivity(intent);
						getActivity().finish();
						// String Mobile = et_phone.getText().toString();
						// String sPassword = et_password.getText().toString();
						// go2SelectDentity(Mobile, sPassword, userGuid, true);
						// finish();
					} else {
						toastShow(bean.message);
						getActivity().finish();
					}
				} else {
					toastShow("绑定失败");
					getActivity().finish();
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