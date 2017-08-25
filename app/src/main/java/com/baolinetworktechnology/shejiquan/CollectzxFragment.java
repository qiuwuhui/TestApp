package com.baolinetworktechnology.shejiquan;

import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.activity.WeizxActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseGZZhuanxiu;
import com.baolinetworktechnology.shejiquan.domain.CasezxGz;
import com.baolinetworktechnology.shejiquan.domain.DataBen;
import com.baolinetworktechnology.shejiquan.fragment.BaseFragment;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class CollectzxFragment extends BaseFragment implements OnClickListener,ILoadData{
	private RefreshListView mCaseListView;
	private List<CasezxGz> Designdata=new ArrayList<CasezxGz>();	
	private BitmapUtils mImageUtil;
	private ZhuanXiuGzAdaoter adapter;
	private View no_layout;
	private int PageIndex=1;
	private MyDialog dialog;
	private int removepost;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_collectzx,
				null);
		dialog = new MyDialog(getActivity());
		no_layout = view.findViewById(R.id.No_layout);
		mImageUtil = new BitmapUtils(getActivity());
		//加载图片类
		mImageUtil.configDefaultLoadingImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		mCaseListView = (RefreshListView) view.findViewById(R.id.listViewCase);
		mCaseListView.getPulldownFooter().isShowBottom(true);
		adapter = new ZhuanXiuGzAdaoter(getActivity());
		mCaseListView.setAdapter(adapter);
		mCaseListView.setOnLoadListener(this);
		mCaseListView.setOnRefreshing();
		mCaseListView.getRefreshableView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							final View view, final int position, long id) {
						final String designerGudi=adapter.getItem(position-1).GUID;
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
										quxiao(designerGudi);
										ad.cancel();
									}
								});

						return true;
					}
				});
		mCaseListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				    removepost=position-1;
				    CasezxGz casezx=adapter.getItem(position-1);
					if (casezx == null)
						return;
					Intent intent = new Intent(getActivity(),WeizxActivity.class);
					intent.putExtra("businessID", casezx.getId()+"");
					intent.putExtra("GUID", casezx.getGUID()+"");
					getActivity().startActivity(intent);
				
			}
		});
		return view;
	}
	
	@Override
	public void loadData(boolean isRefresh) {
		if(isRefresh){
			PageIndex=1;
		}else{
			PageIndex++;
        }
		no_layout.setVisibility(View.GONE);
		loadata();	
		
	}
	private void loadata() {
		String url = ApiUrl.GET_BUSUINESS_LIST+SJQApp.user.guid
				+"&PageIndex="+PageIndex+"&PageSize=10"+"&R="+System.currentTimeMillis(); 	
		dialog.show();
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				mCaseListView.setOnComplete();
				toastShow(message);
				dialog.dismiss();
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (mCaseListView == null || adapter == null)
					return;
				CaseGZZhuanxiu casedata = CommonUtils.getDomain(responseInfo,
						CaseGZZhuanxiu.class);
				
					if (casedata == null || casedata.data.size() == 0) {
						if(PageIndex==1){
							no_layout.setVisibility(View.VISIBLE);
						}else{
							mCaseListView.setOnNullNewsData("没有更多了");                		
						}
					}
					if(casedata!=null){
						if(PageIndex==1){
							adapter.setData(casedata.data);
							Designdata.clear();
							Designdata.addAll(casedata.data);
						}else{
							adapter.addData(casedata.data);
							Designdata.addAll(casedata.data);
						}
					}			
        		dialog.dismiss();
        		mCaseListView.setOnComplete();
			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);		
	}
	private void quxiao(String designerGudi){
		String url = ApiUrl.FAVORITE_CANCEL;
		RequestParams params = getParams(url);
		params.addBodyParameter("ClassType", AppTag.Sql_gs);
		params.addBodyParameter("Type", AppTag.Sql_gz);
		params.addBodyParameter("UserGUID", SJQApp.user.guid);
		params.addBodyParameter("FromGUID", designerGudi);

		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LogUtils.i("设计师详情头部", arg0.result);
				DataBen data = CommonUtils.getDomain(arg0, DataBen.class);
				if (data != null) {
					if (data.success) {
						Designdata.remove(removepost);
						adapter.setData(Designdata);
						adapter.notifyDataSetChanged();
					} else {
						toastShow("取消失败");
					}
				} else {
					toastShow("取消失败");
				}
			}

			@Override
			public void onFailure(HttpException error, String arg1) {
				toastShow(getResources().getString(R.string.network_error));
			}
		};
		getHttpUtils().send(HttpMethod.POST, ApiUrl.API + url, params,
				callBack);
	}
}
