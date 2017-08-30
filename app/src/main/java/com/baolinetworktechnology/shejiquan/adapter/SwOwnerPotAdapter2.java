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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.PostDetailsActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.fragment.CollectCaseFragment;
import com.baolinetworktechnology.shejiquan.fragment.CollectPostFragment;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.view.PerNineGridTestLayout;
import com.baolinetworktechnology.shejiquan.view.SwipeItemLayoutListview;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
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
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 我的--我的收藏--帖子 adapter
 */
public class SwOwnerPotAdapter2 extends BaseAdapter {
    private String TAG = "SwOwnerPotAdapter2";
    private List<PostBean> mData = new ArrayList<>();
    List<String> deleleteList;
    List<String> pathList;
    private BitmapUtils mImageUtil;
    private Context mContex;
    //    private HashMap<Integer, Boolean> deleteMap = new HashMap<Integer, Boolean>();//保存item选中状态 key:position value:isclick
    private boolean isdeletemode;

    public SwOwnerPotAdapter2(Context context) {
        this.mContex = context;
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

    public void setData(List<PostBean> data) {
        if (this.mData != null) {
            this.mData.clear();
        }
        if (data != null && data.size() != 0) {
            this.mData.addAll(data);
            deletemun();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
         ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(),
                    R.layout.item_owner_post_layout2, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PostBean bean = mData.get(position);
        List<String> dapathList = new ArrayList<>();
        if (bean.getPostsItemInfoList().size() != 0) {
            pathList = new ArrayList<>();
            for (int c = 0; c < bean.getPostsItemInfoList().size(); c++) {
                if (bean.getPostsItemInfoList().size() == 1) {
                    pathList.add(bean.getPostsItemInfoList().get(c).path);
                } else {
                    pathList.add(bean.getPostsItemInfoList().get(c).getSmallImages("_300_300."));
                }
                dapathList.add(bean.getPostsItemInfoList().get(c).path);
            }
            viewHolder.imageLayout.setdaUrlList(dapathList);
            viewHolder.imageLayout.setUrlList(pathList);
            viewHolder.imageLayout.setSpacing(10);
        }
        viewHolder.imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContex, PostDetailsActivity.class);
                intent.putExtra(AppTag.TAG_GUID, bean.getGuid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContex.startActivity(intent);
            }
        });
        viewHolder.Post_name.setText(bean.getTitle());
        if (TextUtils.isEmpty(bean.getContents())) {
            viewHolder.contents.setText("分享图片");
        } else {
            viewHolder.contents.setText(bean.getContents());
        }
        viewHolder.chakan.setText(bean.getHits() + "");
        viewHolder.pinluen.setText(bean.getNumComment() + "");
        viewHolder.dianzan.setText(bean.getNumGood() + "");
        viewHolder.tiem_post.setText(bean.getFromatDate());

        if (convertView.getAlpha() != 1) {
            convertView.setAlpha(1);
            convertView.setScaleX(1);
            convertView.setScaleY(1);
            convertView.setTranslationX(0);
            convertView.setEnabled(true);
        }
        if (bean.isDelete()) {
            viewHolder.imgdelete.setBackgroundResource(R.drawable.add_men_check);
        } else {
            viewHolder.imgdelete.setBackgroundResource(R.drawable.rb_bg_no);
        }
        viewHolder.imgdelete.setTag(parent);
        viewHolder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int podtitem = (Integer) view.getTag();
                if (mData.get(position).isDelete()) {
                    mData.get(position).setDelete(false);
                } else {
                    mData.get(position).setDelete(true);
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
        return convertView;
    }

    class ViewHolder {
        PerNineGridTestLayout imageLayout;
        TextView Post_name, contents;
        TextView chakan, pinluen, dianzan, tiem_post;
        LinearLayout deleteLayout;
        ImageView imgdelete;
        SwipeItemLayoutListview swipeRoot;

        public ViewHolder(View v) {
            imageLayout = (PerNineGridTestLayout) v.findViewById(R.id.layout_nine_grid);
            Post_name = (TextView) v.findViewById(R.id.Post_name);
            contents = (TextView) v.findViewById(R.id.contents);
            chakan = (TextView) v.findViewById(R.id.chakan);
            pinluen = (TextView) v.findViewById(R.id.pinluen);
            dianzan = (TextView) v.findViewById(R.id.dianzan);
            tiem_post = (TextView) v.findViewById(R.id.tiem_post);
            deleteLayout = (LinearLayout) v.findViewById(R.id.deleteLayout);
            imgdelete = (ImageView) v.findViewById(R.id.img_delete);
            swipeRoot = (SwipeItemLayoutListview) v.findViewById(R.id.con_item_lay);
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
                    if (mData.get(i).getGuid() != null) {
                        deleleteList.add(mData.get(i).getGuid());
                    }
                }
            }
            if (deleleteList.size() == 0) {
                Toast.makeText(mContex, "请先选择要删除的帖子", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = AppUrl.CANCELPOSTSFAVORITE;
            RequestParams params = new RequestParams();
            params.addBodyParameter("userGUID", SJQApp.user.guid);
            for (int i = 0; i < deleleteList.size(); i++) {
                params.addBodyParameter("fromGUIDList[]", deleleteList.get(i));
            }
            params.addBodyParameter("classType", "0");
//            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
//            params.setBodyEntity(sEntity);

            getHttpUtils().send(HttpRequest.HttpMethod.PUT, AppUrl.API + url, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Log.e("TAG1" + TAG, msg.toString());
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> n) {
                            Gson gson = new Gson();
                            SwresultBen bean = gson.fromJson(n.result, SwresultBen.class);
                            Log.e("TAG2" + TAG, bean.toString());
                            if (bean != null) {
                                if (bean.result.operatResult) {
                                    for (int j = 0; j < deleleteList.size(); j++) {
                                    for (int i = 0 ; i<mData.size();i++){
                                            if(deleleteList.get(j).equals(mData.get(i).getGuid())){
                                                mData.remove(i);
                                            }
                                        }
                                    }
                                    if(mData.size()==0){
                                        Intent intent = new Intent();
                                        intent.setAction("Postshow");
                                        mContex.sendBroadcast(intent);

                                    }else{
                                        notifyDataSetChanged();
                                    }
                                    deletemun();
//                                    Intent intent1 = new Intent();
//                                    intent1.setAction("RefreshSQ");
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

    public void setChangData(boolean isChange) {

    }

    ExplosionField mExplosionField;

    public void setChangData(boolean is, ExplosionField mExplosionField) {
        this.mExplosionField = mExplosionField;
        notifyDataSetChanged();
    }

    int lineColor = 0;


    public void showDelete(boolean mode) {
        if(!mode){
            if(mData !=null){
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
        Intent deletent = new Intent();
        deletent.setAction("deletemun");
        deletent.putExtra("mun",deletemun);
        mContex.sendBroadcast(deletent);
    }
}
