package com.baolinetworktechnology.shejiquan.activity;

import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.byl.datepicker.citywheelview.AbstractWheelTextAdapter;
import com.byl.datepicker.citywheelview.CityWheelView;
import com.byl.datepicker.citywheelview.OnWheelChangedListener;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView.OnEditorActionListener;

public class SelectActivity extends BaseActivity implements AMapLocationListener,LocationSource
,OnClickListener{
	private OnLocationChangedListener mListener =null;
	private AMapLocationClient mLocationClient=null;
	private AMapLocationClientOption mLocationOption =null;
	String APP_FIRST = "APP_FIRST";
	private TextView weizi;
	private EditText mianji;
	List<City> countries = null;
	City sc, cs;
	boolean isCheckCity = false;
	int cityIndex = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_activity2);
    	 inview();
    	 findCity();	
		 initLoc();			 
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

	private void inview() {		
		TextView tiaoguo=(TextView) findViewById(R.id.tiaoguo2);
		tiaoguo.setOnClickListener(this);
		weizi = (TextView) findViewById(R.id.weizi);
		weizi.setOnClickListener(this);		
		Button btn_go=(Button) findViewById(R.id.btn_go);
		btn_go.setOnClickListener(this);
		mianji = (EditText) findViewById(R.id.mianji);
		mianji.clearFocus();
		String Size=CacheUtils.getStringData(SelectActivity.this,AppTag.TJ_SIZE,null);
		mianji.setText(Size);
		findViewById(R.id.gong_lyout).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.back).setVisibility(View.VISIBLE);
		boolean isFirst1 = CacheUtils.getBooleanData(this, "APP_TIAO", false);
	    if(!isFirst1){
	    	tiaoguo.setVisibility(View.GONE);		
		}
	}
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;		
	}

	@Override
	public void deactivate() {
		mListener = null;		
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null) {
			if (amapLocation.getErrorCode() == 0) {
			// 获取位置信息
			String str=amapLocation.getCity();//城市信息
			if(TextUtils.isEmpty(str)){
				weizi.setText("选择所在省市");				
			}else{
				weizi.setText(str);
				CacheUtils.cacheStringData(SelectActivity.this, "dingweics",str);
			}
			
			}else {
				String errText = "failed to locate," + amapLocation.getErrorCode()+ ": " 
				+ amapLocation.getErrorInfo();
				Log.e("error",errText);
				}
		}	
	}
	private void initLoc() {
       //初始化定位
       mLocationClient = new AMapLocationClient(getApplicationContext());
       //设置定位回调监听
       mLocationClient.setLocationListener(this);
       //初始化定位参数
       mLocationOption = new AMapLocationClientOption();
       //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
       mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
       //设置是否返回地址信息（默认返回地址信息）
       mLocationOption.setNeedAddress(true);
       //设置是否只定位一次,默认为false
       mLocationOption.setOnceLocation(true);
       //设置是否强制刷新WIFI，默认为强制刷新
       mLocationOption.setWifiActiveScan(true);
       //设置是否允许模拟位置,默认为false，不允许模拟位置
       mLocationOption.setMockEnable(true);
       //设置定位间隔,单位毫秒,默认为2000ms
       mLocationOption.setInterval(2000000*60);
       //给定位客户端对象设置定位参数
       mLocationClient.setLocationOption(mLocationOption);
       //启动定位
       mLocationClient.startLocation();
   }
	@Override
	protected void onDestroy() {
		mLocationClient.stopLocation();
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tiaoguo2:
			go2Splash();
			break;
	    case R.id.btn_go:
	    	if(mianji.getText().toString().equals("") ||
	    	   weizi.getText().toString().equals("选择所在省市")){
	    		toastShow("请填写完整的信息");
	    	}else{
	    		
	    		CityService mApplication=CityService.getInstance(this);
	    		int TJCityID = mApplication.getTJCityID(weizi.getText().toString());
	    		CacheUtils.cacheStringData(this, AppTag.TJ_CityName, weizi.getText().toString());  
	    		CacheUtils.cacheStringData(this, AppTag.TJ_CityID, TJCityID+"");  
	    		CacheUtils.cacheStringData(this, AppTag.TJ_SIZE,mianji.getText().toString());  
	    		CacheUtils.cacheBooleanData(SelectActivity.this, APP_FIRST,true);
	    		CacheUtils.cacheBooleanData(SelectActivity.this, "APP_XUQIU",true);
	    		Intent intent = new Intent(this, TueiJianActivity.class);
	    		intent.putExtra(TueiJianActivity.TYPE, 1);
	    		startActivity(intent);
	    		boolean isFirst1 = CacheUtils.getBooleanData(this, "APP_TIAO", false);
	    	    if(!isFirst1){
	    	    	Intent intent1 = new Intent();
		    		intent1.setAction("addjd");
		    		// 发送 一个无序广播
		    		SelectActivity.this.sendBroadcast(intent1);
	    		}
	    		finish();	    		
	    	}
			break;
       case R.id.weizi:
    		Intent intentCity = new Intent(SelectActivity.this, SelectCityActivity1.class);
    		startActivityForResult(intentCity, 5);
			break;
       case R.id.back:
    	   Intent intent = new Intent(this, HouseTypeActivity.class);
		   startActivity(intent);
   	       finish();
			break;
       case R.id.gong_lyout:
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).
        hideSoftInputFromWindow(SelectActivity.this.getCurrentFocus().getWindowToken(),
        InputMethodManager.HIDE_NOT_ALWAYS);
   			break;
		default:
			break;
		}
	}
	void findCity() {
		if (TextUtils.isEmpty(SJQApp.address)) {
			return;
		}
		CityService cs = CityService.getInstance(getApplicationContext());
		if (cs == null)
			return;
		final List<City> province_list = cs.getAllProvince();
		if (province_list == null)
			return;
		new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < province_list.size(); i++) {
					if (SJQApp.address.contains(province_list.get(i).Title)) {

						int ProvinceID = province_list.get(i).CityID;
						List<City> listCity = CityService.getInstance(
								getApplicationContext()).getArea(ProvinceID);
						if (listCity != null) {
							for (int ii = 0; ii < listCity.size(); ii++) {
								if (SJQApp.address.contains(listCity.get(ii).Title)) {
									SJQApp.setCity(listCity.get(ii));
									return;

								}

							}
						}

						return;
					}
				}

			}
		}).start();
	}
		// 跳转到启动页
		protected void go2Splash() {
			CacheUtils.cacheBooleanData(SelectActivity.this, APP_FIRST,
					true);
			Intent intent = new Intent(this, SkipActivity.class);
			intent.putExtra(AppTag.TAG_ID, 1);
			startActivity(intent);
			finish();
		}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == AppTag.RESULT_OK) {
			if (requestCode == 5) {
				weizi.setText(data.getStringExtra(AppTag.TAG_TITLE));
				int CityID = data.getIntExtra(AppTag.TAG_ID, 0);
				if (CityID == 310000)
					CityID = 310100;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(mianji.hasFocus()) {
			

		}else{
			Intent intent = new Intent(this, HouseTypeActivity.class);
			startActivity(intent);
			finish();			
		}
		return super.onKeyDown(keyCode, event);
	}
}