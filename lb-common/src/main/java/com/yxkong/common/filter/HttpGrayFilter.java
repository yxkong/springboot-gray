package com.yxkong.common.filter;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 灰度请求过滤器
 *
 * @Author: yxkong
 * @Date: 2021/5/27 10:46 上午
 * @version: 1.0
 */
public class HttpGrayFilter extends OncePerRequestFilter implements Ordered {
    private final MeterRegistry registry;

    public HttpGrayFilter(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!isRequestValid(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            filterChain.doFilter(request, response);
        } finally {

        }
    }
    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }
}