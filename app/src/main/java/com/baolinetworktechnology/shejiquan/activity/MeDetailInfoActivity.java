package com.baolinetworktechnology.shejiquan.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.GteStyeAdapter;
import com.baolinetworktechnology.shejiquan.adapter.MeDetailAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwDesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.UpfileBean;
import com.baolinetworktechnology.shejiquan.interfaces.AppLoadListener;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.net.GetClassList;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.SDPathUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.baolinetworktechnology.shejiquan.view.MyLinearLayout;
import com.baolinetworktechnology.shejiquan.view.MyRelativeLayout;
import com.byl.datepicker.citywheelview.AbstractWheelTextAdapter;
import com.byl.datepicker.citywheelview.AddressData;
import com.byl.datepicker.citywheelview.ArrayWheelAdapter;
import com.byl.datepicker.citywheelview.CityWheelView;
import com.byl.datepicker.citywheelview.OnWheelChangedListener;
import com.google.gson.Gson;
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
import com.umeng.analytics.MobclickAgent;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 个人资料设置
 * 
 * @author JiSheng.Guo
 * 
 */
public class MeDetailInfoActivity extends BaseActivity {
	@ViewInject(R.id.designing)
	TextView designing;
	
	@ViewInject(R.id.wenhou_tv)
	TextView wenhou_tv;

	@ViewInject(R.id.des_style)
	TextView desStyle;

	@ViewInject(R.id.des_field)
	TextView des_field;

	@ViewInject(R.id.fromCity)
	TextView fromCity;
	
	@ViewInject(R.id.shiming_tv)
	TextView shiming_tv;
	
	@ViewInject(R.id.officeTime)
	TextView officeTime;

	@ViewInject(R.id.cost)
	TextView cost;
	
	@ViewInject(R.id.name)
	com.guojisheng.koyview.MyEditText name;

	@ViewInject(R.id.tv_number)
	com.guojisheng.koyview.MyEditText tv_number;

	@ViewInject(R.id.sex)
	TextView sex;

	@ViewInject(R.id.comment)	
	TextView comment;
	@ViewInject(R.id.scrollView)
	ScrollView scrollView;

	private CircleImg mUserHead;

