package com.yxkong.common.filter;

import com.google.common.collect.Sets;
import com.yxkong.common.utils.FastJsonUtils;
import com.yxkong.common.utils.WebUtil;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: yxkong
 * @Date: 2021/5/19 9:26 上午
 * @version: 1.0
 */
public class HttpTraceLogFilter extends OncePerRequestFilter implements Ordered {
    private Logger log = LoggerFactory.getLogger(HttpTraceLogFilter.class);
    // 需要过滤掉的静态文件
    private static final Set<String> EXCLUDE_URI_SET = Sets.newHashSet(
            "actuator/prometheus",
            "html",
            "css",
            "js",
            "png",
            "gif",
            "configure",
            "v2/api-docs",
            "swagger",
            "swagger-resources",
            "csrf",
            "webjars");

    private static final String IGNORE_CONTENT_TYPE = "multipart/form-data";

    private static final Set<String> INCLUDE_HEADERS = Sets.newHashSet("token", "label","version");

    private final MeterRegistry registry;

    public HttpTraceLogFilter(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public int getOrder() {
        return -10;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!isRequestValid(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 如果不需要日志记录，接着往下走
        String path = request.getRequestURI();
        Set<String> excludeSet = EXCLUDE_URI_SET.stream().filter(path::contains).collect(Collectors.toSet());
        if (!excludeSet.isEmpty() || Objects.equals(IGNORE_CONTENT_TYPE, request.getContentType())) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            filterChain.doFilter(request, response);
            stopWatch.stop();
            status = response.getStatus();
        } finally {
            // String requestBody = IOUtils.toString(request.getInputStream(), Charsets.UTF_8);
            // log.info("requestBody:{}", requestBody);
            // 记录日志
            HttpTraceLog traceLog = new HttpTraceLog();
            traceLog.setIpAddr(WebUtil.getIpHost(request));
            traceLog.setPath(path);
            traceLog.setMethod(request.getMethod());
            traceLog.setTimeTaken(stopWatch.getTotalTimeMillis());
            traceLog.setTime(LocalDateTime.now().toString());
            Map<String, String> headersMap = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                if (INCLUDE_HEADERS.contains(key)) {
                    String value = request.getHeader(key);
                    headersMap.put(key, value);
                }
            }
            traceLog.setHeadersMap(headersMap);
            traceLog.setParameterMap(request.getParameterMap());
            traceLog.setStatus(status);
            traceLog.setRequestBody(getRequestBody(request));
            traceLog.setResponseBody(getResponseBody(response));
            log.warn("Http trace log: {}", FastJsonUtils.toJson(traceLog));
            updateResponse(response);
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
    private String getRequestBody(HttpServletRequest request) {
        String requestBody = "";
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            requestBody = new String(wrapper.getContentAsByteArray(), Charsets.toCharset(wrapper.getCharacterEncoding()));
        }
        return requestBody;
    }
    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }
    private String getResponseBody(HttpServletResponse response) {
        String responseBody = "";
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            try {
                responseBody = IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            } catch (IOException e) {
                // NOOP
            }
        }
        return responseBody;
    }
    private static class HttpTraceLog {
        private String ipAddr;
        private String path;
        private Map<String, String> headersMap;
        private Map<String, String[]> parameterMap;
        private String method;
        private Long timeTaken;
        private String time;
        private Integer status;
        private String requestBody;
        private String responseBody;

        public String getIpAddr() {
            return ipAddr;
        }

        public void setIpAddr(String ipAddr) {
            this.ipAddr = ipAddr;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Map<String, String> getHeadersMap() {
            return headersMap;
        }

        public void setHeadersMap(Map<String, String> headersMap) {
            this.headersMap = headersMap;
        }

        public Map<String, String[]> getParameterMap() {
            return parameterMap;
        }

        public void setParameterMap(Map<String, String[]> parameterMap) {
            this.parameterMap = parameterMap;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public Long getTimeTaken() {
            return timeTaken;
        }

        public void setTimeTaken(Long timeTaken) {
            this.timeTaken = timeTaken;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getRequestBody() {
            return requestBody;
        }

        public void setRequestBody(String requestBody) {
            this.requestBody = requestBody;
        }

        public String getResponseBody() {
            return responseBody;
        }

        public void setResponseBody(String responseBody) {
            this.responseBody = responseBody;
        }
    }
}