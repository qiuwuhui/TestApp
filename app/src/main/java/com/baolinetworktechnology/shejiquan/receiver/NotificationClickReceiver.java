package com.baolinetworktechnology.shejiquan.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.MessageActivity1;
import com.baolinetworktechnology.shejiquan.activity.MyPinLuenActivity;
import com.baolinetworktechnology.shejiquan.activity.ThumbUpActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.domain.PushBean;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/4/24.
 */

public class NotificationClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String json =intent.getStringExtra("payload");
        Gson gson = new Gson();
        PushBean bean = gson.fromJson(json, PushBean.class);
        /**
         * type 参数  资讯1，设计师2，案例3，系统消息4，评论5，帖子6，
         */
        int type=bean.getAps().getAlert().getType();
        switch (type){
            case 4:
                boolean result = GETActivity(context,"com.baolinetworktechnology.shejiquan.activity.MessageActivity1");
                if(!result){
                    Intent intent5 = new Intent(context, MessageActivity1.class);
                    startActivity(context, intent5);
                }
               break;
            case 5:
                boolean result1 = GETActivity(context,"com.baolinetworktechnology.shejiquan.activity.MyPinLuenActivity");
                if(!result1){
                    go2Fragment(MyPinLuenActivity.PinluenFragment,context);
                }
                break;
            case 6:
                boolean result2 = GETActivity(context,"com.baolinetworktechnology.shejiquan.activity.ThumbUpActivity");
                if(!result2){
                    Intent intent5 = new Intent(context, ThumbUpActivity.class);
                    startActivity(context, intent5);
                }
                break;
        }

    }
    private void go2Fragment(int type,Context context) {
        Intent intent = new Intent(context, MyPinLuenActivity.class);
        intent.putExtra(MyPinLuenActivity.TYPE, type);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    private void startActivity(Context context, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    private boolean GETActivity(Context context,String srt) {
        boolean result = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(2).get(0).topActivity;
        if (cn != null) {
            if (srt.equals(cn.getClassName())) {
                result = true;
            }
        }
        return result;
    }
}
