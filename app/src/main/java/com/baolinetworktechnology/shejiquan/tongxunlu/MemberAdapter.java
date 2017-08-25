/**
 * created by jiang, 12/3/15
 * Copyright (c) 2015, jyuesong@gmail.com All Rights Reserved.
 * *                #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */

package com.baolinetworktechnology.shejiquan.tongxunlu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.AnewfriendActivity;
import com.baolinetworktechnology.shejiquan.activity.MainNoteActivity;
import com.baolinetworktechnology.shejiquan.activity.NewLabelActivity;
import com.baolinetworktechnology.shejiquan.activity.OwnerShowActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.AnewModel;
import com.baolinetworktechnology.shejiquan.domain.SortModel;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
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
 * Created by jiang on 12/3/15.
 * 根据当前权限进行判断相关的滑动逻辑
 */
public class MemberAdapter extends BaseAdapter<SortModel,MemberAdapter.ContactViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
{
    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();
    private List<SortModel> mLists;
    private Context mContext;
    private BitmapUtils mImageUtil;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;
    private EditText LabelName;
    public MemberAdapter(Context ct, List<SortModel> mLists) {
        this.mLists = mLists;
        mContext = ct;
        mImageUtil = new BitmapUtils(mContext);
        //加载图片类
        mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
        this.addAll(mLists);
    }
    public void setList(List<SortModel> list) {
        mHeaderView = null;
        mLists = list;
        this.addAll(list);
    }
    public void lableName(String lableName) {
        LabelName.setText(lableName);
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }
    @Override
    public MemberAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new ContactViewHolder(mHeaderView);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberAdapter.ContactViewHolder holder, final int position) {
            if(getItemViewType(position) == TYPE_HEADER) {
                holder.add_men.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent addintent = new Intent();
                        addintent.setAction("AddMen");
                        mContext.sendBroadcast(addintent);
                    }
                });
                String mun=mLists.size()-1+"";
                holder.mun_cy.setText("标签成员  ("+mun+")");
                if(!TextUtils.isEmpty(getItem(position).getName())){
                    holder.etitName.setText(getItem(position).getName());
                }
                holder.etitName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent addintent = new Intent();
                        addintent.setAction("LableName");
                        mContext.sendBroadcast(addintent);
                    }
                });
                LabelName =holder.etitName;
            }else {
                SwipeItemLayout swipeRoot = holder.mRoot;
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
                holder.beizu.setVisibility(View.GONE);
                holder.mDelete.setText("删除");
                holder.mDelete.setTag(position);
                holder.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int posiItem = (int) v.getTag();
                        DeletePost(posiItem);
                    }
                });
                TextView textView = holder.mName;
                CircleImg circleImg = holder.icon;
                textView.setText(getItem(position).getshowName());
                mImageUtil.display(circleImg, getItem(position).getLogo());
            }
    }

    @Override
    public long getHeaderId(int position) {
            if (position==0){
                return -1;
            }else {
                return getItem(position).getSortLetters().charAt(0);
            }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        View view =  holder.itemView;
        TextView textView = (TextView) view.findViewById(R.id.text_tv);
        ImageView guan_img = (ImageView) view.findViewById(R.id.guan_img);
        String showValue = getItem(position).getSortLetters();
        guan_img.setVisibility(View.GONE);
        if(showValue.equals("@")){
            guan_img.setVisibility(View.VISIBLE);
            textView.setText("特别关注");
        }else{
            guan_img.setVisibility(View.GONE);
            textView.setText(showValue);
        }
    }


    public int getPositionForSection(char section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mLists.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;

    }
    @Override
    public int getItemCount() {
          return getListSize(mLists);
//        return mHeaderView == null ? getListSize(mLists) : getListSize(mLists) + 1;
    }
    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public SwipeItemLayout mRoot;
        public TextView beizu,mDelete,add_men,mun_cy;
        public EditText etitName;
        public CircleImg icon;
        public ContactViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.item_contact_title);
            icon = (CircleImg) itemView.findViewById(R.id.icon);
            mRoot = (SwipeItemLayout) itemView.findViewById(R.id.item_contact_swipe_root);
            beizu = (TextView) itemView.findViewById(R.id.item_contact_beizhu);
            mDelete = (TextView) itemView.findViewById(R.id.item_contact_delete);
            add_men = (TextView) itemView.findViewById(R.id.add_men);
            mun_cy = (TextView) itemView.findViewById(R.id.mun_cy);
            etitName= (EditText) itemView.findViewById(R.id.etitName);
        }
    }
    private int getListSize(List<SortModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
    public String Label() {
        return LabelName.getText().toString().trim();
    }

    private void DeletePost(final int posiItem){
        remove(mLists.get(posiItem));
        mLists.remove(posiItem);
//        String url = AppUrl.DELETELABLEFRIEND;
//        RequestParams params = new RequestParams();
//        params.setHeader("Content-Type","application/json");
//        try {
//            JSONObject param  = new JSONObject();
//            param.put("labelGUID",lableguid);
//            param.put("friendGUID",mLists.get(posiItem).getFriendGUID());
//            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
//            params.setBodyEntity(sEntity);
//        }catch (JSONException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        RequestCallBack<String> callBack = new RequestCallBack<String>() {
//
//            @Override
//            public void onSuccess(ResponseInfo<String> arg0) {
//                Gson gson = new Gson();
//                SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
//                if (bean != null) {
//                    if (bean.result.operatResult) {
//                        toastShow(bean.result.operatMessage);
//                        remove(mLists.get(posiItem));
//                        mLists.remove(posiItem);
//                        Intent intent = new Intent();
//                        intent.setAction("showlable");
//                        mContext.sendBroadcast(intent);
//                    }else{
//
//                        toastShow(bean.result.operatMessage);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String arg1) {
//                toastShow(arg1);
//            }
//        };
//        HttpUtils httpUtil = new HttpUtils(8 * 1000);
//        httpUtil.send(HttpRequest.HttpMethod.DELETE, AppUrl.API + url, params, callBack);
//    }
//    /**
//     *
//     * @param url
//     *            请求URL,不包含域名
//     * @return RequestParams
//     */
//    public RequestParams getParams(String url) {
//
//        RequestParams params = new RequestParams();
//        if (SJQApp.user != null) {
//            params.setHeader("Token", SJQApp.user.Token);
//        } else {
//            params.setHeader("Token", null);
//        }
//        params.setHeader("Client", AppTag.CLIENT);
//        params.setHeader("Version", ApiUrl.VERSION);
//        params.setHeader("AppAgent", ApiUrl.APP_AGENT);
//        params.setHeader("Hash", MD5Util.getMD5String(url + AppTag.MD5));
//        return params;

    }
    private void toastShow(String str) {
        Toast.makeText(mContext,str,Toast.LENGTH_SHORT).show();
    }
}
