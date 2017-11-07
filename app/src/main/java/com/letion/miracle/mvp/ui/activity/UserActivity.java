/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.letion.miracle.mvp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.letion.geetionlib.base.BaseActivity;
import com.letion.geetionlib.di.component.AppComponent;
import com.letion.miracle.R;
import com.letion.miracle.di.component.DaggerUserComponent;
import com.letion.miracle.di.module.UserModule;
import com.letion.miracle.mvp.contract.UserContract;
import com.letion.miracle.mvp.presenter.UserPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * ================================================
 * 展示 View 的用法
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.2">View wiki 官方文档</a>
 * Created by JessYan on 09/04/2016 10:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class UserActivity extends BaseActivity<UserPresenter> implements UserContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Inject
    RxPermissions mRxPermissions;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;

    private boolean isLoadingMore;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerUserComponent
                .builder()
                .appComponent(appComponent)
                .userModule(new UserModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getResourceId() {
        return R.layout.activity_user;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initRecycleView();
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onRefresh() {
        mPresenter.requestUsers(true);
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(int mode, String message) {

    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
