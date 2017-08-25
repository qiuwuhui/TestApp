package com.baolinetworktechnology.shejiquan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.Login;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.guojisheng.koyview.MyEditText;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class OAuth2Activity extends BaseActivity {
	public static String MARK_NAME = "MARK_NAME";
	
	public static int BIN_OK = 100;
	String openId = "";
	private MyEditText mEtPhone;
	private MyEditText mEtPassword;
	private MyDialog mDialog;
	private String markName = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oauth2);
		setTitle("绑定已有账户");
		TextView title2 = (TextView) findViewById(R.id.tv_title2);
		title2.setText("注册");
		title2.setOnClickListener(this);
		openId = getIntent().getStringExtra(OAuth1Activity.OPEN_ID);
		markName = getIntent().getStringExtra(MARK_NAME);
		findViewById(R.id.btn_login).setOnClickListener(this);
		mEtPhone = (MyEditText) findViewById(R.id.et_phone);
		mEtPassword = (MyEditText) findViewById(R.id.et_password);
		mDialog = new MyDialog(this);
		mEtPhone.setText(CacheUtils.getStringData(getActivity(), "phone", ""));
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
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_title2:
			Intent intent = new Intent(this, SelectiDentityActivity.class);
			intent.putExtra(SelectiDentityActivity.OPEN_ID, openId);
			intent.putExtra(SelectiDentityActivity.MARK_NAME, markName);
			intent.putExtra(SelectiDentityActivity.IS_OAUTH,true);
			startActivityForResult(intent, 500);
			break;
		case R.id.btn_login:
			doLogin();
			break;
		default:
			break;
		}
	}
	
	
	
	

	private void doLogin() {
		String sPhone = mEtPhone.getText().toString().trim();
		String sPassword = mEtPassword.getText().toString().trim();
		if (!CommonUtils.checkNumber(sPhone)) {
			mEtPhone.setError("请输入正确的手机号码");
			return;
		}
		CacheUtils.cacheStringData(getActivity(), "phone", sPhone);
		if (sPassword.length() < 6) {
			mEtPassword.setError("请输入正确的密码");
			return;
		}
		RequestParams params = getParams(ApiUrl.LOGING);
		params.addBodyParameter("UserName", sPhone);
		params.addBodyParameter("Password", sPassword);
		params.addBodyParameter("Client", AppTag.CLIENT);

		getHttpUtils(10).send(HttpMethod.POST, ApiUrl.API + ApiUrl.LOGING,
				params, new RequestCallBack<String>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						mDialog.show();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						toastShow("网络请求失败");
						mDialog.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						mDialog.dismiss();
						Login info = CommonUtils.getDomain(responseInfo,
								Login.class);
						if (info == null) {
							toastShow("服务器繁忙");
							return;
						}

						if (info.success) {
							if (info.userInfo != null) {

								doOAuth(info);

							}
						} else {
							toastShow(info.operatMessage);
						}

					}
				});

	}

	protected void doOAuth(final Login info) {
		mDialog.show("正在绑定");

		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				mDialog.dismiss();
				Bean bean = CommonUtils.getDomain(arg0, Bean.class);
				if (bean != null) {

					if (bean.success) {
						CacheUtils.cacheStringData(getActivity(), "phone", mEtPhone.getText().toString());
						toastShow("绑定成功");
						// 将用户信息保存本地
						((SJQApp) getApplication()).caceUserInfo(info.userInfo);
						// 刷新数据
						((SJQApp) getApplication()).refresh();
						Intent intent =new Intent(OAuth2Activity.this, SkipActivity.class);
						startActivity(intent);
						finish();
//						setResult(BIN_OK);
//						finish();
					} else {
						toastShow(bean.message);
					}
				} else {
					toastShow("绑定失败");
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				toastShow("网络请求失败");
				mDialog.dismiss();
			}
		};
		String url = ApiUrl.AddUserThirdInfo;
		RequestParams params = getParams(url);
		params.addBodyParameter("OpenID", openId);
		params.addBodyParameter("UserGUID", info.userInfo.guid);
		params.addBodyParameter("MarkName", markName);
		params.addBodyParameter("Client", AppTag.CLIENT);
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);

	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==500){
			if(resultCode==BIN_OK){
				setResult(BIN_OK);
				finish();
			}
		}
	}
}
