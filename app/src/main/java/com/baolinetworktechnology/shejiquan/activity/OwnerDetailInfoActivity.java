package com.baolinetworktechnology.shejiquan.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.GteStyeAdapter;
import com.baolinetworktechnology.shejiquan.adapter.LoveStyeAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.OwnerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwOwnerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.UpfileBean;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.net.GetClassList;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.SDPathUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyLinearLayout;
import com.byl.datepicker.citywheelview.AbstractWheelTextAdapter;
import com.byl.datepicker.citywheelview.ArrayWheelAdapter;
import com.byl.datepicker.citywheelview.CityWheelView;
import com.byl.datepicker.citywheelview.OnWheelChangedListener;
import com.google.gson.Gson;
import com.guojisheng.koyview.ActionSheetDialog;
import com.guojisheng.koyview.ActionSheetDialog.OnSheetItemClickListener;
import com.guojisheng.koyview.ActionSheetDialog.SheetItemColor;
import com.guojisheng.koyview.MyEditText;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 业主详细资料
 * 
 * @author JiSheng.Guo
 * 
 */
public class OwnerDetailInfoActivity extends BaseActivity {
	private static final int IMAGE_REQUEST_CODE = 50;// 图片请求码
	private static final int CAMERA_REQUEST_CODE = 10;// 照相请求码
	private static final int RESIZE_REQUEST_CODE = 20;// 返回结果码
	private BitmapUtils mImageUtil;
	private CircleImg mUserHead;
	private TextView tvUserSex,ZhuanUserCity,tv_huxing,tv_liveStye;
	private MyEditText myeUserName,myMianji,myYusuan;
	private String sexString;
	private GteStyeAdapter getStyeapetr;
	private LoveStyeAdapter loveStyeAdapter;
	private MyLinearLayout mRoot1;
	private TextView text_sc;
	private TextView text_fg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(
				R.layout.activity_owner_detail_info, null);
		setContentView(parentView);
		mRoot1 = (MyLinearLayout) findViewById(R.id.mRoot1);
		mRoot1.setFitsSystemWindows(true);
		TextView t2 = (TextView) findViewById(R.id.tv_title2);
		t2.setVisibility(View.VISIBLE);
		t2.setText("保存");
		t2.setOnClickListener(this);
		t2.setFocusable(true);
		t2.setFocusableInTouchMode(true);
		t2.requestFocus();
		geuCase();
		initUI();
		initData();
		getStye();
		getLoveStye();
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
	// 初始化用户数据
	private void initData() {
		if (SJQApp.user == null) {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return;
		}

		if (SJQApp.ownerData == null) {
			toastShow("您的信息获取失败，请稍候重试");
			return;
		}
		SwOwnerDetail owner = SJQApp.ownerData;
		mImageUtil.display(mUserHead, owner.getLogo());

		myeUserName.setText(owner.getName());
		if(owner.getnProvinceID()!=0 && owner.getnCityID()!=0){
			ZhuanUserCity.setText(getfromCity(owner.getnProvinceID(),owner.getnCityID()));
		}else{
			ZhuanUserCity.setText(CacheUtils.getStringData(getActivity(),
					"weizhi", " "));
		}
		ZhuanUserCity.setTag(owner.getnCityID());
		tvUserSex.setText(owner.getSexTitle());
		tv_huxing.setText(gethuxin(owner.getHouseType()));
		tv_liveStye.setText(getloveStye(owner.getLoveStyle()));
		myMianji.setText(owner.getHouseArea());
		myYusuan.setText(owner.getBudget());


	}

