package com.baolinetworktechnology.shejiquan.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.interfaces.OnOAuthListener;
import com.baolinetworktechnology.shejiquan.utils.OAuthHelper;
import com.baolinetworktechnology.shejiquan.view.ButtonM;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.exception.SocializeException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 帐号绑定管理
 * 
 * @author JiSheng.Guo
 * 
 */
public class BindManageActivity extends BaseActivity implements
		OnCheckedChangeListener, OnOAuthListener {
	private OAuthHelper mOAuthHelper;
	TextView amend_butt;
	int markName;
	private TextView cellphone,tv_ivPasswa;
	ImageView mIvQQ, mIvPhone, mIvSina, mIvWeiChat;
	private ButtonM sbTZ,sbWX,sbWB,sbQQ;
	private static final String[] colorList = {"#ffffff","#009AF7"};
	private static final String[] colorText = {"#000000","#ffffff"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_manage);
		setTitle(getTitle().toString());
		sbTZ= (ButtonM) findViewById(R.id.sbTZ);
		sbTZ.setOnClickListener(this);
		sbTZ.setFillet(true);
		sbTZ.setRadius(18);
		sbWX= (ButtonM) findViewById(R.id.sbWX);
		sbWX.setOnClickListener(this);
		sbWX.setFillet(true);
		sbWX.setRadius(18);
		sbWB= (ButtonM) findViewById(R.id.sbWB);
		sbWB.setOnClickListener(this);
		sbWB.setFillet(true);
		sbWB.setRadius(18);
		sbQQ= (ButtonM) findViewById(R.id.sbQQ);
		sbQQ.setOnClickListener(this);
		sbQQ.setFillet(true);
		sbQQ.setRadius(18);
		mOAuthHelper = new OAuthHelper(this);
		mOAuthHelper.setOnOAuthListener(this);
		cellphone = (TextView) findViewById(R.id.cellphone);
		amend_butt = (TextView) findViewById(R.id.amend_butt);
		amend_butt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String buttText = amend_butt.getText().toString();
				if (buttText.equals("去绑定")) {
					Intent intent = new Intent(getActivity(),
							BoundActivity.class);
					intent.putExtra(AppTag.TAG_JSON, SJQApp.user.getMobile());
					startActivity(intent);
				}
//				else {
//					Intent intent = new Intent(getActivity(),
//							MobilePhoneActivity.class);
//					intent.putExtra(AppTag.TAG_JSON, SJQApp.user.getMobile());
//					startActivity(intent);
//				}
			}
		});
		findViewById(R.id.item_passwod).setOnClickListener(this);
		tv_ivPasswa = (TextView) findViewById(R.id.tv_ivPasswa);
		tv_ivPasswa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.putExtra(FindPasswordActivity.IS_SET_PAWSD,true);
				intent.setClass(BindManageActivity.this, FindPasswordActivity.class);
				startActivity(intent);
			}
		});
		mIvQQ = (ImageView) findViewById(R.id.ivQQ_tp);
		mIvPhone = (ImageView) findViewById(R.id.ivPhone_tp);
		mIvSina = (ImageView) findViewById(R.id.ivSina_tp);
		mIvWeiChat = (ImageView) findViewById(R.id.ivWeiChat_tp);
		setBind();
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
	@Override
	protected void onResume() {
		super.onResume();
		initUI();
//		setBind();
	}

	private void initUI() {
		if (sbQQ == null)
			return;
		if (SJQApp.userData != null) {
			if (!TextUtils.isEmpty(SJQApp.userData.mobile)) {// 绑定手机号;
				cellphone.setText("手机号");
				String mobile=SJQApp.userData.mobile;
				mobile=tihuan(mobile);
				amend_butt.setText(mobile);
			}
		} else if (SJQApp.ownerData != null) {
			if (!TextUtils.isEmpty(SJQApp.ownerData.getMobile())) {// 绑定手机号;
				cellphone.setText("手机号");
				String mobile=SJQApp.ownerData.getMobile();
				mobile=tihuan(mobile);
				amend_butt.setText(mobile);
			} else {// 未绑定
				cellphone.setText("手机号未绑定");
				amend_butt.setText("去绑定");
			}
		}

		// 是否绑定QQ
		if (SJQApp.user.isBindQQ) {
			sbQQ.setBackColor(Color.parseColor(colorList[0]));
			sbQQ.setText("√已绑定");
			sbQQ.setTextColori(Color.parseColor(colorText[0]));
		} else {
			sbQQ.setBackColor(Color.parseColor(colorList[1]));
			sbQQ.setText("去绑定");
			sbQQ.setTextColori(Color.parseColor(colorText[1]));
		}

		// 是否绑定微信
		if (SJQApp.user.isBindWeiXin) {
			sbWX.setBackColor(Color.parseColor(colorList[0]));
			sbWX.setText("√已绑定");
			sbWX.setTextColori(Color.parseColor(colorText[0]));
		} else {
			sbWX.setBackColor(Color.parseColor(colorList[1]));
			sbWX.setText("去绑定");
			sbWX.setTextColori(Color.parseColor(colorText[1]));
		}
		if (SJQApp.user.isBindWeibo) {
			sbWB.setBackColor(Color.parseColor(colorList[0]));
			sbWB.setText("√已绑定");
			sbWB.setTextColori(Color.parseColor(colorText[0]));
		} else {
			sbWB.setBackColor(Color.parseColor(colorList[1]));
			sbWB.setText("去绑定");
			sbWB.setTextColori(Color.parseColor(colorText[1]));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.back:
				finish();
				break;
			case R.id.item_passwod:
				Intent intent = new Intent();
				intent.putExtra(FindPasswordActivity.IS_SET_PAWSD,true);
				intent.setClass(BindManageActivity.this, FindPasswordActivity.class);
				startActivity(intent);
				break;
			case R.id.sbQQ:
				markName = 1;
				if(SJQApp.user.isBindQQ){
					showView("QQ");
				}else{
					mOAuthHelper.doOauthQQ();
				}
				break;
			case R.id.sbWX:
				markName = 2;
				if(SJQApp.user.isBindWeiXin){
					showView("微信");
				}else{
					mOAuthHelper.doOauthWeChat();
				}
				break;
			case R.id.sbWB:
				markName = 3;
				if(SJQApp.user.isBindWeibo){
					showView("新浪微博");
				}else{
					mOAuthHelper.doOauthSina();
				}
				break;
		    }
	}

	@Override
	public void onCheckedChanged(final CompoundButton buttonView,
			boolean isChecked) {
		if (isChecked) {// 选中
			if (buttonView.getTag() == null) {
				buttonView.setTag(null);
				switch (buttonView.getId()) {
				case R.id.sbQQ:
					mOAuthHelper.doOauthQQ();
					markName = 1;
					break;
				case R.id.sbWX:
					mOAuthHelper.doOauthWeChat();
					markName = 2;
					break;
				case R.id.sbWB:
					mOAuthHelper.doOauthSina();
					markName = 3;
					break;
				default:
					break;
				}
			}
		} else {// 取消绑定
			String titles = "";
			switch (buttonView.getId()) {
			case R.id.sbQQ:
				titles = "QQ";
				// .show();
				break;
			case R.id.sbWX:
				titles = "微信";
				break;
			case R.id.sbWB:
				titles = "新浪微博";
				break;
			default:
				break;
			}
			View view = View.inflate(this, R.layout.dialog_bind, null);

			final AlertDialog alert = new AlertDialog.Builder(this)
					.setView(view).setCancelable(false).create();
			TextView title = (TextView) view.findViewById(R.id.dialog_title);
			view.findViewById(R.id.dialog_ok).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							buttonView.setTag(null);
							switch (buttonView.getId()) {
							case R.id.sbQQ:
								markName = 1;
								break;
							case R.id.sbWX:
								markName = 2;
								break;
							case R.id.sbWB:
								markName = 3;

								break;
							default:
								break;
							}
							DeleteSinaOauth(markName + "");
							alert.cancel();
						}

					});
			view.findViewById(R.id.dialog_cancel).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							buttonView.setTag("");
							buttonView.setChecked(true);
							alert.cancel();

						}
					});
			String name = getResources().getString(R.string.adialog_bin_title);
			title.setText(String.format(name, titles));
			alert.show();
		}
	}
	private void showView(String titles){
		View view = View.inflate(this, R.layout.dialog_bind, null);

		final AlertDialog alert = new AlertDialog.Builder(this)
				.setView(view).setCancelable(false).create();
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		view.findViewById(R.id.dialog_ok).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						DeleteSinaOauth(markName + "");
						alert.cancel();
					}

				});
		view.findViewById(R.id.dialog_cancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						alert.cancel();

					}
				});
		String name = getResources().getString(R.string.adialog_bin_title);
		title.setText(String.format(name, titles));
		alert.show();
	}
	private void DeleteSinaOauth(String markNames) {
		// ApiUrl.CANCEL_THIRDINFO
		if (SJQApp.user == null) {
			return;
		}
		String url = AppUrl.CANCEL_THIRDINFO;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("userGUID",SJQApp.user.guid);
			param.put("type", markNames);
			param.put("client", AppTag.CLIENT);
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
				if (dialog == null)
					return;
				dialog.show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null)
					dialog.dismiss();
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
				if (bean != null) {
					if (bean.result.operatResult) {
						toastShow(bean.result.operatMessage);
						switch (markName) {
						case 1:
							SJQApp.user.isBindQQ = false;
							sbQQ.setBackColor(Color.parseColor(colorList[1]));
							sbQQ.setText("去绑定");
							sbQQ.setTextColori(Color.parseColor(colorText[1]));
							break;
						case 2:
							SJQApp.user.isBindWeiXin = false;
							sbWX.setBackColor(Color.parseColor(colorList[1]));
							sbWX.setText("去绑定");
							sbWX.setTextColori(Color.parseColor(colorText[1]));
							break;
						case 3:
							SJQApp.user.isBindWeibo = false;
							sbWB.setBackColor(Color.parseColor(colorList[1]));
							sbWB.setText("去绑定");
							sbWB.setTextColori(Color.parseColor(colorText[1]));
							break;
						}
						setBind();
					} else {
						toastShow(bean.result.operatMessage);
						switch (markName) {
						case 1:
							SJQApp.user.isBindQQ = true;
							break;
						case 2:
							SJQApp.user.isBindWeiXin = true;
							break;
						case 3:
							SJQApp.user.isBindWeibo = true;
							break;

						}
					}

				}
				initUI();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (dialog != null)
					dialog.dismiss();
				initUI();

			}
		};
		getHttpUtils()
				.send(HttpMethod.POST, AppUrl.API + url, params, callBack);

	}

	@Override
	public void onStart(SHARE_MEDIA platform) {
		if (dialog == null)
			return;
		dialog.show("正在绑定");

	}

	@Override
	public void onComplete(Bundle value, SHARE_MEDIA platform) {
		if (dialog == null)
			return;
		dialog.dismiss();

	}

	@Override
	public void onComplete(int status, Map<String, Object> info) {
		if (dialog == null)
			return;
		dialog.dismiss();
		String openId = (String) info.get("openid");
		String unionId = (String) info.get("unionid");
//		if (openId == null || openId.equals("")) {
//			openId = (String) info.get("uid");
//		}
		System.out.println("-->>openid=" + openId);
		System.out.println("-->>unionid=" + unionId);
		if (SJQApp.user != null) {
			doOAuth(SJQApp.user.guid, openId,unionId, markName + "");

		}
		if (dialog == null)
			return;
		dialog.dismiss();

	}

	@Override
	public void onCancel(SHARE_MEDIA platform) {
		if (dialog == null)
			return;
		dialog.dismiss();
		initUI();
	}

	@Override
	public void onError(SocializeException e, SHARE_MEDIA platform) {
		if (dialog == null)
			return;
		dialog.dismiss();
		initUI();
	}

	/**
	 *
	 * @param openId
	 *            QQ 1 WEIXIN 2 WEIBO 3
	 */
	protected void doOAuth(String userGuid, String openId,String unionId, String markNames) {
		if (dialog == null)
			return;
		dialog.show("正在绑定中");
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null)
					dialog.dismiss();

				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
				if (bean != null) {
					if (bean.result.operatResult) {
						toastShow("绑定成功");
						switch (markName) {
						case 1:
							SJQApp.user.setBindQQ(true);
							sbQQ.setBackColor(Color.parseColor(colorList[0]));
							sbQQ.setText("√已绑定");
							sbQQ.setTextColori(Color.parseColor(colorText[0]));
							break;
						case 2:
							SJQApp.user.setBindWeiXin(true);
							sbWX.setBackColor(Color.parseColor(colorList[0]));
							sbWX.setText("√已绑定");
							sbWX.setTextColori(Color.parseColor(colorText[0]));
							break;
						case 3:
							SJQApp.user.setBindWeibo(true);
							sbWB.setBackColor(Color.parseColor(colorList[0]));
							sbWB.setText("√已绑定");
							sbWB.setTextColori(Color.parseColor(colorText[0]));
							break;
						}
						setBind();
					} else {
						toastShow(bean.result.operatMessage);
						switch (markName) {
						case 1:
							SJQApp.user.setBindQQ(false);
							break;
						case 2:
							SJQApp.user.setBindWeiXin(false);
							break;
						case 3:
							SJQApp.user.setBindWeibo(false);
							break;
						}
					}
				} else {
					toastShow("绑定失败");
				}
				initUI();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (dialog == null)
					return;
				mOAuthHelper.doDeleteWeiChatOauth();
				mOAuthHelper.doDeleteQQOauth();
				mOAuthHelper.doDeleteSinaOauth();
				toastShow("网络请求失败");
				if (dialog != null)
					dialog.dismiss();
			}
		};
		String url = AppUrl.AddUserThirdInfo;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("userGUID",userGuid);
			if(markNames.equals("2")){
				param.put("unionId", unionId);
				param.put("openID", openId);
			}else{
				param.put("unionId", openId);
			}
			param.put("type", markNames);
			param.put("client", AppTag.CLIENT);
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
			System.out.println("-->>第三方绑定参数=" + param.toString());
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		params.addBodyParameter("OpenID", openId);
//		params.addBodyParameter("UserGUID", userGuid);
//		params.addBodyParameter("MarkName", markNames);
//		params.addBodyParameter("Client", AppTag.CLIENT);
		getHttpUtils()
				.send(HttpMethod.POST, AppUrl.API + url, params, callBack);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		if (mOAuthHelper != null)
			mOAuthHelper.onActivityResult(requestCode, resultCode, data);
	}
	private void setBind() {
		if (SJQApp.user == null)
			return;
		if (SJQApp.user.isBindMobile) {
			mIvPhone.setImageResource(R.drawable.set_bind_phone_yes);
		} else {
			mIvPhone.setImageResource(R.drawable.set_bind_phone_no);
		}
		if (SJQApp.user.isBindQQ) {
			mIvQQ.setImageResource(R.drawable.set_bind_qq_yes);
		} else {
			mIvQQ.setImageResource(R.drawable.set_bind_qq);
		}
		if (SJQApp.user.isBindWeiXin) {
			mIvWeiChat.setImageResource(R.drawable.set_bind_weichat_yes);
		} else {
			mIvWeiChat.setImageResource(R.drawable.set_bind_weichat);
		}
		if (SJQApp.user.isBindWeibo) {
			mIvSina.setImageResource(R.drawable.set_bind_sina_yes);
		} else {
			mIvSina.setImageResource(R.drawable.set_bind_sina);
		}

	}
	private String tihuan(String str){
		if(!TextUtils.isEmpty(str)){
			char[] cs = str.toCharArray();
			int len = str.length();
			for (int i = 0; i < str.length(); i++) {
				if(i>3 && i<7){
					cs[i] = '*';
				}
			}
			return String.valueOf(cs);
		}
		return "";
	}
}
