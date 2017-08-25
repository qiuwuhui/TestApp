package com.baolinetworktechnology.shejiquan.activity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.baolinetworkchnology.shejiquan.map.MapActivity;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.DesignerZhuanxiu;
import com.baolinetworktechnology.shejiquan.interfaces.AppLoadListener;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.byl.datepicker.citywheelview.AbstractWheelTextAdapter;
import com.byl.datepicker.citywheelview.AddressData;
import com.byl.datepicker.citywheelview.CityWheelView;
import com.byl.datepicker.citywheelview.OnWheelChangedListener;
import com.guojisheng.koyview.ActionSheetDialog;
import com.guojisheng.koyview.ActionSheetDialog.OnSheetItemClickListener;
import com.guojisheng.koyview.ActionSheetDialog.SheetItemColor;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 个人资料设置
 * 
 * @author JiSheng.Guo
 * 
 */
public class ZxDetailInfoActivity extends BaseActivity {
	@ViewInject(R.id.designing)
	TextView designing;
	
	@ViewInject(R.id.wenhou_tv)
	TextView wenhou_tv;

	@ViewInject(R.id.des_style)
	TextView desStyle;

	@ViewInject(R.id.fromCity)
	TextView fromCity;
	
	@ViewInject(R.id.shiming_tv)
	TextView shiming_tv;

	@ViewInject(R.id.cost)
	TextView cost;
	
	@ViewInject(R.id.name)
	com.guojisheng.koyview.MyEditText name;
	
	@ViewInject(R.id.scrollView)
	ScrollView scrollView;

	private CircleImg mUserHead;

