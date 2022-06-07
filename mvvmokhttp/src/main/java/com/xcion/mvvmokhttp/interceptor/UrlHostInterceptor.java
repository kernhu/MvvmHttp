package com.xcion.mvvmokhttp.interceptor;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xcion.mvvmokhttp.MvvmHttp;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: huming
 * @date: 2021/6/30
 * @Description: base url拦截替换器
 */
public class UrlHostInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        //拦截retrofit注解的header配置信息
        List<String> list = request.headers("baseUrl");
        if (list.size() > 0) {
            //移除掉header 因为服务器不需要这个header,这个header只是在拦截器里用到
            builder.removeHeader("baseUrl");
            String key = list.get(0);
            //如果配置的header信息在HashMap里有声明
            if (!TextUtils.isEmpty(key) && MvvmHttp.getInstance().baseUrlMap.containsKey(key)) {
                HttpUrl newBaseUrl = HttpUrl.get(MvvmHttp.getInstance().baseUrlMap.get(key));
                HttpUrl oldBaseUrl = request.url();
                //将旧的请求地址里的协议、域名、端口号替换成配置的请求地址
                HttpUrl newFullUrl = oldBaseUrl.newBuilder().
                        scheme(newBaseUrl.scheme()).
                        host(newBaseUrl.host()).
                        port(newBaseUrl.port()).build();
                //创建带有新地址的新请求
                Request newRequest = builder.url(newFullUrl).build();
                return chain.proceed(newRequest);
            }
        }
        return chain.proceed(request);
    }
}
