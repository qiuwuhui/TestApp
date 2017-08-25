package com.baolinetworktechnology.shejiquan.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.baolinetwkchnology.shejiquan.xiaoxi.WeiFragment;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.MainPageAdapter;
import com.baolinetworktechnology.shejiquan.app.Configure;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.DailyBean;
import com.baolinetworktechnology.shejiquan.domain.ReadMessageBean;
import com.baolinetworktechnology.shejiquan.domain.ServiceBean;
import com.baolinetworktechnology.shejiquan.fragment.ContacFragment;
import com.baolinetworktechnology.shejiquan.fragment.MainCaseFragment;
import com.baolinetworktechnology.shejiquan.fragment.MainInspirationFragment;
import com.baolinetworktechnology.shejiquan.fragment.MainMyFragment;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
import com.baolinetworktechnology.shejiquan.net.NetMessage;
import com.baolinetworktechnology.shejiquan.net.OnCallBackBean;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.baolinetworktechnology.shejiquan.view.HomeNavigTab;
import com.baolinetworktechnology.shejiquan.view.HomeNavigTab.NavigTabListener;
import com.baolinetworktechnology.shejiquan.view.NoScrollViewPager;
import com.baolinetworktechnology.shejiquan.view.OpusPopuWindow;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.qihoo.appstore.updatelib.UpdateManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;

/**
 * 主页
 * @author JiSheng.Guo
 */
public class MainActivity extends MPermissionsActivity implements
		OnPageChangeListener, NavigTabListener, OnStateListener,
		AMapLocationListener,LocationSource {
	private  NoScrollViewPager mainViewPage;
	private MainPageAdapter mainPageAdapter;
	public HomeNavigTab mNavigTab;
	private AMapLocationClient mLocationClient=null;
	public  AMapLocationClientOption mLocationOption =null;
	private OnLocationChangedListener mListener =null;
	boolean isXiaoXi =false;
	private View zezao_layout;
	private  TextView message_msg_num;
	private MyBroadcastReciver mybroad;
	private ShareUtils mShareUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.splash, R.anim.alpha1);
		setContentView(R.layout.activity_main);
		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("refresh");
		intentFilter.addAction("ReadMag");
		intentFilter.addAction("delete");
		intentFilter.addAction("showPenyou");
		intentFilter.addAction("denglu");
		intentFilter.addAction("qiehuan");
		intentFilter.addAction("showTong");
		intentFilter.addAction("addpengyou");
		intentFilter.addAction("addFriend");
		intentFilter.addAction("RefreshSQ");
		intentFilter.addAction("HomeCaseShare");
		mybroad=new MyBroadcastReciver();
		MainActivity.this.registerReceiver(mybroad, intentFilter);
		mShareUtils = new ShareUtils(this);
		mShareUtils.addToSocialSDK();
		initView();
		setReadMag();
		getIsNoReadMag();
		MINfFO();
		mNavigTab.setVisibility(View.GONE);
		switch (Configure.updateAppType) {
		case Configure.UPDATE_APP_BAIDU:
		case Configure.UPDATE_APP_UMENG:
			UmengUpdateAgent.update(this);
			break;	
		case Configure.UPDATE_APP_360:
			UpdateManager.checkUpdate(this);
			break;

		default:
			break;
		}
