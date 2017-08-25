package com.baolinetworktechnology.shejiquan.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * 选择身份
 * 
 * @author JiSheng.Guo
 * 
 */
public class SelectiDentityActivity extends BaseActivity {
	public static int OK_CODE = 0x101;
	private String mMarkName;// 用户身份标识
	private String mUserGuid;// 用户身GUid
	private int Userid;// 用户身GUid
	private CheckBox cbYZ, cbSJS;
	private boolean isOAuth;
	private String unionId, nickName, headLogo;
	private int fromWay;
	public static String IS_OAUTH = "IS_OAUTH";
	public static String OPEN_ID = "OPEN_ID";
	public static String MARK_NAME = "MARK_NAME";
	public static String USER_TYPE = "USER_TYPE";

	public static String NICK_NAME = "NICK_NAME";
	public static String HEAD_LOGO = "HEAD_LOGO";
	private CheckBox cbgongsi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selecti_dentity);
		findViewById(R.id.btnOk).setOnClickListener(this);
		cbYZ = (CheckBox) findViewById(R.id.cbYezhu);
		cbSJS = (CheckBox) findViewById(R.id.cbSheji);
		cbgongsi = (CheckBox) findViewById(R.id.cbgongsi);
		cbYZ.setOnClickListener(this);
		cbSJS.setOnClickListener(this);
		cbgongsi.setOnClickListener(this);
		dialog.setCancelable(false);

		mUserGuid = getIntent().getStringExtra(AppTag.TAG_GUID);
		Userid =getIntent().getIntExtra(AppTag.TAG_ID,0);
		unionId = getIntent().getStringExtra(OPEN_ID);
		fromWay = getIntent().getIntExtra(MARK_NAME,0);
		isOAuth = getIntent().getBooleanExtra(IS_OAUTH, false);
		nickName = getIntent().getStringExtra(NICK_NAME);
		headLogo = getIntent().getStringExtra(HEAD_LOGO);
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
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnOk:
			if(mMarkName ==AppTag.ENUM_USER_MARK_PERSONAL){
				gotBound("当前选择角色为业主");
			}else if(mMarkName ==AppTag.ENUM_USER_MARK_DESIGNER){
				gotBound("当前选择角色为设计师");
			}else{
				toastShow("您还没有选择身份角色");
			}
			break;
		case R.id.cbYezhu:
			mMarkName = AppTag.ENUM_USER_MARK_PERSONAL;
			cbYZ.setChecked(true);
			cbSJS.setChecked(false);
			cbgongsi.setChecked(false);
			break;
		case R.id.cbSheji:
			mMarkName = AppTag.ENUM_USER_MARK_DESIGNER;
			cbYZ.setChecked(false);
			cbSJS.setChecked(true);
			cbgongsi.setChecked(false);
			break;
		case R.id.cbgongsi:
			mMarkName = AppTag.ENUM_USER_MARK_ENTERPRISE;
			cbYZ.setChecked(false);
			cbSJS.setChecked(false);
			cbgongsi.setChecked(true);
			break;
		default:
			break;
		}
	}

	// 进行设置身份
	private void doSetDentity() {
		if (TextUtils.isEmpty(mMarkName)) {
			new MyAlertDialog(this).setContent("请选择您的身份").setBtnOk("确定").show();
			return;
		}
		String url = AppUrl.USER_CHANGE_IDENTITY;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("guid", mUserGuid);
			param.put("markName", mMarkName);
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
			public void onSuccess(ResponseInfo<String> respon) {
				dialog.dismiss();
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(respon.result, SwresultBen.class);
				if (bean != null) {
					toastShow(bean.result.operatMessage);
					if (bean.result.operatResult) {
						if(isOAuth){
							if (AppTag.ENUM_USER_MARK_PERSONAL.equals(mMarkName)) {
								Intent intent = new Intent();
								intent.putExtra(AppTag.TAG_TITLE, mMarkName);
								setResult(OK_CODE, intent);
								finish();
							} else {// 设计师
								Intent intent = new Intent(SelectiDentityActivity.this,
										OAuth1Activity.class);
								intent.putExtra(OAuth1Activity.OPEN_ID, unionId);
								intent.putExtra(USER_TYPE, mMarkName);
								intent.putExtra(OAuth1Activity.MARK_NAME, fromWay);
								intent.putExtra(NICK_NAME, nickName);
								intent.putExtra(HEAD_LOGO, headLogo);
								intent.putExtra(AppTag.TAG_GUID, mUserGuid);
								intent.putExtra(AppTag.TAG_ID, Userid);
								startActivityForResult(intent,OK_CODE);
								finish();
							}
						}else{
							Intent intent = new Intent();
							intent.putExtra(AppTag.TAG_TITLE, mMarkName);
							setResult(OK_CODE, intent);
							finish();
						}
					}else{
						toastShow(bean.result.operatMessage);
					}
				}
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				dialog.dismiss();
				toastShow(getResources().getString(R.string.network_error));

			}
		};
		getHttpUtils().send(HttpMethod.PUT, AppUrl.API + url, params, callBack);

	}
	private void gotBound(String strshwo){
		View dialogView = View.inflate(getActivity(),
				R.layout.dialog_collect, null);
		TextView titl = (TextView) dialogView
				.findViewById(R.id.dialog_title);
		Button tit2 = (Button) dialogView
				.findViewById(R.id.dialog_cancel);
		titl.setText(strshwo);
		tit2.setText("重新选择");
		final AlertDialog ad = new AlertDialog.Builder(
				getActivity()).setView(dialogView).show();
		dialogView.findViewById(R.id.dialog_cancel)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						cbYZ.setChecked(false);
						cbSJS.setChecked(false);
						mMarkName="";
						ad.cancel();
					}
				});
		dialogView.findViewById(R.id.dialog_ok)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						    ad.cancel();
							doSetDentity();
					}});
	}
}
