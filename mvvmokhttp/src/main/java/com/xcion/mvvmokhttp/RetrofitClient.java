package com.xcion.mvvmokhttp;


import com.xcion.mvvmokhttp.interceptor.LoggerInterceptor;
import com.xcion.mvvmokhttp.interceptor.NetWorkInterceptor;
import com.xcion.mvvmokhttp.interceptor.ProgressInterceptor;
import com.xcion.mvvmokhttp.interceptor.UrlHostInterceptor;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author: huming
 * @date: 2021/6/22
 * @Description: MVVMHttp Retrofit 管理类
 */
public class RetrofitClient {

    private volatile static RetrofitClient INSTANCE;
    private final Retrofit retrofit;

    public static RetrofitClient getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitClient();
                }
            }
        }
        return INSTANCE;
    }


    private RetrofitClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(MvvmHttp.getInstance().baseUrlMap.get(MvvmHttp.BASE_URL_FIRST))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(MvvmHttp.getInstance().connectTimeout, TimeUnit.SECONDS)
                        .readTimeout(MvvmHttp.getInstance().readTimeout, TimeUnit.SECONDS)
                        .writeTimeout(MvvmHttp.getInstance().writeTimeout, TimeUnit.SECONDS)
                        .addInterceptor(new LoggerInterceptor().setLogTag(MvvmHttp.getInstance().logTag)
                                .setLevel(HttpLoggingInterceptor.Level.BODY)
                                .setPrintable(MvvmHttp.getInstance().logPrintable)
                                .build())
                        .addInterceptor(new ProgressInterceptor())
                        .addNetworkInterceptor(new NetWorkInterceptor())
                        .addNetworkInterceptor(new UrlHostInterceptor())
                        .retryOnConnectionFailure(MvvmHttp.getInstance().retryOnConnectionFailure)
                        //.cookieJar(new NewCookieJar())//不完善，存在bug
                        .build())
                .build();

    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * 在主线程观察
     */
    public void sendHttpRequestMain(RetrofitApi retrofitApi) {
        /*rx处理*/
        if ((retrofitApi.getLifecycleProvider() != null)) {
            retrofitApi.getObservable(retrofit)
                    /*生命周期管理*/
                    .compose(retrofitApi.getLifecycleProvider().bindToLifecycle())
                    /*http请求线程*/
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    /*回调线程*/
                    .observeOn(AndroidSchedulers.mainThread())
                    /*结果判断*/
                    .map(retrofitApi)
                    .subscribe(retrofitApi);
        } else {
            retrofitApi.getObservable(retrofit)
                    /*http请求线程*/
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    /*回调线程*/
                    .observeOn(AndroidSchedulers.mainThread())
                    /*结果判断*/
                    .map(retrofitApi)
                    .subscribe(retrofitApi);
        }
    }

    /**
     * 在IO线程观察
     */
    public void sendHttpRequestIO(RetrofitApi retrofitApi) {
        if ((retrofitApi.getLifecycleProvider() != null)) {
            /*rx处理*/
            retrofitApi.getObservable(retrofit)
                    /*生命周期管理*/
                    .compose(retrofitApi.getLifecycleProvider().bindToLifecycle())
                    /*http请求线程*/
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    /*回调线程*/
                    .observeOn(Schedulers.io())
                    /*结果判断*/
                    .map(retrofitApi)
                    .subscribe(retrofitApi);
        } else {
            /*rx处理*/
            retrofitApi.getObservable(retrofit)
                    /*http请求线程*/
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    /*回调线程*/
                    .observeOn(Schedulers.io())
                    /*结果判断*/
                    .map(retrofitApi)
                    .subscribe(retrofitApi);
        }
    }


}
