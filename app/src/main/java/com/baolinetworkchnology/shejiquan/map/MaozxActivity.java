package com.baolinetworkchnology.shejiquan.map;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
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
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.R.string;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

public class MaozxActivity extends Activity implements OnGeocodeSearchListener,
InfoWindowAdapter,OnClickListener,OnInfoWindowClickListener,OnRouteSearchListener
,AMapLocationListener,LocationSource{
	private OnLocationChangedListener mListener =null;
	private AMapLocationClient mLocationClient=null;
	private AMapLocationClientOption mLocationOption =null;
	double latitude;
	double longitude;
	MapView mMapView = null;
	private RouteSearch mRouteSearch;
	private DriveRouteResult mDriveRouteResult;
	private AMap aMap;
	String Address="";
	private LatLonPoint LonPoint=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maozx);
		Address=getIntent().getStringExtra("Address");
		findViewById(R.id.back).setOnClickListener(this);
		mMapView = (MapView) findViewById(R.id.map_ditu);
		mMapView.onCreate(savedInstanceState);
		init();
		initLoc();		
	}
	 /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
//          mMapView.addView
//          aMap.setOnMapClickListener(this);
        }

    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {   
		    mRouteSearch = new RouteSearch(this);
		    mRouteSearch.setRouteSearchListener(this);
    	    aMap.setOnInfoWindowClickListener(MaozxActivity.this);
            aMap.getUiSettings().setCompassEnabled(false);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//            Address
            GeocodeSearch geocoderSearch = new GeocodeSearch(this); 
			geocoderSearch.setOnGeocodeSearchListener(this); 
			// name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode 
			GeocodeQuery query = new GeocodeQuery(Address, Address); 
			geocoderSearch.getFromLocationNameAsyn(query);
     
   }
    /**
	 * 绘制系统默认的1种marker背景图片
	 */
	public void drawMarkers(LatLng latlng,String str) {
		Marker marker = aMap.addMarker(new MarkerOptions()
				.position(latlng)
				.title(str).snippet("\n"+"点击规划路径")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				.draggable(true));
		marker.showInfoWindow();// 设置默认显示一个infowinfow
	}
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		if (rCode == 1000) { 
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
			GeocodeAddress address = result.getGeocodeAddressList().get(0);
			LonPoint=address.getLatLonPoint();
			LatLng latlng = new LatLng(address.getLatLonPoint().getLatitude(), 
					 address.getLatLonPoint().getLongitude());
			aMap.clear();
			MarkerOptions otMarkerOptions = new MarkerOptions();
		    otMarkerOptions.position(latlng);
		    aMap.addMarker(otMarkerOptions);
		    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
			drawMarkers(latlng,address.getFormatAddress());
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

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
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
	public void onInfoWindowClick(Marker marker) {
		if(latitude==0 || longitude==0){
			Toast.makeText(this, "当前位置定位失败", Toast.LENGTH_SHORT).show();
		}else{
			LatLonPoint mStartPoint = new LatLonPoint(latitude,longitude);//起点
			LatLonPoint mEndPoint = new LatLonPoint(LonPoint.getLatitude(), LonPoint.getLongitude());
			final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
					mStartPoint, mEndPoint);
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null,
					null, "");
			mRouteSearch.calculateDriveRouteAsyn(query);			
		}
	}
	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
		aMap.clear();// 清理地图上的所有覆盖物
		if (errorCode == 1000) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mDriveRouteResult = result;
					final DrivePath drivePath = mDriveRouteResult.getPaths()
							.get(0);
					DriveRouteColorfulOverLay drivingRouteOverlay = new DriveRouteColorfulOverLay(
							aMap, drivePath,
							mDriveRouteResult.getStartPos(),
							mDriveRouteResult.getTargetPos(), null);
					drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
					drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
					drivingRouteOverlay.removeFromMap();
					drivingRouteOverlay.addToMap();
					drivingRouteOverlay.zoomToSpan();
					int dis = (int) drivePath.getDistance();
					int dur = (int) drivePath.getDuration();
					String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
				} else if (result != null && result.getPaths() == null) {
					Toast.makeText(this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
		}
		
		
	}
	@Override
	public void onWalkRouteSearched(WalkRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void activate(OnLocationChangedListener listener) {
		 mListener = listener;
	}
	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
	    mLocationClient.stopLocation();
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
	public void deactivate() {
		 mListener = null;
	}
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null) {
			if (amapLocation != null
                /*开启定位时*/
                    && amapLocation.getErrorCode() == 0) {
//              mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                LogUtils.e("DWlatlog", amapLocation.getLatitude() + "");
                latitude = amapLocation.getLatitude();
                longitude = amapLocation.getLongitude();
                LatLng latlng = new LatLng(latitude, longitude);
//              drawMarkers(latlng, amapLocation.getAddress());
            } else {
            /*没开启定位时*/
//                String notLat = MobclickAgent.getConfigParams(this, "defaultLatitude");
//                String notLog = MobclickAgent.getConfigParams(this, "defaultLongitude");
//                LatLng latLng = new LatLng(Double.valueOf(notLat), Double.valueOf(notLog));
//                aMap.getUiSettings().setCompassEnabled(false);
//                aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
//                aMap.getUiSettings().setScaleControlsEnabled(true);// 设置比例尺
//                latitude = latLng.latitude;
//                longitude = latLng.longitude;
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
}
