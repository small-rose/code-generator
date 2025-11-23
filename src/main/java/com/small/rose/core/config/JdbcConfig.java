package com.small.rose.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ JdbcConfig ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/23 023 23:29
 * @Version: v1.0
 */

@Configuration
public class JdbcConfig {
/*
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("${config.datasource.url}");
        dataSource.setUsername("${config.datasource.username}");
        dataSource.setPassword("${config.datasource.password}");
        dataSource.setDriverClassName("${config.datasource.driverClassName}");
        dataSource.setMaximumPoolSize(20);
        dataSource.setMinimumIdle(5);
        dataSource.setConnectionTimeout(30000);
        dataSource.setIdleTimeout(600000);
        dataSource.setMaxLifetime(1800000);
        return dataSource;
    }*/

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
