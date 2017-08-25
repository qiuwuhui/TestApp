package com.baolinetworktechnology.shejiquan.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baolinetwkchnology.shejiquan.xiaoxi.XiangQinActivity;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.NineGridTest2Adapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.DetailSwCase;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.domain.SwCaseDetailBen;
import com.baolinetworktechnology.shejiquan.domain.SwDesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.fragment.MainHomeCaseFragment;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.ListHomeCase;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.baolinetworktechnology.shejiquan.view.ScrollGridLayoutManager;
import com.google.gson.Gson;
import com.guojisheng.koyview.GScrollView;
import com.guojisheng.koyview.GScrollView.ScrollListener;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * 案例作品详情页
 *
 * @author JiSheng.Guo
 *
 */
public class WebOpusActivity extends BaseActivity {
	private View mTitleBg,view_xin;
	public  String URL1 = "WEB_URL";
	public  String GUID = "GUID";
	public  String ISCASE = "CASE";
	public static String POSITION = "POSITION";//行号
	public static String ISFAVORITE = "ISFAVORITE";//是否收藏
	public static String FAVORITENUM = "FAVORITENUM";
	public static String ISFABULOUS = "ISFABULOUS";//是否点赞
	public static String FABULOUSNUM = "FABULOUSNUM";
	public static String COMMENTNUM = "COMMENTNUM";//评论数
	private CircleImg mUserLogo ,mUserLogo1;
	private TextView mTvTitle, mTvDesc, mTvComment, mTvUser, tv_tips1,
			tv_tips2,mTvCost,feiyong,tvFromCity,sheji_linain,text_titvi,quan_bu;
	private RelativeLayout mLoadingOver,yingdao;
	private TextView mCollect;
	private ShareUtils mShareUtils;
	private BitmapUtils mImageUtil;
	private GScrollView mScroll;
	private String mFromGUID;
	private ImageView mImageView;
	private SwDesignerDetail design;
	private String mFromID="";
	boolean isquanbu = false;
	boolean MaxLines = false;
	private int mItemPosition = 0;//item行号
	private boolean mIsFavorite = false;
	private int mItemFavoriteNum = 0 ;//收藏人数
	private boolean mIsFabulous = false;
	private int mItemFabulousNum = 0 ;//点赞人数
	private int mItemCommentNum = 0 ;//评论人数
	private ListHomeCase mCaseListView;
	private SwCase swCase=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_opus_copy);
		// 初始化view
		initView();//
		// 初始化数据源
		initData();
		//GScrollView 滑动监听
		mScrollOnTou();
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
	private void mScrollOnTou() {
		mScroll.setScrollListener(new ScrollListener() {

			@Override
			public void scrollOritention(int l, int t, int oldl, int oldt) {
				int t1=t;
				if(t1<255){
              	  title_wed.setBackgroundColor(Color.argb((int) t, 255,255,255));
              	  if(t1>150){
//              		  if (!mCollect.isChecked()) {
              			  text_titvi.setVisibility(View.VISIBLE);
              			  view_xin.setVisibility(View.VISIBLE);
//            			     }
              		  backView.setImageResource(R.drawable.nav_button_fhan_default1);
              	  }else{
					  //魅族
					  CommonUtils.setMeizuStatusBarDarkIcon(WebOpusActivity.this, false);
					  //小米
					  CommonUtils.setMiuiStatusBarDarkMode(WebOpusActivity.this, false);
//              		if (!mCollect.isChecked()) {
//          			   }
              		   text_titvi.setVisibility(View.GONE);
              		   view_xin.setVisibility(View.GONE);
					  backView.setImageResource(R.drawable.nav_button_fhan_default1);
              	      }
				}else{
					//		//魅族
					CommonUtils.setMeizuStatusBarDarkIcon(WebOpusActivity.this, true);
					//小米
					CommonUtils.setMiuiStatusBarDarkMode(WebOpusActivity.this, true);
				    title_wed.setBackgroundColor(Color.argb((int) 255, 255,255,255));
					backView.setImageResource(R.drawable.nav_button_fhan_default);
              	    text_titvi.setVisibility(View.VISIBLE);
              	    view_xin.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	private RecyclerView mRecyclerView;
	private ScrollGridLayoutManager mLayoutManager;
	private NineGridTest2Adapter mAdapter;
	private void initView() {
		mTitleBg = findViewById(R.id.titleBg);
		mScroll = (GScrollView) findViewById(R.id.scroll);
		mTvTitle = (TextView) findViewById(R.id.tv_title2);
		mTvUser = (TextView) findViewById(R.id.tv_user);
		mTvCost = (TextView) findViewById(R.id.tvCost);

		mLoadingOver = (RelativeLayout) findViewById(R.id.loading_over2);
		mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		mLayoutManager = new ScrollGridLayoutManager(SJQApp.applicationContext,1);
		mLayoutManager.setScrollEnabled(false);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new NineGridTest2Adapter(this);
		mRecyclerView.setAdapter(mAdapter);

		tv_tips1 = (TextView) findViewById(R.id.tv_tips1);
		tv_tips2 = (TextView) findViewById(R.id.tv_tips2);
		mTvDesc = (TextView) findViewById(R.id.tv_desc);
		text_titvi = (TextView) findViewById(R.id.text_titvi);
		mTvComment = (TextView) findViewById(R.id.tvComment);//评论
		mCollect = (TextView) findViewById(R.id.collect);//收藏
		mUserLogo = (CircleImg) findViewById(R.id.userLogo);
		mUserLogo1 = (CircleImg) findViewById(R.id.userLogo1);
		mImageView = (ImageView) findViewById(R.id.ivBg);
		feiyong = (TextView) findViewById(R.id.feiyong);
		tvFromCity = (TextView) findViewById(R.id.tvFromCity);
		quan_bu = (TextView) findViewById(R.id.quan_bu);
		quan_bu.setOnClickListener(this);
		sheji_linain = (TextView) findViewById(R.id.sheji_linain);
		view_xin = findViewById(R.id.view_xin);
		yingdao = (RelativeLayout) findViewById(R.id.yingdao);
		yingdao.setOnClickListener(this);
        swCase =new SwCase();
		boolean isFirst = CacheUtils.getBooleanData(this, "Webopu", true);
		if (isFirst) {
			//魅族
			CommonUtils.setMeizuStatusBarDarkIcon(WebOpusActivity.this, false);
			//小米
			CommonUtils.setMiuiStatusBarDarkMode(WebOpusActivity.this, false);
			yingdao.setVisibility(View.VISIBLE);
		} else {
			//魅族
			CommonUtils.setMeizuStatusBarDarkIcon(WebOpusActivity.this, false);
			//小米
			CommonUtils.setMiuiStatusBarDarkMode(WebOpusActivity.this, false);
		}
		title_wed = findViewById(R.id.title_wed);
		title_wed.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
		mTvComment.setOnClickListener(this);
		mUserLogo.setOnClickListener(this);
		mUserLogo1.setOnClickListener(this);
		mCollect.setOnClickListener(this);
		share = (ImageView) findViewById(R.id.share);
		share.setOnClickListener(this);
		backView = (ImageView) findViewById(R.id.back);
		backView.setOnClickListener(this);
		findViewById(R.id.item_design).setOnClickListener(this);
		findViewById(R.id.il).setOnClickListener(this);
		mScroll.setImageView(mImageView);
		mScroll.setMaxHeight(260);
		mTitleBg.setAlpha(1);
	}

	private void initData() {
		mImageUtil = new BitmapUtils(this);
		mImageUtil.configDefaultLoadingImage(R.drawable.icon_morentxy);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.icon_morentxy);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型

		mShareUtils = new ShareUtils(this);
		mShareUtils.addToSocialSDK();
		mLoadingOver.setVisibility(View.VISIBLE);
		String url = getIntent().getStringExtra(URL1);
		mItemPosition = getIntent().getIntExtra(POSITION,-1);
		mIsFabulous = getIntent().getBooleanExtra(ISFABULOUS,false);
		mItemFabulousNum = getIntent().getIntExtra(FABULOUSNUM,-1);
        swCase.setGood(mIsFabulous);
        swCase.setNumGood(mItemFabulousNum);
		mItemCommentNum = getIntent().getIntExtra(COMMENTNUM,-1);
		if (url != null) {
			LoadContents(url);
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
				"&fromGUID="+mFromGUID+"&favoriteMark=0&classType=2";
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(responseInfo.result, SwresultBen.class);
				if (bean != null) {
					if (bean.result.operatResult) {
						mCollect.setClickable(true);
						swCase.setFavorite(true);
						Drawable drawable = getResources().getDrawable(R.drawable.nav_button_wsce_on);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
						mCollect.setCompoundDrawables(drawable,null,null,null);
						mCollect.setTextColor(getResources().getColor(R.color.app_color));
					}else{
						swCase.setFavorite(false);
						Drawable drawable = getResources().getDrawable(R.drawable.nav_button_wsce_default);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
						mCollect.setCompoundDrawables(drawable,null,null,null);
						mCollect.setTextColor(getResources().getColor(R.color.text_normal6));
					}
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				toastShow("网络请求失败");
				mCollect.setClickable(false);
			}
		};
		getHttpUtils(10).send(HttpMethod.GET, AppUrl.API + url,callBack);
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
			case R.id.quan_bu:
				if(isquanbu){
					isquanbu=false;
					mTvDesc.setMaxLines(3);
//					TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//                            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                            -1.0f);
//                    mHiddenAction.setDuration(500);
//					mTvDesc.startAnimation(mHiddenAction);
					quan_bu.setTextColor(getResources().getColor(R.color.xiangxi));
					Drawable drawable=getResources().getDrawable(R.drawable.sheji_zk);
					//设置Drawable的边界
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					quan_bu.setCompoundDrawables(null,null, drawable, null);
					quan_bu.setText("详细介绍");
				}else{
					isquanbu=true;
					mTvDesc.setMaxLines(1000);
//					TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//                    mShowAction.setDuration(500);
//					mTvDesc.startAnimation(mShowAction);
					quan_bu.setTextColor(getResources().getColor(R.color.shouqi));
					Drawable drawable=getResources().getDrawable(R.drawable.sheji_sq);
					//设置Drawable的边界
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					quan_bu.setCompoundDrawables(null,null, drawable, null);
					quan_bu.setText("收起");
				}
				break;
		case R.id.shareMore:
		case R.id.share:
			MobclickAgent.onEvent(WebOpusActivity.this,"kControlCaseDetailShare"); // 案例详情页分享点击数
			mShareUtils.doShare();
			break;
		case R.id.collect:
			SJQApp.isRrefresh = true;
			// 收藏
			doPost();
			break;
		case R.id.yingdao:
			yingdao.setVisibility(View.GONE);
			CacheUtils.cacheBooleanData(WebOpusActivity.this, "Webopu",
					false);
			break;
		case R.id.il:
			mScroll.fullScroll(ScrollView.FOCUS_UP);
			break;
		case R.id.item_design:
			if(SJQApp.user==null){
				MobclickAgent.onEvent(WebOpusActivity.this," kControlCaseDetailConslution","Notlogged_add");// 案例详情页咨询点击次数
				go2Login();
			}else if(!SJQApp.user.isBindMobile) {
				gotBound("免费咨询需绑定手机号");
			}else{
				MobclickAgent.onEvent(WebOpusActivity.this," kControlCaseDetailConslution","logged_add");// 案例详情页咨询点击次数
				if(SJQApp.user.guid.equals(mFromGUID)){
					toastShow("自己不能和自己聊天");
				}else{
					if(DemoHelper.getInstance().isConnected()){
							String LoginName=CommonUtils.removeAllSpace(design.getGuid());
							DemoHelper.getInstance().putlianxi(LoginName, design.getName(), design.getLogo());
							Intent intent=new Intent(WebOpusActivity.this,XiangQinActivity.class);
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
		case R.id.tvComment:
			MobclickAgent.onEvent(WebOpusActivity.this,"kControlCaseDetailComment");// 案例详情页评论点击数

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
		case R.id.userLogo1:
		case R.id.userLogo:
			MobclickAgent.onEvent(WebOpusActivity.this,"kControlCaseDetailLogo");// 案例详情页头像点击数
			Intent intent1 = new Intent(this, WeiShopActivity.class);
			if (design != null)
				intent1.putExtra("IS_MY", false);
			intent1.putExtra(AppTag.TAG_GUID, design.getGuid());
			intent1.putExtra(AppTag.TAG_ID, "");
			startActivity(intent1);
			break;
		case R.id.tagGroup:
			startActivity((Intent) v.getTag());
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
			if (SJQApp.user.guid.equals(mFromGUID)) {
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
		intent.putExtra(AppTag.TAG_GUID, mFromGUID);
		if (design != null)
			intent.putExtra(AppTag.TAG_JSON, design.toString());
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mShareUtils != null) {
			mShareUtils.onActivityResult(requestCode, resultCode, data);
			addCaseAssist(mFromGUID,"2");
		}
		if (requestCode == 0) {
			if (data == null)
				return;
			commentSize = data.getIntExtra("commentSize", 1);
			mTvComment.setText(commentSize+"");
			swCase.setNumComment(commentSize);
		}
	}
	public void doPost() {
		dialog.show();
		if (SJQApp.user != null) {
		String url = AppUrl.FAVORITE_ADD;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("classType","2");
			param.put("userGUID", SJQApp.user.guid);
			param.put("fromGUID", mFromGUID);
			if (!swCase.isFavorite()) {
				param.put("operate", "1");
				MobclickAgent.onEvent(WebOpusActivity.this,"kControlCaseDetailFavorite","logged_add");// 登录点击收藏次数
			} else {
				param.put("operate", "0");
				MobclickAgent.onEvent(WebOpusActivity.this,"kControlCaseDetailFavorite","logged_cancel");//登录点击取消收藏次数

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
								if (!swCase.isFavorite()) {
									swCase.setFavorite(true);
									mItemFavoriteNum = swCase.getNumFavorite()+1;
									swCase.setNumFavorite(mItemFavoriteNum);
									toastShow("收藏成功");
									Drawable drawable = getResources().getDrawable(R.drawable.nav_button_wsce_on);
									drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
									mCollect.setCompoundDrawables(drawable,null,null,null);
									mCollect.setTextColor(getResources().getColor(R.color.app_color));

								    }else{
									swCase.setFavorite(false);
									mItemFavoriteNum = swCase.getNumFavorite()-1;
									  swCase.setNumFavorite(mItemFavoriteNum);
									  toastShow("取消收藏成功");
									Drawable drawable = getResources().getDrawable(R.drawable.nav_button_wsce_default);
									drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
									mCollect.setCompoundDrawables(drawable,null,null,null);
									mCollect.setTextColor(getResources().getColor(R.color.text_normal6));

								   }
//								mCollect.setText(mItemFavoriteNum+"");
								swCase.setNumFavorite(mItemFavoriteNum);
							}
						}

					}
				});
	     } else {
		   MobclickAgent.onEvent(WebOpusActivity.this,"kControlCaseDetailFavorite","Notlogged_add");// 未登录点击收藏次数
		   mCollect.setClickable(false);
		   go2Login();
	      }
	}

	private void go2Login() {
		dialog.dismiss();
		MobclickAgent.onEvent(WebOpusActivity.this," kControlCaseDetailLoginAlert");// 案例详情页弹出登录框的次数
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	private void LoadContents(final String url) {
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> n) {

				SwCaseDetailBen caseDetailBen = CommonUtils.getDomain(n,
						SwCaseDetailBen.class);
				if (caseDetailBen == null || caseDetailBen.result == null
						|| caseDetailBen.result == null) {
					toastShow("没有获取到内容");
					AppErrorLogUtil.getErrorLog(getApplicationContext())
							.postError("200", "GET", url);
					finish();
					return;
				}
				setData(caseDetailBen.result);
				design=caseDetailBen.result.designerInfo;
			   }

			@Override
			public void onFailure(HttpException error, String msg) {
				toastShow("网络错误");
				AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
						"" + error.getExceptionCode(), "GET", url);
				LogUtils.i("opus erro", msg);
				finish();
			}
		};
		getHttpUtils().send(HttpMethod.GET, AppUrl.API + url, getParams(url),
				callBack);

	}
	private boolean initCaseHead(DetailSwCase caseDetail) {

		if (caseDetail == null)
			return false;
		if (mTvTitle != null
				&& !TextUtils.isEmpty(mTvTitle.getText().toString())) {
			return true;
		}
		mTvTitle.setText(caseDetail.title);
		text_titvi.setText(caseDetail.title);
		mTvUser.setText(caseDetail.designerInfo.name);
		mImageUtil.display(mUserLogo, caseDetail.designerInfo.logo);
		mImageUtil.display(mUserLogo1, caseDetail.designerInfo.logo);
		mImageUtil.display(mImageView, caseDetail.getSmallImages());
		if(caseDetail.designerInfo!=null){
			mTvCost.setText("擅长风格："+caseDetail.designerInfo.getStrStyle(this));
			if (TextUtils.isEmpty(caseDetail.designerInfo.cityName)) {
				tvFromCity.setText("全国");
			}else{
				tvFromCity.setText(caseDetail.designerInfo.cityName+"  |");
			}
			if(caseDetail.designerInfo.getCost().equals("面议")){
				feiyong.setText(caseDetail.designerInfo.getCost());
			}else{
                if(caseDetail.designerInfo.getCost().contains("¥")){
					feiyong.setText(caseDetail.designerInfo.getCost());
				}else {
					feiyong.setText("¥ "+caseDetail.designerInfo.getCost());
				}
			}
			if(TextUtils.isEmpty(caseDetail.designerInfo.designing)){
				sheji_linain.setText("分享创意无限，点亮设计灵感");
			}else{
				if(caseDetail.designerInfo.designing.length()>50){
					String designing=caseDetail.designerInfo.designing.substring(0,49);
					designing =designing+"...";
					sheji_linain.setText(designing);
				}else{
					sheji_linain.setText(caseDetail.designerInfo.designing);
				}
			}
		}
		if (!TextUtils.isEmpty(caseDetail.descriptions)) {
			mTvDesc.setText(caseDetail.descriptions);
		} else {
			mTvDesc.setText("暂无简介");
			findViewById(R.id.jianLay).setVisibility(View.GONE);
		}
		mTvDesc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if(!MaxLines){
					MaxLines =true;
					if(mTvDesc.getLineCount()<3) {
						quan_bu.setVisibility(View.GONE);
					}
					mTvDesc.setMaxLines(3);
				}
			}
		});
		tv_tips2.setText(caseDetail.getTips());
		commentSize = caseDetail.numComment;
		mTvComment.setText(commentSize+"");
