package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;

/**
 * 修改数据
 * 
 * @author JiSheng.Guo
 * 
 */
public class SetDataActivity extends BaseActivity {
	private EditText mEtitText;
	private TextView mTvNumer;
	private int mTextNumer = 500;
	private CharSequence temp;
	private int selectionStart;
    private int selectionEnd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_data);

		mTvNumer = (TextView) findViewById(R.id.tvNumer);
		TextView tv_title2 = (TextView) findViewById(R.id.tv_title2);
		mEtitText = (EditText) findViewById(R.id.etitText);

		setTitle(getIntent().getStringExtra(AppTag.TAG_TITLE));

		tv_title2.setVisibility(View.VISIBLE);
		tv_title2.setText("保存");
		tv_title2.setOnClickListener(this);

		boolean single = getIntent().getBooleanExtra("single", false);
		if (single) {
			mEtitText.setSingleLine(true);
			mEtitText.setHint("");
			mTvNumer.setVisibility(View.GONE);
		} else {
			mTextNumer = getIntent().getIntExtra(AppTag.TAG_NUMER, 500);
			mTvNumer.setText(mEtitText.getText().toString().length() + "/"
					+ mTextNumer);
			mEtitText.setMaxLines(mTextNumer);
			mEtitText.setHint("请输入"+mTextNumer + "字以内");
			// 字数变化监听
			TextWatcher watcher = new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					mTvNumer.setText(s.length() + "/" + mTextNumer);
					isBack = true;
					temp=s;
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					  selectionStart = mEtitText.getSelectionStart();
                      selectionEnd = mEtitText.getSelectionEnd();
					  if (temp.length() > mTextNumer) {
						  s.delete(selectionStart - 1, selectionEnd);
                          int tempSelection = selectionEnd;
                          mEtitText.setText(s);
                          mEtitText.setSelection(tempSelection); 
					  }

				}
			};
			mEtitText.addTextChangedListener(watcher);
		}
		mEtitText.setText(getIntent().getStringExtra(AppTag.TAG_TEXT));
		//checkToken();
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
	boolean isBack = false;// 是否要退出

//	// 验证用户是否有效
//	private void checkToken() {
//		if (SJQApp.user == null)
//			return;
//		HttpUtils http = new HttpUtils();
//		String url = ApiUrl.CHECK_TOKEN;
//		RequestParams param = getParams(url);
//		param.addBodyParameter("UserGUID", SJQApp.user.UserGUID);
//		param.addBodyParameter("Token", SJQApp.user.Token);
//		param.addBodyParameter("r", System.currentTimeMillis() + "");
//		http.send(HttpMethod.POST, ApiUrl.API + url, param,
//				new RequestCallBack<String>() {
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> arg0) {
//						Bean bean = CommonUtils.getDomain(arg0, Bean.class);
//						if (bean != null) {
//							if (bean.success) {
//							} else {
//								toastShow("您需要重新登录");
//								if (SetDataActivity.this == null)
//									return;
//								((SJQApp) getApplication()).exitAccount();
//								startActivity(new Intent(SetDataActivity.this,
//										LoginActivity.class));
//							}
//						}
//
//					}
//				});
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			doBack();

			break;

		case R.id.tv_title2:
			doSave();
			break;
		}
	}

	// 返回
	private void doBack() {
		if (isBack) {
			new MyAlertDialog(this).setTitle("提示").setContent("是否保存？")
					.setBtnOk("保存").setBtnCancel("取消").setCancelable(true)
					.setBtnOnListener(new DialogOnListener() {

						@Override
						public void onClickListener(boolean isOk) {
							if (isOk) {
								doSave();
							}else{
								finish();
							}

						}
					}).show();
		} else {
			finish();
		}

	}

	private void doSave() {
		hideInput();
		Intent data = new Intent();
		data.putExtra(AppTag.TAG_TEXT, mEtitText.getText().toString());
		setResult(RESULT_OK, data);
		dialog.dismiss();
		finish();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			doBack(); // 调用双击退出函数
		}
		return false;
	}

}
