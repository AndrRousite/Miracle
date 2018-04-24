package com.letion.geetionlib.mvp;

import android.support.annotation.IntRange;

/**
 * Created by liu-feng on 2017/6/5.
 */
public interface IView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     * @param mode 模式 1：toast 2. snackbar 3. alert
     * @param message 提示信息
     */
    void showError(@IntRange(from = 1, to = 3) int mode, String message);
}
