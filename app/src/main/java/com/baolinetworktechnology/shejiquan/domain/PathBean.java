package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PathBean implements Serializable{

    private String url;
    private int heigth;
    private int width;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    @Override
    public String toString() {
        return "PathBean{" +
                "url='" + url + '\'' +
                ", heigth=" + heigth +
                ", width=" + width +
                '}';
    }
}
