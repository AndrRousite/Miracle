package com.letion.geetionlib.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.letion.geetionlib.base.integration.IRepositoryManager;
import com.letion.geetionlib.base.integration.RepositoryManager;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * <p>com.letion.geetionlib.di.module.AppModule
 *
 * @author wuqi
 * @describe 提供一些框架必须的实例的 {@link Module}
 * <p>
 * @date 2018/4/24 0024
 */
@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    public Gson provideGson(Application application, @Nullable GsonConfiguration configuration){
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null)
            configuration.configGson(application, builder);
        return builder.create();
    }

    @Singleton
    @Provides
    public IRepositoryManager provideRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }
}
