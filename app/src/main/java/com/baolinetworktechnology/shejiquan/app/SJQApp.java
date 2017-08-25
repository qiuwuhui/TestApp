package com.baolinetworktechnology.shejiquan.app;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.activity.OAuth1Activity;
import com.baolinetworktechnology.shejiquan.activity.SelectiDentityActivity;
import com.baolinetworktechnology.shejiquan.activity.SkipActivity;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.constant.EnumDientity;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetailBean;
import com.baolinetworktechnology.shejiquan.domain.DesignerZhuanxiu;
import com.baolinetworktechnology.shejiquan.domain.DesignerZxBean;
import com.baolinetworktechnology.shejiquan.domain.GongSiMyanliBean;
import com.baolinetworktechnology.shejiquan.domain.LocationBean;
import com.baolinetworktechnology.shejiquan.domain.Login;
import com.baolinetworktechnology.shejiquan.domain.OwnerDetail;
import com.baolinetworktechnology.shejiquan.domain.OwnerDetailBean;
import com.baolinetworktechnology.shejiquan.domain.ReadMessageBean;
import com.baolinetworktechnology.shejiquan.domain.SwDesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwDesignerDetailBean;
import com.baolinetworktechnology.shejiquan.domain.SwOwnerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwOwnerDetailBean;
import com.baolinetworktechnology.shejiquan.domain.SwUserInfo;
import com.baolinetworktechnology.shejiquan.domain.UserInfo;
import com.baolinetworktechnology.shejiquan.interfaces.AppLoadListener;
import com.baolinetworktechnology.shejiquan.net.GetClassList;
import com.baolinetworktechnology.shejiquan.net.NetMessage;
import com.baolinetworktechnology.shejiquan.net.OnCallBackBean;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.google.gson.Gson;
import com.guojisheng.koyview.utls.MD5Util;
import com.hyphenate.chatuidemo.DemoHelper;
import com.igexin.sdk.PushManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设计圈Application
 * 
 * @author JiSheng.Guo
 * 
 */
