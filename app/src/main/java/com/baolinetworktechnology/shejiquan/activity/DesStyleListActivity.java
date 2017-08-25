package com.baolinetworktechnology.shejiquan.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.CaseClassAdapter;
import com.baolinetworktechnology.shejiquan.adapter.StyleGvAdapter;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.ClassListBean;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 擅长风格选择
 * 
 * @author JiSheng.Guo
 * 
 */
public class DesStyleListActivity extends BaseActivity {
	public static String STYLE_ID = "STYLE_ID";

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
//	private ListView mListView;
//	private CaseClassAdapter mAdapter;
//	StyleGvAdapter mGvAdapter;
//	private ClassListBean mClassListBean;
//	GridView mGridView;
//	boolean isGridv = true;// 是否用GridView方式显示
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_des_style_list);
//		TextView tv_title2 = (TextView) findViewById(R.id.tv_title2);
//		String title = getIntent().getStringExtra(AppTag.TAG_TITLE);
//		setTitle(title == null ? "擅长风格" : title);
//
//		tv_title2.setText("确定");
//		tv_title2.setVisibility(View.GONE);
//		tv_title2.setOnClickListener(this);
//		mListView = (ListView) findViewById(R.id.list);
//		mGridView = (GridView) findViewById(R.id.gridView);
//		// 加载列表
//		loadDataClass();
//
//	}
//
//
//	// 加载列表
//	private void loadDataClass() {
//
//		if (isGridv) {
//			mGvAdapter = new StyleGvAdapter(this);
//			mGridView.setAdapter(mGvAdapter);
//			mGridView.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//					CaseClass caseClasee = (CaseClass) mGvAdapter
//							.getItem(position);
//
//					if (caseClasee.isCheck) {
//						caseClasee.isCheck = false;
//						for(int i=0;i<mCheckCaseList.size();i++){
//							if(mCheckCaseList.get(i).ID==caseClasee.ID){
//								mCheckCaseList.remove(i);
//								continue;
//							}
//						}
//
//
//					} else {
//						if (mCheckCaseList.size() >= 4) {
//							toastShow("最多只能选择四个");
//							return;
//						}
//						caseClasee.isCheck = true;
//						mCheckCaseList.add(caseClasee);
//					}
//					mGvAdapter.notifyDataSetChanged();
//				}
//			});
//
//		} else {
//			mAdapter = new CaseClassAdapter();
//			mAdapter.setCheck();
//			mListView.setAdapter(mAdapter);
//		}
//		String cache = CacheUtils.getStringData(this, "CLASS_LIST","DesStyleListActivity","");
//		mClassListBean = CommonUtils.getDomain(cache, ClassListBean.class);
//		if (mClassListBean != null && mClassListBean.data != null) {
//			for (int i = 0; i < mClassListBean.data.size(); i++) {
//
//				if (mClassListBean.data.get(i).Name.endsWith("风格")) {
//					List<CaseClass> classList = mClassListBean.data.get(i).List;
//					check(classList);
//					if (isGridv) {
//						mGvAdapter.setData(classList);
//					} else {
//						mAdapter.setData(classList);
//					}
//
//				}
//
//			}
//		}
//
//		getHttpUtils().send(HttpMethod.GET,
//				ApiUrl.API + ApiUrl.CASE_CLASS_LIST,
//				getParams(ApiUrl.CASE_CLASS_LIST),
//				new RequestCallBack<String>() {
//
//					@Override
//					public void onFailure(HttpException error, String msg) {
//
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> n) {
//						mClassListBean = CommonUtils.getDomain(n,
//								ClassListBean.class);
//						if (mClassListBean != null
//								&& mClassListBean.data != null) {
//
//							for (int i = 0; i < mClassListBean.data.size(); i++) {
//								if (mClassListBean.data.get(i).Name
//										.endsWith("风格")) {
//									CacheUtils.cacheStringData(
//											DesStyleListActivity.this,"CLASS_LIST",
//											"DesStyleListActivity", n.result);
//									List<CaseClass> classList = mClassListBean.data
//											.get(i).List;
//									check(classList);
//									if (isGridv) {
//										if (mGvAdapter.getCount() == 0) {
//											mGvAdapter.setData(classList);
//											mGvAdapter.notifyDataSetChanged();
//										}
//									} else {
//										mAdapter.setData(classList);
//										check(classList);
//									}
//
//								}
//							}
//
//						}
//
//					}
//				});
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.tv_title2:
//		case R.id.btnOk:
//			doOK();
//			break;
//		case R.id.back:
//			finish();
//			break;
//		}
//	}
//
//	List<CaseClass> mCheckCaseList = new ArrayList<CaseClass>();
//
//	// 提交
//	private void doOK() {
//		Intent data = new Intent();
//		if (isGridv) {
//
//		} else {
//			mCheckCaseList = mAdapter.getCheckCaseList();
//		}
//		StringBuffer text = new StringBuffer();
//		StringBuffer textID = new StringBuffer();
//
//		for (int i = 0; i < mCheckCaseList.size(); i++) {
//
//			textID.append(mCheckCaseList.get(i).ID);
//			text.append(mCheckCaseList.get(i).Title);
//			if (i != mCheckCaseList.size() - 1) {
//				textID.append(",");
//				text.append(",");
//			}
//
//		}
//		data.putExtra(AppTag.TAG_TEXT, text.toString());
//		data.putExtra(STYLE_ID, textID.toString());
//		setResult(RESULT_OK, data);
//		finish();
//
//	}
//
//	// 检查是否包含外部传进的值
//	private void check(List<CaseClass> classList) {
//		if (!isGridv) {
//			return;
//		}
//		String data = getIntent().getStringExtra(AppTag.TAG_TEXT);
//		mCheckCaseList.clear();
//		if (data != null) {
//			String[] datas;
//			if (data.indexOf(",") != -1) {
//				datas = data.split(",");
//			} else {
//				datas = data.split("、");
//			}
//			if (datas != null && datas.length >= 1) {
//				for (int i = 0; i < datas.length; i++) {
//					for (int j = 0; j < classList.size(); j++) {
//						if (datas[i].equals(classList.get(j).ID+"")) {
//							classList.get(j).isCheck = true;
//							mCheckCaseList.add(classList.get(j));
//							continue;
//						}
//
//					}
//				}
//			}
//
//		}
//
//	}
}
