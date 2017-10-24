package com.letion.geetionlib.vender.rxerrorhandler.handler;

import com.letion.geetionlib.vender.rxerrorhandler.RxErrorHandler;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by liu-feng on 2017/10/24.
 */
public abstract class ErrorHandleSubscriber<T> implements Observer<T> {
    private ErrorHandlerFactory mHandlerFactory;

    public ErrorHandleSubscriber(RxErrorHandler rxErrorHandler){
        this.mHandlerFactory = rxErrorHandler.getHandlerFactory();
    }


    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }


    @Override
    public void onComplete() {

    }


    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        mHandlerFactory.handleError(e);
    }
}