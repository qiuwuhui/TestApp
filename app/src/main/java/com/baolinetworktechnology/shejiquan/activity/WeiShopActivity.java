package com.baolinetworktechnology.shejiquan.activity;
/**
 * 设计师详情界面
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baolinetwkchnology.shejiquan.xiaoxi.XiangQinActivity;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.MainPageAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.SwDesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwDesignerDetailBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.fragment.JianCaiFragment;
import com.baolinetworktechnology.shejiquan.fragment.JianJieFragment;
import com.baolinetworktechnology.shejiquan.fragment.SJRecycFragment;
import com.baolinetworktechnology.shejiquan.fragment.ZXRecycFragment;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.WrapContentHeightViewPager;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView.OnScrollChanged;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import android.view.WindowManager.LayoutParams;

public class WeiShopActivity extends BaseFragmentActivity implements OnClickListener,RadioGroup.OnCheckedChangeListener{
	private ShareUtils mShareUtils;// 分享工具
	SwDesignerDetail mDesigner=new SwDesignerDetail();
	private PullToRefreshScrollView sc_scrollview;
	private TextView tite_name,name,tvFromCity,tvCost,tv_conten,panyName,numConcer,numFans;
	private View tiletLay;
	private CircleImg mUserLogo,titleLogo;
	private BitmapUtils mImageUtil;
	private RelativeLayout yingdao;
	private String designerGudi = "";
	private String designerID = "";
	boolean isquanbu = false;
	private int PageIndexsj=1;
	private int PageJianCai=1;
	private int PageIndexzx=1;
	private RadioGroup mRadioGroup;
	private View view_layout,view_layout1,Tuei_layout,Jian_layout;
	private WrapContentHeightViewPager mViewPager;
	private SJRecycFragment sjRecycFragment;
	private JianCaiFragment jianCaiFragment;
	private ZXRecycFragment zxRecycFragment;
	private JianJieFragment jianJieFragment;
	private ImageView share,backView,jinyin_im,renzhen_im,guanzhu_tv;
	private boolean isConcer =false;
	private View top_lay,relativeLayout2;
	private RelativeLayout mLoadingOver;
	//悬浮栏
	private LinearLayout mBuyLayout;
//	private WindowManager mWindowManager;
//	/**
//	 * 手机屏幕宽度
//	 */
//	private int screenWidth;
//	/**
//	 * 悬浮框View
//	 */
//	private static View suspendView;
//	/**
//	 * 悬浮框的参数
//	 */
//	private static WindowManager.LayoutParams suspendLayoutParams;
//	/**
//	 * 购买布局的高度
//	 */
//	private int buyLayoutHeight;
//	/**
//	 * myScrollView与其父类布局的顶部距离
//	 */
//	private int myScrollViewTop;
//
//	/**
//	 * 购买布局与其父类布局的顶部距离
//	 */
//	private int buyLayoutTop;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wei_shop);
		mImageUtil = new BitmapUtils(this);
		mImageUtil.configDefaultLoadingImage(R.drawable.icon_morentxy);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.icon_morentxy);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		designerGudi=getIntent().getStringExtra(AppTag.TAG_GUID);
		designerID=getIntent().getStringExtra(AppTag.TAG_ID);
		//魅族
		CommonUtils.setMeizuStatusBarDarkIcon(WeiShopActivity.this, false);
		//小米
		CommonUtils.setMiuiStatusBarDarkMode(WeiShopActivity.this, false);
		inviwe();
//		xunfu();
		setTvConcer();
		GetDesigner();
		//GScrollView 滑动监听
		mScrollOnTou();
