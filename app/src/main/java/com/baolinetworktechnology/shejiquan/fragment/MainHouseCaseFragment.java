package com.baolinetworktechnology.shejiquan.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.MyFragmentActivity.IBackPressed;
import com.baolinetworktechnology.shejiquan.adapter.OpusPopuWindowAdapter.PopuWindowListener;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
import com.baolinetworktechnology.shejiquan.view.ListHomeCase;
import com.baolinetworktechnology.shejiquan.view.ListHouseCase;
import com.baolinetworktechnology.shejiquan.view.OpusPopuWindow;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 *全屋案例
 */
public class MainHouseCaseFragment extends BaseMainFragment implements
		OnCheckedChangeListener, IBackPressed{
	private int mTags4;
	private CheckBox mCheckBox1;
	private CheckBox mCheckBox2;
	private CheckBox mCheckBox3;
	private CheckBox mCheckBox4;
	private OpusPopuWindow mOpusPopuWindow;// 弹窗
	private PopuWindowListener mPopuWindowListener;
	private ListHouseCase mCaseListView;
	private String Tags = "0-0-0-0";
	private int OrderBy = 0, ItemID = 0, ClassID = 5;
	private String MarkName = AppTag.MARKNAME_DESIGNER;
	private OnStateListener mOnStateListener;
	String Budget = "";
	private CountDownTimer time;
	private ImageView ding_bu;
	public void setonStateListener(OnStateListener onStateListener) {
		this.mOnStateListener = onStateListener;
		if (mCaseListView != null)
			mCaseListView.setonStateListener(mOnStateListener);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = View.inflate(getActivity(),
				R.layout.fragment_main_house_case, null);
		initView(view);
		initData();
		return view;
	}

	private void initView(View view) {
		ding_bu= (ImageView) view.findViewById(R.id.ding_bu);
		ding_bu.setOnClickListener(this);
		mCaseListView = (ListHouseCase) view.findViewById(R.id.listViewCase);
		mCaseListView.getPulldownFooter().isShowBottom(true);
		mCaseListView.setonStateListener(mOnStateListener);

		mCheckBox1 = (CheckBox) view.findViewById(R.id.rb_1);
		mCheckBox2 = (CheckBox) view.findViewById(R.id.rb_2);
		mCheckBox3 = (CheckBox) view.findViewById(R.id.rb_3);
		mCheckBox4 = (CheckBox) view.findViewById(R.id.rb_4);

		mCheckBox1.setTag("全屋风格");
		mCheckBox2.setTag("全屋户型");
		mCheckBox3.setTag("面积");
		mCheckBox4.setTag("预算");

		mCheckBox1.setOnCheckedChangeListener(this);
		mCheckBox2.setOnCheckedChangeListener(this);
		mCheckBox3.setOnCheckedChangeListener(this);
		mCheckBox4.setOnCheckedChangeListener(this);

		mOpusPopuWindow = new OpusPopuWindow(getActivity());
		mPopuWindowListener = new PopuWindowListener() {

			@Override
			public void onItemClickListener(int id, int position, String s) {
				// TODO Auto-generated method stub

				CaseClass caseClass = mOpusPopuWindow.getItem(position);

				if (mCheckBox1.isChecked()) {
					if (caseClass.id == 0) {

						mCheckBox1.setText(mCheckBox1.getTag().toString());
					} else {
						mCheckBox1.setText(caseClass.title);
					}
					mCaseListView.setTags(caseClass.id+"");
				} else if (mCheckBox2.isChecked()) {
					if (caseClass.id == 0) {
						mCheckBox2.setText(mCheckBox2.getTag().toString());
					} else {
						mCheckBox2.setText(caseClass.title);
					}
					mCaseListView.setattrHouseType(caseClass.id+"");
				} else if (mCheckBox3.isChecked()) {
					if (caseClass.id == 0) {
						mCheckBox3.setText(mCheckBox3.getTag().toString());
					} else {
						mCheckBox3.setText(caseClass.title);
					}
				} else if (mCheckBox4.isChecked()) {
					if (caseClass.id == 0) {
						mCheckBox4.setText("造价");
					} else {
						mCheckBox4.setText(caseClass.title);
					}
					mTags4 = caseClass.id;

					try {
						Budget = URLEncoder.encode(caseClass.title, "utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mCaseListView.setBudget(Budget);

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
	public void onDestroy() {
		if(mCaseListView != null){
			setCase(SJQApp.getClassMap().getList("全屋风格"));
			setCase(SJQApp.getClassMap().getList("全屋户型"));
		}
		super.onDestroy();
	}
	private void initData() {
		mCaseListView.setClassID(5);
		mCaseListView.setCaceKey("anli");
		go2GuideActivity();
//		mCaseListView.setOnRefreshing();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ding_bu:
				ding_bu.setVisibility(View.GONE);
				setTabClick();
				break;
		}
	}
	private void go2GuideActivity() {
		time = new CountDownTimer(600, 600) {

			@Override
			public void onTick(long millisUntilFinished) {

			}
			@Override
			public void onFinish() {
				mCaseListView.setOnRefreshing();
			}
		};
		time.start();
	}
	@Override
	public void onStop() {
		super.onStop();
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
	boolean isRefresh = true;


	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainHomeCaseFragment");
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainHomeCaseFragment");
	}


	int mPreId = 0;

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			mPreId = buttonView.getId();
			switch (buttonView.getId()) {
				case R.id.rb_1:
					MobclickAgent.onEvent(getActivity(),"kControlCaseStyle");//首页案例风格选项点击
					mCheckBox2.setChecked(false);
					mCheckBox3.setChecked(false);
					mCheckBox4.setChecked(false);
					break;
				case R.id.rb_2:
					MobclickAgent.onEvent(getActivity(),"kControlCaseHouseType");//首页案例户型选项点击
					mCheckBox1.setChecked(false);
					mCheckBox3.setChecked(false);
					mCheckBox4.setChecked(false);
					break;
				case R.id.rb_3:
					MobclickAgent.onEvent(getActivity(),"kControlCaseSize");//首页案例面积选项点击
					mCheckBox2.setChecked(false);
					mCheckBox1.setChecked(false);
					mCheckBox4.setChecked(false);
					break;
				case R.id.rb_4:
					mCheckBox2.setChecked(false);
					mCheckBox3.setChecked(false);
					mCheckBox1.setChecked(false);
					break;
				default:
					break;
			}

			if (SJQApp.getClassMap() != null) {
				mOpusPopuWindow.setData(SJQApp.getClassMap().getList(
						buttonView.getTag().toString()));
			} else {
				toastShow("获取数据失败，请稍候重试");
			}
			openClassView();

		} else {
			if (mPreId == buttonView.getId())
				colsClassView();
		}

	}
	@Override
	public void backPressed() {
		if (mOpusPopuWindow != null) {
			if (mOpusPopuWindow.isShowing()) {
				colsClassView();
			}
		}

	}

	// 关闭 列表弹窗
	public void colsClassView() {
		if(mCheckBox2 != null){
			mCheckBox2.setChecked(false);
			mCheckBox1.setChecked(false);
			mCheckBox3.setChecked(false);
			mCheckBox4.setChecked(false);
			mOpusPopuWindow.dismiss();
		}
	}

	private void openClassView() {
		mOpusPopuWindow.show(mCheckBox1);
	}

	// 导航栏 TAb 点击事件
	public void setTabClick() {
		int position = mCaseListView.getRefreshableView()
				.getFirstVisiblePosition();
		if (position == 0) {
			mCaseListView.setOnRefreshing();
		} else {
			if (position > 4) {
				mCaseListView.getRefreshableView().setSelection(4);
			}
			mCaseListView.getRefreshableView().smoothScrollToPosition(0);
		}
	}
	public void setTags(String tags2) {
		Tags = tags2;
		// initAdapter();
	}


	public void setOrderBy(int id) {
		OrderBy = id;

	}
	public void lazyLoad() {
//		if (mCaseListView != null)
//			mCaseListView.setRefreshing();
	}

	public void setOnRefreshing() {
		if (mCaseListView != null)
			mCaseListView.setOnRefreshing();
	}
	public void setDinBu(){
		ding_bu.setVisibility(View.VISIBLE);
	}
}