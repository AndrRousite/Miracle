package com.letion.geetionlib.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;

import com.hwangjr.rxbus.RxBus;
import com.letion.geetionlib.base.App;

/**
 * Created by liu-feng on 2017/6/5.
 */
public class ActivityDelegateImpl implements ActivityDelegate {
    private Activity mActivity;
    private IActivity iActivity;

    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //如果要使用eventbus请将此方法返回true
        //注册到事件主线
        if (iActivity.useEventBus())
            RxBus.get().register(mActivity);
        iActivity.setupActivityComponent(((App) mActivity.getApplication()).getAppComponent());

    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        //如果要使用eventbus请将此方法返回true
        if (iActivity.useEventBus())
            RxBus.get().unregister(mActivity);
        this.iActivity = null;
        this.mActivity = null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    protected ActivityDelegateImpl(Parcel in) {
        this.mActivity = in.readParcelable(Activity.class.getClassLoader());
        this.iActivity = in.readParcelable(IActivity.class.getClassLoader());
    }

    public static final Creator<ActivityDelegateImpl> CREATOR = new Creator<ActivityDelegateImpl>
            () {
        @Override
        public ActivityDelegateImpl createFromParcel(Parcel source) {
            return new ActivityDelegateImpl(source);
        }

        @Override
        public ActivityDelegateImpl[] newArray(int size) {
            return new ActivityDelegateImpl[size];
        }
    };
}
