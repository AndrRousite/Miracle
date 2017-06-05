package com.letion.miracle;

import android.os.Bundle;

import com.letion.geetionlib.base.BaseActivity;
import com.letion.geetionlib.di.component.AppComponent;

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
}
