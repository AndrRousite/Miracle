package com.letion.geetionlib.vender.rxerrorhandler.handler;

import android.content.Context;

import com.letion.geetionlib.vender.rxerrorhandler.listener.ResponseErrorListener;

/**
 * Created by liu-feng on 2017/10/24.
 */
public class ErrorHandlerFactory {
    public final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private ResponseErrorListener mResponseErrorListener;

    public ErrorHandlerFactory(Context mContext, ResponseErrorListener mResponseErrorListener) {
        this.mResponseErrorListener = mResponseErrorListener;
        this.mContext = mContext;
    }

    /**
     *  处理错误
     * @param throwable
     */
    public void handleError(Throwable throwable) {
        mResponseErrorListener.handleResponseError(mContext, throwable);
    }
}
