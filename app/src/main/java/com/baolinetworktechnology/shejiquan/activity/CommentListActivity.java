package com.baolinetworktechnology.shejiquan.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.CommentAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.baolinetworktechnology.shejiquan.domain.CommentBean;
import com.baolinetworktechnology.shejiquan.domain.GongSiMyanliBean;
import com.baolinetworktechnology.shejiquan.domain.ReplayUser;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.MyRelativeLayout;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 评论列表
 * 
 * @author JiSheng.Guo
 * 
 */
public class CommentListActivity extends BaseActivity implements ILoadData {
	public static String CLASS_TYPE = "CLASS_TYPE";// 评论对象类型0,1 案例、资讯
	public static String FORM_GUID = "GUID";
	public static String FORM_USER_GUID = "FORM_USER_GUID";
	private TextView mTvTitle;// 评论按钮
	private RefreshListView mListView;// 评论列表
	private CommentAdapter mCommentAdapter;// 适配器
	private String mGUID;// 评论对象GUid
	private int mClassType;// 评论类型
	private int mCommentNumber; // 评论数
	private int mPageIndex = 1;// 页码
	private EditText etComment;
	private String FromID="0";
	private String UserGUID="";
	private String FromName="";
	private String FromComment="";
//	private CharSequence temp;
//	private int selectionStart;
//	private int selectionEnd;
	private InputMethodManager imm;
	private MyRelativeLayout mRoot1;
	private View item_pingluen;
	private int nullDdrawable = R.drawable.icon_qsf;
	private String TAG = "";//页面来源
	private String mPosition = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		setTitle("评论");
		initView();
		dialog.setCancelable(false);
		// 是否直接跳转评论页
		mListView.setOnRefreshing();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(SJQApp.user ==null){
					startActivity(new Intent(CommentListActivity.this,
							LoginActivity.class));
				}else {
					Comment news = (Comment) mCommentAdapter.getItem(arg2 - 1);
					FromID = news.getId() + "";
					FromName = news.getUserName();
					UserGUID = news.getUserGUID();
					if (news.getUserGUID().equals("00000000-0000-0000-0000-000000000000")) {
						toastShow("不能回复游客");
						dialog.dismiss();
						return;
					}
					if (SJQApp.user.getGuid().equals(news.getUserGUID())) {
						toastShow("自己不能回复自己");
						dialog.dismiss();
						return;
					}
					FromComment = news.getContents();
					etComment.setHint("回复" + FromName + "：");
					etComment.setFocusable(true);
					etComment.setFocusableInTouchMode(true);
					etComment.requestFocus();
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
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
	TextView ic_send;

	@Override
	public void finish() {
		super.finish();
		mCommentAdapter = null;
	}

	private void initView() {
		item_pingluen = findViewById(R.id.item_pingluen);
		mRoot1 = (MyRelativeLayout) findViewById(R.id.mRoot1);
		mRoot1.setFitsSystemWindows(true);
		mTvTitle = (TextView) findViewById(R.id.tv_title2);
		mTvTitle.setText("评论");
		mTvTitle.setOnClickListener(this);
		mGUID = getIntent().getStringExtra(FORM_GUID);
		mClassType = getIntent().getIntExtra(CLASS_TYPE, 1);
		TAG = getIntent().getStringExtra("TAG");
		mListView = (RefreshListView) findViewById(R.id.listView);
		ic_send = (TextView) findViewById(R.id.ic_send);
		ic_send.setOnClickListener(this);
		etComment = (EditText) findViewById(R.id.etComments);
		etComment.clearFocus();
		mCommentAdapter = new CommentAdapter(this);
		mListView.setAdapter(mCommentAdapter);
		mListView.setOnLoadListener(this);
		mListView.setNullData(R.drawable.ic_null_comment);
		imm = (InputMethodManager) etComment.getContext().getSystemService(
				this.INPUT_METHOD_SERVICE);
		etComment.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int arg1, int arg2,
					int arg3) {
				String str = etComment.getText().toString();
				if(str.equals("")){
					ic_send.setText("取消");
				}else{
					ic_send.setText("发送");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
//				selectionStart = etComment.getSelectionStart();
//				selectionEnd = etComment.getSelectionEnd();
//				if (temp.length() > 200) {
//					arg0.delete(selectionStart - 1, selectionEnd);
//					int tempSelection = selectionEnd;
//					etComment.setText(arg0);
//					etComment.setSelection(tempSelection);
//				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onClick(View v) {
		// super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_title2:
			Intent intent = new Intent(this, CommentActivity.class);
			intent.putExtra(CommentActivity.FORM_GUID, mGUID);
			intent.putExtra(CommentActivity.CLASS_TYPE, mClassType);
			startActivity(intent);
			break;
		case R.id.ic_send:
			if(SJQApp.user == null){
				((SJQApp) getApplication()).exitAccount();
				startActivity(new Intent(CommentListActivity.this,
						LoginActivity.class));
			}
			else{
				if(ic_send.getText().toString().equals("发送")) {
					if(SJQApp.user.isBindMobile){
						doSubmit();
					}else{
						toastShow("绑定手机号后才能发评论");
					}
				}else{
					FromID="0";
					UserGUID="";
					FromName="";
					FromComment="";
					etComment.setHint("说点什么吧...");
					imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);// 隐藏输入法
				}
			}
			break;
		default:
			Intent data = new Intent();
			data.putExtra("commentSize", mCommentNumber);
			setResult(1, data);
			//从效果图列表进入
			if (TAG !=null && TAG.equals("FROMXGT")){
				Intent d = new Intent();
				d.setAction("XGTCommentSuCCESS");//效果图列表点击评论
				d.putExtra("POSITION", getIntent().getIntExtra("POSITION",-1));
				d.putExtra("PLNUMBER", mCommentNumber);
				Log.e("TAG评论2",getIntent().getIntExtra("POSITION",-1)+"---"+mCommentNumber+"---"+getIntent().getIntExtra("PLNUMBER",0));
				sendBroadcast(d);
			}
			finish();
			break;
		}
	}

