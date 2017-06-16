package com.letion.miracle.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.letion.agentweb.AgentWeb;
import com.letion.geetionlib.base.integration.AppManager;
import com.letion.geetionlib.di.scope.ActivityScope;
import com.letion.geetionlib.mvp.BasePresenter;
import com.letion.geetionlib.vender.imageloader.ImageLoader;
import com.letion.miracle.mvp.contract.WebViewContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class WebViewPresenter extends BasePresenter<WebViewContract.Model, WebViewContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    protected AgentWeb mAgentWeb;

    @Inject
    public WebViewPresenter(WebViewContract.Model model, WebViewContract.View rootView
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
        if (mAgentWeb != null) mAgentWeb.getWebLifeCycle().onDestroy();
    }

    public void init(Activity activity, ViewGroup viewGroup, final TextView textView) {
        mAgentWeb = AgentWeb.with(activity)//
                .setAgentWebParent(viewGroup, new LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()//
                .defaultProgressBarColor()
                .setReceivedTitleCallback((view, title) -> {
                    if (textView != null)
                        textView.setText(title);
                })
                .setSecutityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()//
                .ready()
                .go(null);
        mAgentWeb.getLoader().loadUrl("https://m.jd.com/");
    }

    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
    }

    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return false;
    }
}