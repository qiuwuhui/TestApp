package com.baolinetworktechnology.shejiquan.itemRecycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.AnewfriendActivity;
import com.baolinetworktechnology.shejiquan.activity.MembersActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.AnewModel;
import com.baolinetworktechnology.shejiquan.domain.NewLabelBean;
import com.baolinetworktechnology.shejiquan.domain.SortModel;
import com.baolinetworktechnology.shejiquan.domain.SortModelList;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.paramsMoel;
import com.baolinetworktechnology.shejiquan.tongxunlu.SwipeItemLayout;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
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
public class NewLableAdapter extends RecyclerView.Adapter{
    private Context context;
    private LayoutInflater mInflater;
    private RecyclerView mRecycler;
    private int posiItem;
    private MyDialog dialog;
    private List<NewLabelBean> mList =new ArrayList<>();
    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();
    public NewLableAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        dialog =new MyDialog(context);
    }
    public void setList(List<NewLabelBean> list) {
        mList = list;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycler = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lable_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position) {
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
        NewLabelBean paramsMoel=mList.get(position);
        holder1.lable_name.setText(paramsMoel.getLabelName()+"  ("+paramsMoel.getNumFriend()+")");
        List<SortModel> contactList =paramsMoel.getListFriend();
        String xianqin="";
        for (int i = 0; i < contactList.size(); i++){
            if(contactList.size()-1==i){
                xianqin +=contactList.get(i).getshowName();
            }else{
                xianqin +=contactList.get(i).getshowName()+",  ";
            }
        }
        holder1.lable_xianqin.setText(xianqin);
        holder1.lable_delete.setTag(position);
        holder1.lable_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               posiItem = (int) v.getTag();
                DeletePost();
            }
        });
        holder1.lable_conten.setTag(position);
        holder1.lable_conten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posiItem = (int) v.getTag();
                Intent intent = new Intent(context, MembersActivity.class);
                NewLabelBean bean= new NewLabelBean();
                bean = mList.get(posiItem);
                intent.putExtra("newlable", bean.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListSize(mList);
    }
    private int getListSize(List<NewLabelBean> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public SwipeItemLayout mRoot;
        TextView lable_delete,lable_name,lable_xianqin;
        View lable_conten;
        public MyViewHolder(View itemView) {
            super(itemView);
            mRoot = (SwipeItemLayout) itemView.findViewById(R.id.con_item_lay);
            lable_delete = (TextView) itemView.findViewById(R.id.lable_delete);
            lable_name = (TextView) itemView.findViewById(R.id.lable_name);
            lable_xianqin = (TextView) itemView.findViewById(R.id.lable_xianqin);
            lable_conten = itemView.findViewById(R.id.lable_conten);
        }
    }
    private void DeletePost(){
        dialog.show();
        String url = AppUrl.DELETEEASELABLE;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type","application/json");
        try {
            JSONObject param  = new JSONObject();
            param.put("labelGUID",mList.get(posiItem).getLabelGUID());
            param.put("userGUID",SJQApp.user.getGuid());
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        }catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if(dialog != null){
                    dialog.dismiss();
                }
                Gson gson = new Gson();
                SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
                if (bean != null) {
                    if (bean.result.operatResult) {
                        mList.remove(posiItem);
                        notifyItemRemoved(posiItem);
                        notifyItemRangeChanged(posiItem,getItemCount());
                        Intent intent = new Intent();
                        intent.setAction("deletelable");
                        context.sendBroadcast(intent);
                    }else{
                        toastShow(bean.result.operatMessage);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                dialog.dismiss();
                toastShow(arg1);
            }
        };
        HttpUtils httpUtil = new HttpUtils(8 * 1000);
        httpUtil.send(HttpRequest.HttpMethod.DELETE, AppUrl.API + url, params, callBack);
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
