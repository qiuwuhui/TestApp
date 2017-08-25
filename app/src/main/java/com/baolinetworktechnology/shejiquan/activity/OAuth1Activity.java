package com.baolinetworktechnology.shejiquan.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.Login;
import com.baolinetworktechnology.shejiquan.domain.SwDesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.UserInfo;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.utils.OAuthHelper;
import com.baolinetworktechnology.shejiquan.view.ButtonM;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.Conde;
import com.google.gson.Gson;
import com.guojisheng.koyview.MyEditText;
import com.guojisheng.koyview.utls.LogUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 第三方登录 选择
 * 
 * @author JiSheng.Guo
 * 
 */
public class OAuth1Activity extends BaseActivity {

	public static String OPEN_ID = "OPEN_ID";
	public static String MARK_NAME = "MARK_NAME";
	final int OAUTH_CODE = 0x8;
	private String openId, nickName, headLogo,mUserGuid;
	private BitmapUtils mImageUtil;
	private MyEditText et_phone,etCode;
	private Conde mGetCode;
	private CircleImg shejiImg;
	private ImageView isCerti;
	private TextView tvName;
	private Button btnLogin;
	private OAuthHelper mOAuthHelper;
	private String strphone="";
	private String strcode="";
	private int Userid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oauth1);
		mOAuthHelper =new OAuthHelper(this);
		mImageUtil = new BitmapUtils(this);
		mImageUtil.configDefaultLoadingImage(R.drawable.icon_morentxy);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.icon_morentxy);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		openId = getIntent().getStringExtra(OPEN_ID);
		nickName = getIntent().getStringExtra(SelectiDentityActivity.NICK_NAME);
		headLogo = getIntent().getStringExtra(SelectiDentityActivity.HEAD_LOGO);
		mUserGuid = getIntent().getStringExtra(AppTag.TAG_GUID);
		Userid = getIntent().getIntExtra(AppTag.TAG_ID,0);
		System.out.println("-->>"+"第三方登录设计师mUserGuid");
		System.out.println("-->>"+"第三方登录设计师mUserGuid");
		System.out.println("-->>"+"mUserGuid =  "+mUserGuid);
		invinView();
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
	private void invinView() {
		findViewById(R.id.back1).setOnClickListener(this);
		 shejiImg = (CircleImg) findViewById(R.id.shejiImg);
		 isCerti = (ImageView) findViewById(R.id.isCerti);
		 tvName = (TextView) findViewById(R.id.tvName);
		 et_phone = (MyEditText) findViewById(R.id.et_phone);
		 et_phone.setHintTextColor(getResources().getColor(R.color.shouqi));
		 etCode = (MyEditText) findViewById(R.id.etCode);
		 etCode.setHintTextColor(getResources().getColor(R.color.shouqi));
		 mGetCode = (Conde) findViewById(R.id.btn_getCode);
		 mGetCode.setConde();
		 mGetCode.setOnClickListener(this);
		 btnLogin = (Button) findViewById(R.id.btnLogin);
		 btnLogin.setOnClickListener(this);
		 btnLogin.setClickable(false);
		 btnLogin.setBackgroundResource(R.drawable.anniu_shape1);

		mImageUtil.display(shejiImg,headLogo);
		tvName.setText(nickName);
		if(SJQApp.getFromWay()==5){
			isCerti.setImageResource(R.drawable.set_bind_weichat_yes);
		}else if(SJQApp.getFromWay()==6){
			isCerti.setImageResource(R.drawable.set_bind_sina_yes);
		}else if(SJQApp.getFromWay()==7){
			isCerti.setImageResource(R.drawable.set_bind_qq_yes);
		}
		et_phone.SetButton(mGetCode);
		et_phone.addTextChangedListener(watcher);
		etCode.addTextChangedListener(watcher);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()){
			case R.id.back1:
				gotBound("仅差最后一步即可加入成功正式设计师，确定要放弃吗？");
				break;
			case R.id.btn_getCode:
				doGetCode();
				break;
			case R.id.btnLogin:
				doCheckCode();
				break;
		}
	}
	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			gotBound("仅差最后一步即可加入成功正式设计师，确定要放弃吗？");
		}
		return false;
	}
	private void doCheckCode() {

		String sPhone = et_phone.getText().toString();
		String code = etCode.getText().toString();
		if (!CommonUtils.checkNumber(sPhone)) {
			et_phone.setError("请输入正确的手机号码");
			return;
		}
		if (code.length() < 4) {
			etCode.setError("请输入正确的验证码");
			return;
		}
		doCheckCode(sPhone, code);

	}
	// 检查验证码是否正确
	private void doCheckCode(final String Mobile, String Captcha) {
		dialog.show("绑定中");
		hideInput();
		String url = AppUrl.MOBILEBIND;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("mobile",Mobile);
			param.put("captcha", Captcha);
//			param.put("userGUID", "b6d09efd-3687-4f7c-bb99-18b6011b8a5d");
			param.put("userGUID", mUserGuid);
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> n) {
				com.baolinetworktechnology.shejiquan.utils.LogUtils.i("login",
						n.result);
				dialog.dismiss();
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
				if (bean != null) {
					if (bean.result.operatResult) {
						toastShow("绑定成功");
						UserInfo userInfo=new UserInfo();
						userInfo.Mobile= et_phone.getText().toString();
						userInfo.setBindMobile(true);
						userInfo.setGuid(mUserGuid);
						userInfo.setId(Userid);
						userInfo.nickName=nickName;
						userInfo.logo=headLogo;
						userInfo.markName="DESIGNER";
						SJQApp app = (SJQApp) getApplication();
						app.caceUserInfo(userInfo);
						app.refresh();
						Intent intent =new Intent(OAuth1Activity.this, SkipActivity.class);
						startActivity(intent);
						finish();
					}else{
						toastShow("绑定失败");
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				dialog.dismiss();
				toastShow("服务器繁忙，请稍后");
				AppErrorLogUtil.getErrorLog(getActivity()).postError(
						error.getExceptionCode() + "", "POST",
						ApiUrl.MOBILE_LOGIN);

			}
		};
		getHttpUtils()
				.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params, callBack);

	}
	private void doGetCode() {
		final String sPhone = et_phone.getText().toString().trim();
		if (!CommonUtils.checkNumber(sPhone)) {
			et_phone.setError("请输入正确的手机号码");
			return;
		}
		CacheUtils.cacheStringData(getActivity(), "phone", sPhone);
		dialog.show();
		mGetCode.start();
		long time = new Date().getTime();
		int  time1 = (int) (time/1000);
		String verify=AppTag.YAN_ZHEN+sPhone+time1;
		verify=MD5Util.getmd5String(verify);
		String url = AppUrl.SEND_SMS_CODE_MLOGINZH + sPhone+"&verify="+verify+"&TimesTamp="+time1;
		getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						dialog.dismiss();
						mGetCode.stop();
						toastShow("发送验证码失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						dialog.dismiss();
						Gson gson = new Gson();
						SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
						if (bean != null) {
							if (bean.result.operatResult) {
								toastShow(bean.result.operatMessage);
							}else{
								mGetCode.stop();
								toastShow(bean.result.operatMessage);
							}
						}
					}
				});
	}
	private void gotBound(String strshwo){
		View dialogView = View.inflate(getActivity(),
				R.layout.dialog_collect, null);
		TextView titl = (TextView) dialogView
				.findViewById(R.id.dialog_title);
		Button quxiao = (Button) dialogView
				.findViewById(R.id.dialog_cancel);
		Button queding = (Button) dialogView
				.findViewById(R.id.dialog_ok);
		titl.setText(strshwo);
		quxiao.setText("放弃");
		queding.setText("继续绑定");
		final AlertDialog ad = new AlertDialog.Builder(
				getActivity()).setView(dialogView).show();
		dialogView.findViewById(R.id.dialog_cancel)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mOAuthHelper.doDeleteWeiChatOauth();
						mOAuthHelper.doDeleteQQOauth();
						mOAuthHelper.doDeleteSinaOauth();
						ad.cancel();
						finish();
					}
				});
		dialogView.findViewById(R.id.dialog_ok)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ad.cancel();
					}});
	}
	public void onResume() {
		super.onResume();
		mGetCode.notifyCode();
	}

	public void onPause() {
		super.onPause();
		mGetCode.waitCode();
	}
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable arg0) {
		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
		}
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
								  int arg3) {
			strphone = et_phone.getText().toString();
			strcode = etCode.getText().toString();
			if(!strphone.equals("") && !strcode.equals("")){
				btnLogin.setClickable(true);
				btnLogin.setBackgroundResource(R.drawable.anniu_shape);
			}
			if(strphone.equals("") || strcode.equals("")){
				btnLogin.setClickable(false);
				btnLogin.setBackgroundResource(R.drawable.anniu_shape1);
			}
		}
	};
}