//		loadWeb();
	}


	private void inviwe() {
		mLoadingOver = (RelativeLayout) findViewById(R.id.loading_over);
		yingdao = (RelativeLayout)findViewById(R.id.yingdao);
		yingdao.setOnClickListener(this);
		boolean isFirst = CacheUtils.getBooleanData(WeiShopActivity.this, "Weishop", true);
		if(isFirst){
			yingdao.setVisibility(View.VISIBLE);
		}
		backView = (ImageView) findViewById(R.id.back);
		backView.setOnClickListener(this);
		share = (ImageView) findViewById(R.id.share);
		share.setOnClickListener(this);
		top_lay =findViewById(R.id.top_lay);
		tite_name = (TextView) findViewById(R.id.tite_name);
		tiletLay = findViewById(R.id.tiletLay);
		findViewById(R.id.itemWeiTuo).setOnClickListener(this);
		findViewById(R.id.tvMessage).setOnClickListener(this);
		guanzhu_tv =(ImageView) findViewById(R.id.guanzhu_tv);
		guanzhu_tv.setOnClickListener(this);
		mUserLogo = (CircleImg) findViewById(R.id.userLogo);
		titleLogo = (CircleImg) findViewById(R.id.titleLogo);
		name = (TextView) findViewById(R.id.name);
		tvFromCity = (TextView) findViewById(R.id.tvFromCity);
		tvCost = (TextView) findViewById(R.id.tvCost);

		panyName = (TextView) findViewById(R.id.panyName);
		tv_conten = (TextView) findViewById(R.id.tv_conten);
		numConcer = (TextView) findViewById(R.id.numConcer);
		numFans = (TextView) findViewById(R.id.numFans);
		renzhen_im = (ImageView) findViewById(R.id.renzhen_im);
		jinyin_im = (ImageView) findViewById(R.id.jinyin_im);

		mRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		mRadioGroup.setOnCheckedChangeListener(this);
		view_layout = findViewById(R.id.Meitu_layout);
		view_layout1 = findViewById(R.id.Opus_layout);
		Tuei_layout = findViewById(R.id.Tuei_layout);
		Jian_layout = findViewById(R.id.Jian_layout);

		sc_scrollview=(PullToRefreshScrollView) findViewById(R.id.sc_scrollview);
		//设置刷新的模式
		sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//both  可以上拉、可以下拉
		setPullToRefreshLable();
		sc_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				if (mViewPager.getCurrentItem() ==0) {
						PageIndexsj++;
						sjRecycFragment.setdesignerGudi(PageIndexsj);
				}else if(mViewPager.getCurrentItem() ==1){
					PageJianCai++;
					jianCaiFragment.setdesignerGudi(PageJianCai);
				}else if(mViewPager.getCurrentItem() ==2){
					PageIndexzx++;
					zxRecycFragment.setdesignerGudi(PageIndexzx);
				}
			}
		});
	}
//	private void xunfu() {
//		relativeLayout2= findViewById(R.id.relativeLayout2);
//		mBuyLayout = (LinearLayout) findViewById(R.id.buy);
//		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//		screenWidth = mWindowManager.getDefaultDisplay().getWidth();
//	}
	public void setTvConcer(){
		guanzhu_tv.setImageResource(R.drawable.sj_guanzhu);
		isConcer = false;
	}
	public void setTvConcerTrue(){
		guanzhu_tv.setImageResource(R.drawable.sj_yi_guanzhu);
		isConcer =true;
	}
	private int anInt;
	private void mScrollOnTou(){
		sc_scrollview.setOnScrollChanged(new OnScrollChanged() {
			@Override
			public void onScroll(int scrollX, int scrollY) {
				anInt++;
					if(scrollY<255){
						tiletLay.setBackgroundColor(Color.argb((int) scrollY, 255,255,255));
						if(scrollY>155){
							backView.setImageResource(R.drawable.nav_button_fhan_default);
							share.setImageResource(R.drawable.nav_button_fxy_default);
							top_lay.setVisibility(View.VISIBLE);
						}else{
							backView.setImageResource(R.drawable.nav_button_fhan_default1);
							share.setImageResource(R.drawable.share_write);
							top_lay.setVisibility(View.GONE);
						 }
					}else{
						tiletLay.setBackgroundColor(Color.argb((int) 255, 255,255,255));
						backView.setImageResource(R.drawable.nav_button_fhan_default);
						share.setImageResource(R.drawable.nav_button_fxy_default);
						top_lay.setVisibility(View.VISIBLE);
					}
				    if(scrollY <= 0){
					    tiletLay.setBackgroundColor(Color.argb((int) 0, 255,255,255));
						backView.setImageResource(R.drawable.nav_button_fhan_default1);
						share.setImageResource(R.drawable.nav_button_fxe_default);
						top_lay.setVisibility(View.GONE);
				    }
				anInt=scrollY;
//				if(scrollY >= buyLayoutTop ){
//					if(suspendView == null){
//						showSuspend();
//					}
//				}else if(scrollY <= buyLayoutTop + buyLayoutHeight){
//					if(suspendView != null){
//						removeSuspend();
//					}
//				}
			}
		});
	}
