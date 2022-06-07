package com.xcion.mvvmokhttp.listener;

/**
 * @author: huming
 * @date: 2022/6/2
 * @Description: 接口请求进度事件
 */
public interface ProgressListener {

    /**
     * @param tag      唯一标识
     * @param progress 已经下载或上传字节数
     * @param total    总字节数
     * @param done     是否完成
     */
    void onProgress(int tag, long progress, long total, boolean done);

}
