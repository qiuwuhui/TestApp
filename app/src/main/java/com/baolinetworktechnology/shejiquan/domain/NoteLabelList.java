package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */

public class NoteLabelList {
    public  List<LabelList> result;
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this).toString();
    }
}
