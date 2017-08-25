package com.baolinetworktechnology.shejiquan.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetwkchnology.shejiquan.xiaoxi.XiangQinActivity;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.SheJiShirz;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.google.gson.Gson;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SmActivity extends BaseActivity implements OnClickListener{
	private BitmapUtils mImageUtil;
	private View tixing;
	private CircleImg userHead;
	private TextView sjs_name,sjs_hm,sjs_time;
	private ImageView ren_zhen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sm);
		mImageUtil = new BitmapUtils(this);
		//加载图片类
		mImageUtil.configDefaultLoadingImage(R.drawable.icon_grsmrz);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.icon_grsmrz);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.kefu).setOnClickListener(this);
		findViewById(R.id.sjq_imge).setOnClickListener(this);
		
		tixing = findViewById(R.id.tixing);
		userHead = (CircleImg) findViewById(R.id.userHead);
		sjs_name = (TextView) findViewById(R.id.sjs_name);
		sjs_hm=(TextView) findViewById(R.id.sjs_hm);
		sjs_time=(TextView) findViewById(R.id.sjs_time);
		ren_zhen = (ImageView) findViewById(R.id.ren_zhen);
		loadata();
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
	private void loadata() {
		String url = AppUrl.DESIGNERCERTIFICATION+SJQApp.userData.getGuid();
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
				SheJiShirz bean=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, SheJiShirz.class);
					if(bean!=null){
						senview(bean);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
        			dialog.dismiss();
			}
		};
		getHttpUtils().send(HttpMethod.GET, AppUrl.API + url, getParams(url),
				callBack);		
	}
	private void senview(SheJiShirz bean) {
		mImageUtil.display(userHead, bean.getLogo());
		sjs_name.setText(bean.getName());
		if(bean.getCardNo()!=null){
			sjs_hm.setText("身份证："+tihuan(bean.getCardNo()));			
		}else{
			sjs_hm.setText("身份证："+"未填写");	
		}
		if(bean.getCertificationStatus()==3){
			ren_zhen.setBackgroundResource(drawable.ren_zhen_zc);
		}else if(bean.getCertificationStatus()==2){
			ren_zhen.setBackgroundResource(drawable.ren_zhen_dai);
		}else{
			ren_zhen.setBackgroundResource(drawable.ren_zhen_wei);
		}
		if(bean.getCertificationStatus()==3){
			sjs_time.setText("认证时间："+bean.getCertTime());
			tixing.setVisibility(View.GONE);
			findViewById(R.id.kefu).setVisibility(View.GONE);
		}else{
			sjs_time.setText("提交时间："+bean.getSubCertificationTime());
			tixing.setVisibility(View.VISIBLE);
		}		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.kefu:
			MobclickAgent.onEvent(getActivity(),"kControlDesignerCertificationStatusPhone"); //设计师认证状态界面电话联系事件
			requestPermission(new String[]{Manifest.permission.CALL_PHONE}, 0x0001);
			break;
		case id.sjq_imge:
			MobclickAgent.onEvent(getActivity(),"kControlDesignerCertificationStatusOnline"); //设计师认证状态界面在线联系事件
			if(DemoHelper.getInstance().isLoggedIn()){
				Intent intent1=new Intent(getActivity(),XiangQinActivity.class);
				intent1.putExtra("UserName","KF-SJQ");
				intent1.putExtra("SJQ", 1);
				startActivity(intent1);
			}else{
				Toast.makeText(getActivity(), "账号聊天未开通，请联系客服开通账号", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}
	private void showCall() {
		new MyAlertDialog(this).setTitle(SJQApp.kefuCall)
				.setBtnCancel("取消").setBtnOk("呼叫")
				.setBtnOnListener(new DialogOnListener() {

				@Override
				public void onClickListener(boolean isOk) {
					if (isOk)
					doCall(SJQApp.kefuCall);
					}
				}).show();

		}
	private void doCall(String string) {
		// 调用到编辑框的值
		// 新建一个intent对象，进行调用系统的打电话的方法，然后传递号码过去
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ string));
		// 相应事件
		startActivity(intent);
		}
	private String tihuan(String str){
		char[] cs = str.toCharArray();
		int len = str.length();
		for (int i = 0; i < str.length(); i++) {
			if(i>5 && i<14){
				cs[i] = '*';							
			}
		}		
		return String.valueOf(cs);		
	}
	@Override
	public void permissionSuccess(int requestCode) {
		super.permissionSuccess(requestCode);
		switch (requestCode) {
			case 0x0001:
				showCall();
				break;
		}
	}
}
