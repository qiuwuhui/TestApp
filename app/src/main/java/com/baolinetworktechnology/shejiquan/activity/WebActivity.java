package com.baolinetworktechnology.shejiquan.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;

/**
 * 浏览器
 * 
 * @author JiSheng.Guo
 * 
 */
public class WebActivity extends BaseActivity {
	public static final String URL = "WEB_URL";
	public static final String IMAGE_URL = "IMAGE_URL";
	private WebView mWebview;
	private ProgressBar mProgressBar;
	private ShareUtils mShareUtils;
	private TextView title;
	private String webUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		if (!init())
			initView();

		// mWebview.addJavascriptInterface(new WebJavascriptInterface(this),
		// "imagelistner");

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
	/**
	 * 初始化View
	 */
	private void initView() {
		webUrl = getIntent().getStringExtra(URL);
		title = (TextView) findViewById(R.id.tv_title);
		mProgressBar = (ProgressBar) findViewById(R.id.da_pb);
		mWebview = (WebView) findViewById(R.id.webView2);
		findViewById(R.id.back).setOnClickListener(this);// 分享按钮监听
		findViewById(R.id.ib_share).setOnClickListener(this);
        if(webUrl.equals("http://192.168.23.201:10062/scheme/mobile.html")){
			findViewById(R.id.ib_share).setVisibility(View.GONE);
		}
		WebSettings setting = mWebview.getSettings();
		setting.setJavaScriptEnabled(true);// 能够执行JS代码
		setting.setRenderPriority(RenderPriority.HIGH);// 提高渲染的优先级
		setting.setLoadsImagesAutomatically(false);//
		setting.setCacheMode(WebSettings.LOAD_DEFAULT);
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

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.indexOf("Activity/APP_Redirect") != -1) {
					finish();
					return true;
				} else {
					return super.shouldOverrideUrlLoading(view, url);
				}

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (mWebview == null)
					return;
				setTitle(webUrl,view.getTitle());

				if (!mWebview.getSettings().getLoadsImagesAutomatically()) {
					mWebview.getSettings().setLoadsImagesAutomatically(true);
				}
				if (url.indexOf("Activity/APP_Redirect") != -1) {
					finish();
				}

			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				view.stopLoading();
				showErrorPage();// 显示错误页面
			};
		});
		mWebview.setWebChromeClient(new WebChromeClient());
		mWebview.loadUrl(webUrl);

		mWebview.setDrawingCacheEnabled(true);
		LogUtils.i("WebViewURL=", webUrl);

		// 设置分享内容
		mShareUtils = new ShareUtils(this);
		mShareUtils.addToSocialSDK();
		if (webUrl.contains(ApiUrl.ENTRUSTORDER)) {
			mShareUtils
					.setShareUrl("http://m.sjq315.com/activity/mobile/android/entrustorder");
		} else {
			mShareUtils.setShareUrl(webUrl);
		}
		String imageUrl = getIntent().getStringExtra(IMAGE_URL);
		if (imageUrl != null && imageUrl.endsWith("")) {
			mShareUtils.setImageUrl(imageUrl);
		} else {
			mShareUtils.setImageUrl(R.drawable.ic_launcher);
		}
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
	}

	/**
	 * 是否关闭Activity
	 * 
	 * @return
	 */
	private boolean init() {
		String webUrl = getIntent().getStringExtra(URL);

		if ("caselist".equals(webUrl)) {
//			Intent intent = new Intent(this, MyFragmentActivity.class);
//			intent.putExtra(MyFragmentActivity.TYPE,
//					MyFragmentActivity.MAIN_OPUS);
//			startActivity(intent);
//			finish();
			return true;
		} else if ("designerlist".equals(webUrl)) {
			Intent intent = new Intent(this, MyFragmentActivity.class);
			intent.putExtra(MyFragmentActivity.TYPE,
					MyFragmentActivity.MAIN_DESIGN);
			startActivity(intent);
			finish();
			return true;
		} else if ("activity".equals(webUrl)) {
			Intent intent = new Intent(this, MyFragmentActivity.class);
			intent.putExtra(MyFragmentActivity.TYPE,
					MyFragmentActivity.EntrustFastFragment);
			startActivity(intent);
			finish();
			return true;
			// webUrl = ApiUrl.ENTRUSTORDER;
			// if (SJQApp.user != null) {
			// webUrl = ApiUrl.ENTRUSTORDER + "&UserGUID="
			// + SJQApp.user.UserGUID;
			// }
			// setIntent(getIntent().putExtra(URL, webUrl));
		} else if (webUrl.contains("weixin.qq.com")) {
			try {

				Intent intent = new Intent();
				ComponentName cmp = new ComponentName("com.tencent.mm",
						"com.tencent.mm.ui.LauncherUI");
				intent.setAction(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setComponent(cmp);
				startActivityForResult(intent, 0);
				finish();
				return true;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return false;
	}

	public void setTitle(String webUrl,String text) {
		if(webUrl.equals("http://192.168.23.201:10062/scheme/mobile.html")){
			title.setText("用户协议");
		}else{
			title.setText(text);
		}
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				mProgressBar.setVisibility(View.GONE);
			} else {
				if (mProgressBar.getVisibility() == View.GONE)
					mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	// @Override
	public void onClick(View v) {
		// super.onClick(v);
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		case R.id.ib_share:

			mShareUtils.setShareTitle(mWebview.getTitle())
					.setShareContent(mWebview.getTitle()).doShare();
			break;
		}

	}

	/**
	 * 显示自定义错误提示页面，用一个View覆盖在WebView
	 */
	private View mErrorView;

	protected void showErrorPage() {

		if (mErrorView == null) {
			mErrorView = findViewById(R.id.url_error);
			mErrorView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mWebview.reload();
					mErrorView.setVisibility(View.GONE);

				}
			});
		}
		mErrorView.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history

		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
			// 返回键退回

			mWebview.goBack();

			return true;
		} else {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (mShareUtils != null) {
			mShareUtils.onActivityResult(requestCode, resultCode, data);
		}
	}
	// 注入js函数监听
	// private void addImageClickListner() {
	// // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
	// mWebview.loadUrl("javascript:(function(){"
	// + "var objs = document.getElementsByTagName(\"img\"); "
	// + "for(var i=0;i<objs.length;i++)  " + "{"
	// + "    objs[i].onclick=function()  "
	// + "  {"
	// + "        window.imagelistner.openImage(this.src);  "
	// + "    }  "
	// + "}" +
	// "})()");
	// }
}
