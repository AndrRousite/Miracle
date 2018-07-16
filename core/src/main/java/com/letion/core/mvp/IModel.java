package com.letion.core.mvp;

/**
 * Created by liufeng on 2018/7/16.
 *
 * @email w710989327@foxmail.com
 */
public interface IModel {
    /**
     * 在框架中 {@link BasePresenter#onDestroy()} 时会默认调用 {@link IModel#onDestroy()}
     */
    void onDestroy();
}
