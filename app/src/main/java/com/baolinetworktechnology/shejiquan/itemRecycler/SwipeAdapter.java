package com.baolinetworktechnology.shejiquan.itemRecycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.OwnerShowActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.AnewModel;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.baolinetworktechnology.shejiquan.domain.SortModel;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.goodsList;
import com.baolinetworktechnology.shejiquan.tongxunlu.SwipeItemLayout;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.google.gson.Gson;
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
 * Created by jhon on 2016/4/11.
 */
public class SwipeAdapter extends RecyclerView.Adapter{
    private Context context;
    private LayoutInflater mInflater;
    private RecyclerView mRecycler;
    private List<AnewModel> mList =new ArrayList<>();
    private BitmapUtils mImageUtil;
    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();
    public SwipeAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mImageUtil = new BitmapUtils(context);
        //加载图片类
        mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
    }
    public void setList(List<AnewModel> list) {
        mList = list;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycler = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.swipe_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder holder1 = (MyViewHolder)holder;
        SwipeItemLayout swipeRoot = holder1.mRoot;
        swipeRoot.setSwipeAble(true);
        swipeRoot.setDelegate(new SwipeItemLayout.SwipeItemLayoutDelegate() {
            @Override
            public void onSwipeItemLayoutOpened(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil.add(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutClosed(SwipeItemLayout swipeItemLayout) {
                mOpenedSil.remove(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutStartOpen(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
        holder1.contact_delete.setTag(position);
        holder1.contact_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posiItem = (int) v.getTag();
                DeletePost(posiItem);
            }
        });
        holder1.tx_guan.setTag(position);
        holder1.tx_guan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posiItem = (int) v.getTag();
                SavePosts(posiItem);
            }
        });
        AnewModel news = mList.get(position);
        holder1.item_contact_title.setText(news.getName());
        mImageUtil.display(holder1.icon,news.getLogo());
    }

    @Override
    public int getItemCount() {
        return getListSize(mList);
    }
    private int getListSize(List<AnewModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public SwipeItemLayout mRoot;
        TextView contact_delete;
        TextView item_contact_title;
        CircleImg icon;
        ImageView tx_guan;
        public View ll_constact;
        public MyViewHolder(View itemView) {
            super(itemView);
            mRoot = (SwipeItemLayout) itemView.findViewById(R.id.swiLay);
            contact_delete = (TextView)itemView.findViewById(R.id.item_contact_delete);
            item_contact_title = (TextView)itemView.findViewById(R.id.item_contact_title);
            icon = (CircleImg) itemView.findViewById(R.id.icon);
            tx_guan = (ImageView) itemView.findViewById(R.id.tx_guan);
        }
    }
    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }
    private void SavePosts(final int posiItem){
        String designerGudi = mList.get(posiItem).getGuid();
        if(mList.get(posiItem).getMarkName().equals("DESIGNER")){
            String url = AppUrl.FAVORITE_ADD;
            RequestParams params = new RequestParams();
            params.setHeader("Content-Type","application/json");
            try {
                JSONObject param  = new JSONObject();
                param.put("classType","1");
                param.put("userGUID", SJQApp.user.guid);
                param.put("fromGUID", designerGudi);
                param.put("favoriteMark", "1");
                param.put("operate", "1");
                StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
                params.setBodyEntity(sEntity);
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpUtils httpUtil = new HttpUtils(8 * 1000);
            httpUtil.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            toastShow("当前网络连接失败");
                        }
                        @Override
                        public void onSuccess(ResponseInfo<String> n) {
                            Gson gson = new Gson();
                            SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
                            if (bean != null) {
                                if (bean.result.operatResult) {
                                    mList.remove(posiItem);
                                    notifyDataSetChanged();
                                    Intent intent = new Intent();
                                    intent.setAction("qiehuan");
                                    context.sendBroadcast(intent);
                                }else{
                                    toastShow("关注失败");
                                }
                            }
                        }
                    });
        }else {
            String url = AppUrl.ADDEASEFRIEND;
            RequestParams params = new RequestParams();
            params.setHeader("Content-Type","application/json");
            try {
                JSONObject param  = new JSONObject();
                param.put("userGuid", SJQApp.user.guid);
                param.put("fromGuid", designerGudi);
                StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
                params.setBodyEntity(sEntity);
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpUtils httpUtil = new HttpUtils(8 * 1000);
            httpUtil.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            toastShow("当前网络连接失败");
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> n) {
                            Gson gson = new Gson();
                            SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
                            if (bean != null) {
                                if (bean.result.operatResult) {
                                    mList.remove(posiItem);
                                    notifyDataSetChanged();
                                    Intent intent = new Intent();
                                    intent.setAction("qiehuan");
                                    context.sendBroadcast(intent);
                                }else{
                                    toastShow("关注失败");
                                }
                            }
                        }
                    });
        }
    }
    private void DeletePost(final int posiItem){
        List<AnewModel> anewModels =new ArrayList<>();
        anewModels.add(mList.get(posiItem));
        String url = AppUrl.EDITHINFRIEND;
        RequestParams params = getParams(url);
        params.addBodyParameter("enumHint","0");
        for (int c = 0; c < anewModels.size(); c++){
            params.addBodyParameter("guidList[]",anewModels.get(c).getEaseGuid());
        }
        RequestCallBack<String> callBack = new RequestCallBack<String>() {
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(ResponseInfo<String> n) {
                Gson gson = new Gson();
                SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
                if (bean != null) {
                    if (bean.result.operatResult) {
                        mList.remove(posiItem);
                        notifyItemRemoved(posiItem);
                        notifyItemRangeChanged(posiItem, getItemCount());
                    }else{
                        toastShow("删除失败");
                    }
                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
            }
        };
        HttpUtils httpUtil = new HttpUtils(8 * 1000);
        httpUtil.send(HttpRequest.HttpMethod.PUT, AppUrl.API + url, params, callBack);

    }
    /**
     *
     * @param url
     *            请求URL,不包含域名
     * @return RequestParams
     */
    public RequestParams getParams(String url) {

        RequestParams params = new RequestParams();
        if (SJQApp.user != null) {
            params.setHeader("Token", SJQApp.user.Token);
        } else {
            params.setHeader("Token", null);
        }
        params.setHeader("Client", AppTag.CLIENT);
        params.setHeader("Version", ApiUrl.VERSION);
        params.setHeader("AppAgent", ApiUrl.APP_AGENT);
        params.setHeader("Hash", MD5Util.getMD5String(url + AppTag.MD5));
        return params;

    }
    private void toastShow(String str) {
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
