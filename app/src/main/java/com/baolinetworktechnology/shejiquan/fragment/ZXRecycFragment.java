package com.baolinetworktechnology.shejiquan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.ReleaseActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.adapter.CaseAdapter;
import com.baolinetworktechnology.shejiquan.adapter.OwnerPotAdapter;
import com.baolinetworktechnology.shejiquan.adapter.Periodadapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.CasePageList;
import com.baolinetworktechnology.shejiquan.domain.NineGridTestModel;
import com.baolinetworktechnology.shejiquan.domain.PostMainBean;
import com.baolinetworktechnology.shejiquan.view.ScrollGridLayoutManager;
import com.baolinetworktechnology.shejiquan.view.WrapContentHeightViewPager;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 设计师个人帖子
 * 
 * @author JiSheng.Guo
 * 
 */
public class ZXRecycFragment extends BaseFragment {
	private RecyclerView ZXRecyclerView;
	private ScrollGridLayoutManager mLayoutManager;
	private String designerGudi;
	private int PageIndex=1;
	private OwnerPotAdapter mAdapter;
	private WrapContentHeightViewPager vp;
	private TextView not_Tv;
	View view;
	private View no_layout;
	public ZXRecycFragment(WrapContentHeightViewPager vp) {
		this.vp  = vp;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if(view ==null){
			view = View.inflate(getActivity(), R.layout.view_recyclerviewe,
					null);
			vp.setObjectForPosition(view,2);
			ZXRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
			mLayoutManager = new ScrollGridLayoutManager(SJQApp.applicationContext,1);
			mLayoutManager.setScrollEnabled(false);
			ZXRecyclerView.setLayoutManager(mLayoutManager);
			mAdapter = new OwnerPotAdapter(getActivity());
			ZXRecyclerView.setAdapter(mAdapter);
			no_layout = view.findViewById(R.id.No_layout);
			not_Tv = (TextView) view.findViewById(R.id.not_Tv);
		}
		return view;
	}
	public void setdesignerGudi(int pageIndex){
		     this.PageIndex=pageIndex;
		     loadata();
	}
	public void setShuaxin(String guid){
		designerGudi=guid;
		loadata();
	}
	private void loadata() {
		String url = AppUrl.GETPAGELIST+"pageSize=10"+"&currentPage="+PageIndex+"&userGUID="+ designerGudi+"&IsRefresh=true";
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow("网络连接错误，请检查网络");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject json;
				PostMainBean bean=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, PostMainBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				WeiShopActivity activity= (WeiShopActivity) getActivity();
				if(activity == null){
					return;
				}
				if (bean == null || bean.listData==null || bean.listData.size() ==0 ) {
					activity.RefreshComplete();
					activity.NoTieZiComplete();
					if(PageIndex==1){
                         no_layout.setVisibility(View.VISIBLE);
					}else{

						not_Tv.setVisibility(View.VISIBLE);
//						toastShow("没有更多数据了");
					}
				}

				if (bean!= null && bean.listData != null) {
					if(PageIndex==1){
						mAdapter.setList(bean.listData);
					}else{
						mAdapter.addList(bean.listData);
					}
					mAdapter.notifyDataSetChanged();
					activity.RefreshComplete();
				}
			}
		};
		getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
				callBack);
	}
	public void onDestroy() {
		super.onDestroy();
		ZXRecyclerView.setAdapter(null);
		System.gc();
	}
}
