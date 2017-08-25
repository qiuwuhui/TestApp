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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.SortModel;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jiang on 12/3/15.
 * 根据当前权限进行判断相关的滑动逻辑
 */
public class AddMenAdapter extends BaseAdapter<SortModel,AddMenAdapter.ContactViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
{
    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    private List<SortModel> filList;
    private List<SortModel> mLists;
    private List<CheckBox> add_chBox =new ArrayList<>();
    private List<SortModel> AddmLists =new ArrayList<>();
    private Context mContext;
    private BitmapUtils mImageUtil;
    private boolean IsfilList=false;
    public static final int TYPE_NORMAL = 1;
    public AddMenAdapter(Context ct, List<SortModel> mLists) {
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
        IsfilList=false;
        mLists = list;
        add_chBox.clear();
        this.addAll(list);
    }
    public void setfilList(List<SortModel> list) {
        IsfilList=true;
        filList = list;
        add_chBox.clear();
        this.addAll(list);
    }
    public void setAddList(List<SortModel> list) {
        AddmLists = list;
    }
    @Override
    public int getItemViewType(int position) {
        return TYPE_NORMAL;
    }
    @Override
    public AddMenAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_mem, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddMenAdapter.ContactViewHolder holder, final int position) {
                TextView textView = holder.mName;
                CircleImg circleImg = holder.icon;
                textView.setText(getItem(position).getshowName());
               mImageUtil.display(circleImg, getItem(position).getLogo());
               add_chBox.add(holder.add_chBox);
               holder.add_chBox.setChecked(false);
               for(SortModel sortModel : AddmLists) {
                   if (!TextUtils.isEmpty(sortModel.getFriendGUID())) {
                       if (sortModel.getFriendGUID().equals(getItem(position).getFriendGUID())) {
                           holder.add_chBox.setChecked(true);
                       }
                   }
               }
               holder.ll_constact.setOnClickListener(new View.OnClickListener() {
                      @Override
                     public void onClick(View v) {
                          AddchBox(position);
                       }
                     });
               holder.add_chBox.setOnClickListener(new View.OnClickListener() {
                      @Override
                     public void onClick(View v) {
                          AddchBox(position);
                      }
                    });
    }

    @Override
    public long getHeaderId(int position) {
          return getItem(position).getSortLetters().charAt(0);

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
          if(!IsfilList){
              return getListSize(mLists);
          }else{
              return getListSize(filList);
          }
    }
    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public View ll_constact;
        public CircleImg icon;
        public CheckBox add_chBox;
        public ContactViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.item_contact_title);
            icon = (CircleImg) itemView.findViewById(R.id.icon);
            ll_constact = itemView.findViewById(R.id.ll_constact);
            add_chBox = (CheckBox) itemView.findViewById(R.id.add_chBox);
        }
    }

    private int getListSize(List<SortModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
    private void AddchBox(int position) {
        int a=0;
        SortModel dinMode =mLists.get(position);
        if(!TextUtils.isEmpty(dinMode.getFriendGUID())){
            for(int i = 0; i < AddmLists.size(); i++){
                SortModel sortModel=AddmLists.get(i);
                if(!TextUtils.isEmpty(sortModel.getFriendGUID())){
                    if(sortModel.getFriendGUID().equals(dinMode.getFriendGUID())){
                        a++;
                        AddmLists.remove(i);
//                        add_chBox.get(position).setChecked(false);
                    }
                }
            }
        }
        if(a==0){
            AddmLists.add(dinMode);
//            add_chBox.get(position).setChecked(true);
        }
        Intent intent = new Intent();
        intent.setAction("removeMen");
        intent.putExtra("position",position);
        mContext.sendBroadcast(intent);
    }
}
