package com.baolinetworktechnology.shejiquan.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetwkchnology.shejiquan.xiaoxi.XiangQinActivity;
import com.baolinetworkchnology.shejiquan.map.MaozxActivity;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.R.string;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.DataBen;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.GongSiBean;
import com.baolinetworktechnology.shejiquan.domain.anliBean;
import com.baolinetworktechnology.shejiquan.domain.caselist;
import com.baolinetworktechnology.shejiquan.domain.credentialList;
import com.baolinetworktechnology.shejiquan.net.NetAddRecord;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.google.gson.Gson;
import com.guojisheng.koyview.GScrollView;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class WeizxActivity extends BaseActivity implements OnClickListener {
	private ShareUtils mShareUtils;// 分享工具
	DesignerDetail mDesigner=new DesignerDetail();
	private GScrollView mScroll;
	private TextView tvTitle,chayue,wei_zi,ditu,tvVersionName,anli_titem,tv_cost,
	tv_conten,time_cl,zi_jin,deng_ji,tvcontuer;
	private ImageView image_loge,is_Certi,Image_rz1,Image_rz2,Image_rz3,anli_image;
	private BitmapUtils mImageUtil;
	private CheckBox tvConcer;
	boolean isMy = false;
	boolean isquanbu = false;
	SpannableString msp = null; 
	String businessID;
	String GUID;
	MyDialog dialog;
	GongSiBean bean=null;
	private View gong_zs;
	public  String LoginName="";//联系人标识
	public  String NickName="";//联系人名字
	public  String UserLogo="";//联系人头像
	ArrayList<caselist> CaseList=new ArrayList<caselist>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weizx);
		mImageUtil = new BitmapUtils(this);
		mImageUtil.configDefaultLoadingImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		dialog = new MyDialog(WeizxActivity.this);
		businessID=getIntent().getStringExtra("businessID");
		GUID=getIntent().getStringExtra("GUID");
		inviwe();
		loadata();
		isConcer();
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
	private void inviwe() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
		mScroll = (GScrollView) findViewById(R.id.scroll);
        findViewById(R.id.itemConcer).setOnClickListener(this);
        findViewById(R.id.itemWeiTuo).setOnClickListener(this);
        image_loge =(ImageView) findViewById(R.id.image_loge);
        is_Certi =(ImageView) findViewById(R.id.is_Certi);
        tvTitle=(TextView) findViewById(R.id.tvTitle);
        chayue=(TextView) findViewById(R.id.chayue);
        wei_zi=(TextView) findViewById(R.id.wei_zi);
        ditu=(TextView) findViewById(R.id.ditu);
        ditu.setOnClickListener(this);
        findViewById(R.id.item_update2).setOnClickListener(this);
        tvVersionName=(TextView) findViewById(R.id.tvVersionName);
        anli_image=(ImageView) findViewById(R.id.anli_image);
        anli_image.setOnClickListener(this);
        anli_titem=(TextView) findViewById(R.id.anli_titem);
        tv_cost=(TextView) findViewById(R.id.tv_cost);
        tv_conten=(TextView) findViewById(R.id.tv_conten);
        Image_rz1 =(ImageView) findViewById(R.id.Image_rz1);
        Image_rz2 =(ImageView) findViewById(R.id.Image_rz2);
        Image_rz3 =(ImageView) findViewById(R.id.Image_rz3);
        time_cl =(TextView) findViewById(R.id.time_cl);
        zi_jin =(TextView) findViewById(R.id.zi_jin);
        deng_ji =(TextView) findViewById(R.id.deng_ji);
        tvConcer = (CheckBox) findViewById(R.id.tvConcer);
        tvcontuer =(TextView) findViewById(R.id.tvcontuer);
        gong_zs = findViewById(R.id.gong_zs);
        findViewById(R.id.jianjie_layout).setOnClickListener(this);
        findViewById(R.id.gongs_layout).setOnClickListener(this);
        findViewById(R.id.qiye_layout).setOnClickListener(this);
	}
	private void loadata() {
		String url = ApiUrl.BUSINESSDETAIL +businessID+"&IsRefresh=true"; 
//		+ "&IsRefresh=true;
		dialog.show();
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow(message);
				dialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject json;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, GongSiBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (bean != null) {
					setinview(bean);

				   }
				dialog.dismiss();
			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);
		
	}
	private void setinview(GongSiBean bena){
		LoginName=bena.getLoginName();
		NickName=bena.getNickName();
		UserLogo=bena.getUserLogo();
		mImageUtil.display(image_loge,bena.getLogo());
		tvTitle.setText(bena.getEnterpriseName());
		wei_zi.setText("地址："+bena.getAddress());
		if(bena.getIntroduce()==null){
			tv_conten.setText("暂无");
		}else{
			tv_conten.setText(bena.getIntroduce());			
		}
		tvVersionName.setText("全部"+bena.getNumCase() +"个");
//		Html.fromHtml("<font font size=\"13\" color='#323232'>咨询人数： </font>"
//				+ bena.getNumVisit());
		chayue.setText(Html.fromHtml("<font font size=\"13\" color='#323232'>咨询人数： </font>"
				+ bena.getNumVisit()));
		if(bena.getCaseList()!=null){
			CaseList.clear();
			CaseList=bena.getCaseList();
			mImageUtil.display(anli_image, bena.getCaseList().get(0).getImages());
			anli_titem.setText(bena.getCaseList().get(0).getTitle());
			if(bena.getCaseList().get(0).getClassID()==5){
				tv_cost.setText(bena.getCaseList().get(0).getMcost());					
			}else if(bena.getCaseList().get(0).getClassID()==6){
				tv_cost.setText(bena.getCaseList().get(0).getNumItem()+"图");	
			}
		}else{		
			anli_image.setVisibility(View.GONE);
		}
		chayue.setText(Html.fromHtml("<font font color='#000000'>咨询人数： </font>"
				+ bena.getNumVisit()+" "));
		if(bena.getEstablishTime()!=null){
			time_cl.setText("成立日期："+bena.getEstablishTime());			
		}
		if(bena.getRegisterFund()!=null){
			zi_jin.setText("注册资金："+bena.getRegisterFund());			
		}
		if(bena.getRegisterAuthority()!=null){
			deng_ji.setText("登记机关："+bena.getRegisterAuthority());			
		}
		ArrayList <credentialList> credentialLists=bena.getCredentialList();
		if(credentialLists==null){
			Image_rz1.setVisibility(View.GONE);
			Image_rz2.setVisibility(View.GONE);
			Image_rz3.setVisibility(View.GONE);
			gong_zs.setVisibility(View.VISIBLE);
			return;
		}
		if(credentialLists.size()==0){
			Image_rz1.setVisibility(View.GONE);
			Image_rz2.setVisibility(View.GONE);
			Image_rz3.setVisibility(View.GONE);
			gong_zs.setVisibility(View.VISIBLE);
		}else if(credentialLists.size()==1){
			mImageUtil.display(Image_rz1,credentialLists.get(0).getCertificateUrl());
			Image_rz2.setVisibility(View.GONE);
			Image_rz3.setVisibility(View.GONE);
			gong_zs.setVisibility(View.GONE);
		}else if(credentialLists.size()==2){
			mImageUtil.display(Image_rz1,credentialLists.get(0).getCertificateUrl());
			mImageUtil.display(Image_rz2,credentialLists.get(1).getCertificateUrl());
			Image_rz3.setVisibility(View.GONE);
			gong_zs.setVisibility(View.GONE);
		}else if(credentialLists.size()==3){
			mImageUtil.display(Image_rz1,credentialLists.get(0).getCertificateUrl());
			mImageUtil.display(Image_rz2,credentialLists.get(1).getCertificateUrl());
			mImageUtil.display(Image_rz3,credentialLists.get(2).getCertificateUrl());
			gong_zs.setVisibility(View.GONE);
		}
		
	}
	public void setTvConcer(){
	    tvConcer.setText("关注"); 
	    tvcontuer.setVisibility(View.VISIBLE);
	}
	public void setTvConcerTrue(){
	    tvConcer.setText("已关注"); 
	    tvcontuer.setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		if(bean==null){
			toastShow("获取装修公司数据失败");
			return;
		}
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.ditu:
			Intent intent5=new Intent(WeizxActivity.this,MaozxActivity.class);
			intent5.putExtra("Address", bean.getAddress());
			startActivity(intent5);
			break;
		case R.id.share:
			doShare();
			break;
		case R.id.anli_image:
			if(CaseList!=null){
				Intent intent = new Intent(WeizxActivity.this, WebOpusActivity.class);
				String url = ApiUrl.DETAIL_CASE2 + CaseList.get(0).getId()+"&r="+System.currentTimeMillis();
				intent.putExtra("WEB_URL", url);
				intent.putExtra(AppTag.TAG_ID,CaseList.get(0).getId());
				startActivity(intent);
			}
			break;
		case R.id.itemConcer:
			doConcer();
			break;
		case R.id.item_update2:
			Intent intent4=new Intent(WeizxActivity.this,AnLiownActivity.class);
			intent4.putExtra("GUID", GUID);
			startActivity(intent4);
			break;
		case R.id.qiye_layout:
			Intent intent3=new Intent(WeizxActivity.this,ZhengShuActivity.class);
			intent3.putExtra("GUID", GUID);
			startActivity(intent3);
			break;
		case R.id.jianjie_layout:
			Intent intent1=new Intent(WeizxActivity.this,GongsiActivity.class);
			intent1.putExtra("name", "简介");
			intent1.putExtra("businessID", businessID);
			startActivity(intent1);
			break;
		case R.id.gongs_layout:
			Intent intent2=new Intent(WeizxActivity.this,GongsiActivity.class);
			intent2.putExtra("name", "工商");
			intent2.putExtra("GUID", GUID);
			startActivity(intent2);
			break;
		case R.id.itemWeiTuo:
			if(SJQApp.user==null){
				((SJQApp) getApplication()).exitAccount();
				startActivity(new Intent(WeizxActivity.this,
						LoginActivity.class));
			}else{
				if(SJQApp.user.guid.equals(GUID)){
					toastShow("自己不能和自己聊天");
				}else{
					if(DemoHelper.getInstance().isLoggedIn()){
						if(LoginName.equals("")){
							toastShow("暂时无法联系");	
						}else{
							DemoHelper.getInstance().putlianxi(LoginName, NickName, UserLogo);
							Intent intent8=new Intent(WeizxActivity.this,XiangQinActivity.class); 
							intent8.putExtra("UserName",LoginName);
							startActivity(intent8);																						
						}
					}else{
						toastShow("账号聊天未开通，请联系客服开通账号");
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
		if (SJQApp.user == null) {
			toastShow("请先登录");
			go2Login();
			return;
		}
		if (TextUtils.isEmpty(GUID)) {
			toastShow("请稍候");
			return;
		}
		SJQApp.isRrefresh = true;
		if ("已关注".equals(tvConcer.getText().toString())) {
			// 取消关注
			tvConcer.setText("取消中..");
			String url = ApiUrl.FAVORITE_CANCEL;
			RequestParams params = getParams(url);
			params.addBodyParameter("Type", AppTag.Sql_gz);
			params.addBodyParameter("ClassType", AppTag.Sql_gs);
			params.addBodyParameter("UserGUID", SJQApp.user.guid);
			params.addBodyParameter("FromGUID", GUID);

			RequestCallBack<String> callBack = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					LogUtils.i("设计师详情头部", arg0.result);
					DataBen data = CommonUtils.getDomain(arg0, DataBen.class);
					if (data != null) {
						if (data.success) {
							setTvConcer();
							tvConcer.setChecked(false);
							if (SJQApp.userData != null) {
								SJQApp.userData.numConcer--;
							}
						} else {
							setTvConcerTrue();
							tvConcer.setChecked(true);
							toastShow("取消失败");
							isConcer();
						}
					} else {
						toastShow("取消失败");
						setTvConcerTrue();
						tvConcer.setChecked(true);

					}
				}

				@Override
				public void onFailure(HttpException error, String arg1) {
					LogUtils.i("erro:设计师详情头部", arg1);
					setTvConcer();
					tvConcer.setChecked(false);
					toastShow(getResources().getString(R.string.network_error));
					AppErrorLogUtil.getErrorLog(WeizxActivity.this).postError(
							error.getExceptionCode() + "", "POST",
							ApiUrl.FAVORITE_CANCEL);
				}
			};
			getHttpUtils().send(HttpMethod.POST, ApiUrl.API + url, params,
					callBack);

		} else {
			// 加关注
			if (SJQApp.user.guid.equals(GUID)) {
				toastShow("无需关注自己");
				return;
			}
			tvConcer.setText("关注中..");
			String url = ApiUrl.FAVORITE_ADD;
			RequestParams params = getParams(url);
			params.addBodyParameter("Type", AppTag.Sql_gz);
			params.addBodyParameter("ClassType", AppTag.Sql_gs);
			params.addBodyParameter("UserGUID", SJQApp.user.guid);
			params.addBodyParameter("FromGUID", GUID);

			RequestCallBack<String> callBack = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					DataBen data = CommonUtils.getDomain(arg0, DataBen.class);
					if (data != null) {
						if (data.success) {
							setTvConcerTrue();
							if (SJQApp.userData != null) {
								SJQApp.userData.numConcer++;
							}
							tvConcer.setChecked(true);

						} else {
							setTvConcer();
							tvConcer.setChecked(false);

							toastShow("关注失败");
							isConcer();
						}
					} else {
						toastShow("关注失败");
						setTvConcer();
						tvConcer.setChecked(false);
					}
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					setTvConcer();
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
		Intent intent = new Intent(WeizxActivity.this, LoginActivity.class);
	    startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mShareUtils == null) {
			mShareUtils = new ShareUtils(this);
		}
	}
	private void mScrollOnTou() {
		mScroll.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	private void loadWeb() {
		isConcer();
		new NetAddRecord(GUID);// 添加到访客记录
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
		params.addBodyParameter("Type", AppTag.Sql_gz);
		params.addBodyParameter("ClassType", AppTag.Sql_gs);
		params.addBodyParameter("UserGUID", SJQApp.user.guid);
		if (!TextUtils.isEmpty(GUID)) {
			params.addBodyParameter("FromGUID", GUID);
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

						setTvConcerTrue();
						tvConcer.setChecked(true);
					} else {
						setTvConcer();
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
	/**
	 * 分享
	 */
	private void doShare() {
		if (mShareUtils != null) {
			mShareUtils.setShareUrl(ApiUrl.SHARE_SHEJIQUAN)
			.setImageUrl(R.drawable.ic_launcher)
			.setShareTitle("设计圈")
			.addToSocialSDK()
			.setShareContent2(
					"让设计更有价值，我正在使用#设计圈#app，让我的装修变成一种享受，推荐给大家"
							+ ApiUrl.SHARE_SHEJIQUAN).doShare();
		}
	}
}
