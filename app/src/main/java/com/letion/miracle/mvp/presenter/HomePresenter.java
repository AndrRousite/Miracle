package com.letion.miracle.mvp.presenter;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.letion.geetionlib.base.integration.AppManager;
import com.letion.geetionlib.di.scope.ActivityScope;
import com.letion.geetionlib.mvp.BasePresenter;
import com.letion.geetionlib.vender.imageloader.ImageLoader;
import com.letion.miracle.mvp.contract.HomeContract;
import com.letion.miracle.mvp.ui.adapter.BaseHolder;
import com.letion.miracle.mvp.ui.adapter.DefaultAdapter;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private DefaultAdapter mAdapter;

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

    }

}