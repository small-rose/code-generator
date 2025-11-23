package com.small.rose.core.config;

import cn.hutool.core.io.file.FileReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ SpringResourceReader ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/23 023 1:53
 * @Version: v1.0
 */

@Component
public class SpringResourceReader {

    private final ResourceLoader resourceLoader;

    private Map<String, List<String>> fileCaheList = new HashMap<>();
    // 通过构造器注入ResourceLoader
    public SpringResourceReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<String> readFile(String path) throws IOException{

        if(fileCaheList.keySet().contains(path)){
            return fileCaheList.get(path);
        }
        // "classpath:" 前缀表示从类路径加载
        Resource resource = resourceLoader.getResource("classpath:" + path);
        if (!resource.exists()) {
            throw new IOException("文件不存在: " + path);
        }
        List<String> content = new FileReader(resource.getFile()).readLines();
        System.out.println("resource path :" + path);
        System.out.println(content);
        fileCaheList.put(path, content);
        return content ;
    }
}
