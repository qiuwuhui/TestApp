package com.baolinetworktechnology.shejiquan.activity;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.fragment.MessageFragment;
import com.baolinetworktechnology.shejiquan.fragment.MessageSystemFragment;
import com.baolinetworktechnology.shejiquan.interfaces.IMeaseFragment;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;

public class MessageActivity1 extends FragmentActivity implements
		IMeaseFragment, OnClickListener{
	String MEASGE_TAG = "MessageFragment";
	public static String iS_OWNER = "isOwner";
//	private MessageFragment mesageFrag;
	MessageSystemFragment systemFrag;

	View view;
	boolean isBack = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		if (SJQApp.user == null) {
			startActivity(new Intent(this, MainActivity.class));
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return;
		}
		//魅族
		CommonUtils.setMeizuStatusBarDarkIcon(MessageActivity1.this, true);
		//小米
		CommonUtils.setMiuiStatusBarDarkMode(MessageActivity1.this, true);
		view = findViewById(R.id.frameLayout);
//		mesageFrag = new MessageFragment();
//		Bundle args = new Bundle();
//		args.putBoolean(iS_OWNER, getIntent().getBooleanExtra(iS_OWNER, false));
//		mesageFrag.setArguments(args);

		systemFrag = new MessageSystemFragment();

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();


		transaction.add(R.id.frameLayout, systemFrag);
		// transaction.add(R.id.frameLayout, commentFrag);
//		transaction.add(R.id.frameLayout, mesageFrag);
		transaction.show(systemFrag);

		transaction.commit();
		findViewById(R.id.back).setOnClickListener(this);

	}

	@Override
	public void click(View v) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		switch (v.getId()) {
//			case R.id.itemCommentMeage:
//				transaction.hide(mesageFrag);
//				transaction.hide(systemFrag);
//				break;
//			case R.id.itemSystemMeage:
//				transaction.show(systemFrag);
//				transaction.hide(mesageFrag);
//				break;
			default:
				break;
		}
		isBack = false;
		transaction.commit();
		view.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.slide_right_in));
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		doBack();
	}



	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);// 必须要调用这句
		SJQApp.isRrefresh = true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back:
				doBack();
				break;

			default:
				break;
		}

	}

	private void doBack() {
		if (isBack) {
			finish();
		} else {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			transaction.show(systemFrag);
			// transaction.hide(commentFrag);
//			transaction.hide(mesageFrag);

			transaction.commit();
			view.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.slide_left_in));
			isBack = true;
		}

	}
}
