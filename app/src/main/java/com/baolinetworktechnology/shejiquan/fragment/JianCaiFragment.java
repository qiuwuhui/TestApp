package com.baolinetworktechnology.shejiquan.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.BrandActivity;
import com.baolinetworktechnology.shejiquan.activity.PhotoActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.adapter.CommonAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewHolder;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.BrandBean;
import com.baolinetworktechnology.shejiquan.domain.BrandListBean;
import com.baolinetworktechnology.shejiquan.domain.HonorBean;
import com.baolinetworktechnology.shejiquan.domain.HonorListBean;
import com.baolinetworktechnology.shejiquan.domain.SwDesignerDetail;
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
public class JianCaiFragment extends BaseFragment {

	private WrapContentHeightViewPager vp;
	View view;
	private GridView ry_gridview;
	private View ry_lay;
	private CommonAdapter<BrandBean> hzAdapter;
	private int PageIndex=1;
	private String designerGudi;
	private View No_layout;
	private TextView not_Tv;
	private View buyLayout;
	public JianCaiFragment(WrapContentHeightViewPager vp) {
		this.vp  = vp;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if(view == null){
			view = View.inflate(getActivity(), R.layout.jian_cai_fragment,
					null);
			vp.setObjectForPosition(view,1);
			inview(view);
		}
		return view;
	}

	private void inview(View view) {
		ry_lay = view.findViewById(R.id.ry_lay);
		No_layout = view.findViewById(R.id.No_layout);
		buyLayout =view.findViewById(R.id.buyLayout);
		not_Tv = (TextView) view.findViewById(R.id.not_Tv);
		ry_gridview = (GridView) view.findViewById(R.id.ry_gridview);
		hzAdapter = new CommonAdapter<BrandBean>(getContext(),
				R.layout.item_jian_cai) {

			@Override
			public void convert(ViewHolder holder, BrandBean item) {
				setImageUrl(holder.getView(R.id.zhenshu), item.getLogo());
			}
		};
		hzAdapter.setDefaultLoadingImage(R.drawable.zixun_tu);
		ry_gridview.setAdapter(hzAdapter);
		ry_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		ry_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				BrandBean bean = hzAdapter.getItem(position);
				Intent intent = new Intent(getActivity(),
						BrandActivity.class);
				intent.putExtra("guid",bean.getGuid());
				getActivity().startActivity(intent);
			}
		});
	}
	public void setdesignerGudi(int pageIndex) {
		this.PageIndex=pageIndex;
		loadata();
	}
	public void setShuaxin(String guid){
//		buyLayout.getLayoutParams().height = LayoutTop;
		designerGudi=guid;
		loadata();
	}
	//获取设计师
	private void loadata() {
		String url = AppUrl.DESIGNERRECOMMEND+"designerGuid="+designerGudi+"&pageSize=10"+"&currentPage="+PageIndex;
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				WeiShopActivity activity= (WeiShopActivity) getActivity();
				if(activity == null){
					return;
				}
				No_layout.setVisibility(View.VISIBLE);
				ry_lay.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject json;
				BrandListBean bean=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, BrandListBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				WeiShopActivity activity= (WeiShopActivity) getActivity();
				if(activity == null){
					return;
				}
				if (bean == null || bean.listData==null || bean.listData.size() ==0 ) {
					activity.RefreshComplete();
					activity.NoJianCaiComplete();
					if(PageIndex==1){
						No_layout.setVisibility(View.VISIBLE);
						ry_lay.setVisibility(View.GONE);
					}else{
//						toastShow("没有更多数据了");
						not_Tv.setVisibility(View.VISIBLE);
					}
				}
				if (bean!= null && bean.listData != null) {
					if(PageIndex==1){
						hzAdapter.setData(bean.listData);
					}else{
						hzAdapter.addData(bean.listData);
					}
					hzAdapter.notifyDataSetChanged();
					activity.RefreshComplete();
				}
			}
		};
		getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
				callBack);
	}
}
