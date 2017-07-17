package com.letion.updatelib.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.letion.updatelib.config.DownloadConfig;
import com.letion.updatelib.utils.TDevice;
import com.letion.updatelib.view.UpdateDialog;

import java.lang.ref.WeakReference;

/**
 * Created by liu-feng on 2017/6/14.
 */
public class HandlerRunnable implements Runnable {
    private String versionName = "";
    private int versionCode;
    private UpdateHandler up_handler;

    public HandlerRunnable(Context context) {
        up_handler = new UpdateHandler(context);
        this.versionName = TDevice.getAppVersionName(context);
        this.versionCode = TDevice.getAppVersionCode(context);
    }

    private static class UpdateHandler extends Handler {
        WeakReference<Context> mActivityReference;

        public UpdateHandler(Context context) {
            mActivityReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            final Context context = mActivityReference.get();
            if (context != null) {
                switch (msg.arg1) {
                    case 1:
                        showNoticeDialog(context);
                        break;
                    case 2:
                        if (DownloadConfig.ISManual) {
                            DownloadConfig.LoadManual = false;
                            Toast.makeText(context, "网络不畅通", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 3:
                        if (DownloadConfig.ISManual) {
                            DownloadConfig.LoadManual = false;
                            Toast.makeText(context, "版本已是最新", Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void run() {
        UpdateThread update = new UpdateThread();
        update.start();

        Message msg = new Message();
        try {
            update.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (DownloadConfig.versionName == null) {
            Log.i("UpdateFun TAG", "获取的应用信息为空，不更新，请确认网络是否畅通或者应用ID及API_TOKEN是否正确");
            msg.arg1 = 2;
            up_handler.sendMessage(msg);
        } else if (DownloadConfig.versionCode > versionCode) {
            Log.i("UpdateFun TAG", "需更新版本");
            msg.arg1 = 1;
            up_handler.sendMessage(msg);
        } else {
            Log.i("UpdateFun TAG", "版本已是最新");
            msg.arg1 = 3;
            up_handler.sendMessage(msg);
        }
    }

    public static void showNoticeDialog(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UpdateDialog.class);
        ((Activity) context).startActivityForResult(intent, 100);
    }
}
