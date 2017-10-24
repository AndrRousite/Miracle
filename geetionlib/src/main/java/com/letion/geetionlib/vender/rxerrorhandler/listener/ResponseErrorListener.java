package com.letion.geetionlib.vender.rxerrorhandler.listener;

import android.content.Context;

/**
 * Created by liu-feng on 2017/10/24.
 */
public interface ResponseErrorListener {
    void handleResponseError(Context context, Throwable t);

    ResponseErrorListener EMPTY = new ResponseErrorListener() {
        @Override
        public void handleResponseError(Context context, Throwable t) {


        }
    };
}
