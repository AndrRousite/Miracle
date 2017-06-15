package com.letion.updatelib.config;

import android.os.Environment;

/**
 * Created by liu-feng on 2017/6/14.
 */
public class DownloadConfig {
    public static boolean autoUpdate = false;
    public static String saveFileName = "newVersion.apk";
    public static String saveFilePath = Environment.getExternalStorageDirectory()+"/Letion/UpdatePath/";
    public static String apkUrl = "";
    public static String changeLog = "";
    public static String versionName = "";
    public static int versionCode = 0;
    public static boolean interceptFlag = false;

    public static Boolean ISManual = false;
    public static Boolean LoadManual = false;
    public static int TOShowDownloadView = 0;
}
