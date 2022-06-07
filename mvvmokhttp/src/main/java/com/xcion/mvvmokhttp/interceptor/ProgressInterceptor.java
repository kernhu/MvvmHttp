package com.xcion.mvvmokhttp.interceptor;

import com.xcion.mvvmokhttp.body.ProgressResponseBody;
import com.xcion.mvvmokhttp.listener.ProgressListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.SoftReference;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author: huming
 * @date: 2022/6/2
 * @Description: 监听接口请求进度
 */
public class ProgressInterceptor implements Interceptor {

    private static SoftReference<ProgressListener> listener;

    public static void setListener(ProgressListener listener) {
        ProgressInterceptor.listener = new SoftReference<>(listener);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Response proceed = chain.proceed(chain.request());
        return proceed.newBuilder().body(new ProgressResponseBody(
                proceed.body(), new ProgressListener() {
            @Override
            public void onProgress(int tag, long progress, long total, boolean done) {
                if (listener != null && listener.get() != null) {
                    listener.get().onProgress(tag, progress, total, done);
                }
            }
        }
        )).build();
    }
}
