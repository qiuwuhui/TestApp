package com.baolinetworktechnology.shejiquan.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;

public abstract class BaseMainFragment extends Fragment implements
		OnClickListener {

	protected boolean mIsVisible;

	/**
	 * 在这里实现Fragment数据的缓加载.
	 * 
	 * @param isVisibleToUser
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			mIsVisible = true;
			onVisible();
		} else {
			if (mIsVisible) {
				mIsVisible = false;
				onInvisible();

			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!mIsVisible && getUserVisibleHint()) {
			mIsVisible = true;
			onVisible();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mIsVisible) {
			mIsVisible = false;
			onInvisible();

		}
	}

	// fragment被设置为可见时调用
	protected void onVisible() {
		lazyLoad();
	}

	// fragment被设置为不可见时调用
	protected void onInvisible() {
	}

	protected abstract void lazyLoad();

	public HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(5 * 1000);
		// httpUtil.configDefaultHttpCacheExpiry(1000 * 2);
		return httpUtil;

	}

	public HttpUtils getHttpUtils(int CacheExpiry) {
		HttpUtils httpUtil = new HttpUtils(6 * 1000);
		httpUtil.configDefaultHttpCacheExpiry(CacheExpiry);
		return httpUtil;

	}

	public RequestParams getParams(String url) {

		RequestParams params = new RequestParams();
		if (SJQApp.user != null) {
			params.setHeader("Token", SJQApp.user.Token);
		} else {
			params.setHeader("Token", null);
		}
		params.setHeader("Version", ApiUrl.VERSION);
		params.setHeader("Client", AppTag.CLIENT);
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		params.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
		return params;

	}

	@Override
	public void onClick(View v) {

	}

	Toast toast;

	void toastShow(String text) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(getActivity().getApplicationContext(), text,
				Toast.LENGTH_SHORT);
		toast.show();
	}
}
