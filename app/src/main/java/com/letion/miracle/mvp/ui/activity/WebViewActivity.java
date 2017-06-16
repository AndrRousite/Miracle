package com.letion.miracle.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.letion.geetionlib.base.BaseActivity;
import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.util.UiUtils;
import com.letion.miracle.R;
import com.letion.miracle.di.component.DaggerWebViewComponent;
import com.letion.miracle.di.module.WebViewModule;
import com.letion.miracle.mvp.contract.WebViewContract;
import com.letion.miracle.mvp.presenter.WebViewPresenter;

import butterknife.BindView;

import static com.letion.geetionlib.util.Preconditions.checkNotNull;

@Route(path = "/activity/web")
public class WebViewActivity extends BaseActivity<WebViewPresenter> implements WebViewContract.View {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.container)
    LinearLayout container;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerWebViewComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .webViewModule(new WebViewModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_web_view; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.init(this,container,toolbarTitle);
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
    protected void onResume() {
        mPresenter.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mPresenter.onPause();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPresenter.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}