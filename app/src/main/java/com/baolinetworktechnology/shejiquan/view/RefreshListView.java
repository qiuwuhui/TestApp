package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.util.AttributeSet;

import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;

public class RefreshListView extends BaseListView {
	ILoadData mILoadData;

	public interface ILoadData {
		/**
		 * 加载状态
		 * 
		 * @param isRefresh
		 *            是否刷新（false 加载更多）
		 */
		void loadData(boolean isRefresh);
	}

	/**
	 * 设置加载监听
	 * 
	 * @param onLoadListener
	 */
	public void setOnLoadListener(ILoadData onLoadListener) {
		mILoadData = onLoadListener;
	}

	public RefreshListView(Context context) {
		super(context);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void clickFooterView() {

	}

	@Override
	public void loadData(boolean isRefresh) {
		if (mILoadData != null) {
			mILoadData.loadData(isRefresh);
		}
	}

	/**
	 * 加载失败
	 */
	public void setOnFailure() {
		onRefreshComplete();
		mPulldownFooter.setState(PulldownFooter.STATE_FAILURE);
	}

	/**
	 * 加载完成，没有数据
	 */
	public void setOnNullData() {
		onRefreshComplete();
		setOnNullData("抱歉，没有找到您要的内容");
	}

	/**
	 * 加载完成，没有数据
	 *
	 * @param des
	 *            描述内容
	 */
	public void setOnNullData(String des) {
		onRefreshComplete();
		mPulldownFooter.setState(PulldownFooter.STATE_COMPLETE_NULL_DATA, des);
	}

	/**
	 * 加载更多 没数据
	 */
	public void setOnNullNewsData() {
		onRefreshComplete();
		setOnNullNewsData("没有更多数据");
	}


	/**
	 * 加载更多 没数据
	 */
	public void setOnNullNewsData(String des) {
		onRefreshComplete();
		mPulldownFooter.setState(PulldownFooter.STATE_COMPLETE_NULL_NEW_DATA,
				des);
	}

	public void setOnLoaing() {
		onRefreshComplete();
		mPulldownFooter.setState(PulldownFooter.STATE_LOGING);
	}

	public void setOnComplete() {
		onRefreshComplete();
		mPulldownFooter.setState(PulldownFooter.STATE_COMPLETE);
	}

	private OnStateListener onStateListener;

	@Override
	protected void onScrollStop() {
		super.onScrollStop();
		if (onStateListener != null)
			onStateListener.onStateListener(false);
	}

	public void setOnStateListener(OnStateListener onStateListener) {
		this.onStateListener = onStateListener;
	}

	/**
	 * 设置没数据时候 显示图标
	 * @param resDd 资源文件Id
	 */
	public void setNullData(int resDd) {
		mPulldownFooter.setNullData(resDd);
		
	}
	/**
	 * 加载失败
	 */
	public PulldownFooter getmPulldownFooter() {
		return mPulldownFooter;
	}

}
