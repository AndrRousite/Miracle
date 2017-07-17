package com.letion.updatelib.config;

/**
 * Created by liu-feng on 2017/6/14.
 */
public class UpdateConfig {
    public static String API_TOKEN = "c36415dbfd9dac047ecadeee10910475";
    public static String APP_ID = "5941e65b959d69642c0001d7";

    public static String url = "http://api.fir.im/apps/latest/" + UpdateConfig.APP_ID + "?api_token=" + UpdateConfig.API_TOKEN; // 检查更新接口

    public final static int WITH_DIALOG = 1;
    public final static int WITH_NOTIFITION = 2;

    public static int DialogOrNotification = WITH_DIALOG;//default
}
