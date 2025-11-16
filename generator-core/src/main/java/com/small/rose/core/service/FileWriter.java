package com.small.rose.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ FileWriter ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 23:16
 * @Version: v1.0
 */
@Slf4j
@Component
public class FileWriter {


    /**
     * 写入文件
     */
    public void write(String filePath, String content) {
        write(filePath, content, false);
    }

    /**
     * 写入文件，可选择是否覆盖
     */
    public void write(String filePath, String content, boolean overwrite) {
        try {
            File file = new File(filePath);

            // 检查文件是否存在
            if (file.exists() && !overwrite) {
                log.warn("文件已存在，跳过生成: {}", filePath);
                return;
            }

            // 创建目录
            createDirectories(file.getParentFile());

            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(content.getBytes(StandardCharsets.UTF_8));
            }

            log.info("文件生成成功: {}", filePath);

        } catch (IOException e) {
            throw new RuntimeException("写入文件失败: " + filePath, e);
        }
    }

    /**
     * 创建目录
     */
    private void createDirectories(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("创建目录失败: " + dir.getAbsolutePath());
        }
    }

    /**
     * 检查文件是否存在
     */
    public boolean exists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 删除文件
     */
    public void delete(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
            log.info("文件删除成功: {}", filePath);
        } catch (IOException e) {
            log.warn("删除文件失败: {}", filePath, e);
        }
    }

    /**
     * 清空目录
     */
    public void cleanDirectory(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (Files.exists(path)) {
                Files.walk(path)
                        .sorted((a, b) -> -a.compareTo(b)) // 从深到浅排序
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                log.warn("删除文件失败: {}", p, e);
                            }
                        });
            }
            log.info("目录清理完成: {}", dirPath);
        } catch (IOException e) {
            throw new RuntimeException("清理目录失败: " + dirPath, e);
        }
    }

    /**
     * 复制文件
     */
    public void copy(String sourcePath, String targetPath) {
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);

            // 创建目标目录
            createDirectories(target.getParent().toFile());

            Files.copy(source, target);
            log.info("文件复制成功: {} -> {}", sourcePath, targetPath);

        } catch (IOException e) {
            throw new RuntimeException("复制文件失败: " + sourcePath + " -> " + targetPath, e);
        }
    }
}
