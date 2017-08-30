package com.baolinetworktechnology.shejiquan.activity;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.SwDetailBen;
import com.baolinetworktechnology.shejiquan.domain.SwNewsDetailBen;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.fragment.WebJavascriptInterface;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyRelativeLayout;
import com.google.gson.Gson;
import com.guojisheng.koyview.GScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.utils.Log;
import com.wedviewdb.DAOHelper;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 资讯详情页
 * 
 * @author JiSheng.Guo
 * 
 */
public class WebDetailActivity extends BaseActivity {

	public static final String ID = "ID";
	public static final String GUID = "GUID";
	public static final String ISCASE = "CASE";
	private WebView mWebview;
	private TextView mTvDesc, mTvComment,liulang_tv,daodu_tv,detaName,timeTV;
	private RelativeLayout mLoadingOver;
	private CheckBox mCollect;
	private InputMethodManager imm;
	private ShareUtils mShareUtils;
	private String mUserGuid = "";
	private String mFromGUID;
	private String id;
	private MyRelativeLayout mRoot1;
	private CircleImg userLogo;
	private BitmapUtils mImageUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_detail);
		//数据库操作类
		helper = new DAOHelper(WebDetailActivity.this);
		mImageUtil = new BitmapUtils(this);
		mImageUtil.configDefaultLoadingImage(R.drawable.menu_icon_mrtx_default);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.menu_icon_mrtx_default);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		initView();
		id = getIntent().getStringExtra(ID);

		// 加载需要显示的网页
		loadata();
