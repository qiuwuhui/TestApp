package com.baolinetworktechnology.shejiquan.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.view.NewsListView;
import com.guojisheng.koyview.ExplosionField;

/**
 * 文章收藏
 * 
 * @author JiSheng.Guo
 * 
 */
public class CollectNewsFragment extends BaseFragment {
	private NewsListView mNewsListView;
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view == null){
			view = View.inflate(getActivity(), R.layout.fragment_collect_news,
					null);
			mNewsListView = (NewsListView) view.findViewById(R.id.newsListView);
			mNewsListView.getRefreshableView().setOnItemLongClickListener(
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
											mNewsListView
													.delete(position - 1, view);
											ad.cancel();
										}
									});

							return true;
						}
					});
			mNewsListView.setChangData(true,
					ExplosionField.attach2Window(getActivity()));
			mNewsListView.setUserGuid(SJQApp.user.guid, true);
		}
		return view;

	}

	@Override
	public void onResume() {
		super.onResume();
		if (SJQApp.isRrefresh) {
			if (SJQApp.user == null)
				return;
			mNewsListView.setUserGuid(SJQApp.user.guid, true);
			SJQApp.isRrefresh=false;
		}
	}

	public void setChangData(boolean is) {
		// TODO Auto-generated method stub
		mNewsListView.setChangData(is);
	}

	public void setChangData(boolean is, ExplosionField mExplosionField) {
		mNewsListView.setChangData(is, mExplosionField);

	}
	public void shuaxin(){
		mNewsListView.setRefreshing();
	}
}
