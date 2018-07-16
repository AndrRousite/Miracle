package com.letion.core;

import android.app.Application;
import android.content.Context;

/**
 * <p>com.letion.core.BaseApplication
 *
 * @author wuqi
 * @describe 简要描述
 * @date 2018/7/16 0016
 */
public class BaseApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
