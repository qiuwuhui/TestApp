package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.view.ListViewRecord;

/**
 * 访客记录
 * 
 * @author JiSheng.Guo
 * 
 */
public class RecordActivity extends BaseActivity implements
		OnCheckedChangeListener {
	public static String IS_Me = "isMe";// 是否自己的访客记录
	public static String ALL_RECORD = "ALL_RECORD";// 所有的访问量
	private TextView mTvAllNumber, mTvDayNumber;//所有的访问量 当日的访问量
	private ListViewRecord mListViewRecord;//访客ListView

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		initView();
		setTitle("访客记录");
		if (getIntent().getBooleanExtra(IS_Me, true)) {
			if (SJQApp.user != null)
				mListViewRecord.setUserGuid(SJQApp.user.guid);
		} else {
			mListViewRecord.setUserGuid(getIntent().getStringExtra(AppTag.TAG_GUID));
			findViewById(R.id.nav).setVisibility(View.GONE);
		}

		mListViewRecord.setTextView(mTvDayNumber, mTvAllNumber);

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
		mListViewRecord = (ListViewRecord) findViewById(R.id.myListView);
		mTvAllNumber = (TextView) findViewById(R.id.tvAll);
		mTvDayNumber = (TextView) findViewById(R.id.tvDay);
		mTvAllNumber.setText(getIntent().getStringExtra(ALL_RECORD));
		RadioButton rb1 = (RadioButton) findViewById(R.id.rb1);
		RadioButton rb2 = (RadioButton) findViewById(R.id.rb2);
		rb1.setOnCheckedChangeListener(this);
		rb2.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.rb1:// 谁看了我
			mListViewRecord.setUrlType(ListViewRecord.WHO_AT_LOOK);
			findViewById(R.id.view_div2).setVisibility(View.VISIBLE);
			findViewById(R.id.view_div1).setVisibility(View.INVISIBLE);
			break;

		default://我看了谁
			mListViewRecord.setUrlType(ListViewRecord.LOOK_AT_WHO);
			//切换显示
			findViewById(R.id.view_div1).setVisibility(View.VISIBLE);
			findViewById(R.id.view_div2).setVisibility(View.INVISIBLE);

			break;
		}

	}

}
