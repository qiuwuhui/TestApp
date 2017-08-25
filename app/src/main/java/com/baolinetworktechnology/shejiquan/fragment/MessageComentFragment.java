package com.baolinetworktechnology.shejiquan.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.interfaces.IMeaseFragment;

/**
 *评论消息
 * 
 * @author JiSheng.Guo
 * 
 */
public class MessageComentFragment extends BaseFragment {

	public ListView mListView;
	private View mRootView;
	IMeaseFragment mIMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_measage_comment, null);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);
		}

		TextView tv_title=(TextView) mRootView.findViewById(R.id.tv_title);
		tv_title.setText("评论消息");
		return mRootView;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mIMessage = (IMeaseFragment) activity;
	}

	@Override
	public void onClick(View v) {
		mIMessage.click(v);
	}
}
