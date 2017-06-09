package com.letion.miracle.di.component;

import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.di.scope.ActivityScope;
import com.letion.miracle.di.module.HomeModule;
import com.letion.miracle.mvp.ui.activity.HomeActivity;
import com.letion.miracle.mvp.ui.fragment.HomeFragment;

import dagger.Component;

@ActivityScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeActivity activity);

    void inject(HomeFragment fragment);
}