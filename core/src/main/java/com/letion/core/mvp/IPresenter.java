package com.letion.core.mvp;

/**
 * Created by liufeng on 2018/7/16.
 *
 * @email w710989327@foxmail.com
 */
public interface IPresenter {

    /**
     * 做一些初始化操作
     */
    void onAttach();

    /**
     * 在框架中 {@link Activity#onDestroy()} 时会默认调用 {@link IPresenter#onDestroy()}
     */
    void onDestroy();
}
