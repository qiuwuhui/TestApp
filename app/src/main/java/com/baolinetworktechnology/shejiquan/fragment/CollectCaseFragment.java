package com.baolinetworktechnology.shejiquan.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.view.CaseListView;
import com.guojisheng.koyview.ExplosionField;

/**
 * 案例收藏
 * 
 * @author JiSheng.Guo
 * 
 */
public class CollectCaseFragment extends BaseFragment {
	private boolean mIsDeleteMode;//是否进入删除模式
	private CaseListView mCaseListView;
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if(view==null){
			view = View.inflate(getActivity(), R.layout.fragment_collect_case,
					null);
			mCaseListView = (CaseListView) view.findViewById(R.id.caseListView);
//			mCaseListView.setChangData(true,
//					ExplosionField.attach2Window(getActivity()));
//
//			mCaseListView.getRefreshableView().setOnItemLongClickListener(
//					new OnItemLongClickListener() {
//
//						@Override
//						public boolean onItemLongClick(AdapterView<?> parent,
//													   final View view, final int position, long id) {
//
//							View dialogView = View.inflate(getActivity(),
//									R.layout.dialog_collect, null);
//							TextView titl = (TextView) dialogView
//									.findViewById(R.id.dialog_title);
//							titl.setText("确定要取消收藏？");
//							final AlertDialog ad = new AlertDialog.Builder(
//									getActivity()).setView(dialogView).show();
//							dialogView.findViewById(R.id.dialog_cancel)
//									.setOnClickListener(new OnClickListener() {
//
//										@Override
//										public void onClick(View v) {
//
//											ad.cancel();
//										}
//
//									});
//							dialogView.findViewById(R.id.dialog_ok)
//									.setOnClickListener(new OnClickListener() {
//
//										@Override
//										public void onClick(View v) {
//											mCaseListView
//													.delete(position - 1, view);
//											ad.cancel();
//										}
//									});
//
//							return true;
//						}
//					});
		}

		return view;
	}

	boolean first = true;

	@Override
	public void onResume() {
		super.onResume();
		if (first) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					if (SJQApp.user == null)
						return;
					mCaseListView.setUserGuid(SJQApp.user.guid, true);
				}
			}, 300);
			first = false;
		} else {
			if (SJQApp.isRrefresh) {
				if (SJQApp.user == null)
					return;
				mCaseListView.setUserGuid(SJQApp.user.guid, true);
				//SJQApp.isRrefresh=false;
			}
		}

	}

	// 设置是否编辑
	public void setChangData(boolean is) {
		mCaseListView.setChangData(is);

	}

	// 设置是否编辑
	public void setChangData(boolean is, ExplosionField mExplosionField) {
		// TODO Auto-generated method stub
		mCaseListView.setChangData(is, mExplosionField);
	}
	public void shuaxin(){
		mCaseListView.setRefreshing();
	}
	//是否进入批量删除模式
	public void DeleteMode(boolean isdelete){
		mIsDeleteMode = isdelete;
		mCaseListView.setDeleteMode(mIsDeleteMode);
	}
	public void batchDelete(){
		mCaseListView.bitchdelete();
	}
}
