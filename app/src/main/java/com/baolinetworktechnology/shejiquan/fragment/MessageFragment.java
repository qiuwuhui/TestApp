package com.baolinetworktechnology.shejiquan.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.adapter.MesageAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.domain.OrderLog;
import com.baolinetworktechnology.shejiquan.domain.ReadMessageBean;
import com.baolinetworktechnology.shejiquan.interfaces.IMeaseFragment;
import com.baolinetworktechnology.shejiquan.net.BaseNet.OnCallBackList;
import com.baolinetworktechnology.shejiquan.net.NetMessage;
import com.baolinetworktechnology.shejiquan.net.NetOrderLog;
import com.baolinetworktechnology.shejiquan.net.OnCallBack;
import com.baolinetworktechnology.shejiquan.net.OnCallBackBean;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.lidroid.xutils.exception.HttpException;
/**
 * 
 * 我的消息
 * 
 * @author JiSheng.Guo
 * 
 */
public class MessageFragment extends BaseFragment implements ILoadData,
		OnCallBack {
	private MesageAdapter adapter;
	public RefreshListView mListView;
	private View mRootView;
	private IMeaseFragment mIMessage;
	private OnCallBackList<OrderLog> messageCallBack;
	private MyDialog dialog;
	private int PageIndex = 1;
	private NetOrderLog netOrderLog;
	private TextView mRedTips;
	// private String OrderGuid;
	private boolean isOwner = false;
	private NetMessage netMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		SJQApp.isRrefresh = true;
		initView(inflater);
		// initData();

		return mRootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (SJQApp.user == null) {
			go2Login();
		} else {
			initData();
			if (SJQApp.isRrefresh) {

				mListView.setOnRefreshing(250);
				SJQApp.isRrefresh = false;
			}
		}

	}

	@Override
	public void onDestroy() {
		updateAllOrderIsRead();
		super.onDestroy();
	}

	void go2Login() {
		startActivity(new Intent(getActivity(), LoginActivity.class));
	}

	/**
	 * 初始化View
	 * 
	 * @param inflater
	 */
	private void initView(LayoutInflater inflater) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_measage, null);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);
		}

		mRedTips = (TextView) mRootView.findViewById(R.id.redTips);
		mListView = (RefreshListView) mRootView.findViewById(R.id.myListView);
		mRootView.findViewById(R.id.ivClear).setOnClickListener(this);
