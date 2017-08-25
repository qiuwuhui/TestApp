package com.baolinetworktechnology.shejiquan.activity;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.Conde;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class BoundActivity extends BaseActivity implements OnClickListener {
	private String number;
	EditText number_edit, verify_edit;
	Button queding;
	View shwocomten;
	private Conde mGetCode;
	private TextView tvName;
	private BitmapUtils mImageUtil;
	private String strphone="";
	private String strcode="";
	private ImageView tv_title2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bound);
		tv_title2 = (ImageView) findViewById(R.id.tv_title2);
		tv_title2.setOnClickListener(this);
		mImageUtil = new BitmapUtils(this);
		mImageUtil.configDefaultLoadingImage(R.drawable.icon_morentxy);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.icon_morentxy);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
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
		findViewById(R.id.shwocomten).setOnClickListener(this);
		findViewById(R.id.tvConten).setOnClickListener(this);
		mGetCode = (Conde) findViewById(R.id.gain_butt);
		mGetCode.setConde();
		mGetCode.setOnClickListener(this);
		CircleImg user_long= (CircleImg) findViewById(R.id.user_long);
		mImageUtil.display(user_long,SJQApp.user.logo);
		number_edit = (EditText) findViewById(R.id.number_edit);
		number_edit.setHintTextColor(getResources().getColor(R.color.shouqi));
		verify_edit = (EditText) findViewById(R.id.verify_edit);
		verify_edit.setHintTextColor(getResources().getColor(R.color.shouqi));
		queding = (Button) findViewById(R.id.queding);
		queding.setOnClickListener(this);
		queding.setClickable(false);
		queding.setBackgroundResource(R.drawable.anniu_shape1);
		shwocomten=findViewById(R.id.shwocomten);
		findViewById(R.id.zhidao).setOnClickListener(this);
		TextView tvConten= (TextView) findViewById(R.id.tvConten);
		findViewById(R.id.text_xy).setOnClickListener(this);
		String str = "根据这国家网络信息办公室颁布的"
				+ "<font color= '#FD6530'>《移动互联网应用程序信息服务管理规定》</font>"+"，从2016年8月1日起，互联网注册用户应当提供基于移动电话号码等方式的真实身份信息，才能进行发布作品、发贴、回复、私信、评论等互动操作，所以需要您绑定手机号。";
		tvConten.setText(Html.fromHtml(str));
		tvName = (TextView) findViewById(R.id.tvName);
		tvName.setText(SJQApp.user.nickName);
		ImageView isCerti = (ImageView) findViewById(R.id.isCerti);
		int fromWay=SJQApp.getFromWay();
		if(SJQApp.user != null){
			if(SJQApp.user.isBindWeiXin){
				isCerti.setImageResource(R.drawable.set_bind_weichat_yes);
			}else if(SJQApp.user.isBindWeibo){
				isCerti.setImageResource(R.drawable.set_bind_sina_yes);
			}else if(SJQApp.user.isBindQQ){
				isCerti.setImageResource(R.drawable.set_bind_qq_yes);
			}
		}
		number_edit.addTextChangedListener(watcher);
		verify_edit.addTextChangedListener(watcher);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title2:
			finish();
			break;
		case R.id.zhidao:
			shwocomten.setVisibility(View.GONE);
			break;
		case R.id.gain_butt:
			number = number_edit.getText().toString();
			if (CommonUtils.checkNumber(number)) {
				doGetCode();
			} else {
				toastShow("请输入正确的手机号码");
			}
			break;
		case R.id.queding:
			String verify = verify_edit.getText().toString();
			number = number_edit.getText().toString();
			if (verify.equals("")) {
				toastShow("请输入验证码");
			} else if (CommonUtils.checkNumber(number)) {
				doQueDing();
			} else {
				toastShow("请输入正确的手机号码");
			}
			break;
		case R.id.text_xy:
			Intent intent = new Intent(getActivity(), WebActivity.class);
			intent.putExtra(WebActivity.URL,"http://www.sjq315.com/themes/scheme/mobile.html");
			startActivity(intent);
			break;

		default:
			break;
		}
		super.onClick(v);
	}
	private void doQueDing() {
		if (dialog == null)
			dialog = new MyDialog(this);
		String Mobile = number_edit.getText().toString();
		String Captcha = verify_edit.getText().toString();
		String url = AppUrl.MOBILEBIND;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("mobile",Mobile);
			param.put("captcha", Captcha);
//			param.put("userGUID", "2459e530-7346-47ce-880a-f862d310965a");
			param.put("userGUID", SJQApp.user.guid);
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
				dialog.show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				System.out.println("-->>"+"第三方登录手机绑定");
				System.out.println("-->>"+"第三方登录手机绑定");
				System.out.println("-->>"+arg0.toString());
				dialog.dismiss();
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
				if (bean != null) {
					if (bean.result.operatResult) {
						toastShow("绑定成功");
						SJQApp.user.Mobile = number_edit.getText().toString();
						SJQApp.user.setBindMobile(true);
						((SJQApp) getApplication()).caceUserInfo(SJQApp.user);
						((SJQApp) getApplication()).refresh();
						Intent intent1 = new Intent();
						intent1.setAction("showView");
						sendBroadcast(intent1);
						Intent intent =new Intent(BoundActivity.this, SkipActivity.class);
						startActivity(intent);
						finish();
					}else{
						toastShow(bean.result.operatMessage);
					}
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				dialog.dismiss();
				toastShow("绑定失败");
			}
		};
		getHttpUtils().send(HttpMethod.POST, AppUrl.API + url, params, callBack);
	}
	private void doGetCode() {
		final String sPhone = number_edit.getText().toString().trim();
		if (!CommonUtils.checkNumber(sPhone)) {
			number_edit.setError("请输入正确的手机号码");
			return;
		}
		dialog.show();
		mGetCode.start();
		long time = new Date().getTime();
		int  time1 = (int) (time/1000);
		String verify=AppTag.YAN_ZHEN+sPhone+time1;
		verify=MD5Util.getmd5String(verify);
		String url = AppUrl.SEND_SMS_CODE_MLOGINZH+sPhone+"&verify="+verify+"&TimesTamp="+time1;
		getHttpUtils().send(HttpMethod.GET, AppUrl.API + url, getParams(url),
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						dialog.dismiss();
						mGetCode.stop();
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
								toastShow(bean.result.operatMessage);
								mGetCode.stop();
							}
						}
					}
				});
	}
	public void onResume() {
		super.onResume();
		mGetCode.notifyCode();
	}

	public void onPause() {
		super.onPause();
		mGetCode.waitCode();
	}private TextWatcher watcher = new TextWatcher() {
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
			strphone = number_edit.getText().toString();
			strcode = verify_edit.getText().toString();
			if(!strphone.equals("") && !strcode.equals("")){
				queding.setClickable(true);
				queding.setBackgroundResource(R.drawable.anniu_shape);
			}
			if(strphone.equals("") || strcode.equals("")){
				queding.setClickable(false);
				queding.setBackgroundResource(R.drawable.anniu_shape1);
			}
		}
	};

}
