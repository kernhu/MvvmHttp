package com.xcion.mvvmokhttp.listener;


import com.xcion.mvvmokhttp.exception.HandlerException;

/**
 * @author: huming
 * @date: 2021/6/22
 * @Description: 生命周期
 */
public abstract class HttpLifecycleListener<T> {

    /**
     * 开始
     */
    public abstract void onSubscribe();

    /**
     * 成功后回调方法
     *
     * @param t
     */
    public abstract void onNext(T t);

    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     *
     * @param e
     */
    public abstract void onError(HandlerException.ResponseThrowable e);

    /**
     * 完成
     */
    public abstract void onComplete();

    /**
     * 下载进度
     *
     * @param readLength
     * @param countLength
     */
    public void updateProgress(long readLength, long countLength) {

    }
}
