package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.MyFragmentActivity;
import com.baolinetworktechnology.shejiquan.activity.WebActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.domain.Items;
import com.baolinetworktechnology.shejiquan.domain.LunBoBean;
import com.baolinetworktechnology.shejiquan.domain.brandSidle;
import com.baolinetworktechnology.shejiquan.domain.brandSidleList;
import com.lidroid.xutils.BitmapUtils;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 轮播图
 * 
 * @author gjs
 * 
 * 
 */
public class KoySliBrand extends FrameLayout implements OnPageChangeListener {
	private ViewPager fs_vp;// 图片
	//private TextView tv_pager_description;// 描述信息
	private LinearLayout ll_pager_point_group;// 点
	private brandSidleList listData;
	private SlidAdapter slidAdapter;

	public KoySliBrand(Context context) {
		super(context);
		initView();
	}

	public KoySliBrand(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public void setHeight(int height) {
		fs_vp.getLayoutParams().height = px2dp(height);
	}

	private int px2dp(float pxValue) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (pxValue * scale + 0.5f);
	}

	public void addDataOk() {
		initPointGroup();
	}

	public void clearData() {
		listData.result.clear();
	}

	public void addDataOk(boolean autoPaly) {

		initPointGroup();
		if (autoPaly)
			setAutoPlay();
	}

	private int Height = 1;

	public int getHeights() {
		return Height;
	}

	// 设置要播放的 数据源（图片url，标题，链接等）
	public void setDatas(brandSidleList listData) {
		setVisibility(View.VISIBLE);
		this.listData = listData;
		if (slidAdapter == null) {
			slidAdapter = new SlidAdapter();
			fs_vp.setAdapter(slidAdapter);
		}
		initPointGroup();
		fs_vp.setCurrentItem(1);

	}

	// 设置自动播放默认4500
	public void setAutoPlay() {
		setAutoPlay(4500);
	}

	TimerTask task = null;
	int what = 0;
	Timer timer = null;

	// 设置自动播放时间
	public void setAutoPlay(final long period) {
		what = 2;
		if (task == null) {
			synchronized (KoySliBrand.class) {
				if (task == null) {
					task = new TimerTask() {

						@Override
						public void run() {
							Message message = Message.obtain();
							message.what = what;
							switch (what) {
							case 0:
								break;
							case 1:
								SystemClock.sleep(period);
								if (handler != null)
									handler.sendMessage(message);
								break;
							case 2:
								if (handler != null)
									handler.sendMessage(message);
								break;
							case 3:
								break;
							}

						}
					};
					if (timer == null) {
						timer = new Timer();
						timer.schedule(task, period + 1000, period);

					}
				}

			}
		}

	}

	// 取消自动播放
	public boolean cancelAutoPlay() {
		if (task != null) {
			what = 0;
			return true;
		}
		return false;
	}

	public void pause() {
		what = 3;
	}

	public void restart() {
		what = 2;
	}

	private void initView() {
		View slidView = View.inflate(getContext(), R.layout.view_lunbo, null);
		if (slidView == null)
			return;
		fs_vp = (ViewPager) slidView.findViewById(R.id.fs_vp);
		fs_vp.setOffscreenPageLimit(5);
		ll_pager_point_group = (LinearLayout) slidView
				.findViewById(R.id.ll_pager_point_group);
		fs_vp.setOnPageChangeListener(this);
		setViewPagerScrollSpeed();
		addView(slidView);
	}

	/**
	 * 设置ViewPager的滑动速度
	 * 
	 * */
	private void setViewPagerScrollSpeed() {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(
					fs_vp.getContext(), 1500);
			mScroller.set(fs_vp, scroller);
		} catch (NoSuchFieldException e) {

		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}