	// 初始化UI
	private void initUI() {
		mImageUtil = new BitmapUtils(this);
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		mUserHead = (CircleImg) findViewById(R.id.userHead);
		myeUserName = (MyEditText) findViewById(R.id.myeUserName);
		myeUserName.setHintTextColor(getResources().getColor(R.color.acyivity_bg));
		myMianji = (MyEditText) findViewById(R.id.myMianji);
		myMianji.setHintTextColor(getResources().getColor(R.color.acyivity_bg));
		myYusuan = (MyEditText) findViewById(R.id.myYusuan);
		myYusuan.setHintTextColor(getResources().getColor(R.color.acyivity_bg));
		tvUserSex = (TextView) findViewById(R.id.tvUserSex);
		ZhuanUserCity = (TextView) findViewById(R.id.ZhuanUserCity);
		tv_huxing = (TextView) findViewById(R.id.tv_huxing);
		tv_liveStye = (TextView) findViewById(R.id.tv_liveStye);
		ZhuanUserCity.setTag("");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (SJQApp.user == null) {
			go2Login();
		}
	}
	/**
	 * 更多菜单初始化
	 */
	private PopupWindow popupwindow;
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
	private void go2Login() {
		Toast.makeText(getApplicationContext(), "账户信息失效，请重新登录",
				Toast.LENGTH_SHORT).show();
		startActivity(new Intent(this, LoginActivity.class));
		finish();

	}
	//获取风格,类型信息
	private List<CaseClass> listStye;
	private List<CaseClass> list;
	private List<CaseClass> addlistStye=new ArrayList<>();
	private List<CaseClass> addlist=new ArrayList<>();
	private  void geuCase(){
		list = new GetClassList(this).getList("户型");
		if(list!=null){
			list.remove(0);
		}
		listStye = new GetClassList(this).getList("风格");
		if(listStye!=null){
			listStye.remove(0);
		}
	}
	//获取户型信息
	private  String gethuxin(String str){
		if(list!=null){
			for (int j = 0; j < list.size(); j++) {
				if (str.equals(list.get(j).id + "")) {
					return list.get(j).title;
				}
			}
		}
		return "";
	}
	//获取喜欢风格
	private  String getloveStye(String LoveStyle){
		if(listStye!=null){
			String LoveStyleTitle="";
			if (LoveStyle != null) {
				String[] datas = null;
				if (LoveStyle.indexOf(",") != -1) {
					datas = LoveStyle.split(",");
				}
				if (datas != null && datas.length >= 1) {
					StringBuffer title = new StringBuffer();
					for (int i = 0; i < datas.length; i++) {
						for (int j = 0; j < listStye.size(); j++) {
							if (datas[i].equals(listStye.get(j).id + "")) {
								title.append(listStye.get(j).title);
								if (i != datas.length - 1) {
									title.append(",");
								}
								continue;
							}

						}
					}
					LoveStyleTitle = title.toString();
					return LoveStyleTitle;
				} else {
					for (int j = 0; j < listStye.size(); j++) {
						if (LoveStyle.equals(listStye.get(j).id + "")) {
							LoveStyleTitle = listStye.get(j).title;
							return LoveStyleTitle;
						}

					}
				}
			}
		}
		return "";
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_title2:
			doSave();
			break;
		case R.id.item_head:// 头像
			updateHead();
			break;
		case R.id.item_sex:// 性别
			initmPopupWindowView(getSexPick());
			popupwindow.showAtLocation(findViewById(R.id.lay1), Gravity.BOTTOM,
					0, 0);
//			showSex();
			break;
		case R.id.Zhuan_city://装修资料 城市选择
			initmPopupWindowView(getCity());
			popupwindow.showAtLocation(findViewById(R.id.lay1), Gravity.BOTTOM,
						0, 0);
//			showCitySelectWindow();
			break;
		case R.id.item_huxing:// 房屋户型
			text_sc.setText("请选择您的家居户型 (至多1项)：");
			if(addlist.size()==0){
				if(!SJQApp.ownerData.getHouseType().equals("")){
					CaseClass caseClass=new CaseClass();
					for (int a = 0; a < list.size(); a++){
						if(Integer.valueOf(SJQApp.ownerData.getHouseType()).intValue()==list.get(a).id){
							caseClass=list.get(a);
						}
					}
					getStyeapetr.coloce();
					getStyeapetr.setPosition(caseClass);
				}
			}else{
				getStyeapetr.coloce();
				getStyeapetr.setPosition(addlist.get(0));
			}
			getStyeapetr.setData(list);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(
					OwnerDetailInfoActivity.this,
					R.anim.activity_translate_in));
			pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
					OwnerDetailInfoActivity.this.getCurrentFocus()
							.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		case R.id.love_Stye:// 喜欢风格
			text_fg.setText("请选择您喜欢风格 (至多2项)：");
			if(addlistStye.size()==0){
				if(!SJQApp.ownerData.getLoveStyle().equals("")){
					String LoveStyle=SJQApp.ownerData.getLoveStyle();
					String[] datas = null;
					if (LoveStyle.indexOf(",") != -1) {
						datas = LoveStyle.split(",");
					}else{
						List<CaseClass> list=new ArrayList<CaseClass>();
							for (int a = 0; a < listStye.size(); a++){
								if(Integer.valueOf(SJQApp.ownerData.getLoveStyle()).intValue()==listStye.get(a).id){
									CaseClass caseClass1=new CaseClass();
									caseClass1=listStye.get(a);
									list.add(caseClass1);
								}
							}
						loveStyeAdapter.setaddlist(list);
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
						loveStyeAdapter.setaddlist(list);
					}
				}
			}else{
				loveStyeAdapter.setaddlist(addlistStye);
			}
			loveStyeAdapter.setData(listStye);
			ll_popup1.startAnimation(AnimationUtils.loadAnimation(
						OwnerDetailInfoActivity.this,
						R.anim.activity_translate_in));
			pop1.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
			InputMethodManager inputMethodManager1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager1.hideSoftInputFromWindow(
			OwnerDetailInfoActivity.this.getCurrentFocus()
								.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
				break;
		default:
			break;
		}
	}
	//获取装修公司所在地区省市
	private String getfromCity(int ProvinceID,int CityID){
		CityService cityService = CityService.getInstance(this);
		if (cityService == null)
			return null;
		String shengshi=cityService.fromCity(ProvinceID, CityID);
		return shengshi;
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
					tvUserSex.setText(j[city.getCurrentItem()]);
					popupwindow.dismiss();
				}
			});
			adapter.setTextSize(18);
			city.setViewAdapter(adapter);
			city.setCurrentItem(0);

			return view;
		}
	    boolean NisCheckCity = false;
		List<City> countries = null;
    	City zxsc, zxcs;
		int cityIndex = 1;
		CountryAdapter cityAdapter;
		// 获取城市视图
		private View getCity() {
			NisCheckCity= false;
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
						zxcs = t;

				}
			});
			contentView.findViewById(R.id.relativeOk).setVisibility(View.VISIBLE);
			contentView.findViewById(R.id.ok).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
						if(zxsc != null && zxcs != null){
								NisCheckCity= true;
								ZhuanUserCity.setText(zxsc.Title + "-" +zxcs.Title);

							}
							popupwindow.dismiss();
						}
					});
			country.setCurrentItem(cityIndex);
			city.setCurrentItem(0);

			return contentView;
		}
		/**
		 * 更新城市
		 */
		private void updateCities(CityWheelView city, City c) {
			zxsc = c;
			if (cityAdapter == null) {
				cityAdapter = new CountryAdapter(this);
				cityAdapter.setTextSize(18);
			}
			cityAdapter.setData(getCities(c));
			city.setViewAdapter(cityAdapter);
			city.setCurrentItem(0);
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
			    zxcs = clist.get(0);

			if (zxcs != null) {
				if (zxcs.Title.contains("香港") || zxcs.Title.contains("澳门"))
					return cityService.getArea(zxcs.CityID);
			}

			return clist;
		}
	//获取风格户型
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private View parentView;
	private void getStye() {
		pop = new PopupWindow(OwnerDetailInfoActivity.this);
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
		getStyeapetr = new GteStyeAdapter(this);
		StyeView.setAdapter(getStyeapetr);
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
				if(getStyeapetr.getPosition()==null){
					pop.dismiss();
					ll_popup.clearAnimation();
					addlist.clear();
					tv_huxing.setText("");
					return;
				}
				addlist.clear();
				addlist.addAll(getStyeapetr.getPosition());
				tv_huxing.setText(addlist.get(0).title);
				pop.dismiss();
				ll_popup.clearAnimation();

			}
		});

	}
	private PopupWindow pop1 = null;
	private LinearLayout ll_popup1;
	private void getLoveStye() {
		pop1 = new PopupWindow(OwnerDetailInfoActivity.this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindow_stye,
				null);
		pop1.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		pop1.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		pop1.setBackgroundDrawable(new BitmapDrawable());
		pop1.setFocusable(false);
		pop1.setOutsideTouchable(true);
		pop1.setContentView(view);
		ll_popup1 = (LinearLayout) view.findViewById(R.id.ll_popup);
		text_fg = (TextView) view.findViewById(R.id.text_sc);
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		GridView StyeView = (GridView) view.findViewById(R.id.Stye_GridView);
		loveStyeAdapter = new LoveStyeAdapter(this);
		StyeView.setAdapter(loveStyeAdapter);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop1.dismiss();
				ll_popup1.clearAnimation();
			}
		});
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);

		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(loveStyeAdapter.getPosition()==null){
					addlistStye.clear();
					pop1.dismiss();
					ll_popup1.clearAnimation();
					tv_liveStye.setText("");
					return;
				}
				addlistStye.clear();
				addlistStye.addAll(loveStyeAdapter.getPosition());
				String Positionstr="";
				for (int c = 0; c < addlistStye.size(); c++) {
					if(addlistStye.size()==1 || c==1){
						Positionstr +=addlistStye.get(c).title;
					}else{
						Positionstr +=addlistStye.get(c).title+",";
					}
				}
				tv_liveStye.setText(Positionstr);
				pop1.dismiss();
				ll_popup1.clearAnimation();

			}
		});

	}
	String Yusuan;
	String Mianji;
	// 保存
	private void doSave() {
		if (myeUserName.getText().toString().length() < 2) {
			myeUserName.setError("至少两个字");
			return;
		}
		final String url = AppUrl.UPDATE_PERSON_INFO;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();

		if ("女".equals(tvUserSex.getText().toString())) {
			sexString = "0";
		} else if ("男".equals(tvUserSex.getText().toString())) {
			sexString = "1";
		}
			param.put("guid", SJQApp.user.guid);
		if (SJQApp.userData != null)
			param.put("logo", SJQApp.userData.getLogo());

			param.put("name",myeUserName.getText().toString());
			param.put("sex",sexString);

		int NCityID = SJQApp.ownerData.getnCityID(),
				NParentID = SJQApp.ownerData.getnProvinceID();
		if (zxcs != null && zxcs != null && NisCheckCity) {
			NCityID = zxcs.CityID;
			NParentID = zxsc.CityID;
		}
			param.put("nProvinceID",NParentID+"");
		if (ZhuanUserCity.getTag() != null)
			param.put("nCityID",NCityID+"");
		if(getStyeapetr.getPosition()!=null){
			param.put("houseType",addlist.get(0).id+"");
		}else{
			param.put("houseType",SJQApp.ownerData.getHouseType());
		}
		if(loveStyeAdapter.getPosition()!=null){
			List<CaseClass> list=loveStyeAdapter.getPosition();
			String Positionstr="";
			for (int c = 0; c < list.size(); c++) {
				if(list.size()==1 || c==1){
					Positionstr +=list.get(c).id;
				}else{
					Positionstr +=list.get(c).id+",";
				}
			}
			param.put("loveStyle",Positionstr);
		}else {
			param.put("loveStyle",SJQApp.ownerData.getLoveStyle());
		}
		int saveBitNum = 2;
		if(!TextUtils.isEmpty(myMianji.getText().toString())){
			if(!puanduan(myMianji.getText().toString(),"面积")){
				return;
			}
			BigDecimal b = new BigDecimal(Double.valueOf(myMianji.getText().toString()));
			//四舍五入,保留两个小数.
			double c = b.setScale(saveBitNum , BigDecimal.ROUND_HALF_UP).doubleValue();
			Mianji = String.valueOf(c);
			param.put("houseArea",Mianji);
		}
		if(!TextUtils.isEmpty(myYusuan.getText().toString())){
			if(!puanduan(myYusuan.getText().toString(),"预算")){
				return;
			}
			BigDecimal b1 = new BigDecimal(Double.valueOf(myYusuan.getText().toString()));
			double c1 = b1.setScale(saveBitNum , BigDecimal.ROUND_HALF_UP).doubleValue();
			Yusuan = String.valueOf(c1);
			param.put("budget",Yusuan);
		}

		StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
		params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		dialog.show();
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> n) {
				dialog.dismiss();
				Gson gson = new Gson();
				SwresultBen data=gson.fromJson(n.result, SwresultBen.class);
				if (data != null) {
					toastShow(data.result.operatMessage);
					if (data.result.operatResult) {
						if (SJQApp.ownerData != null) {
							SJQApp.ownerData.setName(myeUserName.getText()
									.toString());
							if(tvUserSex.getText().toString().equals("男")){
								SJQApp.ownerData.setSex(1);
							}else{
								SJQApp.ownerData.setSex(0);
							}
							SJQApp.ownerData.setHouseArea(Mianji);
							SJQApp.ownerData.setBudget(Yusuan);
							int NCityID = SJQApp.ownerData.getnCityID(),
									NParentID = SJQApp.ownerData.getnProvinceID();
							if (zxcs != null && zxcs != null && NisCheckCity) {
								NCityID = zxcs.CityID;
								NParentID = zxsc.CityID;
							}
							SJQApp.ownerData.setnCityID(NCityID);
							SJQApp.ownerData.setnProvinceID(NParentID);
							if(addlist.size()==0){
								SJQApp.ownerData.setHouseType(SJQApp.ownerData.getHouseType());
							}else{
								SJQApp.ownerData.setHouseType(addlist.get(0).id+"");
							}
							if(addlistStye.size()==0){
								SJQApp.ownerData.setLoveStyle(SJQApp.ownerData.getLoveStyle());
							}else{
								String LoveStyleID="";
								for (int c = 0; c < addlistStye.size(); c++) {
									if(addlistStye.size()==1 || c==1){
										LoveStyleID +=addlistStye.get(c).id;
									}else{
										LoveStyleID +=addlistStye.get(c).id+",";
									}
								}
								SJQApp.ownerData.setLoveStyle(LoveStyleID);
							}
						}
						finish();
					}

				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				dialog.dismiss();
				toastShow("修改失败,请稍后");
				AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
						error.getExceptionCode() + "", "POST", url);
			}
		};
		getHttpUtils()
				.send(HttpMethod.PUT, AppUrl.API + url, params, callBack);
	}

	// 修改头像
	private void updateHead() {
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

	}

	// 查看头像
	private void showImage() {
		if (SJQApp.ownerData != null) {
			Intent intent = new Intent(this, PhotoActivity.class);
			intent.putExtra(PhotoActivity.IMAGE_URL, SJQApp.ownerData.getLogo());
			startActivity(intent);
		} else {
			toastShow("请稍后...");
		}

	}

	// 外部存储卡是否可用
	private boolean isSdcardExisting() {
		final String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	// 选择图片
	private void selectImage() {
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
						SJQApp.ownerData.setLogo(logoUrl);
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

	private Uri getImageUri() {
		return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
				"header.jpg"));
	}

	private void showResizeImage(Intent data) {
		if(data!=null){
			Uri path=data.getData();
			Uri fileUri = convertUri(path);
			try {
				Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
				uploadFile(CommonUtils.saveMyBitmap(photo, "head"));
			} catch (IOException e) {
				e.printStackTrace();
			}
//			Bundle extras = data.getExtras();
//			if (extras != null) {
//				Bitmap photo = extras.getParcelable("data");
//				uploadFile(CommonUtils.saveMyBitmap(photo, "head"));
//			}
		}
	}
	private Uri convertUri(Uri uri){
		InputStream is = null;
		try {
			is = getContentResolver().openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			is.close();

			//将Bitmap类型转换为uri，将Bitmap中的数据写入到sdCard中的图像文件中，然后返回该图像的
			//uri,这样即实现将图片保存到sdcard中又实现了将content类型uri转换为file类型的uri
			return saveBitmap(bitmap);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private Uri saveBitmap(Bitmap bm){
		//获取sdcard中的一个路径
		File tmpDir = new File(Environment.getExternalStorageDirectory()+"/avater");
		if(tmpDir.exists()){
			tmpDir.mkdir();
		}
		File img = new File(tmpDir.getAbsolutePath()+"avater.png");
		try {
			FileOutputStream fos = new FileOutputStream(img);

			//将图像的数据写入该输出流中，第一个参数是要压缩的格式，第二个参数：图片的质量
			bm.compress(Bitmap.CompressFormat.PNG,85,fos);

			fos.flush();
			fos.close();
			return Uri.fromFile(img);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case CAMERA_REQUEST_CODE:// 照相请求码
				if (isSdcardExisting()) {
					showResizeImage(data);
//					resizeImage(getImageUri());
				} else {
					toastShow("外部存储不可用，无法存储照片");
				}
				break;

			case RESIZE_REQUEST_CODE:// 返回结果码
				startPhotoZoom(data.getData());
				break;

			case 31:
				if (data != null) {
					setPicToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("OwnerDetailInfoActivity");
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("OwnerDetailInfoActivity");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(popupwindow != null){
			popupwindow.dismiss();
		}
		mRoot1.setFitsSystemWindows(false);
		dialog = null;
	}
	private boolean puanduan(String str,String Show){
		String aa=str.substring(str.length()-1);
		if(aa.equals(".")){
			if(Show.equals("面积")){
				toastShow(Show+"输入格式错误");
			}else{
				toastShow(Show+"输入格式错误");
			}
			return false;
		}
		String aaa=str.replace(".","");
		int a=str.length()-aaa.length();
		if(a!=0 && a!=1){
			if(Show.equals("面积")){
				toastShow(Show+"输入格式错误");
			}else{
				toastShow(Show+"输入格式错误");
			}
			return false;
		}
		return true;
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
