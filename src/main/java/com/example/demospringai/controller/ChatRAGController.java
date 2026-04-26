package com.example.demospringai.controller;

import com.example.demospringai.service.ChatRAGService;
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
public class ChatRAGController {

    private final ChatRAGService chatRAGService;

    /**
     * RAG 物业文档问答接口
     */
    @GetMapping(value = "/rag", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public Flux<String> rag(
            @RequestParam String conversationId,
            @RequestParam String message
    ) {
        return chatRAGService.chatWithRag(conversationId, message);
    }
}