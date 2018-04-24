package com.letion.geetionlib.base;

import android.support.annotation.NonNull;

import com.letion.geetionlib.di.component.AppComponent;

/**
 * <p>com.letion.geetionlib.base.App
 *
 * @author wuqi
 * @describe 框架要求框架中的每个 {@link android.app.Application} 都需要实现此类,以满足规范
 * @date 2018/4/24 0024
 */
public interface App {
    @NonNull
    AppComponent getAppComponent();
}
