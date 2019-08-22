package com.gxzx.testrabbitmq.config.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

@Configuration
@MapperScan({"com.gxzx.testrabbitmq.web.mapper*"})
public class MybatisPlusConfig {

    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    @ConditionalOnProperty(prefix = "mybatis-plus.configuration",name = "log-performance",havingValue = "true",matchIfMissing = false)
    public OtcPerformanceInterceptor performanceInterceptor() {
    	OtcPerformanceInterceptor otcPerformanceInterceptor = new OtcPerformanceInterceptor();
    	otcPerformanceInterceptor.setPreparedStatement(false);
        return otcPerformanceInterceptor;
    }

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

}
