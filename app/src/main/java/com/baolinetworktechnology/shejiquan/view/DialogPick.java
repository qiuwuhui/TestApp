package com.baolinetworktechnology.shejiquan.view;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;

/**
 * 单选择对话框
 * 
 * @author JiSheng.Guo
 * 
 */
public class DialogPick implements OnClickListener {
	Context mContext;
	ScrollerNumberPicker mNumberPicker;
	TextView mTitle;
	AlertDialog dialo2;
	ArrayList<String> mData;
	OnPicker mOnPicker;

	public interface OnPicker {

		void onPicker(String item, int index);
	}

	public DialogPick(Context context) {
		mContext = context;
		initView();
	}

	private void initView() {
		dialo2 = new AlertDialog.Builder(getContext()).create();

		View view = View.inflate(mContext, R.layout.window_select_single, null);

		mNumberPicker = (ScrollerNumberPicker) view.findViewById(R.id.picker);
		mTitle = (TextView) view.findViewById(R.id.tvTitle);

		view.findViewById(R.id.btnCancel).setOnClickListener(this);
		view.findViewById(R.id.btnOk).setOnClickListener(this);

		dialo2.setView(view);
	}

	public DialogPick setPickerListener(OnPicker onPickerListener) {
		mOnPicker = onPickerListener;
		return this;
	}

	/**
	 * 设置数据源
	 * 
	 * @param data
	 * @return
	 */
	public DialogPick setData(ArrayList<String> data) {
		mNumberPicker.setData(data);
		return this;

	}

	/**
	 * 设置数据源
	 * 
	 * @param item
	 * @return
	 */
	public DialogPick setData(String... item) {

		if (mData == null) {
			mData = new ArrayList<String>();
		}

		for (String s : item) {
			mData.add(s);
		}

		mNumberPicker.setData(mData);
		mNumberPicker.setDefault(0);
		return this;

	}

	public DialogPick clearData() {
		if (mData != null) {
			mData.clear();
		}

		return this;
	}

	/**
	 * 设置默认显示选项
	 * 
	 * @param index
	 * @return
	 */
	public DialogPick setDefault(int index) {
		mNumberPicker.setDefault(0);
		return this;
	}

	/**
	 * 设置对话框标题
	 * 
	 * @param title
	 * @return
	 */
	public DialogPick setTitle(String title) {
		mTitle.setText(title);
		return this;
	}

	/**
	 * 显示对话框
	 */
	public void show() {
		dialo2.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOk:
			if (mOnPicker != null)
				mOnPicker.onPicker(mNumberPicker.getSelectedText(),
						mNumberPicker.getSelected());
			break;
		}
		dialo2.dismiss();
	}

	private Context getContext() {
		return mContext;
	}
}
