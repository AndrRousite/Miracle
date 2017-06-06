package com.letion.miracle;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.letion.geetionlib.base.BaseActivity;
import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.vender.immersionbar.ImmersionBar;

@Route(path = "/activity/main")
public class MainActivity extends BaseActivity {

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected boolean applyImmersionBar() {
        ImmersionBar.with(this).init();
        return true;
    }

    @Override
    protected boolean canSwipeBack() {
        return true;
    }
}
