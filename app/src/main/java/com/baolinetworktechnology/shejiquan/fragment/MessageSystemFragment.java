package com.baolinetworktechnology.shejiquan.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.ShenjiActivity;
import com.baolinetworktechnology.shejiquan.activity.SmActivity;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.adapter.MesageSystemAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.MesageBean;
import com.baolinetworktechnology.shejiquan.domain.OrderLog;
import com.baolinetworktechnology.shejiquan.domain.SheJiShianliBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.sjanliBean;
import com.baolinetworktechnology.shejiquan.net.BaseNet.OnCallBackList;
import com.baolinetworktechnology.shejiquan.net.NetMessage;
import com.baolinetworktechnology.shejiquan.net.OnCallBack;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.PulldownFooter;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 系统消息
 * 
 * @author JiSheng.Guo
 * 
 */
public class MessageSystemFragment extends BaseFragment implements
		OnCallBackList<MesageBean>, ILoadData, OnCallBack {
	private MyDialog dialog;
	private RefreshListView mListView;
	private View mRootView;
	// private IMeaseFragment mIMessage;
	private int PageIndex=1;
	private NetMessage netMessage;
	private MesageSystemAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (SJQApp.user == null) {
			getActivity().finish();
		}
		if (mRootView == null) {
			mRootView = inflater
					.inflate(R.layout.fragment_measage_system, null);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);
		}
		mListView = (RefreshListView) mRootView.findViewById(R.id.listView);
		mRootView.findViewById(R.id.ivClear).setOnClickListener(this);
		dialog = new MyDialog(getActivity());
		netMessage = new NetMessage();
		adapter = new MesageSystemAdapter();
		mListView.setOnLoadListener(this);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// 点击进入详情
			MesageBean item = (MesageBean) adapter.getItem(position - 1);
			clickItem(position);
			if(item.getSkipType().equals("CASE")){
				Intent intent = new Intent(getActivity(), WebOpusActivity.class);
				String url = ApiUrl.DETAIL_CASE2 + item.getFromGuid()+"&r="+System.currentTimeMillis();
				intent.putExtra("WEB_URL", url);
				intent.putExtra("classTitle", "");
				intent.putExtra(WebDetailActivity.ISCASE, true);
				getContext().startActivity(intent);
			}else if(item.getSkipType().equals("VERIFY")){
				if(item.getTitle().equals("您未通过实名认证")){
					startActivity(new Intent(getActivity(), ShenjiActivity.class));
				}else{
					startActivity(new Intent(getActivity(), SmActivity.class));
				}
			}else if(item.getSkipType().equals("NEW")){

			}

			}

		});
		mListView.getRefreshableView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						mPosition = position - 1;
						final MesageBean item = (MesageBean) adapter
								.getItem(position - 1);
						new MyAlertDialog(getActivity()).setTitle("确定删除消息？")
								.setBtnOk("删除").setBtnCancel("取消")
								.setBtnOnListener(new DialogOnListener() {

									@Override
									public void onClickListener(boolean isOk) {
										if (isOk) {
											netMessage.updateMessageStatus(
													MessageSystemFragment.this,
													NetMessage.MessageGUID,
													item.getGUID(),
													NetMessage.MessageType,
													NetMessage.message);
										}
									}
								}).show();
						return true;

					}
				});
		loadData();
		// 改变该条消息阅读状态
		if(SJQApp.messageBean!=null){
			if(SJQApp.messageBean.getResult().getNumMessage()!=0){
				allMesage();
			}
		}
		return mRootView;
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	private void loadData() {

		netMessage
				.Search(this, NetMessage.UserGuid, SJQApp.user.guid,NetMessage.MessageType,"0",
						NetMessage.PageIndex, PageIndex + "",
						NetMessage.PageSize, "10");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// mIMessage = (IMeaseFragment) activity;
	}

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivClear:
			new MyAlertDialog(getActivity()).setTitle("确定清空消息？").setBtnOk("清空")
					.setBtnCancel("取消")
					.setBtnOnListener(new DialogOnListener() {

						@Override
						public void onClickListener(boolean isOk) {
							if (isOk) {
								mPosition = -1;
								doClear();
							}

						}
					}).show();

			break;
		default:
			break;
		}

	}

	int mPosition = -1;

	private void doClear() {
		if (SJQApp.user == null)
			return;
		netMessage.updateMessageStatus(this, NetMessage.UserGuid,
				SJQApp.user.guid, NetMessage.MessageType,
				NetMessage.message);

	}
	/**
	 * ListView点击进入详情
	 *
	 * @param position
	 */
	private void clickItem(int position) {

		MesageBean item = (MesageBean) adapter.getItem(position - 1);

		if (item == null)
			return;
		if ("0".equals(item.getIsRead())) {
			item.setIsRead("1");
			adapter.notifyDataSetChanged();
		}

	}
	@Override
	public void onNetStart() {
		if (dialog != null)
			dialog.show();
	}
	//阅读状态全部改变为已读
	protected void allMesage() {
		String url = AppUrl.UpdateMessageRead;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("userGuid", SJQApp.user.getGuid());
			param.put("messageType", "0");
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow(message);
			}

			@Override
			public void onSuccess(ResponseInfo<String> n) {
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
				if (bean != null) {
					if (bean.result.operatResult) {
						SJQApp.messageBean.getResult().setNumMessage(0);
						Intent intent = new Intent();
						intent.setAction("ReadMag");
						getActivity().sendBroadcast(intent);
					}
				}
			}
		};
		getHttpUtils().send(HttpRequest.HttpMethod.PUT, AppUrl.API+ url,params,
				callBack);
	}
	/**
	 * 改变阅读状态
	 */
	protected void doReadMesage(String id) {
		if (SJQApp.user != null) {
			new NetMessage().ChangeMeaseIsRead(null, NetMessage.IsOrder,
					NetMessage.UserGuid, SJQApp.user.guid,
					NetMessage.UpdateType, SJQApp.user.markName, "ID", id);
		}

	}
	@Override
	public void onNetSuccess(List<MesageBean> data) {
		if (dialog != null)
			dialog.dismiss();
		mListView.setOnComplete();
		if (PageIndex == 1) {
			adapter.setData(data);
			if (data == null || data.size() == 0) {
				mListView.setOnNullData("暂无新的系统消息");
				mListView.setNullData(R.drawable.icon_xxwdl);
			}
			
		} else {
			adapter.addData(data);
			if (data == null || data.size() == 0) {
				mListView.setOnNullNewsData();
			}
		}

		adapter.notifyDataSetChanged();

	}

	@Override
	public void onNetFailure(String mesa) {
		if (dialog != null)
			dialog.dismiss();
		mListView.setOnFailure();
	}

	@Override
	public void onNetError(HttpException arg0, String mesa) {
		if (dialog != null)
			dialog.dismiss();
		mListView.setOnFailure();
	}

	@Override
	public void onNetError(String json) {
		if (dialog != null)
			dialog.dismiss();
		mListView.setOnFailure();
	}

	@Override
	public void loadData(boolean isRefresh) {
		if (isRefresh) {
			PageIndex = 1;
		} else {
			PageIndex++;
		}
		loadData();
	}

	@Override
	public void onSuccess(String mesa) {
		if (dialog != null)
			dialog.dismiss();
		toastShow(mesa);
		if (adapter != null) {
			if (mPosition == -1) {
				adapter.setData(null);
			} else {
				adapter.remove(mPosition);
			}
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onFailure(String mesa) {
		if (dialog != null)
			dialog.dismiss();
		toastShow(mesa);

	}

	@Override
	public void onError(HttpException arg0, String mesa) {
		if (dialog != null)
			dialog.dismiss();
		toastShow(R.string.network_error);

	}

	@Override
	public void onError(String json) {
		if (dialog != null)
			dialog.dismiss();
		toastShow(R.string.network_sorry);

	}
	
	@Override
	public void onDestroy() {
		if(dialog != null){
			dialog=null;
		}
		super.onDestroy();
		
	}
}
