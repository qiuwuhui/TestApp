package com.baolinetworktechnology.shejiquan.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/10.
 */

public class MyIntegral {
    //签到时间
    public String createTime;
    //签到时间格式化
    public String createTimeFormart;
    //总积分
    public int totalScore;
    // 每次签到积分
    public int signinScore;
    public String getTimeData(){
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date =null;
        String Time="";
        try {
            date =format.parse(createTime);
            Time= sdf.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return Time;
    }
}
