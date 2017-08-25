package com.baolinetworktechnology.shejiquan.activity;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baolinetwkchnology.shejiquan.xiaoxi.XiangQinActivity;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.CaseDetailBen;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetail;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.guojisheng.koyview.GScrollView;
import com.guojisheng.koyview.GScrollView.ScrollListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;

public class WebOpuszxActivity extends BaseActivity{
	private WebView mWebview;
	private View mTitleBg;
	public static final String URL = "WEB_URL";
	public static final String GUID = "GUID";
	public static final String ISCASE = "CASE";
	private CircleImg mUserLogo ,mUserLogo1;
	private TextView mTvTitle, mTvDesc, mTvComment, mTvUser, tv_tips1,
			tv_tips2;
	private RelativeLayout mLoadingOver;
	private CheckBox mCollect;
	private ShareUtils mShareUtils;
	private BitmapUtils mImageUtil;
	private String mUserGuid = "";
	private GScrollView mScroll;
	private String mFromGUID;
	private ImageView mImageView;
	private DbUtils db;
	private InputMethodManager imm;
	// private String mobile;
	// private String qq;
	private ArrayList<String> imageList;// 套图
	protected ArrayList<String> imageListTitle;// 图片标题
	private DesignerDetail design;
	private int id;
	View itemLinear, waiting;

	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_opuszx);
		// 初始化view
		initView();

		// 初始化数据源
		initData();
		//GScrollView 滑动监听
		mScrollOnTou();
		// 当前文章是否收藏
		isFavorite();
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
	private void controlKeyboardLayout(final View root, final View scrollToView) {
	        root.getViewTreeObserver().addOnGlobalLayoutListener( new OnGlobalLayoutListener() {  
	            @Override  
	            public void onGlobalLayout() {  
	                Rect rect = new Rect();  
	                //获取root在窗体的可视区域  
	                root.getWindowVisibleDisplayFrame(rect);  
	                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)  
	                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;  
	                //若不可视区域高度大于100，则键盘显示  
	                if (rootInvisibleHeight > 100) {  
	                    int[] location = new int[2];  
	                    //获取scrollToView在窗体的坐标  
	                    scrollToView.getLocationInWindow(location);  
	                    //计算root滚动高度，使scrollToView在可见区域  
	                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;  
	                    root.scrollTo(0, srollHeight);  
	                } else {  
	                    //键盘隐藏  
	                    root.scrollTo(0, 0);  
	                }  
	            }  
	        });  
	    }  
	private void mScrollOnTou() {
		mScroll.setScrollListener(new ScrollListener() {
			
			@Override
			public void scrollOritention(int l, int t, int oldl, int oldt) {
				int t1=t;
				if(t1<255){
              	  title_wed.getBackground().setAlpha(t1); 
              	  if(t1>150){
              		  if (!mCollect.isChecked()) {
              			  mCollect.setButtonDrawable(R.drawable.nav_fav1);
            			     } 
              		  share.setImageResource(R.drawable.nav_button_fxy_default);
              	  }else{
              		if (!mCollect.isChecked()) {
            			  mCollect.setButtonDrawable(R.drawable.nav_fav);
          			   } 
                	   share.setImageResource(R.drawable.nav_button_fxe_default);
                     
              	  }
				}else{
					title_wed.getBackground().setAlpha(255); 
              	    mCollect.setButtonDrawable(R.drawable.nav_fav1);
              	    share.setImageResource(R.drawable.nav_button_fxy_default);
				}
			}
		});
//		mScroll.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				if (item_pingluen.getVisibility() == View.VISIBLE) {
//					item_pingluen.setVisibility(View.GONE);
//					imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法					
//				}
//				return false;
//			}
//		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	BitmapUtils bitmapUtils;

	private void initView() {
		bitmapUtils = new BitmapUtils(this);
		mTitleBg = findViewById(R.id.titleBg);
		mWebview = (WebView) findViewById(R.id.webView);
		mScroll = (GScrollView) findViewById(R.id.scroll);
		mTvTitle = (TextView) findViewById(R.id.tv_title2);
		mTvUser = (TextView) findViewById(R.id.tv_user);
		tv_tips1 = (TextView) findViewById(R.id.tv_tips1);
		tv_tips2 = (TextView) findViewById(R.id.tv_tips2);
		mTvDesc = (TextView) findViewById(R.id.tv_desc);
		mTvComment = (TextView) findViewById(R.id.tvComment);
		mCollect = (CheckBox) findViewById(R.id.collect);
		mUserLogo = (CircleImg) findViewById(R.id.userLogo);
		mUserLogo1 = (CircleImg) findViewById(R.id.userLogo1);
		mImageView = (ImageView) findViewById(R.id.ivBg);
		mLoadingOver = (RelativeLayout) findViewById(R.id.loading_over2);
		itemLinear = findViewById(R.id.itemLinear);
		title_wed = findViewById(R.id.title_wed);
		title_wed.getBackground().setAlpha(0);
		waiting = findViewById(R.id.waiting);
		mTvComment.setOnClickListener(this);
		mUserLogo.setOnClickListener(this);
		mUserLogo1.setOnClickListener(this);
		mCollect.setOnClickListener(this);
		share = (ImageView) findViewById(R.id.share);
		share.setOnClickListener(this);
		findViewById(R.id.ivErro).setOnClickListener(this);
		findViewById(R.id.il).setOnClickListener(this);
		mScroll.setImageView(mImageView);
		mScroll.setMaxHeight(260);
		mTitleBg.setAlpha(1);
		// 初始化WebView
		WebSettings settings = mWebview.getSettings();
		settings.setJavaScriptEnabled(true);// 能够执行JS代码
		settings.setRenderPriority(RenderPriority.HIGH);//
		settings.setLoadsImagesAutomatically(false);
		// 设置 缓存模式
		settings.setAppCacheEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		// 开启 DOM storage API 功能
		settings.setDomStorageEnabled(true);
		mWebview.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 用正则判断是否为图片
				String reg = "[^\\s]+\\.(jpg|gif|png|bmp|jpeg)";
				LogUtils.i("imageUrl=", url);
				if (url.matches(reg)) {
					// 点击图片 跳转到图片查看器
					Intent intent = new Intent(WebOpuszxActivity.this,
							PhotoActivity.class);
					// 所有图片集合
					intent.putStringArrayListExtra(PhotoActivity.IMAGE_URLS,
							imageList);
					// 所有图片的标题
					intent.putExtra(PhotoActivity.IMAGE_TITLES, imageListTitle);
					// 当前图片的位置
					int postion = indexOf(imageList, url);

					if (postion == -1) {

						postion = indexOf(listImgUrl2, url);

					}
					intent.putExtra(PhotoActivity.INDEX, postion);
					startActivity(intent);
					return true;
				} else {
					return super.shouldOverrideUrlLoading(view, url);
				}

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (mWebview == null)
					return;
				if (!mWebview.getSettings().getLoadsImagesAutomatically()) {
					// 加载图片
					mWebview.getSettings().setLoadsImagesAutomatically(true);

				}

			}
		});
		findViewById(R.id.item_design).setOnClickListener(this);
		findViewById(R.id.item_comment).setOnClickListener(this);
		item_pingluen = findViewById(R.id.item_pingluen);
		pingluen_text = (EditText) findViewById(R.id.pingluen_text);
		pingluen_text.addTextChangedListener(watcher);  
		fasong_btn = (Button) findViewById(R.id.fasong_btn);
		fasong_btn.setOnClickListener(this);
		imm = (InputMethodManager) pingluen_text.getContext().getSystemService(
				this.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);
		mRoot = (RelativeLayout) findViewById(R.id.mRoot);
		controlKeyboardLayout(mRoot, item_pingluen);  
	}

	private void initData() {

		db = DbUtils.create(this);
		mImageUtil = new BitmapUtils(this);
		mImageUtil.configDefaultLoadingImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型

		mShareUtils = new ShareUtils(this);
		mShareUtils.addToSocialSDK();

		mFromGUID = getIntent().getStringExtra(GUID);
		id = getIntent().getIntExtra(AppTag.TAG_ID, 0);
		String url = getIntent().getStringExtra(URL);

		if (url != null) {
			LoadContents(url);
		}

	}

	// 判断第几张图片
	private int indexOf(ArrayList<String> data, String object) {
		if (object != null) {
			int s = data.size();
			for (int i = 0; i < s; i++) {
				if (object.equalsIgnoreCase(data.get(i))) {
					return i;
				}
			}
		}
		return -1;
	}

	// 是否收藏
	private void isFavorite() {
		if (SJQApp.user == null) {
			return;
		}
		String url = ApiUrl.IS_Favorite;
		RequestParams params = getParams(url);
		params.addBodyParameter("ClassType", "0");
		params.addBodyParameter("UserGUID", SJQApp.user.guid);

		params.addBodyParameter("FromGUID", mFromGUID);
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Bean data = CommonUtils.getDomain(responseInfo, Bean.class);
				// System.out.println("-->>isf=" + responseInfo.result);
				if (data != null) {
					if (data.success) {
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
		getHttpUtils(10).send(HttpMethod.POST, ApiUrl.API + url, params,
				callBack);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.shareQQ:
			mShareUtils.doShareWeChatCircle();
			break;
		case R.id.shareWeChat:
			// 设置微信好友分享内容
			mShareUtils.doShareWeChat();
			break;

		case R.id.shareXinLang:
			mShareUtils.doShareSina();
			break;
		case R.id.shareMore:
		case R.id.share:
			mShareUtils.doShare();

			break;
		case R.id.collect:
			SJQApp.isRrefresh = true;
			// 收藏
			doCollect();
			break;
		case R.id.il:
			mScroll.fullScroll(ScrollView.FOCUS_UP);
			break;
		case R.id.item_design:
			Intent intent1=new Intent(WebOpuszxActivity.this,XiangQinActivity.class); 
            intent1.putExtra("UserName","18050017399");
            startActivity(intent1);
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

			intent2.putExtra(CommentListActivity.CLASS_TYPE, 0);

			startActivityForResult(intent2, 0);
			break;
		case R.id.item_comment:	
			item_pingluen.setVisibility(View.VISIBLE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
			break;
		case R.id.fasong_btn:
			if(fasong_btn.getText().toString().equals("发送")){
				doSubmit();
			}else{
				if (item_pingluen.getVisibility() == View.VISIBLE) {
					item_pingluen.setVisibility(View.GONE);
					imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法					
				}
			}
		break;
		case R.id.userLogo1:
		case R.id.userLogo:
		case R.id.itemWei:
			Intent intent = new Intent(this, WeiShopWebActivity.class);
			if (design != null)
				intent.putExtra(AppTag.TAG_JSON, design.toString());
			intent.putExtra(AppTag.TAG_GUID, mUserGuid);
			startActivity(intent);
			break;
		case R.id.tagGroup:
			startActivity((Intent) v.getTag());
			break;
		case R.id.ivErro:
			// 加载需要显示的网页
			String url = getIntent().getStringExtra(URL);
			LoadContents(url);
			break;
		case R.id.ivCall:

			to2Call();
			break;
		default:
			break;
		}
	}

	// 跳转到拨号
	private void to2Call() {

		if (SJQApp.user != null) {
			if (SJQApp.user.guid.equals(mUserGuid)) {
				toastShow("不能给自己拨号");
				return;
			}

			new MyAlertDialog(this)
					.setTitle("请确认您的联系电话为 " + SJQApp.user.getMobile())
					.setContent("我们将使用该电话为您接通设计师").setBtnOk("呼叫")
					.setBtnCancel("暂不呼叫")
					.setBtnOnListener(new DialogOnListener() {

						@Override
						public void onClickListener(boolean isOk) {
							if (isOk) {
								doCall();
							}

						}
					}).show();
		} else {
			go2Login();
		}

	}

	void doCall() {
		Intent intent = new Intent(this, CallActivity.class);
		intent.putExtra(AppTag.TAG_GUID, mUserGuid);
		if (design != null)
			intent.putExtra(AppTag.TAG_JSON, design.toString());
		startActivity(intent);
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
			Case caseData = getDataDb();
			if (caseData == null)
				return;
			caseData.NumComment = commentSize;
			saveDataDb(caseData);
		}
	}

	private void doCollect() {
		if (SJQApp.user != null) {
			String url;
			if (!mCollect.isChecked()) {
				url = ApiUrl.FAVORITE_CANCEL;

			} else {
				url = ApiUrl.FAVORITE_ADD;
			}
			RequestParams params = getParams(url);

			// NEWS
			// if (getIntent().getBooleanExtra(ISCASE, false)) {
			params.addBodyParameter("ClassType", "0");
			// } else {
			// // CASE
			// params.addBodyParameter("ClassType", "1");
			// }

			params.addBodyParameter("UserGUID", SJQApp.user.guid);
			params.addBodyParameter("Type", "0");
			params.addBodyParameter("FromGUID", mFromGUID);

			RequestCallBack<String> callBack = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {

					Bean data = CommonUtils.getDomain(responseInfo, Bean.class);
					if (data != null) {
						if (!data.success) {
							if (mCollect.isChecked()) {
								toastShow("收藏失败");
							} else {
								toastShow("取消收藏失败");
							}
							mCollect.setChecked(false);
						} else {
							if (mCollect.isChecked()) {
								toastShow("收藏成功");
							} else {
								toastShow("取消收藏成功");
							}

						}
					}
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					toastShow("网络请求失败");
					mCollect.setChecked(false);
				}
			};
			getHttpUtils().send(HttpMethod.POST, ApiUrl.API + url, params,
					callBack);

		} else {
			mCollect.setChecked(false);
			go2Login();

		}

	}

	private void go2Login() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	private void LoadContents(final String url) {
		LogUtils.i("opus Url=", url);

		mLoadingOver.findViewById(R.id.url_error).setVisibility(View.GONE);

		Case data = getDataDb();
		if (data != null) {
			setData(data);
			return;
		}

		String json = getIntent().getStringExtra(AppTag.TAG_JSON);
		Case item = CommonUtils.getDomain(json, Case.class);

		if (initCaseHead(item)) {
			itemLinear.setVisibility(View.INVISIBLE);
			waiting.setVisibility(View.VISIBLE);
		} else {
			mLoadingOver.setVisibility(View.VISIBLE);
		}

		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> n) {

				if (mWebview == null)
					return;

				CaseDetailBen caseDetailBen = CommonUtils.getDomain(n,
						CaseDetailBen.class);
				if (caseDetailBen == null || caseDetailBen.data == null
						|| caseDetailBen.data == null) {
					toastShow("没有获取到内容");
					AppErrorLogUtil.getErrorLog(getApplicationContext())
							.postError("200", "GET", url); 
					finish();
					return;
				}
				// findViewById(R.id.scroll).setVisibility(View.VISIBLE);
				Case caseDetail = caseDetailBen.data;
				setData(caseDetail);
				hideLoding();
				saveDataDb(caseDetailBen.data);
			   }

			@Override
			public void onFailure(HttpException error, String msg) {
				if (mWebview == null)
					return;
				toastShow("网络错误");
				mLoadingOver.findViewById(R.id.url_error).setVisibility(
						View.VISIBLE);
				AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
						"" + error.getExceptionCode(), "GET", url);
				LogUtils.i("opus erro", msg);
			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);

	}

	private void saveDataDb(Case data) {
		if (data == null || getIntent().getIntExtra(AppTag.TAG_ID, 0) == 0) {
			return;
		}
		try {
			long count = db.count(Case.class);
			if (count >= 50) {
				db.deleteAll(Case.class);
			}
			db.saveOrUpdate(data);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	private Case getDataDb() {
		if (id == 0)
			return null;
		Case entity = null;
		try {
			entity = db.findFirst(Selector.from(Case.class)
					.where("ID", "=", id));

		} catch (DbException e) {
			e.printStackTrace();
		}
		return entity;
	}

	private boolean initCaseHead(Case caseDetail) {

		if (caseDetail == null)
			return false;

		if (mTvTitle != null
				&& !TextUtils.isEmpty(mTvTitle.getText().toString())) {
			return true;
		}
		mUserGuid = caseDetail.UserGUID;
		mFromGUID = caseDetail.GUID;
		mTvTitle.setText(caseDetail.Title);
		mTvUser.setText(caseDetail.UserNickName);
		mImageUtil.display(mUserLogo, caseDetail.UserLogo);
		mImageUtil.display(mUserLogo1, caseDetail.UserLogo);
		mImageUtil.display(mImageView, caseDetail.getSmallImages());
		if (caseDetail.Descriptions != null
				&& !caseDetail.Descriptions.equals("")) {
//			mTvDesc.setText(Html
//					.fromHtml("<font font size=\"10\" color='#323232'>摘要： </font>"
//							+ caseDetail.Descriptions));
			mTvDesc.setText(caseDetail.Descriptions);
		} else {
			mTvDesc.setVisibility(View.GONE);
		}

		if (caseDetail.ClassID == 5) {
			tv_tips1.setText("造价：" + caseDetail.getMyBudget());
			tv_tips2.setText(caseDetail.getTips());
		} else {
			if (!TextUtils.isEmpty(caseDetail.AttrRoomText)) {
//				tv_tips1.setVisibility(View.GONE);
				tv_tips2.setText("空间类型：" + caseDetail.AttrRoomText);
			} else {
//				tv_tips2.setVisibility(View.GONE);
				tv_tips2.setText("空间类型：" + caseDetail.getTips());
			}
		}
		commentSize = caseDetail.NumComment;
		mTvComment.setText(commentSize+"");
		return true;
	}

	// 设置数据
	private void setData(Case caseDetail) {
		if (mWebview == null || caseDetail == null) {
			return;
		}
		itemLinear.setVisibility(View.VISIBLE);
		initCaseHead(caseDetail);
		mWebview.loadDataWithBaseURL(null, caseDetail.Contents, "text/html",
				"UTF-8", null);

		mUserGuid = caseDetail.UserGUID;
		mFromGUID = caseDetail.GUID;
		imageListTitle = getImageUrlTitle(caseDetail.Contents);
		imageList = getImageUrl(caseDetail.Contents);
		commentSize = caseDetail.NumComment;
		mTvComment.setText(commentSize+"");
		// 设置分享内容
		mShareUtils.setShareUrl(caseDetail.WebUrl)
				.setShareTitle(caseDetail.Title)
				.setShareContent(caseDetail.Descriptions)
				.setImageUrl(caseDetail.Images);

		newDesigner(caseDetail);
		findViewById(R.id.share).setVisibility(View.VISIBLE);

		itemLinear.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.slde_bottom_top));
	}

	private void newDesigner(Case caseDetail) {
		design = new DesignerDetail();
		design.GUID = caseDetail.UserGUID;
		design.QQ = caseDetail.getQQ();
		design.Mobile = caseDetail.getMobile();
		design.Logo = caseDetail.UserLogo;
		design.Name = caseDetail.UserNickName;
		design.CityID = caseDetail.CityID;
		design.Cost = caseDetail.getCost();
		design.ProvinceID = caseDetail.ProvinceID;
		design.IsCertification = caseDetail.IsCertification;
		design.setServiceStatus(caseDetail.ServiceStatus);
	}

	private void hideLoding() {
		if (mLoadingOver.getVisibility() != View.GONE) {
			Animation out = AnimationUtils.loadAnimation(WebOpuszxActivity.this,
					R.anim.alpha_out);

			mLoadingOver.setAnimation(out);
			mLoadingOver.setVisibility(View.GONE);
		}
		waiting.setVisibility(View.GONE);
	}

	int commentSize = 0;
	private ArrayList<String> listImgUrl2 = new ArrayList<String>();
	private View title_wed;
	private ImageView share;
	private View item_pingluen;
	private EditText pingluen_text;
	private Button fasong_btn;
	private RelativeLayout mRoot;

	// 抓取图片URL
	private ArrayList<String> getImageUrlTitle(String HTML) {
		if (TextUtils.isEmpty(HTML))
			return null;
		Matcher matcher1 = Pattern.compile("<h[\\d] style.*?</h[\\d]>")
				.matcher(HTML);
		StringBuffer sb = new StringBuffer();
		while (matcher1.find()) {
			sb.append(matcher1.group());
		}
		Matcher matcher2 = Pattern.compile(">(.*?)<").matcher(sb.toString());

		ArrayList<String> listTitle = new ArrayList<String>();
		while (matcher2.find()) {
			String temp = matcher2.group();
			if (temp != null && temp.trim().length() > 3) {
				listTitle.add(temp.substring(1, temp.length() - 1));
			}
		}

		return listTitle;
	}

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

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeWebView();
	}

	private void removeWebView() {
		if (mWebview != null) {
			if (mWebview.getRootView() != null) {
				((ViewGroup) mWebview.getRootView()).removeAllViews();
			}
			mWebview.removeAllViews();
			mWebview.onPause();
			mWebview.destroy();

			mWebview = null;
		}
	}

	@Override
	public void finish() {
		super.finish();
		if (db != null)
			db.close();
	}
	private void doSubmit() {
		if (SJQApp.user == null) {
			startActivity(new Intent(WebOpuszxActivity.this,
					LoginActivity.class));
			return;
		}
		String Contents=pingluen_text.getText().toString();
		if(Contents.equals("")){
			toastShow("评论不能为空");
			return;
		}
		String url = ApiUrl.COMMENT_SEND;
		RequestParams params = getParams(url);
		params.addBodyParameter("Contents", Contents);
		params.addBodyParameter("FromGuid", mFromGUID);
		params.addBodyParameter("Star", "5");
		params.addBodyParameter("Evaluate", Contents);
		params.addBodyParameter("ClassType", 0 + "");
		params.addBodyParameter("AdminGUID",
				getIntent().getStringExtra("FORM_USER_GUID"));
		params.addBodyParameter("UserID", "" + SJQApp.user.id);
		params.addBodyParameter("UserGuid", SJQApp.user.guid);
		if (SJQApp.userData != null) {
			params.addBodyParameter("UserName", SJQApp.userData.getName());
			params.addBodyParameter("UserLogo", SJQApp.userData.getLogo());
		} else if (SJQApp.ownerData != null) {
			params.addBodyParameter("UserName", SJQApp.ownerData.getName());
			params.addBodyParameter("UserLogo", SJQApp.ownerData.getLogo());
		} else {
			params.addBodyParameter("UserName", SJQApp.user.nickName);
			params.addBodyParameter("UserLogo", SJQApp.user.logo);
		}

		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				// dialog.show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
			
				Bean data = CommonUtils.getDomain(arg0, Bean.class);
				if (data != null) {			
					toastShow(data.message);
					pingluen_text.setText("");
					item_pingluen.setVisibility(View.GONE);
					imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);
				}
			}
			@Override
			public void onFailure(HttpException error, String arg1) {
				toastShow("网络请求发生错误");
				pingluen_text.setText("");
				item_pingluen.setVisibility(View.GONE);
				imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);
			}
		};

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);
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

}
