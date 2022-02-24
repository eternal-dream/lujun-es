package com.cqvip.innocence.framework.config.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.*;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @ClassName MybatisPlusConfig
 * @Description MybatisPlus配置类
 * @Author Innocence
 * @Date 2020/7/11
 * @Version 1.0
 */
@Configuration
public class MybatisPlusConfig {

    @Value("${spring.datasource.url}")
    private String url;

    private DbType getDbType(@NotNull String str) {
        if (!str.contains(":mysql:") && !str.contains(":cobar:")) {
            if (str.contains(":oracle:")) {
                return DbType.ORACLE;
            } else if (str.contains(":postgresql:")) {
                return DbType.POSTGRE_SQL;
            } else if (str.contains(":sqlserver:")) {
                return DbType.SQL_SERVER;
            } else if (str.contains(":db2:")) {
                return DbType.DB2;
            } else if (str.contains(":mariadb:")) {
                return DbType.MARIADB;
            } else if (str.contains(":sqlite:")) {
                return DbType.SQLITE;
            } else if (str.contains(":h2:")) {
                return DbType.H2;
            } else if (!str.contains(":kingbase:") && !str.contains(":kingbase8:")) {
                if (str.contains(":dm:")) {
                    return DbType.DM;
                } else if (str.contains(":zenith:")) {
                    return DbType.GAUSS;
                } else if (str.contains(":oscar:")) {
                    return DbType.OSCAR;
                } else if (str.contains(":firebird:")) {
                    return DbType.FIREBIRD;
                } else if (str.contains(":xugu:")) {
                    return DbType.XU_GU;
                } else {
                    return str.contains(":clickhouse:") ? DbType.CLICK_HOUSE : DbType.OTHER;
                }
            } else {
                return DbType.KINGBASE_ES;
            }
        } else {
            return DbType.MYSQL;
        }
    }

    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     * @return PerformanceMonitorInterceptor
     */
    @Bean
    public PerformanceMonitorInterceptor performanceMonitorInterceptor(){
        return new PerformanceMonitorInterceptor();
    }

    /**
     * 分页插件
     * @return PaginationInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor(getDbType(url));
        innerInterceptor.setOverflow(false);
        innerInterceptor.setMaxLimit(-1L);
        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }

    /**
     * 使用内置的主键生成策略
     * @return IKeyGenerator
     */
    @Bean
    public IKeyGenerator iKeyGenerator(){
        DbType dbType = getDbType(url);
        if (dbType.getDb().equals(DbType.DB2.getDb())){
            return new DB2KeyGenerator();
        }else if (dbType.getDb().equals(DbType.H2.getDb())){
            return new H2KeyGenerator();
        }else if(dbType.getDb().equals(DbType.POSTGRE_SQL.getDb())){
            return new PostgreKeyGenerator();
        }else if(dbType.getDb().equals(DbType.KINGBASE_ES.getDb())){
            return new KingbaseKeyGenerator();
        }else if(dbType.getDb().equals(DbType.ORACLE.getDb()) || dbType.getDb().equals(DbType.DM.getDb())){
            return new OracleKeyGenerator();
        }else{
            return null;
        }
    }

    /**
     * 使用MyMetaObjectHandler配置的自动字段填充
     * @return GlobalConfig
     */
    @Bean
    public GlobalConfig globalConfig(){
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
        return globalConfig;
    }

}
