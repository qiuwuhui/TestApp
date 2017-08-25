package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/2/22.
 */

public class SJQeaseuser {
    private String LoginName;//环信的id
    private String Avatar;//图片

    private String Nick;//名字

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getNick() {
        return Nick;
    }

    public void setNick(String nick) {
        Nick = nick;
    }
    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
