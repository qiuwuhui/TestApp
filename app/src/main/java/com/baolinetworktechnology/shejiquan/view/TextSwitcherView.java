package com.baolinetworktechnology.shejiquan.view;

import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.HouOrderList;
import com.baolinetworktechnology.shejiquan.domain.HouOrderText;
import com.baolinetworktechnology.shejiquan.manage.CityService;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * 上下轮播TextView
 * 
 * @author JiSheng.Guo
 * 
 */
public class TextSwitcherView extends TextSwitcher implements ViewFactory {
	private boolean isDetached = false;
	private List<HouOrderList> mData = new ArrayList<HouOrderList>();
	private final int HANDLER_CHANGE_TEXT = 50;
	private Thread mThread;

	public interface ISwitcher {
		String getTitle();

	}

	/**
	 * 设置显示数据集合 ， 类需实现ISwitcher
	 * 
	 * @param data
	 */
	public void setData(List<HouOrderList> data) {
		if (data != null) {
			mData.clear();
			mData.addAll(data);
			initThread();
		}
	}

	public void addData(List<HouOrderList> data) {
		if (data != null) {
			mData.addAll(data);
			initThread();
		}
	}

	public void addData(ArrayList<HouOrderList> data) {
		if (data != null) {
			mData.addAll(data);
			initThread();
		}
	}

	public void addData(HouOrderList data) {
		if (data != null) {
			mData.add(data);
			initThread();
		}
	}

	/**
	 * 设置显示数据集合 ， 类需实现ISwitcher
	 * 
	 * @param data
	 */
	public void setData(ArrayList<HouOrderList> data) {
		if (data != null) {
			mData.clear();
			mData.addAll(data);
			initThread();
		}

	}

	private void initThread() {
		if (mThread == null) {
			mThread = new Thread(new Runnable() {

				@Override
				public void run() {

					while (!isDetached) {
						if (mHandler != null) {// view不脱离
							if (hasWindowFocus() && isShown()
									&& getVisibility() == VISIBLE) {// view可见
								Message msg = Message.obtain();
								msg.what = HANDLER_CHANGE_TEXT;
								mHandler.sendMessage(msg);
							}
						}
						SystemClock.sleep(mTime);
					}

				}
			});

			mThread.start();
		}
	}

	public TextSwitcherView(Context context) {
		super(context);
		initView();
	}

	AttributeSet attrs;

	public TextSwitcherView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.attrs = attrs;
		initView();

	}

	private void initView() {
		setFactory(this);
		TranslateAnimation in = new TranslateAnimation(0, 0, dp2px(24), 0);
		in.setDuration(500);
		TranslateAnimation out = new TranslateAnimation(0, 0, 0, dp2px(-20));
		out.setDuration(500);
		setInAnimation(in);
		setOutAnimation(out);

	}

	@Override
	public View makeView() {
		TextView t = new TextView(getContext(), attrs);
		return t;
	}

	float dp2px(int dp) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return density * dp + 0.5f;
	}

//	Handler handler;
//	Runnable mRunnable = new Runnable() {
//
//		@Override
//		public void run() {
//			// handler自带方法实现定时器
//			setText(mData.get(index).getTitle());
//		}
//	};
	private int index = 0;
	MyHandler mHandler = new MyHandler();
	private int mTime = 3000;

	class MyHandler extends Handler {

		// 子类必须重写此方法，接受数据
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case HANDLER_CHANGE_TEXT:
				if (mData.size() != 0) {
					if (index >= mData.size()) {
						index = 0;
					}
					if(mData.get(index).getExpectDesigner().equals("00000000-0000-0000-0000-000000000000")){
						if(mData.get(index).getCityID()==0){
							setText("业主委托: "+mData.get(index).getName()+"发布了委托");							
						}else{
							City city2 = CityService.getInstance(getContext()).getCityDB()
									.getCityID(mData.get(index).getCityID()+ "");
							StringBuffer sb = new StringBuffer();
							sb.append(city2.Title);
							String Title=sb.toString();
							setText("业主委托: "+mData.get(index).getName()+"发布了"+Title+"的委托");	
						}
						
					}else{
						setText("被邀请: "+mData.get(index).getDesignerName()+"被业主邀请设计");	
					}
					index++;
				}
				break;

			default:

			}

		}
	}

	@Override
	protected void onDetachedFromWindow() {
		isDetached = true;
		mHandler = null;
		super.onDetachedFromWindow();
	}

	public void setDuration(int time) {
		mTime = time;

	}
}
