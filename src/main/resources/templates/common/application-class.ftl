package ${config.packageName};

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<#if config.generateSwagger>
    import springfox.documentation.swagger2.annotations.EnableSwagger2;
</#if>

/**
* ${config.projectName} 启动类
*
* @author ${config.author}
* @version ${config.version}
* @date ${.now?string("yyyy-MM-dd")}
*/
@SpringBootApplication
@MapperScan("${config.packageName}.mapper")
<#if config.generateSwagger>
    @EnableSwagger2
</#if>
public class Application {

public static void main(String[] args) {
SpringApplication.run(Application.class, args);
}
}
