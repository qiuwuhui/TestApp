package com.baolinetworktechnology.shejiquan.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.adapter.HorizontalListViewAdapter1;
import com.baolinetworktechnology.shejiquan.adapter.HorizontalListViewAdapter2;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.CaseClassJD;
import com.baolinetworktechnology.shejiquan.domain.CaseClassJDlist;
import com.baolinetworktechnology.shejiquan.domain.CaseClassList;
import com.baolinetworktechnology.shejiquan.net.GetClassList;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.HorizontalListView;
import com.guojisheng.koyview.domain.ChannelItem;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class MyXuanzheActivity extends BaseActivity implements OnClickListener{
	private TextView text_fg;
	 List<CaseClassJD> list=new ArrayList<CaseClassJD>();
	private HorizontalListViewAdapter2 horadapter;
	private CaseClassJDlist styleList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_xuanzhe);
		addCase();
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.btn_go).setOnClickListener(this);
		String Style=CacheUtils.getStringData(MyXuanzheActivity.this,AppTag.YE_ZXJD,null);
		styleList = CommonUtils.getDomain(Style,
				CaseClassJDlist.class);
		if(styleList!=null){
			findViewById(R.id.btn_go).setClickable(true);
			findViewById(R.id.btn_go).setBackgroundResource(R.drawable.anniu_shape);	
		}else{
			findViewById(R.id.btn_go).setClickable(false);
			findViewById(R.id.btn_go).setBackgroundResource(R.drawable.anniu_shape1);			
		}
		text_fg = (TextView) findViewById(R.id.text_fg);
		HorizontalListView Jdlist = (HorizontalListView) findViewById(R.id.Jdlist);
		horadapter = new HorizontalListViewAdapter2(MyXuanzheActivity.this, list);
		Jdlist.setAdapter(horadapter);
		Jdlist.setOnItemClickListener(new OnItemClickListener() {

 			@Override
 			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
 					long arg3) {				
 				horadapter.setPosition(list.get(position));
 				if(horadapter.getPositionstr()!=null && !horadapter.getPositionstr().equals("")){
 					text_fg.setText(Html
 							.fromHtml("<font font size=\"10\" color='#8C8C96'>您已选择: </font>"
 									+horadapter.getPositionstr()));						
 				}else{
 					text_fg.setText("");
 				}
 				if(text_fg.getText().toString().equals("")){
 					findViewById(R.id.btn_go).setClickable(false);
 					findViewById(R.id.btn_go).setBackgroundResource(R.drawable.anniu_shape1);
				}else{
					findViewById(R.id.btn_go).setClickable(true);
					findViewById(R.id.btn_go).setBackgroundResource(R.drawable.anniu_shape);
				}
 			}
 		 });
	}

	@Override
	protected void setUpViewAndData() {

	}
	@Override
	protected void restartApp() {
		startActivity(new Intent(this, SplashActivity.class));
		finish();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_BACK_TO_HOME);
		switch (action) {
			case AppStatusConstant.ACTION_RESTART_APP:
				restartApp();
				break;
		}
	}
	private void addCase() {
		String url = ApiUrl.RECOMMENDDCOR_CLASS;
		dialog.show();
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow(message);
				dialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject json;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					JSONArray arry=new JSONArray(result1);
					for (int i = 0; i < arry.length(); i++) {
						JSONObject object=(JSONObject) arry.get(i);
						CaseClassJD classjd=new CaseClassJD();
						classjd.classID=object.getInt("classID");
						classjd.img=object.getString("img");
						classjd.className=object.getString("className");
						list.add(classjd);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				horadapter.notifyDataSetChanged();
				String Style=CacheUtils.getStringData(MyXuanzheActivity.this,AppTag.YE_ZXJD,null);
				CaseClassJDlist StyleList= CommonUtils.getDomain(Style,
						CaseClassJDlist.class);
				if(StyleList!=null){
					horadapter.setaddlist(StyleList.List);
					text_fg.setText(Html
 							.fromHtml("<font font size=\"10\" color='#8C8C96'>您已选择: </font>"
 									+horadapter.getPostStr()));
				}
				dialog.dismiss();
			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);
	
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.btn_go:
			CaseClassJDlist cBean=new CaseClassJDlist();
			cBean.List=horadapter.getaddlist();
			CacheUtils.cacheStringData(MyXuanzheActivity.this, AppTag.YE_ZXJD,cBean.toString());
		    if(styleList!=null){
		    	if(styleList.List.get(0).classID!=cBean.List.get(0).classID){
		    		Intent intent = new Intent();
		    		intent.setAction("addjd");
		    		// 发送 一个无序广播
		    		MyXuanzheActivity.this.sendBroadcast(intent);				
		    	}		    	
		    }else{
		    	Intent intent = new Intent();
	    		intent.setAction("addjd");
	    		// 发送 一个无序广播
	    		MyXuanzheActivity.this.sendBroadcast(intent);
		    }
			finish();
			break;

		default:
			break;
		}
		
	}

}
