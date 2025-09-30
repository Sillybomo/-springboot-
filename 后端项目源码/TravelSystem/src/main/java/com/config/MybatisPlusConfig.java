package com.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @Author: 薄墨
 * @Description: TODO
 * @DateTime: 2025/9/15 16:47
 **/
@Configuration
@MapperScan(basePackages = "com.mapper", sqlSessionFactoryRef = "mybatisPlusSqlSessionFactory")
public class MybatisPlusConfig {

    @Bean

    public MybatisPlusInterceptor info() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        PaginationInnerInterceptor innerInterceptor  = new PaginationInnerInterceptor();

        interceptor.addInnerInterceptor(innerInterceptor);

        return interceptor;
    }

    @Bean("mybatisPlusSqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // 关键：指定 XML 路径（确保能扫描到你的 UserMapper.xml）
        // 假设 XML 在 resources/mapper/UserMapper.xml，则路径为 "classpath*:mapper/*.xml"
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath*:mapper/*.xml")
        );

        // 配置驼峰转下划线（与 XML 的 resultMap 互补，避免字段名问题）
        com.baomidou.mybatisplus.core.MybatisConfiguration config = new com.baomidou.mybatisplus.core.MybatisConfiguration();
        config.setMapUnderscoreToCamelCase(true);
        sessionFactory.setConfiguration(config);

        return sessionFactory;
    }
}
