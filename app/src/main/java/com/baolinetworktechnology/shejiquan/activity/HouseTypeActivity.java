package com.baolinetworktechnology.shejiquan.activity;

import java.util.List;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.adapter.HorizontalListViewAdapter;
import com.baolinetworktechnology.shejiquan.adapter.HorizontalListViewAdapter1;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.CaseClassList;
import com.baolinetworktechnology.shejiquan.net.GetClassList;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.HorizontalListView;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
public class HouseTypeActivity extends BaseActivity {
	 String APP_FIRST = "APP_FIRST";
	 boolean isCheckCity = false;
	 int cityIndex = 1;
	 private TextView text_jj;
	 List<CaseClass> list;
	private HorizontalListViewAdapter1 horadapter;
	private Button btn_go;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_activity1);
     	inview();
     	addCase();
     	HorizontalListView Hxlist=(HorizontalListView) findViewById(R.id.Hxlist);
     	horadapter = new HorizontalListViewAdapter1(HouseTypeActivity.this, list);
     	Hxlist.setAdapter(horadapter);
     	String Type=CacheUtils.getStringData(HouseTypeActivity.this,AppTag.TJ_Type,null);
		CaseClassList TypeList= CommonUtils.getDomain(Type,
		CaseClassList.class);
		if(TypeList!=null){
			horadapter.setaddlist(TypeList.List);
			text_jj.setText(Html
						.fromHtml("<font font size=\"10\" color='#8C8C96'>您已选择: </font>"
								+horadapter.getPostStr()));
		}
     	Hxlist.setOnItemClickListener(new OnItemClickListener() {

 			@Override
 			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
 					long arg3) {				
 				horadapter.setPosition(list.get(position));
 				if(horadapter.getPositionstr()!=null && !horadapter.getPositionstr().equals("")){
 					text_jj.setText(Html
 							.fromHtml("<font font size=\"10\" color='#8C8C96'>您已选择: </font>"
 									+horadapter.getPositionstr()));						
 				}else{
 					text_jj.setText("");
 				}
 				if(text_jj.getText().toString().equals("")){
					btn_go.setClickable(false);
					btn_go.setBackgroundResource(R.drawable.anniu_shape1);
				}else{
					btn_go.setClickable(true);
					btn_go.setBackgroundResource(R.drawable.anniu_shape);
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
		list = new GetClassList(this).getList("户型");	
		list.remove(0);		
	}


	private void inview() {		
		TextView tiaoguo=(TextView) findViewById(R.id.tiaoguo1);
		tiaoguo.setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		btn_go = (Button) findViewById(R.id.btn_go);
		btn_go.setOnClickListener(this);
		String Type=CacheUtils.getStringData(HouseTypeActivity.this,AppTag.TJ_Type,null);
		CaseClassList TypeList= CommonUtils.getDomain(Type,
		CaseClassList.class);
		if(TypeList!=null){
			btn_go.setBackgroundResource(R.drawable.anniu_shape);
			btn_go.setClickable(true);	 
		}else{
			btn_go.setBackgroundResource(R.drawable.anniu_shape1);
			btn_go.setClickable(false);	    			
		}
		text_jj = (TextView) findViewById(R.id.text_jj);
		findViewById(R.id.back).setVisibility(View.VISIBLE);
		boolean isFirst1 = CacheUtils.getBooleanData(this, "APP_TIAO", false);
		    if(!isFirst1){		
		    	tiaoguo.setVisibility(View.GONE);
			}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tiaoguo1:
			go2Splash();
			break;
	    case R.id.btn_go:
	    	CaseClassList cBean=new CaseClassList();
			cBean.List=horadapter.getaddlist();
			CacheUtils.cacheStringData(HouseTypeActivity.this, AppTag.TJ_Type,cBean.toString());
	    	Intent intent = new Intent(this, SelectActivity.class);
			startActivity(intent);
			finish();
			break;
	    case R.id.back:
	    	Intent intent1 = new Intent(this, StyleActivity.class);
			startActivity(intent1);
			finish();
			break;			
		default:
			break;
		}
	}
		// 跳转到主页面
		protected void go2Splash() {
			CacheUtils.cacheBooleanData(HouseTypeActivity.this, APP_FIRST,
					true);
			Intent intent = new Intent(this, SkipActivity.class);
			intent.putExtra(AppTag.TAG_ID, 1);
			startActivity(intent);
			finish();
		}
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 Intent intent = new Intent(this, StyleActivity.class);
			 startActivity(intent);
	 	     finish();
			return super.onKeyDown(keyCode, event);
		}
}
