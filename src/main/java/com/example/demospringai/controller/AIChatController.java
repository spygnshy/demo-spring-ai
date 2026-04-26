package com.example.demospringai.controller;

import com.example.demospringai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai-chat")
@RequiredArgsConstructor
public class AIChatController {

    private final ChatService chatService;

    /**
     * 带记忆的对话接口
     */
    @GetMapping(
            value = "/memory",
            produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"
    )
    public Flux<String> chatMemory(
            @RequestParam String conversationId,
            @RequestParam String message
    ) {
        return chatService.chatWithMemory(conversationId, message);
    }
}