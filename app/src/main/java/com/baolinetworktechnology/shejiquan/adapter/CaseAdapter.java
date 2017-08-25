package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.PhotoActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.CaseItem;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HMY on 2016/8/6
 */
public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.ViewHolder> {
    private Context mContext;
    private List<SwCase> mList =new ArrayList<>();
    protected LayoutInflater inflater;
    BitmapUtils mImageUtil;
    public CaseAdapter(Context context) {
            mImageUtil = new BitmapUtils(context);
			mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
			mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
			mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
            mContext = context;
            inflater = LayoutInflater.from(context);
    }

    public void setList(List<SwCase> list) {
        if(list==null){
            return;
        }
        mList.clear();
        mList.addAll(list);
    }
    public void addList(List<SwCase> list) {
        if(list==null){
            return;
        }
        mList.addAll(list);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.item_case_home_recy, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SwCase anlibean=mList.get(position);
        holder.tvTitle.setText(anlibean.title);
        holder.tvTips.setText(anlibean.getTips());
        holder.tvCost.setText("收藏 "+anlibean.getNumFavorite());
        mImageUtil.display(holder.iv_image,anlibean.getSmallImages());
        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebOpusActivity.class);
                String url = AppUrl.DETAIL_CASE2 + anlibean.id;
                intent.putExtra("WEB_URL", url);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return getListSize(mList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvTips,tvCost;
        ImageView iv_image;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_image=(ImageView) itemView.findViewById(R.id.iv_image);
            tvTitle=(TextView) itemView.findViewById(R.id.tvTitle);
            tvTips=(TextView) itemView.findViewById(R.id.tvTips);
            tvCost=(TextView) itemView.findViewById(R.id.tvCost);
        }
    }

    private int getListSize(List<SwCase> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
