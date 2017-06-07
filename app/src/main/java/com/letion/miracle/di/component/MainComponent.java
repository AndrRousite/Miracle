package com.letion.miracle.di.component;

import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.di.scope.ActivityScope;
import com.letion.miracle.di.module.MainModule;
import com.letion.miracle.mvp.ui.activity.MainActivity;

import dagger.Component;

@ActivityScope
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {
    void inject(MainActivity activity);
}