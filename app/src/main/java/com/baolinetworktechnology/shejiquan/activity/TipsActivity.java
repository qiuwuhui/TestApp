package com.baolinetworktechnology.shejiquan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.baolinetworktechnology.shejiquan.R;

public class TipsActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tips);
		findViewById(R.id.root).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		finish();

	}

}
