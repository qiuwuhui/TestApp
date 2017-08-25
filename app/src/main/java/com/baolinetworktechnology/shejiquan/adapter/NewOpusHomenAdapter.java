package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.CommentListActivity;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.socialize.utils.Log;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/16.
 * 首页效果图列表适配器
 */

public class NewOpusHomenAdapter extends BaseAdapter {
    protected Context mContext;
    protected ArrayList<SwCase> mDatas = new ArrayList<>();
    protected BitmapUtils mImageUtil;
    private String TAG = "NewOpusHomenAdapter";
    private int posItem;
    private HttpUtils httpUtil;
    public NewOpusHomenAdapter(Context context) {
        this.mContext = context;
        mImageUtil = new BitmapUtils(context);
        //加载图片类
        mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
        httpUtil = new HttpUtils(8 * 1000);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public SwCase getItem(int position) {
        if (position < 0 || position >= mDatas.size())
            return null;
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder vh = null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(),
                    R.layout.item_case_home_new, null);
            vh = new Holder();
            vh.mTvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            vh.mTvTips = (TextView) convertView.findViewById(R.id.tvTips);
            vh.mTvCost = (TextView) convertView.findViewById(R.id.tvCost);
            vh.mImage = (ImageView) convertView.findViewById(R.id.iv_image);

            vh.clickFabulousimage = (ImageView) convertView.findViewById(R.id.clickFabulousimage);
            vh.Collectionimage = (ImageView) convertView.findViewById(R.id.Collectionimage);
            vh.commentimage = (ImageView) convertView.findViewById(R.id.commentimage);
            vh.shareimage = (ImageView) convertView.findViewById(R.id.shareimage);
            vh.clickFabulousnum = (TextView) convertView.findViewById(R.id.clickFabulousnum);
            vh.Collectionnum = (TextView) convertView.findViewById(R.id.Collectionnum);
            vh.commentnum = (TextView) convertView.findViewById(R.id.commentnum);

            vh.clickFabulous = (RelativeLayout) convertView.findViewById(R.id.clickFabulous);
            vh.Collection = (RelativeLayout) convertView.findViewById(R.id.Collection);
            vh.comment = (RelativeLayout) convertView.findViewById(R.id.comment);
            convertView.setTag(vh);
        } else {
            vh = (Holder) convertView.getTag();
        }
        final SwCase myanliBean = mDatas.get(position);
        vh.mTvTitle.setText(myanliBean.getTitle());
        vh.mTvTips.setText(myanliBean.getTips());
        vh.mTvCost.setText("收藏　" + myanliBean.getNumFavorite());
        mImageUtil.display(vh.mImage, myanliBean.getSmallImages());
        vh.clickFabulousnum.setText("0");
        vh.Collectionnum.setText(myanliBean.getNumFavorite() + "");
        vh.commentnum.setText(myanliBean.getNumComment() + "");

