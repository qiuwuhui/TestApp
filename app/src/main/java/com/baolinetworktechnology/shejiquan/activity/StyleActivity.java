package com.baolinetworktechnology.shejiquan.activity;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.adapter.HorizontalListViewAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.CaseClassList;
import com.baolinetworktechnology.shejiquan.domain.ClassListBean;
import com.baolinetworktechnology.shejiquan.net.GetClassList;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.HorizontalListView;
import com.google.zxing.common.StringUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 引导页
 * 
 * @author JiSheng.Guo
 * 
 */
public class StyleActivity extends BaseActivity implements OnClickListener{
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
//	String APP_FIRST = "APP_FIRST";
//	private TextView text_fg;
//	List<CaseClass> list;
//	HashMap<String, List<CaseClass>> hashMap = new HashMap<String, List<CaseClass>>();
//	private HorizontalListViewAdapter horadapter;
//	String KEY_TAG = "GetClassList";
//	private HorizontalListView mYlist;
//	private Button btn_go;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_guide_one);
//		 inview();
//		 findViewById(R.id.back).setOnClickListener(this);
//         mYlist = (HorizontalListView) findViewById(R.id.mYlist);
//		 mYlist.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long arg3) {
//				horadapter.setPosition(list.get(position));
//				if(horadapter.getPositionstr()!=null && !horadapter.getPositionstr().equals("")){
//					text_fg.setText(Html
//							.fromHtml("<font font size=\"10\" color='#8C8C96'>您已选择: </font>"
//									+horadapter.getPositionstr()));
//				}else{
//					text_fg.setText("");
//				}
//				if(text_fg.getText().toString().equals("")){
//					btn_go.setClickable(false);
//					btn_go.setBackgroundResource(R.drawable.anniu_shape1);
//				}else{
//					btn_go.setClickable(true);
//					btn_go.setBackgroundResource(R.drawable.anniu_shape);
//				}
//			}
//		 });
//		 String cache=CacheUtils.getStringData(this, "CLASS_LIST",KEY_TAG, "");
//         if(cache.equals("")){
//        	 loadData();
//         }else{
//        	 ClassListBean mClassListBean = CommonUtils.getDomain(cache,
//     				ClassListBean.class);
//        	 if (mClassListBean != null
//						&& mClassListBean.data != null) {
//					for (int i = 0; i < mClassListBean.data.size(); i++) {
//						CaseClass c1 = new CaseClass(0, "全部");
//						List<CaseClass> list = mClassListBean.data
//								.get(i).List;
//						list.add(0, c1);
//						if (mClassListBean.data.get(i).Index == 11)
//							mClassListBean.data.get(i).Name = "公装空间";
//						hashMap.put(mClassListBean.data.get(i).Name,
//								list);
//					}
//					list = hashMap.get("风格");
//					list.remove(0);
//					horadapter = new HorizontalListViewAdapter(StyleActivity.this, list);
//					mYlist.setAdapter(horadapter);
//					String Style=CacheUtils.getStringData(StyleActivity.this,AppTag.TJ_Style,null);
//					CaseClassList StyleList= CommonUtils.getDomain(Style,
//					CaseClassList.class);
//					if(StyleList!=null){
//						horadapter.setaddlist(StyleList.List);
//						text_fg.setText(Html
//								.fromHtml("<font font size=\"10\" color='#8C8C96'>您已选择: </font>"
//										+horadapter.getPostStr()));
//					}
//				}
//         }
//	  }
//	public List<CaseClass> getList(String key) {
//		if (hashMap != null)
//			return hashMap.get(key);
//		return null;
//	}
//			// 跳转到主页面
//	protected void go2Splash() {
//	   CacheUtils.cacheBooleanData(StyleActivity.this, APP_FIRST,
//						true);
//	   Intent intent = new Intent(this, SkipActivity.class);
//	   intent.putExtra(AppTag.TAG_ID, 1);
//	   startActivity(intent);
//	   finish();
//	}
//	private void inview() {
//	TextView tiaoguo=(TextView) findViewById(R.id.tiaoguo);
//	tiaoguo.setOnClickListener(this);
//	btn_go = (Button) findViewById(R.id.btn_go);
//	btn_go.setOnClickListener(this);
//	String Style=CacheUtils.getStringData(StyleActivity.this,AppTag.TJ_Style,null);
//	CaseClassList StyleList= CommonUtils.getDomain(Style,
//	CaseClassList.class);
//	if(StyleList!=null){
//		btn_go.setBackgroundResource(R.drawable.anniu_shape);
//		btn_go.setClickable(true);
//	}else{
//		btn_go.setBackgroundResource(R.drawable.anniu_shape1);
//		btn_go.setClickable(false);
//	}
//    text_fg = (TextView) findViewById(R.id.text_fg);
//    boolean isFirst1 = CacheUtils.getBooleanData(this, "APP_TIAO", false);
//    if(isFirst1){
//    	findViewById(R.id.back).setVisibility(View.GONE);
//	}else{
//		findViewById(R.id.back).setVisibility(View.VISIBLE);
//		tiaoguo.setVisibility(View.GONE);
//	  }
//	}
//	@Override
//	public void onClick(View v) {
//	  switch (v.getId()) {
//	    case R.id.tiaoguo:
//	    	go2Splash();
//		    break;
//	    case R.id.back:
//	    	CacheUtils.cacheBooleanData(StyleActivity.this, APP_FIRST,
//					true);
//	    	finish();
//		    break;
//		case R.id.btn_go:
//			CaseClassList cBean=new CaseClassList();
//			cBean.List=horadapter.getaddlist();
//			CacheUtils.cacheStringData(StyleActivity.this, AppTag.TJ_Style,cBean.toString());
//			Intent intent = new Intent(this, HouseTypeActivity.class);
//			startActivity(intent);
//			finish();
//			break;
//
//        default:
//		break;
//	  }
//   }
//	private void loadData() {
//		dialog.show();
//		String url = ApiUrl.CASE_CLASS_LIST;
//		RequestCallBack<String> callBack = new RequestCallBack<String>() {
//					@Override
//					public void onFailure(HttpException error, String msg) {
//						dialog.dismiss();
//						toastShow("当前网络不稳定，请检查网络连接...");
////						System.out.println("-->>GetClassList onFailure" + msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> n) {
//						dialog.dismiss();
//						ClassListBean mClassListBean = CommonUtils.getDomain(n,
//								ClassListBean.class);
//
//						if (mClassListBean != null
//								&& mClassListBean.data != null) {
//							for (int i = 0; i < mClassListBean.data.size(); i++) {
//								cace(n.result);
//								CaseClass c1 = new CaseClass(0, "全部");
//								List<CaseClass> list = mClassListBean.data
//										.get(i).List;
//								list.add(0, c1);
//								if (mClassListBean.data.get(i).Index == 11)
//									mClassListBean.data.get(i).Name = "公装空间";
//								hashMap.put(mClassListBean.data.get(i).Name,
//										list);
//							}
//							list = hashMap.get("风格");
//							list.remove(0);
//							horadapter = new HorizontalListViewAdapter(StyleActivity.this, list);
//							mYlist.setAdapter(horadapter);
//							String Style=CacheUtils.getStringData(StyleActivity.this,AppTag.TJ_Style,null);
//							CaseClassList StyleList= CommonUtils.getDomain(Style,
//							CaseClassList.class);
//							if(StyleList!=null){
//								horadapter.setaddlist(StyleList.List);
//								text_fg.setText(Html
//										.fromHtml("<font font size=\"10\" color='#8C8C96'>您已选择: </font>"
//												+horadapter.getPostStr()));
//							}
//						}
//					}
//
//				};
//        getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
//						callBack);
//	}
//	protected void cace(String result) {
//		CacheUtils.cacheStringData(StyleActivity.this, "CLASS_LIST", KEY_TAG, result);
//	}
  }
