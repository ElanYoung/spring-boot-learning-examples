package com.starimmortal.excel.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.starimmortal.excel.base.CustomSqlInjector;
import com.starimmortal.excel.handler.MybatisPlusMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 通用配置项
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
@Configuration(proxyBeanMethods = false)
public class MyBatisPlusConfiguration {
    /**
     * 新的分页插件，一缓和二缓遵循Mybatis规则
     * 需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题（该属性会在旧插件移除后一同移除）
     * 参考链接：<a href="https://mp.baomidou.com/guide/interceptor.html">...</a>
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }


    /**
     * 审计字段自动填充
     *
     * @return {@link MetaObjectHandler}
     */
    @Bean
    public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
        return new MybatisPlusMetaObjectHandler();
    }

    /**
     * SQL注入器
     *
     * @return MySqlInjector
     */
    @Bean
    public CustomSqlInjector mySqlInjector() {
        return new CustomSqlInjector();
    }
}
