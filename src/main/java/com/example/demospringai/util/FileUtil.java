package com.example.demospringai.util;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 文件读取工具
 * 功能：读取本地 txt 文件的全部内容
 * 依赖：commons-io
 */
public class FileUtil {

    /**
     * 读取 txt 文件
     * @param path 文件路径
     * @return 文件文本内容
     */
    public static String readTxt(String path) {
        try {
            File file = new File(path);
            // 读取文件内容，UTF-8 防止乱码
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "读取失败：" + e.getMessage();
        }
    }
}