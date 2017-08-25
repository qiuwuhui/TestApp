package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.PhotoActivity;
import com.baolinetworktechnology.shejiquan.domain.productItem;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HMY on 2016/8/6
 */
public class DeatilsAdapter extends RecyclerView.Adapter<DeatilsAdapter.ViewHolder> {
    private Context mContext;
    private List<productItem> mList=new ArrayList<>();
    private ArrayList<String> imageList;// 套图
    protected ArrayList<String> imageListTitle;// 图片标题
    protected LayoutInflater inflater;
    BitmapUtils mImageUtil;
    public DeatilsAdapter(Context context) {
        mContext = context.getApplicationContext();
        mImageUtil = new BitmapUtils(context.getApplicationContext());
        mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    public void setList(List<productItem> list) {
        mList = list;
        if(mList!=null){
            imageList = new ArrayList<>();
            imageListTitle = new ArrayList<>();
            for(int i=0;i<mList.size();i++) {
                imageList.add(mList.get(i).getPath());
                imageListTitle.add(mList.get(i).getDescriptions());
            }
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.deta_ils_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        productItem anlibean=mList.get(position);
        final int ispostion = position;
        holder.al_name.setVisibility(View.VISIBLE);
        if(TextUtils.isEmpty(anlibean.getDescriptions())){
            holder.al_name.setVisibility(View.GONE);
        }else{
            holder.al_name.setText(anlibean.getDescriptions());
        }
        holder.al_userLogo.setImageResource(R.drawable.zixun_tu);
        holder.al_userLogo.setVisibility(View.VISIBLE);
        if(TextUtils.isEmpty(anlibean.getPath())){
            holder.al_userLogo.setVisibility(View.GONE);
        }else{
            mImageUtil.display(holder.al_userLogo,anlibean.getPath());
        }
        holder.al_userLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(mContext,
							PhotoActivity.class);
					// 所有图片集合
					intent.putStringArrayListExtra(PhotoActivity.IMAGE_URLS,
							imageList);
					// 所有图片的标题
					intent.putExtra(PhotoActivity.IMAGE_TITLES, imageListTitle);
					// 当前图片的位置
					intent.putExtra(PhotoActivity.INDEX, ispostion);
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
        TextView al_name;
        ImageView al_userLogo;
        public ViewHolder(View itemView) {
            super(itemView);
            al_userLogo=(ImageView) itemView.findViewById(R.id.al_userLogo);
            al_name=(TextView) itemView.findViewById(R.id.al_name);
        }
    }

    private int getListSize(List<productItem> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
