package com.baolinetworktechnology.shejiquan.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.Configure;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppStatusManager;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.receiver.DemoIntentService;
import com.baolinetworktechnology.shejiquan.receiver.DemoPushService;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.umeng.analytics.MobclickAgent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 启动页
 * 
 * @author JiSheng.Guo
 * 
 */
public class SplashActivity extends Activity {
	private CountDownTimer time;
	private boolean isDestiry;
	ImageView icLogo;
	View root;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL);//进入应用初始化设置成未登录状态
		// 判断是否第一次安装
		logic();
		initView();
		initLogo();
		initGeTui();
		getDeviceInfo(this);
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
		CityService.getInstance(getApplicationContext());
		String AtyName = getRunningActivityName();
		MobclickAgent.onPageStart(AtyName); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
		// TCAgent.onPageStart(this, AtyName);
	}

	private void initView() {
		icLogo = (ImageView) findViewById(R.id.ivLogo);
		root = findViewById(R.id.root);
		// root.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		String AtyName = getRunningActivityName();
		MobclickAgent.onPageEnd(AtyName); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		MobclickAgent.setDebugMode( true );
//		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.onPause(this);
		// TCAgent.onPageEnd(this, AtyName);
	}

	private void initLogo() {
		TextView tvAlpha = (TextView) findViewById(R.id.tvAlpha);
		tvAlpha.setVisibility(View.GONE);
		icLogo.setVisibility(View.GONE);
		if (!AppUrl.API.equals("http://appapi.sjq315.com") && !AppUrl.API.equals("http://appapi.sjq315.com/V2.4.0")) {
			// 内测版
			tvAlpha.setVisibility(View.VISIBLE);
			tvAlpha.setText("内测版:" + getVersionName());//
		}
		try {
			ApplicationInfo appInfo;
			appInfo = getPackageManager().getApplicationInfo(getPackageName(),
					PackageManager.GET_META_DATA);
			String channel = appInfo.metaData.getString("UMENG_CHANNEL");
			if ("360app".equals(channel)) {
				// icLogo.setVisibility(View.VISIBLE);
				// icLogo.setImageResource(R.drawable.ic_360logo);
				Configure.updateAppType = Configure.UPDATE_APP_360;
			} else if ("baidu".equals(channel)) {
				// Configure.updateAppType = Configure.UPDATE_APP_BAIDU;
			}

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 初始化个推服务
	private void initGeTui() {
		PushManager.getInstance().initialize(this.getApplicationContext(),DemoPushService.class);
		PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
		PushManager geTui = PushManager.getInstance();
		if (SJQApp.user != null) {
			if (geTui != null) {
				geTui.bindAlias(getApplicationContext(),
						SJQApp.user.guid.replace("-","") + "android");
				Tag[] tags = new Tag[1];
				Tag tag = new Tag();
				tag.setName(SJQApp.user.markName);
				tags[0] = tag;
				geTui.setTag(getApplicationContext(), tags,"");
				geTui.turnOnPush(getApplicationContext());
			}
		}
	}

	private void initData() {
		time = new CountDownTimer(2000, 2000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}
			@Override
			public void onFinish() {
				gotoMain();
			}
		};
		time.start();
	}

	// 获取版本号
	private String getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		String version = "未知";
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return version;
	}

	String APP_FIRST = "APP_FIRST";

	// 判断是否第一次使用
	private void logic() {

		boolean isFirst = CacheUtils.getBooleanData(this, APP_FIRST, false);
		if (!isFirst) {
			go2GuideActivity();
			return;
		}else{
			initData();
		}
	}

	void gotoMain() {
		if (!isDestiry) {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);//
			intent.putExtra(AppTag.TAG_ID,
					getIntent().getIntExtra(AppTag.TAG_ID, 0));
			startActivity(intent);
			finish();
		}
	}

	private void go2GuideActivity() {
		time = new CountDownTimer(2000, 2000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}
			@Override
			public void onFinish() {
				Intent intent = new Intent(SplashActivity.this, GuideActivity.class);//		
				startActivity(intent);
				finish();
			}
		};

		time.start();

	}

	@Override
	public void finish() {
		// overridePendingTransition(R.anim.splash, R.anim.alpha1);
		super.finish();
	}

	@Override
	protected void onDestroy() {
		isDestiry = true;
		time.cancel();
		super.onDestroy();

	}

	// 获取当前Activity 名称
	private String getRunningActivityName() {
		String contextString = this.toString();
		return contextString.substring(contextString.lastIndexOf(".") + 1,
				contextString.indexOf("@"));
	}

	public static boolean checkPermission(Context context, String permission) {
		boolean result = false;
		if (Build.VERSION.SDK_INT >= 23) {
			try {
				Class<?> clazz = Class.forName("android.content.Context");
				Method method = clazz.getMethod("checkSelfPermission", String.class);
				int rest = (Integer) method.invoke(context, permission);
				if (rest == PackageManager.PERMISSION_GRANTED) {
					result = true;
				} else {
					result = false;
				}
			} catch (Exception e) {
				result = false;
			}
		} else {
			PackageManager pm = context.getPackageManager();
			if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
				result = true;
			}
		}
		return result;
	}
	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String device_id = null;
			if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
				device_id = tm.getDeviceId();
			}
			String mac = null;
			FileReader fstream = null;
			try {
				fstream = new FileReader("/sys/class/net/wlan0/address");
			} catch (FileNotFoundException e) {
				fstream = new FileReader("/sys/class/net/eth0/address");
			}
			BufferedReader in = null;
			if (fstream != null) {
				try {
					in = new BufferedReader(fstream, 1024);
					mac = in.readLine();
				} catch (IOException e) {
				} finally {
					if (fstream != null) {
						try {
							fstream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			json.put("mac", mac);
			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}
			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}
			json.put("device_id", device_id);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
