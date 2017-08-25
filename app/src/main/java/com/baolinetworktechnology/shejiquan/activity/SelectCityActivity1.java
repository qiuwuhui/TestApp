package com.baolinetworktechnology.shejiquan.activity;

import java.util.List;
import java.util.Map;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.adapter.CityAdapter;
import com.baolinetworktechnology.shejiquan.adapter.CommonAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ResustCityAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewHolder;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.net.NetCityList;
import com.baolinetworktechnology.shejiquan.net.BaseNet.OnCallBackList;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.view.BladeView;
import com.baolinetworktechnology.shejiquan.view.PinnedHeaderListView;
import com.baolinetworktechnology.shejiquan.view.BladeView.OnItemClickListener;
import com.guojisheng.koyview.MyEditText;
import com.lidroid.xutils.exception.HttpException;

import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectCityActivity1 extends BaseActivity implements
OnCallBackList<City>{
	// 首字母集
		private List<String> mSections;
		// 根据首字母存放数据
		private Map<String, List<City>> mMap;
		// 首字母位置集
		private List<Integer> mPositions;
		// 首字母对应的位置
		private Map<String, Integer> mIndexer;
		// private CityDB mCityDB;
		private PinnedHeaderListView mCityListView;
		ListView result_list;
		ResustCityAdapter resultCityAda;
//		private BladeView mLetter;
		private CityAdapter mCityAdapter;
		private CityService mApplication;
		private List<City> mCities;
		private Button btnCity;
		private CommonAdapter<City> gvCityAdapter;
		private String title;
	    private MyEditText myEt;
	    private TextView dingwei_cg;
//		private City city1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_city_activity1);
		
		initView();
		initData();
//		new NetCityList().getHotCityList(this, this);
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
	private void initView() {

		mCityListView = (PinnedHeaderListView) findViewById(R.id.citys_list);
//		mLetter = (BladeView) findViewById(R.id.citys_bladeview);
		TextView mTvCity=(TextView) findViewById(R.id.tv_title2);
		//findViewById(R.id.tv_title2).setOnClickListener(this);
		mTvCity.setText("城市定位");
		dingwei_cg=(TextView) findViewById(R.id.dingwei_cg);
		TextView dingwei_sb=(TextView) findViewById(R.id.dingwei_sb);
		dingwei_cg.setOnClickListener(this);
        title = CacheUtils.getStringData(SelectCityActivity1.this, "dingweics", "");
		if (!title.equals("")) {
			dingwei_cg.setText(title);
			dingwei_sb.setVisibility(View.GONE);
		}else{
			dingwei_cg.setText("未开启定位服务");
			dingwei_sb.setVisibility(View.VISIBLE);			
		}
//		mLetter.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(String s) {
//				if (mIndexer.get(s) != null) {
//					mCityListView.setSelection(mIndexer.get(s) + 1);
//				} else {
//					mCityListView.setSelection(0);
//				}
//			}
//		});
		mCityListView
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						City city = mCityAdapter.getItem(position);
//						CacheUtils.cacheStringData(SelectCityActivity1.this, "getAddress",city.Title);
//						SJQApp.setCity(city);
						CityService mApplication=CityService.getInstance(SelectCityActivity1.this);
						mApplication.getCityDB().getAllCityQU();					
						Intent data = new Intent();
						data.putExtra(AppTag.TAG_TITLE, city.Title);
						data.putExtra(AppTag.TAG_ID, city.CityID);
						setResult(AppTag.RESULT_OK, data);
						finish();
					}
				});
		result_list = (ListView) findViewById(R.id.result_list);
		resultCityAda = new ResustCityAdapter();
		result_list.setAdapter(resultCityAda);
		result_list
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						City city = resultCityAda.getItem(position);
						if (city != null) {
							Intent data = new Intent();
							data.putExtra(AppTag.TAG_TITLE, city.Title);					
							data.putExtra(AppTag.TAG_ID, city.CityID);
							setResult(AppTag.RESULT_OK, data);
							InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(myEt.getWindowToken(), 0); //强制隐藏键盘

							finish();
						}

					}

				});

		myEt = (MyEditText) findViewById(R.id.myEt);
		myEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
//					mLetter.setVisibility(View.VISIBLE);
					mCityListView.setVisibility(View.VISIBLE);
					result_list.setVisibility(View.GONE);
				} else {
//					mLetter.setVisibility(View.GONE);
					mCityListView.setVisibility(View.GONE);
					result_list.setVisibility(View.VISIBLE);
					List<City> res = mApplication.getResultCityList(s
							.toString());
					resultCityAda.setData(res);
					resultCityAda.notifyDataSetChanged();

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void initData() {
		mApplication = CityService.getInstance(this);
		if (mApplication == null)
			return;
		if (mApplication.isCityListComplite()) {

			mCities = mApplication.getCityList();
			mSections = mApplication.getSections();
			mMap = mApplication.getMap();

			mPositions = mApplication.getPositions();
			mIndexer = mApplication.getIndexer();
			mCityAdapter = new CityAdapter(SelectCityActivity1.this, mCities,
					mMap, mSections, mPositions);
			mCityListView.setAdapter(mCityAdapter);
			mCityListView.setOnScrollListener(mCityAdapter);
			mCityListView.setPinnedHeaderView(LayoutInflater.from(
					SelectCityActivity1.this).inflate(
					R.layout.biz_plugin_weather_list_group_item, mCityListView,
					false));
//			mLetter.setVisibility(View.VISIBLE);

		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnAll:
		case R.id.tv_title2:
			Intent data = new Intent();
			data.putExtra(AppTag.TAG_TITLE, "全国");
			data.putExtra(AppTag.TAG_ID, 0);
			setResult(AppTag.RESULT_OK, data);
			finish();
			break;
		case R.id.btnCity:
			if (btnCity.getTag() == null) {
				toastShow("定位失败，请稍候");
			} else {
				Intent intent = new Intent();
				intent.putExtra(AppTag.TAG_TITLE, btnCity.getText().toString());
				intent.putExtra(AppTag.TAG_ID, (Integer) btnCity.getTag());
				setResult(AppTag.RESULT_OK, intent);
				finish();
			}
			break;
		case R.id.dingwei_cg:
			if(dingwei_cg.getText().toString().equals("未开启定位服务")){
				Toast.makeText(this,"当前定位失败...",Toast.LENGTH_SHORT).show();
				return;
			}
			CacheUtils.cacheStringData(SelectCityActivity1.this, "getAddress",title);
			CityService mApplication=CityService.getInstance(SelectCityActivity1.this);
			int dingweiID=mApplication.getCityDB().getTJCityID(title);	
			mApplication.getCityDB().getAllCityQU();					
			Intent data1 = new Intent();
			data1.putExtra(AppTag.TAG_TITLE,title);
			data1.putExtra(AppTag.TAG_ID, dingweiID);			
			City city=new City(dingweiID, 0, 0, "", "", title);
			SJQApp.setCity(city);
			setResult(AppTag.RESULT_OK, data1);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onNetStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetSuccess(List<City> data) {
		if (data != null && data.size() > 0) {
			if (gvCityAdapter == null)
				return;
			gvCityAdapter.setData(data);
			gvCityAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onNetFailure(String mesa) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetError(HttpException arg0, String mesa) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetError(String json) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		gvCityAdapter=null;
		InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(myEt.getWindowToken(), 0); //强制隐藏键盘
	}
}
