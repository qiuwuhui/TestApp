package com.baolinetworktechnology.shejiquan.activity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.StageAdapter;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.guojisheng.koyview.domain.ChannelItem;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class StageActivity extends BaseActivity implements OnClickListener{
	//装修前
	public ArrayList<ChannelItem> beforeChannelList = new ArrayList<ChannelItem>();
	//装修中
	public ArrayList<ChannelItem> middleChannelList = new ArrayList<ChannelItem>();
	//装修后
	public ArrayList<ChannelItem> afterChannelList = new ArrayList<ChannelItem>();
	private MyDialog dialog;
	private String channeName;
	private StageAdapter beforAdapetr;
	private StageAdapter middleAdapetr;
	private StageAdapter afterAdapetr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stage);
		dialog = new MyDialog(StageActivity.this);
		findViewById(R.id.fin_image).setOnClickListener(this);
		channeName = getIntent().getStringExtra("name");
		initTabColumn();
		invinen();
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
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.fin_image){
			finish();
		}
	}
	private void invinen() {
		GridView beforeView = (GridView) findViewById(R.id.beforeView);	
		beforAdapetr = new StageAdapter(this, 1);
		beforeView.setAdapter(beforAdapetr);
	
		GridView middleView = (GridView) findViewById(R.id.middleView);
		middleAdapetr = new StageAdapter(this, 2);
		middleView.setAdapter(middleAdapetr);
	
		GridView afterView = (GridView) findViewById(R.id.afterView);
		afterAdapetr = new StageAdapter(this, 3);
		afterView.setAdapter(afterAdapetr);
		afterView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				ChannelItem ChannelItem=(ChannelItem) afterAdapetr.getItem(position);
				int postID=ChannelItem.getId();
				int postbox=position+beforeChannelList.size()+middleChannelList.size();
				Intent data = new Intent();
				data.putExtra("postID", postID);
				data.putExtra("postbox", postbox);
				setResult(AppTag.RESULT_OK, data);
				finish();
			}			
		});
	}
	/**
	 * 初始化Column栏目项
	 * */
	private void initTabColumn() {
		String url = ApiUrl.GETDECORATIONTYPE; 
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
					json = new JSONObject(result1);
					JSONArray arry=new JSONArray(json.getString("before"));
					for (int i = 0; i < arry.length(); i++) {
						JSONObject object=(JSONObject) arry.get(i);
						int id=object.getInt("id");
						String title=object.getString("title");
						ChannelItem channelItem=new ChannelItem(id,title,true);
						beforeChannelList.add(channelItem);
					}
					beforAdapetr.Stename(channeName);
					beforAdapetr.setData(beforeChannelList);
					
					JSONArray arry1=new JSONArray(json.getString("middle"));
					for (int i = 0; i < arry1.length(); i++) {
						JSONObject object=(JSONObject) arry1.get(i);
						int id=object.getInt("id");
						String title=object.getString("title");
						ChannelItem channelItem=new ChannelItem(id,title,true);
						middleChannelList.add(channelItem);
					}
					middleAdapetr.Stename(channeName);
					middleAdapetr.setData(middleChannelList);
					
					JSONArray arry2=new JSONArray(json.getString("after"));
					for (int i = 0; i < arry2.length(); i++) {
						JSONObject object=(JSONObject) arry2.get(i);
						int id=object.getInt("id");
						String title=object.getString("title");
						ChannelItem channelItem=new ChannelItem(id,title,true);
						afterChannelList.add(channelItem);
					}
					afterAdapetr.Stename(channeName);
					afterAdapetr.setData(afterChannelList);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);
	}
	public void SetChannelItem(int postbox, int postID,int getlist){
		int postbox1 = 0;
		if(getlist==1){
			postbox1=postbox;			
		}else if(getlist==2){
			postbox1=postbox+beforeChannelList.size();		
		}else if(getlist==3){
			postbox1=postbox+beforeChannelList.size()+middleChannelList.size();	
		}
		Intent data = new Intent();
		data.putExtra("postID", postID);
		data.putExtra("postbox", postbox1);
		setResult(AppTag.RESULT_OK, data);
		finish();
	}
}
