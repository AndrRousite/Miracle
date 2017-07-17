package com.letion.updatelib.module;

import android.util.Log;

import com.letion.updatelib.config.DownloadConfig;
import com.letion.updatelib.config.UpdateConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by liu-feng on 2017/6/14.
 */
public class UpdateThread extends Thread {

    private String result;

    public void run() {
        try {
            URL httpUrl = new URL(UpdateConfig.url);

            HttpURLConnection conn = (HttpURLConnection) httpUrl
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(3000);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;

                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                result = new String(sb.toString().getBytes(), "utf-8");

                interpretingData(result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void interpretingData(String result) {
        try {
            JSONObject object = new JSONObject(result);
            DownloadConfig.changeLog = object.getString("changelog");
            DownloadConfig.versionName = object.getString("versionShort");
            DownloadConfig.versionCode = Integer.parseInt(object.getString("version"));
            DownloadConfig.apkUrl = object.getString("installUrl");
            Log.i("UpdateFun TAG",
                    String.format("ChangeLog:%s, Version:%s, ApkDownloadUrl:%s",
                            DownloadConfig.changeLog, DownloadConfig.versionName, DownloadConfig.apkUrl));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
