package com.letion.miracle.mvp.presenter;

import android.app.Application;
import android.content.Context;

import com.letion.geetionlib.base.BaseRecyclerAdapter;
import com.letion.geetionlib.base.integration.AppManager;
import com.letion.geetionlib.di.scope.ActivityScope;
import com.letion.geetionlib.mvp.BasePresenter;
import com.letion.geetionlib.vender.imageloader.ImageLoader;
import com.letion.miracle.mvp.contract.HomeContract;
import com.letion.miracle.mvp.ui.adapter.HomeAdapter;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private HomeAdapter mAdapter;

    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView
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

    public void request(Context context, boolean refresh) {
        if (mAdapter == null) {
            mAdapter = new HomeAdapter(context, BaseRecyclerAdapter.ONLY_FOOTER);
            mRootView.setAdapter(mAdapter);
        }
        List<String> list = mModel.getItems();
        if (refresh) {
            mAdapter.clearAll();
        }
        mAdapter.addAll(list);
        mAdapter.notifyDataSetChanged();
        mRootView.finishRefreshing();
    }

}