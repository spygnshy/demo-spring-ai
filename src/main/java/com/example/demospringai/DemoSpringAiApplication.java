package com.example.demospringai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoSpringAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringAiApplication.class, args);
    }

    // 关键：手动创建 ChatClient 并注册为 Bean
    @Bean
    public ChatClient chatClient() {
        OllamaApi ollamaApi = new OllamaApi("http://localhost:11434");
        OllamaChatModel chatModel = new OllamaChatModel(ollamaApi, OllamaOptions.create()
                .withModel("qwen2.5:7b")
        );
        return ChatClient.create(chatModel);
    }
}