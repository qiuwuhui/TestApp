package com.baolinetworktechnology.shejiquan.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.RegisterFragment;
import com.baolinetworktechnology.shejiquan.adapter.MainPageAdapter;
import com.baolinetworktechnology.shejiquan.fragment.LoginFastFragment;
import com.baolinetworktechnology.shejiquan.fragment.LoginFragment;
import com.baolinetworktechnology.shejiquan.view.FixedSpeedScroller;
import com.baolinetworktechnology.shejiquan.view.HomeViewPager;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
 

/**
 * 登陆
 * 
 * @author JiSheng.Guo
 * 
 */
public class LoginActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener{
	private HomeViewPager mMainViewPage;
//	private LoginFragment mLoginFragment;
	private LoginFastFragment mlLoginFastFragment;
	public MyDialog mDialog;
	public static String IS_SPLASH = "IS_SPLASH";// 是否显示欢迎界面
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.aty_slide_bottom_in, R.anim.alpha1);
		setContentView(R.layout.activity_login);
		initView();
		// startActivity(new Intent(this, SelectiDentityActivity.class));
	}

	// 初始化View
	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		mMainViewPage = (HomeViewPager) findViewById(R.id.viewPage);
		mMainViewPage.setOnPageChangeListener(this);
		setViewPagerScrollSpeed();
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
//		mLoginFragment = new LoginFragment();
		mlLoginFastFragment = new LoginFastFragment();
//		fragments.add(mLoginFragment);
//		fragments.add(new RegisterFragment());
		fragments.add(mlLoginFastFragment);

		mMainViewPage.setAdapter(new MainPageAdapter(
				getSupportFragmentManager(), fragments));
		mDialog = new MyDialog(this);
		mDialog.setCancelable(false);
//		mMainViewPage.setOnPageChangeListener(this);

	}

	private void setViewPagerScrollSpeed() {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(this, 600);
			mScroller.set(mMainViewPage, scroller);
		} catch (NoSuchFieldException e) {

		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_reginer:
			startRegisterActivity();
			break;
		case R.id.back:
			finish();
//			if (mMainViewPage.getCurrentItem() == 0) {
//				finish();
//			} else {
//				mMainViewPage.setCurrentItem(0);
//				mIvBack.setImageResource(R.drawable.ic_login_finsh);
//			}
			break;
		case R.id.rbCass:
			mMainViewPage.setCurrentItem(0);
			break;
        case R.id.rbNew:
        	mMainViewPage.setCurrentItem(1);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (mMainViewPage.getCurrentItem() == 0) {
			finish();
		} else {
			mMainViewPage.setCurrentItem(0);
			// mIvBack.setImageResource(R.drawable.ic_login_finsh);
		}
	}

	public void onResume() {
		super.onResume();
		//MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
	//	MobclickAgent.onPause(this);
	}

	//账号登录
	public void startFastFragment() {
		mMainViewPage.setCurrentItem(0);
		// mIvBack.setImageResource(R.drawable.ic_login_back);
	}
	// 快速登录
	public void startFastFragment1() {
		mMainViewPage.setCurrentItem(2);
			// mIvBack.setImageResource(R.drawable.ic_login_back);
    }
	
	// 跳转注册界面
	public void startRegisterActivity() {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivityForResult(intent, 200);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		mLoginFragment.onResult(requestCode, resultCode, data);
		mlLoginFastFragment.onResult(requestCode, resultCode, data);
//		if (requestCode == 200) {
			if (resultCode == 100) {
				if (data != null) {
					// 设置返回数据
//					mLoginFragment.setLoginData(data.getStringExtra("USER"),
//							data.getStringExtra("PASSWD"));
				}
//			}
		}	

	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.alpha1, R.anim.aty_slide_bottom_out);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDialog = null;

	}

	public void finish(boolean b) {
		if (getIntent().getBooleanExtra(IS_SPLASH, false)) {
			startActivity(new Intent(this, SkipActivity.class));
		}
		finish();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int page) {

	}

	/**
	 * 关闭输入法弹窗
	 */
	public void hideInput() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (this.getCurrentFocus() != null)
			inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

	}
}
