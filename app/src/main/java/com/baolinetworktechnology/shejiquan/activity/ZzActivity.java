package com.baolinetworktechnology.shejiquan.activity;
import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.GongSirz;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ZzActivity extends BaseActivity implements OnClickListener{
	
	private ImageView gslogo;
	private TextView gsname,GsTime,Gszhuantai;
	private View tixing;
	private BitmapUtils mImageUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zz);		
		mImageUtil = new BitmapUtils(this);
		//加载图片类
		mImageUtil.configDefaultLoadingImage(R.drawable.icon_qysmrz);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.icon_qysmrz);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.kefu).setOnClickListener(this);
		gslogo = (ImageView) findViewById(R.id.Gslogo);
		gsname = (TextView) findViewById(R.id.Gsname);
		GsTime = (TextView) findViewById(R.id.GsTime);
		Gszhuantai = (TextView) findViewById(R.id.Gszhuantai);
		tixing = findViewById(R.id.tixing);
		loadata();
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.kefu:
			requestPermission(new String[]{Manifest.permission.CALL_PHONE}, 0x0001);
			break;

		default:
			break;
		}
		
	}
	private void loadata() {
		String url = ApiUrl.BUSINESSCERTIFICATION+SJQApp.ZhuanxiuData.getID()+"&IsRefresh=true"; 	
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
				GongSirz bean=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, GongSirz.class);
					if(bean!=null){
						senview(bean);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
        			dialog.dismiss();
			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);		
	}
	private void senview(GongSirz bean) {
		mImageUtil.display(gslogo, bean.getLogo());
		gsname.setText(bean.getEnterpriseName());
		if(bean.getCertificationStatus()==3){
			GsTime.setText("认证时间："+bean.getCertTime());
			tixing.setVisibility(View.GONE);
			Gszhuantai.setText("恭喜获得认证");
		}else{
			GsTime.setText("提交时间："+bean.getSubCertificationTime());	
			Gszhuantai.setText("审核中");
		}		
	}
	private void showCall() {
		new MyAlertDialog(this).setTitle(SJQApp.kefuCall)
				.setBtnCancel("取消").setBtnOk("呼叫")
				.setBtnOnListener(new DialogOnListener() {

					@Override
					public void onClickListener(boolean isOk) {
						if (isOk)
							doCall(SJQApp.kefuCall);
					}
				}).show();

	}
	private void doCall(String string) {
		// 调用到编辑框的值
		// 新建一个intent对象，进行调用系统的打电话的方法，然后传递号码过去
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ string));
		// 相应事件
		startActivity(intent);
	}
	@Override
	public void permissionSuccess(int requestCode) {
		super.permissionSuccess(requestCode);
		switch (requestCode) {
			case 0x0001:
				showCall();
				break;
		}
	}
}
