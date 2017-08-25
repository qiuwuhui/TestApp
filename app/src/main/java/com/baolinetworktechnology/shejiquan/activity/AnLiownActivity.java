package com.baolinetworktechnology.shejiquan.activity;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.adapter.anliownAdater;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.GongSiBean;
import com.baolinetworktechnology.shejiquan.domain.GongSianliBean;
import com.baolinetworktechnology.shejiquan.domain.OrderLog;
import com.baolinetworktechnology.shejiquan.domain.anliBean;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AnLiownActivity extends BaseActivity implements OnClickListener ,ILoadData{
	private List<anliBean> anliList=new ArrayList<anliBean>();
	private RefreshListView mListView;
	private anliownAdater adater;
	private TextView tite_name;
	private String GUID;
	private int PageIndex=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_an_liown);
		tite_name = (TextView) findViewById(R.id.tite_name);
		tite_name.setText("全部案例");
		GUID=getIntent().getStringExtra("GUID");
		mListView = (RefreshListView)findViewById(R.id.myListView);
		adater = new anliownAdater(this);
		mListView.setAdapter(adater);
		mListView.setOnLoadListener(this);
		mListView.setOnRefreshing();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				anliBean item=adater.getItem(position-1);
				Intent intent = new Intent(AnLiownActivity.this, WebOpusActivity.class);
				String url = ApiUrl.DETAIL_CASE2 + item.getId()+"&r="+System.currentTimeMillis();
				intent.putExtra("WEB_URL", url);
				intent.putExtra(AppTag.TAG_ID, item.getId());
				startActivity(intent);
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
	@Override
	public void loadData(boolean isRefresh) {
		if(isRefresh){
			PageIndex=1;
		}else{
			PageIndex++;
        }
		loadata();	
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		 case R.id.back:
			 finish();
			 break;

		default:
			break;
		}
		
	}
	private void loadata() {
		String url = ApiUrl.GETBUSINESSCASELIST+"BusinessGUID="+GUID
				+"&PageSize=10"+"&PageIndex="+PageIndex; 
		
		dialog.show();
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				mListView.setOnComplete();
				toastShow(message);
				dialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				mListView.setOnComplete();
				JSONObject json;
				GongSianliBean bean=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, GongSianliBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (bean == null || bean.listData.size() == 0) {
					if(PageIndex==1){
						mListView.setOnNullData("抱歉，没有数据");
					}else{
						mListView.setOnNullNewsData();                    		
					}
				}
				if (bean!= null) {
                    if(PageIndex==1){
                    	adater.setDate(bean);
                    }else{
                    	adater.addDate(bean);
                    }
				   }
				dialog.dismiss();
			}


		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);
		
	}

}
