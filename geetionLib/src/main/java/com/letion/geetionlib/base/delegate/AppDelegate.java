package com.letion.geetionlib.base.delegate;

import android.app.Application;

/**
 * AppDelegate可以代理Application的生命周期,在对应的生命周期,执行对应的逻辑,因为Java只能单继承
 * 而我的框架要求Application要继承于BaseApplication
 * 所以当遇到某些三方库需要继承于它的Application的时候,就只有自定义Application继承于三方库的Application
 * 再将BaseApplication的代码复制进去,而现在就不用再复制代码,只用在对应的生命周期调用AppDelegate对应的方法(Application一定要实现APP接口)
 * <p>
 * Created by liu-feng on 2017/6/5.
 */
public interface AppDelegate {

    void attachBaseContext(Application application);

    void onCreate(Application application);

    void onTerminate(Application application);

}

