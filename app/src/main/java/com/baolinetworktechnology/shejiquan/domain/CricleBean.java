package com.baolinetworktechnology.shejiquan.domain;

import android.net.ParseException;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public class CricleBean {
    private int id;
    private String guid;
    private String createTime;
    private String updateTime;
    private String updateUser;
    private String createUser;
    private String title;
    private String contents;

    private userInfo userInfo;
    private List<postsItemInfoList> postsItemInfoList;
    private List<postcommentList> comment;
    private List<goodsList> goods;
    public class userInfo{
        public int id;
        public String guid;
        public String name;
        public String identity;
        public String logo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }
    public CricleBean.userInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(CricleBean.userInfo userInfo) {
        this.userInfo = userInfo;
    }
    public List<postsItemInfoList> getPostsItemInfoList() {
        return postsItemInfoList;
    }

    public void setPostsItemInfoList(List<postsItemInfoList> postsItemInfoList) {
        this.postsItemInfoList = postsItemInfoList;
    }
    public List<postcommentList> getComment() {
        return comment;
    }

    public void setComment(List<postcommentList> comment) {
        this.comment = comment;
    }
    public List<goodsList> getGoods() {
        return goods;
    }

    public void setGoods(List<goodsList> goods) {
        this.goods = goods;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    private String FromatDate;// 格式化时间
    public String getFromatDate() {
        if (TextUtils.isEmpty(FromatDate)) {
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String str = formatter.format(curDate);
            if (createTime == null) {
                createTime = str;
            }
            dateDiff(createTime, str, "yyyy-MM-dd HH:mm:ss");
        }
        return FromatDate;
    }
    public String timeData(String time){
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date =null;
        String Time="";
        try {
            date =format.parse(time);
            Time= sdf.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return Time;
    }
    public String timeData1(String time){
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日");
        Date date =null;
        String Time="";
        try {
            date =format.parse(time);
            Time= sdf.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return Time;
    }
    public String timeData2(String time){
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date =null;
        String Time="";
        try {
            date =format.parse(time);
            Time= sdf.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return Time;
    }
    public void dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            long day = diff / nd;// 计算差多少天
            if (day == 0) {
                long hour = diff % nd / nh;// 计算差多少小时
                long min = diff % nd % nh / nm;// 计算差多少分钟
                if (hour < 0)
                    hour = 1;
                if (min < 0)
                    min = 1;
                if (hour == 0) {
                    if (min == 0) {
                        long sec = diff % nd % nh % nm / ns;// 计算差多少秒
                        if (sec < 60) {
                            sec = 0;
                            FromatDate = "刚刚";
                        }
//                        else {
//                            FromatDate = sec + "秒前";
//                        }
                        return;
                    }

                    FromatDate = min + "分钟以前";
                    return;
                }
                FromatDate = hour + "小时以前";
            } else {
                if(day >=365){
                    FromatDate = timeData(createTime);
                }else{
                    FromatDate = timeData1(createTime);
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
            FromatDate = timeData(createTime);
        } catch (java.text.ParseException e) {
            FromatDate = timeData(createTime);
            e.printStackTrace();
        }
    }
}