//		checkToken();

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
		// 验证用户是否有效
	private void checkToken() {
		if (SJQApp.user == null)
			return;
		HttpUtils http = new HttpUtils();
		String url = ApiUrl.CHECK_TOKEN;
		RequestParams param = getParams(url);
		param.addBodyParameter("UserGUID", SJQApp.user.guid);
		param.addBodyParameter("Token", SJQApp.user.Token);
		param.addBodyParameter("r", System.currentTimeMillis() + "");
		http.send(HttpMethod.POST, ApiUrl.API + url, param,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Bean bean = CommonUtils.getDomain(arg0, Bean.class);
						if (bean != null) {
							if (bean.success) {
							} else {
								toastShow("您需要重新登录");
								if (MainActivity.this == null)	
									return;
								((SJQApp) getApplication()).exitAccount();
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));
							}
						}
					}
				});
	}

	RequestParams getParams(String url) {

		RequestParams params = new RequestParams();
		if (SJQApp.user != null) {
			params.setHeader("Token", SJQApp.user.Token);
		} else {
			params.setHeader("Token", null);
		}
		params.setHeader("Version", "1.0");
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		params.setHeader("Hash", MD5Util.getMD5String(url + AppTag.MD5));
		return params;

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);       //统计时长
		if (SJQApp.fragmentIndex != -1) {
			mNavigTab.setCheckedTab(SJQApp.fragmentIndex);
			SJQApp.fragmentIndex = -1;
		}
		// 开启动画
		if (mNavigTab.getVisibility() != View.VISIBLE) {
			mNavigTab.setVisibility(View.VISIBLE);
			TranslateAnimation anim = new TranslateAnimation(0, 0, 300, 0);
			anim.setStartOffset(650);
			anim.setDuration(600);
			mNavigTab.startAnimation(anim);
		}
        //注册环信的推送通知
		DemoHelper.getInstance().getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {
			@Override
			public String getDisplayedText(EMMessage message) {
				// 设置状态栏的消息提示，可以根据message的类型做相应提示
				String ticker = EaseCommonUtils.getMessageDigest(message, getApplicationContext());
				if(message.getType() == EMMessage.Type.TXT){
					ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
				}
				EaseUser user = getUserInfo(message.getFrom());
				if(user != null){
					return getUserInfo(message.getFrom()).getNick() + ": " + ticker;
				}else{
					return message.getFrom() + ": " + ticker;
				}
			}

			@Override
			public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
				return "收到"+messageNum + "条消息";
			}

			@Override
			public String getTitle(EMMessage message) {
				return null;
			}

			@Override
			public int getSmallIcon(EMMessage message) {
				return 0;
			}

			@Override
			public Intent getLaunchIntent(EMMessage message) {
				Intent intent = new Intent(getApplicationContext(), SkipActivity.class);

				return intent;
			}
		});
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onStop() {
		super.onStop();
//		stopLocation();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// ((SJQApp) getApplication()).refresh();
	}

//	IBackPressed iBackPressed;
	private MainCaseFragment mMainCaseFrament;//发现
//	private MainHomeFragment mMainHomeFragment;
   private MainInspirationFragment mainInspirationFragment;//首页
	//通讯录
//	private  HuiHuaFragment huiHuaFragment;
	//消息
	private ContacFragment contactFragment;//通讯录
	//社区
//	private PeriodFragment periodafapter;
	//图库
//	private MainNewFragment designerFragment;
//	private MainOrderFragment mMainOrderFragment;
	private MainMyFragment mMainMyFragment;//我的

	// 初始化View
	private void initView() {
		getDaily();
		zezao_layout = findViewById(R.id.zezao_layout);
		zezao_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				zezao_layout.setVisibility(View.GONE);
				CacheUtils.cacheBooleanData(MainActivity.this, "MAINYD",
						false);
				//魅族
				CommonUtils.setMeizuStatusBarDarkIcon(MainActivity.this, true);
				//小米
				CommonUtils.setMiuiStatusBarDarkMode(MainActivity.this, true);
				requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0x0001);
//				requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
//				requestPermission(new String[]{Manifest.permission.CAMERA}, 0x0003);
			}
		});
		boolean isFirst = CacheUtils.getBooleanData(MainActivity.this, "MAINYD", true);
		if(isFirst){
			zezao_layout.setVisibility(View.VISIBLE);
		}else{
		//魅族
		CommonUtils.setMeizuStatusBarDarkIcon(MainActivity.this, true);
		//小米
		CommonUtils.setMiuiStatusBarDarkMode(MainActivity.this, true);
		requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0x0001);
