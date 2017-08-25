package com.baolinetworktechnology.shejiquan.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.domain.PushBean;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class DemoIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        System.out.println("------SS------");
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        byte[] payload = msg.getPayload();
        if (payload != null) {
            String json = new String(payload);
            Gson gson = new Gson();
            PushBean bean = gson.fromJson(json, PushBean.class);
            if(bean != null){
                int type=bean.getAps().getAlert().getType();
                ShowLogin(context);
                switch (type){
                    case 4:
                        if(SJQApp.messageBean!=null){
                            int NumMessage=SJQApp.messageBean.getResult().getNumMessage()+1;
                            SJQApp.messageBean.getResult().setNumMessage(NumMessage);
                        }
                        break;
                    case 5:
                        if(SJQApp.messageBean!=null){
                            int NumComment=SJQApp.messageBean.getResult().getNumComment()+1;
                            SJQApp.messageBean.getResult().setNumComment(NumComment);
                        }
                        break;
                    case 6:
                        if(SJQApp.messageBean!=null){
                            int NumPostsGood=SJQApp.messageBean.getResult().getNumPostsGood()+1;
                            SJQApp.messageBean.getResult().setNumPostsGood(NumPostsGood);
                        }
                    case 7:
                        if(SJQApp.user != null){
                            addFriend(context);
                        }
                        break;
                }
                if(SJQApp.user != null){
                    showNotification(context, bean);
                    showBroadcast(context);
                }
            }
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        System.out.println("------SS------");
        System.out.println(cmdMessage.toString());
    }
    private String APP_FIRST = "APP_FIRST";
    private void ShowLogin(Context context){
        boolean isFirst = CacheUtils.getBooleanData(this, APP_FIRST, false);
        if(isFirst){
            if(SJQApp.user==null|| SJQApp.user.guid==null){
                Intent intent5 = new Intent(context, LoginActivity.class);
                startActivity(context, intent5);
                return;
            }
        }
    }

    private void startActivity(Context context, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    private void showBroadcast(Context context) {
        Intent intent15 = new Intent();
        intent15.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent15.setAction("ReadMag");
        context.sendBroadcast(intent15);
    }
    private void addFriend(Context context) {
        Intent intent15 = new Intent();
        intent15.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent15.setAction("addFriend");
        context.sendBroadcast(intent15);
    }
    private void showNotification(Context context,PushBean bean){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(bean.getAps().getAlert().getTitle())
                        .setContentText(bean.getAps().getAlert().getBody());
        Intent resultIntent = new Intent(context, NotificationClickReceiver.class);
        resultIntent.putExtra("payload",bean.toString());
        PendingIntent resultPendingIntent =PendingIntent.getBroadcast(context,0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        // 通知来时震动或者响铃
        // notification.sound
        // notification.vibrate
        //使用系统默认声音用下面这条
        notification.defaults = Notification.DEFAULT_SOUND;
        // 点击以后消失
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, notification);
    }
}