	private BitmapUtils mImageUtil;
	private static final int CAMERA_REQUEST_CODE = 10;// 照相请求码
	private static final int RESIZE_REQUEST_CODE = 20;// 返回结果码
	private static final String IMAGE_FILE_NAME = "headLogo.jpg";
	private MeDetailAdapter meDetailAdapter;
	private int xuanzhe;
	private boolean Isex;
	private MyLinearLayout mRoot1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(
				R.layout.activity_me_detail_info, null);
		setContentView(parentView);
		mRoot1 = (MyLinearLayout) findViewById(R.id.mRoot1);
		mRoot1.setFitsSystemWindows(true);
		TextView tv_title2 = (TextView) findViewById(R.id.tv_title2);
		tv_title2.setVisibility(View.VISIBLE);
		tv_title2.setText("完成");
		tv_title2.setOnClickListener(this);
		tv_title2.setFocusable(true);
		tv_title2.setFocusableInTouchMode(true);
		tv_title2.requestFocus();
		setTitle("我的资料");
		ViewUtils.inject(this);
		geuCase();
		getStye();
		mUserHead = (CircleImg) findViewById(R.id.userHead);
		comment.setOnClickListener(this);
		mImageUtil = new BitmapUtils(this);
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		// 如果本地有数据则直接读取，否则联网获取
		if (SJQApp.user != null) {
			name.setText(SJQApp.user.nickName);
			if (SJQApp.userData != null) {
				setData(SJQApp.userData);
				getCityIndex();
			}
			SJQApp app = ((SJQApp) getApplication());

			app.setOnAppLoadListener(new AppLoadListener() {

				@Override
				public void onAppLoadListener(boolean loadState) {
					if (loadState) {
						setData(SJQApp.userData);
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

	// 获取日期
	private View getDataPick() {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		final String[] dates = new String[21];
		for (int i = 0; i < 20; i++) {
			dates[i] = norYear + "年";
			norYear--;

		}
		dates[20] = 20 + "年以上";
		View view = View.inflate(this, R.layout.wheel_const_picker, null);
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				dates);
		final CityWheelView city = (CityWheelView) view
				.findViewById(R.id.whee_cost);

		view.findViewById(R.id.relativeOk).setVisibility(View.VISIBLE);
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				officeTime.setText(dates[city.getCurrentItem()]);
				popupwindow.dismiss();
			}
		});
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(2);

		return view;
	}
	//获取风格户型
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private View parentView;
	private TextView text_sc;
	private void getStye() {
		pop = new PopupWindow(MeDetailInfoActivity.this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindow_stye,
				null);
		pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(false);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		text_sc = (TextView) view.findViewById(R.id.text_sc);
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		GridView StyeView = (GridView) view.findViewById(R.id.Stye_GridView);
		meDetailAdapter = new MeDetailAdapter(this);
		StyeView.setAdapter(meDetailAdapter);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);

		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(meDetailAdapter.getPosition()==null){
					if(xuanzhe==1){
						addlistStye.clear();
						desStyle.setText("");
					}else if(xuanzhe==2){
						addlist.clear();
						des_field.setText("");
					}
					pop.dismiss();
					ll_popup.clearAnimation();
				}else{
					List<CaseClass> list=meDetailAdapter.getPosition();
					String Positionstr="";
					String des_ID="";
					for (int c = 0; c < list.size(); c++) {
						if(list.size()==1 || c==list.size()-1 ){
							Positionstr +=list.get(c).title;
							des_ID +=list.get(c).id;
						}else{
							Positionstr +=list.get(c).title+"、";
							des_ID +=list.get(c).id+",";
						}
					}
					if(xuanzhe==1){
						addlistStye.clear();
						desStyle.setText(Positionstr);
						desStyle.setTag(des_ID);
						addlistStye.addAll(list);
						meDetailAdapter.colocelist();
					}else if(xuanzhe==2){
						des_field.setText(Positionstr);
						des_field.setTag(des_ID);
						addlist.clear();
						addlist.addAll(list);
						meDetailAdapter.colocelist();
					}
					pop.dismiss();
					ll_popup.clearAnimation();
				}

			}
		});

	}
	//获取风格,领域信息
	private List<CaseClass> listStye;
	private List<CaseClass> list;
	private List<CaseClass> addlistStye=new ArrayList<>();
	private List<CaseClass> addlist=new ArrayList<>();
	private  void geuCase(){
		list = new GetClassList(this).getList("擅长空间");
		if(list!=null){
			list.remove(0);
		}
		listStye = new GetClassList(this).getList("风格");
		if(listStye!=null){
			listStye.remove(0);
		}
	}
	//获取设计师所在地区
	private String getfromCity(int ProvinceID,int CityID){
		CityService cityService = CityService.getInstance(getActivity());
		if (cityService == null)
			return null;
		String shi=cityService.fromCity(ProvinceID,CityID);
		return shi;
	}
	// 获取性别数据
	private View getSexPick() {
		final String[] j = { "男", "女" };
		View view = View.inflate(this, R.layout.wheel_const_picker, null);
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				j);
		final CityWheelView city = (CityWheelView) view
				.findViewById(R.id.whee_cost);
		view.findViewById(R.id.relativeOk).setVisibility(View.VISIBLE);
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sex.setText(j[city.getCurrentItem()]);
				popupwindow.dismiss();
			}
		});
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);

		return view;
	}

	// 获取价钱数据
	private View getCostPick() {
		final String[] j = { "0-50元/㎡", "50-100元/㎡", "100-150元/㎡",
				"150-200元/㎡", "200-300元/㎡", "300-600元/㎡", "600以上元/㎡", "面议" };
		View view = View.inflate(this, R.layout.wheel_const_picker, null);
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				j);
		final CityWheelView city = (CityWheelView) view
				.findViewById(R.id.whee_cost);
		view.findViewById(R.id.relativeOk).setVisibility(View.VISIBLE);
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cost.setText(j[city.getCurrentItem()]);
				popupwindow.dismiss();
			}
		});
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(3);

		return view;
	}

	// 展现数据
	private void setData(SwDesignerDetail designer) {
		if (designer == null) {
			toastShow("您的账户存在错误，请联系我们");
			return;
		}
		name.setText(designer.getName());
		name.setHintTextColor(getResources().getColor(R.color.acyivity_bg));
		tv_number.setHintTextColor(getResources().getColor(R.color.acyivity_bg));
		tv_number.setFocusable(false);
		tv_number.setEnabled(false);
		designing.setText(designer.getDesigning());
		desStyle.setText(designer.getStrStyle(this));
		des_field.setText(designer.getStrArea(this));
		desStyle.setTag(designer.getDesStyle());
		des_field.setTag(designer.getDesArea());
		if(designer.getProvinceID()==0 && designer.getCityID()==0){
			fromCity.setText(CacheUtils.getStringData(getActivity(),
					"weizhi", " "));
		}else{
			fromCity.setText(getfromCity(designer.getProvinceID(),designer.getCityID()));
		}
		tv_number.setText(designer.mobile);
		if ("-1".equals(designer.officeTime)
				|| "20年以上".equals(designer.officeTime)) {
			designer.officeTime = "20年以上";
			officeTime.setText(designer.officeTime);

		} else if (!"".equals(designer.officeTime)) {

			if (designer.officeTime == null) {
				officeTime.setText("");
			} else {

				officeTime.setText(designer.officeTime + "年");
			}
		}
		cost.setText(designer.getCost());
		if ("0".equals(designer.sex)) {
			sex.setText("女");
		} else if ("1".equals(designer.sex)) {
			sex.setText("男");
		} else {
			sex.setText(designer.sex);
		}
		if(designer.getRenzheng().equals("已认证")){
			shiming_tv.setText("已认证");
			name.setCursorVisible(false);
			name.setFocusable(false);
			name.setFocusableInTouchMode(false);
			Isex =true;
		}else if(designer.getRenzheng().equals("未认证")){
			shiming_tv.setText("未认证");
		}else if(designer.getRenzheng().equals("审核中")){
			shiming_tv.setText("审核中");
		}else if(designer.getRenzheng().equals("未认证")){
			shiming_tv.setText("未认证");
		}
		comment.setText(designer.getComment());
		if(designer.greetings!=null && !designer.greetings.equals("")){
			wenhou_tv.setText(designer.greetings);
		}else{
			wenhou_tv.setText("	点亮设计灵感，打造美好家居生活！");
		}
		wenhou_tv.setText(designer.greetings);
		mImageUtil.display(mUserHead, designer.getLogo());
	}

	int cityIndex = 1;

	private void getCityIndex() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < AddressData.PROVINCES.length; i++) {
					if (SJQApp.userData != null) {
						if (TextUtils.isEmpty(SJQApp.userData.getCityName())) {
							return;
						}
						if (SJQApp.userData.getCityName()
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
			String nametxt = name.getText().toString();
			String time = officeTime.getText().toString();
			String feiyong=cost.getText().toString();
			String fenge=desStyle.getText().toString();
			String chenshi=fromCity.getText().toString();
			if(SJQApp.userData.logo.equals("") || nametxt.equals("") ||	time.equals("")
				||feiyong.equals("") || fenge.equals("") || chenshi.equals("")){		
				
				toastShow("头像、名字、城市、从业时间、设计费用、擅长风格必须填写完整");
			}else{
				doSave();
			}
			break;
		case R.id.item_sex:// 性别
			if(!Isex) {
				initmPopupWindowView(getSexPick());
				popupwindow.showAtLocation(findViewById(R.id.lay), Gravity.BOTTOM,
						0, 0);
			}
			break;
		case R.id.item_cost:// 设计费用
			if (scrollView.getScrollY() < 30)
				scrollView.scrollBy(0, 30);
			initmPopupWindowView(getCostPick());
			popupwindow.showAtLocation(findViewById(R.id.lay), Gravity.BOTTOM,
					0, 0);
			break;
		case R.id.item_desStyle:// 擅长风格
			text_sc.setText("请选择您擅长风格 (至多4项)：");
			if(addlistStye.size()==0){
				if(!TextUtils.isEmpty(SJQApp.userData.getDesStyle())){
					String LoveStyle=SJQApp.userData.getDesStyle();
					String[] datas = null;
					if (LoveStyle.indexOf(",") != -1) {
						datas = LoveStyle.split(",");
					}else {
						List<CaseClass> list=new ArrayList<CaseClass>();
						for (int a = 0; a < listStye.size(); a++) {
							if (Integer.valueOf(LoveStyle).intValue() == listStye.get(a).id) {
								CaseClass caseClass2 = new CaseClass();
								caseClass2 = listStye.get(a);
								list.add(caseClass2);
							}
						}
						meDetailAdapter.setaddlist(list);
					}
					if (datas != null && datas.length >= 1) {
						List<CaseClass> list=new ArrayList<CaseClass>();
						for (int i = 0; i < datas.length; i++) {
							CaseClass caseClass1=new CaseClass();
							for (int a = 0; a < listStye.size(); a++){
								if(Integer.valueOf(datas[i]).intValue()==listStye.get(a).id){
									caseClass1=listStye.get(a);
								}
							}
							list.add(caseClass1);
						}
						meDetailAdapter.setaddlist(list);
					}
				}
			}else{
				meDetailAdapter.colocelist();
				meDetailAdapter.setaddlist(addlistStye);
			}
			xuanzhe=1;
			meDetailAdapter.setData(listStye);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(
					MeDetailInfoActivity.this,
					R.anim.activity_translate_in));
			pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
					MeDetailInfoActivity.this.getCurrentFocus()
							.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

			break;
		case R.id.item_field:// 擅长领域
			text_sc.setText("请选择您擅长领域 (至多4项)：");
			if(addlist.size()==0){
				if(!TextUtils.isEmpty(SJQApp.userData.getDesArea())){
					String LoveStyle=SJQApp.userData.getDesArea();
					String[] datas = null;
					if (LoveStyle.indexOf(",") != -1) {
						datas = LoveStyle.split(",");
					}else{
						List<CaseClass> lists=new ArrayList<CaseClass>();
						for (int a = 0; a < list.size(); a++){
								if(Integer.valueOf(SJQApp.userData.getDesArea()).intValue()==list.get(a).id){
									CaseClass caseClass1=new CaseClass();
									caseClass1=list.get(a);
									lists.add(caseClass1);
								}
							}
						meDetailAdapter.setaddlist(lists);
					}
					if (datas != null && datas.length >= 1) {
						List<CaseClass> lists=new ArrayList<CaseClass>();
						for (int i = 0; i < datas.length; i++) {
							CaseClass caseClass1=new CaseClass();
							for (int a = 0; a < list.size(); a++){
								if(Integer.valueOf(datas[i]).intValue()==list.get(a).id){
									caseClass1=list.get(a);
								}
							}
							lists.add(caseClass1);
						}
						meDetailAdapter.setaddlist(lists);
					}
				  }
			    }else{
				   meDetailAdapter.colocelist();
				   meDetailAdapter.setaddlist(addlist);
			    }
			xuanzhe=2;
			meDetailAdapter.setData(list);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(
					MeDetailInfoActivity.this,
					R.anim.activity_translate_in));
			pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
			InputMethodManager inputMethodManager1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager1.hideSoftInputFromWindow(
					MeDetailInfoActivity.this.getCurrentFocus()
							.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
				break;
		case R.id.comment:
			Intent intent1 = new Intent(this, SetDataActivity.class);
			intent1.putExtra(AppTag.TAG_TITLE, "个人简介");
			intent1.putExtra(AppTag.TAG_NUMER, 500);
			intent1.putExtra(AppTag.TAG_TEXT, comment.getText().toString());
			startActivityForResult(intent1, 1);
			break;
		case R.id.item_designing:// 设计理念
			MobclickAgent.onEvent(MeDetailInfoActivity.this,"kControlDesignerInfoGreeting"); // 设计师个人资料设计理念点击
			Intent intent = new Intent(this, SetDataActivity.class);
			intent.putExtra(AppTag.TAG_TITLE, "设计理念");
			intent.putExtra(AppTag.TAG_NUMER, 50);
			intent.putExtra(AppTag.TAG_TEXT, designing.getText().toString());
			startActivityForResult(intent, 0);

			break;
		case R.id.wenhou:// 设计理念
			Intent intent2 = new Intent(this, SetDataActivity.class);
			intent2.putExtra(AppTag.TAG_TITLE, "问候语");
			intent2.putExtra(AppTag.TAG_NUMER, 20);
			intent2.putExtra(AppTag.TAG_TEXT, wenhou_tv.getText().toString());
			startActivityForResult(intent2, 2);

			break;
		case R.id.item_officeTime:// 从业时间
			initmPopupWindowView(getDataPick());
			popupwindow.showAtLocation(findViewById(R.id.lay), Gravity.BOTTOM,
					0, 0);
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
									requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
								}
							}).show();
			break;
		case R.id.item_code:
			if (SJQApp.user != null) {
				SwDesignerDetail desing=SJQApp.userData;
				if(SJQApp.userData!=null){
					if(TextUtils.isEmpty(desing.getLogo()) || TextUtils.isEmpty(desing.getName())||TextUtils.isEmpty(desing.officeTime)
						|| TextUtils.isEmpty(desing.getCost()) || TextUtils.isEmpty(desing.getDesStyle())){
						
						toastShow("请先将头像、名字、从业时间、设计费用、擅长风格等资料进行保存才能认证");
					}else{
						if(shiming_tv.getText().toString().equals("未认证")){
							MobclickAgent.onEvent(MeDetailInfoActivity.this,"kControlDesignerInfoCertifiCation"); //  设计师个人认证点击事件
							startActivityForResult(new Intent(getActivity(), ShenjiActivity.class),15);					
						}else{
							startActivity(new Intent(getActivity(), SmActivity.class));		
						}					
					}					
				}else{
					toastShow("获取用户数据出错");
				}
			}
			break;
		default:
			break;
		}
	}

	protected void selectImage() {
//		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//			ActivityCompat.requestPermissions(MeDetailInfoActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
//			return;
//		} else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//			ActivityCompat.requestPermissions(MeDetailInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
//			return;
//		} else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//			ActivityCompat.requestPermissions(MeDetailInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 222);
//			return;
//		} else {
//			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SDPathUtils.getCachePath(), "temp.jpg")));
//				startActivityForResult(openCameraIntent, 30);
//			} else {
//				Uri imageUri = FileProvider.getUriForFile(MeDetailInfoActivity.this, "com.camera_photos.fileprovider", new File(SDPathUtils.getCachePath(), "temp.jpg"));
//				openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//				startActivityForResult(openCameraIntent, 30);
//			}
//		}
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, RESIZE_REQUEST_CODE);
	}



	// 上传文件
	public void uploadFile(File file) {
		final String url = AppUrl.UPLOAD_FILE;
		RequestParams params = getParams(url);
		params.addBodyParameter("file",file);
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onStart() {
				if (dialog != null) {
					dialog.setCancelable(false);
					dialog.show("上传中");

				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> n) {
				if (dialog == null)
					return;
				dialog.dismiss();
				UpfileBean data = CommonUtils.getDomain(n, UpfileBean.class);
				if (data != null) {
					if (data.success) {
						uploadLogo(data.result.get(0).Url);
					} else {
						toastShow(data.result.get(0).Message);
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				if (dialog != null)
					dialog.dismiss();
				AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
						error.getExceptionCode() + "", "POST", url);
				toastShow("无法连接到服务器，请稍后重试");
			}

		};
		getHttpUtils().send(HttpMethod.POST, AppUrl.API + url, params, callBack);
	}

	// 更新头像
	public void uploadLogo(final String logoUrl) {
		final String url = AppUrl.UPLOAD_LOGO;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("userGuid",SJQApp.user.guid);
			param.put("Logo", logoUrl);
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
				dialog.dismiss();
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
				if (bean != null) {
					if (bean.result.operatResult) {
						toastShow(bean.result.operatMessage);
						SJQApp.userData.setLogo(logoUrl);
						mImageUtil.display(mUserHead, logoUrl);
					}else{
						toastShow(bean.result.operatMessage);
					}
				}
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
		getHttpUtils().send(HttpMethod.PUT, AppUrl.API + url, params, callBack);

	}

	protected void showImage() {
		if (SJQApp.userData != null) {
			Intent intent = new Intent(this, PhotoActivity.class);
			intent.putExtra(PhotoActivity.IMAGE_URL, SJQApp.userData.getLogo());
			startActivity(intent);
		} else {
			toastShow("请稍后...");
		}
	}

	String sexString = "0";

	private String yer;

	// 提交保存到服务器
	private void doSave() {
		MobclickAgent.onEvent(MeDetailInfoActivity.this,"kControlDesignerInfoSave"); //设计师个人中心头像点击
		if (SJQApp.user == null) {
			finish();
			return;
		}
		if (SJQApp.userData == null) {
			toastShow("数据未获取到，请稍候");
			return;
		}
		if (name.getText().toString().length() < 2) {
			name.setError("姓名至少两位数");
			return;
		}
		if (!CommonUtils.checkNumber(tv_number.getText().toString())) {
			toastShow("请输入正确的手机号码");
			return;
		}
		dialog.show();
		int CityID = SJQApp.userData.getCityID(), ParentID = SJQApp.userData.getProvinceID();
		if (cs != null && sc != null && isCheckCity) {
			CityID = cs.CityID;
			ParentID = sc.CityID;
		} 
		final String url = AppUrl.UPDATE_DESIGNER_INFO;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();

		if ("女".equals(sex.getText().toString())) {
			sexString = "0";
		} else if ("男".equals(sex.getText().toString())) {
			sexString = "1";
		}
		param.put("guid",SJQApp.user.guid);
		if (SJQApp.userData != null)
		param.put("logo",SJQApp.userData.getLogo());
		param.put("name",name.getText().toString());
		param.put("sex",sexString);
		param.put("provinceID",ParentID + "");
		param.put("cityID",CityID + "");
		String time = officeTime.getText().toString();
		yer = time;
		if (time.length() >= 1) {
			yer = time.split("年")[0];
			if (yer.equals("20")) {
				yer = "-1";
			}
		}
		LogUtils.d("OfficeTime", yer);
		param.put("officeTime",yer);
		param.put("cost",cost.getText().toString());// "/㎡"
		param.put("mobile",tv_number.getText().toString());//电话
		if(addlistStye.size()==0){
//			param.put("desStyle","");//擅长风格
		}else{
			String LoveStyleID="";
			for (int c = 0; c < addlistStye.size(); c++) {
				if(addlistStye.size()==1 || c==addlistStye.size()-1){
					LoveStyleID +=addlistStye.get(c).id;
				}else{
					LoveStyleID +=addlistStye.get(c).id+",";
				}
			}
			param.put("desStyle",LoveStyleID);//擅长风格
		}
		if(addlist.size()==0){
//			param.put("desArea","");//擅长领域
		}else {
			String StyleID="";
			for (int c = 0; c < addlist.size(); c++) {
				if(addlist.size()==1 || c==addlist.size()-1){
					StyleID +=addlist.get(c).id;
				}else{
					StyleID +=addlist.get(c).id+",";
				}
			}
			param.put("desArea",StyleID);//擅长领域
		}
		param.put("comment",comment.getText().toString());//内容
		param.put("designing",designing.getText().toString());
		param.put("greetings", wenhou_tv.getText().toString());
		StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
		params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> n) {
				if (dialog == null)
					return;
				dialog.dismiss();
				Gson gson = new Gson();
				SwresultBen data=gson.fromJson(n.result, SwresultBen.class);
				if (data != null) {
					toastShow(data.result.operatMessage);
					if (data.result.operatResult) {
						MobclickAgent.onEvent(MeDetailInfoActivity.this,"kControlDesignerInfoValidatedSave"); //设计师个人资料正确保存
						isBack = false;
						if (SJQApp.userData != null) {
							SJQApp.userData.setName(name.getText().toString());
							if(sex.getText().toString().equals("男")){
								SJQApp.userData.setSex("1");
							}else{
								SJQApp.userData.setSex("0");
							}
							SJQApp.userData.officeTime = yer;
							SJQApp.userData.setCost(cost.getText().toString());
							SJQApp.userData.desStyleName = desStyle.getText()
									.toString();
							SJQApp.userData.setComment(comment.getText().toString());
							SJQApp.userData.mobile = tv_number.getText().toString();
							SJQApp.userData.setDesStyle((String) desStyle.getTag());
							SJQApp.userData.setDesArea((String) des_field.getTag());
							SJQApp.userData.desAreaName=des_field.getText().toString();
							SJQApp.userData.setDesigning(designing.getText().toString());
							SJQApp.userData.setFromCityName(fromCity.getText().toString());
							SJQApp.userData.greetings = wenhou_tv.getText()
									.toString();
						}
						finish();
					}

				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				if (dialog == null)
					return;
				dialog.dismiss();
				toastShow("修改失败,请稍后");
				AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
						error.getExceptionCode() + "", "put", url);
			}
		};

		hideInput();
		getHttpUtils()
				.send(HttpMethod.PUT, AppUrl.API + url, params, callBack);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {

			String text = data.getStringExtra(AppTag.TAG_TEXT);
			switch (requestCode) {
			case 0:
				designing.setText(text);
				break;
			case 1:
				comment.setText(text);
				break;
			case 2:
                wenhou_tv.setText(text);
				break;
				
			case 8:
				desStyle.setText(text);
				desStyle.setTag(data
						.getStringExtra(DesStyleListActivity.STYLE_ID));
				break;
			case 15:
				if (data != null) {
				shiming_tv.setText("审核中");
				}
				break;
			case RESIZE_REQUEST_CODE:
//				File temp = new File(SDPathUtils.getCachePath(), "temp.jpg");
//				startPhotoZoom(Uri.fromFile(temp));
				startPhotoZoom(data.getData());
				break;
			case 31:
				if (data != null) {
					setPicToView(data);
				}
				break;
			default:
				break;
			}

		}

	}
	private String localImg;
	/**
	 * 保存裁剪之后的图片数据
	 *
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bitmap bitmap = null;
		byte[] bis = picdata.getByteArrayExtra("bitmap");
		bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
		localImg = System.currentTimeMillis() + ".JPEG";

		if (bitmap != null) {

			SDPathUtils.saveBitmap(bitmap, localImg);
			localImg=SDPathUtils.getCachePath() + localImg;
			uploadFile(new File(localImg));
		}
	}
	/**
	 * 裁剪图片方法实现
	 *
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent(getActivity(), PreviewActivity.class);
		intent.setDataAndType(uri, "image/*");
		startActivityForResult(intent, 31);
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
			                    MobclickAgent.onEvent(MeDetailInfoActivity.this,"kControlDesignerInfoBackAlert","No"); //  设计师个人资料返回按钮点击警告事件否
							} else {
								MobclickAgent.onEvent(MeDetailInfoActivity.this,"kControlDesignerInfoBackAlert","Yes"); //  设计师个人资料返回按钮点击警告事件是
								finish();
							}

						}
					}).show();

		} else {
			finish();
		}

	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MeDetailInfoActivity");
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MeDetailInfoActivity");
	}
	@Override
	public void onBackPressed() {
		doBack();
	}
	protected void onDestroy() {
		super.onDestroy();
		if(pop != null){
			pop.dismiss();
		}
		if(popupwindow != null){
			popupwindow.dismiss();
		}
		mRoot1.setFitsSystemWindows(false);
	}
	@Override
	public void permissionSuccess(int requestCode) {
		super.permissionSuccess(requestCode);
		switch (requestCode) {
			case 0x0002:
				selectImage();
				break;
		}

	}
}
