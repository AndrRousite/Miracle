package com.letion.geetionlib.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liu-feng on 2017/6/5.
 */
public interface GlobalHttpHandler {
    Response onHttpResponse(String httpResult, Interceptor.Chain chain, Response response);

    Request onHttpRequest(Interceptor.Chain chain, Request request);

    GlobalHttpHandler EMPTY = new GlobalHttpHandler() {
        @Override
        public Response onHttpResponse(String httpResult, Interceptor.Chain chain, Response
                response) {
            //不管是否处理,都必须将response返回出去
            return response;
        }

        @Override
        public Request onHttpRequest(Interceptor.Chain chain, Request request) {
            //不管是否处理,都必须将request返回出去
            return request;
        }
    };
}
