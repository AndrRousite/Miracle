package com.letion.miracle.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letion.geetionlib.base.BaseFragment;
import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.util.UiUtils;
import com.letion.uikit.qqalert.AlertFactory;
import com.letion.miracle.R;
import com.letion.miracle.di.component.DaggerTestComponent;
import com.letion.miracle.di.module.TestModule;
import com.letion.miracle.mvp.contract.TestContract;
import com.letion.miracle.mvp.presenter.TestPresenter;
import com.letion.miracle.mvp.ui.adapter.TestAdapter;
import com.letion.uikit.smartisan.LetionRefreshLayout;

import butterknife.BindView;

import static com.letion.geetionlib.util.Preconditions.checkNotNull;


public class TestFragment extends BaseFragment<TestPresenter> implements TestContract.View, LetionRefreshLayout.PullToRefreshListener {

    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.refreshView)
    LetionRefreshLayout refreshView;

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
        mPresenter.request();
        refreshView.setOnRefreshListener(this);
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

    @Override
    public void setAdapter(TestAdapter adapter) {
        listView.setAdapter(adapter);
        UiUtils.configRecycleView(listView, new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                AlertFactory.create(getActivity())
                        .setTitle("Default Title")
                        .setText("this is a default text")
                        .disableOutsideTouch()
                        .show();
                break;
            case 1:
                AlertFactory.create(getActivity())
                        .setTitle("BackgroundColor Title")
                        .setText("this is a background text")
                        .setBackgroundColor(R.color.colorAccent)
                        .show();
                break;
            case 2:
                AlertFactory.create(getActivity())
                        .setTitle("CustomIcon Title")
                        .setText("this is a icon text")
                        .setIcon(R.drawable.ic_alert_face)
                        .show();
                break;
            case 3:
                AlertFactory.create(getActivity())
                        .setText("this is a text only")
                        .show();
                break;
            case 4:
                AlertFactory.create(getActivity())
                        .setTitle("Click inside Title")
                        .setText("this is a click inside")
                        .setDuration(10000)
                        .setOnClickListener(v -> UiUtils.SnackbarText("click a alert bar."))
                        .show();
                break;
            case 5:
                AlertFactory.create(getActivity())
                        .setTitle("Verbose  Title")
                        .setText("The alert scales to accommodate larger bodies of text.\n " +
                                "The alert scales to accommodate larger bodies of text. \n" +
                                "The alert scales to accommodate larger bodies of text.\n")
                        .show();
                break;
            case 6:
                AlertFactory.create(getActivity())
                        .setTitle("Click inside Title")
                        .setText("this is a click inside")
                        .setDuration(10000)
                        .setOnShowListener(() -> UiUtils.SnackbarText("Show Alert"))
                        .setOnHideListener(() -> UiUtils.SnackbarText("Hide Alert"))
                        .show();
                break;
            case 7:
                AlertFactory.create(getActivity())
                        .setTitle("Infinite Title")
                        .setText("alert infinite...")
                        .enableInfiniteDuration(true)   // 不自动消失
                        .show();
                break;
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.request();
    }

    @Override
    public void finishRefreshing() {

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            UiUtils.SnackbarText("加载中哦");
            getActivity().runOnUiThread(() -> refreshView.finishRefreshing());
        }).start();
    }
}