package com.letion.geetionlib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.letion.geetionlib.base.delegate.IActivity;
import com.letion.geetionlib.mvp.IPresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.letion.geetionlib.base.delegate.ActivityDelegate.LAYOUT_FRAMELAYOUT;
import static com.letion.geetionlib.base.delegate.ActivityDelegate.LAYOUT_LINEARLAYOUT;
import static com.letion.geetionlib.base.delegate.ActivityDelegate.LAYOUT_RELATIVELAYOUT;

/**
 * Created by liu-feng on 2017/6/5.
 */
public abstract class BaseActivity<P extends IPresenter> extends RxAppCompatActivity implements
        IActivity {
    private Unbinder mUnbinder;

    @Inject
    @Nullable
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
        //依赖注入
        try {
            //如果initView返回0,框架则不会调用setContentView()
            if (getResourceId() != 0) {
                setContentView(getResourceId());
                //绑定到butterknife
                mUnbinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        this.mUnbinder = null;
        if (mPresenter != null) mPresenter.onDestroy();
        this.mPresenter = null;
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
     * 这个Activity是否会使用Fragment,
     * 框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link BaseFragment}
     * 的Fragment将不起任何作用
     *
     * @return
     */
    @Override
    public boolean useFragment() {
        return true;
    }
}


