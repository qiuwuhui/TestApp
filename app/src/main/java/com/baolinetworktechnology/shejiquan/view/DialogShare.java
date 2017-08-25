package com.baolinetworktechnology.shejiquan.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;

public class DialogShare extends Dialog implements
		android.view.View.OnClickListener {
	ShareUtils mShareUtils;

	public DialogShare(Context context, ShareUtils mShareUtils) {
		super(context, R.style.ActionSheetDialogStyle);
		this.mShareUtils = mShareUtils;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(getContext(), R.layout.dialog_share, null);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setContentView(view, params);
		Window win = getWindow();

		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		win.setAttributes(lp);
		win.setGravity(Gravity.BOTTOM);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		view.findViewById(R.id.tvShareqq).setOnClickListener(this);
		view.findViewById(R.id.tvShareWx).setOnClickListener(this);
		view.findViewById(R.id.tvShareWxc).setOnClickListener(this);
		view.findViewById(R.id.tvShareWxs).setOnClickListener(this);
		view.findViewById(R.id.tvShareqz).setOnClickListener(this);
		view.findViewById(R.id.tvSharexl).setOnClickListener(this);
		view.findViewById(R.id.dismiss).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvShareqq:
			mShareUtils.doShareQQ();
			break;
		case R.id.tvShareWx:
			mShareUtils.doShareWeChat();
			break;
		case R.id.tvShareWxc:
			mShareUtils.doShareWeChatCircle();
			break;
		case R.id.tvShareqz:
			mShareUtils.doShareQQZ();
			break;
		case R.id.tvShareWxs:
			mShareUtils.doShareWeiXinS();
			break;
		case R.id.tvSharexl:
			mShareUtils.doShareSina();
			break;
		case R.id.dismiss:
			mShareUtils.doDismiss();
			break;
		default:
			break;
		}
		dismiss();

	}

}
