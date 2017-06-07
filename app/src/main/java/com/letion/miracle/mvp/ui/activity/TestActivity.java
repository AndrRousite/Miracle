package com.letion.miracle.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.letion.geetionlib.base.BaseActivity;
import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.util.UiUtils;

import com.letion.miracle.di.component.DaggerTestComponent;
import com.letion.miracle.di.module.TestModule;
import com.letion.miracle.mvp.contract.TestContract;
import com.letion.miracle.mvp.presenter.TestPresenter;

import com.letion.miracle.R;
import com.letion.miracle.mvp.ui.fragment.TestFragment;


import static com.letion.geetionlib.util.Preconditions.checkNotNull;

@Route(path = "/activity/test")
public class TestActivity extends BaseActivity<TestPresenter> implements TestContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerTestComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .testModule(new TestModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_test; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, TestFragment.newInstance())
                .commit();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

}