package com.baolinetworktechnology.shejiquan.activity;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
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
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MobilePhoneActivity extends BaseActivity implements OnClickListener {
	EditText number_edit;
	Button queding;
	private String number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_phone);
		setTitle("修改绑定");
		number_edit=(EditText) findViewById(R.id.number_edit);
		queding=(Button) findViewById(R.id.queding);
		queding.setOnClickListener(this);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mobile_phone, menu);
		return true;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.queding:
			loadata1();
			break;

		default:
			break;
		}
		super.onClick(v);
	}

	private void loadata1() {
		if (dialog == null)
			dialog = new MyDialog(this);
		String url = ApiUrl.CHECKUSERPWD;
		RequestParams params = getParams(url);
		String pwd = number_edit.getText().toString();
		if(pwd.equals("")){
			toastShow("密码不能为空");
			return;
		}
		params.addBodyParameter("userGuid", SJQApp.user.guid);
		params.addBodyParameter("pwd", pwd);
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
		              Intent intent = new Intent(MobilePhoneActivity.this,
		            		  ModifyphoneActivity.class);
		              startActivity(intent);
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
}
