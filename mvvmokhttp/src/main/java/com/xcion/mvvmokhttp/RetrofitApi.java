package com.xcion.mvvmokhttp;


import com.trello.rxlifecycle4.LifecycleProvider;
import com.xcion.mvvmokhttp.exception.HandlerException;
import com.xcion.mvvmokhttp.listener.HttpLifecycleListener;

import java.lang.ref.WeakReference;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import retrofit2.Response;
import retrofit2.Retrofit;
/**
 * @author: huming
 * @date: 2021/6/22
 * @Description: rxlifecycle2 与 rxAndroid 绑定
 */
public abstract class RetrofitApi<T> implements Function<Response<T>, T>, Observer<T> {

    private WeakReference<LifecycleProvider> lifecycle;
    protected HttpLifecycleListener listener;

    public RetrofitApi(HttpLifecycleListener listener, WeakReference<LifecycleProvider> lifecycle) {
        this.listener = listener;
        this.lifecycle = lifecycle;
    }

    public abstract Observable getObservable(Retrofit retrofit);

    public LifecycleProvider getLifecycleProvider() {
        if (lifecycle == null) {
            return null;
        }
        return lifecycle.get();
    }

    public HttpLifecycleListener getHttpOnNextListener() {
        return listener;
    }

    @Override
    public T apply(Response<T> httpResult) {
        if (httpResult.isSuccessful() && httpResult.body() != null) {
            return httpResult.body();
        } else {
            throw new HandlerException.ResponseThrowable(httpResult.message(),
                    "error: " + httpResult.code() + " " + httpResult.message());
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onSubscribe(Disposable d) {
        if (listener != null) {
            listener.onSubscribe();
        }
    }

    @Override
    public void onNext(T t) {
        if (listener != null) {
            listener.onNext(t);
        }
    }

    /**
     * 完成
     */
    @Override
    public void onComplete() {
        if (listener != null) {
            listener.onComplete();
        }
    }

    /**
     * 对错误进行处理
     */
    @Override
    public void onError(Throwable e) {
        if (listener != null) {
            listener.onError(HandlerException.handleException(e));
        }
    }

}
