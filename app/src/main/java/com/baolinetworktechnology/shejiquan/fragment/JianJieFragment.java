package com.baolinetworktechnology.shejiquan.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.PhotoActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.adapter.CaseAdapter;
import com.baolinetworktechnology.shejiquan.adapter.CommonAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewHolder;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.CasePageList;
import com.baolinetworktechnology.shejiquan.domain.DesignerItemInfo;
import com.baolinetworktechnology.shejiquan.domain.HonorBean;
import com.baolinetworktechnology.shejiquan.domain.HonorListBean;
import com.baolinetworktechnology.shejiquan.domain.PostMainBean;
import com.baolinetworktechnology.shejiquan.domain.SwDesignerDetail;
import com.baolinetworktechnology.shejiquan.view.ScrollGridLayoutManager;
import com.baolinetworktechnology.shejiquan.view.WrapContentHeightViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 案例收藏
 * 
 * @author JiSheng.Guo
 * 
 */
public class JianJieFragment extends BaseFragment {

	private WrapContentHeightViewPager vp;
	View view;
	private SwDesignerDetail desiDate;
	private TextView feng_ge,lin_yu,chong_ye,jian_jie;
	private GridView ry_gridview;
	private View ry_lay;
	private CommonAdapter<HonorBean> hzAdapter;
	private ArrayList<String> imageList;// 套图
	public JianJieFragment(WrapContentHeightViewPager vp) {
		this.vp  = vp;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if(view == null){
			view = View.inflate(getActivity(), R.layout.jian_jie_fragment,
					null);
			vp.setObjectForPosition(view,3);
			inview(view);
			showDesignData(desiDate);
			loadata(desiDate.getGuid());
		}
		return view;
	}

	private void inview(View view) {
		feng_ge = (TextView) view.findViewById(R.id.feng_ge);
		lin_yu = (TextView) view.findViewById(R.id.lin_yu);
		chong_ye = (TextView) view.findViewById(R.id.chong_ye);
		jian_jie = (TextView) view.findViewById(R.id.jian_jie);
		ry_lay = view.findViewById(R.id.ry_lay);
		ry_gridview = (GridView) view.findViewById(R.id.ry_gridview);
		ry_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		hzAdapter = new CommonAdapter<HonorBean>(getContext(),
				R.layout.item_jian_jie) {

			@Override
			public void convert(ViewHolder holder, HonorBean item) {
				setImageUrl(holder.getView(R.id.zhenshu), item.getPhotoUrl());
			}
		};
		hzAdapter.setDefaultLoadingImage(R.drawable.zixun_tu);
		ry_gridview.setAdapter(hzAdapter);
		ry_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				Intent intent = new Intent(getActivity(),
						PhotoActivity.class);
				// 所有图片集合
				intent.putStringArrayListExtra(PhotoActivity.IMAGE_URLS,
						imageList);
				intent.putExtra(PhotoActivity.INDEX, position);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getActivity().startActivity(intent);
			}
		});
	}
	//显示数据
	private void showDesignData(SwDesignerDetail data) {
		if(data.desStyle==null){
			feng_ge.setText("未填写");
		}else{
			feng_ge.setText(data.getStrStyle(getActivity()));
		}
		lin_yu.setText(data.getStrArea(getActivity()));
		if(data.officeTime!=null && !data.officeTime.equals("") ){
			chong_ye.setText(getWorkYear(data.officeTime)+"年");
		}
		jian_jie.setText(data.comment);
	}
	public void setShuaxin(SwDesignerDetail data) {
		desiDate =data;
	}
	//获取工作多少年
	public  int  getWorkYear(String OfficeTime){
		if(TextUtils.isEmpty(OfficeTime)){
			return 0;
		}
		int Time=Integer.parseInt(OfficeTime);
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR); //获取当前年份
		return mYear-Time;
	}
	//获取设计师
	private void loadata(String userGUID) {
		String url = AppUrl.PHOTOGETPAGELIST+"userGUID="+userGUID+"&markName=3";
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow("网络连接错误，请检查网络");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject json;
				HonorListBean bean=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, HonorListBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (bean == null || bean.listData==null || bean.listData.size() ==0 ) {
					ry_lay.setVisibility(View.GONE);
				}
				if (bean!= null && bean.listData != null) {
					imageList = new ArrayList<>();
					for(int i=0;i<bean.listData.size();i++) {
						imageList.add(bean.listData.get(i).getPhotoUrl());
					}
					hzAdapter.setData(bean.listData);
					hzAdapter.notifyDataSetChanged();
				}
			}
		};
		getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
				callBack);
	}
	public void setAdapter() {
		if(ry_gridview != null){
			ry_gridview.setAdapter(null);
		}
	}
}
