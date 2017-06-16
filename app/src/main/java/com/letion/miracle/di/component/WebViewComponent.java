package com.letion.miracle.di.component;

import com.letion.geetionlib.di.scope.ActivityScope;

import dagger.Component;

import com.letion.geetionlib.di.component.AppComponent;

import com.letion.miracle.di.module.WebViewModule;

import com.letion.miracle.mvp.ui.activity.WebViewActivity;

@ActivityScope
@Component(modules = WebViewModule.class, dependencies = AppComponent.class)
public interface WebViewComponent {
    void inject(WebViewActivity activity);
}