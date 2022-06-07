package com.xcion.mvvmokhttp.body;

import com.xcion.mvvmokhttp.listener.ProgressListener;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author: huming
 * @date: 2022/6/2
 * @Description: 接口请求进度Body
 */
public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private WeakReference<ProgressListener> progressListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = new WeakReference<>(progressListener);
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                int tag = 0;
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (progressListener != null && progressListener.get() != null) {
                    progressListener.get().onProgress(tag, totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                }
                if (bytesRead == -1) {
                    if (progressListener != null && progressListener.get() != null) {
                        progressListener.clear();
                        progressListener = null;
                    }
                }
                return bytesRead;
            }
        };
    }
}
