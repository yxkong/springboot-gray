package com.yxkong.gateway.filter;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import com.alibaba.fastjson.JSONObject;
import com.yxkong.common.configuration.sleuth.filter.SkipHeaderEnum;
import com.yxkong.common.dto.ResultBean;
import com.yxkong.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import com.yxkong.common.enums.OrderEnum;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: yxkong
 * @Date: 2021/8/4 6:30 下午
 * @version: 1.0
 */
@Component
@Slf4j
public class GatewayTracingFilter implements GlobalFilter, Ordered {
    @Autowired
    Tracer tracer;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getRequest().getHeaders().forEach((k,v)->{
            if (!SkipHeaderEnum.contains(k)) {
                log.info("{} {}",k,v);
                putSpan( "request.headers." + k,v.stream().collect(Collectors.joining(",")));
            }
        });
        String url = exchange.getRequest().getURI().getQuery();
        if (url != null && url.length() > 1) {
            putSpan( "request.queryString", url);
        }
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        // probably should reuse buffers
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        //释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        String result = new String(content, StandardCharsets.UTF_8);

                        putResult(result);
                        return bufferFactory.wrap(result.getBytes(StandardCharsets.UTF_8));
                    }));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }
    private void putResult(String result){
        log.info(result);
        putSpan("response.content", result);
        JSONObject jsonObject = JsonUtils.jsonObject(result);
        if (jsonObject.containsKey(ResultBean.STATUS)){
            putSpan("response.status", jsonObject.getString(ResultBean.STATUS));
        }
        if (jsonObject.containsKey(ResultBean.MESSAGE)){
            putSpan("response.message", jsonObject.getString(ResultBean.MESSAGE));
        }
        if (jsonObject.containsKey(ResultBean.TIMESTAMP)){
            putSpan("response.timestamp", jsonObject.getString(ResultBean.TIMESTAMP));
        }
    }

    @Override
    public int getOrder() {
        return  OrderEnum.TRACING.getOrder();
    }
    private void putSpan( String key, String value) {
        tracer.currentSpan().tag(key, value);
    }
    private Span getSpan(){
        Span span = null;
        try {
            span = Tracing.currentTracer().currentSpan();
        } catch (Exception e){
        }
        return span;
    }

}