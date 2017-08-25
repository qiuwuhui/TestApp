package com.baolinetworktechnology.shejiquan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.Configure;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.ReadMessageBean;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.net.NetMessage;
import com.baolinetworktechnology.shejiquan.net.OnCallBackBean;
import com.baolinetworktechnology.shejiquan.receiver.DemoIntentService;
import com.baolinetworktechnology.shejiquan.receiver.DemoPushService;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.MobclickAgent;

/**
 * 跳转页
 * 
 * @author JiSheng.Guo
 * 
 */
public class SkipActivity extends Activity {

//	private CountDownTimer time;
	View root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skip);
		// 判断是否第一次安装
		logic();
		initView();
		initGeTui();
	}

	// protected void doSplash() {
	// root.setVisibility(View.VISIBLE);
	// Animation scaleAni = AnimationUtils.loadAnimation(SplashActivity.this,
	// R.anim.splash);
	// root.startAnimation(scaleAni);
	//
	// }

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
	}

	// 判断是否第一次使用
	private void logic() {
		    gotoMain();
//			initData();
	}

	void gotoMain() {
			Intent intent = new Intent(SkipActivity.this, MainActivity.class);//
			intent.putExtra(AppTag.TAG_ID,
					getIntent().getIntExtra(AppTag.TAG_ID, 0));
			startActivity(intent);
			finish();
	}

	@Override
	public void finish() {
		// overridePendingTransition(R.anim.splash, R.anim.alpha1);
		super.finish();
	}

	@Override
	protected void onDestroy() {
//		time.cancel();
		super.onDestroy();

	}
	// 初始化个推服务
	private void initGeTui() {
		PushManager.getInstance().initialize(this.getApplicationContext(),DemoPushService.class);
		PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
		PushManager geTui = PushManager.getInstance();
		if (SJQApp.user != null) {
			if (geTui != null) {
				geTui.bindAlias(getApplicationContext(),
						SJQApp.user.guid.replace("-", "") + "android");
				Tag[] tags = new Tag[1];
				Tag tag = new Tag();
				tag.setName(SJQApp.user.markName);
				tags[0] = tag;
				City city = SJQApp.getCity();
				if (city != null) {
					geTui.setTag(getApplicationContext(), tags,city.Title);
				}else{
					geTui.setTag(getApplicationContext(), tags,"");
				}
				geTui.turnOnPush(getApplicationContext());
			}
		}
	}
}
