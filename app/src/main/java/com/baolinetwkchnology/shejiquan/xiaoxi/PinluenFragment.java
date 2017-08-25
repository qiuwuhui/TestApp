package com.baolinetwkchnology.shejiquan.xiaoxi;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.adapter.MainPageAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.fragment.BaseMainFragment;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.view.HomeViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *评论界面
 * 
 */
public class PinluenFragment extends Fragment implements OnClickListener,OnPageChangeListener{
	private ViewPager mViewPage;
	private ReceiveFragment receiveFragment;
	private IssueFragment issueFragment;
	private View pinluen_layout;
	private RadioButton mRB1;
	private RadioButton mRB2;
	private View tuei_view;
	private View tuei_view1;
	private CountDownTimer time;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View layout=inflater.inflate(R.layout.fragment_pinluen, container, false);
		mViewPage = (ViewPager) layout.findViewById(R.id.viewPager);
		layout.findViewById(R.id.back).setOnClickListener(this);
		pinluen_layout=layout.findViewById(R.id.pinluen_layout);
        layout.findViewById(R.id.relativeLayout4).setOnClickListener(this);
		pinluen_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pinluen_layout.setVisibility(View.GONE);
				CacheUtils.cacheBooleanData(getActivity(), "PINLUEN",
						false);
			}
		});
		boolean isFirst = CacheUtils.getBooleanData(getActivity(), "PINLUEN", true);
		if(isFirst){
			pinluen_layout.setVisibility(View.VISIBLE);
		}

		initview(layout);
		return layout;
	}
	private void initview(View view) {
		mRB1 = (RadioButton) view.findViewById(R.id.rbCass);
		mRB2 = (RadioButton) view.findViewById(R.id.rbNew);
		mRB1.setOnClickListener(this);
		mRB2.setOnClickListener(this);
		tuei_view  =view.findViewById(R.id.tuei_view);
		tuei_view1 =view.findViewById(R.id.tuei_view1);
		tuei_view1.setVisibility(View.INVISIBLE);
//		completeFragment = new CompleteFragment();
		receiveFragment = new ReceiveFragment();
		issueFragment = new IssueFragment();
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(receiveFragment);
		fragments.add(issueFragment);
		mViewPage.setAdapter(new MainPageAdapter(getFragmentManager(),
				fragments));		
		mViewPage.setOnPageChangeListener(this);
		time = new CountDownTimer(500, 500) {

			@Override
			public void onTick(long millisUntilFinished) {

			}
			@Override
			public void onFinish() {
				if(SJQApp.messageBean!=null){
					if(SJQApp.messageBean.getResult().getNumComment()!=0){
						allComment();
						SJQApp.messageBean.getResult().setNumComment(0);
						Intent intent = new Intent();
						intent.setAction("ReadMag");
						getActivity().sendBroadcast(intent);
					}
				}
			}
		};
		time.start();
	}
	public void setTabClick() {
		if (mViewPage != null && mViewPage.getCurrentItem() == 0) {
			receiveFragment.setTabClick();
		} else {
			issueFragment.setTabClick();
		}

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		    case R.id.back:
			    getActivity().finish();
			    break;
			case R.id.relativeLayout4:
				setTabClick();
				break;
			case R.id.rbCass:
				mViewPage.setCurrentItem(0);
				tuei_view.setVisibility(View.VISIBLE);
				tuei_view1.setVisibility(View.INVISIBLE);
				break;
			case R.id.rbNew:
				mViewPage.setCurrentItem(1);
				tuei_view.setVisibility(View.INVISIBLE);
				tuei_view1.setVisibility(View.VISIBLE);
				break;
			}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int page) {
		switch (page) {
		case 0:
			mRB1.setChecked(true);
			mViewPage.setCurrentItem(0);
			tuei_view.setVisibility(View.VISIBLE);
			tuei_view1.setVisibility(View.INVISIBLE);
			break;
		case 1:
			mRB2.setChecked(true);
			mViewPage.setCurrentItem(1);
			tuei_view.setVisibility(View.INVISIBLE);
			tuei_view1.setVisibility(View.VISIBLE);
		    break;
		default:
			break;
		}
		
	}
//阅读状态全部改变为已读
protected void allComment() {
	String url = AppUrl.UpdateCommentRead;
	RequestParams params = new RequestParams();
	params.setHeader("Content-Type","application/json");
	try {
		JSONObject param  = new JSONObject();
		param.put("userGuid", SJQApp.user.getGuid());
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
			int a=0;
		}
		@Override
		public void onSuccess(ResponseInfo<String> n) {
		}
	};
	HttpUtils httpUtil = new HttpUtils(8 * 1000);
	httpUtil.send(HttpRequest.HttpMethod.PUT, AppUrl.API+ url,params,
			callBack);
}
	public void onResume() {

		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

}
