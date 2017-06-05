package com.letion.geetionlib.base;

import android.app.Application;

import com.letion.geetionlib.base.delegate.AppDelegate;
import com.letion.geetionlib.di.component.AppComponent;

/**
 * Created by liu-feng on 2017/6/5.
 */
public class BaseApplication extends Application implements App {

    private AppDelegate mAppDelegate;


    @Override
    public void onCreate() {
        super.onCreate();
        this.mAppDelegate = new AppDelegate(this);
        this.mAppDelegate.onCreate();
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate();
    }

    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return mAppDelegate.getAppComponent();
    }
}
