package com.baolinetworktechnology.shejiquan.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.MyFragmentActivity.IBackPressed;
import com.baolinetworktechnology.shejiquan.adapter.OpusPopuWindowAdapter.PopuWindowListener;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
import com.baolinetworktechnology.shejiquan.view.GvPopuWindow;
import com.baolinetworktechnology.shejiquan.view.ListViewCaseHome;

/**
 * 首页-作品-公装
 * 
 * @author JiSheng.Guo
 * 
 */
public class MainPublicCaseFragment extends Fragment implements IBackPressed,
		OnCheckedChangeListener, PopuWindowListener {
	private GvPopuWindow gvPopuWindow;
	private CheckBox mCheckBox1;
	private CheckBox mCheckBox2;
	private int mPreId;
	private ListViewCaseHome mCaseListView;

	// List<CaseClass> sortList;
	public void setonStateListener(OnStateListener onStateListener) {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = View.inflate(getActivity(),
				R.layout.fragment_main_public_case, null);
		initView(view);
		mCaseListView.setTags("0");
		mCaseListView.setClassID(mCaseListView.CLASS_ID_PUBLIC);

		return view;
	}

	private void initView(View view) {
		gvPopuWindow = new GvPopuWindow(getActivity());
		gvPopuWindow.setOnItemClickListener(this);
		mCheckBox1 = (CheckBox) view.findViewById(R.id.cb1);
		mCheckBox1.setTag("公装空间");
		mCheckBox2 = (CheckBox) view.findViewById(R.id.cb2);
		mCheckBox1.setOnCheckedChangeListener(this);
		mCheckBox2.setOnCheckedChangeListener(this);
		// sortList=new ArrayList<CaseClass>();
		// sortList.add(new CaseClass(0, "默认排序"));
		// sortList.add(new CaseClass(15, "收藏最多"));
		// sortList.add(new CaseClass(14, "评论最多"));
		List<City> data = new ArrayList<City>();
		data.add(new City(0, 0, 0, "", "", "默认排序"));
		data.add(new City(15, 0, 0, "", "", "收藏最多"));
		data.add(new City(14, 0, 0, "", "", "评论最多"));
		gvPopuWindow.setSortData(data);
		gvPopuWindow.initSortView();
		gvPopuWindow.numColumns(4);
		mCaseListView = (ListViewCaseHome) view.findViewById(R.id.listViewCase);
		mCaseListView.getPulldownFooter().isShowBottom(true);
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
				if (SJQApp.getClassMap() != null) {
					gvPopuWindow.setData(SJQApp.getClassMap().getList(
							buttonView.getTag().toString()));
				} else {
					toastShow("获取数据失败，请稍候重试");
				}
				gvPopuWindow.show(mCheckBox2);
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

	private void toastShow(String string) {
		if (getActivity() != null)
			Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onItemClickListener(int id, int position, String s) {
		if (mCheckBox1.isChecked()) {
			if (id == 0) {
				mCheckBox1.setText("公装空间");
			} else {
				mCheckBox1.setText(s);
			}
			mCaseListView.setTags(id + "");
			mCaseListView.setRefreshing();
		} else {
			mCheckBox2.setText(s);
			mCaseListView.setOrderBy(id);
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
			mCaseListView.setOnRefreshing();
		} else {
			if (position > 4) {
				mCaseListView.getRefreshableView().setSelection(4);
			}
			mCaseListView.getRefreshableView().smoothScrollToPosition(0);
		}

	}

	boolean isLoad = false;

	public void setOnRefreshing() {
		if (!isLoad) {
			if (mCaseListView != null) {
				mCaseListView.setOnRefreshing();
				isLoad = true;
			}
		}
	}

}
