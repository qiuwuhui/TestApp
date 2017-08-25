package com.baolinetwkchnology.shejiquan.xiaoxi;
import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.activity.CommentListActivity;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.baolinetworktechnology.shejiquan.domain.CommentBean;
import com.baolinetworktechnology.shejiquan.fragment.BaseFragment;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
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
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 全部评论
 * 
 */
public class CompleteFragment extends BaseFragment implements ILoadData{
	List<Comment> Commentdata = new ArrayList<Comment>();
	private RefreshListView mListView;
	private pinluenAdater adater;
	private BitmapUtils mImageUtil;
	private int PageIndex=1;
	private MyDialog dialog;
	private View no_layout,item_pingluen;
	private RelativeLayout mRoot1;
	private InputMethodManager imm;
	private EditText pingluen_text;
	private Button fasong_btn;
	private String FromID="0";
	private String mGUID="0";
	private String FromName="";
	private int mClassType=0;
	public void setonStateListener(OnStateListener onStateListener) {
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        View layout=inflater.inflate(R.layout.fragment_complete, container, false);
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
		fasong_btn = (Button) layout.findViewById(R.id.ic_send);
		fasong_btn.setOnClickListener(this);
		imm = (InputMethodManager) pingluen_text.getContext().getSystemService(
				getActivity().INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);
		no_layout = layout.findViewById(R.id.No_layout);
		mRoot1 = (RelativeLayout) layout.findViewById(id.mooRet);
		controlKeyboardLayout(mRoot1,item_pingluen);

		mListView = (RefreshListView)layout.findViewById(R.id.myListView);
		no_layout = layout.findViewById(R.id.No_layout);
		mListView.getPulldownFooter().isShowBottom(true);
		adater = new pinluenAdater();
		mListView.setAdapter(adater);
		mListView.setOnLoadListener(this);
		mListView.setOnRefreshing();
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
				if(comm.replayInfo!=null && comm.replayInfo.getReplayID()!=0){
					Intent intent = new Intent(getActivity(), WebOpusActivity.class);
					String url = AppUrl.DETAIL_CASE2 + comm.replayInfo.getReplayID();
					intent.putExtra("WEB_URL", url);
					intent.putExtra("classTitle", "");
					intent.putExtra(AppTag.TAG_ID,comm.replayInfo.getReplayID());
					getContext().startActivity(intent);
				}else{
					toastShow("案例数据出错");
				}
			}
		});
		return layout;
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
						R.layout.receive_pinluen_layout, null);
				vh = new Holder();
				vh.name=(TextView) convertView.findViewById(R.id.name);
				vh.tvTime=(TextView) convertView.findViewById(R.id.tvTime);
				vh.myConten=(TextView) convertView.findViewById(R.id.myConten);
				vh.tvConten=(TextView) convertView.findViewById(R.id.tvConten);
				vh.userLogo = (ImageView) convertView.findViewById(R.id.userLogo);
				vh.huifu = (TextView) convertView.findViewById(id.huifu);
				convertView.setTag(vh);
			} else {
				vh = (Holder) convertView.getTag();
			}
			final Comment comment=Commentdata.get(position);
			if (vh.userLogo.getTag() == null
					|| vh.userLogo.getTag().toString() != comment.userLogo) {
				mImageUtil.display(vh.userLogo, comment.userLogo);
				vh.userLogo.setTag(comment.userLogo);
			}
			vh.name.setText(comment.userName);
			vh.tvTime.setText(comment.getFromatDate());
			vh.tvConten.setText(comment.contents);
			if(comment.replayUser!=null){
				if(comment.userGUID.equals(SJQApp.user.guid)){
					vh.myConten.setText("我回复"+comment.replayUser.getReplayUserName()
							+comment.replayUser.getReplyContents());
				}else{
					vh.myConten.setText("回复我的评论："+comment.replayUser.getReplyContents());
				}
			}else{
				if(comment.userGUID.equals(SJQApp.user.guid)){
					String Title=comment.replayInfo.getReplayTitle();
				if(comment.ClassType==AppTag.ClassType1){
					Title="案例";
				}else if(comment.ClassType==AppTag.ClassType2){
					Title="文章";
				}
				String name="";
				if(TextUtils.isEmpty(comment.replayInfo.getReplayAuthor())){
					name="设计圈";
				}else{
					name=comment.replayInfo.getReplayAuthor();
				}
				vh.myConten.setText("评论了"+name+"的"+Title+"："+
						comment.replayInfo.getReplayTitle());
				}

			}
			vh.huifu.setVisibility(View.GONE);
			if(!comment.userGUID.equals(SJQApp.user.guid)){
				vh.huifu.setVisibility(View.VISIBLE);
			}
			vh.huifu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(comment.userGUID.equals(SJQApp.user.guid)){
						toastShow("自己不可以回复自己");
						return;
					}
					Message message = Message.obtain();
					message.what = 1;
					timeHandler.sendMessage(message);
					FromName=comment.userName;
					mClassType=comment.ClassType;
					FromID=comment.id+"";
					mGUID=comment.userGUID;
				}
			});
			return convertView;
		}
		
	}
	class Holder {
		TextView name,myConten,tvTime,tvConten;
		ImageView userLogo;
		TextView huifu;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case id.ic_send:
				if(fasong_btn.getText().toString().equals("发送")){
					dialog.show();
					doSubmit();
				}else{
					itemGone();
				}
				break;
		}
		super.onClick(v);
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

	private Handler timeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
			item_pingluen.setVisibility(View.VISIBLE);
			pingluen_text.requestFocus();
			pingluen_text.setHint("回复"+FromName+"：");
		}
	};
	// 发布评论
	private void doSubmit() {
		String url = ApiUrl.COMMENT_SEND;
		RequestParams params = getParams(url);
		String Contents = pingluen_text.getText().toString();

		if (Contents.trim().length() < 1) {
			toastShow("您输入的内容太少了");
			return;
		}
		// 评论对象信息
		params.addBodyParameter("Contents", Contents);
		params.addBodyParameter("FromGuid", mGUID);
		params.addBodyParameter("Star", "5");
		params.addBodyParameter("Evaluate", Contents);
		params.addBodyParameter("ClassType", mClassType + "");
		params.addBodyParameter("AdminGUID",
				getActivity().getIntent().getStringExtra("FORM_USER_GUID"));
		//给谁回复的ID
		params.addBodyParameter("FromID", FromID);
		params.addBodyParameter("UserID", "" + SJQApp.user.guid);
		params.addBodyParameter("UserGuid", SJQApp.user.guid);
		if (SJQApp.userData != null) {
			params.addBodyParameter("UserName", SJQApp.userData.getName());
			params.addBodyParameter("UserLogo", SJQApp.userData.getLogo());
		} else if (SJQApp.ownerData != null) {
			params.addBodyParameter("UserName", SJQApp.ownerData.getName());
			params.addBodyParameter("UserLogo", SJQApp.ownerData.getLogo());
		} else {
			params.addBodyParameter("UserName", SJQApp.user.nickName);
			params.addBodyParameter("UserLogo", SJQApp.user.logo);
		}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Bean data = CommonUtils.getDomain(arg0, Bean.class);
				if (data != null) {
					if (data.success) {
						dialog.dismiss();
						toastShow(data.message);
						itemGone();
					}}}
			@Override
			public void onFailure(HttpException error, String arg1) {
				dialog.dismiss();
				toastShow("网络请求发生错误");
			}};
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);
	}
	private  void  itemGone(){
		FromName="";
		mClassType=0;
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
	@Override
	public void loadData(boolean isRefresh) {
		if(isRefresh){
			PageIndex=1;
		}else{
			PageIndex++;
        }
		no_layout.setVisibility(View.GONE);
		loadata();			
	}
	private void loadata() {
		String url = AppUrl.GETALLCOMMENTLIST+SJQApp.user.guid
				+"&commentType=0"+"&currentPage="+PageIndex+"&pageSize=10";
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
				if (mListView == null || adater == null)
					return;
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
							no_layout.setVisibility(View.VISIBLE);
						}else{
							mListView.setOnNullNewsData("没有更多了");
						}
					}
					if(comments!=null && comments.listData != null){
						if(PageIndex==1){
							setDate(comments);
						}else{
							addDate(comments);
						}
					}			
        		dialog.dismiss();
        		mListView.setOnComplete();
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
}
