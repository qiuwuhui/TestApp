package com.baolinetworktechnology.shejiquan.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.SettingActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.EnumDientity;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.baolinetworktechnology.shejiquan.view.MineView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

/**
 * 首页-我的
 * 
 * @author JiSheng.Guo
 * 
 */
public class MainMyFragment extends BaseMainFragment {
	private boolean mIsPrepared;
	private boolean mIsUpate = false;
	private MineView view;
	ShareUtils mShareUtils;
	MyFragmentFace mMyFragmentFace = new MyFragmentFace() {

		@Override
		public void doSing(TextView tv) {
			sing(tv);

		}

		@Override
		public void setting() {
			Intent intent = new Intent();
			intent.setClass(getActivity(), SettingActivity.class);
			intent.putExtra("IS_UPATE", mIsUpate);
			startActivityForResult(intent, 501);

		}
		//分享动作
		@Override
		public void doShare() {
			if (mShareUtils == null) {
				mShareUtils = new ShareUtils(getActivity());
	      }
	      mShareUtils
					.setShareUrl(ApiUrl.SHARE_SHEJIQUAN)
					.setImageUrl(R.drawable.ic_launcher)
					.setShareTitle("设计圈")
					.addToSocialSDK()
					.setShareContent2(
							"让设计更有价值，我正在使用#设计圈#app，让我的装修变成一种享受，推荐给大家"
									+ ApiUrl.SHARE_SHEJIQUAN).doShare();

			
		}
	};

	public interface MyFragmentFace {
		void doSing(TextView tv);

		void setting();
		void doShare();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = new MineView(getActivity(), mMyFragmentFace);
		lazyLoad();
		// checkUpdate();

		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainMyFragment");
		if (SJQApp.user != null) {
			if (SJQApp.getUserEnumDientity() == EnumDientity.USER_DESIGNER) { // 1业主
				// 2设计师
				int di = view.getDienId();                                    // 3装修公司 
				view.setDidentity(EnumDientity.USER_DESIGNER);
				view.setData();
				mIsPrepared = true;
			}else if(SJQApp.getUserEnumDientity() == EnumDientity.USER_GONGSI){
				int di = view.getDienId();                                    // 3装修公司 
				view.setDidentity(EnumDientity.USER_GONGSI);
				view.setData();
				mIsPrepared = true;				
			}else {
				view.setDidentity(EnumDientity.USER_OWNER);
				mIsPrepared = true;
				view.setData();
			}

		} else {
			view.setDidentity(EnumDientity.USER_OWNER);
			mIsPrepared = true;

		}
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainMyFragment");
	}

	@Override
	protected void onVisible() {
		// MobclickAgent.onPageStart("MyFragment"); // 统计页面
		super.onVisible();
	}

	@Override
	protected void onInvisible() {
		// MobclickAgent.onPageEnd("MyFragment");
		super.onInvisible();
	}

	@Override
	protected void lazyLoad() {
		if (!mIsPrepared || !mIsVisible) {
			return;
		}
		view.setData();

	}

	void showTips() {
		// if (mIsUpate) {
		// if (mRedTips.getVisibility() != View.VISIBLE)
		// mRedTips.setVisibility(View.VISIBLE);
		// } else {
		// if (mRedTips.getVisibility() != View.GONE)
		// mRedTips.setVisibility(View.GONE);
		// }
	}

	// 签到
	private void sing(final TextView mTvSing) {
		mTvSing.setEnabled(false);

		String url = ApiUrl.SIGNIN;
		RequestParams params = getParams(url);
		params.addBodyParameter("UserGuid", SJQApp.user.guid);
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> n) {
				Bean bean = CommonUtils.getDomain(n, Bean.class);
				String Score = CommonUtils.getString(n.result, "TotalScore");
				int score = 0;
				try {
					score = Integer.parseInt(Score);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (bean != null && bean.success) {
					toastShow("签到成功");
					mTvSing.setText(" 已签到");
					if (SJQApp.user != null) {
//						SJQApp.user.IsSignIn = 1;
//						SJQApp.user.TotalScore = score;
						view.setData();
					}
				} else {
					toastShow("签到失败");
					mTvSing.setEnabled(true);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				toastShow("签到失败");
				mTvSing.setEnabled(true);

			}
		};
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 501) {
			if (resultCode == 502) {
				getActivity().finish();
			}
		}
	}

	public void setTabClick() {
		if (view != null)
			view.setTabClick();
	}
	public void setReadMag(){
		if (view != null){
			view.setReadMag();
		}
	}
}
