package com.baolinetworktechnology.shejiquan.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.ClassGvAdapter;
import com.baolinetworktechnology.shejiquan.adapter.OlderNavAdapter;
import com.baolinetworktechnology.shejiquan.adapter.OpusPopuWindowAdapter.PopuWindowListener;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.manage.CityService;

/**
 * 导航栏弹窗
 * 
 * @author JiSheng.Guo
 * 
 */
public class GvPopuWindow {
	private PopupWindow mPopupWindow;
	private GridView mListView;// 导航栏控件
	private ClassGvAdapter mAdapter;// 导航栏数据表适配器
	private PopuWindowListener mOnPopuWindowListener;// 点击监听器
	private Context mContext;

	/**
	 * 设置数据 （并不显示）
	 * 
	 * @param caseList
	 *            列表数据List<CaseClass>
	 */
	public void setData(List<CaseClass> caseList) {
		mAdapter.setData(caseList);

	}

	/**
	 * 设置数据并显示
	 * 
	 * @param caseList
	 *            列表数据List<CaseClass>
	 * @param view
	 *            显示该View下方
	 */
	public void setData(List<CaseClass> caseList, View view) {
		mAdapter.setData(caseList);
		show(view);
	}

	/**
	 * 
	 * @param view
	 *            显示在该View下方
	 * 
	 */
	public void show(View view) {
		
	
		
		if (mListView.getVisibility() != View.VISIBLE) {
			mListView.setVisibility(View.VISIBLE);
			older_liner_nav.setVisibility(View.GONE);
		}
		mPopupWindow.showAsDropDown(view);
		
		//mListView.clearAnimation();
		//mListView.setSelection(0);
	

	}

	/**
	 * 关闭popuWindows弹窗
	 */
	public void dismiss() {
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
//			mListView.clearAnimation();
//			((View) mListView.getParent()).clearAnimation();
//			Animation animation = AnimationUtils.loadAnimation(mContext,
//					R.anim.clos_gv);
//			animation.setAnimationListener(mAnimListener);
//			((View) mListView.getParent()).setAnimation(animation);
		}
	}

	public boolean isShowing() {
		if (mPopupWindow == null)
			return false;
		return mPopupWindow.isShowing();
	}

	/**
	 * 设置点击监听器
	 * 
	 * @param onPopuWindowListener
	 */
	public void setOnItemClickListener(PopuWindowListener onPopuWindowListener) {
		mOnPopuWindowListener = onPopuWindowListener;
		mAdapter.setonPopuWindowItemClickListener(mOnPopuWindowListener);
	}

	/**
	 * 根据Position获取条目数据
	 * 
	 * @param position
	 * 
	 * @return CaseClass
	 */
	public CaseClass getItem(int position) {
		return (CaseClass) mAdapter.getItem(position);
	}

	public GvPopuWindow(Context context) {
		this(context, false);
	}

	int porvIndex = 0;

	public GvPopuWindow(Context context, boolean isOutsideTouchable) {
		mContext = context;
		View contentView = View
				.inflate(mContext, R.layout.popu_window_gv, null);

		mListView = (GridView) contentView.findViewById(R.id.case_class_list);
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		initCity(contentView);
		mPopupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setOutsideTouchable(isOutsideTouchable);
		// mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(
				R.color.trans_black));
		mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style3);
		mAdapter = new ClassGvAdapter();
		mListView.setAdapter(mAdapter);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (mOnPopuWindowListener != null) {
					mOnPopuWindowListener.onClosePopuWindow();
				}

			}
		});

	}

	private OlderNavAdapter olderNavAdapter,cityAdapter;

	public void c() {
		olderNavAdapter.notifyDataSetChanged();
	}

	// private int CityID;
	private View older_liner_nav;
	ListView older_list_first;

	private void initCity(View view) {
		older_liner_nav = view.findViewById(R.id.older_liner_nav);
		older_liner_nav.setVisibility(View.GONE);
		older_list_first = (ListView) view.findViewById(R.id.older_list_first);
		final ListView older_list_two = (ListView) view
				.findViewById(R.id.older_list_two);

		olderNavAdapter = new OlderNavAdapter();

		older_list_first.setAdapter(olderNavAdapter);

		olderNavAdapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				porvIndex = position;
				if (cityAdapter == null) {
					cityAdapter = new OlderNavAdapter();
					older_list_two.setAdapter(cityAdapter);
				}
				if (position != -1 && id > 0) {
					List<City> citys;

					citys = CityService.getInstance(mContext).getArea((int) id);

					cityAdapter.setData(citys);
					cityAdapter.notifyDataSetChanged();
				} else {
					porvIndex = 0;
					if (mOnPopuWindowListener != null) {
						mOnPopuWindowListener.onItemClickListener(0, 0, "");
					}
					cityAdapter.setData(new ArrayList<City>());
					cityAdapter.notifyDataSetChanged();
				}
			}
		});
		if (cityAdapter == null) {
			cityAdapter = new OlderNavAdapter();
			older_list_two.setAdapter(cityAdapter);
			cityAdapter.setData(new ArrayList<City>());
		}
		cityAdapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mOnPopuWindowListener != null) {
					City city = (City) cityAdapter.getItem(position);
					if (city == null)
						return;
					mOnPopuWindowListener.onItemClickListener((int) id, 0,
							city.Title);
				}
			}
		});

	}

	public void ShowCity(View view) {

		mPopupWindow.showAsDropDown(view);
		older_liner_nav.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);

		if (older_list_first != null)
			older_list_first.setSelection(porvIndex - 1);
	}

	public void initSortView() {
		older_list_first.setVisibility(View.GONE);

	}

	public void setSortData(List<City> data) {

		cityAdapter.setData(data);
	}

	public void numColumns(int i) {
		mListView.setNumColumns(i);
		
	}
}
