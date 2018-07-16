package com.letion.core.mvp;

import android.support.annotation.NonNull;

/**
 * Created by liufeng on 2018/7/16.
 *
 * @email w710989327@foxmail.com
 */
public interface IView {
    /**
     * 显示加载
     */
    default void showLoading() {

    }

    /**
     * 隐藏加载
     */
    default void hideLoading() {

    }

    /**
     * 显示信息
     *
     * @param message 消息内容, 不能为 {@code null}
     */
    void showMessage(@NonNull String message);
}
