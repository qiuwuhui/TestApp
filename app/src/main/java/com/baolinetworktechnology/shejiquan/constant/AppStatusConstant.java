package com.baolinetworktechnology.shejiquan.constant;

/**
 * Created by Administrator on 2017/7/20.
 */

public class AppStatusConstant {
    public static final int STATUS_FORCE_KILLED=-1; //应用放在后台被强杀了
    public static final int STATUS_NORMAL=2;  //APP正常态
    //intent到MainActivity 区分跳转目的
    public static final String KEY_HOME_ACTION="key_home_action";//返回到主页面
    public static final int ACTION_BACK_TO_HOME=6; //默认值
    public static final int ACTION_RESTART_APP=9;//被强杀
}
