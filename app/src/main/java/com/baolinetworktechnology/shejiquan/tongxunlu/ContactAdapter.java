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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.AnewfriendActivity;
import com.baolinetworktechnology.shejiquan.activity.MainNoteActivity;
import com.baolinetworktechnology.shejiquan.activity.NewLabelActivity;
import com.baolinetworktechnology.shejiquan.activity.OwnerShowActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.AnewModel;
import com.baolinetworktechnology.shejiquan.domain.SortModel;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiang on 12/3/15.
 * 根据当前权限进行判断相关的滑动逻辑
 */
public class ContactAdapter extends BaseAdapter<SortModel,ContactAdapter.ContactViewHolder>
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
    private boolean Isser=false;
    private AnewModel comanewModel=null;
    public ContactAdapter(Context ct, List<SortModel> mLists) {
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
        Isser=false;
        mHeaderView = null;
        if(list != null){
            SortModel sortModel = new SortModel();
            sortModel.setSortLetters("@");
            list.add(0,sortModel);
        }
        mLists = list;
        this.addAll(list);
    }
    public void serList(List<SortModel> list) {
        Isser=true;
        mHeaderView = null;
        mLists = list;
        this.addAll(list);
    }

    public void serList1(List<SortModel> list) {
        Isser=false;
        mHeaderView = null;
        mLists = list;
        this.addAll(list);
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public void setHeaderModel(AnewModel anewModel) {
        comanewModel =anewModel;
    }
    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(!Isser){
            if(mHeaderView != null && viewType == TYPE_HEADER) return new ContactViewHolder(mHeaderView);
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ContactViewHolder holder, final int position) {
        if(!Isser){
            if(getItemViewType(position) == TYPE_HEADER) {
                holder.list_tongzhi.setVisibility(View.GONE);
                holder.list_add.setVisibility(View.GONE);
                if(comanewModel !=null && comanewModel.getIsHint()!=0){
                    holder.list_add.setVisibility(View.VISIBLE);
                    mImageUtil.display(holder.Image_long, comanewModel.getLogo());
                    holder.add_name.setText(comanewModel.getName());
                    holder.add_py_mun.setText(comanewModel.getIsHint()+"");
                }else{
                    holder.list_tongzhi.setVisibility(View.VISIBLE);
                }
                holder.list_tongzhi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, AnewfriendActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
                holder.list_biaoqian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, NewLabelActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
                holder.list_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        comanewModel =null;
                        Intent addintent = new Intent();
                        addintent.setAction("addpengyou");
                        mContext.sendBroadcast(addintent);
                        Intent intent = new Intent(mContext, AnewfriendActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
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
                holder.beizhu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SortModel sortModel= mLists.get(position);
                        Intent intent = new Intent(mContext, MainNoteActivity.class);
                        intent.putExtra(AppTag.TAG_JSON, sortModel.toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
                holder.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction("showTong");
                        intent.putExtra("position", position);
                        mContext.sendBroadcast(intent);
//                    mContext.deleteUser(position);
                    }
                });
                holder.ll_constact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String markName = mLists.get(position).getMarkName();
                        Intent intents = null;
                        if (markName.equals("DESIGNER")) {
                            intents = new Intent(mContext, WeiShopActivity.class);
                            intents.putExtra(AppTag.TAG_GUID, mLists.get(position).getFriendGUID());
                            intents.putExtra(AppTag.TAG_ID, "");
                            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intents);
                        } else if (markName.equals("PERSONAL")) {
                            intents = new Intent(mContext, OwnerShowActivity.class);
                            intents.putExtra(AppTag.TAG_GUID, mLists.get(position).getFriendGUID());
                            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intents);
                        } else if (markName.equals("ROBOT")) {
                            intents = new Intent(mContext, OwnerShowActivity.class);
                            intents.putExtra(AppTag.TAG_GUID, mLists.get(position).getFriendGUID());
                            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intents);
                        }

                    }
                });
                holder.mName.setText(getItem(position).getshowName());
                mImageUtil.display(holder.icon, getItem(position).getLogo());
            }
        }else{
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
                holder.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction("showTong");
                        intent.putExtra("position",position);
                        mContext.sendBroadcast(intent);
//                    mContext.deleteUser(position);
                    }
                });
                holder.beizhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SortModel sortModel= mLists.get(position);
                    Intent intent = new Intent(mContext, MainNoteActivity.class);
                    intent.putExtra(AppTag.TAG_JSON, sortModel.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                  }
                });
                holder.ll_constact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String markName=mLists.get(position).getMarkName();
                        Intent intents =null;
                        if(markName.equals("DESIGNER")){
                            intents = new Intent(mContext, WeiShopActivity.class);
                            intents.putExtra(AppTag.TAG_GUID, mLists.get(position).getFriendGUID());
                            intents.putExtra(AppTag.TAG_ID,"");
                            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intents);
                        }else if(markName.equals("PERSONAL")){
                            intents = new Intent(mContext, OwnerShowActivity.class);
                            intents.putExtra(AppTag.TAG_GUID, mLists.get(position).getFriendGUID());
                            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intents);
                        }else if(markName.equals("ROBOT")){
                            intents = new Intent(mContext, OwnerShowActivity.class);
                            intents.putExtra(AppTag.TAG_GUID, mLists.get(position).getFriendGUID());
                            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intents);
                        }

                    }
                });

                TextView textView = holder.mName;
                CircleImg circleImg= holder.icon;
                textView.setText(getItem(position).getshowName());
                mImageUtil.display(circleImg,getItem(position).getLogo());
        }
    }

    @Override
    public long getHeaderId(int position) {
        if(!Isser){
            if (position==0){
                return -1;
            }else {
                return getItem(position).getSortLetters().charAt(0);
            }
        }else{
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
        if(showValue.equals("%")){
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
        public TextView beizhu,mDelete,add_name,add_py_mun;
        public CircleImg icon,Image_long;
        public View ll_constact,list_tongzhi,list_add,list_biaoqian;
        public ContactViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.item_contact_title);
            icon = (CircleImg) itemView.findViewById(R.id.icon);
            Image_long = (CircleImg) itemView.findViewById(R.id.Image_long);
            mRoot = (SwipeItemLayout) itemView.findViewById(R.id.item_contact_swipe_root);
            beizhu = (TextView) itemView.findViewById(R.id.item_contact_beizhu);
            mDelete = (TextView) itemView.findViewById(R.id.item_contact_delete);
            add_name = (TextView) itemView.findViewById(R.id.add_name);
            add_py_mun = (TextView) itemView.findViewById(R.id.add_py_mun);
            ll_constact = itemView.findViewById(R.id.ll_constact);
            list_tongzhi = itemView.findViewById(R.id.list_tongzhi);
            list_add = itemView.findViewById(R.id.list_add);
            list_biaoqian = itemView.findViewById(R.id.list_biaoqian);
        }
    }

    private int getListSize(List<SortModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
