package com.letion.miracle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.letion.geetionlib.base.BaseApplication;

/**
 * Created by liu-feng on 2017/6/6.
 */
public class MiracleApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.LOG_DEBUG){
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }
}
