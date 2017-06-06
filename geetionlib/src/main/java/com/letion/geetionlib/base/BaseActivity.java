package com.letion.geetionlib.base;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.letion.geetionlib.base.delegate.IActivity;
import com.letion.geetionlib.mvp.IPresenter;
import com.letion.geetionlib.util.UiUtils;
import com.letion.geetionlib.vender.immersionbar.ImmersionBar;
import com.letion.geetionlib.vender.log.L;
import com.letion.geetionlib.weight.swipeback.SwipeBackLayout;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import javax.inject.Inject;

import static com.letion.geetionlib.base.delegate.ActivityDelegate.LAYOUT_FRAMELAYOUT;
import static com.letion.geetionlib.base.delegate.ActivityDelegate.LAYOUT_LINEARLAYOUT;
import static com.letion.geetionlib.base.delegate.ActivityDelegate.LAYOUT_RELATIVELAYOUT;

/**
 * Created by liu-feng on 2017/6/5.
 */
public abstract class BaseActivity<P extends IPresenter> extends RxAppCompatActivity implements IActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected SwipeBackLayout mSwipeBackLayout;
    @Inject
    protected P mPresenter;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }
        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }
        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }
        return view == null ? super.onCreateView(name, context, attrs) : view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!applyImmersionBar())
            ImmersionBar.with(this).init();
        if (canSwipeBack()) {
            getWindow().setBackgroundDrawable(new ColorDrawable(0));
            //getWindow().getDecorView().setBackgroundDrawable(null);
            mSwipeBackLayout = new SwipeBackLayout(this);
            L.d(getResources().getDisplayMetrics().widthPixels + "");
            mSwipeBackLayout.setEdgeSize(UiUtils.dip2px(this, 50));
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
            mSwipeBackLayout.setEnableGesture(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
        ImmersionBar.with(this).destroy();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (canSwipeBack()) {
            mSwipeBackLayout.attachToActivity(this);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (canSwipeBack()) {
            mSwipeBackLayout.setEdgeSize(UiUtils.dip2px(this, 50));
        }
    }

    /**
     * 是否使用eventBus,默认为使用(false)，
     *
     * @return
     */
    @Override
    public boolean useEventBus() {
        return false;
    }

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link BaseFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    @Override
    public boolean useFragment() {
        return true;
    }

    /**
     * 执行 渲染沉浸式状态栏
     *
     * @return
     */
    protected boolean applyImmersionBar() {
        return false;
    }

    /**
     * activity 设置是否滑动退出
     *
     * @return
     */
    protected boolean canSwipeBack() {
        return false;
    }
}


