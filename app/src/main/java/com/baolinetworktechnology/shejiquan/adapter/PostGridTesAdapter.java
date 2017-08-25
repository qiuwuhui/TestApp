package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.PhotoActivity;
import com.baolinetworktechnology.shejiquan.domain.postsItemInfoList;
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
public class PostGridTesAdapter extends RecyclerView.Adapter<PostGridTesAdapter.ViewHolder> {
    private Context mContext;
    private List<postsItemInfoList> mList;
    private ArrayList<String> imageList;// 套图
    BitmapUtils mImageUtil;
    public PostGridTesAdapter(Context context) {
          mImageUtil = new BitmapUtils(context);
          mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
          mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
          mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
          mImageUtil.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(context));
          mContext = context;
    }

    public void setList(List<postsItemInfoList> list) {
        mList = list;
        if(mList!=null){
            imageList = new ArrayList<>();
            for(int i=0;i<mList.size();i++) {
                imageList.add(mList.get(i).path);
            }
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.post_opus_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int ispostion = position;
        postsItemInfoList postsItemInfoList=mList.get(position);
        holder.al_userLogo.setImageResource(R.drawable.zixun_tu);
                mImageUtil.display(holder.al_userLogo, postsItemInfoList.path,
                new BitmapLoadCallBack<ImageView>() {
                    @Override
                    public void onLoadCompleted(ImageView arg0, String arg1,
                                                Bitmap arg2, BitmapDisplayConfig arg3,
                                                BitmapLoadFrom arg4) {
                        arg0.setImageBitmap(setBitmaps(arg2));
                    }
                    @Override
                    public void onLoadFailed(ImageView arg0, String arg1,
                                             Drawable arg2) {
                    }
                });
        holder.al_userLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(mContext,
							PhotoActivity.class);
					// 所有图片集合
					intent.putStringArrayListExtra(PhotoActivity.IMAGE_URLS,
							imageList);
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
        ImageView al_userLogo;
        public ViewHolder(View itemView) {
            super(itemView);
            al_userLogo=(ImageView) itemView.findViewById(R.id.al_userLogo);
        }
    }

    private int getListSize(List<postsItemInfoList> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
    public static Bitmap setBitmaps(Bitmap bitMap){
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        if(width > 4096 || height > 4096){
            int newWidth = 0;
            int newHeight = 0;
            if(width > 4096){
                newWidth = 4096;
            }else {
                newWidth =width;
            }
            if(height > 4096){
                newHeight = 4096;
            }else {
                newHeight =height;
            }
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix,
                    true);
            bitMap =Bitmap.createBitmap(bitMap,0,0,newWidth,newHeight);
//        bitMap =Watermark(bitMap,255);
            return bitMap ;
        }else{
            return bitMap;
        }
    }
}
