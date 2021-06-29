package com.yxkong.service.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Author: yxkong
 * @Date: 2021/6/27 12:26 下午
 * @version: 1.0
 */
@Configuration
public class DruidConfiguration {
    private static final Logger log = LoggerFactory.getLogger(DruidConfiguration.class);

    /**
     * 数据库
     * @return DataSource
     */
    @Bean(name = "userDataSource")
    @ConfigurationProperties(prefix = "datasource.user")
    public DataSource userDataSource() {
        log.info("-------------------- userDataSource init ---------------------");
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }
}