        vh.clickFabulousimage.setBackgroundResource(R.drawable.dian_zan_jie);
        vh.clickFabulousnum.setTextColor(mContext.getResources().getColor(R.color.text_normal6));
        if (myanliBean.isGood()) {
            vh.clickFabulousimage.setBackgroundResource(R.drawable.dian_zan_jie_on);
            vh.clickFabulousnum.setTextColor(mContext.getResources().getColor(R.color.app_color));
        } else {
            vh.clickFabulousimage.setBackgroundResource(R.drawable.dian_zan_jie);
            vh.clickFabulousnum.setTextColor(mContext.getResources().getColor(R.color.text_normal6));
        }
        vh.clickFabulousnum.setText(myanliBean.getNumGood()+"");
        vh.Collectionimage.setBackgroundResource(R.drawable.nav_button_wsce_default);
        vh.Collectionnum.setTextColor(mContext.getResources().getColor(R.color.text_normal6));
        if (myanliBean.isFavorite()) {
            vh.Collectionimage.setBackgroundResource(R.drawable.nav_button_wsce_on);
            vh.Collectionnum.setTextColor(mContext.getResources().getColor(R.color.app_color));
        } else {
            vh.Collectionimage.setBackgroundResource(R.drawable.nav_button_wsce_default);
            vh.Collectionnum.setTextColor(mContext.getResources().getColor(R.color.text_normal6));
        }
        vh.mImage.setOnClickListener(new View.OnClickListener() {//详情页
            @Override
            public void onClick(View view) {
                Log.e("TAG图片","---");
                if (myanliBean == null){
                    return;
                }
                Intent intent = new Intent(mContext, WebOpusActivity.class);
				String url = AppUrl.DETAIL_CASE2 + myanliBean.id+"&r="+System.currentTimeMillis();
				intent.putExtra("WEB_URL", url);
				intent.putExtra("classTitle", "");
				intent.putExtra(WebDetailActivity.GUID, myanliBean.guid);
				intent.putExtra(AppTag.TAG_ID, myanliBean.id);
				intent.putExtra(WebDetailActivity.ISCASE, true);
				intent.putExtra(AppTag.TAG_JSON, myanliBean.toString());
                intent.putExtra(WebOpusActivity.POSITION,position);//行号
//                intent.putExtra(WebOpusActivity.ISFAVORITE,myanliBean.isFavorite());//是否收藏
//                intent.putExtra(WebOpusActivity.FAVORITENUM,myanliBean.getNumFavorite());//收藏数量
                intent.putExtra(WebOpusActivity.ISFABULOUS,myanliBean.isGood());//是否点赞
                intent.putExtra(WebOpusActivity.FABULOUSNUM,myanliBean.getNumGood());//点赞数量
                intent.putExtra(WebOpusActivity.COMMENTNUM,myanliBean.getNumComment());//评论数量
                mContext.startActivity(intent);
            }
        });
        //点赞
        vh.clickFabulous.setTag(position);
        vh.clickFabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG点赞","---");
                posItem = (int) v.getTag();
                if (SJQApp.user == null) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }
                if (mDatas.get(posItem).isGood()) {
                    return;
                }
                String url = AppUrl.SAVEPOSTSGOOD;
                RequestParams params = new RequestParams();
                params.setHeader("Content-Type", "application/json");
                try {
                    JSONObject param = new JSONObject();
//                    param.put("userGUID",myanliBean.guid);
                    param.put("sendUserGuid", SJQApp.user.guid);
                    if (SJQApp.userData != null) {
                        param.put("sendUserLogo", SJQApp.userData.getLogo());
                        param.put("sendUserName", SJQApp.userData.getName());
                    } else if (SJQApp.ownerData != null) {
                        param.put("sendUserLogo", SJQApp.ownerData.getLogo());
                        param.put("sendUserName", SJQApp.ownerData.getName());
                    } else {
                        param.put("sendUserLogo", SJQApp.user.logo);
                        param.put("sendUserName", SJQApp.user.logo);
                    }
                    param.put("markName", "3");
                    param.put("sendUserIdentity", SJQApp.user.markName);
                    param.put("postsId", myanliBean.getId());
                    param.put("postsGuid", myanliBean.guid);
                    param.put("postsTitle", myanliBean.getTitle());
                    StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
                    params.setBodyEntity(sEntity);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                httpUtil.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
                        new RequestCallBack<String>() {

                            @Override
                            public void onFailure(HttpException error, String msg) {
//						if (dialog == null)
//							return;
//						dialog.dismiss();
//						toastShow("当前网络连接失败");
                                Toast.makeText(mContext, "当前网络连接失败",
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> n) {
//						if (dialog == null)
//							return;
//						dialog.dismiss();
                                Gson gson = new Gson();
                                SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
                                if (bean != null) {
                                    if (bean.result.operatResult) {
                                        myanliBean.setGood(true);
                                        myanliBean.setNumGood(myanliBean.getNumGood()+1);
                                        Toast.makeText(mContext, "点赞成功",
                                                Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                }
                            }
                        });


            }
        });
        //收藏
        vh.Collection.setTag(position);
        vh.Collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG收藏","---");
                posItem = (int) v.getTag();
                doPost();
            }
        });
        //评论
        vh.comment.setTag(position);
        vh.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                posItem = (int) v.getTag();
                Intent intent = new Intent(mContext, CommentListActivity.class);
                intent.putExtra(CommentListActivity.FORM_GUID, mDatas.get(posItem).guid);
                intent.putExtra(CommentListActivity.CLASS_TYPE, 0);
                intent.putExtra("TAG","FROMXGT");//效果图列表
                intent.putExtra("POSITION",position);
                intent.putExtra("PLNUMBER",myanliBean.getNumComment());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.e("TAG评论1",position+"---"+myanliBean.getNumComment());
                mContext.startActivity(intent);
            }
        });

        //分享
        vh.shareimage.setTag(position);
        vh.shareimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posItem = (int) v.getTag();
                Intent intent = new Intent();
                intent.setAction("HomeCaseShare");
                intent.putExtra("id", mDatas.get(posItem).getId() + "");
                intent.putExtra("title", mDatas.get(posItem).getTitle());
                intent.putExtra("descriptions", mDatas.get(posItem).getDescriptions());
                mContext.sendBroadcast(intent);
            }
        });
        return convertView;
    }


    public class Holder {
        ImageView mImage;
        TextView mTvTitle, mTvTips, mTvCost;
        //底部Item控件
        ImageView clickFabulousimage, Collectionimage, commentimage, shareimage;
        TextView clickFabulousnum, Collectionnum, commentnum;
        RelativeLayout clickFabulous,Collection,comment;
    }

    public void setData(ArrayList<SwCase> data) {
        if (mDatas != null) {
            mDatas.clear();
            if (data != null) {
                mDatas.addAll(data);
            }
        } else {
            mDatas = data;
        }

    }

    public void setData1(ArrayList<SwCase> data) {
        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
        }
    }

    public void addData(ArrayList<SwCase> data) {
        if (mDatas != null) {
            if (data != null) {
                mDatas.addAll(data);
            }
        } else {
            mDatas = data;
        }
    }

    public void updateItemView(int position, String string) {//刷新整个item
        Gson gson = new Gson();
        SwCase bean = gson.fromJson(string, SwCase.class);
        mDatas.get(position).setFavorite(bean.isFavorite());
        mDatas.get(position).setNumFavorite(bean.getNumFavorite());
        mDatas.get(position).setGood(bean.isGood());
        mDatas.get(position).setNumGood(bean.getNumGood());
        mDatas.get(position).setNumComment(bean.getNumComment());
        notifyDataSetChanged();
    }
    public void updateItemView(int position, int number) {//刷新整个item
//        Gson gson = new Gson();
//        SwCase bean = gson.fromJson(string, SwCase.class);
//        mDatas.get(position).setFavorite(bean.isFavorite());
//        mDatas.get(position).setNumFavorite(bean.getNumFavorite());
//        mDatas.get(position).setGood(bean.isGood());
//        mDatas.get(position).setNumGood(bean.getNumGood());
//        mDatas.get(position).setNumComment(bean.getNumComment());
        Log.e("TAG评论4",position+"---"+number);
        mDatas.get(position).setNumComment(number);
        notifyDataSetChanged();
    }

    public void doPost() {
        if (SJQApp.user != null) {
            String url = AppUrl.FAVORITE_ADD;
            RequestParams params = new RequestParams();
            params.setHeader("Content-Type", "application/json");
            try {
                JSONObject param = new JSONObject();
                param.put("classType", "2");
                param.put("userGUID", SJQApp.user.guid);
                param.put("fromGUID", mDatas.get(posItem).guid);
                if (mDatas.get(posItem).isFavorite()) {
                    param.put("operate", "0");
                } else {
                    param.put("operate", "1");
                }
                StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
                params.setBodyEntity(sEntity);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            getHttpUtils().send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            toastShow("服务器开小差...");
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> n) {
                            Gson gson = new Gson();
                            SwresultBen bean = gson.fromJson(n.result, SwresultBen.class);
                            if (bean != null) {
                                if (bean.result.operatResult) {
                                    if (mDatas.get(posItem).isFavorite()) {
                                        toastShow("取消收藏成功");
                                        mDatas.get(posItem).setFavorite(false);
                                        mDatas.get(posItem).setNumFavorite(mDatas.get(posItem).getNumFavorite() - 1);
                                        notifyDataSetChanged();
                                    } else {
                                        toastShow("收藏成功");
                                        mDatas.get(posItem).setFavorite(true);
                                        mDatas.get(posItem).setNumFavorite(mDatas.get(posItem).getNumFavorite() + 1);
                                        notifyDataSetChanged();
                                    }
                                } else {
                                    toastShow(bean.result.operatMessage);
                                }
                            }

                        }
                    });
        } else {
            go2Login();
        }
    }

    HttpUtils getHttpUtils() {
        HttpUtils httpUtil = new HttpUtils(6 * 1000);
        return httpUtil;

    }

    private void go2Login() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void toastShow(String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }
}
