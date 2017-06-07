package com.letion.miracle.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.letion.geetionlib.base.SwipeBackActivity;
import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.util.UiUtils;
import com.letion.miracle.R;
import com.letion.miracle.di.component.DaggerMainComponent;
import com.letion.miracle.di.module.MainModule;
import com.letion.miracle.mvp.contract.MainContract;
import com.letion.miracle.mvp.presenter.MainPresenter;

@Route(path = "/activity/main")
public class MainActivity extends SwipeBackActivity<MainPresenter> implements MainContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (mPresenter == null){
            UiUtils.SnackbarText("Presenter is null.");
        }else{
            UiUtils.SnackbarText("Presenter inject.");
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


}