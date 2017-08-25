package com.baolinetworktechnology.shejiquan.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.adapter.HorizontalListViewAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewPagerAdapter;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.CaseClassList;
import com.baolinetworktechnology.shejiquan.domain.ClassListBean;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.guojisheng.koyview.utls.WindowsUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 引导页
 * 
 * @author JiSheng.Guo
 * 
 */
public class GuideActivity extends Activity {
	String APP_FIRST = "APP_FIRST";
	private ViewPager mViewPager;
	private ViewPagerAdapter mViewPagerAdapter;
	private int indexWidth;
	private RadioGroup point_group;
	private Button btn;
	String KEY_TAG = "GetClassList";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initView();
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		// 存放引导页图片集合
		List<View> list = new ArrayList<View>();
		// 初始化三个引导页
		View view = View.inflate(this, R.layout.view_image, null);
		View view2 = View.inflate(this, R.layout.view_image, null);
		View view3 = View.inflate(this, R.layout.view_image, null);
		ImageView iv1 = (ImageView) view.findViewById(R.id.guidIV);
		ImageView iv2 = (ImageView) view2.findViewById(R.id.guidIV);
		ImageView iv3 = (ImageView) view3.findViewById(R.id.guidIV);

		iv1.setImageResource(R.drawable.guid_bg1);
		iv2.setImageResource(R.drawable.guid_bg2);
		iv3.setImageResource(R.drawable.guid_bg3);

		btn = (Button) view3.findViewById(R.id.btnOK);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				go2Splash();
			}
		});

		list.add(view);
		list.add(view2);
		list.add(view3);
		indexWidth = WindowsUtil.getWindowsWidth(this) / list.size();
		mViewPagerAdapter = new ViewPagerAdapter(list);
		// 设置适配器

		point_group = (RadioGroup) findViewById(R.id.ll_pager_point_group);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int id = 0;
				switch (arg0) {
				case 0:
					id = R.id.rb1;
					break;
				case 1:
					id = R.id.rb2;
					break;
				case 2:
					id = R.id.rb3;
					break;
				default:
					break;
				}
				if(arg0==2){
					btn.setVisibility(View.VISIBLE);
				}
				point_group.check(id);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		mViewPager.setAdapter(mViewPagerAdapter);
	}

	// 跳转到启动页
	protected void go2Splash() {
		CacheUtils.cacheBooleanData(GuideActivity.this, "APP_TIAO",
					true);
		CacheUtils.cacheBooleanData(GuideActivity.this, APP_FIRST,
				true);
		Intent intent = new Intent(this, SkipActivity.class);
		startActivity(intent);
		finish();
	}
}
