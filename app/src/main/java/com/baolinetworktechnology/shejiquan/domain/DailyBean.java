package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/12.
 */

public class DailyBean {
    public ArrayList<Dailylise> listData;
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
