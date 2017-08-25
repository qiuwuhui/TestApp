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
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.CollectsjFragment;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.PostDetailsActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.NineGridTestModel;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.NineGridTestLayout;
import com.google.gson.Gson;
import com.guojisheng.koyview.ActionSheetDialog;
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
import java.security.Key;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/11.
 * 我的帖子 适配器
 */

public class myPostAdapter extends BaseAdapter {
    private String TAG = "myPostAdapter";
    private List<PostBean> mData = new ArrayList<>();
    List<String> pathList;
    private BitmapUtils mImageUtil;
    private Context mContex;
    private MyDialog dialog;
    private ViewHolder viewHolder;
    private boolean isdeletemode;
    private HashMap<Integer, Boolean> deleteMap = new HashMap<Integer, Boolean>();//保存item选中状态 key:position value:isclick
    public myPostAdapter(Context context) {
        this.mContex = context;
        mImageUtil = new BitmapUtils(context);
        mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
        dialog = new MyDialog(context);
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

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(),
                    R.layout.my_post_layout, null);
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
                pathList.add(bean.getPostsItemInfoList().get(c).path);
                dapathList.add(bean.getPostsItemInfoList().get(c).path);
            }
            viewHolder.imageLayout.setdaUrlList(dapathList);
            viewHolder.imageLayout.setUrlList(pathList);
            viewHolder.imageLayout.setSpacing(10);
        }
        viewHolder.Post_name.setText(bean.getTitle());
        if (TextUtils.isEmpty(bean.getContents())) {
            viewHolder.contents.setText("分享图片");
        } else {
            viewHolder.contents.setText(bean.getContents());
        }
        //遍历map,如果行号不存在则添加
        if (deleteMap.size() == 0){
            deleteMap.put(0,false);
        }
        if (position >= deleteMap.size()){
            deleteMap.put(position,false);
        }
        Log.e("2TAG"+TAG,deleteMap.toString());

//        while(iter.hasNext()){
//            int c = iter.next();
//            if (position == c){
//                break;
//            }
//            Log.e("TAG"+TAG,c+"");
//        }




