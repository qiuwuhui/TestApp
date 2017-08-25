package com.baolinetworkchnology.shejiquan.map;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class MapActivity extends Activity implements OnGeocodeSearchListener,
  LocationSource,AMapLocationListener,AMap.OnMapClickListener
  ,InfoWindowAdapter,OnClickListener{
	 MapView mMapView = null;
	 private AMap aMap;
	 private OnLocationChangedListener mListener;
	 private AMapLocationClient mlocationClient;
	 private AMapLocationClientOption mLocationOption;
	 double latitude;
	 double longitude;
	 String addressName ="";
	 TextView editTex_serch;
	 TextView TextView01;
	 TextView tv_title2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		   //获取地图控件引用
	    mMapView = (MapView) findViewById(R.id.map_ditu);
	    //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
	    mMapView.onCreate(savedInstanceState);
	    findViewById(R.id.back).setOnClickListener(this);
	    findViewById(R.id.btn_go).setOnClickListener(this);
	    editTex_serch=(TextView) findViewById(R.id.editTex_serch);
	    editTex_serch.setOnClickListener(this);
	    TextView01=(TextView) findViewById(R.id.TextView01);
	    tv_title2=(TextView) findViewById(R.id.tv_title2);
	    init();
	}
	 /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
//          mMapView.addView
            aMap.setOnMapClickListener(this);
        }

    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {      
            aMap.getUiSettings().setCompassEnabled(false);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            aMap.setLocationSource(this);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
          
//          GeocodeSearch geocoderSearch = new GeocodeSearch(this); 
//			geocoderSearch.setOnGeocodeSearchListener(this); 
//			//name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode 
			if(!SJQApp.ZhuanxiuData.getGisLat().equals("")&& 
					!SJQApp.ZhuanxiuData.getGisLng().equals("")){
				double GisLat=Double.valueOf(SJQApp.ZhuanxiuData.getGisLat()).doubleValue();
				double GisLng=Double.valueOf(SJQApp.ZhuanxiuData.getGisLng()).doubleValue();
				LatLng latlng = new LatLng(GisLat, GisLng);
				drawMarkers(latlng, SJQApp.ZhuanxiuData.getAddress());
			}
//			GeocodeQuery query = new GeocodeQuery(Address, Address); 
//			geocoderSearch.getFromLocationNameAsyn(query);
   }
	/**
	 * 绘制系统默认的1种marker背景图片
	 */
	public void drawMarkers(LatLng latlng,String str) {
		Marker marker = aMap.addMarker(new MarkerOptions()
				.position(latlng)
				.title(str)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				.draggable(true));
		marker.showInfoWindow();// 设置默认显示一个infowinfow
	}
	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
	    mMapView.onDestroy();
	  }
	 @Override
	 protected void onResume() {
	    super.onResume();
	    //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
	    mMapView.onResume();
	    }
	 @Override
	 protected void onPause() {
	    super.onPause();
	    //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
	    mMapView.onPause();
	    }
	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
	    mMapView.onSaveInstanceState(outState);
	  }
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
			if (amapLocation != null
                /*开启定位时*/
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                LogUtils.e("DWlatlog", amapLocation.getLatitude() + "");
                latitude = amapLocation.getLatitude();
                longitude = amapLocation.getLongitude();
                aMap.clear();
                mlocationClient.stopLocation();
                if(SJQApp.ZhuanxiuData.getGisLat().equals("")&& 
    					SJQApp.ZhuanxiuData.getGisLng().equals("")){
                	LatLng latlng = new LatLng(latitude, longitude);
                	drawMarkers(latlng, amapLocation.getAddress());                	
                }
            } else {
            /*没开启定位时*/
//                String notLat = MobclickAgent.getConfigParams(this, "defaultLatitude");
//                String notLog = MobclickAgent.getConfigParams(this, "defaultLongitude");
//                LatLng latLng = new LatLng(Double.valueOf(notLat), Double.valueOf(notLog));
//                aMap.getUiSettings().setCompassEnabled(false);
//                aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
//                aMap.getUiSettings().setScaleControlsEnabled(true);// 设置比例尺
//////                MarkerOptions otMarkerOptions = new MarkerOptions();
//////                otMarkerOptions.position(latLng);
//////                otMarkerOptions.draggable(true);
//////                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
//////                aMap.addMarker(otMarkerOptions);
//                latitude = latLng.latitude;
//                longitude = latLng.longitude;
//                drawMarkers(latLng, "厦门市思明区");
//                mlocationClient.stopLocation();
            }
	}

	@Override
	public void onMapClick(LatLng latLng) {
		//点击地图后清理图层插上图标，在将其移动到中心位置
        aMap.clear();
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        MarkerOptions otMarkerOptions = new MarkerOptions();
        otMarkerOptions.position(latLng);
        aMap.addMarker(otMarkerOptions);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));   
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);//传入context
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);  
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP); 
        geocoderSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener() {
        	LatLng LatLng=new LatLng(latitude, longitude);
			@Override
			public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
				if (rCode == 1000) {  
                    addressName =result.getRegeocodeAddress().getFormatAddress();
					drawMarkers(LatLng,addressName);
                } else {  
                	drawMarkers(LatLng, "暂时未获取到地理位置");
                }  				
			}
			
			public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
        geocoderSearch.getFromLocationAsyn(query); 
	}
	 /**
     * 激活定位
     */
	@Override
	public void activate(OnLocationChangedListener listener) {
		 mListener = listener;
	        if (mlocationClient == null) {
	            mlocationClient = new AMapLocationClient(this);
	            mLocationOption = new AMapLocationClientOption();
	            //设置定位监听
	            mlocationClient.setLocationListener(this);
	            //设置为高精度定位模式
	            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
	            //设置定位参数
	            mlocationClient.setLocationOption(mLocationOption);
	            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
	            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
	            // 在定位结束后，在合适的生命周期调用onDestroy()方法
	            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
	            mlocationClient.startLocation();
	        }

		
	}
	 /**
     * 停止定位
     */
	@Override
	public void deactivate() {
		 mListener = null;
	        if (mlocationClient != null) {
	            mlocationClient.stopLocation();
	            mlocationClient.onDestroy();
	        }
	        mlocationClient = null;
		
	}
	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {
		ImageView imageView = (ImageView) view.findViewById(R.id.badge);
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
					titleText.length(), 0);
			titleUi.setTextSize(15);
			titleUi.setText(titleText);

		} else {
			titleUi.setText("");
		}
		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
					snippetText.length(), 0);
			snippetUi.setTextSize(20);
			snippetUi.setText(snippetText);
		} else {
			snippetUi.setText("");
		}
	}
	@Override
	public View getInfoContents(Marker arg0) {
		
		return null;
	}
	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.editTex_serch:
			Intent intent2 = new Intent(MapActivity.this, MapSerchActivity.class);
			startActivityForResult(intent2,3);
			break;
		case R.id.tv_title2:
				
			break;
		case R.id.btn_go:
			Intent data = new Intent();
			data.putExtra(AppTag.TAG_TEXT, addressName);
			data.putExtra("latitude", latitude+"");
			data.putExtra("longitude", longitude+"");
			setResult(RESULT_OK, data);
			finish();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==3 && resultCode==AppTag.RESULT_OK){
			String District=data.getStringExtra("District");
			String Adcode=data.getStringExtra("Adcode");
			GeocodeSearch geocoderSearch = new GeocodeSearch(this); 
			geocoderSearch.setOnGeocodeSearchListener(this); 
			// name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode 
			GeocodeQuery query = new GeocodeQuery(District, Adcode); 
			geocoderSearch.getFromLocationNameAsyn(query);
			
		}
	}
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		if (rCode == 1000) { 
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
			GeocodeAddress address = result.getGeocodeAddressList().get(0);
			latitude=address.getLatLonPoint().getLatitude();
			longitude=address.getLatLonPoint().getLongitude();
			LatLng latlng = new LatLng(latitude,longitude);
			addressName=address.getFormatAddress();
			aMap.clear();
			MarkerOptions otMarkerOptions = new MarkerOptions();
		    otMarkerOptions.position(latlng);
		    aMap.addMarker(otMarkerOptions);
		    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
			drawMarkers(latlng,addressName);
			editTex_serch.setText(address.getFormatAddress());
			}else{
				Toast.makeText(this, "地理位置查找失败", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, "地理位置查找失败", Toast.LENGTH_SHORT).show();					
		}
	}
	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