//		if (!SJQApp.user.isDesinger()) {
//			mRootView.findViewById(R.id.itemSystemMeage).setVisibility(
//					View.GONE);
//		} else {
//			mRootView.findViewById(R.id.itemSystemMeage).setOnClickListener(
//					this);
//		}
		mRootView.findViewById(R.id.itemCommentMeage).setOnClickListener(this);
		mListView.setOnLoadListener(this);

		messageCallBack = new OnCallBackList<OrderLog>() {

			@Override
			public void onNetSuccess(List<OrderLog> data) {

				mListView.setOnComplete();
				if (PageIndex == 1) {

					// 改变第一个已读
					// if (!TextUtils.isEmpty(OrderGuid)) {
					// if (data != null) {
					// if (data.size() > 0) {
					// if (data.get(0).getFromGUID().equals(OrderGuid)) {
					// doReadMesage(data.get(0).getID());
					// data.get(0).setIsRead("1");
					// }
					// }
					// }
					// }

					adapter.setData(data);
				} else {
					adapter.addData(data);
					if (data == null || data.size() == 0) {
						mListView.setOnNullNewsData();
					}
				}

				adapter.notifyDataSetChanged();

			}

			@Override
			public void onNetStart() {
				//
			}

			@Override
			public void onNetFailure(String mesa) {
				mListView.setOnFailure();
			}

			@Override
			public void onNetError(String json) {
				mListView.setOnFailure();
			}

			@Override
			public void onNetError(HttpException arg0, String mesa) {
				mListView.setOnFailure();
			}
		};

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击进入详情
				clickItem(position);
			}

		});

		// 长按监听事件
		mListView.getRefreshableView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						longClickItem(position);
						return true;

					}

				});

		dialog = new MyDialog(getActivity());
		adapter = new MesageAdapter();
		mListView.setAdapter(adapter);
		netOrderLog = new NetOrderLog();
		netMessage = new NetMessage();

	}

	/**
	 * ListView点击进入详情
	 * 
	 * @param position
	 */
	private void clickItem(int position) {

		OrderLog item = (OrderLog) adapter.getItem(position - 1);

		if (item == null)
			return;

		// 改变该条消息阅读状态
		doReadMesage(item.getID());
		if ("0".equals(item.getIsRead())) {
			item.setIsRead("1");
			adapter.notifyDataSetChanged();
		}

	}

	/**
	 * 长按事件
	 * 
	 * @param position
	 */
	private void longClickItem(int position) {
		mPosition = position - 1;
		final OrderLog item = (OrderLog) adapter.getItem(position - 1);
		new MyAlertDialog(getActivity()).setTitle("确定删除消息？").setBtnOk("删除")
				.setBtnCancel("取消").setBtnOnListener(new DialogOnListener() {

					@Override
					public void onClickListener(boolean isOk) {
						if (isOk) {

							netMessage.updateMessageStatus(
									MessageFragment.this,
									NetMessage.MessageGUID, item.getGUID(),
									NetMessage.MessageType, NetMessage.order);
						}

					}
				}).show();
	}

	private void initData() {
		switch (SJQApp.getUserEnumDientity()) {
		case USER_DESIGNER:
			isOwner = false;
			break;
		case USER_OWNER:
			isOwner = true;
			break;
		default:
			break;
		}
		adapter.setOwner(isOwner);

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

	/**
	 * 更改所有消息为已读
	 */
	protected void updateAllOrderIsRead() {
		if (SJQApp.user != null) {
			new NetMessage().updateAllOrderIsRead(null, SJQApp.user.guid);
		}

	}

	int mPosition = -1;

	public void setOwner() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mIMessage = (IMeaseFragment) activity;
		PageIndex = 1;

	}

	// 加载消息列表数据
	private void loadData() {
		if (SJQApp.user == null)
			return;
		if (isOwner) {
			netOrderLog.getHouseOrderLogList(messageCallBack, "UserGuid",
					SJQApp.user.guid, "LoggingType", "0",
					NetOrderLog.PageIndex, PageIndex + "",
					NetOrderLog.PageSize, "100", "r",
					System.currentTimeMillis() + "");
		} else {
			netOrderLog.getHouseOrderLogList(messageCallBack, "DesignerGuid",
					SJQApp.user.guid, "LoggingType", "0",
					NetOrderLog.PageIndex, PageIndex + "",
					NetOrderLog.PageSize, "100", "r",
					System.currentTimeMillis() + "");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.itemCommentMeage:

		case R.id.itemSystemMeage:
			if (mRedTips != null)
				mRedTips.setVisibility(View.GONE);
			mIMessage.click(v);
			if (SJQApp.user != null) {
				// netMessage.ChangeMeaseIsRead(null,
				// NetMessage.IsSystem,NetMessage.UserGuid,
				// SJQApp.user.UserGUID,NetMessage.UpdateType,
				// SJQApp.user.MarkName);
			}
			break;
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

	private void doClear() {
		if (SJQApp.user == null)
			return;
		netMessage.updateMessageStatus(this, NetMessage.UserGuid,
				SJQApp.user.guid, NetMessage.MessageType, NetMessage.order);

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
	public void onNetStart() {
		if (dialog != null)
			dialog.show();
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
	public void onDetach() {
		super.onDetach();
		if (dialog.isShowing())
			dialog.dismiss();
		dialog = null;
		mRedTips = null;
	}
}