//        for (int key:deleteMap.keySet()){
//            Log.e("TAG"+TAG, key+"");
//            if (position <= key){
//                break;
//            }else {
//                deleteMap.put(position,false);
//                break;
//            }
//        }
        viewHolder.chakan.setText(bean.getHits() + "");
        viewHolder.pinluen.setText(bean.getNumComment() + "");
        viewHolder.dianzan.setText(bean.getNumGood() + "");
        viewHolder.tiem_post.setText(bean.getFromatDate());
        final int itempost = position;
        //item点击跳转或选中
        viewHolder.Post_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemClick(position,bean);
            }
        });
        viewHolder.contents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemClick(position,bean);
            }
        });
        viewHolder.chao_zuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateHead(itempost);
            }
        });
        //打钩按钮选中item
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemSelected(position);
            }
        });
        if (isdeletemode) {//设置是否删除模式
            viewHolder.delete.setVisibility(View.VISIBLE);
            viewHolder.chao_zuo.setVisibility(View.GONE);
        } else {
            viewHolder.delete.setVisibility(View.GONE);
            viewHolder.chao_zuo.setVisibility(View.VISIBLE);
        }
        if (deleteMap.get(position)){//显示
            viewHolder.delete.setBackgroundResource(R.drawable.dian_zan_jie_on);
        }else{
            viewHolder.delete.setBackgroundResource(R.drawable.dian_zan_jie);
        }



        return convertView;
    }

    class ViewHolder {
        NineGridTestLayout imageLayout;
        ImageView chao_zuo, delete;
        TextView Post_name, contents;
        TextView chakan, pinluen, dianzan, tiem_post;

        public ViewHolder(View v) {
            chao_zuo = (ImageView) v.findViewById(R.id.chao_zuo);
            delete = (ImageView) v.findViewById(R.id.delete);
            imageLayout = (NineGridTestLayout) v.findViewById(R.id.layout_nine_grid);
            Post_name = (TextView) v.findViewById(R.id.Post_name);
            contents = (TextView) v.findViewById(R.id.contents);
            chakan = (TextView) v.findViewById(R.id.chakan);
            pinluen = (TextView) v.findViewById(R.id.pinluen);
            dianzan = (TextView) v.findViewById(R.id.dianzan);
            tiem_post = (TextView) v.findViewById(R.id.tiem_post);

        }
    }

    private void updateHead(final int itempost) {
        new ActionSheetDialog(mContex)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
//                .addSheetItem("分享", ActionSheetDialog.SheetItemColor.Blue,
//                        new ActionSheetDialog.OnSheetItemClickListener() {
//
//                            @Override
//                            public void onClick(int which) {
//
//                            }
//                        }).show();
                .addSheetItem("删除", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                deletePost(itempost);
                            }
                        }).show();
    }
    //删除单条
    private void deletePost(final int itempost) {
        dialog.show();
        PostBean bean = mData.get(itempost);
        String url = AppUrl.POSTREMOVE;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type", "application/json");
        try {
            JSONObject param = new JSONObject();
            param.put("guid", bean.getGuid());
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getHttpUtils().send(HttpRequest.HttpMethod.DELETE, AppUrl.API + url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        dialog.dismiss();
                        Toast.makeText(mContex, "删除帖子失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> n) {
                        dialog.dismiss();
                        Gson gson = new Gson();
                        SwresultBen bean = gson.fromJson(n.result, SwresultBen.class);
                        if (bean != null) {
                            if (bean.result.operatResult) {
                                mData.remove(itempost);
                                notifyDataSetChanged();
                                Toast.makeText(mContex, "删除帖子成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContex, "删除帖子失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    //批量删除帖子
    public void MoreDeletePost() {
        HashMap<Integer,String> map = new HashMap<>();//key  行号  value guid
        for (int i = 0 ; i < deleteMap.size();i++){
            if (deleteMap.get(i)){
                map.put(i,mData.get(i).getGuid());
            }
        }
        if (map.size() == 0){
            Toast.makeText(mContex,"请先选择要删除的帖子",Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList list = new ArrayList();
        for (int i = 0; i< map.size();i++){
            list.add(map.get(i));
        }
        dialog.show();
        String url = AppUrl.POSTMOREREMOVE;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type", "application/json");
        try {
            JSONObject param = new JSONObject();
            param.put("guidList", list.toString());
            param.put("userGuid ", SJQApp.user.getGuid());
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getHttpUtils().send(HttpRequest.HttpMethod.DELETE, AppUrl.API + url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.e("TAG"+TAG,msg.toString());
                        dialog.dismiss();
                        Toast.makeText(mContex, "删除帖子失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> n) {
                        Log.e("TAG"+TAG,n.toString());
                        dialog.dismiss();
                        Gson gson = new Gson();
                        Toast.makeText(mContex, "已删除", Toast.LENGTH_SHORT).show();
                        SwresultBen bean = gson.fromJson(n.result, SwresultBen.class);
//                        if (bean != null) {
//                            if (bean.result.operatResult) {
////                                mData.remove(itempost);
//                                notifyDataSetChanged();
//                                Toast.makeText(mContex, "删除帖子成功", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(mContex, "删除帖子失败", Toast.LENGTH_SHORT).show();
//                            }
//                        }
                    }
                });
    }
    HttpUtils getHttpUtils() {
        HttpUtils httpUtil = new HttpUtils(6 * 1000);
        return httpUtil;
    }

    public boolean isIsdeletemode(boolean isdelete) {
        isdeletemode = isdelete;
        return isdeletemode;
    }
    //listview item 点击跳转或者选中
    public void ItemClick(int position,PostBean bean) {
        if (isdeletemode){
            //编辑模式下,点击选中item
            ItemSelected(position);
        }else {
            Intent intent = new Intent(mContex, PostDetailsActivity.class);
            intent.putExtra(AppTag.TAG_GUID, bean.getGuid());
            mContex.startActivity(intent);
        }
    }
    //item选中
    public void ItemSelected(int position){
        if (deleteMap.get(position) == false){//未选中
            viewHolder.delete.setBackgroundResource(R.drawable.dian_zan_jie_on);
            deleteMap.put(position,true);
            Log.e("TAG"+TAG,deleteMap.toString());
            notifyDataSetChanged();
        }else {
            deleteMap.put(position,false);
            viewHolder.delete.setBackgroundResource(R.drawable.dian_zan_jie);
            Log.e("TAG"+TAG,deleteMap.toString());
            notifyDataSetChanged();
        }
    }

    public void emptyMap(){
        deleteMap.clear();
    }
    public void RemoveItem(ArrayList list){
        for (int i = 0 ;i < list.size();i++){
            mData.remove(i);
        }
        notifyDataSetChanged();
    }
}