	// 发布评论
	private void doSubmit() {
		dialog.show();
		if(mListView.getVisibility()==View.GONE){
			mListView.setVisibility(View.VISIBLE);				
		}
		String Contents = etComment.getText().toString();
		if (Contents.trim().length() < 1) {
			toastShow("输入内容不能为空");
			dialog.dismiss();
			return;

		}
		if (SJQApp.user == null) {
			dialog.dismiss();
			startActivity(new Intent(CommentListActivity.this,
					LoginActivity.class));
			return;
		}
//		if(TextUtils.isEmpty(SJQApp.user.Mobile)) {
//			gotBound("评论需绑定手机号");
//			return;
//		}
		String url = AppUrl.COMMENT_SEND;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("contents",Contents);
			param.put("fromGUID", mGUID);
			param.put("markName", mClassType+"");
			param.put("fromID", FromID);
			param.put("userGUID", SJQApp.user.guid);
			if (SJQApp.userData != null) {
			param.put("userName",SJQApp.userData.name);
			param.put("userLogo", SJQApp.userData.logo);
		} else if (SJQApp.ownerData != null) {
			param.put("userName",SJQApp.ownerData.getName());
			param.put("userLogo", SJQApp.ownerData.getLogo());
		}
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				// dialog.show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dialog.dismiss();
				if (mCommentAdapter == null)
					return;
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
				if (bean != null) {
					if (bean.result.operatResult) {
						mCommentAdapter.notifyDataSetChanged();
						toastShow(bean.result.operatMessage);
						addComment();
//						loadData(true);
						//
//						if (TAG !=null && TAG.equals("FROMXGT")){
//							Intent intent = new Intent();
//							intent.setAction("XGTCommentSuCCESS");//效果图列表点击评论
//							intent.putExtra("POSITION", getIntent().getIntExtra("POSITION",-1));
//							intent.putExtra("NUMBER", mCommentNumber+getIntent().getIntExtra("PLNUMBER",0));
//							sendBroadcast(intent);
//						}
					}else{
						toastShow(bean.result.operatMessage);
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String arg1) {
				dialog.dismiss();
				if (mCommentAdapter == null)
					return;
				toastShow("网络请求发生错误");
				AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
						error.getExceptionCode() + "", "POST",
						ApiUrl.COMMENT_SEND);
			}
		};

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, AppUrl.API + url, params, callBack);

	}

	private void addComment() {
		mCommentNumber++;
		if (etComment != null) {

			Comment comment = new Comment();
			if (SJQApp.userData != null) {
				comment.userName = SJQApp.userData.name;
				comment.userLogo = SJQApp.userData.logo;
			} else if (SJQApp.ownerData != null) {
				comment.userName = SJQApp.ownerData.getName();
				comment.userLogo = SJQApp.ownerData.getLogo();
			} else{
				comment.userName = SJQApp.user.nickName;
				comment.userLogo = SJQApp.user.logo;
			}
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			comment.setCreateTime(str);
			comment.setUserGUID(SJQApp.user.getGuid());
			if(comment.getUserGUID().equals(SJQApp.user.getGuid())){
				comment.setAuthor(true);
			}
			comment.contents = etComment.getText().toString();
			if(!FromID.equals("0")){
				ReplayUser replayUser=new ReplayUser();
				replayUser.setReplayUserName(FromName);
				replayUser.setReplyContents(FromComment);
				comment.replayUser=replayUser;
			}
			comment.setAuthor(false);
			mCommentAdapter.addData(comment);
			mCommentAdapter.notifyDataSetChanged();
			etComment.setText("");
			etComment.setHint("说点什么吧...");
			FromID="0";
			UserGUID="";
			FromName="";
			FromComment="";
			mListView.setOnComplete();
			ListView tempList = mListView.getRefreshableView();
			int tempPosition = tempList.getSelectedItemPosition();
			if (tempPosition == 0 || tempPosition > 3)
				tempList.setSelection(3);
			tempList.smoothScrollToPosition(0);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent data = new Intent();
		data.putExtra("commentSize", mCommentNumber);
		setResult(1, data);
		//从效果图列表进入
		if (TAG !=null && TAG.equals("FROMXGT")){
			Intent intent = new Intent();
			intent.setAction("XGTCommentSuCCESS");//效果图列表点击评论
			intent.putExtra("POSITION", getIntent().getIntExtra("POSITION",-1));
			intent.putExtra("PLNUMBER", mCommentNumber);
			sendBroadcast(intent);
		}
		finish();
	}

	@Override
	protected void onDestroy() {
		etComment = null;
		mListView = null;
		mRoot1.setFitsSystemWindows(false);
		super.onDestroy();

	}

	public void loadData() {
		HttpUtils httpUtils = new HttpUtils();

		final String url = AppUrl.COMMENT_LIST + "&fromGUID=" + mGUID
				+ "&currentPage=" + mPageIndex + "&pageSize=10" + "&markName="
				+ mClassType;
		RequestParams params = getParams(url);
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				// dialog.show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (mListView == null || mCommentAdapter == null)
					return;
				JSONObject json;
				CommentBean comments=null;
				try {
					json = new JSONObject(arg0.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					comments=gson.fromJson(result1, CommentBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mListView.setOnComplete();
				if (comments != null && comments.listData != null) {

					mCommentNumber = comments.recordCount;
					if (mPageIndex == 1) {
						mCommentAdapter.setData(comments.listData);
					} else {
						mCommentAdapter.addData(comments.listData);
						if (comments.listData.size() == 0) {
							mListView.setOnNullNewsData("没有更多了");
							mPageIndex = -1;
						}

					}
				}
				if(comments.listData == null){
					mListView.setOnNullData("赶紧，抢沙发");
					mListView.setNullData(nullDdrawable);
				}

			}

			@Override
			public void onFailure(HttpException error, String arg1) {
				if (mListView == null || mCommentAdapter == null)
					return;
				AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
						error.getExceptionCode() + "", "GET", url);
				mListView.setOnComplete();
			}
		};
		httpUtils.send(HttpMethod.GET, AppUrl.API + url, params, callBack);
	}
	private void gotBound(String strshwo){
		View dialogView = View.inflate(getActivity(),
				R.layout.dialog_collect, null);
		TextView titl = (TextView) dialogView
				.findViewById(R.id.dialog_title);
		titl.setText(strshwo);
		final AlertDialog ad = new AlertDialog.Builder(
				getActivity()).setView(dialogView).show();
		dialogView.findViewById(R.id.dialog_cancel)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ad.cancel();
					}
				});
		dialogView.findViewById(R.id.dialog_ok)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra(AppTag.TAG_JSON, SJQApp.user.getMobile());
						intent.setClass(getActivity(),
								BoundActivity.class);
						startActivity(intent);
						ad.cancel();
					}});
	}
	@Override
	public void loadData(boolean isRefresh) {
		if (isRefresh) {
			mPageIndex = 1;
		} else {
			if (mPageIndex == -1) {
				mListView.setOnNullNewsData("没有更多了");
				return;
			}
			mPageIndex++;
		}
		loadData();
	}
	private void controlKeyboardLayout(final View root, final View scrollToView) {
		root.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				//获取root在窗体的可视区域
				root.getWindowVisibleDisplayFrame(rect);
				//获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
				int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
				//若不可视区域高度大于100，则键盘显示
				if (rootInvisibleHeight > 100) {
					int[] location = new int[2];
					//获取scrollToView在窗体的坐标
					scrollToView.getLocationInWindow(location);
					//计算root滚动高度，使scrollToView在可见区域
					int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
					root.scrollTo(0, srollHeight);
				} else {
					//键盘隐藏
					root.scrollTo(0, 0);
				}
			}
		});
	}
}
