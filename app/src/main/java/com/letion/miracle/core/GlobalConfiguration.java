package com.letion.miracle.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.JsonParseException;
import com.letion.geetionlib.base.delegate.AppDelegate;
import com.letion.geetionlib.base.integration.ConfigModule;
import com.letion.geetionlib.base.integration.IRepositoryManager;
import com.letion.geetionlib.di.module.GlobalConfigModule;
import com.letion.geetionlib.http.GlobalHttpHandler;
import com.letion.geetionlib.util.UiUtils;
import com.letion.geetionlib.vender.log.L;
import com.letion.miracle.R;
import com.letion.miracle.mvp.model.api.Api;
import com.letion.miracle.mvp.model.api.cache.CommonCache;
import com.letion.miracle.mvp.model.api.service.UserService;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.HttpException;

/**
 * app的全局配置信息在此配置,需要将此实现类声明到AndroidManifest中
 * Created by jess on 12/04/2017 17:25
 */
public class GlobalConfiguration implements ConfigModule {
    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        builder.baseurl(Api.APP_DOMAIN)
                .globalHttpHandler(new GlobalHttpHandler() {
                    // 这里可以提供一个全局处理Http请求和响应结果的处理类,
                    // 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                    @Override
                    public Response onHttpResultResponse(String httpResult, Interceptor.Chain
                            chain, Response response) {
                     /* 这里如果发现token过期,可以先请求最新的token,然后在拿新的token放入request里去重新请求
                        注意在这个回调之前已经调用过proceed,所以这里必须自己去建立网络请求,如使用okhttp使用新的request去请求
                        create a new request and modify it accordingly using the new token
                        Request newRequest = chain.request().newBuilder().header("token", newToken)
                                             .build();

                        retry the request

                        response.body().close();
                        如果使用okhttp将新的请求,请求成功后,将返回的response  return出去即可
                        如果不需要返回新的结果,则直接把response参数返回出去 */

                        return response;
                    }

                    // 这里可以在请求服务器之前可以拿到request,做一些操作比如给request统一添加token或者header以及参数加密等操作
                    @Override
                    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
//                        //如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的requeat如增加header,不做操作则直接返回request参数

                        return request;
                    }
                })
                .responseErrorListener((context12, t) -> {
                     /* 用来提供处理所有错误的监听
                   rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效 */
                    //@Deprecated(禁止使用Timber) Timber.tag("Catch-Error").w(t.getMessage());
                    //这里不光是只能打印错误,还可以根据不同的错误作出不同的逻辑处理
                    String msg = "未知错误";
                    if (t instanceof UnknownHostException) {
                        msg = "网络不可用";
                    } else if (t instanceof SocketTimeoutException) {
                        msg = "请求网络超时";
                    } else if (t instanceof HttpException) {
                        HttpException httpException = (HttpException) t;
                        msg = convertStatusCode(httpException);
                    } else if (t instanceof JsonParseException || t instanceof ParseException ||
                            t instanceof JSONException) {
                        msg = "数据解析错误";
                    }
                    UiUtils.SnackbarTextWithLong(msg);
                })
                .gsonConfiguration((context1, gsonBuilder) -> {//这里可以自己自定义配置Gson的参数
                    gsonBuilder
                            .serializeNulls()//支持序列化null的参数
                            .enableComplexMapKeySerialization();//支持将序列化key为object的map,
                    // 默认只能序列化key为string的map
                })
                .retrofitConfiguration((context1, retrofitBuilder) -> {//这里可以自己自定义配置Retrofit的参数,
                    // 甚至你可以替换系统配置好的okhttp对象
                    // retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());
                    // 比如使用fastjson替代gson
                })
                .okhttpConfiguration((context1, okhttpBuilder) -> {//这里可以自己自定义配置Okhttp的参数
                    okhttpBuilder.readTimeout(30, TimeUnit.SECONDS);
                    okhttpBuilder.writeTimeout(30, TimeUnit.SECONDS);
                    //开启使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听
                    //ProgressManager.getInstance().with(okhttpBuilder);
                })
                .rxCacheConfiguration((context1, rxCacheBuilder) -> {//这里可以自己自定义配置RxCache的参数
                    rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                });
    }

    @Override
    public void registerComponents(Context context, IRepositoryManager repositoryManager) {
        repositoryManager.injectRetrofitService(UserService.class);
        repositoryManager.injectCacheService(CommonCache.class);
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppDelegate.Lifecycle> lifecycles) {
        // AppDelegate.Lifecycle 的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycles.add(new AppDelegate.Lifecycle() {
            @Override
            public void attachBaseContext(Application application) {
                //MultiDex.install(application);
            }

            @Override
            public void onCreate(Application application) {

            }

            @Override
            public void onTerminate(Application application) {

            }
        });
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application
            .ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                L.d(activity + " - onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                L.d(activity + " - onActivityStarted");
                if (!activity.getIntent().getBooleanExtra("isInitToolbar", false)) {
                    //由于加强框架的兼容性,故将 setContentView 放到 onActivityCreated 之后,onActivityStarted 之前执行
                    //而 findViewById 必须在 Activity setContentView() 后才有效,所以将以下代码从之前的
                    // onActivityCreated 中移动到 onActivityStarted 中执行
                    activity.getIntent().putExtra("isInitToolbar", true);
                    //这里全局给Activity设置toolbar和title,你想象力有多丰富,这里就有多强大,以前放到BaseActivity的操作都可以放到这里
                    if (activity.findViewById(R.id.toolbar) != null) {
                        if (activity instanceof AppCompatActivity) {
                            ((AppCompatActivity) activity).setSupportActionBar((Toolbar) activity
                                    .findViewById(R.id.toolbar));
                            ((AppCompatActivity) activity).getSupportActionBar()
                                    .setDisplayShowTitleEnabled(false);
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                activity.setActionBar((android.widget.Toolbar) activity
                                        .findViewById(R.id.toolbar));
                                activity.getActionBar().setDisplayShowTitleEnabled(false);
                            }
                        }
                    }
                    if (activity.findViewById(R.id.toolbar_title) != null) {
                        ((TextView) activity.findViewById(R.id.toolbar_title)).setText(activity
                                .getTitle());
                    }
                    if (activity.findViewById(R.id.toolbar_back) != null) {
                        activity.findViewById(R.id.toolbar_back).setOnClickListener(v -> activity
                                .onBackPressed());
                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                L.d(activity + " - onActivityResumed");
                // clear all notification
            }

            @Override
            public void onActivityPaused(Activity activity) {
                L.d(activity + " - onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                L.d(activity + " - onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                L.d(activity + " - onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                L.d(activity + " - onActivityDestroyed");
            }
        });
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager
            .FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {

            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle
                    savedInstanceState) {
                // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建是重复利用已经创建的Fragment。
                // https://developer.android.com/reference/android/app/Fragment
                // .html?hl=zh-cn#setRetainInstance(boolean)
                // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag
                // 属性,否则 setRetainInstance(true) 无效
                // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager
                // 需要展示较多 Fragment
                f.setRetainInstance(true);
            }

            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                //这里应该是检测 Fragment 而不是 FragmentLifecycleCallbacks 的泄露。
//                ((RefWatcher) ((App) f.getActivity()
//                        .getApplication())
//                        .getAppComponent()
//                        .extras()
//                        .get(RefWatcher.class.getName()))
//                        .watch(f);
            }
        });
    }


    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }

}
