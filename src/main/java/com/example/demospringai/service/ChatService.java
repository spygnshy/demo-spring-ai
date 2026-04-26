package com.example.demospringai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    // Spring AI 对话模型：调用本地Ollama
    private final ChatModel chatModel;

    // Spring AI 对话记忆：保存历史聊天
    private final ChatMemory chatMemory;

    /**
     * 第4周最终版：带记忆 + 强制返回JSON
     * 作用：AI 自动提取用户信息 → 输出标准JSON
     */
    public Flux<String> chatWithMemory(String conversationId, String message) {
        try {
            // 1. 把用户当前说的话，存入记忆
            chatMemory.add(conversationId, new UserMessage(message));

            // 2. 读取历史对话（最近100条）
            List<Message> history = chatMemory.get(conversationId, 100);

            // ======================
            // 【本周核心】强制输出JSON
            // ======================
            SystemMessage systemMessage = new SystemMessage("""
                    你是一个用户信息提取助手。
                    规则：
                    1. 只返回标准JSON，不返回任何多余文字
                    2. 从历史对话中提取用户的：名字、爱好、年龄
                    3. 不知道的字段填 null
                    4. 绝对不要解释，不要打招呼，不要加说明，只输出JSON
                    
                    返回格式示例：
                    {"name":"张三","age":20,"hobby":"篮球","summary":"张三喜欢篮球"}
                    """);

            // 3. 组装消息：系统规则 + 历史对话
            List<Message> allMessages = new ArrayList<>();
            allMessages.add(systemMessage);
            allMessages.addAll(history);
            // 4. 构建发给AI的请求
            Prompt prompt = new Prompt(allMessages);

            // 5. 调用AI，并把回答存入记忆
            return chatModel.stream(prompt)
                    .map(ChatResponse::getResult)
                    .map(gen -> {
                        String answer = gen.getOutput().getContent();
                        chatMemory.add(conversationId, gen.getOutput());
                        return answer;
                    });

        } catch (Exception e) {
            log.error("AI异常", e);
            return Flux.just("{\"error\":\"服务异常\"}");
        }
    }
}