package com.baolinetworktechnology.shejiquan.activity;
import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.domain.GongsigsBean;
import com.baolinetworktechnology.shejiquan.domain.GongsijjBean;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class GongsiActivity extends BaseActivity implements OnClickListener{
	String businessID;
	String GUID;
	MyDialog dialog;
	//公司简介TextView
	private TextView introduce,proportion,initialDesign,thoroughDesign,initialBudget,
	thoroughBudget,material,contractNorm,characteristicService,areaRange,serviceRange
	,priceRange,AdeptStyle,Finalservice;
	//公司工商注册信息TextView
	private TextView enterpriseName,enterpriseType,registerAddress,registerFund,businessTime,
	establishTime,registerAuthority,operateRang,examineTime,registerNum,legarPerson;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = new MyDialog(GongsiActivity.this);
		businessID=getIntent().getStringExtra("businessID");
		GUID =getIntent().getStringExtra("GUID");
		String strname=getIntent().getStringExtra("name");
		if(strname.equals("简介")){
			setContentView(R.layout.activity_gongsi);
			invinjj();
			loadata();
		}else{
			setContentView(R.layout.activity_gongsi_gs);
			invinGs();
			loadataGS();
		}
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

	private void invinjj() {
		findViewById(R.id.back).setOnClickListener(this);
		introduce = (TextView) findViewById(R.id.introduce);
		proportion =(TextView) findViewById(R.id.proportion);
		initialDesign =(TextView) findViewById(R.id.initialDesign);
		thoroughDesign =(TextView) findViewById(R.id.thoroughDesign);
		initialBudget =(TextView) findViewById(R.id.initialBudget);
		thoroughBudget =(TextView) findViewById(R.id.thoroughBudget);
		material =(TextView) findViewById(R.id.material);
		contractNorm =(TextView) findViewById(R.id.contractNorm);
		characteristicService =(TextView) findViewById(R.id.characteristicService);
		areaRange =(TextView) findViewById(R.id.areaRange);
		serviceRange =(TextView) findViewById(R.id.serviceRange);
		priceRange =(TextView) findViewById(R.id.priceRange);
		AdeptStyle =(TextView) findViewById(R.id.AdeptStyle);
		Finalservice =(TextView) findViewById(R.id.Finalservice);
	}
	private void invinGs() {
	   enterpriseName = (TextView) findViewById(R.id.enterpriseName);
	   enterpriseType = (TextView) findViewById(R.id.enterpriseType);
	   registerAddress = (TextView) findViewById(R.id.registerAddress);
	   registerFund = (TextView) findViewById(R.id.registerFund);
	   businessTime = (TextView) findViewById(R.id.businessTime);
	   establishTime = (TextView) findViewById(R.id.establishTime);
	   registerAuthority = (TextView) findViewById(R.id.registerAuthority);
	   operateRang = (TextView) findViewById(R.id.operateRang);
	   examineTime = (TextView) findViewById(R.id.examineTime);
	   registerNum = (TextView) findViewById(R.id.registerNum);
	   legarPerson = (TextView) findViewById(R.id.legarPerson);   
	}
	private void setinview(GongsijjBean bean) {
		if(!TextUtils.isEmpty(bean.getIntroduce()))
			introduce.setText(bean.getIntroduce());	
		
		if(!TextUtils.isEmpty(bean.getProportion()))
		proportion.setText(bean.getProportion());

		if(!TextUtils.isEmpty(bean.getInitialDesign()))
		initialDesign.setText(bean.getInitialDesign());

		if(!TextUtils.isEmpty(bean.getThoroughDesign()))
		thoroughDesign.setText(bean.getThoroughDesign());

		if(!TextUtils.isEmpty(bean.getInitialBudget()))
		initialBudget.setText(bean.getInitialBudget());
		
		if(!TextUtils.isEmpty(bean.getThoroughBudget()))
		thoroughBudget.setText(bean.getThoroughBudget());
		
		if(!TextUtils.isEmpty(bean.getMaterial()))
		material.setText(bean.getMaterial());
		
		if(!TextUtils.isEmpty(bean.getContractNorm()))
		contractNorm.setText(bean.getContractNorm());
		
		if(!TextUtils.isEmpty(bean.getCharacteristicService()))
		characteristicService.setText(bean.getCharacteristicService());
		
		if(!TextUtils.isEmpty(bean.getAreaRange()))
		areaRange.setText(bean.getAreaRange());
		
		if(!TextUtils.isEmpty(bean.getServiceRange()))
		serviceRange.setText(bean.getServiceRange());
		
		if(!TextUtils.isEmpty(bean.getPriceRange()))
		priceRange.setText(bean.getPriceRange());
		
		if(!TextUtils.isEmpty(bean.getAdeptStyle()))
		AdeptStyle.setText(bean.getAdeptStyle());
		
		if(!TextUtils.isEmpty(bean.getFinalservice()))
		Finalservice.setText(bean.getFinalservice());
	}
	private void setinviewGS(GongsigsBean bean){
		if(!TextUtils.isEmpty(bean.getEnterpriseName()))
		enterpriseName.setText(bean.getEnterpriseName());
		
		if(!TextUtils.isEmpty(bean.getEnterpriseType()))
		enterpriseType .setText(bean.getEnterpriseType());
		
		if(!TextUtils.isEmpty(bean.getRegisterAddress()))
		registerAddress.setText(bean.getRegisterAddress());
		
		if(!TextUtils.isEmpty(bean.getRegisterFund()))
		registerFund.setText(bean.getRegisterFund());
		
		if(!TextUtils.isEmpty(bean.getBusinessTime()))
		businessTime.setText(bean.getBusinessTime());
		
		if(!TextUtils.isEmpty(bean.getEstablishTime()))
		establishTime.setText(bean.getEstablishTime());
		
		if(!TextUtils.isEmpty(bean.getRegisterAuthority()))
		registerAuthority.setText(bean.getRegisterAuthority());
		
		if(!TextUtils.isEmpty(bean.getOperateRang()))
		operateRang.setText(bean.getOperateRang());
		
		if(!TextUtils.isEmpty(bean.getExamineTime()))
		examineTime.setText(bean.getExamineTime());
		
		if(!TextUtils.isEmpty(bean.getRegisterNum()))
		registerNum.setText(bean.getRegisterNum());
		
		if(!TextUtils.isEmpty(bean.getLegarPerson()))
		legarPerson.setText(bean.getLegarPerson());
	}
	private void loadata() {
		String url = ApiUrl.BUSINESSINTRO +businessID; 
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
//				GongsijjBean bean= CommonUtils.getDomain(responseInfo,
//						GongsijjBean.class);
				JSONObject json;
				GongsijjBean bean = null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, GongsijjBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (bean != null) {
					setinview(bean);

				   }
				dialog.dismiss();
			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);
		
	}
	private void loadataGS() {
		String url = ApiUrl.BUSINESSREGISTER +GUID; 
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
//				GongsijjBean bean= CommonUtils.getDomain(responseInfo,
//						GongsijjBean.class);
				JSONObject json;
				GongsigsBean bean = null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, GongsigsBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (bean != null) {
					setinviewGS(bean);

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
}
