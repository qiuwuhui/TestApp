package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/4/21.
 */

public class PushBean {
    private apsben aps;

    public apsben getAps() {
        return aps;
    }
    public class apsben{
        private alertben alert;
        public alertben getAlert() {
            return alert;
        }
    }
    public class alertben{
        private  String title;
        private  String body;
        private int type;
        private int badge;
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public int getBadge() {
            return badge;
        }

        public void setBadge(int badge) {
            this.badge = badge;
        }
        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
