package com.baolinetwkchnology.shejiquan.xiaoxi;

import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.activity.CommentListActivity;
import com.baolinetworktechnology.shejiquan.activity.MyContacActivity;
import com.baolinetworktechnology.shejiquan.activity.PostDetailsActivity;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.baolinetworktechnology.shejiquan.domain.CommentBean;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.fragment.BaseFragment;
import com.baolinetworktechnology.shejiquan.interfaces.OnStateListener;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 发出评论
 * 
 */
public class IssueFragment extends BaseFragment implements ILoadData{
	List<Comment> Commentdata = new ArrayList<Comment>();
	private RefreshListView mListView;
	private pinluenAdater adater;
	private BitmapUtils mImageUtil;
	private int PageIndex=1;
	private MyDialog dialog;
	private CountDownTimer time;
	private int nullDdrawable = R.drawable.my_ping_luen;
	public void setonStateListener(OnStateListener onStateListener) {
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 super.onCreateView(inflater, container, savedInstanceState);
		 View layout=inflater.inflate(R.layout.fragment_issue, container, false);
	        dialog = new MyDialog(getActivity());
	        mImageUtil = new BitmapUtils(getActivity());
			//加载图片类
			mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
			mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
			mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
			mListView = (RefreshListView)layout.findViewById(R.id.myListView);
			mListView.getPulldownFooter().isShowBottom(true);
			adater = new pinluenAdater();
			mListView.setAdapter(adater);
			mListView.setOnLoadListener(this);
		    time = new CountDownTimer(1000, 1000) {

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
					Comment comm=Commentdata.get(position-1);
					if(comm==null)
						return;
					if(comm.getId() !=0){
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
						R.layout.pinluen_layout, null);
				vh = new Holder();
				vh.name=(TextView) convertView.findViewById(R.id.name);
				vh.tvTime=(TextView) convertView.findViewById(R.id.tvTime);
				vh.myConten=(TextView) convertView.findViewById(R.id.myConten);
				vh.tvConten=(TextView) convertView.findViewById(R.id.tvConten);
				vh.userLogo = (ImageView) convertView.findViewById(R.id.userLogo);
				convertView.setTag(vh);
			} else {
				vh = (Holder) convertView.getTag();
			}
			Comment comment=Commentdata.get(position);
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
					vh.myConten.setText("我回复"+comment.replayUser.getReplayUserName()+"："
							+comment.replayUser.getReplyContents());
				}else{
					vh.myConten.setText("回复我的评论："+comment.replayUser.getReplyContents());
				}
			}else if(comment.replayInfo!=null) {
//				if(comment.userGUID.equals(SJQApp.user.guid)){
				String Title = comment.replayInfo.getReplayTitle();
				if (comment.getMarkName().equals("CASE")) {
					Title = "案例";
				} else if (comment.getMarkName().equals("NEWS")) {
					Title = "资讯";
				} else if (comment.getMarkName().equals("POSTS")) {
					Title = "帖子";
				}
				String name = "";
				if (TextUtils.isEmpty(comment.replayInfo.getReplayAuthor())) {
					name = "";
				} else {
					name = comment.replayInfo.getReplayAuthor() + "的";
				}
				if (TextUtils.isEmpty(name)) {
					vh.myConten.setText("评论了" + Title
							+ "：" + comment.replayInfo.getReplayTitle());
				} else {
					vh.myConten.setText("评论了" + name + Title
							+ "：" + comment.replayInfo.getReplayTitle());
				}
			}
//			}
			vh.myConten.setVisibility(View.VISIBLE);
			if(comment.replayInfo== null && comment.replayUser == null) {
				vh.myConten.setVisibility(View.GONE);
			}
			return convertView;
		}
		
	}
	class Holder {
		TextView name,myConten,tvTime,tvConten;
		ImageView userLogo;
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
		loadata();			
	}
	private void loadata() {
		String url = AppUrl.GETALLCOMMENTLIST+SJQApp.user.guid
				+"&commentType=2"+"&currentPage="+PageIndex+"&pageSize=10";
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				mListView.setOnComplete();
				toastShow(message);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (mListView == null || adater == null) {
					return;
				}
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