public class SJQApp extends Application {
	private static SJQApp instance;
	public static Context applicationContext;
	public static UserInfo user;// 全局用户信息
	public static SwDesignerDetail userData;// 全局用户 设计师账户
	public static SwOwnerDetail ownerData;// 全局用户 业主账户
	public static DesignerZhuanxiu ZhuanxiuData;//全局用户 装修公司账户
	public static LocationBean location;// 地理位置
	public static AMapLocation amapLocation;// 地理位置
	private ArrayList<WeakReference<AppLoadListener>> mOnAppLoadListener = new ArrayList<WeakReference<AppLoadListener>>();
	private boolean mIsLoadOk = false;// 是否加载成功
	private static GetClassList classMap;
	public static boolean isRrefresh = false;
	public static String address = "福建省厦门市思明区";
	public static int fragmentIndex = -1;
	public static City City;
//	public static City sjCity;
	public static int fromWay=0; //QQ==7 微博==6  微信==5
	public static ReadMessageBean messageBean;
	public static String kefuCall="0592-3583299";
	@Override
	public void onCreate() {
		MultiDex.install(this);
		applicationContext = this;
		instance = this;
//		//初始化环信
		DemoHelper.getInstance().init(applicationContext);
		super.onCreate();
		mIsLoadOk = false;// 是否加载成功
		initData();
		initImageLoader();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
			StrictMode.setVmPolicy(builder.build());
			builder.detectFileUriExposure();
		}
	}
	private void initImageLoader() {
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
		ImageLoader.getInstance().init(configuration);

	}
	public static SJQApp getInstance() {
		return instance;
	}
	private void initData() {
		location = new LocationBean();
		refresh();
		setClassMap(new GetClassList(getApplicationContext()));
		String channel = "shejiquan";
		try {
			ApplicationInfo appInfo;
			appInfo = getPackageManager().getApplicationInfo(getPackageName(),
					PackageManager.GET_META_DATA);
			channel = appInfo.metaData.getString("UMENG_CHANNEL");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 System.out.println("-->>channel="+channel);
		TCAgent.init(getApplicationContext(),
				"6B5ADFF5B2DF90CA0F86BC0CC212E9FF", channel);
		TCAgent.LOG_ON = false;
	}


	// 加载用户资料
	// public void loadData() {
	// loadData();
	//
	// }

	/**
	 * 退出当前登录用户
	 */
	public void exitAccount() {
		if (user != null) {
			//XGPushManager.registerPush(getApplicationContext(), "*");
			PushManager geTui = PushManager.getInstance();
			if (geTui != null)
				geTui.unBindAlias(getApplicationContext(), user.guid+"android", true);
		    	geTui.turnOffPush(getApplicationContext());
		}
		user = null;
		userData = null;
		ownerData = null;
		mIsLoadOk = false;
		messageBean =null;
		CacheUtils.cacheStringData(this, ApiUrl.USER_JSON, "");
	}
	/**
	 * 存储
	 * 
	 * @param info
	 */
	public void caceUserInfo(UserInfo info) {
		Intent intent1 = new Intent();
		intent1.setAction("showView");
		sendBroadcast(intent1);
		CacheUtils.cacheStringData(this, ApiUrl.USER_JSON, info.toString());
		System.out.println("------微博登录信息------");
		System.out.println(info.toString());
	}

	// 刷新用户信息
	public void refresh() {
		
		if (user == null) {
			String jsons = CacheUtils.getStringData(getApplicationContext(),
					ApiUrl.USER_JSON, null);
			user = CommonUtils.getDomain(jsons, UserInfo.class);	
		}
		loadUserInfo();
		loadData();
		getIsNoReadMag();
	}

	// 请求账户信息
	private void loadUserInfo() {
		if (user == null) {
			return;
		}

	String userid = user.getGuid();
	HttpUtils httpUtil = new HttpUtils();
	httpUtil.configCurrentHttpCacheExpiry(0);
	String url = AppUrl.GET_USER_INFO + userid;
	Log.e("userGuid", ""+userid);
	RequestCallBack<String> callBack = new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			System.out.println("-->>"+"第三方登录业主资料"+arg0.result.toString());
			JSONObject json;
			SwUserInfo templ=null;
			try {
				json = new JSONObject(arg0.result);
				String result1=json.getString("result");
				Gson gson = new Gson();
				templ=gson.fromJson(result1, SwUserInfo.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (templ != null) {
				if (user == null)
					return;
				user.setBindEmail(templ.isBindEmail);
				user.setBindMobile(templ.isBindMobile);
				user.setBindQQ(templ.isBindQQ);
				user.setBindWeiXin(templ.isBindWeiXin);
				user.setBindWeibo(templ.isBindWeibo);
				caceUserInfo(SJQApp.user);
			}
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			System.out.println("-->>onFailure" + arg0.getExceptionCode()
					+ arg1);

		}
	};
	RequestParams params = new RequestParams();
	params.setHeader("Version", "1.0");
	if (SJQApp.user != null) {
		params.setHeader("Token", SJQApp.user.Token);
	} else {
		params.setHeader("Token", null);
	}
	params.setHeader("AppAgent", "ANDROID_SHEJIQUAN_Ver.1.0");
	params.setHeader("Hash", MD5Util.getMD5String(url + AppTag.MD5));
	httpUtil.send(HttpMethod.GET, AppUrl.API + url, params, callBack);
}

	public void setOnAppLoadListener(AppLoadListener onAppLoadListener) {
		if (onAppLoadListener != null)
			this.mOnAppLoadListener.add(new WeakReference<AppLoadListener>(
					onAppLoadListener));
		if (userData != null && mIsLoadOk) {
			onAppLoadListener.onAppLoadListener(true);
			onAppLoadListener = null;
		}
	}

	boolean isloading = false;

	public boolean isLoading() {
		return isloading;
	}

	/**
	 * 获取用户身份
	 * 
	 * @return
	 */
	public static EnumDientity getUserEnumDientity() {
		if (user != null) {
			if ("DESIGNER".equals(user.markName) || "100".equals(user.markName)) {
				// 设计师
				return EnumDientity.USER_DESIGNER;
			} else if("ENTERPRISE".equals(user.markName) || "100".equals(user.markName)) {
				// 公司
				return EnumDientity.USER_GONGSI;
			}else{
				// 普通个人
				return EnumDientity.USER_OWNER;
			}
		}
		return EnumDientity.ERRO;
	}

	public void loadData() {
		if (isloading) {
			return;
		}
		if (user == null)
			return;
		mIsLoadOk = false;
		isloading = true;
		HttpUtils httpUtil = new HttpUtils();
		httpUtil.configHttpCacheSize(10);
		String url = "";
		String userid = user.guid;
		switch (getUserEnumDientity()) {
		case USER_DESIGNER:// 设计师
			url = AppUrl.GET_DESIGNER_INFO_SHISI + userid;
			System.out.println("-->>"+"第三方登录设计师url"+url);
			break;
		case USER_OWNER:// 普通个人
			url = AppUrl.GET_PERSONAL_INFO + userid;
			break;
		case USER_GONGSI:// 装修公司
			url = ApiUrl.GET_ENTERPRI_INFO + userid;
			break;
		default:
			break;
		}
		RequestParams params = new RequestParams();
		params.setHeader("Token", null);
		params.setHeader("Client", AppTag.CLIENT);
		params.setHeader("Version", ApiUrl.VERSION);
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		params.setHeader("Hash", MD5Util.getMD5String(url + AppTag.MD5));
		httpUtil.send(HttpMethod.GET, AppUrl.API + url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
						isloading = true;
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						mIsLoadOk = false;
						isloading = false;
						LogUtils.i("AppErro ", msg);
						System.out.println("-->>"+"第三方登录设计师资料错误");
						for (int i = 0; i < mOnAppLoadListener.size(); i++) {
							if (mOnAppLoadListener.get(i).get() != null)
								mOnAppLoadListener.get(i).get()
										.onAppLoadListener(false);
						}
						mOnAppLoadListener.clear();
						if (user == null)
							return;
						String urls = "";
						int userid = user.id;
						switch (getUserEnumDientity()) {
						case USER_DESIGNER:// 设计师
							urls = AppUrl.GET_DESIGNER_INFO_SHISI + userid;
							break;
						case USER_OWNER:// 普通个人
							urls = AppUrl.GET_PERSONAL_INFO + userid;
							break;
						case USER_GONGSI:// 装修公司
							urls = ApiUrl.GET_ENTERPRI_INFO + userid;
							break;
						default:
							break;
						}
						AppErrorLogUtil.getErrorLog(getApplicationContext())
								.postError(error.getExceptionCode() + "",
										"GET", urls);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("-->>"+"第三方登录设计师资料"+responseInfo.result.toString());
						isloading = false;
						mIsLoadOk = true;
						if (user == null)
							return;
						switch (getUserEnumDientity()) {
						case USER_DESIGNER:
							initDesigner(responseInfo);
							break;
						case USER_OWNER:
							initPersonal(responseInfo);
							break;
						case USER_GONGSI:// 装修公司
							initZhuanxiu(responseInfo);
							break;
						default:
							break;
						}

						mOnAppLoadListener.clear();

					}

					@SuppressWarnings("unused")
					private void initPersonal(ResponseInfo<String> responseInfo) {
						SwOwnerDetailBean data=CommonUtils.getDomain(
								responseInfo, SwOwnerDetailBean.class);
						System.out.println(data.toString());
						if (data != null) {
							ownerData = data.result;
							for (int i = 0; i < mOnAppLoadListener.size(); i++) {
								if (mOnAppLoadListener.get(i).get() != null)
									mOnAppLoadListener.get(i).get()
											.onAppLoadListener(true);
							       }
						} else {
							for (int i = 0; i < mOnAppLoadListener.size(); i++) {
								if (mOnAppLoadListener.get(i).get() != null)
									mOnAppLoadListener.get(i).get()
											.onAppLoadListener(false);
							}
						}

					}
					@SuppressWarnings("unused")
					private void initDesigner(ResponseInfo<String> responseInfo) {
						SwDesignerDetailBean data = CommonUtils.getDomain(
								responseInfo, SwDesignerDetailBean.class);
						System.out.println("-->>"+"第三方登录aaaa设计师资料"+responseInfo.result.toString());
						if (data != null) {
							userData = data.result;
							for (int i = 0; i < mOnAppLoadListener.size(); i++) {
								if (mOnAppLoadListener.get(i).get() != null) {
									mOnAppLoadListener.get(i).get()
											.onAppLoadListener(true);
								}
							}
						} else {
							for (int i = 0; i < mOnAppLoadListener.size(); i++) {
								if (mOnAppLoadListener.get(i).get() != null)
									mOnAppLoadListener.get(i).get()
											.onAppLoadListener(false);
							}
						}

					}
					private void initZhuanxiu(ResponseInfo<String> responseInfo) {
						DesignerZxBean data = CommonUtils.getDomain(
								responseInfo, DesignerZxBean.class);
						if (data != null) {
							ZhuanxiuData = data.data;
							for (int i = 0; i < mOnAppLoadListener.size(); i++) {
								if (mOnAppLoadListener.get(i).get() != null) {
									mOnAppLoadListener.get(i).get()
											.onAppLoadListener(true);
								}
							}

						} else {
							for (int i = 0; i < mOnAppLoadListener.size(); i++) {
								if (mOnAppLoadListener.get(i).get() != null)
									mOnAppLoadListener.get(i).get()
											.onAppLoadListener(false);
							}
						}

					}
				});		

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
								messageBean =data;
								Intent intent = new Intent();
								intent.setAction("ReadMag");
								getApplicationContext().sendBroadcast(intent);
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
	public static GetClassList
	getClassMap() {
		return classMap;
	}

	public static List<CaseClass> getClassMap(String key) {
		if (classMap != null)
			return classMap.getList(key);
		return null;
	}

	public static void setClassMap(GetClassList classMap) {
		SJQApp.classMap = classMap;
	}

	public static City getCity() {
		return City;
	}

	public static void setCity(City city) {
		City = new City(city.CityID, city.ParentID, city.ParentLevels,
				city.Initial, city.Letter, city.Title);
	}
//	public static City getSjCity() {
//		return sjCity;
//	}

//	public static void setSjCity(City sjcity) {
//		sjCity = new City(sjcity.CityID, sjcity.ParentID, sjcity.ParentLevels,
//				sjcity.Initial, sjcity.Letter, sjcity.Title);
//	}
	public static SJQApp instance() {
		// TODO Auto-generated method stub
		return instance;
	}
	public static int getFromWay() {
		return fromWay;
	}

	public static void setFromWay(int fromWay) {
		SJQApp.fromWay = fromWay;
	}

}
