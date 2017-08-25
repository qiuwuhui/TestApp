package com.wedviewdb;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/3/15.
 */

public class TestBean {
    public String email;
    public homeBean homes;
    public String httpsResourceUrl;
    public String id;
    public String mobile;
    public String name;
    public String nickname;
    public String password;
    public String regTime;
    public String resourceUrl;
    public String userData;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this).toString();
    }
}
