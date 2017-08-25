package com.baolinetworktechnology.shejiquan.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.domain.GongSizsBean;
import com.baolinetworktechnology.shejiquan.domain.credentialList;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ZhengShuActivity extends BaseActivity implements OnClickListener{
	String GUID;
	private BitmapUtils mImageUtil;
	ArrayList<credentialList> credentialList=new ArrayList<credentialList>();
	private ZhuenshuAdater adater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zheng_shu);
		findViewById(R.id.back).setOnClickListener(this);
		GUID=getIntent().getStringExtra("GUID");
		
		mImageUtil = new BitmapUtils(this);
		//加载图片类
		mImageUtil.configDefaultLoadingImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		
		ListView MyZhenshu=(ListView) findViewById(R.id.MyZhenshu); 
		adater = new ZhuenshuAdater();
		MyZhenshu.setAdapter(adater);
		loadata();
		MyZhenshu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(ZhengShuActivity.this,
						PhotoActivity.class);
				intent.putExtra("position", position);
				ArrayList<String> value = new ArrayList<String>();
				for (int i = 0; i < credentialList.size(); i++) {
					value.add(credentialList.get(i).getCertificateUrl());
				}
				intent.putStringArrayListExtra(
						PhotoActivity.IMAGE_URLS, value);
				intent.putExtra(PhotoActivity.INDEX, position);
				startActivity(intent);
				
			}
		});
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
	private void loadata() {
		String url = ApiUrl.BUSINESSCREDENTIAL +GUID+"&IsRefresh=true"; 
//		+ "&R=+ System.currentTimeMillis();
		dialog.show();
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow(message);
				dialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject json;
				GongSizsBean bean = null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, GongSizsBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (bean.getCredentialList() == null || bean.getCredentialList().size() == 0) {
					 toastShow("抱歉，没有数据");
				}
				if (bean.getCredentialList() != null) {
					credentialList.addAll(bean.getCredentialList());
					adater.notifyDataSetChanged();
				   }
				dialog.dismiss();
			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);
				
	}

	@Override
	public void onClick(View v) {
	    switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}
	private class ZhuenshuAdater extends BaseAdapter{

		@Override
		public int getCount() {
			return credentialList.size();
		}
		@Override
		public Object getItem(int position) {
			if (position < 0 || position >= credentialList.size())
				return null;
			return credentialList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder vh = null;
			if (convertView == null) {
				convertView = View.inflate(parent.getContext(),
						R.layout.zhuenshu_layout, null);
				vh = new Holder();
				vh.zhuenshu_im=(ImageView) convertView.findViewById(R.id.zhuenshu_im);

				convertView.setTag(vh);
			} else {
				vh = (Holder) convertView.getTag();
			}
			credentialList credentiallist=credentialList.get(position);
			mImageUtil.display(vh.zhuenshu_im,credentiallist.getCertificateUrl());
			return convertView;
		}
		
	}
	class Holder {
		ImageView zhuenshu_im;

	}	

}
