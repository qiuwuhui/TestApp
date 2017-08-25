package com.baolinetworktechnology.shejiquan.activity;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.R.menu;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.Conde;
import com.baolinetworktechnology.shejiquan.view.Conde1;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PasswodActivity extends BaseActivity implements OnClickListener {
	private Conde code1;
	private String number;
	EditText number_edit, verify_edit, password_tv;
	Button queding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passwdo_activity);
		setTitle("修改密码");
		code1 = (Conde) findViewById(R.id.gain_butt);
		code1.setOnClickListener(this);
		number_edit = (EditText) findViewById(R.id.number_edit);
		verify_edit = (EditText) findViewById(R.id.verify_edit);
		password_tv = (EditText) findViewById(R.id.password_tv);
		queding = (Button) findViewById(R.id.queding);
		cbPawd = (CheckBox) findViewById(R.id.cbPawd_bd);
		cbPawd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {// //显示密码
					password_tv
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else { // 隐藏密码
					password_tv.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				password_tv.setSelection(password_tv.getText().toString()
						.length());
			}
		});
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
		switch (v.getId()) {
		case R.id.gain_butt:
			number = number_edit.getText().toString();
			if (CommonUtils.checkNumber(number)) {
				code1.start();
				loadata();
			} else {
				toastShow("请输入正确的手机号码");
			}
			break;
		case R.id.queding:
			String verify = verify_edit.getText().toString();
			number = number_edit.getText().toString();
			if (verify.equals("")) {
				toastShow("请输入验证码");
			} else if (CommonUtils.checkNumber(number)) {
				doQueDing();
			} else {
				toastShow("请输入正确的手机号码");
			}
			break;

		default:
			break;
		}
		super.onClick(v);
	}

	MyDialog dialog;
	private CheckBox cbPawd;

	private void doQueDing() {
		if (dialog == null)
			dialog = new MyDialog(this);

		String url = ApiUrl.RESET_PASSWORD;

		RequestParams params = getParams(url);
		String Mobile = number_edit.getText().toString();
		String Captcha = verify_edit.getText().toString();
		String Password = password_tv.getText().toString();
		params.addBodyParameter("Mobile", Mobile);
		params.addBodyParameter("Captcha", Captcha);
		params.addBodyParameter("Password", Password);

		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				dialog.show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dialog.dismiss();
				Bean bean = CommonUtils.getDomain(arg0, Bean.class);
				if (bean != null) {

					if (bean.success) {
						toastShow("修改密码成功");
						SJQApp.fragmentIndex = 0;
						((SJQApp) getApplication()).exitAccount();
						Intent i = new Intent(PasswodActivity.this,
								SkipActivity.class);
						startActivity(i);
						finish();
						Intent intent = new Intent();
						intent.setAction("addSuccess");
						// 发送 一个无序广播
						getActivity().sendBroadcast(intent);
					} else {
						toastShow(bean.message);
					}
				}

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				dialog.dismiss();

			}
		};
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);

	}

	private void loadata() {
		String url = ApiUrl.SEND_SMS_CODE_MO + number
				+ "&template=SheJiQuanCode";
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow(message);

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Bean bean = CommonUtils.getDomain(responseInfo, Bean.class);
				if (bean.success) {

				}
				toastShow(bean.message);
			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);
	}
}
