package com.letion.test.core

import android.app.Application
import android.content.Context
import android.support.v4.app.FragmentManager
import com.google.gson.JsonParseException
import com.letion.geetionlib.base.delegate.AppDelegate
import com.letion.geetionlib.base.integration.IConfigManager
import com.letion.geetionlib.base.integration.IRepositoryManager
import com.letion.geetionlib.di.module.ConfigModule
import com.letion.geetionlib.http.GlobalHttpHandler
import com.letion.geetionlib.vender.log.L
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeUnit

/**
 * <p>com.letion.test.core.Configuration
 * @describe 简要描述
 *
 * @author wuqi
 * @date 2018/4/24 0024
 */
class Configuration : IConfigManager {
    override fun registerComponents(context: Context?, repositoryManager: IRepositoryManager?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun injectAppLifecycle(context: Context?, lifecycles: MutableList<AppDelegate>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun injectActivityLifecycle(context: Context?, lifecycles: MutableList<Application.ActivityLifecycleCallbacks>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun injectFragmentLifecycle(context: Context?, lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun applyOptions(context: Context?, builder: ConfigModule.Builder?) {
        builder!!.baseurl(Api.APP_DOMAIN)
                .globalHttpHandler(GlobalHttpHandler.EMPTY)
                .responseErrorListener { context12, t ->
                    /* 用来提供处理所有错误的监听
           rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效 */
                    //@Deprecated(禁止使用Timber) Timber.tag("Catch-Error").w(t.getMessage());
                    //这里不光是只能打印错误,还可以根据不同的错误作出不同的逻辑处理
                    var msg = "未知错误"
                    if (t is UnknownHostException) {
                        msg = "网络不可用"
                    } else if (t is SocketTimeoutException) {
                        msg = "请求网络超时"
                    } else if (t is HttpException) {
                        val httpException = t as HttpException
                        msg = convertStatusCode(httpException)
                    } else if (t is JsonParseException || t is ParseException ||
                            t is JSONException) {
                        msg = "数据解析错误"
                    }
                    L.d(msg)
                }
                .gsonConfiguration {//这里可以自己自定义配置Gson的参数
                    context1, gsonBuilder ->
                    gsonBuilder
                            .serializeNulls()//支持序列化null的参数
                            .enableComplexMapKeySerialization()//支持将序列化key为object的map,
                    // 默认只能序列化key为string的map
                }
                .retrofitConfiguration {//这里可以自己自定义配置Retrofit的参数,
                    context1, retrofitBuilder ->
                    // 甚至你可以替换系统配置好的okhttp对象
                    // retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());
                    // 比如使用fastjson替代gson
                }
                .okhttpConfiguration {//这里可以自己自定义配置Okhttp的参数
                    context1, okhttpBuilder ->
                    okhttpBuilder.readTimeout(30, TimeUnit.SECONDS)
                    okhttpBuilder.writeTimeout(30, TimeUnit.SECONDS)
                    //开启使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听
                    //ProgressManager.getInstance().with(okhttpBuilder);
                }
                .rxCacheConfiguration {//这里可以自己自定义配置RxCache的参数
                    context1, rxCacheBuilder ->
                    rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true)
                }
    }

    private fun convertStatusCode(httpException: HttpException): String {
        val msg: String
        if (httpException.code() == 500) {
            msg = "服务器发生错误"
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在"
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝"
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面"
        } else {
            msg = httpException.message()
        }
        return msg
    }
}