//		LoadContents(url);
		initShare();
		//GScrollView 滑动监听

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
	// 初始化 分享平台
	private void initShare() {
		mShareUtils = new ShareUtils(this);
		mShareUtils.addToSocialSDK();
	}

	// 初始化View
	private void initView() {
		mWebview = (WebView) findViewById(R.id.webView3);
		WebSettings settings = mWebview.getSettings();
		settings.setJavaScriptEnabled(true);// 能够执行JS代码
		settings.setRenderPriority(RenderPriority.HIGH);//
		// 提高渲染的优先级
		settings.setBlockNetworkImage(true);
		// 设置 缓存模式
		settings.setAppCacheEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		// 开启 DOM storage API 功能
		settings.setDomStorageEnabled(true);
		// false先不加载图片
//		mWebview.getSettings().setLoadsImagesAutomatically(false);

		mLoadingOver = (RelativeLayout) findViewById(R.id.loading_over);
		mTvDesc = (TextView) findViewById(R.id.tv_desc);
		mTvDesc.setFocusable(true);
		mTvDesc.setFocusableInTouchMode(true);
		mTvDesc.requestFocus();
		mCollect = (CheckBox) findViewById(R.id.collect);
		liulang_tv = (TextView) findViewById(R.id.liulang_tv);
		daodu_tv = (TextView) findViewById(R.id.daodu_tv);
		mTvComment = (TextView) findViewById(R.id.tvComment);
		mTvComment.setOnClickListener(this);
		userLogo = (CircleImg) findViewById(R.id.userLogo);
		detaName = (TextView) findViewById(R.id.detaName);
		timeTV = (TextView) findViewById(R.id.timeTV);
		item_pingluen = findViewById(R.id.item_pingluen);
		pingluen_text = (EditText) findViewById(R.id.pingluen_text);
		pingluen_text.clearFocus();
		pingluen_text.addTextChangedListener(watcher);  
		fasong_btn = (TextView) findViewById(R.id.fasong_btn);
		fasong_btn.setOnClickListener(this);
		imm = (InputMethodManager) pingluen_text.getContext().getSystemService(
				this.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);
		mRoot1 = (MyRelativeLayout) findViewById(R.id.mRoot1);
		mRoot1.setFitsSystemWindows(true);
		mCollect.setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
		findViewById(R.id.ivErro).setOnClickListener(this);
		findViewById(R.id.item_comment).setOnClickListener(this);
		mWebview.setWebViewClient(new WebViewClient() {
			// @Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (mWebview == null)
					return;
				// mWebview.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
				if (mWebview.getSettings().getBlockNetworkImage()) {
					addImageClickListner();
					Animation out = AnimationUtils.loadAnimation(
							WebDetailActivity.this, R.anim.alpha_out);
					mLoadingOver.setVisibility(View.GONE);
					mLoadingOver.setAnimation(out);
					mWebview.getSettings().setBlockNetworkImage(false);
				}
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				mWebview.loadUrl(url); //在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
				return true;
			}
            //加载https时候，需要加入 下面代码
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				Log.d("onReceivedSslError: ",""+error.getPrimaryError());
				handler.proceed(); //接受所有证书
			}
	    });
		mRScroll = (GScrollView) findViewById(R.id.scroll);
		findViewById(R.id.il).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRScroll.fullScroll(ScrollView.FOCUS_UP);
			}
		});

		mWebview.addJavascriptInterface(new WebJavascriptInterface(this),
				"imagelistner");
	}

	@JavascriptInterface
	public void resize(final float height) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				mWebview.setLayoutParams(new LinearLayout.LayoutParams(
						getResources().getDisplayMetrics().widthPixels,
						(int) (height * getResources().getDisplayMetrics().density)));
			}
		});
	}
	private void loadata() {
		if(id==null){
			toastShow("获取内容出错");
			finish();
			return;
		}
		mLoadingOver.setVisibility(View.VISIBLE);
		String url = AppUrl.DETAILINFO+id;
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow("没有获取到内容");
				finish();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				SwNewsDetailBen newsDetailBen = CommonUtils.getDomain(responseInfo,
						SwNewsDetailBen.class);
				if (newsDetailBen == null || newsDetailBen.result == null) {
					toastShow("没有获取到内容");
					finish();
					return;
				}

				if (newsDetailBen.result.contents.equals("")) {
					mLoadingOver.setVisibility(View.GONE);

				} else {
					if(newsDetailBen.result!=null || newsDetailBen.result.contents!=null){
						StringBuffer htmlString= new StringBuffer();
						htmlString.append("<!DOCTYPE html><html><head>");
						htmlString.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
						htmlString.append("<meta name=\"description\" />");
						htmlString.append("<meta name=\"keywords\" />");
						htmlString.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=0.1, maximum-scale=5.0, user-scalable=no\" />");
						htmlString.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">");
						htmlString.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content='black'>");
						htmlString.append("<meta name=\"format-detection\" content=\"telephone=no\">");
						htmlString.append("<script type=\"text/javascript\" src=\"http://m.sjq315.com/Scripts/jquery-1.10.2.min.js\"></script>");
						htmlString.append("<style type=\"text/css\">");
						htmlString.append("body{ font-size:16px; line-height:1.5em; padding:12px 0px; color:#3c3c46;}");
						htmlString.append("img { max-width:98%;padding:0.2em 0;width:auto!important;height:auto!important;}");
						htmlString.append("p{padding:0 2px;}");
						htmlString.append("</style>");
						htmlString.append("</head><body style=\"min-height:200px;\">");
						String contents=newsDetailBen.result.contents;
						String repcontents=contents.replace("\n\t","");
						String repcontents1=repcontents.replaceAll("<p>\\s*|<strong>\\s*", "");
						htmlString.append(repcontents1);
						htmlString.append("</body></html>");
						if(mWebview!=null){
							mWebview.loadDataWithBaseURL(null,
									htmlString.toString(),
									"text/html", "UTF-8", null);// cass
						}
					}
				}
				setNews(newsDetailBen.result);
			}
		};
		getHttpUtils().send(HttpMethod.GET, AppUrl.API + url, getParams(url), callBack);
		
	}
	/**
	 * 双击滑动到顶部
	 */
	// private static Boolean isExit = false;

	// private void exitBy2Click() {
	// Timer tExit = null;
	// if (isExit == false) {
	// isExit = true; // 准备退出
	// tExit = new Timer();
	// tExit.schedule(new TimerTask() {
	// @Override
	// public void run() {
	// isExit = false; // 取消退出
	// }
	// }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
	//
	// } else {
	// mRScroll.scrollTo(0, 0);
	// }
	// }

	GScrollView mRScroll;

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 暂停播放
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeWebView();
	}

	private void removeWebView() {
		// 一定要销毁，否则无法停止播放
		if (mWebview != null) {
			mRoot1.setFitsSystemWindows(false);
			if (mWebview.getRootView() != null) {
				((ViewGroup) mWebview.getRootView()).removeAllViews();
			}
			mWebview.removeAllViews();
			mWebview.destroy();
			mWebview = null;
		}
	}
	private void addCaseAssist(String mFromGUID, String assist){
		String url = AppUrl.ASSISTNPUT;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("guid", mFromGUID);
			param.put("assist", assist);
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		getHttpUtils().send(HttpMethod.PUT, AppUrl.API + url, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException error, String msg) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> n) {
					}
				});
	}
	// 是否收藏
	private void isFavorite() {
		if (SJQApp.user == null) {
			return;
		}
		String url = AppUrl.IS_Favorite+"userGUID="+SJQApp.user.guid+
				"&fromGUID="+mFromGUID+"&favoriteMark=0&classType=0";
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(responseInfo.result, SwresultBen.class);
				if (bean != null) {
					if (bean.result.operatResult) {
						mCollect.setChecked(true);
					}
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				toastShow("网络请求失败");
				mCollect.setChecked(false);
			}
		};
		getHttpUtils(0).send(HttpMethod.GET, AppUrl.API + url,callBack);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.collect:
			// 收藏
			SJQApp.isRrefresh = true;
			doCollect();
			break;
		case R.id.tvComment:
			Intent intent2 = new Intent();
			if (commentSize == 0) {// 评论条数==0时,直接评论，根据要求取消
				intent2.setClass(this, CommentListActivity.class);
				intent2.putExtra("comment", true);
			} else {
				intent2.setClass(this, CommentListActivity.class);
			}

			intent2.putExtra(CommentListActivity.FORM_GUID, mFromGUID);
			intent2.putExtra(CommentListActivity.CLASS_TYPE, 1);
			startActivityForResult(intent2, 0);
			break;
		case R.id.userLogo:
		case R.id.itemWei:

			Intent itemWei = new Intent(this, WeiShopWebActivity.class);
			// itemWei.putExtra(AppTag.TAG_JSON,
			// getIntent().getStringExtra(AppTag.TAG_JSON));
			itemWei.putExtra(AppTag.TAG_GUID, mUserGuid);
			startActivity(itemWei);

			break;
		case R.id.tagGroup:
			startActivity((Intent) v.getTag());
			break;
		case R.id.ivErro:
			loadata();
			break;
		case R.id.item_comment:
			if (SJQApp.user == null) {
				startActivity(new Intent(WebDetailActivity.this,
						LoginActivity.class));
				return;
			}
			item_pingluen.setVisibility(View.VISIBLE);
			pingluen_text.setFocusable(true);
			pingluen_text.setFocusableInTouchMode(true);
			pingluen_text.requestFocus();
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
			break;
		case R.id.fasong_btn:
			if(fasong_btn.getText().toString().equals("发送")){
				if(SJQApp.user.isBindMobile){
					doSubmit();
				}else{
					toastShow("绑定手机号后才能发评论");
				}
			}else{
				if (item_pingluen.getVisibility() == View.VISIBLE) {
					item_pingluen.setVisibility(View.GONE);
					imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法					
				}
			}
			break;
		case R.id.share:
		  addCaseAssist(mFromGUID,"2");
		  mShareUtils.doShare();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mShareUtils != null) {
			mShareUtils.onActivityResult(requestCode, resultCode, data);
		}
		if (requestCode == 0) {
			if (data == null)
				return;
			commentSize = data.getIntExtra("commentSize", 1);
			mTvComment.setText(commentSize+"");
		}
	}

	boolean collecting = false;

	private void doCollect() {
		if (SJQApp.user != null) {
			dialog.show();
			String url = AppUrl.FAVORITE_ADD;
			RequestParams params = new RequestParams();
			params.setHeader("Content-Type","application/json");
			try {
				JSONObject param  = new JSONObject();
				param.put("classType","0");
				param.put("userGUID", SJQApp.user.guid);
				param.put("fromGUID", mFromGUID);
				if (mCollect.isChecked()) {
					addCaseAssist(mFromGUID,"3");
					param.put("operate", "1");
				} else {
					param.put("operate", "0");
				}
				StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
				params.setBodyEntity(sEntity);
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			getHttpUtils().send(HttpMethod.POST, AppUrl.API + url, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException error, String msg) {
							if (dialog == null)
								return;
							dialog.dismiss();
							toastShow("当前网络连接失败");
						}

						@Override
						public void onSuccess(ResponseInfo<String> n) {
							if (dialog == null)
								return;
							dialog.dismiss();
							Gson gson = new Gson();
							SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
							if (bean != null) {
								if (bean.result.operatResult) {
									if (mCollect.isChecked()) {
										toastShow("收藏成功");
									}else{
										toastShow("取消收藏成功");
									}
								}
							}

						}
					});
		} else {
			mCollect.setChecked(false);
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);

		}

	}

	private void setNews(SwDetailBen newsDetailBen) {
		mFromGUID=newsDetailBen.guid;
		// 是否收藏
		isFavorite();
		addCaseAssist(mFromGUID,"10");
		if(TextUtils.isEmpty(newsDetailBen.getLogo())){
			userLogo.setBackgroundResource(R.drawable.ic_launcher);
		}else{
			mImageUtil.display(userLogo,newsDetailBen.getLogo());
		}
		detaName.setText(newsDetailBen.author);
		timeTV.setText(newsDetailBen.timeData());
		commentSize = newsDetailBen.numComment;
		mTvComment.setText(commentSize + "");
		mTvDesc.setText(newsDetailBen.title);
		liulang_tv.setText(newsDetailBen.hits+"");
		if(!newsDetailBen.descriptions.equals(""))
		daodu_tv.setText(newsDetailBen.descriptions);
		// 设置分享内容
		mShareUtils.setShareUrl("http://www.sjq315.com/news/"+newsDetailBen.id+".html")
				.setShareTitle(newsDetailBen.title)
				.setShareContent(newsDetailBen.descriptions)
				.setImageUrl(newsDetailBen.images);
	}

	public String getStringDateShort(String currentime) {
		// Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date dateDate = formatter.parse(currentime, pos);
		if (dateDate == null)
			return currentime;
		SimpleDateFormat formatter2 = new SimpleDateFormat("MM-dd HH:mm");
		String dateString = formatter2.format(dateDate);
		return dateString;
	}
	private void doSubmit() {
		dialog.show();
		if (SJQApp.user == null) {
			startActivity(new Intent(WebDetailActivity.this,
					LoginActivity.class));
			return;
		}
		String Contents=pingluen_text.getText().toString();
		if(Contents.trim().length() < 1){
			dialog.dismiss();
			toastShow("输入内容不能为空");
			return;
		}
		String url = AppUrl.COMMENT_SEND;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("contents",Contents);
			param.put("fromGUID", mFromGUID);
			param.put("markName", "1");
			param.put("fromID ", "0");
			param.put("userGUID", SJQApp.user.guid);
			param.put("anonymous", false);
			if (SJQApp.userData != null) {
				param.put("userName",SJQApp.userData.name);
				param.put("userLogo", SJQApp.userData.logo);
			} else if (SJQApp.ownerData != null) {
				param.put("userName",SJQApp.ownerData.getName());
				param.put("userLogo", SJQApp.ownerData.getLogo());
			}
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				// dialog.show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dialog.dismiss();
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
				if (bean != null) {
					if (bean.result.operatResult) {
						commentSize=commentSize+1;
						mTvComment.setText(commentSize + "");
						pingluen_text.setText("");
						pingluen_text.setHint("说点什么吧...");
						item_pingluen.setVisibility(View.GONE);
						imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法
						toastShow(bean.result.operatMessage);
					}else{
						toastShow(bean.result.operatMessage);
					}
				}
			}
			@Override
			public void onFailure(HttpException error, String arg1) {
				toastShow("网络请求发生错误");
				dialog.dismiss();
			}
		};

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params, callBack);
	}
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			 String str = pingluen_text.getText().toString();
			 if(str.equals("")){
				 fasong_btn.setText("取消"); 
			 }else{
				 fasong_btn.setText("发送"); 
			 }
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	int commentSize = 0;
	private View item_pingluen;
	private EditText pingluen_text;
	private TextView fasong_btn;

	// 注入js函数监听
	private void addImageClickListner() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
		mWebview.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\"); "
				+ "for(var i=0;i<objs.length;i++)  " + "{"
				+ "     window.imagelistner.addImage(objs[i].src);  "
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage(this.src);  "
				+ "    }  " + "}" + "})()");
	}

	private ArrayList<String> imageList;// 套图
	private ArrayList<String> listImgUrl2 = new ArrayList<String>();
	private ArrayList<String> imageListDemo=new ArrayList<>();// 套图
	private String rootPath1;
	public static final String SEPERATOR = "/";
	private DAOHelper helper;
	//图片文件夹
	private String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ SEPERATOR + "WebViewDemo";
	private String html;
	// 抓取图片URL
	private ArrayList<String> getImageUrl(String HTML) {
		if (TextUtils.isEmpty(HTML))
			return null;
		Matcher matcher1 = Pattern.compile("http:\"?(.*?)(\\.\\\\|>|\\\\s+)",
				Pattern.CASE_INSENSITIVE).matcher(HTML);
		StringBuffer sb = new StringBuffer();
		while (matcher1.find()) {
			sb.append(matcher1.group());
		}

		String[] temps = sb.toString().split("\\\">");
		ArrayList<String> listImgUrl = new ArrayList<String>();
		int size = temps.length;
		for (int i = 0; i < size; i++) {
			if (i % 2 == 0) {
				listImgUrl.add(temps[i]);
			} else {

				listImgUrl2.add(temps[i]);
			}

		}

		return listImgUrl;
	}
	/**
	 * 将文本中的相对地址转换成对应的绝对地址
	 * @param content
	 * @param baseUrl
	 * @return
	 */
	private  String processImgSrc(String content,String baseUrl){
		Document document = Jsoup.parse(content);
		document.setBaseUri(baseUrl);
		Elements elements = document.select("img[src]");
		for(Element el:elements){
			el.attr("src",baseUrl);
		}
		return document.html();
	}
	/**
	 * 将文本中的相对地址转换成下载好的本地
	 * @param content
	 * @return
	 */
	private  String processImgSrc1(String content){
		Document document = Jsoup.parse(content);
		Elements elements = document.select("img[src]");
		for(int i=0;i<elements.size();i++) {
			Element el=elements.get(i);
			el.attr("src","file://"+imageListDemo.get(i));
		}
		return document.html();
	}
	//创建本地缓存文件夹 - 存放图片
	private boolean createCacheFolder()
	{
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			rootPath1 = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ SEPERATOR + "WebViewDemo";

			File cFile = new File(rootPath1);
			if(!cFile.exists())
			{
				cFile.mkdir();
			}
			return true;
		}
		else
		{
			toastShow("无法创建本地文件夹,请插入SD卡");
			return false;
		}
	}
	private  void getbitmap(){
		for(int i=0;i<imageList.size();i++) {
			String imgPath = helper.find(imageList.get(i));
			if(imgPath != null && new File(imgPath).exists()){
				imageListDemo.add(imgPath);
				if(imageListDemo.size()==imageList.size()){
					html=processImgSrc1(html);
					mWebview.loadDataWithBaseURL(null, html, "text/html","UTF-8", null);
				}
			}else{
				DownLoadTask task = new DownLoadTask(imageList.get(i));
				task.execute();
			}
		}
	}
	//图片下载器
	private class DownLoadTask extends AsyncTask<Void, Void, String>
	{
		String imgUrl;			//图片网络链接

		public DownLoadTask(String imgUrl)
		{
			this.imgUrl = imgUrl;
		}

		@Override
		protected String doInBackground(Void... params)
		{
			try
			{
				// 下载图片
				URL url = new URL(imgUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(20 * 1000);
				conn.setReadTimeout(20 * 1000);
				conn.setRequestMethod("GET");
				conn.connect();
				InputStream in = conn.getInputStream();
				byte[] myByte = readStream(in);
				//压缩存储,有需要可以将bitmap放入别的缓存中,另作他用, 比如点击图片放大等等
				Bitmap bitmap = BitmapFactory.decodeByteArray(myByte, 0, myByte.length);
				String fileName = Long.toString(System.currentTimeMillis()) + ".jpg";
				File imgFile = new File(rootPath + SEPERATOR +fileName);
				BufferedOutputStream bos
						= new BufferedOutputStream(new FileOutputStream(imgFile));
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				bos.close();

				return imgFile.getAbsolutePath();

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String imgPath)
		{
			super.onPostExecute(imgPath);
			if(imgPath != null)
			{
				imageListDemo.add(imgPath);
				// 将图片信息缓存进数据库
				helper.save(imgUrl, imgPath);
				if(imageListDemo.size()==imageList.size()){
					html=processImgSrc1(html);
					mWebview.loadDataWithBaseURL(null, html, "text/html","UTF-8", null);
				}
			}
			else
			{
				Log.e("WebViewActicvity error", "DownLoadTask has a invalid imgPath...");
			}

		}

	}

	private byte[] readStream(InputStream inStream) throws Exception
	{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];
		int len = 0;
		while( (len=inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}
}
