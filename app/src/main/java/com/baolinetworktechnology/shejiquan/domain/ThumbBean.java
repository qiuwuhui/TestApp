package com.baolinetworktechnology.shejiquan.domain;

import android.net.ParseException;
import android.text.TextUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ThumbBean implements Serializable {
    private String createTime;
    private String sendUserGuid;
    private int markName;
    private String sendUserLogo;
    private String sendUserIdentity;
    private int postsId;
    private String postsGuid;
    private String sendUserName;
    private int isRead;
    private String postsTitle;


    public String getPostsTitle() {
        return postsTitle;
    }

    public void setPostsTitle(String postsTitle) {
        this.postsTitle = postsTitle;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSendUserGuid() {
        return sendUserGuid;
    }

    public void setSendUserGuid(String sendUserGuid) {
        this.sendUserGuid = sendUserGuid;
    }
    public int getMarkName() {
        return markName;
    }

    public void setMarkName(int markName) {
        this.markName = markName;
    }


    public String getSendUserLogo() {
        return sendUserLogo;
    }

    public void setSendUserLogo(String sendUserLogo) {
        this.sendUserLogo = sendUserLogo;
    }

    public String getSendUserIdentity() {
        return sendUserIdentity;
    }

    public void setSendUserIdentity(String sendUserIdentity) {
        this.sendUserIdentity = sendUserIdentity;
    }

    public int getPostsId() {
        return postsId;
    }

    public void setPostsId(int postsId) {
        this.postsId = postsId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
    public String getPostsGuid() {
        return postsGuid;
    }

    public void setPostsGuid(String postsGuid) {
        this.postsGuid = postsGuid;
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
            dateDiff(timeData(createTime), str, "yyyy-MM-dd HH:mm:ss");
        }
        return FromatDate;
    }
    public String timeData(String time){
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                        if (sec < 10) {
                            sec = 0;
                            FromatDate = "刚刚";
                        } else {
                            FromatDate = sec + "秒前";
                        }
                        return;
                    }

                    FromatDate = min + "分钟前";
                    return;
                }
                FromatDate = hour + "小时前";
            } else {

                FromatDate = timeData(createTime);
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
