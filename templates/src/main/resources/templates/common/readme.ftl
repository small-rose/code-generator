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

src/
├── main/
│ ├── java/
│ │ └── ${config.packageName?replace(".", "/")}/
│ │ ├── entity/ # 实体类
│ │ ├── mapper/ # 数据访问层
│ │ ├── service/ # 业务逻辑层
│ │ ├── controller/ # 控制层
│ │ └── Application.java # 启动类
│ └── resources/
│ ├── mapper/ # MyBatis映射文件
│ ├── application.yml # 配置文件
│ └── static/ # 静态资源
└── test/ # 测试代码


## 快速开始

1. 配置数据库连接信息
2. 运行Application启动类
3. 访问 http://localhost:8080/api/swagger-ui.html 查看API文档

## 生成信息

- 表数量: ${tables?size}
- 生成时间: ${.now?string("yyyy-MM-dd HH:mm:ss")}
- 生成工具: 智能代码生成器 v1.0.0