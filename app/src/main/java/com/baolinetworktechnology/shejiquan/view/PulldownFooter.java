package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;

/**
 * ListView 底部视图
 * 
 * @author JiSheng.Guo
 * 
 */
public class PulldownFooter {
	/**
	 * 空闲状态
	 */
	public static final int STATE_FREE = 0x13;//
	/**
	 * 加载中
	 */
	public static final int STATE_LOGING = 0x10;//
	/**
	 * 加载完成
	 */
	public static final int STATE_COMPLETE = 0x11;//
	/**
	 * 加载完成，没有数据
	 */
	public static final int STATE_COMPLETE_NULL_DATA = 0x12;//
	/**
	 * 加载完成，没有更多数据
	 */
	public static final int STATE_COMPLETE_NULL_NEW_DATA = 0x14;//
	/**
	 * 加载失败
	 */
	public static final int STATE_FAILURE = 0x16;//
	private TextView mPulldownFooterText;
	private View mPulldownFooterLoading;
	private ImageView mIvNullData;
	private Context mContext;
	private boolean IsPull=true;
	View bottom;
	public PulldownFooter(Context context, View view) {
		// super(context);
		mContext = context;
		initView(context, view);
	}

	//

	private void initView(Context context, View view) {
		// View view = View.inflate(context, R.layout.pulldown_footer, null);
		mPulldownFooterText = (TextView) view
				.findViewById(R.id.pulldown_footer_text);
		mPulldownFooterLoading = view
				.findViewById(R.id.pulldown_footer_loading);
		mIvNullData = (ImageView) view.findViewById(R.id.ivNullData);
//		bottom = view.findViewById(R.id.bottom);
		// addView(view);

	}

	public void setNullData(int resId) {
		mIvNullData.setImageResource(resId);
	};

	// 根据不同状态，显示不同效果
	public void setState(int state) {

		setState(state, mContext.getResources().getString(R.string.NULL_DATA));
	}

	private void doFailure() {
		mPulldownFooterLoading.setVisibility(View.GONE);
		mIvNullData.setVisibility(View.GONE);
		mPulldownFooterText.setVisibility(View.VISIBLE);
		mPulldownFooterText.setText(mContext.getResources().getString(
				R.string.LOAD_FAILURE));

	}

	public void isShowBottom(boolean show) {
		if (show) {
//			bottom.setVisibility(View.VISIBLE);
		} else {
//			bottom.setVisibility(View.GONE);
		}
	}

	public void setText(String text) {
		mPulldownFooterLoading.setVisibility(View.GONE);
		mIvNullData.setVisibility(View.GONE);
		mPulldownFooterText.setVisibility(View.VISIBLE);
		mPulldownFooterText.setText(text);
	}

	private void doNullNewData() {
		mPulldownFooterLoading.setVisibility(View.GONE);
		mIvNullData.setVisibility(View.GONE);
		mPulldownFooterText.setVisibility(View.VISIBLE);
		mPulldownFooterText.setText("没有更多了...");
	}

	private void doFree() {
		mPulldownFooterLoading.setVisibility(View.GONE);
		mPulldownFooterText.setVisibility(View.GONE);
		mIvNullData.setVisibility(View.GONE);
	}

	private void doCompleteNullData(String tips) {
		mPulldownFooterLoading.setVisibility(View.GONE);
		mPulldownFooterText.setVisibility(View.VISIBLE);
		mIvNullData.setVisibility(View.VISIBLE);
		mPulldownFooterText.setText(tips);
	}

	private void doComplete() {
		mPulldownFooterLoading.setVisibility(View.GONE);
		mPulldownFooterText.setVisibility(View.VISIBLE);
		mPulldownFooterText.setText("");
		mIvNullData.setVisibility(View.GONE);
	}

	// 加载中
	String pulldownLogingText = "正在加载中";

	private void doLoging() {
		mPulldownFooterLoading.setVisibility(View.VISIBLE);
		mPulldownFooterText.setVisibility(View.VISIBLE);
		mIvNullData.setVisibility(View.GONE);
		mPulldownFooterText.setText(pulldownLogingText);
	}

	public void setState(int state, String string) {
		if (mPulldownFooterText == null)
			return;
		switch (state) {
		case STATE_LOGING:// 加载中
			if(IsPull){
				doLoging();
			}
			IsPull=true;
			break;
		case STATE_COMPLETE:// 加载完成
			doComplete();
			break;
		case STATE_COMPLETE_NULL_DATA:// 加载完成 没数据
			doCompleteNullData(string);
			break;
		case STATE_FREE:// 空闲状态
			doFree();
			break;
		case STATE_COMPLETE_NULL_NEW_DATA:// 没有新数据
			IsPull=false;
			doNullNewData();
			break;
		case STATE_FAILURE:
			doFailure();
			break;
		default:

			break;
		}

	}

}
