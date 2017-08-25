package com.baolinetworktechnology.shejiquan.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baolinetwkchnology.shejiquan.xiaoxi.XiangQinActivity;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.DataBen;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetail;
import com.baolinetworktechnology.shejiquan.net.NetAddRecord;
import com.baolinetworktechnology.shejiquan.net.NetGetDesigner;
import com.baolinetworktechnology.shejiquan.net.OnCallBackBean;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.baolinetworktechnology.shejiquan.view.ContactDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class WeiShopWebActivity extends BaseActivity implements
		OnCallBackBean<DesignerDetail> {
	private String designerGudi = "";
	private String designerID = "";
	private WebView mWebview;
	private CheckBox tvConcer;
	private RelativeLayout mLoadingOver;
	private ShareUtils mShareUtils;// 分享工具
	
	private String webUrl;
	boolean isMy = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wei_shop_web);
		designerGudi = getIntent().getStringExtra(AppTag.TAG_GUID);
		designerID = getIntent().getStringExtra(AppTag.TAG_ID);
		initUI();
		if (!TextUtils.isEmpty(designerID)) {
			loadWeb();
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
	private void loadWeb() {
		if (mWebview == null)
			return;
		if (SJQApp.user != null) {
			if (SJQApp.user.guid.equals(designerGudi)) {
				isMy = true;
			}
		}
		webUrl = ApiUrl.HTTP_HEAD + "/Shop/des" + designerID + "/?from=android"+"&R="+System.currentTimeMillis();
		mWebview.loadUrl(webUrl);
		isConcer();
		new NetAddRecord(designerGudi);// 添加到访客记录
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mShareUtils == null) {
			mShareUtils = new ShareUtils(this);
			getData();
		}
	}

	/**
	 * 获取设计师信息
	 */
	private void getData() {
		if (!TextUtils.isEmpty(designerGudi)) {// 根据Guid获取
			new NetGetDesigner(this, NetGetDesigner.UserGUID, designerGudi);
		} else if (!TextUtils.isEmpty(designerID)) {// 根据ID获取
			new NetGetDesigner(this, NetGetDesigner.DesignerID, designerID);
		} else {
			toastShow("数据丢失，无法访问");
			finish();
		}

	}

	// 初始化WebView
	private void initUI() {

		// mProgressBar = (ProgressBar) findViewById(R.id.da_pb);

		mLoadingOver = (RelativeLayout) findViewById(R.id.loading_over2);
		mLoadingOver.setVisibility(View.VISIBLE);
		tvConcer = (CheckBox) findViewById(R.id.tvConcer);

		mWebview = (WebView) findViewById(R.id.webView2);
		WebSettings ws = mWebview.getSettings();
		ws.setJavaScriptEnabled(true);// 能够执行JS代码
		ws.setRenderPriority(RenderPriority.HIGH);// 提高渲染的优先级
		ws.setBlockNetworkImage(true);
		// 设置 缓存模式
		ws.setAppCacheEnabled(true);
		ws.setCacheMode(WebSettings.LOAD_DEFAULT);
		// 开启 DOM storage API 功能
		ws.setDomStorageEnabled(true);

		mWebview.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);

			}
		});
		mWebview.setWebViewClient(new WebViewClient() {
			/*
			 * 此处能拦截超链接的url,即拦截href请求的内容.
			 */

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.indexOf("works") != -1
						|| url.indexOf("xiaoguotu") != -1) {
					Pattern p = Pattern.compile("[1-9][0-9]{3,}");
					String[] ss = url.split("xiaoguotu");
					Matcher m;
					if (ss != null && ss.length >= 2) {
						m = p.matcher(ss[1]);
					} else {
						m = p.matcher(url);
					}

					if (m.find()) {
						Intent intent = new Intent(WeiShopWebActivity.this,
								WebOpusActivity.class);
						String caseUrl = ApiUrl.DETAIL_CASE2 + m.group();
						intent.putExtra(AppTag.TAG_ID, m.group());
						intent.putExtra("WEB_URL", caseUrl);
						startActivity(intent);
						return true;
					}
					// return super.shouldOverrideUrlLoading(view, url);
				} else {
					if (url.indexOf("news") != -1) {
						Pattern p = Pattern.compile("[1-9][0-9]{3,}");
						Matcher m = p.matcher(url);
						if (m.find()) {
							Intent intent = new Intent(WeiShopWebActivity.this,
									WebDetailActivity.class);
							String newUrl = ApiUrl.DETAIL_NEWS + m.group();
							intent.putExtra("WEB_URL", newUrl);
							startActivity(intent);
							return true;
						}
						// return super.shouldOverrideUrlLoading(view, url);
					}

				}
				Intent intent = new Intent(WeiShopWebActivity.this,
						WebActivity.class);
				intent.putExtra(WebActivity.URL, url);
				startActivity(intent);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

				super.onPageStarted(view, url, favicon);

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (mWebview == null)
					return;
				if (mLoadingOver.getVisibility() == View.VISIBLE) {
					Animation animation = AnimationUtils.loadAnimation(
							WeiShopWebActivity.this, R.anim.alpha_out);
					mLoadingOver.setVisibility(View.GONE);
					mLoadingOver.setAnimation(animation);
					mWebview.getSettings().setBlockNetworkImage(false);

				}
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

			};
		});
		mWebview.setWebChromeClient(new WebChromeClient());
		findViewById(R.id.itemConcer).setOnClickListener(this);
		findViewById(R.id.itemWeiTuo).setOnClickListener(this);
		findViewById(R.id.itemCall).setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.share:
			doShare();
			break;
		case R.id.itemConcer:
			doConcer();
			break;
		case R.id.itemWeiTuo:
			Intent intent=new Intent(WeiShopWebActivity.this,XiangQinActivity.class);
			intent.putExtra("UserName", "18050017399");
			startActivity(intent);
			break;
		case R.id.itemCall:
			doCall(v);
			break;
		default:
			break;
		}
	}

	ContactDialog mContactDialog;

	/**
	 * 拨打电话
	 */
	private void doCall(View v) {
		if (SJQApp.user == null) {
			go2Login();
			return;
		}
		if (isMy) {
			toastShow("不能给自己拨号");
			return;
		}
		if (mContactDialog == null)
			mContactDialog = new ContactDialog(this);

		mContactDialog.setData(mDesigner);
		mContactDialog.showAsTop(v);

	}

	/**
	 * 分享
	 */
	private void doShare() {
		if (mShareUtils != null) {
			if (mDesigner != null) {
				mShareUtils
						.setShareUrl(mDesigner.getUrl())
						.setShareTitle(mDesigner.Name + "的微名片")
						.setShareContent(
								mDesigner.getProfession() + "\n"
										+ mDesigner.WorkCompany + "\n"
										+ mDesigner.FromCity)
						.setImageUrl(mDesigner.Logo).addToSocialSDK().doShare();
			} else {
				mShareUtils.setShareUrl(webUrl).setShareTitle("微名片")
						.setShareContent("我发现一个不错的设计师")
						.setImageUrl(R.drawable.ic_launcher).addToSocialSDK()
						.doShare();
			}
		}

	}
	/**
	 * 关注动作
	 */
	public void doConcer() {
		if (SJQApp.user == null) {
			toastShow("请先登录");
			go2Login();
			return;
		}
		if (TextUtils.isEmpty(designerGudi)) {
			toastShow("请稍候");
			return;
		}
		SJQApp.isRrefresh = true;
		if ("已关注".equals(tvConcer.getText().toString())) {
			// 取消关注
			tvConcer.setText("取消中..");
			String url = ApiUrl.FAVORITE_CANCEL;
			RequestParams params = getParams(url);
			params.addBodyParameter("ClassType", "5");
			params.addBodyParameter("Type", "1");
			params.addBodyParameter("UserGUID", SJQApp.user.guid);
			params.addBodyParameter("FromGUID", designerGudi);

			RequestCallBack<String> callBack = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					LogUtils.i("设计师详情头部", arg0.result);
					DataBen data = CommonUtils.getDomain(arg0, DataBen.class);
					if (data != null) {
						if (data.success) {
							tvConcer.setText("关注我");
							tvConcer.setChecked(false);
							if (SJQApp.userData != null) {
								SJQApp.userData.numConcer--;
							}
						} else {
							tvConcer.setText("已关注");
							tvConcer.setChecked(true);
							toastShow("取消失败");
							isConcer();
						}
					} else {
						Toast.makeText(getContext(), "取消失败", Toast.LENGTH_SHORT)
								.show();
						tvConcer.setText("已关注");
						tvConcer.setChecked(true);

					}
				}

				@Override
				public void onFailure(HttpException error, String arg1) {
					LogUtils.i("erro:设计师详情头部", arg1);
					tvConcer.setText("关注我");
					tvConcer.setChecked(false);
					toastShow(getResources().getString(R.string.network_error));
					AppErrorLogUtil.getErrorLog(getContext()).postError(
							error.getExceptionCode() + "", "POST",
							ApiUrl.FAVORITE_CANCEL);
				}
			};
			getHttpUtils().send(HttpMethod.POST, ApiUrl.API + url, params,
					callBack);

		} else {
			// 加关注
			if (SJQApp.user.guid.equals(designerGudi)) {
				toastShow("无需关注自己");
				return;
			}
			tvConcer.setText("关注中..");
			String url = ApiUrl.FAVORITE_ADD;
			RequestParams params = getParams(url);
			params.addBodyParameter("Type", "1");
			params.addBodyParameter("ClassType", "5");
			params.addBodyParameter("UserGUID", SJQApp.user.guid);
			params.addBodyParameter("FromGUID", designerGudi);

			RequestCallBack<String> callBack = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					DataBen data = CommonUtils.getDomain(arg0, DataBen.class);
					if (data != null) {
						if (data.success) {
							tvConcer.setText("已关注");
							if (SJQApp.userData != null) {
								SJQApp.userData.numConcer++;
							}
							tvConcer.setChecked(true);

						} else {
							tvConcer.setText("关注我");
							tvConcer.setChecked(false);

							toastShow("关注失败");
							isConcer();
						}
					} else {
						toastShow("关注失败");
						tvConcer.setText("关注我");
						tvConcer.setChecked(false);

					}
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					tvConcer.setText("关注我");
					tvConcer.setChecked(false);

					toastShow(getResources().getString(R.string.network_error));
				}
			};
			getHttpUtils().send(HttpMethod.POST, ApiUrl.API + url, params,
					callBack);

		}
	}

	/**
	 * 去登录
	 */
	private void go2Login() {
		Intent intent = new Intent(getContext(), LoginActivity.class);
		getContext().startActivity(intent);
	}

	/**
	 * 获取上下文
	 * 
	 * @return
	 */
	private Context getContext() {
		return this;
	}

	/**
	 * 是否关注
	 */
	void isConcer() {
		if (SJQApp.user == null) {

			return;
		}
		if (isMy)
			return;
		String url = ApiUrl.IS_Favorite;
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = getParams(url);
		params.addBodyParameter("Type", "1");
		params.addBodyParameter("ClassType", "5");
		params.addBodyParameter("UserGUID", SJQApp.user.guid);
		if (!TextUtils.isEmpty(designerGudi)) {
			params.addBodyParameter("FromGUID", designerGudi);
		} else if (mDesigner != null) {
			params.addBodyParameter("FromGUID", mDesigner.GUID);
		} else {
			return;
		}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				DataBen data = CommonUtils.getDomain(arg0, DataBen.class);
				if (data != null) {
					if (data.success) {

						tvConcer.setText("已关注");
						tvConcer.setChecked(true);
					} else {
						tvConcer.setText("关注");
						tvConcer.setChecked(false);

					}
				}
			}

			@Override
			public void onFailure(HttpException error, String arg1) {
				AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
						error.getExceptionCode() + "", "POST",
						ApiUrl.IS_Favorite);
			}
		};
		httpUtils.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);

	}

	class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			/*
			 * if (newProgress == 100) { mProgressBar.setVisibility(View.GONE);
			 * } else { if (mProgressBar.getVisibility() == View.GONE)
			 * mProgressBar.setVisibility(View.VISIBLE);
			 * mProgressBar.setProgress(newProgress); }
			 */
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	public void onBackPressed() {
		if (mWebview.canGoBack()) {
			mWebview.goBack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWebview != null) {
			if (mWebview.getRootView() != null) {
				((ViewGroup) mWebview.getRootView()).removeAllViews();
			}
			mWebview.removeAllViews();
			mWebview.destroy();
			mWebview = null;
		}

	}

	@Override
	public void onNetStart() {

	}

	DesignerDetail mDesigner;

	@Override
	public void onSuccess(DesignerDetail bean) {
		mDesigner = bean;
		if (bean != null) {
			if (bean.ID == 0) {
				toastShow("无法查看该用户");
				finish();
			} else {
				if (!TextUtils.isEmpty(bean.GUID)) {
					designerGudi = bean.GUID;
				}

				if (TextUtils.isEmpty(designerID)) {
					designerID = bean.ID + "";
					loadWeb();
				}

			}
		}
	}

	@Override
	public void onFailure(String mesa) {
		toastShow("数据获取失败");
		finish();

	}

	@Override
	public void onError(HttpException arg0, String mesa) {
		toastShow("数据获取失败");
		finish();
	}

	@Override
	public void onError(String json) {
		toastShow("数据获取失败");
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mShareUtils != null)
			mShareUtils.onActivityResult(requestCode, resultCode, data);
	}
}
