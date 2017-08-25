package com.baolinetworktechnology.shejiquan.view;

import com.baolinetworktechnology.shejiquan.R;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyAlertDialog implements OnClickListener {
	AlertDialog alertDialog;
	TextView tvTitle, tvText;
	Button btnOk, btnCancel;
	DialogOnListener dialogOnListener;

	public interface DialogOnListener {
		void onClickListener(boolean isOk);
	}

	public MyAlertDialog(Context context) {
		View view = View.inflate(context, R.layout.dialog_bind, null);
		tvTitle = (TextView) view.findViewById(R.id.dialog_title);
		tvText = (TextView) view.findViewById(R.id.tvText);
		btnOk = (Button) view.findViewById(R.id.dialog_ok);
		btnCancel = (Button) view.findViewById(R.id.dialog_cancel);
		tvTitle.setVisibility(View.GONE);
		tvText.setVisibility(View.GONE);
		btnOk.setVisibility(View.GONE);
		btnCancel.setVisibility(View.GONE);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		alertDialog = new AlertDialog.Builder(context).setView(view)

		.create();
	}

	public MyAlertDialog setTitle(String title) {
		if (title == null)
			return this;
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(title);
		return this;
	}

	public MyAlertDialog setTitle(Spanned title) {
		if (title == null)
			return this;
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(title);
		return this;
	}

	public MyAlertDialog setContent(String content) {
		if (TextUtils.isEmpty(content))
			return this;
		tvText.setVisibility(View.VISIBLE);
		tvText.setText(content);
		return this;
	}

	public MyAlertDialog setBtnOk(String des) {
		btnOk.setVisibility(View.VISIBLE);
		btnOk.setText(des);

		return this;
	}

	public MyAlertDialog setBtnCancel(String des) {
		btnCancel.setVisibility(View.VISIBLE);
		btnCancel.setText(des);
		alertDialog.setCancelable(false);
		return this;
	}

	public MyAlertDialog setCancelable(boolean isCance) {
		alertDialog.setCancelable(isCance);
		return this;
	}

	public MyAlertDialog setBtnOnListener(DialogOnListener dialogOnListener) {
		this.dialogOnListener = dialogOnListener;
		return this;

	}

	public MyAlertDialog show() {
		alertDialog.show();
		return this;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_ok:
			if (dialogOnListener != null)
				dialogOnListener.onClickListener(true);
			break;
		case R.id.dialog_cancel:
			if (dialogOnListener != null)
				dialogOnListener.onClickListener(false);
			break;
		default:
			break;
		}
		alertDialog.cancel();
	}
}
