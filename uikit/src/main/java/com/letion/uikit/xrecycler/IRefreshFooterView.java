package com.letion.uikit.xrecycler;

/**
 * Created by liu-feng on 2017/5/27.
 */
public interface IRefreshFooterView {

    int STATE_LOADING = 0;
    int STATE_COMPLETE = 1;
    int STATE_NOMORE = 2;
    int STATE_ERROR = 3;

    void setState(int state);

    void reset();
}
