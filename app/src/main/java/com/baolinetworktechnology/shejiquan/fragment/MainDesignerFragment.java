package com.baolinetworktechnology.shejiquan.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.MyFragmentActivity.IBackPressed;
import com.baolinetworktechnology.shejiquan.activity.SelectCityActivity;
import com.baolinetworktechnology.shejiquan.activity.SelectCityActivity1;
import com.baolinetworktechnology.shejiquan.activity.SerchActivity;
import com.baolinetworktechnology.shejiquan.activity.SkipActivity;
import com.baolinetworktechnology.shejiquan.adapter.OpusPopuWindowAdapter.PopuWindowListener;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.view.ListDesigner;
import com.baolinetworktechnology.shejiquan.view.OpusPopuWindow;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.umeng.analytics.MobclickAgent;

/**
 * 首页-设计大师
 * 
 * @author JiSheng.Guo
 * 
 */
public class MainDesignerFragment extends BaseMainFragment implements
		IBackPressed {
	private int CITY_CODE = 5;
	private int CITY_CODE1 = 6;
	// private boolean mIsPrepared;
	private ListDesigner mDesignerListView;
	// private String CACE_KEY = "DesignerFragment";
	private CheckBox mCBDesStyle, mCBWorkYear, mCbSort;//cbCost
	private OnStateListener mOnStateListener;
	private TextView tVtitle;
	private OpusPopuWindow mOpusPopuWindow;
	private PopuWindowListener mPopuWindowListener;
	private boolean isChenShi;
	List<CaseClass> spceList;
	private View view;
	private ImageView ding_bu;
	public void setonStateListener(OnStateListener onStateListener) {
		this.mOnStateListener = onStateListener;
		if (mDesignerListView != null)
			mDesignerListView.setonStateListener(mOnStateListener);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (view ==null){
			view = inflater.inflate(R.layout.fragment_designer, container,
					false);
			initView(view);//
			LoadData();
		}
		return view;
	}

	private void LoadData() {
		if (mOpusPopuWindow != null) {
			mOpusPopuWindow.setOnItemClickListener(mPopuWindowListener);
		}

		loadDataClass();
		go2GuideActivity();
	}

	private void initView(View view) {
		ding_bu = (ImageView) view.findViewById(R.id.ding_bu);
		ding_bu.setOnClickListener(this);
		mDesignerListView = (ListDesigner) view
				.findViewById(R.id.myListView);
		mDesignerListView.getPulldownFooter().isShowBottom(true);
		mDesignerListView.setonStateListener(mOnStateListener);
		mCBDesStyle = (CheckBox) view.findViewById(R.id.cbDesStyle);
//		mCBCost = (CheckBox) view.findViewById(R.id.cbCost);
		mCBWorkYear = (CheckBox) view.findViewById(R.id.cbSpace);
		mCbSort = (CheckBox) view.findViewById(R.id.cbSort);
		tVtitle = (TextView) view.findViewById(R.id.tv_title);
		mOpusPopuWindow = new OpusPopuWindow(getActivity());
		view.findViewById(R.id.back).setOnClickListener(this);
		
		view.findViewById(R.id.ib_serch).setOnClickListener(this);
		view.findViewById(R.id.title_near).setOnClickListener(this);
		view.findViewById(R.id.desigo).setOnClickListener(this);
		// mTvCity.setOnClickListener(this);
		mCBDesStyle.setOnClickListener(this);
//		mCBCost.setOnClickListener(this);
		mCBWorkYear.setOnClickListener(this);
		mCbSort.setOnClickListener(this);
		tVtitle.setText("设计师");
		// 设置提示消息
		ILoadingLayout endLabels = mDesignerListView.getLoadingLayoutProxy(
				false, true);
		endLabels.setPullLabel("上拉加载更多");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入中...");// 刷新时
		endLabels.setReleaseLabel("放开加载更多");// 下来达到一定距离时，显示的提示
		// mIsPrepared = true;
	}
	private CountDownTimer time;
	public void go2GuideActivity() {
		City city = SJQApp.getCity();
		CityService mApplication= CityService.getInstance(getActivity());
		if (city != null && !city.Title.equals("")) {
			int TJCityID = mApplication.getTJCityID(city.Title);
			mDesignerListView.setCity2(TJCityID);
//			mCBCost.setText(city.Title);
		}else{
			mDesignerListView.setCity2(0);
//			mCBCost.setText("全国");
		}
		mDesignerListView.setCaceKey("shejishi");

		time = new CountDownTimer(500, 500) {

			@Override
			public void onTick(long millisUntilFinished) {

			}
			@Override
			public void onFinish() {
				mDesignerListView.setOnRefreshing();
			}
		};
		time.start();
	}

	@Override
	protected void onVisible() {
		// MobclickAgent.onPageStart("DesignerFragment"); // 统计页面
		super.onVisible();
	}

	@Override
	protected void onInvisible() {
		// MobclickAgent.onPageEnd("DesignerFragment");
		super.onInvisible();
	}

	@Override
	protected void lazyLoad() {

	}

	private void loadDataClass() {
		mPopuWindowListener = new PopuWindowListener() {
			@Override
			public void onItemClickListener(int id, int position, String s) {
				CaseClass item = mOpusPopuWindow.getItem(position);
				if (mCBDesStyle.isChecked()) {

					if (position == 0) {
						mCBDesStyle.setText("擅长风格 ");
					} else {
						mCBDesStyle.setText(item.title);
					}
					mDesignerListView.setTags(item.id + "");

				} 
//				else if (mCBCost.isChecked()) {
//					switch (position) {
//					case -1:
//
//						break;
//					case -11:
//						go2CitySelect();
//						break;
//					default:
//						if (position == 310000)
//							position = 310100;
//						mDesignerListView.setCity(position);
//						mCBCost.setText(s);
//						break;
//					}
// 
//				} 
				else if (mCBWorkYear.isChecked()) {
					if (position == 0) {
						mCBWorkYear.setText("擅长户型");
					} else {
						mCBWorkYear.setText(item.title);
					}

					mDesignerListView.setWorkYear(item.id + "");
				} else {
					if (position == 0) {
						mCbSort.setText("默认排序");
					} else {
						mCbSort.setText(item.title);
					}

					mDesignerListView.setOrderBy(item.id);
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

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back:
			getActivity().finish();
			break;
		case R.id.tvCity:
			go2CitySelect();
			break;
		case R.id.title_near:
			break;
		case R.id.cbCost:
			mCBWorkYear.setChecked(false);
			mCbSort.setChecked(false);
//			mCBCost.setChecked(true);
			mCBDesStyle.setChecked(false);
			colsClassView();
			MobclickAgent.onEvent(getActivity(),"kControlDesignerCity");//首页设计师城市选择点击
			Intent intentCity = new Intent(getActivity(), SelectCityActivity1.class);
			isChenShi =true;
			startActivityForResult(intentCity, CITY_CODE1);
			break;
		case R.id.cbSort:
			if (!mCbSort.isChecked()) {
				colsClassView();
			} else {
				MobclickAgent.onEvent(getActivity(),"kControlDesignerOrderBy");//首页设计师擅长排序点击
				mCbSort.setChecked(true);
//				mCBCost.setChecked(false);
				mCBWorkYear.setChecked(false);
				mCBDesStyle.setChecked(false);
				mOpusPopuWindow.setNumColumns(1);
				mOpusPopuWindow.setData(getSort());
				openClassView();
			}
			break;
		case R.id.cbDesStyle:
			if (!mCBDesStyle.isChecked()) {
				colsClassView();
			} else {
				MobclickAgent.onEvent(getActivity(),"kControlDesignerDesStyle");//首页设计师擅长风格点击
				mCBDesStyle.setChecked(true);
				mCbSort.setChecked(false);
				mCBWorkYear.setChecked(false);
//				mCBCost.setChecked(false);
				mOpusPopuWindow.setNumColumns(3);
				mOpusPopuWindow.setData(getDesStyle());
				openClassView();
			}
			break;
		case R.id.cbSpace:
			if (!mCBWorkYear.isChecked()) {
				colsClassView();
			} else {
				MobclickAgent.onEvent(getActivity(),"kControlDesignerDesArea");//首页设计师擅长户型点击
				mCBWorkYear.setChecked(true);
				mCbSort.setChecked(false);
//				mCBCost.setChecked(false);
				mCBDesStyle.setChecked(false);
				mOpusPopuWindow.setNumColumns(3);
				mOpusPopuWindow.setData(getSpace());
				openClassView();

			}
			break;
		case R.id.ib_serch:
			Intent intent2 = new Intent(getActivity(), SerchActivity.class);
			intent2.putExtra(AppTag.TAG_ID, 1);
			getActivity().startActivity(intent2);
			break;
		case R.id.desigo:
			setTabClick();
			break;
		case R.id.ding_bu:
			ding_bu.setVisibility(View.GONE);
			setTabClick();
			break;
		}

	}

	private void go2CitySelect() {
		Intent intentCity = new Intent(getActivity(), SelectCityActivity.class);
		startActivityForResult(intentCity, CITY_CODE);
	}

	// 显示弹窗
	private void openClassView() {
		mOpusPopuWindow.show(mCBDesStyle);
	}

	// 关闭弹窗
	public void colsClassView() {
		if(mCBDesStyle !=null){
//			mCBCost.setChecked(false);
			mCBDesStyle.setChecked(false);
			mCBWorkYear.setChecked(false);
			mCbSort.setChecked(false);
			mOpusPopuWindow.dismiss();
		}
	}

	// 获取户型
	private List<CaseClass> getSpace() {
		if (SJQApp.getClassMap() != null) {
			List<CaseClass> list = SJQApp.getClassMap().getList("擅长空间");
			if (list != null) {
				return list;
			}
		}
		return null;
	}

	// 排序
	List<CaseClass> mSortList = null;

	private List<CaseClass> getSort() {
		if (mSortList == null) {
			mSortList = new ArrayList<CaseClass>();
			CaseClass item0 = new CaseClass(0, "默认排序");
			CaseClass item4 = new CaseClass(1, "作品最多");
//			CaseClass item3 = new CaseClass(15, "评价最多");
//			CaseClass item2 = new CaseClass(16, "委托最多");
			CaseClass item5 = new CaseClass(2, "费用最高");
			CaseClass item6 = new CaseClass(21, "费用最低");
			item0.isCheck = true;
			mSortList.add(item0);
//			mSortList.add(item2);
//			mSortList.add(item3);
			mSortList.add(item4);
			mSortList.add(item5);
			mSortList.add(item6);
		}
		return mSortList;
	}
	// 获取风格
	private List<CaseClass> getDesStyle() {
			if (SJQApp.getClassMap() != null) {
				List<CaseClass> list = SJQApp.getClassMap().getList("设计师风格");
				if (list != null) {
					return list;
			}
		}
		return null;
	}
	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mDesignerListView !=null){
			setCase(SJQApp.getClassMap().getList("设计师风格"));
			setCase(SJQApp.getClassMap().getList("擅长空间"));

		}
	}

	private void setCase(List<CaseClass> caseList) {
		if (caseList != null) {
			for (int i = 0; i < caseList.size(); i++) {
				caseList.get(i).setCheck(false);
				if (i == 0) {
					caseList.get(i).setCheck(true);
				}
			}
		}
	}


	public void refresh(int cityid){
//		CacheUtils.cacheStringData(getActivity(), "CITY_NAME",
//				data.getStringExtra(AppTag.TAG_TITLE));
//		CacheUtils.cacheIntData(getActivity(), "CITY_ID",
//				data.getIntExtra(AppTag.TAG_ID, 0));
		if(mDesignerListView !=null){
			mDesignerListView.setCity(cityid);
		}
	}
	void go2Splash() {
		toastShow("抱歉程序出现问题，即将重启动");
		startActivity(new Intent(getActivity(), SkipActivity.class));
		getActivity().finish();
	}

	public void setTabClick() {
		if (getActivity() == null)
			return;
		if (mDesignerListView == null
				|| mDesignerListView.getRefreshableView() == null) {
			go2Splash();
			return;
		}
		int position = mDesignerListView.getRefreshableView()
				.getFirstVisiblePosition();
		if (position == 0) {
			mDesignerListView.setOnRefreshing();
		} else {
			if (position > 5) {
				mDesignerListView.getRefreshableView().setSelection(5);
			}
			mDesignerListView.getRefreshableView().smoothScrollToPosition(0);
		}

	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainDesignerFragment");
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainDesignerFragment");
	}
	@Override
	public void backPressed() {	
		if (mOpusPopuWindow != null)
			mOpusPopuWindow.dismiss();

	}
	public void setDinBu(){
		ding_bu.setVisibility(View.VISIBLE);
	}
}