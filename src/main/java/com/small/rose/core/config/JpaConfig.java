package com.small.rose.core.config;

import com.small.rose.core.base.dao.jpa.JpaBaseDaoImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ JpaConfig ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/23 023 23:20
 * @Version: v1.0
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.small.rose.core.dao.repository",
        repositoryBaseClass = JpaBaseDaoImpl.class
)
public class JpaConfig {


}