//	/**
//	 * 窗口有焦点的时候，即所有的布局绘制完毕的时候，我们来获取购买布局的高度和myScrollView距离父类布局的顶部位置
//	 */
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		super.onWindowFocusChanged(hasFocus);
//		if(hasFocus){
//			myScrollViewTop = relativeLayout2.getTop()+tiletLay.getHeight()-2;
//			buyLayoutHeight = mBuyLayout.getHeight();
//			buyLayoutTop = mBuyLayout.getTop();
////			myScrollViewTop = sc_scrollview.getTop();
//		}
//	}
//	/**
//	 * 显示购买的悬浮框
//	 */
//	private RadioGroup showRadioGroup;
//	private View show_layout,show_layout1,show_Tuei,show_Jian;
//	private void showSuspend(){
//		if(suspendView == null){
//			suspendView = LayoutInflater.from(this).inflate(R.layout.wei_radio_layot, null);
//			showRadioGroup = (RadioGroup)suspendView.findViewById(R.id.radioGroup);
//			show_layout = suspendView.findViewById(R.id.Meitu_layout);
//			show_layout1 = suspendView.findViewById(R.id.Opus_layout);
//			show_Tuei = suspendView.findViewById(R.id.Tuei_layout);
//			show_Jian = suspendView.findViewById(R.id.Jian_layout);
//			int id = 0;
//			switch (mViewPager.getCurrentItem()) {
//				case 0:
//					id= R.id.rbMeitu;
//					show_layout.setVisibility(View.VISIBLE);
//					show_layout1.setVisibility(View.INVISIBLE);
//					show_Tuei.setVisibility(View.INVISIBLE);
//					show_Jian.setVisibility(View.INVISIBLE);
//				 break;
//				case 1:
//					id= R.id.rbOpus;
//					show_layout.setVisibility(View.INVISIBLE);
//					show_layout1.setVisibility(View.VISIBLE);
//					show_Tuei.setVisibility(View.INVISIBLE);
//					show_Jian.setVisibility(View.INVISIBLE);
//					break;
//				case 2:
//					id= R.id.rbTuei;
//					show_layout.setVisibility(View.INVISIBLE);
//					show_layout1.setVisibility(View.INVISIBLE);
//					show_Tuei.setVisibility(View.VISIBLE);
//					show_Jian.setVisibility(View.INVISIBLE);
//					break;
//				case 3:
//					id= R.id.rbJian;
//					show_layout.setVisibility(View.INVISIBLE);
//					show_layout1.setVisibility(View.INVISIBLE);
//					show_Tuei.setVisibility(View.INVISIBLE);
//					show_Jian.setVisibility(View.VISIBLE);
//					break;
//			}
//			showRadioGroup.check(id);
//			showRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//				@Override
//				public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//					int id = 0;
//					switch (checkedId) {
//						case R.id.rbMeitu:
//							id= R.id.rbMeitu;
//							if(IsAnLi){
//								sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//							}else{
//								sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
//							}
//							if (mViewPager.getCurrentItem() != 0) {
//								mViewPager.setCurrentItem(0);
//								show_layout.setVisibility(View.VISIBLE);
//								show_layout1.setVisibility(View.INVISIBLE);
//								show_Tuei.setVisibility(View.INVISIBLE);
//								show_Jian.setVisibility(View.INVISIBLE);
//
//								view_layout.setVisibility(View.VISIBLE);
//								view_layout1.setVisibility(View.INVISIBLE);
//								Tuei_layout.setVisibility(View.INVISIBLE);
//								Jian_layout.setVisibility(View.INVISIBLE);
//							}
//							break;
//						case R.id.rbOpus:
//							id= R.id.rbOpus;
//							if(IsJianCai){
//								sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//							}else{
//								sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
//							}
//							if (mViewPager.getCurrentItem() != 1) {
//								mViewPager.setCurrentItem(1);
//								show_layout.setVisibility(View.INVISIBLE);
//								show_layout1.setVisibility(View.VISIBLE);
//								show_Tuei.setVisibility(View.INVISIBLE);
//								show_Jian.setVisibility(View.INVISIBLE);
//
//								view_layout.setVisibility(View.INVISIBLE);
//								view_layout1.setVisibility(View.VISIBLE);
//								Tuei_layout.setVisibility(View.INVISIBLE);
//								Jian_layout.setVisibility(View.INVISIBLE);
//							}
//							break;
//						case R.id.rbTuei:
//							id= R.id.rbTuei;
//							if(IsTieZi){
//								sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//							}else{
//								sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
//							}
//							if (mViewPager.getCurrentItem() != 2) {
//								mViewPager.setCurrentItem(2);
//								show_layout.setVisibility(View.INVISIBLE);
//								show_layout1.setVisibility(View.INVISIBLE);
//								show_Tuei.setVisibility(View.VISIBLE);
//								show_Jian.setVisibility(View.INVISIBLE);
//
//								view_layout.setVisibility(View.INVISIBLE);
//								view_layout1.setVisibility(View.INVISIBLE);
//								Tuei_layout.setVisibility(View.VISIBLE);
//								Jian_layout.setVisibility(View.INVISIBLE);
//							}
//							break;
//						case R.id.rbJian:
//							id= R.id.rbJian;
//							sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
//							if (mViewPager.getCurrentItem() != 3) {
//								mViewPager.setCurrentItem(3);
//								show_layout.setVisibility(View.INVISIBLE);
//								show_layout1.setVisibility(View.INVISIBLE);
//								show_Tuei.setVisibility(View.INVISIBLE);
//								show_Jian.setVisibility(View.VISIBLE);
//
//								view_layout.setVisibility(View.INVISIBLE);
//								view_layout1.setVisibility(View.INVISIBLE);
//								Tuei_layout.setVisibility(View.INVISIBLE);
//								Jian_layout.setVisibility(View.VISIBLE);
//							}
//							break;
//					}
//					mRadioGroup.check(id);
//				}
//			});
//
//			if(suspendLayoutParams == null){
//				suspendLayoutParams = new LayoutParams();
//				suspendLayoutParams.type = LayoutParams.TYPE_PHONE;
//				suspendLayoutParams.format = PixelFormat.RGBA_8888;
//				suspendLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
//						| LayoutParams.FLAG_NOT_FOCUSABLE;
//				suspendLayoutParams.gravity = Gravity.TOP;
//				suspendLayoutParams.width = screenWidth;
//				suspendLayoutParams.height = buyLayoutHeight;
//				suspendLayoutParams.x = 0;
//				suspendLayoutParams.y = myScrollViewTop;
//			}
//		}
//
//		mWindowManager.addView(suspendView, suspendLayoutParams);
//	}
//
//
//	/**
//	 * 移除购买的悬浮框
//	 */
//	private void removeSuspend(){
//		if(suspendView != null){
//			mWindowManager.removeView(suspendView);
//			suspendView = null;
//			showRadioGroup =null;
//			show_layout =null;
//			show_layout1 =null;
//			show_Tuei =null;
//			show_Jian =null;
//		}
//	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.yingdao:
			yingdao.setVisibility(View.GONE);
			CacheUtils.cacheBooleanData(WeiShopActivity.this, "Weishop",false);
			break;
		case R.id.share:
			doShare();
			break;
		case R.id.guanzhu_tv:
			if(SJQApp.user==null) {
				((SJQApp) getApplication()).exitAccount();
				startActivity(new Intent(WeiShopActivity.this,
						LoginActivity.class));
				return;
			}
			if(SJQApp.user.guid.equals(designerGudi)) {
				toastShow("自己不能关注自己");
				return;
			}
			doConcer();
			break;
		case R.id.tvMessage:
		case R.id.itemWeiTuo:
			if(SJQApp.user==null){
				((SJQApp) getApplication()).exitAccount();
				startActivity(new Intent(WeiShopActivity.this,
						LoginActivity.class));
			}else if(!SJQApp.user.isBindMobile) {
				gotBound("免费咨询需绑定手机号");
			}else{
			if(SJQApp.user.guid.equals(designerGudi)){
				toastShow("自己不能和自己聊天");
			}else{
				if(DemoHelper.getInstance().isConnected()){
					String LoginName=CommonUtils.removeAllSpace(mDesigner.getGuid());
			    	DemoHelper.getInstance().putlianxi(LoginName, mDesigner.getName(), mDesigner.getLogo());
			    	Intent intent=new Intent(WeiShopActivity.this,XiangQinActivity.class);
			    	intent.putExtra("UserName", LoginName);
			    	startActivity(intent);
				  }else{
					String LoginName=CommonUtils.removeAllSpace(SJQApp.user.getGuid());
					setdenlu(LoginName);
					toastShow("账号离线正在重新登录");
				  }
			  }
			}
			break;
		default:
			break;
		}

	}
	/**
	 * 关注动作
	 */
	public void doConcer() {
		dialog.show();
		if (SJQApp.user == null) {
			MobclickAgent.onEvent(WeiShopActivity.this,"kControlDesignerHomePageAttention","Notlogged_add");// 未登录点击设计师主页关注次数

			toastShow("请先登录");
			go2Login();
			return;
		}
		if (TextUtils.isEmpty(designerGudi)) {
			toastShow("请稍候");
			return;
		}

		SJQApp.isRrefresh = true;
		String url = AppUrl.FAVORITE_ADD;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("classType","1");
			param.put("userGUID", SJQApp.user.guid);
			param.put("fromGUID", designerGudi);
			param.put("favoriteMark", "1");
			if (isConcer) {
				param.put("operate", "0");
				MobclickAgent.onEvent(WeiShopActivity.this,"kControlDesignerHomePageAttention","logged_canceol");// 登录点击设计师主页取消关注次数
			} else {
				param.put("operate", "1");
				MobclickAgent.onEvent(WeiShopActivity.this,"kControlDesignerHomePageAttention","logged_add");//登录点击设计师关注次数
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
								if (isConcer) {
									toastShow("取消关注成功");
									setTvConcer();
								}else{
									toastShow("关注成功");
									setTvConcerTrue();
								}
								Intent intent = new Intent();
								intent.setAction("qiehuan");
								sendBroadcast(intent);
							}else{
								if (isConcer) {
									toastShow("取消关注失败");
								}else{
									toastShow("关注失败");
								}
							}
						}

					}
				});
	}
	/**
	 * 去登录
	 */
	private void go2Login() {
		Intent intent = new Intent(WeiShopActivity.this, LoginActivity.class);
	    startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WeiShopActivity");
		if (mShareUtils == null) {
			mShareUtils = new ShareUtils(this);
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
//		removeSuspend();
		MobclickAgent.onPageEnd("WeiShopActivity");
	}
	private void addCaseAssist(String mFromGUID, String assist){
		String url = AppUrl.DESIGNASSISTNPUT;
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
	/**
	 * 是否关注
	 */
	void isConcer() {
		if (SJQApp.user == null) {
			return;
		}
		String url = AppUrl.IS_Favorite+"userGUID="+SJQApp.user.guid+
				"&fromGUID="+designerGudi+"&favoriteMark=1&classType=1";
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
				if (bean != null) {
					if (bean.result.operatResult) {
						isConcer = true;
						setTvConcerTrue();
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
		httpUtils.send(HttpMethod.GET, AppUrl.API + url, callBack);

	}
	private void GetDesigner() {
		mLoadingOver.setVisibility(View.VISIBLE);
		String url ="";
		if(!TextUtils.isEmpty(designerID)){
			url = AppUrl.GET_DESIGNER_INFO + designerID;
		}else{
			url = AppUrl.GET_DESIGNER_INFO + designerGudi;
		}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				SwDesignerDetailBean detailBean = CommonUtils.getDomain(
						responseInfo, SwDesignerDetailBean.class);

				if(detailBean!=null && detailBean.success){
					if(detailBean!=null && detailBean.result!=null){
						showDesignData(detailBean.result);
						mDesigner=detailBean.result;
					}
				}
			}
			@Override
			public void onFailure(HttpException error, String arg1) {
				mLoadingOver.setVisibility(View.GONE);
				toastShow("获取设计师信息失败");
			}
		};
		getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url), callBack);

	}
	private void addFragment() {
		mViewPager = (WrapContentHeightViewPager) findViewById(R.id.viewPager);
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		sjRecycFragment=new SJRecycFragment(mViewPager);
		jianCaiFragment =new JianCaiFragment(mViewPager);
		zxRecycFragment=new ZXRecycFragment(mViewPager);
		jianJieFragment = new JianJieFragment(mViewPager);
		fragments.add(sjRecycFragment);
		fragments.add(jianCaiFragment);
		fragments.add(zxRecycFragment);
		fragments.add(jianJieFragment);
		mViewPager.setAdapter(new MainPageAdapter(getSupportFragmentManager(),
				fragments));
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int id = 0;
//				if(suspendView != null){
//					switch (arg0) {
//						case 0:
//							show_layout.setVisibility(View.VISIBLE);
//							show_layout1.setVisibility(View.INVISIBLE);
//							show_Tuei.setVisibility(View.INVISIBLE);
//							show_Jian.setVisibility(View.INVISIBLE);
//							break;
//						case 1:
//							show_layout.setVisibility(View.INVISIBLE);
//							show_layout1.setVisibility(View.VISIBLE);
//							show_Tuei.setVisibility(View.INVISIBLE);
//							show_Jian.setVisibility(View.INVISIBLE);
//							break;
//						case 2:
//							show_layout.setVisibility(View.INVISIBLE);
//							show_layout1.setVisibility(View.INVISIBLE);
//							show_Tuei.setVisibility(View.VISIBLE);
//							show_Jian.setVisibility(View.INVISIBLE);
//							break;
//						case 3:
//							show_layout.setVisibility(View.INVISIBLE);
//							show_layout1.setVisibility(View.INVISIBLE);
//							show_Tuei.setVisibility(View.INVISIBLE);
//							show_Jian.setVisibility(View.VISIBLE);
//							break;
//						default:
//							break;
//					}
//					showRadioGroup.check(id);
//				}
				switch (arg0) {
					case 0:
						if(IsAnLi){
							sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
						}else{
							sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
						}
						mViewPager.resetHeight(0);
						id=R.id.rbMeitu;
						view_layout.setVisibility(View.VISIBLE);
						view_layout1.setVisibility(View.INVISIBLE);
						Tuei_layout.setVisibility(View.INVISIBLE);
						Jian_layout.setVisibility(View.INVISIBLE);
						break;
					case 1:
						if(IsJianCai){
							sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
						}else{
							sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
						}
						mViewPager.resetHeight(1);
						id=R.id.rbOpus;
						view_layout.setVisibility(View.INVISIBLE);
						view_layout1.setVisibility(View.VISIBLE);
						Tuei_layout.setVisibility(View.INVISIBLE);
						Jian_layout.setVisibility(View.INVISIBLE);
						break;
					case 2:
						if(IsTieZi){
							sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
						}else{
							sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
						}
						mViewPager.resetHeight(2);
						id=R.id.rbTuei;
						view_layout.setVisibility(View.INVISIBLE);
						view_layout1.setVisibility(View.INVISIBLE);
						Tuei_layout.setVisibility(View.VISIBLE);
						Jian_layout.setVisibility(View.INVISIBLE);
						break;
					case 3:
						sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
						mViewPager.resetHeight(3);
						id=R.id.rbJian;
						view_layout.setVisibility(View.INVISIBLE);
						view_layout1.setVisibility(View.INVISIBLE);
						Tuei_layout.setVisibility(View.INVISIBLE);
						Jian_layout.setVisibility(View.VISIBLE);
						break;
					default:
						break;
				}
				mRadioGroup.check(id);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		mViewPager.resetHeight(0);
	}
	//显示数据
	private void showDesignData(SwDesignerDetail data) {
		addFragment();
		designerGudi =data.getGuid();
		sjRecycFragment.setShuaxin(designerGudi);
		jianCaiFragment.setShuaxin(designerGudi);
		zxRecycFragment.setShuaxin(designerGudi);
		jianJieFragment.setShuaxin(data);
		isConcer();
		mLoadingOver.setVisibility(View.GONE);
		if(data == null){
			toastShow("获取设计师信息出错");
			finish();
			return;
		}
		tite_name.setText(data.name);
		name.setText(data.name);
		if(data.getIsCertification()!=3){
			renzhen_im.setVisibility(View.GONE);
		}
		if(data.getServiceLevel() == 0){
			jinyin_im.setVisibility(View.GONE);
		}else if(data.getServiceLevel() == 2){
			jinyin_im.setImageResource(R.drawable.weishop_grad_2);
		}else if(data.getServiceLevel() == 3){
			jinyin_im.setImageResource(R.drawable.weishop_grad_3);
		}
		mImageUtil.display(mUserLogo, data.logo);
		mImageUtil.display(titleLogo, data.logo);
		if(data.cityID==0){
			tvFromCity.setText("未填写地址"+"  |  "+getWorkYear(data.officeTime)+"年设计经验"+"  |");
		}else{
			String fromCity= getfromCity(data.cityID);
			if(fromCity.length()>6){
				String	str=fromCity.substring(0,6);
				tvFromCity.setText(str+"  |  "+getWorkYear(data.officeTime)+"年设计经验"+"  |");
			}else{
				tvFromCity.setText(fromCity+"  |  "+getWorkYear(data.officeTime)+"年设计经验"+"  |");
		}
		}
		if(data.getCost().equals("面议")){
			tvCost.setText("  "+data.getCost());
		}else if(data.getCost().contains("¥")){
			tvCost.setText("   "+data.getCost());
		}else{
			tvCost.setText(Html.fromHtml("　"+"¥"+ data.getCost()));
		}
		panyName.setText(data.getWorkCompany());
		numConcer.setText(data.getNumConcer()+"");
		numFans.setText(data.getNumFans()+"");
//		tvCost.setText("设计费用：" + data.getCost());
		tv_conten.setText(data.designing);
//		if(data.LoginName!=null){
//			LoginName=data.LoginName;
//			NickName=data.NickName;
//			UserLogo=data.UserLogo;
//		}
	}

	//获取工作多少年
	public  int  getWorkYear(String OfficeTime){
		if(TextUtils.isEmpty(OfficeTime)){
			return 0;
		}
		int Time=Integer.parseInt(OfficeTime);
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR); //获取当前年份
		return mYear-Time;
	}
	//获取设计师所在地区
	private String getfromCity(int CityID){
		CityService cityService = CityService.getInstance(getActivity());
		if (cityService == null)
			return null;
		String shi=cityService.fromCityID(CityID);
		return shi;
	}
	private void gotBound(String strshwo){
		View dialogView = View.inflate(getActivity(),
				R.layout.dialog_collect, null);
		TextView titl = (TextView) dialogView
				.findViewById(R.id.dialog_title);
		titl.setText(strshwo);
		final AlertDialog ad = new AlertDialog.Builder(
				getActivity()).setView(dialogView).show();
		dialogView.findViewById(R.id.dialog_cancel)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ad.cancel();
					}
				});
		dialogView.findViewById(R.id.dialog_ok)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra(AppTag.TAG_JSON, SJQApp.user.getMobile());
						intent.setClass(getActivity(),
								BoundActivity.class);
						startActivity(intent);
						ad.cancel();
					}});
	}
	/**
	 * 分享
	 */
	private void doShare() {
		if (mShareUtils != null) {
			if (mDesigner != null) {
				mShareUtils
						.setShareUrl("http://m.sjq315.com/shop/des"+mDesigner.id+"/")
						.setShareTitle(mDesigner.name + "的微名片")
						.setShareContent(
								mDesigner.profession + "\n"
										+ mDesigner.workCompany)
						.setImageUrl(mDesigner.logo).addToSocialSDK().doShare();
			} else {
				toastShow("数据出错");
			}
		}
	}
	private void setPullToRefreshLable() {
		sc_scrollview.getLoadingLayoutProxy().setLastUpdatedLabel("正在拼命加载中...");
		sc_scrollview.onRefreshComplete();
	}
	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		switch (checkedId) {
			case R.id.rbMeitu:
				if(IsAnLi){
					sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
				}else{
					sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
				}
				if (mViewPager.getCurrentItem() != 0) {
					mViewPager.setCurrentItem(0);
					view_layout.setVisibility(View.VISIBLE);
					view_layout1.setVisibility(View.INVISIBLE);
					Tuei_layout.setVisibility(View.INVISIBLE);
					Jian_layout.setVisibility(View.INVISIBLE);
				}
				break;
			case R.id.rbOpus:
				if(IsJianCai){
					sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
				}else{
					sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
				}
				if (mViewPager.getCurrentItem() != 1) {
					mViewPager.setCurrentItem(1);
					view_layout.setVisibility(View.INVISIBLE);
					view_layout1.setVisibility(View.VISIBLE);
					Tuei_layout.setVisibility(View.INVISIBLE);
					Jian_layout.setVisibility(View.INVISIBLE);
				}
				break;
			case R.id.rbTuei:
				if(IsTieZi){
					sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
				}else{
					sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
				}
				if (mViewPager.getCurrentItem() != 2) {
					mViewPager.setCurrentItem(2);
					view_layout.setVisibility(View.INVISIBLE);
					view_layout1.setVisibility(View.INVISIBLE);
					Tuei_layout.setVisibility(View.VISIBLE);
					Jian_layout.setVisibility(View.INVISIBLE);
				}
				break;
			case R.id.rbJian:
				sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
				if (mViewPager.getCurrentItem() != 3) {
					mViewPager.setCurrentItem(3);
					view_layout.setVisibility(View.INVISIBLE);
					view_layout1.setVisibility(View.INVISIBLE);
					Tuei_layout.setVisibility(View.INVISIBLE);
					Jian_layout.setVisibility(View.VISIBLE);
				}
				break;
		}
	}
	public void RefreshComplete(){
			sc_scrollview.onRefreshComplete();
	}
	private boolean IsAnLi=true;
	private boolean IsTieZi=true;
	private boolean IsJianCai=true;
	public void NoAnLiComplete(){
			IsAnLi = false;
		if (mViewPager.getCurrentItem() == 0) {
			sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
		    }
	}
	public void NoTieZiComplete(){
			IsTieZi = false;
		if (mViewPager.getCurrentItem() == 2) {
			sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
		   }
	}
	public void NoJianCaiComplete(){
		   IsJianCai = false;
		if (mViewPager.getCurrentItem() == 1) {
			sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
		   }
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mShareUtils != null) {
			mShareUtils.onActivityResult(requestCode, resultCode, data);
			addCaseAssist(designerGudi,"2");
		}
	}
	private void setdenlu(String name){
		Intent intent = new Intent();
		intent.setAction("denglu");
		intent.putExtra("name",name);
		sendBroadcast(intent);
	}
}
