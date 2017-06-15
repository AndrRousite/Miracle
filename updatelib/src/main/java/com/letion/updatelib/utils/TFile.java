package com.letion.updatelib.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by liu-feng on 2017/6/14.
 */
public class TFile {
    private TFile() {
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted and app has appropriate permission. Else -
     * Android defines cache directory on device's file system.
     *
     * @param context Application context
     * @return Cache {@link File directory}
     */
    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
            appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
            if (!appCacheDir.exists()) {
                if (!appCacheDir.mkdirs()) {
                    Log.w("UpdateFun TAG", "Unable to create external cache directory");
                    return null;
                }
                try {
                    new File(appCacheDir, ".nomedia").createNewFile();
                } catch (IOException e) {
                    Log.i("UpdateFun TAG", "Can't create \".nomedia\" file in application external cache directory");
                }
            }
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            Log.w("UpdateFun TAG", "Can't define system cache directory! The app should be re-installed.");
        }
        return appCacheDir;
    }
}
