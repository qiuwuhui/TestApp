package com.baolinetworktechnology.shejiquan.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.constant.OperateEnum;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.GongSiMyanliBean;
import com.baolinetworktechnology.shejiquan.domain.MyanliBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.google.gson.Gson;
import com.guojisheng.koyview.ActionSheetDialog;
import com.guojisheng.koyview.ActionSheetDialog.OnSheetItemClickListener;
import com.guojisheng.koyview.ActionSheetDialog.SheetItemColor;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyanliActivity extends BaseActivity implements OnClickListener,ILoadData{
	private List<MyanliBean> anliList=new ArrayList<MyanliBean>();
	private BitmapUtils mImageUtil;
	private RefreshListView mCaseListView;
	private CollectAdater adapter;	
	private int PageIndex=1;
	private String GUID;
	private int removepost;
	private int nullDdrawable = R.drawable.icon_wfbal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myanli);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.tv_title2).setOnClickListener(this);
		if(SJQApp.user!=null){
			GUID=SJQApp.user.guid;
		}
		mImageUtil = new BitmapUtils(this);
		//加载图片类
		mImageUtil.configDefaultLoadingImage(drawable.zixun_tu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		
		mCaseListView = (RefreshListView)findViewById(R.id.listViewCase);
//		mCaseListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
		mCaseListView.getPulldownFooter().isShowBottom(true);
		adapter = new CollectAdater();
		mCaseListView.setAdapter(adapter);
		mCaseListView.setOnLoadListener(this);
		mCaseListView.setOnRefreshing();
		mCaseListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				removepost=position-1;
				final MyanliBean bean= anliList.get(position-1);
				Intent intent = new Intent(MyanliActivity.this, WebOpusActivity.class);
				String url = AppUrl.DETAIL_CASE2 + bean.id;
				intent.putExtra("WEB_URL", url);
				intent.putExtra(AppTag.TAG_ID, bean.id);
				startActivity(intent);				
			}
		});
		mCaseListView.getRefreshableView().setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							final View view, final int position, long id) {
						removepost=position-1;
						final MyanliBean bean= anliList.get(position-1);
						View dialogView = View.inflate(MyanliActivity.this,
								R.layout.dialog_collect, null);
						TextView titl = (TextView) dialogView
								.findViewById(R.id.dialog_title);
						titl.setText("确定要删除案例？");
						final AlertDialog ad = new AlertDialog.Builder(
								MyanliActivity.this).setView(dialogView).show();
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
										deleteItem(bean);
										ad.cancel();
									}
								});

						return true;
					}
				});
	}

	@Override
	protected void setUpViewAndData() {

	}
	@Override
	protected void restartApp() {
		startActivity(new Intent(this, SplashActivity.class));
		finish();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_BACK_TO_HOME);
		switch (action) {
			case AppStatusConstant.ACTION_RESTART_APP:
				restartApp();
				break;
		}
	}
	private class CollectAdater extends BaseAdapter{

		@Override
		public int getCount() {
			return anliList.size();
//			return messgaelist.size();
		}
		@Override
		public Object getItem(int position) {
			if (position < 0 || position >= anliList.size())
				return null;
			return anliList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder vh = null;
			if (convertView == null) {
				convertView = View.inflate(parent.getContext(),
						R.layout.my_anli_layout, null);
				vh = new Holder();
				vh.anli_image=(ImageView) convertView.findViewById(R.id.anli_image);
				vh.anli_zt=(ImageView) convertView.findViewById(R.id.anli_zt);
				vh.al_name=(TextView) convertView.findViewById(R.id.al_name);
				vh.hitsTv=(TextView) convertView.findViewById(R.id.hitsTv);
				vh.numCommentTv=(TextView) convertView.findViewById(R.id.numCommentTv);
				vh.numFavoriteTv=(TextView) convertView.findViewById(R.id.numFavoriteTv);
				vh.shareTv=(TextView) convertView.findViewById(R.id.shareTv);
				convertView.setTag(vh);
			} else {
				vh = (Holder) convertView.getTag();
			}
			vh.anli_zt.setVisibility(View.VISIBLE);
			MyanliBean myanliBean=anliList.get(position);
			if(myanliBean.markStatus==50){
				vh.anli_zt.setBackgroundResource(R.drawable.ren_zhen_wei);
			}else if(myanliBean.markStatus==0){
				vh.anli_zt.setBackgroundResource(R.drawable.ren_zhen_zc);
			}else if(myanliBean.markStatus==20) {
				vh.anli_zt.setBackgroundResource(R.drawable.ren_zhen_dai);
			}else if(myanliBean.markStatus==30) {
				vh.anli_zt.setBackgroundResource(R.drawable.ren_zhen_zc);
			}else{
				vh.anli_zt.setBackgroundResource(R.drawable.ren_zhen_wei);
			}
			mImageUtil.display(vh.anli_image, myanliBean.getImages());
			vh.al_name.setText(myanliBean.title);
			vh.hitsTv.setText("阅读: "+myanliBean.getHits());
			vh.numCommentTv.setText("评论: "+myanliBean.getNumComment());
			vh.numFavoriteTv.setText("收藏: "+myanliBean.getNumFavorite());
			vh.shareTv.setText("被分享: "+myanliBean.getNumShare());
			return convertView;
		}
		
	}
	class Holder {
		ImageView anli_image,anli_zt;
		TextView al_name,hitsTv,numCommentTv,numFavoriteTv,shareTv;
	}
	@Override
	public void loadData(boolean isRefresh) {
		if(isRefresh){
			PageIndex=1;
		}else{
			PageIndex++;
        }
		loadata();	
		
	}
	private void loadata() {
		String url = AppUrl.DESINGNERCASELIST+"userGUID="+GUID
					+"&pageSize=10"+"&currentPage="+PageIndex;

		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				mCaseListView.setOnComplete();
				toastShow("网络连接错误，请检查网络");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				mCaseListView.setOnComplete();
				JSONObject json;
				GongSiMyanliBean bean=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, GongSiMyanliBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (bean == null || bean.listData.size() == 0) {
					if(PageIndex==1){
						mCaseListView.setOnNullData("你还没有发布过案例\n请登录www.sjq315.com发布案例");
						mCaseListView.setNullData(nullDdrawable);
					}else{
						mCaseListView.setOnNullNewsData();                    		
					}
				}
				if (bean!= null) {
                    if(PageIndex==1){
                    	setDate(bean);
                    }else{
                    	addDate(bean);
                    }
				  }
			}


		};
		getHttpUtils().send(HttpMethod.GET, AppUrl.API + url, getParams(url),
				callBack);
		
	}
	private void addDate(GongSiMyanliBean bean) {
		anliList.addAll(bean.listData);
		adapter.notifyDataSetChanged();
	}
	private void setDate(GongSiMyanliBean bean) {
		anliList.clear();
		anliList.addAll(bean.listData);
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.tv_title2:
			MobclickAgent.onEvent(getActivity(),"kControlDesignerMyCaseHelp"); //设计师我的作品帮助点击事件
			startActivity(new Intent(MyanliActivity.this,HelpActivity.class));
			break;

		default:
			break;
		}
		
	}
	private void deleteItem(MyanliBean news) {
		if (SJQApp.user != null) {
			String url = AppUrl.REMOVE;
			RequestParams params = new RequestParams();
			params.setHeader("Content-Type","application/json");
			try {
				JSONObject param  = new JSONObject();
				param.put("id",news.id);
				param.put("guid",news.guid);
				param.put("userGuid",SJQApp.user.getGuid());
				StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
				params.setBodyEntity(sEntity);
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			RequestCallBack<String> callBack = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					Gson gson = new Gson();
					SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
					if (bean != null) {
						if (bean.result.operatResult) {
							toastShow(bean.result.operatMessage);
							anliList.remove(removepost);
							adapter.notifyDataSetChanged();
						}else{
							toastShow(bean.result.operatMessage);
						}
					}
				}

				@Override
				public void onFailure(HttpException error, String arg1) {
					dialog.dismiss();
				}
			};
			getHttpUtils().send(HttpMethod.DELETE, AppUrl.API + url, params,
					callBack);

		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MyanliActivity");
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MyanliActivity");
	}
}
