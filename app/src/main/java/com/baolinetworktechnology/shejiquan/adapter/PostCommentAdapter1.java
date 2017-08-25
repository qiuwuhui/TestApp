package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class PostCommentAdapter1 extends BaseAdapter {
	BitmapUtils mImageUtil;
	List<Comment> data = new ArrayList<Comment>();
	Context mContext;

	public PostCommentAdapter1(Context context) {
		this.mContext=context.getApplicationContext();
		mImageUtil = new BitmapUtils(context.getApplicationContext());
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultShowOriginal(false);
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
			holder1 = new Holder1();
			holder1.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder1.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder1.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
			holder1.tv_Reply = (TextView) convertView.findViewById(R.id.tv_Reply);
			holder1.iv_image = (ImageView) convertView.findViewById(R.id.circle_image);
			holder1.is_zuozhe = (ImageView) convertView.findViewById(R.id.is_zuozhe);
			convertView.setTag(holder1);
		} else {
			holder1 = (Holder1) convertView.getTag();
		}
		if (holder1.iv_image.getTag() == null
				|| holder1.iv_image.getTag().toString() != news.getSmallImages("_300_300.")) {
			mImageUtil.display(holder1.iv_image, news.getSmallImages("_300_300."));
			holder1.iv_image.setTag(news.getSmallImages("_300_300."));
		}
		holder1.tv_title.setText(news.getUserName());
		holder1.tv_Reply.setVisibility(View.VISIBLE);
		if(news.getReplayUser()==null){
			holder1.tv_Reply.setVisibility(View.GONE);
		}else{
			holder1.tv_Reply.setText(Html.fromHtml("<font font size=\"17\" color='#000000'>"+news.getReplayUser().getReplayUserName()+"： "+"</font>"+
				 news.getReplayUser().getReplyContents()));
//			holder1.tv_Reply.setText(news.ReplayUser.ReplayUserName+":　"+news.ReplayUser.ReplyContents);
		}
		holder1.tv_time.setText(news.getFromatDate());
		holder1.tv_text.setText(news.getContents());
		holder1.is_zuozhe.setVisibility(View.GONE);
		holder1.is_zuozhe.setBackgroundResource(R.drawable.post_my);
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
	}

}
