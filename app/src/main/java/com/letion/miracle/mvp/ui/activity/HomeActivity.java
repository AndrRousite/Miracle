package com.letion.miracle.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.letion.geetionlib.base.BaseActivity;
import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.util.UiUtils;

import com.letion.miracle.di.component.DaggerHomeComponent;
import com.letion.miracle.di.module.HomeModule;
import com.letion.miracle.mvp.contract.HomeContract;
import com.letion.miracle.mvp.presenter.HomePresenter;

import com.letion.miracle.R;
import com.letion.miracle.mvp.ui.adapter.HomeAdapter;
import com.letion.miracle.mvp.ui.fragment.HomeFragment;


import static com.letion.geetionlib.util.Preconditions.checkNotNull;

@Route(path = "/activity/home")
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_home; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, HomeFragment.newInstance())
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


    @Override
    public void setAdapter(HomeAdapter adapter) {

    }

    @Override
    public void finishRefreshing() {

    }

    @Override
    public void onItemClick(int position) {

    }
}