//		requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
//		requestPermission(new String[]{Manifest.permission.CAMERA}, 0x0003);
//		requestPermission(new String[]{Manifest.permission.READ_PHONE_STATE}, 0x0004);
		}
		mainViewPage = (NoScrollViewPager) findViewById(R.id.mainViewPage);
		mainViewPage.setOffscreenPageLimit(5);
		mNavigTab = (HomeNavigTab) findViewById(R.id.navigTab);
		mNavigTab.setOnNavigTabListener(this);
		message_msg_num= (TextView) findViewById(R.id.message_msg_num);
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		mMainCaseFrament=new MainCaseFragment();//发现
//		mMainHomeFragment = new MainHomeFragment();
		mainInspirationFragment =new MainInspirationFragment();
//        huiHuaFragment=new HuiHuaFragment();
		contactFragment =new ContacFragment();//通讯录
        weifragment = new WeiFragment();
//		periodafapter=new PeriodFragment();
//	    mMainOrderFragment = new MainOrderFragment();
		mMainMyFragment = new MainMyFragment();

		fragments.add(mainInspirationFragment);
//		fragments.add(huiHuaFragment);
		fragments.add(mMainCaseFrament);
		//	fragments.add(mMainOrderFragment);
    //	fragments.add(designerFragment);

		fragments.add(mMainMyFragment);
		fragments.add(weifragment);
//		fragments.add(periodafapter);
		fragments.add(contactFragment);


		mainPageAdapter = new MainPageAdapter(getSupportFragmentManager(),
				fragments);
		mainViewPage.setAdapter(mainPageAdapter);
		mainViewPage.setOnPageChangeListener(this);
		// mainViewPage.setPageTransformer(false, new AlphaPageTransformer());

	}
	void toastShow(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}

	// ViewPage 滑动监听事件
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
	// 滑动选中
	@Override
	public void onPageSelected(int index) {

		mNavigTab.setCheckedTab(index);

	}

	// ViewPage 滑动监听事件end

	@Override
	public void onTabCheckedChangeListener(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case 0:// 首页
			//	魅族
			CommonUtils.setMeizuStatusBarDarkIcon(MainActivity.this, true);
			//小米
			CommonUtils.setMiuiStatusBarDarkMode(MainActivity.this, true);
			mainViewPage.setCurrentItem(0, false);
			break;
		case 1:// 聊天
			//	魅族
			CommonUtils.setMeizuStatusBarDarkIcon(MainActivity.this, true);
			//小米
			CommonUtils.setMiuiStatusBarDarkMode(MainActivity.this, true);
			mainViewPage.setCurrentItem(1, false);

			break;
		case 2:// 社区
			if(SJQApp.user == null){
				if (MainActivity.this == null)
					return;
				if(!isXiaoXi){
					((SJQApp) getApplication()).exitAccount();
					startActivity(new Intent(MainActivity.this,
							LoginActivity.class));
					isXiaoXi=true;
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							mainViewPage.setCurrentItem(3, false);
						}
					},500);
				}else{
					mainViewPage.setCurrentItem(3, false);
				}
			}
			else{
			mainViewPage.setCurrentItem(4, false);
			}
			break;
		case 3:// 我的
			//		//魅族
    		CommonUtils.setMeizuStatusBarDarkIcon(MainActivity.this, false);
			//小米
			CommonUtils.setMiuiStatusBarDarkMode(MainActivity.this, false);
			mainViewPage.setCurrentItem(2, false);
			break;
		default:
			break;
		}
		
	}

	// -----导航栏显示与关闭
	private void dissNavig() {
		// 开启动画
		if (mNavigTab.getVisibility() != View.VISIBLE) {
			mNavigTab.setVisibility(View.VISIBLE);
			TranslateAnimation anim = new TranslateAnimation(0, 0, 400, 0);
			anim.setDuration(400);
			anim.setStartOffset(500);
			mNavigTab.startAnimation(anim);
		}
	}

	private void showNavig() {

		if (mNavigTab.getVisibility() != View.GONE) {
			TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 400);
			anim.setDuration(400);

			mNavigTab.startAnimation(anim);
			mNavigTab.setVisibility(View.GONE);
		}
	}

	@Override
	public void onStateListener(boolean isUpScroll) {
		if (isUpScroll) {
			showNavig();
		} else {
			dissNavig();
		}

	}
	private void setReadMag() {
		if (SJQApp.messageBean != null && SJQApp.messageBean.getResult() != null) {
			int UnreadMsgCount =0;
			Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
			List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
				}
			}
			 for (Pair<Long, EMConversation> sortItem : sortList) {
				  EMConversation emConversation=sortItem.second;
				 if(!emConversation.getUserName().equals("sjq-kefu")){
					 UnreadMsgCount += emConversation.getUnreadMsgCount();
				  }
				}
				int numMessage = SJQApp.messageBean.getResult().getNumMessage();
				int numComment = SJQApp.messageBean.getResult().getNumComment();
				int numPostsGood = SJQApp.messageBean.getResult().getNumPostsGood();
				int msgnum = UnreadMsgCount + numMessage + numComment + numPostsGood;
				if (msgnum == 0) {
					message_msg_num.setVisibility(View.GONE);
				} else {
					message_msg_num.setVisibility(View.VISIBLE);
				}
		}else{
			message_msg_num.setVisibility(View.GONE);
		}
	}
	private Handler timeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				int ViewPageItem=mainViewPage.getCurrentItem();
				if(ViewPageItem==4 || ViewPageItem==3){
					mainViewPage.setCurrentItem(4, false);
					mMainCaseFrament.showPenyou();
				}
			}
		}
	};
	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
