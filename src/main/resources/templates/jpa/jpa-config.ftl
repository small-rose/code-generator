package ${config.packageName}.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
* JPA配置类
*
* @author ${config.author}
* @since ${.now?string("yyyy-MM-dd")}
*/
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
basePackages = "${config.packageName}.repository",
repositoryBaseClass = ${config.packageName}.dao.base.impl.BaseDaoImpl.class
)
public class JpaConfig {

    // 审计配置（可选）
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("system"); // 从SecurityContext获取当前用户
    }
}