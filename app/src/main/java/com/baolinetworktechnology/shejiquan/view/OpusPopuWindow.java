package com.baolinetworktechnology.shejiquan.view;

import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.ClassGvAdapter;
import com.baolinetworktechnology.shejiquan.adapter.CommonAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewHolder;
import com.baolinetworktechnology.shejiquan.adapter.OpusPopuWindowAdapter.PopuWindowListener;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.db.CityDB;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.net.BaseNet.OnCallBackList;
import com.baolinetworktechnology.shejiquan.net.NetCityList;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.WindowsUtil;
import com.lidroid.xutils.exception.HttpException;

/**
 * 导航栏弹窗
 * 
 * @author JiSheng.Guo
 * 
 */
public class OpusPopuWindow implements OnClickListener {
	private PopupWindow mPopupWindow;
	private GridView mListView;// 导航栏控件
	private ClassGvAdapter mAdapter;// 导航栏数据表适配器
	private PopuWindowListener mOnPopuWindowListener;// 点击监听器
	private Context mContext;
	private View contentView;
	private View itemCity;

	// private AnimationListener mAnimListener;

	/**
	 * 设置数据 （并不显示）
	 * 
	 * @param caseList
	 *            列表数据List<CaseClass>
	 */
	public void setData(List<CaseClass> caseList) {
		mAdapter.setData(caseList);
		itemCity.setVisibility(View.GONE);
		itemCityqu.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
	}

	public List<CaseClass> getData(List<CaseClass> caseList) {
		return mAdapter.getData();

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

		// if (!mPopupWindow.isShowing()) {
		// // Animation animation = AnimationUtils.loadAnimation(mContext,
		// // R.anim.open_gv);
		// // ((View) mListView.getParent()).setAnimation(animation);
		// //showViewWithVerticalAnimator();
		// }
		mPopupWindow.showAsDropDown(view);

		// mListView.clearAnimation();
		// mListView.setSelection(0);

	}

	public void show(View view, int x, int y) {
		mListView.clearAnimation();
		mListView.setSelection(0);
		if (!mPopupWindow.isShowing()) {
			Animation animation = AnimationUtils.loadAnimation(mContext,
					R.anim.open_gv);
		}

		mPopupWindow.showAsDropDown(view, x, y);

	}

	/**
	 * 关闭popuWindows弹窗
	 */
	public void dismiss() {
		if (mPopupWindow.isShowing()) {
			// mListView.clearAnimation();
			// ((View) mListView.getParent()).clearAnimation();

			// Animation animation = AnimationUtils.loadAnimation(mContext,
			// R.anim.clos_gv);
			// animation.setAnimationListener(mAnimListener);
			// ((View) mListView.getParent()).setAnimation(animation);
			mPopupWindow.dismiss();
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

	public OpusPopuWindow(Context context) {
		this(context, false);
	}

	public OpusPopuWindow(Context context, boolean isOutsideTouchable) {
		mContext = context;
		contentView = View.inflate(mContext, R.layout.popu_window_opus, null);
		mListView = (GridView) contentView.findViewById(R.id.case_class_list);
		itemCity = contentView.findViewById(R.id.itemCity);
		itemCityqu = contentView.findViewById(R.id.itemCityqu);
		initCity();
		initCityqu();
		contentView.setOnClickListener(this);
		mPopupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setOutsideTouchable(isOutsideTouchable);
		mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(
				R.color.trans_black));
		mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style3);
		mAdapter = new ClassGvAdapter();
		mListView.setAdapter(mAdapter);
		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
		// mPopupWindow.setClippingEnabled(false);

	}

	int num = 0;
	private int verticalSpacing = -1;
	private View itemCityqu;

