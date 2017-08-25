package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.OwnerShowActivity;
import com.baolinetworktechnology.shejiquan.activity.PostDetailsActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.PerNineGridTestLayout;
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
import org.hamcrest.core.Is;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 社区列表适配器
 *
 * @author JiSheng.Guo
 */
public class PeriodListAdapter extends BaseAdapter {

    private List<PostBean> mData = new ArrayList<>();
    Context mContext;
    BitmapUtils mImageUtil;
    //	public MyDialog dialog;
    private HttpUtils httpUtil;

    public PeriodListAdapter(Context context) {
        this.mContext = context;
        mImageUtil = new BitmapUtils(context.getApplicationContext());
        mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
//		dialog = new MyDialog(mContext);
        httpUtil = new HttpUtils(8 * 1000);
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

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(),
                    R.layout.period_layout, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final PostBean bean = mData.get(position);
        mImageUtil.display(vh.shequ_loge, bean.getUserInfo().logo);
        vh.userName.setText(bean.getUserInfo().name);
        vh.isAuthor.setVisibility(View.VISIBLE);
        if (bean.getUserInfo().identity.equals("PERSONAL")) {
            vh.isAuthor.setVisibility(View.GONE);
        }
        if (bean.getUserInfo().identity.equals("ROBOT")) {
            vh.isAuthor.setVisibility(View.GONE);
        }
        vh.Post_name.setText(bean.getTitle());
        if (TextUtils.isEmpty(bean.getContents())) {
            vh.contents.setText("分享图片");
        } else {
            vh.contents.setText(bean.getContents());
        }
        //
        vh.Post_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Go2PostDetailsActivity(position,bean);
            }
        });
        vh.contents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Go2PostDetailsActivity(position,bean);
            }
        });
        List<String> pathList = new ArrayList<>();
        List<String> dapathList = new ArrayList<>();
        if (bean.getPostsItemInfoList().size() != 0) {
            for (int c = 0; c < bean.getPostsItemInfoList().size(); c++) {
                if (bean.getPostsItemInfoList().size() == 1) {
                    pathList.add(bean.getPostsItemInfoList().get(c).path);
                } else {
                    pathList.add(bean.getPostsItemInfoList().get(c).getSmallImages("_300_300."));
                }
                dapathList.add(bean.getPostsItemInfoList().get(c).path);
            }
        }
        vh.imageLayout.setUrlList(pathList);
        vh.imageLayout.setdaUrlList(dapathList);
        vh.imageLayout.setSpacing(10);
        vh.imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostDetailsActivity.class);
                intent.putExtra(AppTag.TAG_GUID, bean.getGuid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
//		viewHolder.chakan.setText(bean.getHits()+"");
//		viewHolder.pinluen.setText(bean.getNumComment()+"");
//		viewHolder.dianzan.setText(bean.getNumGood()+"");
        vh.clickFabulousimage.setBackgroundResource(R.drawable.dian_zan_jie);
        vh.clickFabulousNum.setTextColor(mContext.getResources().getColor(R.color.text_normal6));
        if (bean.isGood()) { //已经点赞
            vh.clickFabulousimage.setClickable(false);
            vh.clickFabulousimage.setBackgroundResource(R.drawable.dian_zan_jie_on);
            vh.clickFabulousNum.setTextColor(mContext.getResources().getColor(R.color.app_color));
        } else {
            vh.clickFabulousimage.setBackgroundResource(R.drawable.dian_zan_jie);
            vh.clickFabulousNum.setTextColor(mContext.getResources().getColor(R.color.text_normal6));
        }
        vh.Collectionimage.setBackgroundResource(R.drawable.nav_button_wsce_default);
        vh.CollectionNum.setTextColor(mContext.getResources().getColor(R.color.text_normal6));
        if (bean.isFavorite()) {
            vh.Collectionimage.setBackgroundResource(R.drawable.nav_button_wsce_on);
            vh.CollectionNum.setTextColor(mContext.getResources().getColor(R.color.app_color));
        } else {
            vh.Collectionimage.setBackgroundResource(R.drawable.nav_button_wsce_default);
            vh.CollectionNum.setTextColor(mContext.getResources().getColor(R.color.text_normal6));
        }
        vh.clickFabulousNum.setText(bean.getNumGood() + "");
        vh.CollectionNum.setText(bean.getnumFavorite() + "");
        vh.commentNum.setText(bean.getNumComment() + "");
        vh.tiem_post.setText(bean.getFromatDate());
        vh.figureLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (bean.getUserInfo().identity.equals("DESIGNER")) {
                    intent = new Intent(mContext, WeiShopActivity.class);
                    intent.putExtra(AppTag.TAG_GUID, bean.getUserInfo().guid);
                    intent.putExtra(AppTag.TAG_ID, "");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else if (bean.getUserInfo().identity.equals("PERSONAL")) {
                    intent = new Intent(mContext, OwnerShowActivity.class);
                    intent.putExtra(AppTag.TAG_GUID, bean.getUserInfo().guid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else if (bean.getUserInfo().identity.equals("ROBOT")) {
                    intent = new Intent(mContext, OwnerShowActivity.class);
                    intent.putExtra(AppTag.TAG_GUID, bean.getUserInfo().guid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        });
        vh.clickFabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bean.isGood()) {
                    SavePosts(mContext, bean);
                }
            }
        });
        vh.Collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCollect(bean);
            }
        });
        vh.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostDetailsActivity.class);
                intent.putExtra(AppTag.TAG_GUID, bean.getGuid());
                intent.putExtra(PostDetailsActivity.NEEDOPENKEYBORD, "NEED");
                intent.putExtra(PostDetailsActivity.POSITION, position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        PerNineGridTestLayout imageLayout;
        CircleImg shequ_loge;
        ImageView isAuthor;
        TextView userName, Post_name, contents;
        TextView chakan, pinluen, dianzan, tiem_post;
        View figureLay;
        ImageView clickFabulousimage, Collectionimage, commentimage;//点赞收藏评论
        TextView clickFabulousNum, CollectionNum, commentNum;

        RelativeLayout clickFabulous,Collection,comment;
        public ViewHolder(View v) {
            imageLayout = (PerNineGridTestLayout) v.findViewById(R.id.layout_nine_grid);
            isAuthor = (ImageView) v.findViewById(R.id.isAuthor);
            shequ_loge = (CircleImg) v.findViewById(R.id.shequ_loge);
            userName = (TextView) v.findViewById(R.id.userName);
            Post_name = (TextView) v.findViewById(R.id.Post_name);
            contents = (TextView) v.findViewById(R.id.contents);
            chakan = (TextView) v.findViewById(R.id.chakan);
            pinluen = (TextView) v.findViewById(R.id.pinluen);
            dianzan = (TextView) v.findViewById(R.id.dianzan);
            tiem_post = (TextView) v.findViewById(R.id.tiem_post);
            figureLay = v.findViewById(R.id.figureLay);
            clickFabulousimage = (ImageView) v.findViewById(R.id.clickFabulousimage);
            Collectionimage = (ImageView) v.findViewById(R.id.Collectionimage);
            commentimage = (ImageView) v.findViewById(R.id.commentimage);
            clickFabulousNum = (TextView) v.findViewById(R.id.clickFabulousnum);
            CollectionNum = (TextView) v.findViewById(R.id.Collectionnum);
            commentNum = (TextView) v.findViewById(R.id.commentnum);

            clickFabulous = (RelativeLayout) v.findViewById(R.id.clickFabulous);
            Collection = (RelativeLayout) v.findViewById(R.id.Collection);
            comment = (RelativeLayout) v.findViewById(R.id.comment);
        }
    }
    private void Go2PostDetailsActivity(int position,PostBean bean){//进入详情页
        if (bean == null){
//                    toastShow("数据出错");
            Toast.makeText(mContext,"数据出错",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(mContext, PostDetailsActivity.class);
        intent.putExtra(AppTag.TAG_GUID,bean.getGuid());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PostDetailsActivity.POSITION,position);//position
//        intent.putExtra("COLLECTIONNUMBER",bean.getnumFavorite());//收藏数
//        intent.putExtra("FABULOUSNUMBER",bean.getNumGood());//点赞数
        mContext.startActivity(intent);
    }

    //点赞
    private void SavePosts(Context context, final PostBean pbean) {
        if (SJQApp.user == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return;
        }
        if (pbean.isGood()) {
            return;
        }
        String url = AppUrl.SAVEPOSTSGOOD;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type", "application/json");
        try {
            JSONObject param = new JSONObject();
            param.put("classType", "0");
            param.put("sendUserGuid", SJQApp.user.guid);
            param.put("userGUID", pbean.getUserInfo().getGuid());
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
            param.put("markName", "1");
            param.put("sendUserIdentity", SJQApp.user.markName);
            param.put("postsId", pbean.getId());
            param.put("postsGuid", pbean.getGuid());
            param.put("postsTitle", pbean.getTitle());
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
                        SwresultBen bean = gson.fromJson(n.result, SwresultBen.class);
                        if (bean != null) {
                            if (bean.result.operatResult) {
                                pbean.setGood(true);
                                pbean.setNumGood(pbean.getNumGood() + 1);
                                Toast.makeText(mContext, "点赞成功",
                                        Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                        }

                    }
                });
    }

    //收藏
    private void doCollect(final PostBean pbean) {
        if (SJQApp.user != null) {
//			dialog.show();
            String url = AppUrl.OPERATEPOSTADD;
            RequestParams params = new RequestParams();
            params.setHeader("Content-Type", "application/json");
            try {
                JSONObject param = new JSONObject();
                param.put("classType", "0");
                param.put("userGUID", SJQApp.user.guid);
                param.put("fromGUID", pbean.getGuid());
                if (!pbean.isFavorite()) {
                    param.put("operate", "1");
                } else {
                    param.put("operate", "0");
                }
                param.put("favoriteMark", "0");
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
//							if (dialog == null)
//								return;
//							dialog.dismiss();
//							toastShow("当前网络连接失败");
                            Toast.makeText(mContext, "当前网络连接失败",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> n) {
//							if (dialog == null)
//								return;
//							dialog.dismiss();
                            Gson gson = new Gson();
                            SwresultBen bean = gson.fromJson(n.result, SwresultBen.class);
                            if (bean != null) {
                                if (bean.result.operatResult) {
                                    if (!pbean.isFavorite()) {
                                        pbean.setFavorite(true);
                                        pbean.setnumFavorite(pbean.getnumFavorite() + 1);
                                        Toast.makeText(mContext, "收藏成功",
                                                Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    } else {
                                        pbean.setFavorite(false);
                                        if (pbean.getnumFavorite() > 0) {
                                            pbean.setnumFavorite(pbean.getnumFavorite() - 1);
                                        }
                                        Toast.makeText(mContext, "取消收藏",
                                                Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }

//									if (mCollect.isChecked()) {
//										toastShow("收藏成功");
//										if(!TextUtils.isEmpty(isPost)){
//											Intent intent = new Intent();
//											intent.setAction("Postshow");
//											sendBroadcast(intent);
//										}
//									}else{
//										toastShow("取消收藏成功");
//										if(!TextUtils.isEmpty(isPost)){
//											Intent intent = new Intent();
//											intent.setAction("Postshow");
//											sendBroadcast(intent);
//										}
//									}
                                }
                            }

                        }
                    });
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
        }

    }

    //更新评论数
    public void updateItemView(int position, int commentNum) {
        if (position > 0) {
            mData.get(position - 1).setNumComment(commentNum);
            notifyDataSetChanged();
        }
    }

    //更新收藏状态
    public void updateCollection(int position, int number, boolean isCollection) {
        if (isCollection) {
            mData.get(position - 1).setFavorite(true);
            mData.get(position - 1).setnumFavorite(number);
        } else {
            mData.get(position - 1).setFavorite(false);
            mData.get(position - 1).setnumFavorite(number);
        }
        notifyDataSetChanged();
    }

    //更新点赞状态
    public void updateFobulous(int position, String string) {
        SwCase bean = new Gson().fromJson(string,SwCase.class);
        mData.get(position).setGood(bean.isGood());
        mData.get(position).setNumGood(bean.getNumGood());
        mData.get(position).setFavorite(bean.isFavorite());
        mData.get(position).setnumFavorite(bean.getNumFavorite());
        mData.get(position).setNumComment(bean.getNumComment());
        notifyDataSetChanged();
    }

}
