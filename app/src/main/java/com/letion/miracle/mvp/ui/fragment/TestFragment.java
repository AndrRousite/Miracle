package com.letion.miracle.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letion.geetionlib.base.BaseFragment;
import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.util.UiUtils;
import com.letion.geetionlib.vender.qqalert.AlertFactory;
import com.letion.miracle.R;
import com.letion.miracle.di.component.DaggerTestComponent;
import com.letion.miracle.di.module.TestModule;
import com.letion.miracle.mvp.contract.TestContract;
import com.letion.miracle.mvp.presenter.TestPresenter;

import butterknife.OnClick;

import static com.letion.geetionlib.util.Preconditions.checkNotNull;


public class TestFragment extends BaseFragment<TestPresenter> implements TestContract.View {

    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerTestComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .testModule(new TestModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }

    @OnClick({R.id.btnAlertDefault, R.id.btnAlertColoured, R.id.btnAlertCustomIcon, R.id.btnAlertTextOnly, R.id.btnAlertOnClick, R.id.btnAlertVerbose, R.id.btnAlertCallback, R.id.btnAlertInfiniteDuration})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAlertDefault:
                AlertFactory.create(getActivity())
                        .setTitle("Default Title")
                        .setText("this is a default text")
                        .disableOutsideTouch()
                        .show();
                break;
            case R.id.btnAlertColoured:
                AlertFactory.create(getActivity())
                        .setTitle("BackgroundColor Title")
                        .setText("this is a background text")
                        .setBackgroundColor(R.color.colorAccent)
                        .show();
                break;
            case R.id.btnAlertCustomIcon:
                AlertFactory.create(getActivity())
                        .setTitle("CustomIcon Title")
                        .setText("this is a icon text")
                        .setIcon(R.drawable.ic_alert_face)
                        .show();
                break;
            case R.id.btnAlertTextOnly:
                AlertFactory.create(getActivity())
                        .setText("this is a text only")
                        .show();
                break;
            case R.id.btnAlertOnClick:
                AlertFactory.create(getActivity())
                        .setTitle("Click inside Title")
                        .setText("this is a click inside")
                        .setDuration(10000)
                        .setOnClickListener(v -> UiUtils.SnackbarText("click a alert bar."))
                        .show();
                break;
            case R.id.btnAlertVerbose:
                AlertFactory.create(getActivity())
                        .setTitle("Verbose  Title")
                        .setText("The alert scales to accommodate larger bodies of text.\n " +
                                "The alert scales to accommodate larger bodies of text. \n" +
                                "The alert scales to accommodate larger bodies of text.\n")
                        .show();
                break;
            case R.id.btnAlertCallback:
                AlertFactory.create(getActivity())
                        .setTitle("Click inside Title")
                        .setText("this is a click inside")
                        .setDuration(10000)
                        .setOnShowListener(() -> UiUtils.SnackbarText("Show Alert"))
                        .setOnHideListener(() -> UiUtils.SnackbarText("Hide Alert"))
                        .show();
                break;
            case R.id.btnAlertInfiniteDuration:
                AlertFactory.create(getActivity())
                        .setTitle("Infinite Title")
                        .setText("alert infinite...")
                        .enableInfiniteDuration(true)   // 不自动消失
                        .show();
                break;
        }
    }
}