	public void setNumColumns(int i) {
		if (mListView != null) {
			if (num != i) {
				num = i;
				mListView.setNumColumns(i);
				if (verticalSpacing < 0) {
					verticalSpacing = WindowsUtil.dip2px(mContext, 16);
				}
				if (i == 1) {
					mListView.setVerticalSpacing(0);
				} else {
					mListView.setVerticalSpacing(verticalSpacing);
				}

				mAdapter.setNumColumns(i);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	public void setNumColumns2(int i) {
		if (mListView != null) {
			mListView.setNumColumns(i);
		}
	}

	public void showCity() {
		itemCity.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.INVISIBLE);
		itemCityqu.setVisibility(View.GONE);
	}
	public void showCityqu() {
		itemCity.setVisibility(View.GONE);
		mListView.setVisibility(View.INVISIBLE);
		itemCityqu.setVisibility(View.VISIBLE);
	}
	private void initCityqu() {
		TextView chengshi=(TextView) itemCityqu.findViewById(R.id.chengshi);
		chengshi.setOnClickListener(this);
		itemCityqu.findViewById(R.id.chengsi_layout).setOnClickListener(this);
		City city1 = SJQApp.getCity();
		String Address="";
//				CacheUtils.getStringData(mContext, key, defValue);			
		if(city1!=null){
			Address=city1.Title;		
		}		
		if(TextUtils.isEmpty(Address)){
			chengshi.setText("当前城市："+"定位中");
		}else{
			chengshi.setText("当前城市："+Address);
		}		
		GridView gvCityqu = (GridView) itemCityqu.findViewById(R.id.gvCityqu);
		final CommonAdapter<City> gvCityAdapterqu = new CommonAdapter<City>(
				itemCityqu.getContext(), R.layout.item_gv_city) {

			@Override
			public void convert(ViewHolder holder, City item) {
				holder.setText(R.id.tv_name, item.Title);
				holder.getView(R.id.tv_name).setTag(item.CityID + "");
				holder.getView(R.id.tv_name).setOnClickListener(
						OpusPopuWindow.this);
				
			}
		};
		gvCityqu.setAdapter(gvCityAdapterqu);
		CityService mApplication=CityService.getInstance(mContext);
		List<City> CityListqu=mApplication.getCityListqu();
		gvCityAdapterqu.setData(CityListqu);
		gvCityAdapterqu.notifyDataSetChanged();
	}
	private void initCity() {
		Button btnDw = (Button) itemCity.findViewById(R.id.btnCity);
		itemCity.findViewById(R.id.btnAll).setOnClickListener(this);
		itemCity.findViewById(R.id.tvAll).setOnClickListener(this);

		City city = SJQApp.getCity();
		if (city != null) {
			btnDw.setText(city.Title);
			btnDw.setTag(city.CityID + "");
		}
		btnDw.setOnClickListener(this);
		GridView gvCity = (GridView) itemCity.findViewById(R.id.gvCity);
		final CommonAdapter<City> gvCityAdapter = new CommonAdapter<City>(
				itemCity.getContext(), R.layout.item_gv_city) {

			@Override
			public void convert(ViewHolder holder, City item) {
				holder.setText(R.id.tv_name, item.Title);
				holder.getView(R.id.tv_name).setTag(item.ID + "");
				holder.getView(R.id.tv_name).setOnClickListener(
						OpusPopuWindow.this);

			}
		};
		gvCity.setAdapter(gvCityAdapter);
		new NetCityList().getHotCityList(new OnCallBackList<City>() {

			@Override
			public void onNetSuccess(List<City> data) {
				if (data != null && data.size() > 0) {
					if (gvCityAdapter == null)
						return;
					gvCityAdapter.setData(data);
					gvCityAdapter.notifyDataSetChanged();
				}

			}

			@Override
			public void onNetStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onNetFailure(String mesa) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onNetError(String json) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onNetError(HttpException arg0, String mesa) {
				// TODO Auto-generated method stub

			}
		}, itemCity.getContext());
	}
	public void setCity() {
		initCityqu();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_name:
		case R.id.btnCity:// 定位
		case R.id.btnAll:// 全国
		case R.id.tvAll:// 跳转到城市选择
		case R.id.chengshi:
		case R.id.chengsi_layout:
			TextView tv = (TextView) v;
			int id = Integer.parseInt((String) v.getTag());
			if (mOnPopuWindowListener != null) {
				mOnPopuWindowListener.onItemClickListener(-1, id, tv.getText()
						.toString());
			}
			break;
		default:
			if (mOnPopuWindowListener != null) {
				mOnPopuWindowListener.onClosePopuWindow();
			}
			break;
		}

	}
}
