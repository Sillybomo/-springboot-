package com.controller;

/**
 * @Author: 薄墨
 * @Description: TODO
 * @DateTime: 2025/9/19 11:37
 **/
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class ChatController {
    @Autowired
    private ChatClient client;
    
    // 存储会话历史，key为sessionId，value为消息列表
    private final Map<String, List<Message>> sessionHistory = new ConcurrentHashMap<>();

    // 接收chat请求，返回响应，仅供测试时使用
    @RequestMapping("/callChat")
    public String callChat(@RequestParam("question") String question) {
        System.out.println("--------callChat-------");
        System.out.println("client:" + client);
        System.out.println("question:" + question);
        // 注入问题，给出回复
        String content = client.prompt(question)
                .call() // 阻塞式调用，一次性给出所有回复
                .content();
        return content;
    }

    // 接收streamChat请求，返回响应
    @RequestMapping(value = "/streamChat",
            produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> streamChat(@RequestParam("question") String question,
                                   @RequestParam(value = "sessionId", defaultValue = "default") String sessionId) {
        System.out.println("--------streamChat-------");
        System.out.println("client:" + client);
        System.out.println("question:" + question);
        System.out.println("sessionId:" + sessionId);

        // 获取或创建会话历史
        List<Message> history = sessionHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());
        
        // 添加用户消息到历史
        history.add(new UserMessage(question));
        
        // 构建包含历史的prompt
        Flux<String> content = client.prompt()
                .messages(history)
                .stream() // 流式调用，不间断的给出回复
                .content();
        
        // 收集AI回复并添加到历史
        StringBuilder aiResponse = new StringBuilder();
        return content.doOnNext(chunk -> {
            aiResponse.append(chunk);
        }).doOnComplete(() -> {
            // 流结束后，将AI回复添加到历史
            history.add(new AssistantMessage(aiResponse.toString()));
            System.out.println("AI response added to history for session: " + sessionId);
        });
    }
    
    // 清空会话历史
    @RequestMapping("/clearSession")
    public String clearSession(@RequestParam(value = "sessionId", defaultValue = "default") String sessionId) {
        sessionHistory.remove(sessionId);
        return "Session " + sessionId + " cleared";
    }
}
