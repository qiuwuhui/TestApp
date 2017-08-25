package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.os.Bundle;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;

/**
 * 帮助文档-上传作品
 * @author JiSheng.Guo
 *
 */
public class HelpActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		setTitle("帮助");
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
}
