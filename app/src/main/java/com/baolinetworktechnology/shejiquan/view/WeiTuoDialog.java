package com.baolinetworktechnology.shejiquan.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.net.GetCode;
import com.baolinetworktechnology.shejiquan.net.OnCallBack;
import com.lidroid.xutils.exception.HttpException;

public class WeiTuoDialog extends Dialog implements
		android.view.View.OnClickListener {
	EditText mEtPhone, etCode;// etMessage
	String phone;
	private Conde code;
	 OnWeiTuoCallBack mOnWeiTuoCallBack;

	 public interface OnWeiTuoCallBack {
		void onSuccess(String phone,String code);

	}

	public WeiTuoDialog(Context context, String phone,
			OnWeiTuoCallBack onWeiTuoCallBack) {
		super(context, R.style.MyDialogStyle);
		this.phone = phone;
		mOnWeiTuoCallBack = onWeiTuoCallBack;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_weituo);
		findViewById(R.id.shutDown).setOnClickListener(this);
		findViewById(R.id.ok).setOnClickListener(this);

		mEtPhone = (EditText) this.findViewById(R.id.etPhone);
		mEtPhone.setText(phone);
		mEtPhone.setEnabled(false);
		etCode = (EditText) this.findViewById(R.id.etCode);
		code = (Conde) findViewById(R.id.btn_getCode);
		code.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shutDown:
			dismiss();
			break;

		case R.id.ok:
			doOk();// 发布招标
			break;
		case R.id.btn_getCode:
			doGetCode();
			break;
		}

	}

	private void doGetCode() {

		OnCallBack onCallBack = new OnCallBack() {

			@Override
			public void onSuccess(String mesa) {
				Toast.makeText(getContext(), mesa, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNetStart() {
				code.start();

			}

			@Override
			public void onFailure(String mesa) {
				Toast.makeText(getContext(), mesa, Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onError(String json) {
				Toast.makeText(getContext(), "服务器繁忙，请稍候", Toast.LENGTH_SHORT)
						.show();

			}

			@Override
			public void onError(HttpException arg0, String mesa) {
				code.stop();
				Toast.makeText(getContext(), R.string.network_error,
						Toast.LENGTH_SHORT).show();
			}
		};
		new GetCode(onCallBack, phone, GetCode.TEMPLE_SheJiQuanCode);

	}

	private void doOk() {
		String codes = etCode.getText().toString();
		if (codes.length() < 4) {
			etCode.setError("验证码不正确");
			return;
		}

		mOnWeiTuoCallBack.onSuccess(phone,codes);
	}

}
