package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */

public class NewLabelBean {
  private String labelGUID;
  private String labelName;
  private List<SortModel> listFriend;
  private int  numFriend;

    public String getLabelGUID() {
        return labelGUID;
    }

    public void setLabelGUID(String labelGUID) {
        this.labelGUID = labelGUID;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public List<SortModel> getListFriend() {
        return listFriend;
    }

    public void setListFriend(List<SortModel> listFriend) {
        this.listFriend = listFriend;
    }

    public int getNumFriend() {
        return numFriend;
    }

    public void setNumFriend(int numFriend) {
        this.numFriend = numFriend;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this).toString();
    }
}
