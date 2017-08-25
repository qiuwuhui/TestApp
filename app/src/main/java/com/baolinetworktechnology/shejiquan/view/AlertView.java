package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.guojisheng.koyview.MyEditText;

public class AlertView extends FrameLayout implements OnClickListener {
	TextView tv;
	int tag = 0;

	public AlertView(Context context, TextView tv, int tag) {
		super(context);
		this.tv = tv;
		initUI();
	}

	MyEditText et;

	private void initUI() {
		View view;
		switch (tag) {
		case 0:
			view = View.inflate(getContext(), R.layout.view_et_diao, null);
			et = (MyEditText) view.findViewById(R.id.myEdit);
			view.findViewById(R.id.ok).setOnClickListener(this);
			view.findViewById(R.id.cancel).setOnClickListener(this);
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok:
			if (tv != null) {
				tv.setText(et.getText().toString());
			}
			break;
		case R.id.cancel:
			
			break;
		}

	}

}
