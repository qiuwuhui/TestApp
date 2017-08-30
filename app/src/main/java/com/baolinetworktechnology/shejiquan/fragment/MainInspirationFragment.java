package com.baolinetworktechnology.shejiquan.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baolinetwkchnology.shejiquan.xiaoxi.CompleteFragment;
import com.baolinetwkchnology.shejiquan.xiaoxi.IssueFragment;
import com.baolinetwkchnology.shejiquan.xiaoxi.ReceiveFragment;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.ChannelActivity;
import com.baolinetworktechnology.shejiquan.activity.CodeActivity;
import com.baolinetworktechnology.shejiquan.activity.HouseTypeActivity;
import com.baolinetworktechnology.shejiquan.activity.SelectCityActivity1;
import com.baolinetworktechnology.shejiquan.activity.SelectDsCityActivity;
import com.baolinetworktechnology.shejiquan.activity.SerchActivity;
import com.baolinetworktechnology.shejiquan.activity.SkipActivity;
import com.baolinetworktechnology.shejiquan.activity.StageActivity;
import com.baolinetworktechnology.shejiquan.adapter.MainPageAdapter;
import com.baolinetworktechnology.shejiquan.adapter.MyPageAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseClassList;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.ColumnListBean;
import com.baolinetworktechnology.shejiquan.draggridview.DragAdapter;
import com.baolinetworktechnology.shejiquan.draggridview.DragGridView;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
import com.baolinetworktechnology.shejiquan.manage.ChanneManage;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.HomeHead;
import com.baolinetworktechnology.shejiquan.view.KoySliding;
import com.baolinetworktechnology.shejiquan.view.ListViewNews;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.google.gson.Gson;
import com.guojisheng.koyview.ChanneView;
import com.guojisheng.koyview.ChanneView.OnChanneClickListener;
import com.guojisheng.koyview.domain.ChannelItem;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;


/**
 * 首页
 * @author JiSheng.Guo
 */