//		mCollect.setText(caseDetail.numFavorite+"");
		return true;
	}

	// 设置数据
	private void setData(DetailSwCase caseDetail) {
		if (caseDetail == null) {
			return;
		}

		swCase.setNumFavorite(caseDetail.getNumFavorite());//收藏数
		swCase.setNumComment(caseDetail.getNumComment());//评论数

		if(caseDetail.caseItemInfoList!=null){
			mAdapter.setList(caseDetail.caseItemInfoList);
			mAdapter.notifyDataSetChanged();
		}
		hideLoding();
		initCaseHead(caseDetail);
		mFromGUID = caseDetail.guid;
		mFromID=caseDetail.id+"";
		commentSize = caseDetail.numComment;
		mTvComment.setText(commentSize+"");
		// 设置分享内容
		mShareUtils.setShareUrl("http://m.sjq315.com/works/"+caseDetail.id+".html")
				.setShareTitle(caseDetail.title)
				.setShareContent(caseDetail.descriptions)
				.setImageUrl(R.drawable.ic_launcher);

		findViewById(R.id.share).setVisibility(View.VISIBLE);
//		mCollect.setText(caseDetail.numFavorite+"");
		// 当前文章是否收藏
		isFavorite();
	}



	int commentSize = 0;
	private View title_wed;
	private ImageView share,backView;
	@Override
	public void onPause() {
		super.onPause();
		mImageUtil.clearMemoryCache();
		MobclickAgent.onPageEnd("WebOpusActivity");
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WebOpusActivity");
	}

	@Override
	protected void onDestroy() {
		if(mRecyclerView!=null){
			mRecyclerView.setAdapter(null);
		}
		if(mItemPosition != -1){
			Intent intent = new Intent();
			intent.setAction("OpusSave");//效果图收藏
			intent.putExtra(POSITION,mItemPosition);
			intent.putExtra("itemString",swCase.toString());
			sendBroadcast(intent);
		}
		super.onDestroy();
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
	//获取设计师所在地区
	private String getfromCity(int CityID){
			CityService cityService = CityService.getInstance(this);
			if (cityService == null)
				return null;
			String shi=cityService.fromCityID(CityID);
			return shi;
		}
	private void hideLoding() {
		if (mLoadingOver.getVisibility() != View.GONE) {
			Animation out = AnimationUtils.loadAnimation(WebOpusActivity.this,
					R.anim.alpha_out);

			mLoadingOver.setAnimation(out);
			mLoadingOver.setVisibility(View.GONE);
		}
	}
	private void setdenlu(String name){
		Intent intent = new Intent();
		intent.setAction("denglu");
		intent.putExtra("name",name);
		sendBroadcast(intent);
	}

}
