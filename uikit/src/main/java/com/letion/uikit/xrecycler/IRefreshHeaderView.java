package com.letion.uikit.xrecycler;

/**
 * Created by liu-feng on 2017/5/27.
 */
public interface IRefreshHeaderView {
    int STATE_NORMAL = 0;
    int STATE_RELEASE_TO_REFRESH = 1;
    int STATE_REFRESHING = 2;
    int STATE_DONE = 3;

    void move(float delta);

    boolean release();

    void complete();

    int getVisibleHeight();

    int getState();
}
