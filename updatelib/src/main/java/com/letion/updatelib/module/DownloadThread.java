package com.letion.updatelib.module;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.letion.updatelib.config.DownloadConfig;
import com.letion.updatelib.config.UpdateConfig;
import com.letion.updatelib.utils.TDevice;
import com.letion.updatelib.utils.TFile;
import com.letion.updatelib.view.DownloadDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by liu-feng on 2017/6/14.
 */
public class DownloadThread extends Thread {

    private static final int DOWN_UPDATE = 1; // 下载中
    private static final int DOWN_OVER = 2; // 下载完成

    private Context context;
    private static int progress, length, count;
    private DownloadHandler mHandler;
    private static File apkFile;

    public DownloadThread(Context context) {
        this.context = context;
        mHandler = new DownloadHandler(context);
    }

    public DownloadThread(Context context, Notification.Builder builder) {
        this.context = context;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mHandler = new DownloadHandler(context, builder, notificationManager);
    }

    public void run() {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            url = new URL(DownloadConfig.apkUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i("UpdateFun TAG",
                String.format("ApkDownloadUrl:%s", DownloadConfig.apkUrl));
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            length = conn.getContentLength();
            is = conn.getInputStream();
        } catch (FileNotFoundException e0) {
//            e0.printStackTrace();
            try {
                conn.disconnect();
                conn = (HttpURLConnection) url.openConnection();
                conn.setInstanceFollowRedirects(false);
                conn.connect();
                String location = new String(conn.getHeaderField("Location").getBytes("ISO-8859-1"), "UTF-8").replace(" ", "");
                url = new URL(location);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                length = conn.getContentLength();
                is = conn.getInputStream();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }


        try {
            File file = TFile.getCacheDirectory(context);
            if (!file.exists()) {
                file.mkdir();
            }

            apkFile = new File(file, DownloadConfig.saveFileName);
            FileOutputStream fos = new FileOutputStream(apkFile);
            long tempFileLength = file.length();
            byte buf[] = new byte[1024];
            int times = 0; //这很重要
            int numread;
            do {
                numread = is.read(buf);
                count += numread;
                progress = (int) (((float) count / length) * 100);
                if ((times == 512) || (tempFileLength == length)) {
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    times = 0;
                }
                times++;
                if (numread <= 0) {
                    mHandler.sendEmptyMessage(DOWN_OVER);
                    break;
                }
                fos.write(buf, 0, numread);
            } while (!DownloadConfig.interceptFlag);// 点击取消就停止下载.
            fos.flush();
            fos.close();
            is.close();
            conn.disconnect();

            if (DownloadConfig.interceptFlag) {
                Log.i("UpdateFun TAG", "interrupt");
                length = 0;
                count = 0;
                DownloadConfig.interceptFlag = false;
                this.interrupt();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    private static class DownloadHandler extends Handler {
        WeakReference<Context> mContextReference;
        Notification.Builder builder;
        NotificationManager notificationManager;

        DownloadHandler(Context context) {
            mContextReference = new WeakReference<>(context);
        }

        DownloadHandler(Context context, Notification.Builder builder, NotificationManager notificationManager) {
            mContextReference = new WeakReference<>(context);
            this.builder = builder;
            this.notificationManager = notificationManager;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            Context context = mContextReference.get();
            switch (msg.what) {
                case DOWN_UPDATE:
                    if (UpdateConfig.DialogOrNotification == 1) {
                        ((DownloadDialog) context).progressBar.setProgress(progress);
                        ((DownloadDialog) context).textView.setText(progress + "%");
                    } else if (UpdateConfig.DialogOrNotification == 2) {
                        builder.setProgress(length, count, false)
                                .setContentText("下载进度:" + progress + "%");
                        notificationManager.notify(1115, builder.build());
                    }
                    break;
                case DOWN_OVER:
                    if (UpdateConfig.DialogOrNotification == 1) {
                        ((DownloadDialog) context).finish();
                    } else if (UpdateConfig.DialogOrNotification == 2) {
                        builder.setTicker("下载完成");
                        notificationManager.notify(1115, builder.build());
                        notificationManager.cancel(1115);
                    }
                    length = 0;
                    count = 0;
                    DownloadConfig.TOShowDownloadView = 1;
                    if (DownloadConfig.ISManual) {
                        DownloadConfig.LoadManual = false;
                    }
                    if (TDevice.checkApkSign(context, apkFile)) {
                        TDevice.installApk(context, apkFile);
                    }
                    break;
                default:
                    break;
            }
        }

    }

}
