package com.baolinetworktechnology.shejiquan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.Configure;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Login;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.FilesManager;
import com.baolinetworktechnology.shejiquan.utils.OAuthHelper;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.guojisheng.koyview.UISwitchButton;
import com.guojisheng.koyview.utls.MD5Util;
import com.hyphenate.EMCallBack;
import com.hyphenate.chatuidemo.DemoHelper;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.qihoo.appstore.updatelib.UpdateManager;
import com.qihoo.appstore.updatelib.UpdateManager.CheckUpdateListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 设置
 * 
 * @author JiSheng.Guo
 * 
 */
public class SettingActivity extends BaseActivity {
	private TextView mTvCachesize, mTvVersionName,tvUsercha,tv_banben;
	ShareUtils mShareUtils;
	private OAuthHelper mOAuthHelper;
	private UISwitchButton switch1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mOAuthHelper =new OAuthHelper(this);
		setTitle("设置");
		mTvCachesize = (TextView) findViewById(R.id.tv_cachesize);
		mTvVersionName = (TextView) findViewById(R.id.tvVersionName);
		tv_banben = (TextView) findViewById(R.id.tv_banben);

		switch1 = (UISwitchButton) findViewById(R.id.switch1);
		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				if (isChecked) {
					CacheUtils.cacheBooleanData(SettingActivity.this,"switch",isChecked);
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
				} else {
					CacheUtils.cacheBooleanData(SettingActivity.this,"switch",isChecked);
					if (SJQApp.user != null) {
						//XGPushManager.registerPush(getApplicationContext(), "*");
						PushManager geTui = PushManager.getInstance();
						if (geTui != null)
							geTui.unBindAlias(getApplicationContext(), SJQApp.user.guid+"android", true);
						geTui.turnOffPush(getApplicationContext());
					}
				}
			}
		});
		Boolean IsWitch =CacheUtils.getBooleanData(SettingActivity.this,"switch",true);
		switch1.setChecked(IsWitch);

		String cacheSize = FilesManager.getAllCache(getApplicationContext());
		mTvCachesize.setText(cacheSize);
		if (SJQApp.user != null) {
			findViewById(R.id.item_exit).setVisibility(View.VISIBLE);
			findViewById(R.id.item_exit).setOnClickListener(this);
			tvUsercha = (TextView) findViewById(R.id.tvUsercha);
			tvUsercha.setOnClickListener(this);
			if (SJQApp.user.isBindMobile) {// 绑定手机号;
				tvUsercha.setText("");
			} else {// 未绑定
				tvUsercha.setText("绑定手机");
			}
			loadUserInfo();
		} else {
			findViewById(R.id.item_exit).setVisibility(View.GONE);
			findViewById(R.id.item_bind).setVisibility(View.GONE);
			findViewById(R.id.item_mima).setVisibility(View.GONE);
		}
		//检查更新版本信息
		getVersionName();
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
	// 请求账户信息
	private void loadUserInfo() {
		if (SJQApp.user == null) {
			return;
		}

		String userGuid = SJQApp.user.guid;
		HttpUtils httpUtil = new HttpUtils();
		String url = ApiUrl.GET_USER_INFO + userGuid + "&r="
				+ System.currentTimeMillis();

		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Login templ = CommonUtils.getDomain(arg0, Login.class);
				if (templ != null && templ.userInfo != null) {
					if (SJQApp.user == null)
						return;
//					SJQApp.user.IsBindEmail = templ.userInfo.IsBindEmail;
					SJQApp.user.isBindMobile = templ.userInfo.isBindMobile;
					SJQApp.user.isBindQQ = templ.userInfo.isBindQQ;
					SJQApp.user.isBindWeiXin = templ.userInfo.isBindWeiXin;
					SJQApp.user.isBindWeibo = templ.userInfo.isBindWeibo;
					((SJQApp) getApplication()).caceUserInfo(SJQApp.user);
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {

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
		httpUtil.send(HttpMethod.GET, ApiUrl.API + url, params, callBack);
	}

//	 获取版本号
	private String getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		String version = "未知";
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version = packInfo.versionName;
			tv_banben.setText("当前版本：V"+version);
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return version;
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.item_share:
			doShare();
			break;
		case R.id.item_about:
			MobclickAgent.onEvent(SettingActivity.this,"kControlSettingEvaluate");//关于我们
			intent.setClass(this, AboutActivity.class);
			// intent.putExtra(WebActivity.URL, ApiUrl.ABOUT);
			startActivity(intent);
			break;
		case R.id.tvUsercha:
			if(tvUsercha.getText().toString().equals("绑定手机")){
				intent.setClass(getActivity(),
						BoundActivity.class);
				startActivity(intent);
		  }
			break;
		case R.id.item_update2:
			dialog.show();
			switch (Configure.updateAppType) {
			case Configure.UPDATE_APP_UMENG:
				doUmenUp();
				break;
			case Configure.UPDATE_APP_360:
				doUmenUp();
				break;
			case Configure.UPDATE_APP_BAIDU:
				doBaiduUp();
				break;
			default:
				break;
			}

			break;
		case R.id.item_cleancache:
			View dialogView2 = View
					.inflate(this, R.layout.dialog_collect, null);
			TextView titl2 = (TextView) dialogView2
					.findViewById(R.id.dialog_title);
			titl2.setText("缓存将全部清空，确定吗？");
			final AlertDialog ad2 = new AlertDialog.Builder(this).setView(
					dialogView2).show();
			dialogView2.findViewById(R.id.dialog_cancel).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							MobclickAgent.onEvent(getActivity(),"kControlSettingCleanCache","no");//设置清理缓存事件  取消
							ad2.cancel();
						}

					});
			dialogView2.findViewById(R.id.dialog_ok).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							MobclickAgent.onEvent(getActivity(),"kControlSettingCleanCache","yes");//设置清理缓存事件  清理
							ad2.cancel();
							FilesManager.cleanAllCache(SettingActivity.this);
							mTvCachesize.setText(FilesManager
									.getAllCache(getApplicationContext()));
							CacheUtils.clear(SettingActivity.this, "List");

						}
					});

			break;
		case R.id.item_exit:
			View dialogView = View.inflate(this, R.layout.dialog_collect, null);
			TextView titl = (TextView) dialogView
					.findViewById(R.id.dialog_title);
			titl.setText("你确定退出当前账号吗？");
			final AlertDialog ad = new AlertDialog.Builder(this).setView(
					dialogView).show();
			dialogView.findViewById(R.id.dialog_cancel).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							ad.cancel();
						}

					});
			dialogView.findViewById(R.id.dialog_ok).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							ad.cancel();
							DemoHelper.getInstance().logout(false,new EMCallBack() {

								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub

								}

								@Override
								public void onProgress(int arg0, String arg1) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onError(int arg0, String arg1) {
									// TODO Auto-generated method stub

								}
							});
							mOAuthHelper.doDeleteWeiChatOauth();
							mOAuthHelper.doDeleteQQOauth();
							mOAuthHelper.doDeleteSinaOauth();
							SJQApp.fragmentIndex = 0;
							((SJQApp) getApplication()).exitAccount();
