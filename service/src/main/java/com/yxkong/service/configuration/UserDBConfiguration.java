package com.yxkong.service.configuration;

import org.apache.ibatis.plugin.Interceptor;
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
@MapperScan(basePackages = {"com.yxkong.service.mapper.user"}, sqlSessionFactoryRef = "sqlSessionFactoryUser")
public class UserDBConfiguration {

    @Resource(name="userDataSource")
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactoryUser")
    public SqlSessionFactory sqlSessionFactoryUser() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/user/*Mapper.xml"));
        return factoryBean.getObject();

    }

    @Bean(name = "sqlSessionTemplateUser")
    public SqlSessionTemplate sqlSessionTemplateUser() throws Exception {
        // 使用上面配置的Factory
        return new SqlSessionTemplate(sqlSessionFactoryUser());
    }
}