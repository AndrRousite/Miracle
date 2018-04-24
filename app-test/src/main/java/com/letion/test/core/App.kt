package com.letion.test.core

import com.letion.geetionlib.base.BaseApplication
import com.squareup.leakcanary.LeakCanary

/**
 * <p>com.letion.test.core.App
 * @describe 简要描述
 *
 * @author wuqi
 * @date 2018/4/24 0024
 */
class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)
    }
}