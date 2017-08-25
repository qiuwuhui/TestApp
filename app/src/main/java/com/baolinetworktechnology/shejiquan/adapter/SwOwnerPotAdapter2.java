package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.tongxunlu.SwipeItemLayoutListview;
import com.baolinetworktechnology.shejiquan.view.NineGridTestLayout;
import com.google.gson.Gson;
import com.guojisheng.koyview.ExplosionField;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的--我的收藏--帖子 adapter
 */
public class SwOwnerPotAdapter2 extends BaseAdapter {
    private String TAG = "SwOwnerPotAdapter2";
    private List<PostBean> mData =new ArrayList<>();
    List<String> pathList;
    private BitmapUtils mImageUtil;
    private Context mContex;
    private boolean isdeletemode;
    private int postitem;
    public SwOwnerPotAdapter2(Context context) {
        this.mContex =context;
        mImageUtil = new BitmapUtils(context.getApplicationContext());
        mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
    }

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public void setData(List<PostBean> data){
        if (this.mData != null) {
            this.mData.clear();
        }
        if (data!=null && data.size()!=0 ) {
            this.mData.addAll(data);
            notifyDataSetChanged();
        }
    }
    public void addData(List<PostBean> data) {
        if (data != null)
            this.mData.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public PostBean getItem(int position) {

        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(),
                    R.layout.item_owner_post_layout2, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PostBean bean=mData.get(position);
        List<String> dapathList=new ArrayList<>();
        if(bean.getPostsItemInfoList().size()!=0){
            pathList=new ArrayList<>();
            for (int c = 0; c < bean.getPostsItemInfoList().size(); c++){
                if(bean.getPostsItemInfoList().size() ==1){
                    pathList.add(bean.getPostsItemInfoList().get(c).path);
                }else{
                    pathList.add(bean.getPostsItemInfoList().get(c).getSmallImages("_300_300."));
                }
                dapathList.add(bean.getPostsItemInfoList().get(c).path);
            }
            viewHolder.imageLayout.setdaUrlList(dapathList);
            viewHolder.imageLayout.setUrlList(pathList);
            viewHolder.imageLayout.setSpacing(10);
        }
        viewHolder.Post_name.setText(bean.getTitle());
        if(TextUtils.isEmpty(bean.getContents())){
            viewHolder.contents.setText("分享图片");
        }else{
            viewHolder.contents.setText(bean.getContents());
        }
        viewHolder.chakan.setText(bean.getHits()+"");
        viewHolder.pinluen.setText(bean.getNumComment()+"");
        viewHolder.dianzan.setText(bean.getNumGood()+"");
        viewHolder.tiem_post.setText(bean.getFromatDate());
        if(bean.isdeletemode()){
            viewHolder.imgdelete.setBackgroundResource(R.drawable.add_men_check);
        }else{
            viewHolder.imgdelete.setBackgroundResource(R.drawable.rb_bg_no);
        }
        if (convertView.getAlpha() != 1) {
            convertView.setAlpha(1);
            convertView.setScaleX(1);
            convertView.setScaleY(1);
            convertView.setTranslationX(0);
            convertView.setEnabled(true);
        }

        viewHolder.swipeRoot.setSwipeAble(false);
        if (isdeletemode){
            viewHolder.swipeRoot.openWithAnim();
        }else {
            viewHolder.swipeRoot.closeWithAnim();
        }
        viewHolder.imgdelete.setTag(position);
        viewHolder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postitem = (int) view.getTag();
                if(mData.get(postitem).isdeletemode()){
                    mData.get(postitem).setIsdeletemode(false);
                }else{
                    mData.get(postitem).setIsdeletemode(true);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    class ViewHolder {
        NineGridTestLayout imageLayout;
        TextView Post_name, contents;
        TextView chakan,pinluen,dianzan,tiem_post;
        LinearLayout deleteLayout;
        ImageView imgdelete;
        SwipeItemLayoutListview swipeRoot;
        public ViewHolder(View v) {
            imageLayout = (NineGridTestLayout) v.findViewById(R.id.layout_nine_grid);
            Post_name = (TextView) v.findViewById(R.id.Post_name);
            contents = (TextView) v.findViewById(R.id.contents);
            chakan = (TextView) v.findViewById(R.id.chakan);
            pinluen = (TextView) v.findViewById(R.id.pinluen);
            dianzan = (TextView) v.findViewById(R.id.dianzan);
            tiem_post = (TextView) v.findViewById(R.id.tiem_post);
            deleteLayout = (LinearLayout) v.findViewById(R.id.deleteLayout);
            imgdelete = (ImageView) v.findViewById(R.id.img_delete);
            swipeRoot= (SwipeItemLayoutListview) v.findViewById(R.id.con_item_lay);
        }
    }
    public void doCollect(final int position, View view) {
        if (SJQApp.user != null) {
            PostBean news = mData.get(position);
            String url = AppUrl.OPERATEPOSTADD;
            String FromGUID = news.getGuid();
            RequestParams params = new RequestParams();
            params.setHeader("Content-Type","application/json");
            try {
                JSONObject param  = new JSONObject();
                param.put("classType","0");
                param.put("userGUID", SJQApp.user.guid);
                param.put("fromGUID", FromGUID);
                param.put("favoriteMark", "0");
                param.put("operate", "0");
                StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
                params.setBodyEntity(sEntity);
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            getHttpUtils().send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException error, String msg) {
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> n) {
                            Gson gson = new Gson();
                            SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
                            if (bean != null) {
                                if (bean.result.operatResult) {
                                }else{
                                }
                            }

                        }
                    });
            mExplosionField.clear();
            mExplosionField.explode(view);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mData.remove(position);
                    notifyDataSetChanged();
                    if(mData.size()==0){
                        Intent intent = new Intent();
                        intent.setAction("Postshow");
                        mContex.sendBroadcast(intent);
                    }
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable, 800);
        }

    }
    HttpUtils getHttpUtils() {
        HttpUtils httpUtil = new HttpUtils(6 * 1000);
        return httpUtil;

    }

    ExplosionField mExplosionField;
    public void setChangData(boolean is) {
    }
    public void setChangData(boolean is, ExplosionField mExplosionField) {
        this.mExplosionField = mExplosionField;
        notifyDataSetChanged();
    }
    public void showDelete(boolean mode){
        isdeletemode = mode;
        notifyDataSetChanged();
    }



}
