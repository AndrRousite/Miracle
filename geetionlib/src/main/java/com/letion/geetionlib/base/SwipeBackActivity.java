//package com.letion.geetionlib.base;
//
//import android.content.res.Configuration;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//
//import com.letion.geetionlib.mvp.IPresenter;
//import com.letion.geetionlib.util.UiUtils;
//import com.letion.geetionlib.vender.immersionbar.ImmersionBar;
//import com.letion.geetionlib.vender.log.L;
//import com.letion.uikit.swipeback.SwipeBackLayout;
//
///**
// * Created by liu-feng on 2017/6/7.
// */
//@Deprecated
//public abstract class SwipeBackActivity<T extends IPresenter> extends BaseActivity<T> {
//    protected SwipeBackLayout mSwipeBackLayout;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (!applyImmersionBar())
//            ImmersionBar.with(this).init();
//        if (canSwipeBack()) {
//            getWindow().setBackgroundDrawable(new ColorDrawable(0));
//            //getWindow().getDecorView().setBackgroundDrawable(null);
//            mSwipeBackLayout = new SwipeBackLayout(this);
//            L.d(getResources().getDisplayMetrics().widthPixels + "");
//            mSwipeBackLayout.setEdgeSize(UiUtils.dip2px(this, 50));
//            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
//            mSwipeBackLayout.setEnableGesture(true);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ImmersionBar.with(this).destroy();
//    }
//
//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        if (canSwipeBack()) {
//            mSwipeBackLayout.attachToActivity(this);
//        }
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (canSwipeBack()) {
//            mSwipeBackLayout.setEdgeSize(UiUtils.dip2px(this, 50));
//        }
//    }
//
//    /**
//     * 执行 渲染沉浸式状态栏
//     *
//     * @return
//     */
//    protected boolean applyImmersionBar() {
//        return false;
//    }
//
//    /**
//     * activity 设置是否滑动退出
//     *
//     * @return
//     */
//    protected boolean canSwipeBack() {
//        return false;
//    }
//}
