package com.baolinetworktechnology.shejiquan.activity;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.CityAdapter;
import com.baolinetworktechnology.shejiquan.adapter.CommonAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ResustCityAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewHolder;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.net.BaseNet.OnCallBackList;
import com.baolinetworktechnology.shejiquan.net.NetCityList;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.view.BladeView;
import com.baolinetworktechnology.shejiquan.view.BladeView.OnItemClickListener;
import com.baolinetworktechnology.shejiquan.view.PinnedHeaderListView;
import com.guojisheng.koyview.MyEditText;
import com.lidroid.xutils.exception.HttpException;

/**
 * 城市选择
 * 
 * @author JiSheng.Guo
 * 
 */
public class SelectCityActivity extends BaseActivity implements
		OnCallBackList<City> {

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
	private BladeView mLetter;
	private CityAdapter mCityAdapter;
	private CityService mApplication;
	// private InputMethodManager mInputMethodManager;
	private List<City> mCities;
	private Button btnCity;
	private GridView gvCity;
	private CommonAdapter<City> gvCityAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_city);

		initView();
		initData();
		new NetCityList().getHotCityList(this, this);
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
		mLetter = (BladeView) findViewById(R.id.citys_bladeview);
		TextView mTvCity=(TextView) findViewById(R.id.tv_title2);
		//findViewById(R.id.tv_title2).setOnClickListener(this);
		mTvCity.setText("当前城市-"+CacheUtils.getStringData(this, "CITY_NAME",
				"全国"));
		mLetter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(String s) {
				if (mIndexer.get(s) != null) {
					mCityListView.setSelection(mIndexer.get(s) + 1);
				} else {
					mCityListView.setSelection(0);
				}
			}
		});
		//mLetter.setVisibility(View.GONE);
		mCityListView
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position == 0)
							return;
						City city = mCityAdapter.getItem(position - 1);
						Intent data = new Intent();
						data.putExtra(AppTag.TAG_TITLE, city.Title);
						data.putExtra(AppTag.TAG_ID, city.CityID);
						setResult(AppTag.RESULT_OK, data);
						finish();
					}
				});

		View head = View.inflate(this, R.layout.select_city_head, null);
		head.findViewById(R.id.btnAll).setOnClickListener(this);
		gvCity = (GridView) head.findViewById(R.id.gvCity);
		gvCityAdapter = new CommonAdapter<City>(this, R.layout.item_gv_city) {

			@Override
			public void convert(ViewHolder holder, City item) {
				holder.setText(R.id.tv_name, item.Title);

			}
		};
		gvCity.setAdapter(gvCityAdapter);
		btnCity = (Button) head.findViewById(R.id.btnCity);
		btnCity.setOnClickListener(this);
		mCityListView.addHeaderView(head);

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
							finish();
						}

					}

				});
		gvCity.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				City city = gvCityAdapter.getItem(position);
				if (city != null) {
					Intent data = new Intent();
					data.putExtra(AppTag.TAG_TITLE, city.Title);
					data.putExtra(AppTag.TAG_ID, city.ID);
					setResult(AppTag.RESULT_OK, data);
					finish();
				}

			}

		});

		MyEditText myEt = (MyEditText) findViewById(R.id.myEt);
		myEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
					mLetter.setVisibility(View.VISIBLE);
					mCityListView.setVisibility(View.VISIBLE);
					result_list.setVisibility(View.GONE);
				} else {
					mLetter.setVisibility(View.GONE);
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
		// mCityDB = mApplication.getCityDB();
		// mInputMethodManager = (InputMethodManager)
		// getSystemService(INPUT_METHOD_SERVICE);
		if (mApplication == null)
			return;
		if (mApplication.isCityListComplite()) {

			mCities = mApplication.getCityList();
			mSections = mApplication.getSections();
			mMap = mApplication.getMap();

			mPositions = mApplication.getPositions();
			mIndexer = mApplication.getIndexer();
			// String t = mSections.get(0);
			mCityAdapter = new CityAdapter(SelectCityActivity.this, mCities,
					mMap, mSections, mPositions);
			mCityListView.setAdapter(mCityAdapter);
			mCityListView.setOnScrollListener(mCityAdapter);
			mCityListView.setPinnedHeaderView(LayoutInflater.from(
					SelectCityActivity.this).inflate(
					R.layout.biz_plugin_weather_list_group_item, mCityListView,
					false));
			mLetter.setVisibility(View.VISIBLE);

		}

		if (TextUtils.isEmpty(SJQApp.address)) {
			return;
		}
		final List<City> province_list = mApplication.getAllProvince();
		if (province_list == null)
			return;
		new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < province_list.size(); i++) {
					if (SJQApp.address.contains(province_list.get(i).Title)) {

						int ProvinceID = province_list.get(i).CityID;
						List<City> listCity = mApplication.getArea(ProvinceID);
						if (listCity != null) {
							for (int ii = 0; ii < listCity.size(); ii++) {
								if (SJQApp.address.contains(listCity.get(ii).Title)) {
									int CityID = listCity.get(ii).CityID;
									Looper.prepare();
									btnCity.setText(listCity.get(ii).Title);
									btnCity.setTag(CityID);
									Looper.loop();
									return;

								}

							}
						}

						return;
					}
				}

			}
		}).start();
		for (int i = 0; i < province_list.size(); i++) {
			if (SJQApp.address.contains(province_list.get(i).Title)) {
				btnCity.setText(province_list.get(i).Title);
				return;
			}
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
		if(gvCityAdapter != null){
			gvCityAdapter=null;
		}
	}
}
