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
import com.letion.geetionlib.vender.qqalert.AlertFactory;
import com.letion.miracle.R;
import com.letion.miracle.di.component.DaggerHomeComponent;
import com.letion.miracle.di.module.HomeModule;
import com.letion.miracle.mvp.contract.HomeContract;
import com.letion.miracle.mvp.presenter.HomePresenter;
import com.letion.miracle.mvp.ui.adapter.HomeAdapter;
import com.letion.uikit.smartisan.LetionRefreshLayout;

import butterknife.BindView;

import static com.letion.geetionlib.util.Preconditions.checkNotNull;


public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View, LetionRefreshLayout.PullToRefreshListener {


    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.refreshView)
    LetionRefreshLayout refreshView;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.request(getActivity(), true);
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
    public void setAdapter(HomeAdapter adapter) {
        listView.setAdapter(adapter);
        UiUtils.configRecycleView(listView, new LinearLayoutManager(getActivity()));
    }

    @Override
    public void finishRefreshing() {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(() -> refreshView.finishRefreshing());
        }).start();
    }

    @Override
    public void onItemClick(int position) {
        AlertFactory.create(getActivity())
                .setText("Title ")
                .setText("position:" + position)
                .show();
    }

    @Override
    public void onRefresh() {
        mPresenter.request(getActivity(), true);
    }
}