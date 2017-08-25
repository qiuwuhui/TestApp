package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.CommentListActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.lidroid.xutils.BitmapUtils;

public class CommentAdapter extends BaseAdapter {
	BitmapUtils mImageUtil;
	List<Comment> data = new ArrayList<Comment>();
	Context mContext;

	public CommentAdapter(Context context) {
		this.mContext=context;
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
	}


	public void setData(List<Comment> data) {
		this.data.clear();
		if (data != null)
			this.data.addAll(data);
		notifyDataSetChanged();
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public void addData(List<Comment> data) {
		if (data != null)
			this.data.addAll(data);
		notifyDataSetChanged();
	}

	public void addData(Comment comment) {
		if (comment != null) {
			data.add(0, comment);
			notifyDataSetChanged();
		}
	}

	
	@Override
	public int getCount() {
		if (data == null)
			return 0;
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder1 holder1 = null;
		final Comment news = data.get(position);
		if (convertView == null) {

			convertView = View.inflate(parent.getContext(),
					R.layout.items_comment, null);
			holder1 = new Holder1(convertView);

			convertView.setTag(holder1);
		} else {
			holder1 = (Holder1) convertView.getTag();
		}
		if (holder1.iv_image.getTag() == null
				|| holder1.iv_image.getTag().toString() != news.userLogo) {
			mImageUtil.display(holder1.iv_image, news.userLogo);
			holder1.iv_image.setTag(news.userLogo);
		}
		holder1.tv_title.setText(news.userName);
		holder1.tv_Reply.setVisibility(View.VISIBLE);
		if(news.replayUser==null){
			holder1.tv_Reply.setVisibility(View.GONE);
		}else{
			holder1.tv_Reply.setText(Html.fromHtml("<font font size=\"17\" color='#000000'>"+news.replayUser.getReplayUserName()+"： "+"</font>"+
				 news.replayUser.getReplyContents()));
//			holder1.tv_Reply.setText(news.ReplayUser.ReplayUserName+":　"+news.ReplayUser.ReplyContents);
		}
		holder1.tv_time.setText(news.getFromatDate());
		holder1.tv_text.setText(news.contents);
		holder1.is_zuozhe.setVisibility(View.GONE);
		if(news.isAuthor()){
			holder1.is_zuozhe.setVisibility(View.VISIBLE);
		}else{
			holder1.is_zuozhe.setVisibility(View.GONE);
		}
		return convertView;
	}

	class Holder1 {
		TextView tv_title, tv_time, tv_text,tv_Reply;
		ImageView iv_image,is_zuozhe;

		Holder1(View view) {
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_text = (TextView) view.findViewById(R.id.tv_text);
			tv_Reply = (TextView) view.findViewById(R.id.tv_Reply);
			iv_image = (ImageView) view.findViewById(R.id.circle_image);
			is_zuozhe = (ImageView) view.findViewById(R.id.is_zuozhe);
		}

	}

}
