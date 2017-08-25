package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.guojisheng.koyview.SwitchButton;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 评论Activity
 * 
 * @author JiSheng.Guo
 * 
 */
public class CommentActivity extends BaseActivity {
	public static String CLASS_TYPE = "CLASS_TYPE";// 评论对象类型0,1 案例、资讯
	public static String FORM_GUID = "GUID";
	private EditText mEtitText;
	private TextView mTvNumer;// 评论数
	private SwitchButton mSwitchButton;// 匿名开关
	private String mGUID;// 对象Guid
	private int mClassType;// 评论类型
	private boolean mIsChecked = false;// 是否匿名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		mGUID = getIntent().getStringExtra(FORM_GUID);
		mClassType = getIntent().getIntExtra(CLASS_TYPE, 1);
		initView();

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
	private void initView() {
		setTitle("发表评论");
		TextView tv_title2 = (TextView) findViewById(R.id.tv_title2);
		mSwitchButton = (SwitchButton) findViewById(R.id.switchButton);
		tv_title2.setVisibility(View.VISIBLE);
		tv_title2.setText("发布");
		tv_title2.setOnClickListener(this);
		mEtitText = (EditText) findViewById(R.id.etitText);
		mTvNumer = (TextView) findViewById(R.id.tvNumer);
		// 字数变化监听
		TextWatcher watcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mTvNumer.setText(s.length() + "/256");

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		};
		mEtitText.addTextChangedListener(watcher);
		mSwitchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked2) {
				mIsChecked = isChecked2;

			}
		});
		if (SJQApp.user == null) {
			mSwitchButton.setChecked(true);
			mSwitchButton.setEnabled(false);
			mIsChecked = true;
			// toastShow("您未登录,无须匿名");
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_title2:
			doSubmit();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	// 发布评论
	private void doSubmit() {

		String url = ApiUrl.COMMENT_SEND;
		RequestParams params = getParams(url);
		String Contents = mEtitText.getText().toString();

		if (Contents.length() < 2) {
			toastShow("您输入的内容太少了");
			return;

		}
		// 评论对象信息
		params.addBodyParameter("Contents", Contents);
		params.addBodyParameter("FromGuid", mGUID);
		params.addBodyParameter("Star", "5");
		params.addBodyParameter("Evaluate", Contents);
		params.addBodyParameter("ClassType", mClassType + "");
		params.addBodyParameter("AdminGUID",
				getIntent().getStringExtra("FORM_USER_GUID"));
		if (mIsChecked) {
			// 匿名
			params.addBodyParameter("Anonymous", "true");

		}

		if (SJQApp.user != null) {
			params.addBodyParameter("UserID", "" + SJQApp.user.id);
			params.addBodyParameter("UserGuid", SJQApp.user.guid);
			if (SJQApp.userData != null) {
				params.addBodyParameter("UserName", SJQApp.userData.getName());
				params.addBodyParameter("UserLogo", SJQApp.userData.getLogo());
			} else {
				params.addBodyParameter("UserName", SJQApp.user.nickName);
				params.addBodyParameter("UserLogo", SJQApp.user.logo);
			}

		} else {
			//params.addBodyParameter("UserName", "游客");
		}
		// else {}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				dialog.show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dialog.dismiss();
				Bean data = CommonUtils.getDomain(arg0, Bean.class);
				if (data != null) {
					toastShow(data.message);
					if (data.success) {
						setResult(1, new Intent());
						finish();
					}

				}
			}

			@Override
			public void onFailure(HttpException error, String arg1) {
				toastShow("网络请求发生错误");
				dialog.dismiss();
				AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
						error.getExceptionCode() + "", "POST",
						ApiUrl.COMMENT_SEND);
			}
		};

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);

	}
}
