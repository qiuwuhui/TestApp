package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/14.
 */

public class SwDetailBen {
    public int id;
    public String guid;
    public String title;
    public int classID;
    public String author;
    private String logo;
    public String className;
    public String images;
    public String descriptions;
    public String contents;
    public int markStatus;
    public int numComment;
    public int numFavorite;
    public int hits;
    public String createTime;
    public String timeData(){
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
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
