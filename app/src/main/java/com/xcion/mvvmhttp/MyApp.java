package com.xcion.mvvmhttp;

import android.app.Application;

import com.xcion.mvvmokhttp.MvvmHttp;

import java.util.HashMap;

/**
 * @author: huming
 * @date: 2022/6/7
 * @Description: java类作用描述
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HashMap baseUrlMap = new HashMap<String, String>();
        baseUrlMap.put(MvvmHttp.BASE_URL_FIRST, "http://172.30.61.121:8080/");
        baseUrlMap.put(MvvmHttp.BASE_URL_SECOND, "http://172.30.61.121:8080/");
        baseUrlMap.put(MvvmHttp.BASE_URL_THREE, "http://172.30.61.121:8080/");
        MvvmHttp.getInstance()
                .setConnectTimeout(60)
                .setWriteTimeout(180)
                .setReadTimeout(180)
                .setLogPrintable(true)
                .setRetrofitLockable(true)
                .setRetryOnConnectionFailure(true)
                .setLogTag("http-api")
                .setBaseUrlMap(baseUrlMap)
                .bind();

    }
}
