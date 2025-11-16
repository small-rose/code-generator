server:
    port: 8080
    servlet:
        context-path: /api

spring:
    application:
        name: ${config.projectName}

    datasource:
        url: ${config.url}
        username: ${config.username}
        password: ${config.password}
        driver-class-name: ${config.driverClassName}
        hikari:
            maximum-pool-size: 20
            minimum-idle: 5
            connection-timeout: 30000
            idle-timeout: 600000
            max-lifetime: 1800000

    jackson:
        date-format: yyyy-MM-dd HH:mm:ss
        time-zone: GMT+8

mybatis-plus:
    configuration:
        map-underscore-to-camel-case: true
        log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    global-config:
        db-config:
            logic-delete-field: deleted
            logic-delete-value: 1
            logic-not-delete-value: 0

# Swagger配置
swagger:
    enabled: true
    title: ${config.projectName} API文档
    description: 自动生成的API接口文档
    version: ${config.version}
    base-package: ${config.packageName}

# 日志配置
logging:
    level:
        ${config.packageName}: debug
        com.baomidou.mybatisplus: info