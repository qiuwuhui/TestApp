package com.baolinetworktechnology.shejiquan;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.SelectCityActivity1;
import com.baolinetworktechnology.shejiquan.activity.SerchActivity;
import com.baolinetworktechnology.shejiquan.activity.WeizxActivity;
import com.baolinetworktechnology.shejiquan.activity.MyFragmentActivity.IBackPressed;
import com.baolinetworktechnology.shejiquan.adapter.OpusPopuWindowAdapter.PopuWindowListener;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.Casezx;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.net.NetCaseSearch;
import com.baolinetworktechnology.shejiquan.net.BaseNet.OnCallBackList;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.view.GvPopuWindow;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.OpusPopuWindow;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.lidroid.xutils.exception.HttpException;

/**
 * 装修公司列表
 *
 * @author JiSheng.Guo
 */
public class ZhuanXiuFragment extends Fragment implements IBackPressed,
        OnCheckedChangeListener, PopuWindowListener, OnClickListener, ILoadData, OnCallBackList<Casezx>, AMapLocationListener, LocationSource {
    private OnLocationChangedListener mListener = null;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private GvPopuWindow gvPopuWindow;
    private CheckBox mCheckBox1;
    private CheckBox mCheckBox2;
    private int mPreId;
    private RefreshListView mCaseListView;
    private TextView weizhi_str;
    public OpusPopuWindow mOpusPopuWindow;// 弹窗
    private PopuWindowListener mPopuWindowListener;
    private int CITY_CODE = 5;
    private int ProvinceID = 0;
    private int CityID;
    private int AreaID;
    private int BusinessOrder = 20;
    private NetCaseSearch mNetCaseSearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = View.inflate(getActivity(),
                R.layout.zhuan_xiu_layout, null);
        CacheUtils.cacheStringData(getActivity(), "getAddress", "");
        initView(view);
        mNetCaseSearch = new NetCaseSearch();
        if (mOpusPopuWindow != null) {
            mOpusPopuWindow.setOnItemClickListener(mPopuWindowListener);
        }
        loadDataClass();
        return view;
    }

    private void loadDataClass() {
        mPopuWindowListener = new PopuWindowListener() {
            @Override
            public void onItemClickListener(int id, int position, String s) {
                CaseClass item = mOpusPopuWindow.getItem(position);
                if (mCheckBox1.isChecked()) {
                    switch (position) {
                        case -1:

                            break;
                        case -11:
                            go2CitySelect();
                            break;
                        default:
                            if (position == 310000)
                                position = 310100;

                            setClassID(position, s);
                            mCaseListView.setRefreshing();
                            mCheckBox1.setText(s);
                            break;
                    }
                }
                colsClassView();
            }

            @Override
            public void onClosePopuWindow() {
                colsClassView();

            }
        };
        mOpusPopuWindow.setOnItemClickListener(mPopuWindowListener);
    }

    private void initView(View view) {
        dialog = new MyDialog(getActivity());
        TextView tVtitle = (TextView) view.findViewById(R.id.tv_title);
        tVtitle.setText("同城装修公司");
        view.findViewById(R.id.back).setOnClickListener(this);
        view.findViewById(R.id.ib_serch).setOnClickListener(this);
        view.findViewById(R.id.desigo).setOnClickListener(this);
        mOpusPopuWindow = new OpusPopuWindow(getActivity());
        weizhi_str = (TextView) view.findViewById(R.id.weizhi_str);
        weizhi_str.setText(CacheUtils.getStringData(getActivity(),
                "Address", "定位中"));
        ImageView shuaxin_img = (ImageView) view.findViewById(R.id.shuaxin_img);
        shuaxin_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    dialog.show();
                    initLoc();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    //没有权限
                    dialog.dismiss();
                    weizhi_str.setText("定位失败，请开启定位权限");
                    Toast.makeText(getActivity(), "请开启定位权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gvPopuWindow = new GvPopuWindow(getActivity());
        gvPopuWindow.setOnItemClickListener(this);
        mCheckBox1 = (CheckBox) view.findViewById(R.id.cb1);
        mCheckBox2 = (CheckBox) view.findViewById(R.id.cb2);
        mCheckBox1.setOnCheckedChangeListener(this);
        mCheckBox1.setOnClickListener(this);
        mCheckBox2.setOnCheckedChangeListener(this);
        mCheckBox2.setOnClickListener(this);
        List<City> data = new ArrayList<City>();
        data.add(new City(20, 0, 0, "", "", "热门排序"));
//		data.add(new City(15, 0, 0, "", "", "离我最近"));
        data.add(new City(21, 0, 0, "", "", "作品最多"));
        gvPopuWindow.setSortData(data);
        gvPopuWindow.initSortView();
        gvPopuWindow.numColumns(4);
        mCaseListView = (RefreshListView) view.findViewById(R.id.listViewCase);
        mCaseListView.getPulldownFooter().isShowBottom(true);
        mCaseListView.setOnLoadListener(this);
        mAdapter = new ZhuanXiuAdaoter(getActivity());
        mCaseListView.setAdapter(mAdapter);
//		listvew点击事件
        oncListview();
        City city = SJQApp.getCity();
        if (city != null) {
            setClassID(city.CityID, "全城");
            mCheckBox1.setText(city.Title);
        } else {
            setClassID(0, "");
        }
        mCheckBox1.setText("全城");
        mCaseListView.setOnRefreshing();
    }

    private void oncListview() {
        mCaseListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Casezx casezx = mAdapter.getItem(position - 1);
                if (casezx == null)
                    return;
                Intent intent = new Intent(getActivity(), WeizxActivity.class);
                intent.putExtra("businessID", casezx.getId() + "");
                intent.putExtra("GUID", casezx.getGUID() + "");
                getActivity().startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void lazyLoad() {
        if (mCaseListView != null)
            mCaseListView.setRefreshing();
    }

    @Override
    public void backPressed() {
        onClosePopuWindow();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mPreId = buttonView.getId();
            switch (buttonView.getId()) {
                case R.id.cb1:
                    mCheckBox2.setChecked(false);
                    if (!mCheckBox1.isChecked()) {
                        colsClassView();
                    } else {
                        mCheckBox1.setChecked(true);
                        mCheckBox2.setChecked(false);
                        mOpusPopuWindow.showCityqu();
                        openClassView();
                    }
                    break;
                case R.id.cb2:
                    mCheckBox1.setChecked(false);
                    gvPopuWindow.ShowCity(mCheckBox2);
                    break;
                default:
                    break;
            }

        } else {
            if (mPreId == buttonView.getId())
                onClosePopuWindow();
        }

    }

    @Override
    public void onItemClickListener(int id, int position, String s) {
        if (mCheckBox1.isChecked()) {
            if (id == 0) {
                mCheckBox1.setText("公装空间");
            } else {
                mCheckBox1.setText(s);
            }
            mCaseListView.setRefreshing();
        } else {
            mCheckBox2.setText(s);
            setOrderBy(id);
            mCaseListView.setRefreshing();
        }

        onClosePopuWindow();
    }

    // 关闭弹窗
    @Override
    public void onClosePopuWindow() {
        if (mCheckBox1 == null)
            return;
        mCheckBox1.setChecked(false);
        mCheckBox2.setChecked(false);
        if (gvPopuWindow != null)
            gvPopuWindow.dismiss();

    }

    public void setTabClick() {
        int position = mCaseListView.getRefreshableView()
                .getFirstVisiblePosition();
        if (position == 0) {
            mCaseListView.setRefreshing();
        } else {
            if (position > 4) {
                mCaseListView.getRefreshableView().setSelection(4);
            }
            mCaseListView.getRefreshableView().smoothScrollToPosition(0);
        }

    }

    boolean isLoad = false;
    private ZhuanXiuAdaoter mAdapter;

    public void setOnRefreshing() {
        if (!isLoad) {
            if (mCaseListView != null) {
                mCaseListView.setOnRefreshing();
                isLoad = true;
            }
        }
    }

    // 关闭弹窗
    private void colsClassView() {
        mCheckBox1.setChecked(false);
        mCheckBox2.setChecked(false);
        mOpusPopuWindow.dismiss();
    }

    // 显示弹窗
    private void openClassView() {
        mOpusPopuWindow.show(mCheckBox2);
    }

    private void go2CitySelect() {
        Intent intentCity = new Intent(getActivity(), SelectCityActivity1.class);
        startActivityForResult(intentCity, CITY_CODE);
    }

    public void setClassID(int classID, String str) {
        if (str.equals("全城")) {
            CityID = classID;
            AreaID = 0;
        } else {
            CityID = 0;
            AreaID = classID;
        }
        // loadData(true);
    }

    public void setOrderBy(int id) {
        BusinessOrder = id;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.ib_serch:
                Intent intent2 = new Intent(getActivity(), SerchActivity.class);
                intent2.putExtra(AppTag.TAG_ID, 1);
                getActivity().startActivity(intent2);
                break;
            case R.id.cb1:
                mCheckBox2.setChecked(false);
                if (!mCheckBox1.isChecked()) {
                    colsClassView();
                } else {
                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(false);
                    mOpusPopuWindow.showCityqu();
                    openClassView();
                }
                break;
            case R.id.cb2:
                mCheckBox1.setChecked(false);
                if (!mCheckBox2.isChecked()) {
                    mCheckBox1.setChecked(false);
                    mCheckBox2.setChecked(false);
                    gvPopuWindow.dismiss();
                } else {
                    mCheckBox1.setChecked(false);
                    mCheckBox2.setChecked(true);
                    mOpusPopuWindow.dismiss();
                    gvPopuWindow.ShowCity(mCheckBox2);
                }
                break;
            case R.id.desigo:
                setTabClick();
                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AppTag.RESULT_OK) {
            if (requestCode == CITY_CODE) {
                mOpusPopuWindow.setCity();
                setClassID(SJQApp.getCity().CityID, "全城");
                mCaseListView.setRefreshing();
                mCheckBox1.setText("全城");
                mOpusPopuWindow.show(mCheckBox1);
                mCheckBox1.setChecked(true);
                mCheckBox2.setChecked(false);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int PageIndex = 1;
    private MyDialog dialog;

    @Override
    public void loadData(boolean isRefresh) {
        if (isRefresh) {
            PageIndex = 1;

        } else {
            PageIndex++;
            if (mNetCaseSearch.isLoading)
                return;
        }
        mNetCaseSearch.SearchCase1(this, "ProvinceID", ProvinceID + "", "CityID",
                CityID + "", "AreaID", AreaID + "", "BusinessOrder", BusinessOrder + "", "PageSize", "10",
                "PageIndex", PageIndex + "", "IsRefresh", "true");
    }

    @Override
    public void onNetStart() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNetSuccess(List<Casezx> data) {
        mCaseListView.setOnComplete();
        if (PageIndex == 1) {
            if (data == null || data.size() == 0) {
                mCaseListView.setOnNullData("抱歉，没有找到您要的装修公司");
            }
            if (mAdapter != null)
                mAdapter.setData(data);

        } else {
            if (data == null || data.size() == 0) {
                mCaseListView.setOnNullNewsData();
            }
            if (mAdapter != null)
                mAdapter.addData(data);

        }
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNetFailure(String mesa) {
        mCaseListView.setOnComplete();
        mCaseListView.setOnFailure();

    }

    @Override
    public void onNetError(HttpException arg0, String mesa) {
        mCaseListView.setOnComplete();
        mCaseListView.setOnNullData("加载失败");

    }

    @Override
    public void onNetError(String json) {
        mCaseListView.setOnComplete();
        mCaseListView.setOnNullData("加载失败");
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
        dialog.dismiss();
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                // 获取位置信息
                String str = amapLocation.getCity();//城市信息
                String str1 = amapLocation.getAddress();
                Double geoLat = amapLocation.getLatitude();
                Double geoLng = amapLocation.getLongitude();
                if (str.equals("")) {
                    weizhi_str.setText("定位失败");
                } else {
                    SJQApp.location.geoLat = geoLat;
                    SJQApp.location.geoLng = geoLng;
                    weizhi_str.setText(str1);
                    CityService mApplication = CityService.getInstance(getActivity());
                    int dingweiID = mApplication.getCityDB().getTJCityID(str);
                    City city = new City(dingweiID, 0, 0, "", "", str);
                    SJQApp.setCity(city);
                    CacheUtils.cacheStringData(getActivity(), "getAddress", str);
                    mApplication.getCityDB().getAllCityQU();
                    setClassID(SJQApp.getCity().CityID, "全城");
                    mCaseListView.setRefreshing();
                    mCheckBox1.setText("全城");
                    mOpusPopuWindow.setCity();
                }

            }
        }
    }

    @Override
    public void onDestroy() {
//		mLocationClient.stopLocation();
        super.onDestroy();
    }

    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
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
        mLocationOption.setInterval(2000000 * 60);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
}