	/**
	 * 定时器，实现自动播放
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:// 外部取消

				break;

			case 1:// 暂停，一段时间后，会继续自动播放
				restart();

				break;

			case 2:
				if (fs_vp == null) {
					return;
				}
				if (isShown() && getVisibility() == VISIBLE && hasWindowFocus()) {
					int dex = fs_vp.getCurrentItem() + 1;
					fs_vp.setCurrentItem(dex, true);

				}
				break;
			}
		}
	};

	private class SlidAdapter extends PagerAdapter implements OnClickListener {

		private BitmapUtils bitmapUtils;

		public SlidAdapter() {
			if (bitmapUtils == null) {
				bitmapUtils = new BitmapUtils(getContext()
						.getApplicationContext());
				bitmapUtils.configDefaultLoadingImage(R.drawable.default_icon);
				bitmapUtils
						.configDefaultLoadFailedImage(R.drawable.default_icon);
			}

		}

		@Override
		public int getCount() {
			if (listData == null || listData.result == null
					|| listData.result.size() == 0) {
				setVisibility(View.GONE);
				return 0;
			}
			return listData.result.size() + 2;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (position == 0) {
				position = listData.result.size() - 1;
			} else if (position == getCount() - 1) {
				position = 0;
			} else {
				position--;
			}

			ImageView iv = (ImageView) View.inflate(getContext(),
					R.layout.viewpage_item, null);

			iv.setOnClickListener(this);
			// 根据图片的连接地址去请求图片并且设置给iv上.

			if (iv.getTag() == null
					|| iv.getTag() != listData.result.get(position)) {
				bitmapUtils.display(iv, listData.result.get(position).getImage());
				iv.setTag(listData.result.get(position));
			}

			container.addView(iv); // 把imageview添加到viewpager中
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public void onClick(View v) {
			brandSidle data = (brandSidle) v.getTag();
		}
	}

	private void initPointGroup() {
		if (listData == null || listData.result == null)
			return;
		if (slidAdapter == null) {
			slidAdapter = new SlidAdapter();
			fs_vp.setAdapter(slidAdapter);
		} else {
			slidAdapter.notifyDataSetChanged();
		}
		ll_pager_point_group.removeAllViews();
		if(listData.result.size()>1){
			for (int i = 0; i < listData.result.size(); i++) {

				ImageView view = new ImageView(getContext());
				view.setPadding(px2dp(2), px2dp(2), px2dp(2),  px2dp(2));
				view.setImageResource(R.drawable.point_bg);
				if (i == 0) {
					//tv_pager_description.setText(listData.ItemList.get(0).Title);
					view.setEnabled(true);
				} else {
					view.setEnabled(false);
				}
				ll_pager_point_group.addView(view);
			}
		}
	}

	public ImageView getView() {
		return (ImageView) fs_vp.getChildAt(fs_vp.getCurrentItem());

	}

	public void setWhat(int w) {
		what = w;
	}

	@Override
	protected void onDetachedFromWindow() {
		if (timer != null) {
			timer.cancel();
			timer = null;
			handler = null;
		}
		super.onDetachedFromWindow();

	}

	@Override
	public void onPageSelected(int location) {
		if (location == 0) {
			location = ll_pager_point_group.getChildCount() - 1;
		} else if (location == slidAdapter.getCount() - 1) {
			location = 0;
		} else {
			location--;
		}

		if (ll_pager_point_group.getChildCount() == listData.result.size()) {
			for (int i = 0; i < listData.result.size(); i++) {
				ll_pager_point_group.getChildAt(i).setEnabled(false);
			}

			ll_pager_point_group.getChildAt(location).setEnabled(true);
		}

	}

	// 0 123 4

	@Override
	public void onPageScrolled(int location, float arg1, int arg2) {
		if (arg1 != 0 || slidAdapter == null)
			return;
		if (location == 0) {
			int index = slidAdapter.getCount() - 2;
			fs_vp.setCurrentItem(index, false);
		} else if (location == slidAdapter.getCount() - 1) {
			fs_vp.setCurrentItem(1, false);
		}

	}

	@Override
	public void onPageScrollStateChanged(int location) {

	}
}
