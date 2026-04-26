package com.example.demospringai.service;

import com.example.demospringai.util.FileUtil;
import com.example.demospringai.util.TextSplitter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRAGService {

    // ====================== 注入的类（全部解释） ======================
    /**
     * Spring AI 核心：对话模型
     * 作用：调用本地 Ollama 模型
     */
    private final ChatModel chatModel;

    /**
     * Spring AI 核心：对话记忆
     * 作用：保存历史聊天记录
     */
    private final ChatMemory chatMemory;

    // ====================== 配置 ======================
    /**
     * 文档路径：从 application.yml 读取
     */
    @Value("${app.document.path:doc/wyInfo.txt}")
    private String docPath;

    // ====================== 新增：存放切片后的文本片段 ======================
    private List<String> documentChunks;

    // ====================== 项目启动时自动加载文档 ======================
    /**
     * @ PostConstruct
     * 作用：项目启动后自动执行一次
     * 这里自动读取物业文档
     */
    @PostConstruct
    public void init() {
        log.info("项目启动，正在加载物业文档...");
        // 使用我们第1课写的 FileUtil 读取文档
        /*
         * 文档全文内容（项目启动后只加载一次）
         */
        String documentContent = FileUtil.readTxt(docPath);
        log.info("读取到的文档内容：{}", documentContent);

        // ====================== 自动切片 ======================
        // 每块 300 字，重叠 50 字
        this.documentChunks = TextSplitter.split(documentContent, 300, 50);
        log.info("文档切片完成，总片段数：{}", documentChunks.size());
        log.info("文档加载完成！");
    }

    // ====================== 带RAG的对话方法 ======================
    /**
     * 带记忆 + RAG 文档问答
     * @param conversationId 对话ID
     * @param message 用户问题
     * @return AI 回答
     */
    public Flux<String> chatWithRag(String conversationId, String message) {
        try {
            // 1. 保存用户消息到记忆
            chatMemory.add(conversationId, new UserMessage(message));

            // 2. 读取历史对话
            List<Message> history = chatMemory.get(conversationId, 100);

            // 3. ====================== RAG 核心 ======================
            // 把【文档内容】拼进系统提示词，强制 AI 只看文档回答
            // 根据用户问题，自动找最相关的片段
            String bestChunk = findRelevantChunk(message);
            log.info("最相关的片段内容：{}",bestChunk);

            String systemPrompt = """
                    你是小区物业AI助手。
                    规则1：只能根据下面提供的【物业文档片段】回答，不能编造。
                    规则2：文档里没有答案，必须回答“抱歉，文档中无相关信息123”。
                    规则3：回答简洁、准确。
            
                    【参考文档】：
                    {{CHUNK}}
                    """.replace("{{CHUNK}}", bestChunk);

            SystemMessage systemMessage = new SystemMessage(systemPrompt);

            // 4. 组装消息：系统规则 + 历史对话
            List<Message> allMessages = new ArrayList<>();
            allMessages.add(systemMessage);
            allMessages.addAll(history);

            // 5. 构建请求发给 AI
            Prompt prompt = new Prompt(allMessages);

            // 6. 流式调用 AI
            return chatModel.stream(prompt)
                    .map(ChatResponse::getResult)
                    .map(gen -> {
                        String answer = gen.getOutput().getContent();
                        // 保存 AI 回答到记忆
                        chatMemory.add(conversationId, gen.getOutput());
                        return answer;
                    });

        } catch (Exception e) {
            log.error("RAG对话异常", e);
            return Flux.just("服务异常，请稍后再试");
        }
    }

    /**
     * 第5周 RAG 核心：简单片段匹配
     * 根据用户的问题，找到最相关的文档片段
     * @param question 用户问题
     * @return 最相关的片段
     */
    private String findRelevantChunk(String question) {
        // 遍历所有切片
        for (String chunk : documentChunks) {
            // 如果片段包含问题里的关键词，就返回这个片段
            if (chunk.contains(question)) {
                return chunk;
            }
        }
        // 没找到就返回第一个片段
        return documentChunks.getFirst();
    }
}