package com.letion.geetionlib.util;

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
    private TFile() {
    }

    /**
     * 返回缓存文件夹
     * 扩展卡的缓存目录 --> 自定义扩展卡的缓存目录 --> app 包名下的缓存目录
     */
    public static File getCacheFolder(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = null;
            file = context.getExternalCacheDir();//获取系统管理的sd卡缓存文件
            if (file == null) {//如果获取的为空,就是用自己定义的缓存文件夹做缓存路径
                file = new File("/mnt/sdcard/" + context.getPackageName());
                makeFolder(file);
            }
            return file;
        } else {
            return context.getCacheDir();
        }
    }

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
     * 是否存在文件夹，不存在就新建
     *
     * @param strFolder
     * @return
     */
    public static boolean isFolderExists(String strFolder) {
        try {
            File file = new File(strFolder);
            if (file.exists()) {
                return true;
            } else {
                if (file.mkdir()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 检查文件是否存在
     *
     * @param string
     * @return
     */
    public static boolean isFileExists(String string) {
        try {
            File f = new File(string);
            if (f.exists()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 读取配置文件
     *
     * @param file
     * @return
     */
    public static Properties getProperties(String file) {
        Properties properties = new Properties();
        try {
            FileInputStream s = new FileInputStream(file);
            properties.load(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    /**
     * @param Path        目录
     * @param name1       扩展名(判断的文件类型的后缀名)
     * @param name2       扩展名(判断的文件类型的后缀名)
     * @param name3       扩展名(判断的文件类型的后缀名)
     * @param name4       扩展名(判断的文件类型的后缀名)
     * @param IsIterative 是否遍历子文件夹
     * @return
     */
    public static List<String> getFiles(String Path, String name1, String name2, String name3, String name4, boolean IsIterative) {
        List<String> fileList = new ArrayList<String>();
        File[] files = new File(Path).listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isFile()) {
                    if (f.getPath().substring(f.getPath().length() - name1.length()).equals(name1) || f.getPath().substring(f.getPath().length() - name1.length()).equals(name2)
                            || f.getPath().substring(f.getPath().length() - name1.length()).equals(name3) || f.getPath().substring(f.getPath().length() - name1.length()).equals(name4)) // 判断扩展名
                        fileList.add(f.getPath());
                    if (!IsIterative)
                        break; // 如果不进入子集目录则跳出
                } else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) // 忽略点文件（隐藏文件/文件夹）
                    getFiles(f.getPath(), name1, name2, name3, name4, IsIterative); // 这里就开始递归了
            }
        }
        return fileList;
    }
}
