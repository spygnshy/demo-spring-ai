//package com.example.demospringai.tools;
//
//import org.springframework.ai.tool.annotation.Tool;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//@Configuration
//public class TimeTools {
//
//    // 标准 Spring AI 函数调用注解
//    @Bean
//    @Tool(description = "获取当前系统时间，返回格式 yyyy-MM-dd HH:mm:ss")
//    public String getCurrentTime() {
//        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//    }
//}