//		if (iBackPressed != null)
//			iBackPressed.backPressed();
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;
	private static WeiFragment  weifragment;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
//			System.exit(0);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mybroad);
		mainPageAdapter = null;
		mainViewPage = null;
		mNavigTab = null;
		// // 在Service销毁的时候销毁定位资源
//		stopLocation();
		// System.exit(0);
		if(mLocationClient !=null){
			mLocationClient.stopLocation();
		}
	}

	@Override
	public void onTabClickListener(int position) {
		switch (position) {
		case 0:
//			mMainHomeFragment.setTabClick();
			break;
		case 1:
//			mainCaseFragment.setTabClick();
			break;
		case 2:
			//mMainOrderFragment.setTabClick();
			
			break;
		case 3:
//			designerFragment.setTabClick();
			break;
		case 4:
			mMainMyFragment.setTabClick();
			break;
		default:
			break;
		}

	}
	private void getDaily() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String date=sdf.format(new java.util.Date());
		String url = AppUrl.ExpectCover+"beginTime="+date+"&pageSize=8&CurrentPage=1";
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow(message);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject json;
				DailyBean bean=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					CacheUtils.cacheStringData(MainActivity.this, "daily", result1);
					Gson gson = new Gson();
					bean=gson.fromJson(result1, DailyBean.class);
					if(bean!=null){
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		};
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		RequestParams params = new RequestParams();
		params.setHeader("Client", AppTag.CLIENT);
		params.setHeader("Version", ApiUrl.VERSION);
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		params.setHeader("Hash", MD5Util.getMD5String(url + AppTag.MD5));
		httpUtil.send(HttpMethod.GET, AppUrl.API + url, params,
				callBack);
	}
	private void MINfFO() {
		String url = AppUrl.GETCUSTOMINfFO;
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow(message);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject json;
				ServiceBean bean=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, ServiceBean.class);
					if(bean!=null && bean.getService()!=null){
							if (!TextUtils.isEmpty(bean.getService().getPhone())) {
								SJQApp.kefuCall = bean.getService().getPhone();
							}
						}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		};
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		RequestParams params = new RequestParams();
		params.setHeader("Client", AppTag.CLIENT);
		params.setHeader("Version", ApiUrl.VERSION);
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		params.setHeader("Hash", MD5Util.getMD5String(url + AppTag.MD5));
		httpUtil.send(HttpMethod.GET, AppUrl.API + url, params,
				callBack);
	}


	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null) {
			if (amapLocation.getErrorCode() == 0) {
			// 获取位置信息
			Double geoLat = amapLocation.getLatitude();
			Double geoLng = amapLocation.getLongitude();
			String Address=amapLocation.getAddress();	//	获取地理位置	
			if(TextUtils.isEmpty(Address)){
				Address="定位中";
			}
			CacheUtils.cacheStringData(MainActivity.this, "Address",Address);
			String str=amapLocation.getCity();//城市信息
			String weizhi=amapLocation.getProvince()+"-"+amapLocation.getCity();//城市信息
			if(TextUtils.isEmpty(str)){
				str="";
				weizhi="";
			}else{
				CacheUtils.cacheStringData(MainActivity.this, "dingweics",str);
				CacheUtils.cacheStringData(MainActivity.this, "weizhi",weizhi);
			}
			City city=new City(0, 0, 0, "", "", str);
			SJQApp.setCity(city);
			SJQApp.location.geoLat = geoLat;
			SJQApp.location.geoLng = geoLng;
			SJQApp.amapLocation = amapLocation;
			mainInspirationFragment.setonLocation(str);
			}else {
				String errText = "failed to locate," + amapLocation.getErrorCode()+ ": " 
				+ amapLocation.getErrorInfo();
				Log.e("error",errText);
				mainInspirationFragment.setonLocation("");
				}
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;		
	}

	@Override
	public void deactivate() {
		mListener = null;		 	
	}
	public void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1800000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
	}
	private void denlu(String name){
		System.out.println("-----"+name+"-----");
		EMClient.getInstance().login(name, "123456", new EMCallBack() {
			@Override
			public void onSuccess() {
				EMClient.getInstance().groupManager().loadAllGroups();
				EMClient.getInstance().chatManager().loadAllConversations();
				String name="";
				String Logo="";
				String UserName="";
				if(SJQApp.user!=null){
					UserName=SJQApp.user.UserName;
				}
				if(SJQApp.userData!=null){
					name=SJQApp.userData.name;
					Logo=SJQApp.userData.logo;
				}else if(SJQApp.ownerData!=null){
					name=SJQApp.ownerData.getName();
					Logo=SJQApp.ownerData.getLogo();
				}else if(SJQApp.ZhuanxiuData!=null){
					name=SJQApp.ZhuanxiuData.getEnterpriseName();
					Logo=SJQApp.ZhuanxiuData.getLogo();
				}
				DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(name);
				DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(Logo);
				DemoHelper.getInstance().setCurrentUserName(UserName);
			}
			@Override
			public void onProgress(int arg0, String arg1) {
				Message message = Message.obtain();
			}

			@Override
			public void onError(int arg0, String arg1) {
				Message message = Message.obtain();
			}
		});
		Message message = Message.obtain();
		message.what = 1;
		timeHandler.sendMessage(message);
	}
	private class MyBroadcastReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
