package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */

public class SortModelList {
    public List<SortModel> result;
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this).toString();
    }
}
