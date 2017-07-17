package com.letion.updatelib;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.letion.updatelib.config.DownloadConfig;
import com.letion.updatelib.config.UpdateConfig;
import com.letion.updatelib.module.DownloadThread;
import com.letion.updatelib.module.HandlerRunnable;
import com.letion.updatelib.utils.TDevice;
import com.letion.updatelib.view.DownloadDialog;

/**
 * Created by liu-feng on 2017/6/14.
 */
public class UpdateManager {
    private static Thread download;
    private static Thread update;
    private static volatile UpdateManager mInstance = null;

    private UpdateManager(Context context) {
        if (DownloadConfig.TOShowDownloadView != 2) {
            update = new Thread(new HandlerRunnable(context));
            update.start();
        }
    }

    public static UpdateManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (UpdateManager.class) {
                if (mInstance == null) {
                    mInstance = new UpdateManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 手动更新功能，调用该方法则会启动 getInstance --> UpdateThread
     *
     * @param context
     */
    public static void manualStart(Context context) {
        DownloadConfig.ISManual = true;
        if (!DownloadConfig.LoadManual) {
            DownloadConfig.LoadManual = true;
            getInstance(context);
        } else Toast.makeText(context, "正在更新中,请稍等", Toast.LENGTH_LONG).show();
    }

    public static void onResume(Context context) {
        if (DownloadConfig.TOShowDownloadView == 2) {
            DownloadConfig.saveFileName =
                    TDevice.getAppPackageName(context) + ".apk";
            if (UpdateConfig.DialogOrNotification == 1) {
                Intent intent = new Intent();
                intent.setClass(context, DownloadDialog.class);
                ((Activity) context).startActivityForResult(intent, 0);
            } else if (UpdateConfig.DialogOrNotification == 2) {
                Notification.Builder builder = notificationInit(context);
                download = new DownloadThread(context, builder);
                download.start();
            }
        } else {
            if (mInstance != null) mInstance = null;
        }
    }

    public static void onStop(Context context) {
        if (DownloadConfig.TOShowDownloadView == 2 && UpdateConfig.DialogOrNotification == 2) {
            download.interrupt();
        }
        if (update != null) {
            update.interrupt();
        }
        if (DownloadConfig.ISManual) {
            DownloadConfig.ISManual = false;
        }
        if (DownloadConfig.LoadManual) {
            DownloadConfig.LoadManual = false;
        }
    }

    private static Notification.Builder notificationInit(Context context) {
        Intent intent = new Intent(context, context.getClass());
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker("开始下载")
                .setContentTitle(TDevice.getAppName(context))
                .setContentText("正在更新")
                .setContentIntent(pIntent)
                .setWhen(System.currentTimeMillis());
        return builder;
    }

}