//							 发送 一个无序广播
							Intent intent = new Intent();
							intent.setAction("ReadMag");
							intent.setAction("showPenyou");
							sendBroadcast(intent);
							Intent i = new Intent(SettingActivity.this,
									SkipActivity.class);
							startActivity(i);
							finish();
						}
					});

			break;

		case R.id.item_advice:
			try {
				String mAddress = "market://details?id=" + getPackageName();
				Intent marketIntent = new Intent("android.intent.action.VIEW");
				marketIntent.setData(Uri.parse(mAddress));
				startActivity(marketIntent);
			} catch (Exception e) {
				toastShow("您尚未安装应用市场，安装后再来吐槽吧");
			}

			// intent.setClass(this, AdviceActivity.class);// 建议
			// startActivity(intent);
			break;
		case R.id.item_bind:
			Intent i = new Intent(SettingActivity.this, BindManageActivity.class);
			startActivity(i);
			break;
		default:
			break;
		}
	}

	private void do360App() {
		System.out.println("-->>do360App");
		
		UpdateManager.checkUpdate(this, new CheckUpdateListener() {

			@Override
			public void onResult(boolean arg0, Bundle arg1) {
				System.out.println("-->>"+arg0);
				dismissDialog();
				UpdateManager.checkUpdate(SettingActivity.this,
						new CheckUpdateListener() {

							@Override
							public void onResult(boolean arg0, Bundle arg1) {
								// TODO Auto-generated method stub

							}

							
							
						}
				
						
						);
				

			}
		});

	}

	private void doUmenUp() {
		// <友盟更新>
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				dialog.dismiss();
				Context mContext = getApplicationContext();
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					// UmengUpdateAgent.showUpdateDialog(mContext,
					// updateInfo);
					break;
				case UpdateStatus.No: // has no update
					Toast.makeText(mContext, "暂时没有发现新的版本", Toast.LENGTH_SHORT)
							.show();
					break;
				case UpdateStatus.NoneWifi: // none wifi

					break;
				case UpdateStatus.Timeout: // time out
					Toast.makeText(mContext, "服务器连接超时", Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		});
		UmengUpdateAgent.forceUpdate(this);

	}

	private void doBaiduUp() {
		// BDAutoUpdateSDK.cpUpdateCheck(this, new CPCheckUpdateCallback() {
		//
		// @Override
		// public void onCheckUpdateCallback(AppUpdateInfo arg0,
		// AppUpdateInfoForInstall arg1) {
		//
		// if (arg0 == null) {
		// dialog.dismiss();
		// toastShow("当前已是最新版本，无需更新");
		// } else {
		// BDAutoUpdateSDK.uiUpdateAction(SettingActivity.this,
		// new UICheckUpdateCallback() {
		//
		// @Override
		// public void onCheckComplete() {
		// // TODO Auto-generated method stub
		// dialog.dismiss();
		// }
		// });
		// }
		//
		// }
		// });

	}

	// 分享动作
	private void doShare() {
		if (mShareUtils == null) {
			mShareUtils = new ShareUtils(this);
		}
		mShareUtils
				.setShareUrl(ApiUrl.SHARE_SHEJIQUAN)
				.setImageUrl(R.drawable.ic_launcher)
				.setShareTitle("设计圈")
				.addToSocialSDK()
				.setShareContent2(
						"让设计更有价值，我正在使用#设计圈#app，让我的装修变成一种享受，推荐给大家"
								+ ApiUrl.SHARE_SHEJIQUAN).doShare();

	}
	@Override
	protected void onStop() {
		super.onStop();
		switch (Configure.updateAppType) {
		case Configure.UPDATE_APP_UMENG:
			UmengUpdateAgent.setUpdateListener(null);
			break;
		case Configure.UPDATE_APP_360:
			break;
		case Configure.UPDATE_APP_BAIDU:
			break;
		default:
			break;
		}

	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SettingActivity");
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SettingActivity");
	}
}
