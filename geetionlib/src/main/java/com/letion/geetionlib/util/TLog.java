package com.letion.geetionlib.util;

import android.util.Log;

/**
 * Created by liu-feng on 2017/6/15.
 */
public class TLog {
    private TLog() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static boolean isDebug = true;// 判断是否需要打印日志，可以在Application中初始化

    private static final String TAG = "MESSAGE";

    public static void setLogEnabled(boolean debug) {
        isDebug = debug;
    }

    // 下面四个默人的Tag函数
    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (isDebug) {
            Log.v(TAG, msg);
        }
    }

    // 下面为传入自定义的Tag函数
    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }
}
