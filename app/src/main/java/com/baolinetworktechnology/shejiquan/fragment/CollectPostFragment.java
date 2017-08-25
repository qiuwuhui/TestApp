package com.baolinetworktechnology.shejiquan.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

import com.baolinetwkchnology.shejiquan.xiaoxi.HuiHuaFragment;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.view.PostListView;
import com.guojisheng.koyview.ExplosionField;
import com.hyphenate.chat.EMClient;

/**
 * 帖子收藏
 * 
 * @author JiSheng.Guo
 * 
 */
public class CollectPostFragment extends BaseFragment {
	private String TAG = "CollectPost";
	private PostListView mCaseListView;
	public boolean mIsDeleteMode;//是否进入删除模式
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if(view == null){
			view = View.inflate(getActivity(), R.layout.fragment_collect_post,
					null);
			mCaseListView = (PostListView) view.findViewById(R.id.PostListView);
			mCaseListView.getRefreshableView().setOnItemLongClickListener(
					new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
													   final View view, final int position, long id) {

							View dialogView = View.inflate(getActivity(),
									R.layout.dialog_collect, null);
							TextView titl = (TextView) dialogView
									.findViewById(R.id.dialog_title);
							titl.setText("确定要取消收藏？");
							final AlertDialog ad = new AlertDialog.Builder(
									getActivity()).setView(dialogView).show();
							dialogView.findViewById(R.id.dialog_cancel)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {

											ad.cancel();
										}

									});
							dialogView.findViewById(R.id.dialog_ok)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											mCaseListView
													.delete(position - 1, view);
											ad.cancel();
										}
									});

							return true;
						}
					});
			mCaseListView.setChangData(true,
					ExplosionField.attach2Window(getActivity()));
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

}
