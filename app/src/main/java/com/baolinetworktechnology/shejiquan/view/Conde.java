package com.baolinetworktechnology.shejiquan.view;

import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.view.Conde1.TimeCount;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Toast;

/**
 * 发送验证码按钮
 * 
 * @author gjs
 * 
 */
public class Conde extends Button {
	private TimeCount mTimeCount;

	public Conde(Context context) {
		super(context);
		initView();
	}

	public Conde(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();

	}
	

	//初始化View
	private void initView() {
		// 定时器
		int t = CacheUtils.getIntData(getContext(), CODE_TIME, 0);
		long current = CacheUtils.getLongData(getContext(), CURRENT_TIME);
		if (current != 0) {
			int between =(int) (( System.currentTimeMillis() - current)/ 1000);
			t=t-between;
		}

		if (t<=0 ) {
			mTimeCount = new TimeCount(60 * 1000, 1000);
			CacheUtils.cacheLongData(getContext(), CURRENT_TIME, System.currentTimeMillis());
			CacheUtils.cacheIntData(getContext(), CODE_TIME, 0);
		} else {
			mTimeCount = new TimeCount(t * 1000, 1000);
			mTimeCount.start();
		}

	}

	// 获取验证码

	void toastShow(Context context, String text) {

		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 开始倒计时
	 * 
	 */
	public void start() {
		mTimeCount.onCancel();
		int t = CacheUtils.getIntData(getContext(), CODE_TIME, 0);
		if (t == 0) {
			mTimeCount = new TimeCount(60 * 1000, 1000);
		}
		mTimeCount.start();
	}

	/**
	 * 停止
	 */
	public void stop() {
		setText("重新获取");
		setClickable(true);
		setEnabled(true);
		CacheUtils.cacheIntData(getContext(), CODE_TIME, 0);
		mTimeCount.onCancel();
		mTimeCount = new TimeCount(60 * 1000, 1000);
	}
	public void setConde() {
		setText("获取");
		setClickable(true);
		setEnabled(true);
		CacheUtils.cacheIntData(getContext(), CODE_TIME, 0);
		mTimeCount.onCancel();
		mTimeCount = new TimeCount(120 * 1000, 1000);
	}
	// 定时器类
	public class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			setText("重新获取");
			setEnabled(true);
			setClickable(true);
			CacheUtils.cacheIntData(getContext(), CODE_TIME, 0);

		}

		public void onCancel() {
			super.cancel();
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示

			int currenTiem = (int) (millisUntilFinished / 1000);
			if (isWait)
				return;
			setEnabled(false);
			setClickable(false);
			setText(currenTiem + "s重新获取");
			CacheUtils.cacheIntData(getContext(), CODE_TIME, currenTiem);
		}

	}

	String CODE_TIME = "CODE_TIME";
	String CURRENT_TIME = "CURRENT_TIME";// 当前脱离时间

	@Override
	protected void onDetachedFromWindow() {
		mTimeCount.onCancel();
		CacheUtils.cacheLongData(getContext(), CURRENT_TIME,
				System.currentTimeMillis());
		super.onDetachedFromWindow();
	}

	boolean isWait = false;

	/**
	 * 等待状态
	 */
	public void waitCode() {
		CacheUtils.cacheLongData(getContext(), CURRENT_TIME,
				System.currentTimeMillis());
		isWait = true;
	}

	/**
	 * 继续倒计时
	 */
	public void notifyCode() {
		isWait = false;
	}
}
