package com.yxkong.lb.interceptor;

import com.yxkong.lb.holder.GrayHolder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

/**
 * 灰度拦截器，用于请求时将lable和version向下传递
 * 默认使用的Default Client 内部是由httpConnection实现，不起作用
 * @Author: yxkong
 * @Date: 2021/5/17 3:57 下午
 * @version: 1.0
 */
public class GrayRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
        requestWrapper.getHeaders().add(GrayHolder.LABEL_KEY,GrayHolder.getLable());
        requestWrapper.getHeaders().add(GrayHolder.VERSION_KEY, GrayHolder.getVersion());
        return execution.execute(requestWrapper, body);
    }
}