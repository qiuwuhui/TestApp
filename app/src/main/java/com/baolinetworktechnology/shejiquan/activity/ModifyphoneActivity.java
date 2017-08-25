package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.Conde1;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ModifyphoneActivity extends BaseActivity implements
		OnClickListener {
	EditText number_edit, verify_edit;
	Button gain_butt, queding;
	private Conde1 code1;
	private String number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifyphone);
		setTitle("修改绑定");
		number_edit = (EditText) findViewById(R.id.number_edit);
		verify_edit = (EditText) findViewById(R.id.verify_edit);
		queding = (Button) findViewById(R.id.queding);
		queding.setOnClickListener(this);
		code1 = (Conde1) findViewById(R.id.gain_butt);
		code1.setOnClickListener(this);
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

	private void doQueDing() {
		if (dialog == null)
			dialog = new MyDialog(this);

		String url = ApiUrl.UPDATEBINDINGMOBILE;

		RequestParams params = getParams(url);
		String Mobile = number_edit.getText().toString();
		String Captcha = verify_edit.getText().toString();
		String UserGUID = SJQApp.user.guid;
		params.addBodyParameter("Mobile", Mobile);
		params.addBodyParameter("Captcha", Captcha);
		params.addBodyParameter("Client ", AppTag.CLIENT);
		params.addBodyParameter("UserGUID", UserGUID);

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
					toastShow(bean.message);
					if (bean.success) {
						SJQApp.fragmentIndex = 0;
						((SJQApp) getApplication()).exitAccount();
						Intent i = new Intent(ModifyphoneActivity.this,
								SkipActivity.class);
						startActivity(i);
						finish();
						Intent intent = new Intent();
						intent.setAction("addSuccess");
						// 发送 一个无序广播
						getActivity().sendBroadcast(intent);
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
