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

public class PostBean {
    private int id;
    private String guid;
    private String createTime;
    private String updateTime;
    private String title;
    private String contents;
    private int numGood;//点赞数
    private int numComment;//评论数
    private int numFavorite;//收藏数
    private boolean isdeletemode =false;

    public boolean isdeletemode() {
        return isdeletemode;
    }

    public void setIsdeletemode(boolean isdeletemode) {
        this.isdeletemode = isdeletemode;
    }
    public boolean isGood() {
        return isGood;
    }

    @Override
    public String toString() {
        return "PostBean{" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", numGood=" + numGood +
                ", numComment=" + numComment +
                ", numFavorite=" + numFavorite +
                ", hits=" + hits +
                ", isGood=" + isGood +
                ", isFavorite=" + isFavorite +
                ", userInfo=" + userInfo +
                ", postsItemInfoList=" + postsItemInfoList +
                ", FromatDate='" + FromatDate + '\'' +
                '}';
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private int hits;
    private boolean isGood ;//是否点赞
    private boolean isFavorite  ;//是否收藏
    private userInfo userInfo;
    private List<postsItemInfoList> postsItemInfoList;
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
    public PostBean.userInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(PostBean.userInfo userInfo) {
        this.userInfo = userInfo;
    }
    public List<postsItemInfoList> getPostsItemInfoList() {
        return postsItemInfoList;
    }

    public void setPostsItemInfoList(List<postsItemInfoList> postsItemInfoList) {
        this.postsItemInfoList = postsItemInfoList;
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

    public int getNumGood() {
        return numGood;
    }

    public void setNumGood(int numGood) {
        this.numGood = numGood;
    }

    public int getNumComment() {
        return numComment;
    }

    public void setNumComment(int numComment) {
        this.numComment = numComment;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
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
    public int getnumFavorite() {
        return numFavorite;
    }

    public void setnumFavorite(int numcommentNum) {
        this.numFavorite = numcommentNum;
    }
}