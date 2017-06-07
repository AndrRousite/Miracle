package com.letion.miracle.di.component;

import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.di.scope.ActivityScope;
import com.letion.miracle.di.module.TestModule;
import com.letion.miracle.mvp.ui.activity.TestActivity;
import com.letion.miracle.mvp.ui.fragment.TestFragment;

import dagger.Component;

@ActivityScope
@Component(modules = TestModule.class, dependencies = AppComponent.class)
public interface TestComponent {
    void inject(TestActivity activity);

    void inject(TestFragment fragment);
}