//			if (action.equals("refresh")) {
//				setReadMag();
//				huiHuaFragment.refresh();
//			}
			//小红点显示
			if(action.equals("ReadMag")){
				setReadMag();
				mMainMyFragment.setReadMag();
			}
			if(action.equals("showPenyou")){
				setReadMag();
				mMainCaseFrament.showPenyou();
				City city = SJQApp.getCity();
				if(city == null){
					mainInspirationFragment.setHomeList("");
				}else{
					if(TextUtils.isEmpty(city.Title)){
						mainInspirationFragment.setHomeList("");
					}else{
						mainInspirationFragment.setHomeList(city.Title);
					}
				 }
			}
			//登录刷新数据
			if(action.equals("denglu")){
				String name = intent.getStringExtra("name");
				denlu(name);
				City city = SJQApp.getCity();
				if(city!=null){
					if(TextUtils.isEmpty(city.Title)){
						mainInspirationFragment.setHomeList("");
					}else{
						mainInspirationFragment.setHomeList(city.Title);
					}
				}else{
					mainInspirationFragment.setHomeList("");
				}

				mMainCaseFrament.showPenyou();
				contactFragment.setShowData();
			}
			//设计师关注通讯录刷新数据
			if(action.equals("qiehuan")){
				City city = SJQApp.getCity();
				if(city !=null){
					if(TextUtils.isEmpty(city.Title)){
						mainInspirationFragment.setHomeList("");
					}else{
						mainInspirationFragment.setHomeList(city.Title);
					}
				}else{
					mainInspirationFragment.setHomeList("");
				}
//				mainViewPage.setCurrentItem(4, false);
//				mNavigTab.setCheckedTab(6);
				contactFragment.setShowData();
			}
			//通讯录删除关注刷新数据
			if(action.equals("delete")){
				City city = SJQApp.getCity();
				if(TextUtils.isEmpty(city.Title)){
					mainInspirationFragment.setHomeList("");
				}else{
					mainInspirationFragment.setHomeList(city.Title);
				}
			}
			//通讯录删除关注刷新数据
			if(action.equals("showTong")){
				int position = intent.getIntExtra("position",-1);
				contactFragment.deleteUser(position);
			}
			//通讯录消除添加好友
			if(action.equals("addpengyou")){
				contactFragment.addpengyou();
			}
			//通讯录更新好友添加
			if(action.equals("addFriend")){
				contactFragment.ShowFriend();
			}
			//刷新社区
			if(action.equals("RefreshSQ")){
				mMainCaseFrament.refreshSQ();
			}
			//效果图分享
			if(action.equals("HomeCaseShare")){
				String id = intent.getStringExtra("id");
				String title = intent.getStringExtra("title");
				String descriptions = intent.getStringExtra("descriptions");
				doShare(id,title,descriptions);
				mShareUtils.doShare();
			}


		}
	}
	private void getIsNoReadMag() {
		if (SJQApp.user != null ) {
			new NetMessage().GetIsNoReadMessage(
					new OnCallBackBean<ReadMessageBean>() {

						@Override
						public void onSuccess(ReadMessageBean data) {

							if (SJQApp.user == null)
								return;
							if (data != null) {
								SJQApp.messageBean =data;
								Intent intent = new Intent();
								intent.setAction("ReadMag");
								sendBroadcast(intent);
							}
						}
						@Override
						public void onNetStart() {
						}
						@Override
						public void onFailure(String mesa) {
						}
						@Override
						public void onError(String json) {
						}
						@Override
						public void onError(HttpException arg0, String mesa) {
						}
					}, SJQApp.user.guid);
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void permissionSuccess(int requestCode) {
		super.permissionSuccess(requestCode);
		switch (requestCode) {
			case 0x0001:
				initLoc();
				break;
//			case 0x0004:
//				String tmDevice="";
//				TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//				if(tm != null){
//					tmDevice = ""+tm.getDeviceId();
//				}
//				CacheUtils.cacheStringData(this, AppTag.APP_TOKEN,tmDevice);
//				break;
		}
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			int ViewPageItem=mainViewPage.getCurrentItem();
			if(ViewPageItem == 4){
				contactFragment.dispatchTouchEvent(ev);
			}
		}
		return super.dispatchTouchEvent(ev);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mShareUtils != null) {
			mShareUtils.onActivityResult(requestCode, resultCode, data);
		}
	}
	/**
	 * 分享
	 */
	public void doShare(String id,String title,String descriptions) {
		if (mShareUtils != null) {
			mShareUtils.setShareUrl("http://m.sjq315.com/works/"+id+".html")
							.setShareTitle(title)
							.setShareContent(descriptions)
							.setImageUrl(R.drawable.ic_launcher);
		}
	}

}
