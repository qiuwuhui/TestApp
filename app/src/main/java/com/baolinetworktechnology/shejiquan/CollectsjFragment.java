package com.baolinetworktechnology.shejiquan;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.fragment.BaseFragment;
import com.baolinetworktechnology.shejiquan.view.DesignerListView;
import com.guojisheng.koyview.ExplosionField;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class CollectsjFragment extends BaseFragment{
	private DesignerListView mCaseListView;
	private static View no_layout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_collectsj,
				null);
		no_layout = view.findViewById(R.id.No_layout);
		mCaseListView = (DesignerListView) view.findViewById(R.id.listViewCase);
		mCaseListView.setChangData(true,
				ExplosionField.attach2Window(getActivity()));

		mCaseListView.getRefreshableView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
												   final View view, final int position, long id) {

						View dialogView = View.inflate(getActivity(),
								R.layout.dialog_collect, null);
						TextView titl = (TextView) dialogView
								.findViewById(R.id.dialog_title);
						titl.setText("确定要取消关注？");
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

	public static void vienin(){
		no_layout.setVisibility(View.VISIBLE);
	}
	public static void gong(){
		no_layout.setVisibility(View.GONE);
	}
}
