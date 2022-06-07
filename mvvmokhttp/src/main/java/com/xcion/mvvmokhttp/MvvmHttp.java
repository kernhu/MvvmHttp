package com.xcion.mvvmokhttp;

import android.util.Log;

import java.util.HashMap;

/**
 * @author: huming
 * @date: 2021/6/22
 * @Description: 基础配置参数
 */
public class MvvmHttp {

    /**
     * 切忌 常量名称不能修改；
     * retrofit header 替换 baseUrl
     */
    public static final String BASE_URL_FIRST = "firstly";
    public static final String BASE_URL_SECOND = "secondary";
    public static final String BASE_URL_THREE = "thirdly";

    public long readTimeout = 10;
    public long connectTimeout = 10;
    public long writeTimeout = 10;
    public boolean retryOnConnectionFailure = true;
    public String logTag = "MvvmHttp";
    public boolean logPrintable = true;
    public boolean retrofitLockable = true;
    public HashMap<String, String> baseUrlMap = new HashMap<>();

    public volatile static MvvmHttp mvvmHttp;

    public static MvvmHttp getInstance() {
        synchronized (MvvmHttp.class) {
            if (mvvmHttp == null) {
                mvvmHttp = new MvvmHttp();
            }
        }
        return mvvmHttp;
    }


    public MvvmHttp setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }


    public MvvmHttp setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }


    public MvvmHttp setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }


    public MvvmHttp setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
        return this;
    }


    public MvvmHttp setLogTag(String logTag) {
        this.logTag = logTag;
        return this;
    }


    public MvvmHttp setLogPrintable(boolean logPrintable) {
        this.logPrintable = logPrintable;
        return this;
    }


    public MvvmHttp setRetrofitLockable(boolean retrofitLockable) {
        this.retrofitLockable = retrofitLockable;
        return this;
    }

    public MvvmHttp setBaseUrlMap(HashMap<String, String> baseUrlMap) {
        this.baseUrlMap = baseUrlMap;
        return this;
    }

    public void bind() {

        Log.i(logTag, "**************************************************");
        Log.i(logTag, "*********** MvvmOKHttp 基础参数配置成功 ***********");
        Log.i(logTag, "**************************************************");

    }
}
