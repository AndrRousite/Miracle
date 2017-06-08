package com.letion.miracle.mvp.presenter;

import android.app.Application;

import com.letion.geetionlib.base.integration.AppManager;
import com.letion.geetionlib.di.scope.ActivityScope;
import com.letion.geetionlib.mvp.BasePresenter;
import com.letion.geetionlib.vender.imageloader.ImageLoader;
import com.letion.miracle.mvp.contract.TestContract;
import com.letion.miracle.mvp.ui.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class TestPresenter extends BasePresenter<TestContract.Model, TestContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private TestAdapter mAdapter;
    private List<String> tests = new ArrayList<>();

    @Inject
    public TestPresenter(TestContract.Model model, TestContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public synchronized void request() {
        if (mAdapter == null) {
            mAdapter = new TestAdapter(tests, position -> mRootView.onItemClick(position));
            mRootView.setAdapter(mAdapter);
        }
        tests.clear();
        tests.addAll(mModel.getTests());
        mAdapter.notifyDataSetChanged();
        mRootView.finishRefreshing();
    }
}