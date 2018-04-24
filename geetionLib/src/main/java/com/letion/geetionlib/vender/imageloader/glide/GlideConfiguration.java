package com.letion.geetionlib.vender.imageloader.glide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.letion.geetionlib.base.App;
import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.http.OkHttpUrlLoader;
import com.letion.geetionlib.util.TFile;

import java.io.File;
import java.io.InputStream;

/**
 * Created by liu-feng on 16/4/15.
 */
@GlideModule(glideName = "GlideMiracle")
public class GlideConfiguration extends AppGlideModule {
    public static final int IMAGE_DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;//图片缓存文件最大值为100Mb

    @Override
    public void applyOptions(@NonNull final Context context, @NonNull GlideBuilder builder) {
        final AppComponent appComponent = ((App) context.getApplicationContext()).getAppComponent();

        builder.setDiskCache(new DiskCache.Factory() {
            @Nullable
            @Override
            public DiskCache build() {
                return DiskLruCacheWrapper.create(TFile.makeFolder(new File(appComponent
                        .cacheFile(), "Glide")), IMAGE_DISK_CACHE_MAX_SIZE);
            }
        });

        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull
            Registry registry) {
        //Glide默认使用HttpURLConnection做网络请求,在这切换成okhttp请求
        AppComponent appComponent = ((App) context.getApplicationContext()).getAppComponent();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory
                (appComponent.okHttpClient()));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return !super.isManifestParsingEnabled();
    }
}
