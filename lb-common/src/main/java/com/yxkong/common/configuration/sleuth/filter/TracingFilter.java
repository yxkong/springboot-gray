package com.yxkong.common.configuration.sleuth.filter;

import brave.Span;
import brave.Tracing;
import com.alibaba.fastjson.JSONObject;
import com.yxkong.common.dto.ResultBean;
import com.yxkong.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 采集输入输出信息到brave.Span
 * @Author: yxkong
 * @Date: 2021/7/27 3:16 下午
 * @version: 1.0
 */
public class TracingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(TracingFilter.class);

    public static final Integer payloadMaxLength = 1024;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)httpServletRequest;
        String uri = request.getRequestURI();
        //属于跳过资源，直接放行
        if (SkipUrlSuffixEnum.contains(uri)){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            final ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
            final ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);
            before(requestWrapper);
            filterChain.doFilter(requestWrapper, responseWrapper);
            after(requestWrapper, responseWrapper);
        }

    }

    /**
     * 执行前设置
     * @param requestWrapper
     */
    private void before(ContentCachingRequestWrapper requestWrapper) {
        try {
            if (Objects.nonNull(getSpan())){
                Map<String, String> header = getHeaders((HttpServletRequest)requestWrapper);
                header.forEach((k,v) ->{
                    if (!SkipHeaderEnum.contains(k)) {
                        putSpan( "request.headers." + k,v);
                    }
                });
                String url = requestWrapper.getQueryString();
                if (url != null && url.length() > 1) {
                    putSpan( "request.queryString", url);
                }
            }
        } catch (Exception e) {
            //异常后不影响
        }

    }
    private Span getSpan(){
        Span span = null;
        try {
            span = Tracing.currentTracer().currentSpan();
        } catch (Exception e){
        }
        return span;
    }

    /**
     * 执行完设置
     * @param requestWrapper
     * @param responseWrapper
     */
    private void after(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) throws IOException{

        try {
            String requestPayload = null;
            String responsePayload = null;
            try {
                requestPayload = getPayLoad(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
                responsePayload = getPayLoad(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
            } catch (Exception e) {
                logger.warn("getPayLoad异常.", e);
            } finally {
                responseWrapper.copyBodyToResponse();
            }
            putSpan("request.content", requestPayload);
            putSpan("response.content", responsePayload);
            String contentType = requestWrapper.getContentType();
            if (Objects.nonNull(contentType)) {
                putSpan("response.contentType", contentType);
            }
            String characterEncoding = responseWrapper.getCharacterEncoding();
            if (Objects.nonNull(characterEncoding)) {
                putSpan("response.characterEncoding", characterEncoding);
            }

            JSONObject jsonObject = JsonUtils.jsonObject(responsePayload);
            if (jsonObject.containsKey(ResultBean.STATUS)){
                putSpan("response.status", jsonObject.getString(ResultBean.STATUS));
            }
            if (jsonObject.containsKey(ResultBean.MESSAGE)){
                putSpan("response.message", jsonObject.getString(ResultBean.MESSAGE));
            }
            if (jsonObject.containsKey(ResultBean.TIMESTAMP)){
                putSpan("response.timestamp", jsonObject.getString(ResultBean.TIMESTAMP));
            }
        } catch (Exception e) {
        }
    }
    private String getPayLoad(byte[] buf, String characterEncoding) {
        String payload = "[unknown]";
        if (buf == null){
            return payload;
        }
        if (buf.length > 0) {
            int length = Math.min(buf.length, payloadMaxLength);
            try {
                payload = new String(buf, 0, length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                logger.warn("getPayLoad 异常.", ex);
            }
        }
        return payload;
    }
    private void putSpan( String key, String value) {
        Span span = getSpan();
        if ( Objects.nonNull(span) && Objects.nonNull(value)) {
            span.tag(key, value);
        }
    }
    public Map<String, String> getHeaders(HttpServletRequest servletRequest) {

        Map<String, String> headers = new HashMap<String, String>();
        for (Enumeration<?> headerNames = servletRequest.getHeaderNames(); headerNames.hasMoreElements();) {
            Object elementKey = headerNames.nextElement();
            String headerName = null;
            if (elementKey != null) {
                headerName = (String)elementKey;
            }
            for (Enumeration<?> headerValues = servletRequest.getHeaders(headerName); headerValues.hasMoreElements();) {
                Object element = headerValues.nextElement();
                if (element != null) {
                    String headerValue = (String)element;
                    if (headerValue != null && headerValue.length() > 0) {
                        headers.put(headerName, headerValue);
                    }
                }
            }
        }

        return headers;
    }

}