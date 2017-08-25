package com.baolinetworktechnology.shejiquan.activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.ZhuanXiuFragment;
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
public class MyFragmentActivity extends FragmentActivity {

	
	IBackPressed iBackPressed;

	public interface IBackPressed {
		void backPressed();
	}

	public final static String TYPE = "TYPE";
	public final static int OPUS = 1;// 作品
	public final static int MICRO = 2;// 微名片
	public final static int ENTRUST = 3;// 委托
	public final static int ENTRUST_INFO = 4;// 委托详情

	public final static String USER_GUID = "USER_GUID";// 用户的guid
	/**
	 * 更改设计师-评价
	 */
	public final static int MAIN_OPUS = 12;
	public final static int MAIN_DESIGN = 13;
	public final static int EntrustFastFragment = 14;
	public static final int MAIN_ORDER = 15;
	
	public final static String USER_ID = "USER_ID";// 用户id
	private boolean isOk = false;// 是否显示Fragment

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opus);
		//小米
		CommonUtils.setMiuiStatusBarDarkMode(MyFragmentActivity.this, true);
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
			// String Id = null;
			// XGPushClickedResult click =
			// XGPushManager.onActivityStarted(this);
			// if (click != null) { // 判断是否来自信鸽的打开方式
			// // 根据实际情况处理...
			// // 如获取自定义key-value
			// String json = click.getCustomContent();
			//
			// Id = CommonUtils.getString(json, "content") + "";
			// type = MICRO;
			// }
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			Bundle bundle = new Bundle();
			String userGuid = getIntent().getStringExtra(USER_GUID);
			switch (type) {			
			case MAIN_OPUS:
				ZhuanXiuFragment zhuanxiuFragment=new ZhuanXiuFragment();
				bundle.putBoolean("back", true);
				zhuanxiuFragment.setArguments(bundle);
				transaction.replace(R.id.frameLayout, zhuanxiuFragment);
				break;
			case MAIN_DESIGN:
				MainDesignerFragment mainDesignerFragment = new MainDesignerFragment();
				bundle.putBoolean("back", true);
				mainDesignerFragment.setArguments(bundle);
				transaction.replace(R.id.frameLayout, mainDesignerFragment);
				break;
			case EntrustFastFragment:
				MainCaseFragment mainCaseFragment = new MainCaseFragment();
				bundle.putBoolean("back", true);
				mainCaseFragment.setArguments(bundle);
				transaction.replace(R.id.frameLayout, mainCaseFragment);
				break;
			case MAIN_ORDER:
				MainInspirationFragment mainOrderFragment = new MainInspirationFragment();
				mainOrderFragment.setArguments(bundle);
				transaction.replace(R.id.frameLayout, mainOrderFragment);
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
