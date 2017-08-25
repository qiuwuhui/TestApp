package com.baolinetworktechnology.shejiquan.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.guojisheng.koyview.MyEditText;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

/**
 * 建议与反馈
 * 
 * @author JiSheng.Guo
 * 
 */
public class AdviceActivity extends BaseActivity {
	private TextView mTvNumer;
	private EditText mEtAdvice;
	private MyEditText mEtPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice);
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
		mTvNumer = (TextView) findViewById(R.id.tvNumer);
		mEtAdvice = (EditText) findViewById(R.id.etAdvice);
		mEtPhone = (MyEditText) findViewById(R.id.metPhone);
		//魅族
		CommonUtils.setMeizuStatusBarDarkIcon(AdviceActivity.this, true);
		//小米
		CommonUtils.setMiuiStatusBarDarkMode(AdviceActivity.this, true);
		// TextView titl2 = (TextView) findViewById(R.id.tv_title2);
		// titl2.setText("提交");
		// titl2.setVisibility(View.VISIBLE);
		// titl2.setOnClickListener(this);
		setTitle("意见与反馈");
		// 字数变化监听
		TextWatcher watcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mTvNumer.setText(s.length() + "/" + 500);

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
		mEtAdvice.addTextChangedListener(watcher);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_title2:
			doFeedBack();
			break;
		case R.id.btnOK:
			doFeedBack();
//			new Thread(downloadRun).start();
			break;

		case R.id.back:
			doBack();

			break;
		}
	}

	// 返回时候监听提示
	private void doBack() {
		if (mEtAdvice.getText().toString().length() == 0) {
			finish();
		} else {
			View dialogView = View.inflate(AdviceActivity.this,
					R.layout.dialog_collect, null);
			TextView titl = (TextView) dialogView
					.findViewById(R.id.dialog_title);
			titl.setText("尚未提交反馈，确定退出？");
			final AlertDialog ad = new AlertDialog.Builder(AdviceActivity.this)
					.setView(dialogView).show();
			dialogView.findViewById(R.id.dialog_cancel).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							ad.cancel();

						}
					});
			dialogView.findViewById(R.id.dialog_ok).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							ad.cancel();
							finish();
						}
					});

		}

	}

	@Override
	protected void onDestroy() {
		dialog = null;
		super.onDestroy();

	}

	@Override
	public void onBackPressed() {
		doBack();
		// super.onBackPressed();
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("AdviceActivity");
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("AdviceActivity");
	}
	// 提交反馈
	private void doFeedBack() {
		String Contents = mEtAdvice.getText().toString();
		String Phone= mEtPhone.getText().toString();
		if (Contents.length() < 3) {
			View dialogView = View.inflate(this, R.layout.dialog_collect, null);
			TextView titl = (TextView) dialogView
					.findViewById(R.id.dialog_title);
			titl.setText("请您提供稍详细些的意见，以便我们改进");
			final AlertDialog ad = new AlertDialog.Builder(this).setView(
					dialogView).show();
			dialogView.findViewById(R.id.dialog_cancel)
					.setVisibility(View.GONE);
			dialogView.findViewById(R.id.dialog_ok).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							ad.cancel();

						}
					});

			return;
		}
		dialog.show();
		final String url =AppUrl.FEEDBACK;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			if(SJQApp.user != null){
				param .put("userGUID",SJQApp.user.getGuid());
			}else{
				param .put("userGUID","");
			}
			param .put("content", Contents);
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
						AppErrorLogUtil.getErrorLog(getApplicationContext())
								.postError(error.getExceptionCode() + "",
										"POST", url);
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						if (dialog == null)
							return;
						dialog.dismiss();
						Bean data = CommonUtils.getDomain(n, Bean.class);
						if (data != null) {

							if (data.success) {
								View dialogView = View.inflate(
										AdviceActivity.this,
										R.layout.dialog_collect, null);
								TextView titl = (TextView) dialogView
										.findViewById(R.id.dialog_title);
								titl.setText("感谢您的反馈，我们将尽快改善");
								final AlertDialog ad = new AlertDialog.Builder(
										AdviceActivity.this)
										.setView(dialogView).show();
								dialogView.findViewById(R.id.dialog_cancel)
										.setVisibility(View.GONE);
								dialogView.findViewById(R.id.dialog_ok)
										.setOnClickListener(
												new View.OnClickListener() {

													@Override
													public void onClick(View v) {
														ad.cancel();
														finish();
													}
												});
							} else {
								toastShow(data.message);
							}
						}

					}
				});
	}
}