	private BitmapUtils mImageUtil;
	private static final int IMAGE_REQUEST_CODE = 50;// 图片请求码
	private static final int CAMERA_REQUEST_CODE = 10;// 照相请求码
	private static final int RESIZE_REQUEST_CODE = 20;// 返回结果码
	private static final String IMAGE_FILE_NAME = "headLogo.jpg";
    String AreaIds="";
    String GisLat="";
    String GisLng="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zx_detail_info);
		TextView tv_title2 = (TextView) findViewById(R.id.tv_title2);
		tv_title2.setVisibility(View.VISIBLE);
		tv_title2.setText("完成");
		tv_title2.setOnClickListener(this);
		setTitle("个人资料");
		ViewUtils.inject(this);
		mUserHead = (CircleImg) findViewById(R.id.userHead);
		mImageUtil = new BitmapUtils(this);
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		// 如果本地有数据则直接读取，否则联网获取
		if (SJQApp.user != null) {
			name.setText(SJQApp.user.nickName);
			if (SJQApp.ZhuanxiuData != null) {
				setData(SJQApp.ZhuanxiuData);
				getCityIndex();
			}
			SJQApp app = ((SJQApp) getApplication());

			app.setOnAppLoadListener(new AppLoadListener() {

				@Override
				public void onAppLoadListener(boolean loadState) {
					if (loadState) {
						setData(SJQApp.ZhuanxiuData);
					}

				}
			});
			app.loadData();

		} else {
			// 没登陆时候，直接返回
			finish();
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
	@Override
	protected void onRestart() {
		super.onRestart();
		if (SJQApp.user == null) {
			go2Login();

		}
	}

	private void go2Login() {
		Toast.makeText(getApplicationContext(), "账户信息失效，请重新登录",
				Toast.LENGTH_SHORT).show();
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}


	// 展现数据
	private void setData(DesignerZhuanxiu zxData) {
		if (zxData == null) {
			toastShow("您的账户存在错误，请联系我们");
			return;
		}
		name.setText(zxData.getEnterpriseName());
		mImageUtil.display(mUserHead,zxData.getLogo());
		if( TextUtils.isEmpty(SJQApp.ZhuanxiuData.getGisLng())
				&& TextUtils.isEmpty(SJQApp.ZhuanxiuData.getGisLng())){
			designing.setText("未设置");			
		}else{
			designing.setText("已设置");	
		}
		desStyle.setText(zxData.getAddress());
		if(zxData.getProvinceID()!=0 && zxData.getCityID()!=0){
			fromCity.setText(getfromCity(zxData.getProvinceID(),zxData.getCityID()));		
		}		
		cost.setText(zxData.getAreaRange());
		AreaIds=zxData.getAreaIds();
		GisLat=zxData.getGisLat();
		GisLng=zxData.getGisLng();
		desStyle.setText(zxData.getAddress());
		if(zxData.getRenzheng().equals("已认证")){
			shiming_tv.setText("已认证");
		}else if(zxData.getRenzheng().equals("未认证")){
			shiming_tv.setText("未认证");
		}else if(zxData.getRenzheng().equals("审核中")){
			shiming_tv.setText("审核中");
		}else if(zxData.getRenzheng().equals("未认证")){
			shiming_tv.setText("未认证");
		}
		wenhou_tv.setText(zxData.getGreetings());
	}

	int cityIndex = 1;

	private void getCityIndex() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < AddressData.PROVINCES.length; i++) {
					if (SJQApp.userData != null) {
						if (TextUtils.isEmpty(SJQApp.userData.getFromCityName())) {
							return;
						}
						if (SJQApp.userData.getFromCityName()
								.contains(AddressData.PROVINCES[i])) {
							cityIndex = i;
							return;
						}
					}
				}

			}
		}).start();

	}

	// 点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			doBack();
			break;
		case R.id.item_city:
			initmPopupWindowView(getCity());
			popupwindow.showAtLocation(findViewById(R.id.lay), Gravity.BOTTOM,
					0, 0);
			break;
		case R.id.tv_title2:
			String nametx=name.getText().toString();
			String from=fromCity.getText().toString();
			String fuwu=cost.getText().toString();
			String dizhi=desStyle.getText().toString();
			String ditu=designing.getText().toString();
			if(SJQApp.ZhuanxiuData.getLogo().equals("") ||nametx.equals("")
				||from.equals("") || dizhi.equals("") ||ditu.equals("未设置")){
				toastShow("公司logo、公司名称、城市、服务区域、详细地址、 地图位置不能为空");
			}else{
				doSave();				
			}
			break;
		case R.id.item_cost:// 服务地址
			if(fromCity.getText().toString().equals("")){
				toastShow("请选择所在城市");
			}else{
				Intent intent1 = new Intent(this, FuwuActivity.class);
				if(cs!=null){
					intent1.putExtra("CityID",cs.CityID);					
				}
				startActivityForResult(intent1, 101);							
			}
			break;
		case R.id.item_designing:// 地图位置
			Intent intent = new Intent(this, MapActivity.class);
			startActivityForResult(intent, 0);

			break;
		case R.id.wenhou:// 问候语
			Intent intent2 = new Intent(this, SetDataActivity.class);
			intent2.putExtra(AppTag.TAG_TITLE, "问候语");
			intent2.putExtra(AppTag.TAG_TEXT, wenhou_tv.getText().toString());
			startActivityForResult(intent2, 2);
			break;
		case R.id.item_desStyle:// 地理位置详情
			Intent intent3 = new Intent(this, SetDataActivity.class);
			intent3.putExtra(AppTag.TAG_TITLE, "地理位置");
			intent3.putExtra(AppTag.TAG_TEXT, desStyle.getText().toString());
			startActivityForResult(intent3, 25);
			break;
		case R.id.item_head:

			new ActionSheetDialog(this)
					.builder()
					.setCancelable(true)
					.setCanceledOnTouchOutside(true)
					.addSheetItem("查看头像", SheetItemColor.Red,
							new OnSheetItemClickListener() {

								@Override
								public void onClick(int which) {
									showImage();

								}

							})
					.addSheetItem("去相册选择头像", SheetItemColor.Red,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									selectImage();
								}
							}).show();
			break;
		case R.id.item_code:
			if (SJQApp.user != null) {
				DesignerZhuanxiu zxDesin=SJQApp.ZhuanxiuData;
				if(zxDesin!=null){
					if(zxDesin.getLogo().equals("") || zxDesin.getEnterpriseName().equals("")|| 
					   zxDesin.getProvinceID()==0 || zxDesin.getCityID()==0 || zxDesin.getAreaRange().equals("")
					   ||zxDesin.getAddress().equals("")|| TextUtils.isEmpty(zxDesin.getGisLng())
					   || TextUtils.isEmpty(zxDesin.getGisLng())){
						
					toastShow("请先将公司logo、公司名称、城市、服务区域、详细地址、 地图位置等资料进行保存才能认证");	
					}else{
						if(shiming_tv.getText().toString().equals("未认证")){
							startActivityForResult(new Intent(getActivity(), ZhuanxiuActivity.class), 15);					
						}else{
							startActivity(new Intent(getActivity(), ZzActivity.class));		
						}					
					}
				}else{
					toastShow("获取用户数据出错");
				}
//				Intent codeIntent = new Intent(this, MyCodeActivity.class);
//				startActivity(codeIntent);
			}
			break;
		default:
			break;
		}
	}

	protected void selectImage() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESIZE_REQUEST_CODE);
	}

	private void showResizeImage(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			uploadFile(CommonUtils.saveMyBitmap(photo, "head"));
		}
	}

	// 上传文件
	public void uploadFile(File file) {
		final String url = ApiUrl.UPLOAD_FILE;
		RequestParams params = getParams(url);
		params.addBodyParameter("file", file);
		params.addBodyParameter("UserID", SJQApp.user.id + "");
		
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onStart() {
				if (dialog == null)
					return;
				dialog.show("上传中");
			}

			@Override
			public void onSuccess(ResponseInfo<String> n) {
				if (dialog == null)
					return;
				dialog.dismiss();
				Bean data = CommonUtils.getDomain(n, Bean.class);
				if (data != null) {
					if (data.success) {
						String imageUrl = CommonUtils
								.getString(n.result, "url");
						uploadLogo(imageUrl);
						LogUtils.i("logo url", n.result);
					} else {
						toastShow(data.message);
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				
				if (dialog == null)
					return;
				dialog.dismiss();
				AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
						error.getExceptionCode() + "", "POST", url);
				toastShow("无法连接到服务器，请稍后重试");
			}

		};
		getHttpUtils().send(HttpMethod.POST,ApiUrl.API+url, params, callBack);// "http://api.shejiquan.me"
																	// +

	}

	// 更新头像
	public void uploadLogo(final String logoUrl) {
		final String url = ApiUrl.UPLOAD_LOGO;
		RequestParams parm = getParams(url);
		parm.addBodyParameter("Logo", logoUrl);
		parm.addBodyParameter("Guid", SJQApp.user.guid);
		parm.addBodyParameter("UserType", "" + AppTag.ZHUANXIU);
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				if (dialog != null)
					dialog.show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> n) {
				if (dialog == null)
					return;
				Bean data = CommonUtils.getDomain(n, Bean.class);
				if (data != null && data.success) {
					mImageUtil.display(mUserHead, logoUrl);
					SJQApp.ZhuanxiuData.setLogo(logoUrl);
				}
				toastShow(data.message);
				dialog.dismiss();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				if (dialog == null)
					return;
				toastShow("网络请求失败");
				dialog.dismiss();
				AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
						error.getExceptionCode() + "", "POST", url);
			}
		};
		getHttpUtils().send(HttpMethod.POST, ApiUrl.API + url, parm, callBack);

	}

	protected void showImage() {
		if (SJQApp.ZhuanxiuData!= null) {
			Intent intent = new Intent(this, PhotoActivity.class);
			intent.putExtra(PhotoActivity.IMAGE_URL, SJQApp.ZhuanxiuData.getLogo());
			startActivity(intent);
		} else {
			toastShow("请稍后...");
		}
	}

	// 提交保存到服务器
	private void doSave() {
		if (SJQApp.user == null) {
			finish();
			return;
		}
		if (SJQApp.ZhuanxiuData == null) {
			toastShow("数据未获取到，请稍候");
			return;
		}
		if (name.getText().toString().length() < 2) {
			name.setError("姓名至少两位数");
			return;
		}
		 dialog.show();
		 new Thread(downloadRun).start();  
//	               取得HTTP response  	
    }
    Runnable downloadRun = new Runnable(){  
	  
	 @Override  
	 public void run() {  
		 final String url = ApiUrl.API+ApiUrl.UPDATE_BUSINESS_DETAIL;
		 HttpPost httpRequest =new HttpPost(url); 
		 httpRequest.addHeader("Content-Type", "application/json");		 
		  //发出HTTP request  
	     try {
	    	int CityID = SJQApp.ZhuanxiuData.getCityID(), 
	    		ParentID = SJQApp.ZhuanxiuData.getProvinceID();
	    			if (cs != null && sc != null && isCheckCity) {
	    				CityID = cs.CityID;
	    				ParentID = sc.CityID;
	    			 }
	    	JSONObject jsonObject = new JSONObject(); 
	    	jsonObject.put("ID",SJQApp.ZhuanxiuData.getID()+"");
	    	jsonObject.put("EnterpriseName",name.getText().toString());	
	    	jsonObject.put("Logo",SJQApp.ZhuanxiuData.getLogo());	
	    	jsonObject.put("ProvinceID",ParentID+"");	
	    	jsonObject.put("CityID",CityID+"");	
	    	jsonObject.put("AreaRange",cost.getText().toString());	
	    	jsonObject.put("AreaRangIds",AreaIds);	
	    	jsonObject.put("Address",desStyle.getText().toString());
	    	jsonObject.put("Greetings",wenhou_tv.getText().toString());	
	    	jsonObject.put("GisLat",GisLat);	
	    	jsonObject.put("GisLng",GisLng);	
	    	
			httpRequest.setEntity(new StringEntity(jsonObject.toString(),HTTP.UTF_8));
			HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest); 
			 if(httpResponse.getStatusLine().getStatusCode()==200){  
   		      //取出回应字串  
			      String strResult=EntityUtils.toString(httpResponse.getEntity());  
				  JSONObject json=new JSONObject(strResult);
				  boolean success=json.getBoolean("success");
				  Message message = Message.obtain();
				  if(success){
					  message.what = 1;			  
				  }else{
					  message.what = 2;
				  }
				  timeHandler.sendMessage(message);
			     }else{  
			      Message message = Message.obtain();
				  message.what = 2;
				  timeHandler.sendMessage(message);
			     }  
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
	    	 // TODO Auto-generated catch block
	    	 e.printStackTrace();
	     } catch (IOException e) {
	    	 // TODO Auto-generated catch block
	    	 e.printStackTrace();
	     } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 }  
	   };  
	   private Handler timeHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					dialog.dismiss();
					toastShow("公司个人资料修改成功");
					finish();
				}else if(msg.what == 2){
					dialog.dismiss();
					toastShow("公司个人资料修改失败");
				}

			}
		};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {

			String text = data.getStringExtra(AppTag.TAG_TEXT);
			switch (requestCode) {
			case 0:
//				designing.setText("已设置");
				GisLat=data.getStringExtra("latitude");
				GisLng=data.getStringExtra("longitude");
//				SJQApp.ZhuanxiuData.setGisLat(gisLat);
//				SJQApp.ZhuanxiuData.setGisLng(gisLng);
				break;
			case 2:
                wenhou_tv.setText(text);
				break;
			case IMAGE_REQUEST_CODE:
				resizeImage(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (isSdcardExisting()) {
					resizeImage(getImageUri());
				} else {
					Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
							.show();
				}
				break;

			case RESIZE_REQUEST_CODE:
				if (data != null) {
					showResizeImage(data);
				}
				break;
			case 15:
				shiming_tv.setText("审核中");
				break;
			case 25:
				desStyle.setText(text);
				break;
			case 101:
				cost.setText(text);
				AreaIds=data.getStringExtra("AreaIds");
				break;
			default:
				break;
			}

		}

	}

	private Uri getImageUri() {
		return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
				IMAGE_FILE_NAME));
	}

	private boolean isSdcardExisting() {
		final String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public void resizeImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESIZE_REQUEST_CODE);
	}

	/**
	 * 更多菜单初始化
	 */
	private PopupWindow popupwindow;

	private boolean isBack = true;

	// 初始化Pop
	public void initmPopupWindowView(View view) {
		hideInput();
		// 创建PopupWindow实例, , 分别是宽度和高度
		popupwindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupwindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		popupwindow.setOutsideTouchable(true);
		popupwindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
			}
		});
		popupwindow.setBackgroundDrawable(getResources().getDrawable(
				R.color.write));

	}

	// 关闭弹窗
	boolean popDismiss() {
		if (popupwindow != null && popupwindow.isShowing()) {
			popupwindow.dismiss();
			popupwindow = null;
			return true;
		} else {
			return false;
		}
	}
	List<City> countries = null;
	City sc, cs;
	boolean isCheckCity = false;

	// 获取城市视图
	private View getCity() {
		isCheckCity = false;
		final View contentView = LayoutInflater.from(this).inflate(
				R.layout.wheelcity_cities_layout, null);
		final CityWheelView country = (CityWheelView) contentView
				.findViewById(R.id.wheelcity_country);
		final CityWheelView city = (CityWheelView) contentView
				.findViewById(R.id.wheelcity_city);
		CityService cityService = CityService.getInstance(this);
		if (cityService == null)
			return contentView;

		countries = cityService.getAllProvince();

		country.setVisibleItems(3);
		country.setViewAdapter(new CountryAdapter(this, countries));
		city.setVisibleItems(0);

		country.addChangingListener(new OnWheelChangedListener<City>() {

			@Override
			public void onChanged(CityWheelView wheel, int oldValue,
					int newValue, City c) {

				updateCities(city, c);
				cityIndex = newValue;

			}
		});

		city.addChangingListener(new OnWheelChangedListener<City>() {

			@Override
			public void onChanged(CityWheelView wheel, int oldValue,
					int newValue, City t) {
				cs = t;

			}
		});
		contentView.findViewById(R.id.relativeOk).setVisibility(View.VISIBLE);
		contentView.findViewById(R.id.ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (sc != null && cs != null) {
							isCheckCity = true;
							fromCity.setText(sc.Title + "-" + cs.Title);
							
						}
						popupwindow.dismiss();
					}
				});
		country.setCurrentItem(cityIndex);
		city.setCurrentItem(0);

		return contentView;
	}

	CountryAdapter cityAdapter;

	/**
	 * 更新城市
	 */
	private void updateCities(CityWheelView city, City c) {
		sc = c;
		if (cityAdapter == null) {
			cityAdapter = new CountryAdapter(this);
			cityAdapter.setTextSize(18);
		}
		cityAdapter.setData(getCities(c));
		city.setViewAdapter(cityAdapter);
		city.setCurrentItem(0);
	}
	//获取装修公司所在地区省市
	private String getfromCity(int ProvinceID,int CityID){
		CityService cityService = CityService.getInstance(this);
		if (cityService == null)
			return null;
		String shengshi=cityService.fromCity(ProvinceID, CityID);
		return shengshi;	
	}
	private List<City> getCities(City city) {
		CityService cityService = CityService.getInstance(this);
		if (cityService == null)
			return null;
		if (countries == null)
			return null;
		if (city == null)
			return null;

		List<City> clist = cityService.getArea(city.CityID);
		if (clist != null && clist.size() > 0)
			cs = clist.get(0);
		if (cs != null) {
			if (cs.Title.contains("香港") || cs.Title.contains("澳门"))
				return cityService.getArea(cs.CityID);
		}

		return clist;
	}
	

	class CountryAdapter extends AbstractWheelTextAdapter {

		private List<City> countries;

		public void setData(List<City> countries) {
			this.countries = countries;
		}

		protected CountryAdapter(Context context) {
			super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
			setItemTextResource(R.id.wheelcity_country_name);

		}

		protected CountryAdapter(Context context, List<City> countries) {
			super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
			setItemTextResource(R.id.wheelcity_country_name);
			this.countries = countries;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			if (countries == null)
				return 0;
			return countries.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			if (countries == null)
				return "";
			City c = countries.get(index);
			if (c == null)
				return "";

			String title = c.Title;
			if (title.length() > 6)
				return title.subSequence(0, 4)+"...";

			return title;
		}

		@Override
		public Object getItem(int index) {
			if (countries == null)
				return null;
			if (index >= 0 && index < countries.size())
				return countries.get(index);
			return null;
		}
	}
	protected void onDestroy() {
		super.onDestroy();
		if(popupwindow != null){
			popupwindow.dismiss();
		}
	}
	// 返回
	private void doBack() {
		if (popupwindow != null && popupwindow.isShowing()) {
			popupwindow.dismiss();
			return;
		}
		if (isBack) {
			new MyAlertDialog(this).setTitle("提示").setContent("是否放弃编辑？")
					.setBtnOk("继续编辑").setBtnCancel("放弃编辑")
					.setBtnOnListener(new DialogOnListener() {

						@Override
						public void onClickListener(boolean isOk) {
							if (isOk) {
							} else {
								finish();
							}

						}
					}).show();

		} else {
			finish();
		}

	}

	@Override
	public void onBackPressed() {
		doBack();
	}
}
