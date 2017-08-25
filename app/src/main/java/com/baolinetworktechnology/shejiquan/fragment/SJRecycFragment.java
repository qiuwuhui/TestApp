package com.baolinetworktechnology.shejiquan.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.adapter.CaseAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.CasePageList;
import com.baolinetworktechnology.shejiquan.view.ScrollGridLayoutManager;
import com.baolinetworktechnology.shejiquan.view.WrapContentHeightViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 案例收藏
 * 
 * @author JiSheng.Guo
 * 
 */
public class SJRecycFragment extends BaseFragment {
	private RecyclerView SJRecyclerView;
	private CaseAdapter mAdapter;
	private ScrollGridLayoutManager mLayoutManager;
	private String designerGudi;
	private int PageIndex=1;
	private WrapContentHeightViewPager vp;
	private TextView not_Tv;
	View view;
	private View no_layout;
	private View buyLayout;
	public SJRecycFragment(WrapContentHeightViewPager vp) {
		this.vp  = vp;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if(view == null){
			view = View.inflate(getActivity(), R.layout.sjview_recyclerviewe,
					null);
			vp.setObjectForPosition(view,0);
			not_Tv = (TextView) view.findViewById(R.id.not_Tv);
			SJRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
			mLayoutManager = new ScrollGridLayoutManager(SJQApp.applicationContext,1);
			mLayoutManager.setScrollEnabled(false);

			SJRecyclerView.setLayoutManager(mLayoutManager);
			mAdapter = new CaseAdapter(getActivity());
			SJRecyclerView.setAdapter(mAdapter);
			no_layout = view.findViewById(R.id.No_layout);
			buyLayout = view.findViewById(R.id.buyLayout);
		}
		return view;
	}
	public void setShuaxin(String guid){
//		buyLayout.getLayoutParams().height = LayoutTop;
		designerGudi=guid;
		loadata();
	}
	public void setdesignerGudi(int pageIndex){
		     this.PageIndex=pageIndex;
		     loadata();
	}
	private void loadata() {
		String url = AppUrl.CASELISET+"userGUID="+designerGudi+"&orders=12"+"&attrHouseType=0&attrStyle=0"
				+"&pageSize=15"+"&currentPage="+PageIndex;
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow("服务器连接错误");
			}

			@Override
			public void onSuccess(ResponseInfo<String> n) {
				JSONObject json;
				String result1="";
				CasePageList newsBean=null;
				try {
					json = new JSONObject(n.result);
					result1=json.getString("result");
					Gson gson = new Gson();
					newsBean=gson.fromJson(result1, CasePageList.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(SJRecyclerView == null){
					return;
				}
				if (newsBean != null && newsBean.listData != null) {
					WeiShopActivity activity= (WeiShopActivity) getActivity();
					if(activity == null){
						return;
					}
					if(newsBean.listData.size()==0){
						activity.RefreshComplete();
						activity.NoAnLiComplete();
						if(PageIndex == 1){
							no_layout.setVisibility(View.VISIBLE);
						}else{
							not_Tv.setVisibility(View.VISIBLE);
//							toastShow("没有更多数据了");
						}
						return;
					}
					if(PageIndex==1){
						mAdapter.setList(newsBean.listData);
					}else{
						mAdapter.addList(newsBean.listData);
					}
					mAdapter.notifyDataSetChanged();
					activity.RefreshComplete();
				}
			}


		};
		getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API+ url, getParams(url),
				callBack);
	}
	public void onDestroy() {
		super.onDestroy();
		SJRecyclerView.setAdapter(null);
		System.gc();
	}
}
