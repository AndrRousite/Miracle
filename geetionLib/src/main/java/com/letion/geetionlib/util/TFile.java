package com.letion.geetionlib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by liu-feng on 2017/6/14.
 */
public class TFile {

    /**
     * 创建未存在的文件夹
     *
     * @param file
     * @return
     */
    public static File makeFolder(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 返回缓存文件夹
     * 扩展卡的缓存目录 --> 自定义扩展卡的缓存目录 --> app 包名下的缓存目录
     */
    @SuppressLint("SdCardPath")
    public static File getCacheFolder(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file;
            //获取系统管理的sd卡缓存文件
            file = context.getExternalCacheDir();
            //如果获取的为空,就是用自己定义的缓存文件夹做缓存路径
            if (file == null) {
                file = new File("/mnt/sdcard/" + context.getPackageName());
                makeFolder(file);
            }
            return file;
        } else {
            return context.getCacheDir();
        }
    }
}
