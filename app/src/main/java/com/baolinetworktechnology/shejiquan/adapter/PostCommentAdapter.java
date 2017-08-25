package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.PhotoActivity;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.baolinetworktechnology.shejiquan.domain.postsItemInfoList;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HMY on 2016/8/6
 */
public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.ViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<Comment> mList =new ArrayList<>();
    protected LayoutInflater inflater;
    BitmapUtils mImageUtil;
    public PostCommentAdapter(Context context) {
            mImageUtil = new BitmapUtils(context);
			mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
			mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
			mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
            mImageUtil.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(context));
            mContext = context;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(Comment)v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , Comment data);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void setList(List<Comment> list) {
        mList = list;
    }
    public void addList(List<Comment> list) {
        mList.addAll(list);
    }
    public void addList(Comment comment) {
        if (comment != null) {
            mList.add(0, comment);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.items_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        convertView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Comment news = mList.get(position);
        holder.itemView.setTag(mList.get(position));
        mImageUtil.display(holder.circle_image, news.getSmallImages("_300_300."));
        holder.tv_title.setText(news.getUserName());
        holder.tv_Reply.setVisibility(View.VISIBLE);
        if(news.getReplayUser()==null){
            holder.tv_Reply.setVisibility(View.GONE);
        }else{
            holder.tv_Reply.setText(Html.fromHtml("<font font size=\"17\" color='#000000'>"+news.getReplayUser().getReplayUserName()+"： "+"</font>"+
                    news.getReplayUser().getReplyContents()));
        }
        holder.tv_time.setText(news.getFromatDate());
        holder.tv_text.setText(news.getContents());
        holder.is_zuozhe.setVisibility(View.GONE);
        holder.is_zuozhe.setBackgroundResource(R.drawable.post_my);
        holder.is_zuozhe.setVisibility(View.GONE);
        if(news.isAuthor() ){
            holder.is_zuozhe.setVisibility(View.VISIBLE);
        }else{
            holder.is_zuozhe.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return getListSize(mList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_time, tv_text,tv_Reply;
        ImageView is_zuozhe;
        CircleImg circle_image;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title=(TextView) itemView.findViewById(R.id.tv_title);
            tv_time=(TextView) itemView.findViewById(R.id.tv_time);
            tv_text=(TextView) itemView.findViewById(R.id.tv_text);
            tv_Reply=(TextView) itemView.findViewById(R.id.tv_Reply);
            circle_image=(CircleImg) itemView.findViewById(R.id.circle_image);
            is_zuozhe=(ImageView) itemView.findViewById(R.id.is_zuozhe);
        }
    }

    private int getListSize(List<Comment> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

}
