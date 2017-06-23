package com.letion.miracle;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by liu-feng on 2017/6/6.
 */
public class MiracleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)) return;
        if (BuildConfig.LOG_DEBUG){
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
        LeakCanary.install(this);
    }
}
