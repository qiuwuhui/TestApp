package com.baolinetworktechnology.shejiquan.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;

/**
 * 自定义对话框
 * 
 * @author JiSheng.Guo
 * 
 */
public class MyDialog extends AlertDialog {
	TextView mProgress, mDialogText;
	AlertDialog dialog;
	public static String IS_FULLSCREEN = "IS_FULLSCREEN";

	public MyDialog(Context context) {
		super(context, R.style.MyDialogStyle);
		// super(context, R.style.MyDialogStyle);
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);
	}

	public MyDialog(Context context, boolean b) {

		this(context, R.style.MyDialogStyleFullscreen);
		System.out.println("-->>MyDialog" + b);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_dialog_loading);
		mProgress = (TextView) findViewById(R.id.progress);
		mDialogText = (TextView) findViewById(R.id.dialogText);
	}

	public void setProgress(String pro) {
		if (mProgress.getVisibility() != View.VISIBLE) {
			mProgress.setVisibility(View.VISIBLE);
		}
		mProgress.setText(pro);
	}

	public void show(String text) {
		if (mDialogText != null) {
			mDialogText.setText(text);
			mDialogText.setVisibility(View.VISIBLE);

		}
		super.show();
	}

	public void show() {
		if (mDialogText != null) {
			if (mDialogText.getVisibility() == View.VISIBLE) {
				mDialogText.setVisibility(View.GONE);
			}
		}
		super.show();
	}

}
