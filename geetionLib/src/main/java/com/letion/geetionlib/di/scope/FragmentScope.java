package com.letion.geetionlib.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import dagger.Module;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>com.letion.geetionlib.di.scope.FragmentScope
 *
 * @author wuqi
 * @describe A scoping annotation to permit objects whose lifetime should
 * conform to the life of the activity to be memorized in the
 * correct component.
 * <p>
 * @date 2018/4/24 0024
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface FragmentScope {
}
