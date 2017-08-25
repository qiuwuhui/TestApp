package com.baolinetworktechnology.shejiquan.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.baolinetwkchnology.shejiquan.xiaoxi.PinluenFragment;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.ZhuanXiuFragment;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.fragment.MainCaseFragment;
import com.baolinetworktechnology.shejiquan.fragment.MainDesignerFragment;
import com.baolinetworktechnology.shejiquan.fragment.MainInspirationFragment;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 作品 微名片 委托 Fragmen
 * 
 * @author JiSheng.Guo
 * 
 */
public class MyPinLuenActivity extends MPermissionsActivity {

	
	IBackPressed iBackPressed;

	public interface IBackPressed {
		void backPressed();
	}

	public final static String TYPE = "TYPE";
	public final static int OPUS = 1;// 作品
	public static final int PinluenFragment=20;//消息评论界面
	private boolean isOk = false;// 是否显示Fragment

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opus);
		//小米
		CommonUtils.setMiuiStatusBarDarkMode(MyPinLuenActivity.this, true);
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
	public void onResume() {
		super.onResume();
		showFragment(); 
	} 

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	private void showFragment() {
		MobclickAgent.onResume(this); // 统计时长
		if (!isOk) {
			int type = getIntent().getIntExtra(TYPE, OPUS);
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			Bundle bundle = new Bundle();
			switch (type) {
			case PinluenFragment:
				PinluenFragment pinluenFragment=new PinluenFragment();
				pinluenFragment.setArguments(bundle);
				transaction.replace(R.id.frameLayout, pinluenFragment);
			break;
			default:
				break;
			}

			transaction.commit();
			isOk = true;
		}

	}
	@Override
	public void onBackPressed() {
		if (iBackPressed != null) {
			iBackPressed.backPressed();
		} else {
			super.onBackPressed();
		}

	}
}
