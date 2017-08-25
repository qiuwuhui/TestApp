package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.BoundActivity;
import com.baolinetworktechnology.shejiquan.activity.CallActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetail;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;

public class ContactDialog implements android.view.View.OnClickListener {
	private TextView mTvPhone, mTvQQ;
	public DesignerDetail mUserData;
	private Context mContext;
	private PopupWindow mPopupWindow;
	private int popupWidth;
	private int popupHeight;

	public ContactDialog(Context context) {
		mContext = context;
		initView(context);

	}

	public ContactDialog(Context context, DesignerDetail mUserData2) {
		// TODO Auto-generated constructor stub
	}

	private void initView(Context context) {
		View contentView = View.inflate(context, R.layout.dialog_contact, null);
		mTvPhone = (TextView) contentView.findViewById(R.id.tvPhone);
		mTvQQ = (TextView) contentView.findViewById(R.id.tvQQ);
		mTvPhone.setOnClickListener(this);
		mTvQQ.setOnClickListener(this);

		mPopupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(
				R.color.transparent));
		mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style2);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		contentView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		popupWidth = contentView.getMeasuredWidth();
		popupHeight = contentView.getMeasuredHeight();
	}

	public void setData(String mobile, String qq, String mUserGuid) {
		if (mUserData == null) {
			mUserData = new DesignerDetail();

		}
		mUserData.Mobile = mobile;
		mUserData.QQ = qq;
		mUserData.GUID = mUserGuid;
		setData(mUserData);
	}

	public void setData(DesignerDetail userData) {
		if (userData != null) {
			this.mUserData = userData;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tvPhone:
			to2Call();
			break;

		case R.id.tvQQ:
			try {
				String url = "mqqwpa://im/chat?chat_type=wpa&uin="
						+ mUserData.QQ;
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(url)));
			} catch (Exception e) {

			}

			break;
		}

		mPopupWindow.dismiss();
	}

	public void show() {
		// TODO Auto-generated method stub

	}

	public PopupWindow showAsDropDown(View v) {
		if (mUserData != null) {
			if (!TextUtils.isEmpty(mUserData.QQ)) {
				if (!mPopupWindow.isShowing())
					mPopupWindow.showAsDropDown(v);
				return mPopupWindow;
			}

			to2Call();
			return mPopupWindow;

		}
		if (!mPopupWindow.isShowing())
			mPopupWindow.showAsDropDown(v);
		return mPopupWindow;

	}

	// 跳转到拨号
	private void to2Call() {
		if (mUserData == null)
			return;
		if (TextUtils.isEmpty(SJQApp.user.Mobile)) {
			new MyAlertDialog(mContext).setTitle("您的帐号尚未绑定手机号，暂无法电话联系 ")
					.setContent("是否进行手机号绑定？").setBtnOk("前往绑定")
					.setBtnCancel("暂不绑定")
					.setBtnOnListener(new DialogOnListener() {

						@Override
						public void onClickListener(boolean isOk) {
							if (isOk) {
								Intent intent = new Intent(mContext,
										BoundActivity.class);
								mContext.startActivity(intent);
							}

						}
					}).show();
			return;
		}

		new MyAlertDialog(mContext)
				.setTitle("请确认您的联系电话为 " + SJQApp.user.getMobile())
				.setContent("我们将使用该电话为您接通设计师").setBtnOk("呼叫")
				.setBtnCancel("暂不呼叫").setBtnOnListener(new DialogOnListener() {

					@Override
					public void onClickListener(boolean isOk) {
						if (isOk) {
							Intent intent = new Intent(mContext,
									CallActivity.class);
							intent.putExtra(AppTag.TAG_GUID, mUserData.GUID);
							intent.putExtra(AppTag.TAG_JSON,
									mUserData.toString());
							intent.putExtra(AppTag.TAG_PHONE,
									SJQApp.user.getMobile());
							mContext.startActivity(intent);
						}

					}
				}).show();

	}

	public boolean isShowing() {
		return mPopupWindow.isShowing();
	}

	public void showAsTop(View v) {
		if (mUserData != null) {
			if (TextUtils.isEmpty(mUserData.QQ)) {// QQ为空
				to2Call();// 直接进行拨号
				return;
			}
		}

		if (!mPopupWindow.isShowing()) {
			int[] location = new int[2];
			v.getLocationOnScreen(location);
			mPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
					(location[0] + v.getWidth() / 2) - popupWidth / 2,
					location[1] - popupHeight);
		}
	}

}
