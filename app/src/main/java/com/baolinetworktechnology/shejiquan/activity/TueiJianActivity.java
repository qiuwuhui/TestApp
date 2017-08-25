package com.baolinetworktechnology.shejiquan.activity;

import java.util.ArrayList;

import com.baolinetwkchnology.shejiquan.xiaoxi.HuiHuaFragment;
import com.baolinetworktechnology.shejiquan.CollectsjFragment;
import com.baolinetworktechnology.shejiquan.CollectzxFragment;
import com.baolinetworktechnology.shejiquan.CompanyFragment;
import com.baolinetworktechnology.shejiquan.ProjectFragment;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.adapter.MainPageAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.fragment.CollectCaseFragment;
import com.baolinetworktechnology.shejiquan.fragment.CollectNewsFragment;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.HomeViewPager;
import com.umeng.analytics.MobclickAgent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class TueiJianActivity extends MPermissionsActivity implements
OnClickListener, OnPageChangeListener {
	private ViewPager mViewPage;
	private RadioButton mRB1;
	private RadioButton mRB2;
	private View tuei_view;
	private View tuei_view1;
	private int type;
	public static String TYPE = "TYPE";
	
	public final static int OWNER_ENTRUST = 1;// 推荐页面
	public final static int OWNER_ORDER = 2;//   我的收藏
	public final static int DESIGNER_ENTRUST = 3; //我的关注
	public final static int DESIGNER_ORDER = 4;
	private TextView tv_title;
	private ImageView back;
	private MyBroadcastReciver mybroad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuei_jian);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("refresh");
		mybroad=new MyBroadcastReciver();
		this.registerReceiver(mybroad, intentFilter);

		//魅族
		CommonUtils.setMeizuStatusBarDarkIcon(TueiJianActivity.this, true);
		//小米
		CommonUtils.setMiuiStatusBarDarkMode(TueiJianActivity.this, true);
		mViewPage = (ViewPager) findViewById(R.id.viewPager);
		mViewPage.setOnPageChangeListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		back.setVisibility(View.GONE);
		mRB1 = (RadioButton) findViewById(R.id.rbCass);
		mRB2 = (RadioButton) findViewById(R.id.rbNew);
		mRB1.setOnClickListener(this);
		mRB2.setOnClickListener(this);
		tuei_view  =findViewById(R.id.tuei_view);
		tuei_view1 =findViewById(R.id.tuei_view1);
		tuei_view1.setVisibility(View.GONE);
		type = getIntent().getIntExtra(TYPE, 0);
		switch (type) {
		case OWNER_ENTRUST:
			initDesiTueiJian();
			break;
		case OWNER_ORDER:
			initDesiZhuanxiu();
			break;
		case DESIGNER_ENTRUST:
			initDesiguanzhu();
			break;
		default:
			break;
		}
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
	private HuiHuaFragment huiHuaFragment;
	private void initDesiguanzhu() {
		mRB1.setText("设计师");
		mRB2.setText("装修公司");
		tv_title.setText("我的消息");
		back.setVisibility(View.VISIBLE);
		huiHuaFragment = new HuiHuaFragment();
//		CollectsjFragment CollectsjFragment = new CollectsjFragment();
//		CollectzxFragment CollectzxFragment = new CollectzxFragment();
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(huiHuaFragment);
//		fragments.add(CollectzxFragment);
		mViewPage.setAdapter(new MainPageAdapter(getSupportFragmentManager(),
				fragments));
	}

	private void initDesiZhuanxiu() {
		mRB1.setText("案例");
		mRB2.setText("攻略");
		tv_title.setText("我的收藏");
		back.setVisibility(View.VISIBLE);
		CollectCaseFragment mCollectCaseFragment = new CollectCaseFragment();
//		CollectNewsFragment mCollectNewsFragment = new CollectNewsFragment();
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(mCollectCaseFragment);
//		fragments.add(mCollectNewsFragment);
		mViewPage.setAdapter(new MainPageAdapter(getSupportFragmentManager(),
				fragments));
	}

	private void initDesiTueiJian() {
		mRB1.setText("装修公司推荐");
		mRB2.setText("设计师推荐");
		tv_title.setText("我的装修需求");
		boolean isFirst = CacheUtils.getBooleanData(this, "APP_TIAO", false);
		if(isFirst){
			back.setVisibility(View.GONE);			
		}else{
			back.setVisibility(View.VISIBLE);
		}
		CompanyFragment companyFragment = new CompanyFragment();
		ProjectFragment projectFragment = new ProjectFragment();	
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(companyFragment);
		fragments.add(projectFragment);
		mViewPage.setAdapter(new MainPageAdapter(getSupportFragmentManager(),
				fragments));
     	mViewPage.setCurrentItem(1);
    	tuei_view.setVisibility(View.GONE);
    	tuei_view1.setVisibility(View.VISIBLE);
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
		switch (page) {
		case 0:
			mRB1.setChecked(true);
			tuei_view.setVisibility(View.VISIBLE);
			tuei_view1.setVisibility(View.GONE);
			break;
		case 1:
			mRB2.setChecked(true);
			tuei_view.setVisibility(View.GONE);
			tuei_view1.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rbCass:
			mViewPage.setCurrentItem(0);
			tuei_view.setVisibility(View.VISIBLE);
			tuei_view1.setVisibility(View.GONE);
			break;
        case R.id.rbNew:
        	mViewPage.setCurrentItem(1);
        	tuei_view.setVisibility(View.GONE);
        	tuei_view1.setVisibility(View.VISIBLE);
			break;
        case R.id.back:
        	Intent intent = new Intent(TueiJianActivity.this, MainActivity.class);//
			intent.putExtra(AppTag.TAG_ID,
					getIntent().getIntExtra(AppTag.TAG_ID, 0));
			startActivity(intent);
			finish();
			break;
			
		default:
			break;
		}
		
	}
	private class MyBroadcastReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("refresh")) {
				if(huiHuaFragment != null){
					huiHuaFragment.refresh();
				}
			}

		}
	}
	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		return false;
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("TueiJianActivity");
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("TueiJianActivity");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mybroad);
	}
}
