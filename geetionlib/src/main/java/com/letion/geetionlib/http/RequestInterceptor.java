package com.letion.geetionlib.http;

import android.support.annotation.Nullable;

import com.letion.geetionlib.util.ZipHelper;
import com.letion.geetionlib.vender.log.L;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by liu-feng on 2017/6/5.
 */
@Singleton
public class RequestInterceptor implements Interceptor {
    private GlobalHttpHandler mHandler;

    @Inject
    public RequestInterceptor(@Nullable GlobalHttpHandler handler) {
        this.mHandler = handler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        boolean hasRequestBody = request.body() != null;

        Buffer requestbuffer = new Buffer();

        if (hasRequestBody) {
            request.body().writeTo(requestbuffer);
        }
        Response originalResponse;
        try {
            originalResponse = chain.proceed(request);
        } catch (Exception e) {
            throw e;
        }

        //打印响应结果
        String bodyString = printResult(request, originalResponse);

        if (mHandler != null)//这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
            return mHandler.onHttpResultResponse(bodyString, chain, originalResponse);

        return originalResponse;
    }

    /**
     * 打印响应结果
     *
     * @param request
     * @param originalResponse
     * @return
     * @throws IOException
     */
    @Nullable
    private String printResult(Request request, Response originalResponse) throws IOException {
        //读取服务器返回的结果
        ResponseBody responseBody = originalResponse.body();
        String bodyString = null;
        if (isParcelable(responseBody)) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            //获取content的压缩类型
            String encoding = originalResponse
                    .headers()
                    .get("Content-Encoding");

            Buffer clone = buffer.clone();


            //解析response content
            bodyString = parseContent(responseBody, encoding, clone);

            L.d(bodyString);

        } else {

        }
        return bodyString;
    }

    /**
     * 解析服务器响应的内容
     *
     * @param responseBody
     * @param encoding
     * @param clone
     * @return
     */
    private String parseContent(ResponseBody responseBody, String encoding, Buffer clone) {
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content使用gzip压缩
            return ZipHelper.decompressForGzip(clone.readByteArray(), convertCharset(charset));//解压
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content使用zlib压缩
            return ZipHelper.decompressToStringForZlib(clone.readByteArray(), convertCharset
                    (charset));//解压
        } else {//content没有被压缩
            return clone.readString(charset);
        }
    }

    public static String convertCharset(Charset charset) {
        String s = charset.toString();
        int i = s.indexOf("[");
        if (i == -1)
            return s;
        return s.substring(i + 1, s.length() - 1);
    }

    public static boolean isParcelable(ResponseBody responseBody) {
        if (responseBody.contentLength() == 0) return false;
        return responseBody.contentType().toString().contains("text") || isJson(responseBody);
    }

    public static boolean isJson(ResponseBody responseBody) {
        return responseBody.contentType().toString().contains("json");
    }
}
