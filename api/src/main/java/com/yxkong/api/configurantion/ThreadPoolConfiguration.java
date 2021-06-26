package com.yxkong.api.configurantion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: yxkong
 * @Date: 2021/6/26 9:59 下午
 * @version: 1.0
 */
@Slf4j
@EnableAsync
@Configuration
public class ThreadPoolConfiguration {

    @Bean
    public ThreadPoolExecutor initExecutor() {
        return  new ThreadPoolExecutor(1,2,60, TimeUnit.SECONDS,new LinkedBlockingQueue<>(10));
    }
}