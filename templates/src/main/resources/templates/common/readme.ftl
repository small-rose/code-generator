# ${config.projectName}

> 自动生成的Spring Boot项目

## 项目信息

- **项目名称**: ${config.projectName}
- **包名**: ${config.packageName}
- **版本**: ${config.version}
- **作者**: ${config.author}
- **生成时间**: ${.now?string("yyyy-MM-dd HH:mm:ss")}

## 技术栈

- **框架**: Spring Boot 2.7.0
- **ORM**: MyBatis Plus 3.5.3.1
- **数据库**: ${config.driverClassName?replace("com.", "")?replace(".jdbc.Driver", "")}
- **前端**: Vue3 + Element Plus
- **文档**: Swagger 3.0

## 项目结构