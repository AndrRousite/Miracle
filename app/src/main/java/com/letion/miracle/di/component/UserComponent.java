package com.letion.miracle.di.component;


import com.letion.geetionlib.di.component.AppComponent;
import com.letion.geetionlib.di.scope.ActivityScope;
import com.letion.miracle.di.module.UserModule;
import com.letion.miracle.mvp.ui.activity.UserActivity;

import dagger.Component;
/**
 * Created by jess on 9/4/16 11:17
 * Contact with jess.yan.effort@gmail.com
 */
@ActivityScope
@Component(modules = UserModule.class,dependencies = AppComponent.class)
public interface UserComponent {
    void inject(UserActivity activity);
}
