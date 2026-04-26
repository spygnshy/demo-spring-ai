package com.example.demospringai.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本切片工具
 * RAG 必须把大文档切成小块
 */
public class TextSplitter {

    /**
     * 按长度切片
     * @param text 原文
     * @param size 每块大小
     * @param overlap 重叠长度
     * @return 切片后的列表
     */
    public static List<String> split(String text, int size, int overlap) {
        List<String> list = new ArrayList<>();
        int len = text.length();
        int start = 0;

        while (start < len) {
            int end = Math.min(start + size, len);
            String chunk = text.substring(start, end);
            list.add(chunk);
            // 下一段 = 当前 + 块大小 - 重叠
            start += size - overlap;
        }
        return list;
    }
}