public class MainInspirationFragment extends Fragment implements View.OnClickListener,ViewPager.OnPageChangeListener {
	/** 当前选中的栏目 */
	/** 请求CODE */
	public final static int CHANNELREQUEST = 1;
	private ChanneView channe;
	private ViewPager mViewPage;
	private MainHomeFragment mainHomeFragment;//推荐
	private MainHouseCaseFragment mainHouseCaseFragment;//全屋案例
	private DynamicFragment dynamicfragment;//行业动态
	private DesignArtFragment designfragment;//设计艺术
	private HouseholdLifeFragment householdLifeFragment;//家居生活
	private ConstellationFragment constellationFragment;//风水星座
	private MainDesignerFragment mainDesignerFragment;//设计大师
	private MainHomeCaseFragment mainhomeCaseFragment;//效果图
	private DailyCharFragment dailyCharFragment;

//	private PeriodFragment periodFragment;
	private ArrayList<ChannelItem> mUserChannelList = new ArrayList<ChannelItem>();
	private ArrayList<Integer> Inpx = new ArrayList<Integer>();
	private MyPageAdapter Adapter;
	private View allview,transparentview;
	private TextView dinwei_tv;
	private MyBroadcastReciver mybroad;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// 初始化控件
		View view = View.inflate(getActivity(), R.layout.fragment_inspiration,
				null);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("HomeCaseshow");
		intentFilter.addAction("Dynamicshow");
		intentFilter.addAction("HouseCaseshow");
		intentFilter.addAction("DesignArtshow");
		intentFilter.addAction("HouseholdLifeshow");
		intentFilter.addAction("Constellationshow");
		intentFilter.addAction("MainDesignershow");
		intentFilter.addAction("dingshow");
		intentFilter.addAction("shejiquan");
		intentFilter.addAction("OpusSave");
		intentFilter.addAction("XGTCommentSuCCESS");//效果图列表的评论返回
		intentFilter.addAction("refreshCase");
		mybroad=new MyBroadcastReciver();
		getActivity().registerReceiver(mybroad, intentFilter);
		mViewPage = (ViewPager) view.findViewById(R.id.viewPager);
		dinwei_tv = (TextView) view.findViewById(R.id.dinwei_tv);
		dinwei_tv.setOnClickListener(this);
		initview();
		channe = (ChanneView) view.findViewById(R.id.channe);
		view.findViewById(R.id.ll_more_columns).setOnClickListener(this);
		view.findViewById(R.id.editTex_serch).setOnClickListener(this);
		allview=view.findViewById(R.id.Gofor);
		transparentview=view.findViewById(R.id.transparentview);
		channe.setOnChanneClickListener(new OnChanneClickListener() {

			@Override
			public void onChanneClickListener(View v, int index) {
				if (index == -1) {
					Intent intent_channel = new Intent(getActivity(),
							ChannelActivity.class);
					startActivityForResult(intent_channel, CHANNELREQUEST);
					getActivity().overridePendingTransition(
							R.anim.slide_in_right, R.anim.slide_out_left);
				} else {
					    int Current=0;
					for (int i=0;i< Inpx.size();i++){
						if(index == Inpx.get(i)){
							Current=i;
						}
					}
					if(index !=164){
						mainhomeCaseFragment.colsClassView();
					}
					if(index !=163){
						mainDesignerFragment.colsClassView();
					}
					if(index != 166){
						mainHouseCaseFragment.colsClassView();
					}
					mViewPage.setCurrentItem(Current);
				}
			}
		});
		initTabColumn();
		return view;
	}
	private void initview() {
		mainHomeFragment = new MainHomeFragment();
		dynamicfragment = new DynamicFragment();
		designfragment = new DesignArtFragment();
		mainHouseCaseFragment =new MainHouseCaseFragment();
		householdLifeFragment = new HouseholdLifeFragment();
		constellationFragment = new ConstellationFragment();
		mainDesignerFragment= new MainDesignerFragment();
		mainhomeCaseFragment= new MainHomeCaseFragment();
		dailyCharFragment =new DailyCharFragment();
//		periodFragment= new PeriodFragment();
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(mainHomeFragment);
		fragments.add(mainhomeCaseFragment);
//		fragments.add(mainHouseCaseFragment);
		fragments.add(mainDesignerFragment);
		fragments.add(dynamicfragment);
		fragments.add(designfragment);
		fragments.add(householdLifeFragment);
		fragments.add(constellationFragment);
		fragments.add(dailyCharFragment);
//		fragments.add(periodFragment);
		mViewPage.setOffscreenPageLimit(6);//
		Adapter=new MyPageAdapter(getFragmentManager(),
				fragments);
		mViewPage.setAdapter(Adapter);
		mViewPage.setOnPageChangeListener(this);
		showPoputon();
	}

	/**
	 * 初始化Column栏目项
	 * */
	private void initTabColumn() {
		String colum= CacheUtils.getStringData(getActivity(),AppTag.TJ_Column,null);
		if(colum !=null){
			ColumnListBean columList = CommonUtils.getDomain(colum,
					ColumnListBean.class);
			initTabColumn1(columList);
		}else {
			ChannelItem tab0 = new ChannelItem(142, "推荐", false);
			ChannelItem tab6 = new ChannelItem(164, "效果图", true);
//			ChannelItem tab8 = new ChannelItem(166, "全屋案例", true);
			ChannelItem tab5 = new ChannelItem(163, "设计大师", true);
			ChannelItem tab1 = new ChannelItem(143, "行业动态", true);
			ChannelItem tab2 = new ChannelItem(148, "设计艺术", true);
			ChannelItem tab3 = new ChannelItem(155, "家居生活", true);
			ChannelItem tab4 = new ChannelItem(162, "风水星座", true);
			ChannelItem tab7 = new ChannelItem(165, "一期一会", true);
			mUserChannelList.add(tab0);
			mUserChannelList.add(tab6);
//			mUserChannelList.add(tab8);
			mUserChannelList.add(tab5);
			mUserChannelList.add(tab1);
			mUserChannelList.add(tab2);
			mUserChannelList.add(tab3);
			mUserChannelList.add(tab4);
			mUserChannelList.add(tab7);
			Inpx.add(142);
			Inpx.add(164);
//			Inpx.add(166);
			Inpx.add(163);
			Inpx.add(143);
			Inpx.add(148);
			Inpx.add(155);
			Inpx.add(162);
			Inpx.add(165);
			channe.setData(getActivity(), mUserChannelList);
			ColumnListBean cBean = new ColumnListBean();
			cBean.List = mUserChannelList;
			CacheUtils.cacheStringData(getActivity(), AppTag.TJ_Column, cBean.toString());
		}
	}
	private void initTabColumn1(ColumnListBean BeanList) {
		int indexPost=0;
		if(Inpx.size() != 0){
			indexPost= Inpx.get(mViewPage.getCurrentItem());
		}
		mUserChannelList.clear();
		Inpx.clear();
		for(int i=0;i<BeanList.List.size();i++){
			if(i<8){
				mUserChannelList.add(BeanList.List.get(i));
			}
		}
		channe.setData(getActivity(),mUserChannelList);

		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		for(int i=0;i<mUserChannelList.size();i++){
			ChannelItem item= mUserChannelList.get(i);
			int index = item.getId();
			if(index == 143){
				Inpx.add(143);
				fragments.add(dynamicfragment);
			}else if(index == 148){
				Inpx.add(148);
				fragments.add(designfragment);
			}else if(index == 155){
				Inpx.add(155);
				fragments.add(householdLifeFragment);
			}else if(index == 162){
				Inpx.add(162);
				fragments.add(constellationFragment);
			}else if(index == 142){
				Inpx.add(142);
				fragments.add(mainHomeFragment);
			}else if(index == 163){
				Inpx.add(163);
				fragments.add(mainDesignerFragment);
			}else if(index == 164){
				Inpx.add(164);
				fragments.add(mainhomeCaseFragment);
			}else if(index == 165){
				Inpx.add(165);
				fragments.add(dailyCharFragment);
			}else if(index == 166){
				Inpx.add(166);
				fragments.add(mainHouseCaseFragment);
			}
//			else if(index == 166){
//				Inpx.add(166);
//				fragments.add(periodFragment);
//			}
		}
		Adapter.setData(fragments);
		int Current=0;
		for (int i=0;i< Inpx.size();i++) {
			if (indexPost == Inpx.get(i)) {
				Current = i;
			}
		}
		channe.selectTab(Current);
		mViewPage.setCurrentItem(Current);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.editTex_serch:
			Intent intent2 = new Intent(getActivity(), SerchActivity.class);
			intent2.putExtra(AppTag.TAG_ID, 0);
			getActivity().startActivity(intent2);
			break;
		case R.id.ll_more_columns:
			dataSourceList.clear();
			for (int i = 0; i < mUserChannelList.size(); i++) {
				if(mUserChannelList.get(i).getId() != 142){
					HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
					itemHashMap.put("item_image",mUserChannelList.get(i).getId());
					itemHashMap.put("item_text", mUserChannelList.get(i).getName());
					dataSourceList.add(itemHashMap);
				}
			}
			mDragAdapter.setData(dataSourceList);
			if (Build.VERSION.SDK_INT <24){//7.0bug
				mPopupWindow.showAsDropDown(transparentview,0,0);//
			}else{
				int[] location = new int[2];
				// 获取控件在屏幕的位置
				transparentview.getLocationOnScreen(location);
				mPopupWindow.showAtLocation(transparentview, Gravity.NO_GRAVITY, 0, location[1] + transparentview.getHeight() + 0);
			}
			break;
		case R.id.finsh_img:
			mPopupWindow.dismiss();
			break;
		case R.id.dinwei_tv:
			MobclickAgent.onEvent(getActivity(),"kControlDesignerCity");//首页设计师城市选择点击
			Intent intentCity = new Intent(getActivity(), SelectDsCityActivity.class);
			startActivityForResult(intentCity, 6);
			break;
		case R.id.complete:
			if(complete.getText().toString().equals("排序")){
				mDragGridView.setpaixn();
				complete.setText("完成");
				tv_channel.setText("拖动排序");
			}else if(complete.getText().toString().equals("完成")){
				mDragGridView.stoppaixn();
				complete.setText("排序");
				tv_channel.setText("切换栏目");
				List<HashMap<String, Object>> list=(List<HashMap<String, Object>>) mDragAdapter.getList();
				ArrayList<ChannelItem> ChannelList = new ArrayList<ChannelItem>();
				ChannelItem tab0 = new ChannelItem(142, "推荐", false);
				ChannelList.add(tab0);
				for (int i = 0; i < list.size(); i++) {
					if(i<8){
						ChannelItem tab= new ChannelItem();
						tab.setId((Integer) list.get(i).get("item_image"));
						tab.setName((String) list.get(i).get("item_text"));
						ChannelList.add(tab);
					}
				}
				ColumnListBean columList =new ColumnListBean();
				columList.List=ChannelList;
				initTabColumn1(columList);
				CacheUtils.cacheStringData(getActivity(), AppTag.TJ_Column, columList.toString());
				mPopupWindow.dismiss();
			}
			break;

		}
	}
	private PopupWindow mPopupWindow;
	private DragAdapter mDragAdapter;
	private DragGridView mDragGridView;
	private TextView complete;
	private TextView tv_channel;
	private List<HashMap<String, Object>> dataSourceList = new ArrayList<HashMap<String, Object>>();
	public void showPoputon() {
		View popupView = getActivity().getLayoutInflater().inflate(R.layout.main_insoir_layout, null);
		mPopupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setFocusable(false);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style3);
		mPopupWindow.dismiss();
		View lay_diss = popupView.findViewById(R.id.lay_diss);
		mDragGridView = (DragGridView) popupView.findViewById(R.id.dragGridView);
		complete = (TextView) popupView.findViewById(R.id.complete);
		tv_channel = (TextView) popupView.findViewById(R.id.tv_channel);
		popupView.findViewById(R.id.finsh_img).setOnClickListener(this);
		complete.setOnClickListener(this);
		for (int i = 0; i < mUserChannelList.size(); i++) {
			if(i<8) {
				if (mUserChannelList.get(i).getId() != 142) {
					HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
					itemHashMap.put("item_image", mUserChannelList.get(i).getId());
					itemHashMap.put("item_text", mUserChannelList.get(i).getName());
					dataSourceList.add(itemHashMap);
				}
			}
		}
		mDragAdapter = new DragAdapter(getActivity(), dataSourceList);
		mDragGridView.setAdapter(mDragAdapter);
		lay_diss.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPopupWindow.dismiss();
			}
		});
	}


	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		channe.selectTab(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}
	public void setonLocation(String CityTitle){
		if(dinwei_tv != null){
			City city = SJQApp.getCity();
			if (city != null && !city.Title.equals("")) {
				dinwei_tv.setText(city.Title);
			}else{
				dinwei_tv.setText("全国");
			}
		}
		mainHomeFragment.go2GuideActivity();
		mainDesignerFragment.go2GuideActivity();
	}
	public void setHomeList(String CityTitle){
		mainHomeFragment.go2GuideActivity();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == AppTag.RESULT_OK) {
			if (requestCode == 6) {
				dinwei_tv.setText(data.getStringExtra(AppTag.TAG_TITLE));
				int CityID = data.getIntExtra(AppTag.TAG_ID, 0);
				if (CityID == 310000)
					CityID = 310100;
				CacheUtils.cacheStringData(getActivity(), "CITY_NAME",
						data.getStringExtra(AppTag.TAG_TITLE));
				CacheUtils.cacheIntData(getActivity(), "CITY_ID",
						data.getIntExtra(AppTag.TAG_ID, 0));
				City city=new City(CityID, 0, 0, "", "", data.getStringExtra(AppTag.TAG_TITLE));
				SJQApp.setCity(city);
				mainHomeFragment.go2GuideActivity();
				mainDesignerFragment.refresh(CityID);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private class MyBroadcastReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();//
			if (action.equals("HomeCaseshow")) {
				mainhomeCaseFragment.setDinBu();
			}else if(action.equals("HouseCaseshow")){//
				mainHouseCaseFragment.setDinBu();
			}else if(action.equals("Dynamicshow")){
				dynamicfragment.setDinBu();
			}else if(action.equals("DesignArtshow")){
				designfragment.setDinBu();
			}else if(action.equals("HouseholdLifeshow")){
				householdLifeFragment.setDinBu();
			}else if(action.equals("Constellationshow")){
				constellationFragment.setDinBu();
			}else if(action.equals("MainDesignershow")){
			    mainDesignerFragment.setDinBu();
		    }else if(action.equals("dingshow")){
				mainHomeFragment.setDinBu();
			}else if(action.equals("shejiquan")){
				int Current=0;
				for (int i=0;i< Inpx.size();i++) {
					if (163 == Inpx.get(i)) {
						Current = i;
					}
				}
				channe.selectTab(Current);
				mViewPage.setCurrentItem(Current);
			}//效果图收藏
			else if(action.equals("OpusSave")){
				int position = intent.getIntExtra("POSITION",0);
				String itemstring = intent.getStringExtra("itemString");
				mainhomeCaseFragment.updateitem(position,itemstring);
			}
			else if(action.equals("XGTCommentSuCCESS")){//从效果图列表点击进入
				int position = intent.getIntExtra("POSITION",0);
				int number = intent.getIntExtra("PLNUMBER",0);
				mainhomeCaseFragment.updateitem(position,number);
			}
			if(action.equals("refreshCase")){
				mainhomeCaseFragment.go2GuideActivity();
			}
//			else if(action.equals("DETAILS")){//
//				int position = intent.getIntExtra("POSITION",0);
//				int number = intent.getIntExtra("NUMBER",0);
//				mainhomeCaseFragment.updateitem(position,number);
//			}
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mPopupWindow!=null){
			mPopupWindow.dismiss();
		}
		if(mybroad!=null){
			getActivity().unregisterReceiver(mybroad);
		}
	}
}
