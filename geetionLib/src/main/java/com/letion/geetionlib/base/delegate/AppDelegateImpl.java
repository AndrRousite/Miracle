package com.letion.geetionlib.base.delegate;

import android.app.Application;
import android.content.Context;

import com.letion.geetionlib.base.App;
import com.letion.geetionlib.base.integration.ActivityLifecycle;
import com.letion.geetionlib.base.integration.IConfigManager;
import com.letion.geetionlib.base.integration.ManifestParser;
import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.di.component.DaggerAppComponent;
import com.letion.geetionlib.di.module.AppModule;
import com.letion.geetionlib.di.module.ClientModule;
import com.letion.geetionlib.di.module.ConfigModule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <p>com.letion.geetionlib.base.delegate.AppDelegateImpl
 *
 * @author wuqi
 * @describe 简要描述
 * @date 2018/4/24 0024
 */
public class AppDelegateImpl implements AppDelegate, App {
    private AppComponent mAppComponent;
    @Inject
    protected ActivityLifecycle mActivityLifecycle;
    private final List<IConfigManager> mModules;
    private List<AppDelegate> mAppLifecycles = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();

    public AppDelegateImpl(Context context) {
        this.mModules = new ManifestParser(context).parse();
        for (IConfigManager module : mModules) {
            module.injectAppLifecycle(context, mAppLifecycles);
            module.injectActivityLifecycle(context, mActivityLifecycles);
        }
    }

    @Override
    public void attachBaseContext(Application application) {
        for (AppDelegate lifecycle : mAppLifecycles) {
            lifecycle.attachBaseContext(application);
        }
    }

    @Override
    public void onCreate(Application application) {
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(application))//提供application
                .clientModule(new ClientModule())//用于提供okhttp和retrofit的单例
                .configModule(getConfigModule(application, mModules))//全局配置
                .build();
        mAppComponent.inject(this);

        application.registerActivityLifecycleCallbacks(mActivityLifecycle);

        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            application.registerActivityLifecycleCallbacks(lifecycle);
        }

        for (IConfigManager module : mModules) {
            module.registerComponents(application, mAppComponent.repositoryManager());
        }

        for (AppDelegate lifecycle : mAppLifecycles) {
            lifecycle.onCreate(application);
        }

    }

    @Override
    public void onTerminate(Application application) {
        if (mActivityLifecycle != null) {
            application.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                application.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mAppLifecycles != null && mAppLifecycles.size() > 0) {
            for (AppDelegate lifecycle : mAppLifecycles) {
                lifecycle.onTerminate(application);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mActivityLifecycles = null;
        this.mAppLifecycles = null;
    }


    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link IConfigManager}的实现类,和Glide的配置方式相似
     *
     * @return
     */
    private ConfigModule getConfigModule(Application context, List<IConfigManager>
            modules) {

        ConfigModule.Builder builder = ConfigModule.builder();

        for (IConfigManager module : modules) {
            module.applyOptions(context, builder);
        }

        return builder.build();
    }


    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
