package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autonavi.amap.mapcore.MapSourceGridData;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.fragment.CollectCaseFragment;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.utils.WindowsUtil;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.SwipeItemLayoutListview;
import com.google.gson.Gson;
import com.guojisheng.koyview.ExplosionField;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 收藏资讯适配器
 *
 * @author JiSheng.Guo
 */
public class SwCollectOwnerOpusAdapter2 extends BaseAdapter {
    private String TAG = "SwCollectOwnerOpus2";
    List<SwCase> mData = new ArrayList<SwCase>();
    List<String> deleleteList;
    BitmapUtils mImageUtil;
    Context mContex;
    private boolean IsOpenWith = false;
    public SwCollectOwnerOpusAdapter2(Context context) {
        this(context, false);
    }

    public SwCollectOwnerOpusAdapter2(Context context, boolean isShowLine) {
        mImageUtil = new BitmapUtils(context);
        mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
        mContex = context;
    }

    public void setData(List<SwCase> data) {
        if (this.mData != null) {
            this.mData.clear();
        }
        if (data != null) {
            this.mData.addAll(data);
            deletemun();
            notifyDataSetChanged();
        }
    }

    public void addData(List<SwCase> data) {
        if (data != null)
            this.mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < 0 || position >= mData.size())
            return null;
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).id;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        SwCase news = mData.get(position);
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(),
                    R.layout.item_case_collect2, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_favorat.setText("收藏　" + news.getNumFavorite());
        viewHolder.tv_tips.setText(news.getTips());
        viewHolder.tv_title.setText(news.title);
        if (viewHolder.iv_image.getTag() == null
                || !viewHolder.iv_image.getTag().toString()
                .equals(news.getImages())) {
            mImageUtil.display(viewHolder.iv_image, news.getImages());
            viewHolder.iv_image.setTag(news.getImages());
        }
        if (news.isDelete()) {
            viewHolder.imgdelete.setBackgroundResource(R.drawable.add_men_check);
        } else {
            viewHolder.imgdelete.setBackgroundResource(R.drawable.rb_bg_no);
        }
        viewHolder.imgdelete.setTag(position);
        viewHolder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postitem = (Integer) view.getTag();
                if (mData.get(postitem).isDelete()) {
                    mData.get(postitem).setDelete(false);
                } else {
                    mData.get(postitem).setDelete(true);
                }
                deletemun();
                notifyDataSetChanged();
            }
        });
        viewHolder.swipeRoot.setSwipeAble(false);
        if (isdeletemode) {
            viewHolder.swipeRoot.openWithAnim();
        } else {
            viewHolder.swipeRoot.closeWithAnim();
        }
        if(position ==3){
            if(!IsOpenWith){
                IsOpenWith=true;
                notifyDataSetChanged();
            }
        }
        return convertView;
    }

    public void doCollect(final int position, View view) {
        if (SJQApp.user != null) {
            SwCase news = mData.get(position);
            String url = AppUrl.FAVORITE_ADD;
            String FromGUID = news.guid;
            RequestParams params = new RequestParams();
            params.setHeader("Content-Type", "application/json");
            try {
                JSONObject param = new JSONObject();
                param.put("classType", "2");
                param.put("userGUID", SJQApp.user.guid);
                param.put("fromGUID", FromGUID);
                param.put("favoriteMark", "0");
                param.put("operate", "0");
                StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
                params.setBodyEntity(sEntity);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            getHttpUtils().send(HttpMethod.POST, AppUrl.API + url, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException error, String msg) {

                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> n) {
                            Gson gson = new Gson();
                            SwresultBen bean = gson.fromJson(n.result, SwresultBen.class);
                            if (bean != null) {
                                if (bean.result.operatResult) {
                                    if(mData.size()==0){
                                        for (int j = 0; j < deleleteList.size(); j++) {
                                        for (int i = 0 ; i<mData.size();i++){
                                                if(deleleteList.get(j).equals(mData.get(i).guid)){
                                                    mData.remove(i);
                                                }
                                            }
                                        }
                                        Intent intent = new Intent();
                                        intent.setAction("Caseshow");
                                        mContex.sendBroadcast(intent);
                                    }else{
                                        notifyDataSetChanged();
                                    }
                                    deletemun();
                                } else {
                                    Toast.makeText(mContex,bean.result.operatMessage,Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
        }

    }

    public void doBitchCollect() {
        if(deleleteList == null){
            deleleteList= new ArrayList();
        }else{
            deleleteList.clear();
        }
        if (SJQApp.user != null) {
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).isDelete()) {
                    if (mData.get(i).guid != null) {
                        deleleteList.add(mData.get(i).guid);
                    }
                }
            }
            if (deleleteList.size() == 0) {
                Toast.makeText(mContex, "请先选择要删除的案例", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = AppUrl.FAVORITE_CANCEL;
            RequestParams params = new RequestParams();
            params.addBodyParameter("userGUID",SJQApp.user.guid);
            for (int i = 0; i < deleleteList.size(); i++) {
                params.addBodyParameter("fromGUIDList[]", deleleteList.get(i));
            }
            params.addBodyParameter("classType","2");
            params.addBodyParameter("favoriteMark","0");
            getHttpUtils().send(HttpMethod.PUT, AppUrl.API + url, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Log.e("TAG" + TAG + "onfs", msg.toString());
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> n) {
                            Gson gson = new Gson();
                            SwresultBen bean = gson.fromJson(n.result, SwresultBen.class);
                            Log.e("TAG" + TAG + "suc", n.toString());
                            if (bean != null) {
                                if (bean.result.operatResult) {
                                    for (int i = 0 ; i<mData.size();i++){
                                        for (int j = 0; j < deleleteList.size(); j++) {
                                            if(deleleteList.get(j).equals(mData.get(i).guid)){
                                                mData.remove(i);
                                            }
                                        }
                                    }
                                    if(mData.size()==0){
                                        Intent intent = new Intent();
                                        intent.setAction("Caseshow");
                                        mContex.sendBroadcast(intent);
                                    }else{
                                        notifyDataSetChanged();
                                    }
                                      deletemun();
//                                    Intent intent1 = new Intent();
//                                    intent1.setAction("refreshCase");
//                                    mContex.sendBroadcast(intent1);
                                } else {
                                    Toast.makeText(mContex,bean.result.operatMessage,Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
        }

    }

    HttpUtils getHttpUtils() {
        HttpUtils httpUtil = new HttpUtils(6 * 1000);
        return httpUtil;

    }

    class ViewHolder {
        TextView tv_title, tv_tips, tv_favorat, tvCost;
        ImageView iv_image, imgdelete;
        SwipeItemLayoutListview swipeRoot;
        LinearLayout deleteLayout;

        public ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.tvTitle);
            tv_tips = (TextView) view.findViewById(R.id.tvTips);
            tv_favorat = (TextView) view.findViewById(R.id.tv_favorat);
            tvCost = (TextView) view.findViewById(R.id.tvCost);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            deleteLayout = (LinearLayout) view.findViewById(R.id.deleteLayout);
            imgdelete = (ImageView) view.findViewById(R.id.img_delete);
            swipeRoot = (SwipeItemLayoutListview) view.findViewById(R.id.con_item_lay);
        }


    }

    public void setChangData(boolean isChange) {

    }

    public void setChangData(boolean is, ExplosionField mExplosionField) {
        // notifyDataSetChanged();
    }

    int lineColor = 0;

    /**
     * 设置分割线颜色
     *
     * @param color
     */
    public void setDLineColor(int color) {
        lineColor = color;

    }

    private boolean isdeletemode;

    public void showDelete(boolean mode) {
        if(!mode){
            if(mData!=null){
                for (int i = 0 ; i<mData.size();i++){
                    mData.get(i).setDelete(false);
                }
            }
        }
        isdeletemode = mode;
        notifyDataSetChanged();
    }
    private void deletemun(){
        int deletemun =0;
        for (int i = 0 ; i<mData.size();i++){
            if(mData.get(i).isDelete()){
                deletemun++;
            }
        }
        Intent deletent= new Intent();
        deletent.setAction("deletemun");
        deletent.putExtra("mun",deletemun);
        mContex.sendBroadcast(deletent);
    }
}
