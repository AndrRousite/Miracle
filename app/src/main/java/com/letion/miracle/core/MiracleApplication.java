package com.letion.miracle.core;

import com.alibaba.android.arouter.launcher.ARouter;
import com.letion.geetionlib.base.BaseApplication;
import com.letion.miracle.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by liu-feng on 2017/6/6.
 */
public class MiracleApplication extends BaseApplication {
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
