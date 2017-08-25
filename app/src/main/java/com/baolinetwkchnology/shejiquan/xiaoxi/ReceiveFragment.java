package com.baolinetwkchnology.shejiquan.xiaoxi;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.activity.MyContacActivity;
import com.baolinetworktechnology.shejiquan.activity.PostDetailsActivity;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.baolinetworktechnology.shejiquan.domain.CommentBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.fragment.BaseFragment;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.MyRelativeLayout;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 收到评论
 * 
 */
public class ReceiveFragment extends BaseFragment implements ILoadData{
	List<Comment> Commentdata = new ArrayList<Comment>();
	private RefreshListView mListView;
	private pinluenAdater adater;
	private BitmapUtils mImageUtil;
	private int PageIndex=1;
	private MyDialog dialog;
	private View item_pingluen;
	private MyRelativeLayout mRoot1;
	private InputMethodManager imm;
	private EditText pingluen_text;
	private TextView fasong_btn;
	private String FromID="0";
	private String mGUID="0";
	private String FromName="";
	private String markName="";
	private CountDownTimer time;
	private int nullDdrawable = R.drawable.my_ping_luen;
	public void setonStateListener(OnStateListener onStateListener) {
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		    super.onCreateView(inflater, container, savedInstanceState);
		    View layout=inflater.inflate(R.layout.fragment_receive, container, false);
	        dialog = new MyDialog(getActivity());
	        mImageUtil = new BitmapUtils(getActivity());
			//加载图片类
			mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
			mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
	        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型

		    item_pingluen=layout.findViewById(R.id.item_pingluen);
		    pingluen_text = (EditText) layout.findViewById(R.id.etComments);
	    	pingluen_text.clearFocus();
		    pingluen_text.addTextChangedListener(watcher);
		    fasong_btn = (TextView) layout.findViewById(R.id.ic_send);
	    	fasong_btn.setOnClickListener(this);
	      	imm = (InputMethodManager) pingluen_text.getContext().getSystemService(
				getActivity().INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);
		    mRoot1 = (MyRelativeLayout) layout.findViewById(id.mooRetss);
		    mRoot1.setFitsSystemWindows(true);
//		    controlKeyboardLayout(mRoot1,item_pingluen);

			mListView = (RefreshListView)layout.findViewById(R.id.myListView);
			mListView.getPulldownFooter().isShowBottom(true);
			adater = new pinluenAdater();
			mListView.setAdapter(adater);
			mListView.setOnLoadListener(this);
		    time = new CountDownTimer(500, 500) {

			@Override
			public void onTick(long millisUntilFinished) {

			}
			@Override
			public void onFinish() {
				mListView.setOnRefreshing();
			}
	    	};
		    time.start();
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if (item_pingluen.getVisibility() == View.VISIBLE) {
						itemGone();
						return;
					}
					Comment comm=Commentdata.get(position-1);
					if(comm==null)
						return;
					if(comm.getId()!=0){
						String markName =comm.getMarkName();
						if(markName.equals("CASE")){
							    Intent intent = new Intent(getActivity(), WebOpusActivity.class);
								String url = AppUrl.DETAIL_CASE2 + comm.getFromGUID();
								intent.putExtra("WEB_URL", url);
								intent.putExtra("classTitle", "");
								intent.putExtra(AppTag.TAG_ID, comm.getFromGUID());
								intent.putExtra(WebDetailActivity.ISCASE, true);
								getContext().startActivity(intent);
						}else if(markName.equals("NEWS")){
								Intent intent = new Intent(getActivity(), WebDetailActivity.class);
								intent.putExtra(WebDetailActivity.GUID, "0");
								intent.putExtra(WebDetailActivity.ID,comm.getFromGUID());
								getActivity().startActivity(intent);
						}else if(markName.equals("POSTS")){
							Intent intent = new Intent(getActivity(), PostDetailsActivity.class);
							intent.putExtra(AppTag.TAG_GUID,comm.getFromGUID());
							startActivity(intent);
						}else if(markName.equals("CIRCLE")){
							Intent intent = new Intent(getActivity(), MyContacActivity.class);
							intent.putExtra(AppTag.TAG_GUID,comm.getFromGUID());
							startActivity(intent);
						}
					}else{
						toastShow("数据出错");
					}
				}
			});
			return layout;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case id.ic_send:
				if(fasong_btn.getText().toString().equals("发送")){
					if(SJQApp.user.isBindMobile){
						dialog.show();
						doSubmit();
					}else{
						toastShow("绑定手机号后才能发评论");
					}
				}else{
					itemGone();
				}
				break;
		}
		super.onClick(v);
	}

	private  void  itemGone(){
		FromName="";
		markName="";
		FromID="0";
		mGUID="";
		fasong_btn.setText("取消");
		pingluen_text.setText("");
		pingluen_text.setHint("");
		if (item_pingluen.getVisibility() == View.VISIBLE) {
			item_pingluen.setVisibility(View.GONE);
			item_pingluen.clearFocus();
			imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法
		}
	}
	private class pinluenAdater extends BaseAdapter{

		@Override
		public int getCount() {
			return Commentdata.size();
		}
		@Override
		public Object getItem(int position) {
			if (position < 0 || position >= Commentdata.size())
				return null;
			return Commentdata.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder vh = null;
			if (convertView == null) {
				convertView = View.inflate(parent.getContext(),
						layout.receive_pinluen_layout, null);
				vh = new Holder();
				vh.name=(TextView) convertView.findViewById(R.id.name);
				vh.tvTime=(TextView) convertView.findViewById(R.id.tvTime);
				vh.myConten=(TextView) convertView.findViewById(R.id.myConten);
				vh.tvConten=(TextView) convertView.findViewById(R.id.tvConten);
				vh.userLogo = (ImageView) convertView.findViewById(R.id.userLogo);
				vh.is_zuozhe = (ImageView) convertView.findViewById(R.id.is_zuozhe);
				vh.huifu = (TextView) convertView.findViewById(id.huifu);
				convertView.setTag(vh);
			} else {
				vh = (Holder) convertView.getTag();
			}
			final  Comment comment=Commentdata.get(position);
			if (vh.userLogo.getTag() == null
					|| vh.userLogo.getTag().toString() != comment.userLogo) {
				mImageUtil.display(vh.userLogo, comment.userLogo);
				vh.userLogo.setTag(comment.userLogo);
			}
			vh.name.setText(comment.userName);
			vh.tvTime.setText(comment.getFromatDate());
			vh.tvConten.setText(comment.contents);
			vh.huifu.setVisibility(View.VISIBLE);
			if(comment.getUserGUID().equals(SJQApp.user.guid)){
				vh.huifu.setVisibility(View.GONE);
			}
			if(comment.getUserGUID().equals("00000000-0000-0000-0000-000000000000")){
				vh.huifu.setVisibility(View.GONE);
			}
			vh.is_zuozhe.setVisibility(View.GONE);
			if(comment.getIsRead()==0){
				vh.is_zuozhe.setVisibility(View.VISIBLE);
			}else{
				vh.is_zuozhe.setVisibility(View.GONE);
			}
			if(comment.replayUser!=null){
				if(comment.userGUID.equals(SJQApp.user.guid)){
					vh.myConten.setText("我回复"+comment.replayUser.getReplayUserName()+"："
							+comment.replayUser.getReplyContents());
				}else{
					vh.myConten.setText("回复我的评论："+comment.replayUser.getReplyContents());
				}
			}else if(comment.replayInfo!=null){
//				if(comment.userGUID.equals(SJQApp.user.guid)){
					String Title=comment.replayInfo.getReplayTitle();
				if(comment.getMarkName().equals("CASE")){
					Title="我的案例";
				}else if(comment.getMarkName().equals("NEWS")){
					Title="我的资讯";
				}else if(comment.getMarkName().equals("POSTS")){
					Title="我的帖子";
				}
				String name="";
				if(TextUtils.isEmpty(comment.replayInfo.getReplayAuthor())){
					name="";
				}else{
					name=comment.replayInfo.getReplayAuthor();
				}
				if(TextUtils.isEmpty(name)){
					vh.myConten.setText("评论了"+Title
							+"："+comment.replayInfo.getReplayTitle());
				}else{
					if(comment.getMarkName().equals("CASE")){
						Title="的案例";
					}else if(comment.getMarkName().equals("NEWS")){
						Title="的资讯";
					}else if(comment.getMarkName().equals("POSTS")){
						Title="的帖子";
					}
					vh.myConten.setText("评论了"+name+Title
							+"："+comment.replayInfo.getReplayTitle());
				}
				}
//			}
			vh.myConten.setVisibility(View.VISIBLE);
			if(comment.replayInfo== null && comment.replayUser == null) {
				vh.myConten.setVisibility(View.GONE);
			}
			vh.huifu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(comment.getUserGUID().equals(SJQApp.user.guid)){
						toastShow("自己不可以回复自己");
						return;
					}
					if(comment.getUserGUID().equals("00000000-0000-0000-0000-000000000000")){
						toastShow("不能回复游客");
						return;
					}
					Message message = Message.obtain();
					message.what = 1;
					timeHandler.sendMessage(message);
					FromName=comment.getUserName();
					markName=comment.getMarkName();
					FromID=comment.getId()+"";
					mGUID=comment.getFromGUID();
				}
			});
			return convertView;
		}
		
	}
	private  Handler timeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
			item_pingluen.setVisibility(View.VISIBLE);
			pingluen_text.requestFocus();
			pingluen_text.setHint("回复"+FromName+"：");
		}
	};
	class Holder {
		TextView name,myConten,tvTime,tvConten;
		ImageView userLogo,is_zuozhe;
		TextView huifu;
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
	// 发布评论
	private void doSubmit() {
		String url = AppUrl.COMMENT_SEND;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		String Contents = pingluen_text.getText().toString();

		if (Contents.trim().length() < 1) {
			toastShow("输入内容不能为空");
			dialog.dismiss();
			return;

		}
		try {
			JSONObject param  = new JSONObject();
			param.put("contents",Contents);
			param.put("fromGUID", mGUID);
			if(markName.equals("CASE")){
				param.put("markName", "1");
			}else if(markName.equals("NEWS")){
				param.put("markName", "5");
			}else if(markName.equals("POSTS")){
				param.put("markName", "7");
			}else if(markName.equals("CIRCLE")){
				param.put("markName", "8");
			}
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
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Gson gson = new Gson();
				SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
				dialog.dismiss();
				if (bean != null) {
					if (bean.result.operatResult) {
						toastShow(bean.result.operatMessage);
						itemGone();
					}else{
						toastShow(bean.result.operatMessage);
					}
				}
			}
			@Override
			public void onFailure(HttpException error, String arg1) {
				dialog.dismiss();
				toastShow("网络请求发生错误");
			}};
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, AppUrl.API + url, params, callBack);
	}
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			String str = pingluen_text.getText().toString();
			if(str.equals("")){
				fasong_btn.setText("取消");
			}else{
				fasong_btn.setText("发送");
			}
		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {}
		@Override
		public void afterTextChanged(Editable arg0) {}
	};
	@Override
	public void loadData(boolean isRefresh) {
		if(isRefresh){
			PageIndex=1;
		}else{
			PageIndex++;
        }
		loadata();
	}
	private void loadata() {
		String url = AppUrl.GETALLCOMMENTLIST+SJQApp.user.guid
				+"&commentType=1"+"&currentPage="+PageIndex+"&pageSize=10";
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				mListView.setOnComplete();
				toastShow(message);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (mListView == null || adater == null)
					return;
				mListView.setOnComplete();
				JSONObject json;
				CommentBean comments=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					comments=gson.fromJson(result1, CommentBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
					if (comments == null || comments.listData == null ||comments.listData.size() == 0) {
						if(PageIndex==1){
							mListView.setOnNullData("暂无新的评论消息");
							mListView.setNullData(nullDdrawable);
						}else{
							mListView.setOnNullNewsData("没有更多了");
							return;
						}
					}
					if(comments!=null && comments.listData != null){
						if(PageIndex==1){
							setDate(comments);
						}else{
							addDate(comments);
						}
					}
			}
		};
		getHttpUtils().send(HttpMethod.GET, AppUrl.API + url, getParams(url),
				callBack);		
	}
	private void addDate(CommentBean bean) {
		Commentdata.addAll(bean.listData);
		adater.notifyDataSetChanged();
	}
	private void setDate(CommentBean bean) {
		Commentdata.clear();
		Commentdata.addAll(bean.listData);
		adater.notifyDataSetChanged();
	}
	public void lazyLoad() {
		if (mListView != null)
			mListView.setOnRefreshing();
	}
	public void onDestroy() {
		super.onDestroy();
		mRoot1.setFitsSystemWindows(false);
	}
	public void setTabClick() {
		int position = mListView.getRefreshableView()
				.getFirstVisiblePosition();
		if (position == 0) {
			mListView.setOnRefreshing();
		} else {
			if (position < 8) {
//                if (position > 5)
//                mCaseListView.getRefreshableView().setSelection(5);
				mListView.getRefreshableView().setSelection(0);
			} else {
				mListView.getRefreshableView().setSelection(0);
			}

		}
	}
}
