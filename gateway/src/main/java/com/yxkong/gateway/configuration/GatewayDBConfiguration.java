package com.yxkong.gateway.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @Author: yxkong
 * @Date: 2021/6/27 12:31 下午
 * @version: 1.0
 */
@Configuration
@MapperScan(basePackages = {"com.yxkong.gateway.mapper.gateway"}, sqlSessionFactoryRef = "sqlSessionFactoryGateway")
public class GatewayDBConfiguration {

    @Resource(name="gatewayDataSource")
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactoryGateway")
    public SqlSessionFactory sqlSessionFactoryGateway() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/gateway/*Mapper.xml"));
        return factoryBean.getObject();

    }

    @Bean(name = "sqlSessionTemplateGateway")
    public SqlSessionTemplate sqlSessionTemplateGateway() throws Exception {
        // 使用上面配置的Factory
        return new SqlSessionTemplate(sqlSessionFactoryGateway());
    }
}