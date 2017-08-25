package com.baolinetworktechnology.shejiquan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;

/**
 * Fragment 的基类
 * 
 * @author JiSheng.Guo
 * 
 */
public class BaseFragment extends Fragment implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(5 * 1000);
		return httpUtil;

	}

	public HttpUtils getHttpUtils(int CacheExpiry) {
		HttpUtils httpUtil = new HttpUtils(6 * 1000);
		httpUtil.configDefaultHttpCacheExpiry(CacheExpiry);
		return httpUtil;

	}

	void finish() {
		if (getActivity() != null)
			getActivity().finish();
	}

	public RequestParams getParams(String url) {
		RequestParams params = new RequestParams();
		if (SJQApp.user == null) {
			params.setHeader("Token", null);
		} else {
			params.setHeader("Token", SJQApp.user.Token);
		}
		params.setHeader("Version", ApiUrl.VERSION);
		params.setHeader("Client", AppTag.CLIENT);
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		params.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
		return params;

	}

	void setTitle(View view, String string) {
		TextView title = (TextView) view.findViewById(R.id.tv_title);
		title.setText(string);
		view.findViewById(R.id.back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			getActivity().finish();
			break;

		default:
			break;
		}
	}

	public void toastShow(String text) {
		if (getActivity() != null)
			Toast.makeText(getActivity().getApplicationContext(), text,
					Toast.LENGTH_SHORT).show();
	}

	public void toastShow(int StringID) {
		if (getActivity() != null)
			Toast.makeText(getActivity().getApplicationContext(), StringID,
					Toast.LENGTH_SHORT).show();
	}
}
