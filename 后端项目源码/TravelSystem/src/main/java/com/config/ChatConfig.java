package com.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatConfig {

    @Value("${spring.ai.ollama.chat.model}")
    private String modelName;

    @Bean
    public ChatClient mychatClient(OllamaChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem("""
                    你是一个智能AI助手。请按照以下格式回答问题：

                    1. 对于需要推理的问题，请先在<think>和</think>标签中展示你的思考过程
                    2. 思考过程应该详细、逻辑清晰
                    3. 在</think>标签后，提供清晰简洁的最终答案
                    4. 如果问题不需要复杂推理，可以直接给出答案

                    记住：思考过程要放在<think></think>标签内，最终答案要放在标签外。
                    """)
                .build();
    }

    @Bean
    public OllamaChatModel chatModel(OllamaApi ollamaApi) {
        OllamaOptions options = OllamaOptions.builder()
                .model(modelName)
                .temperature(0.7)   // 适中的创造性
                .topP(0.9)          // 多样性控制
                .numCtx(4096)       // 上下文长度
                .build();

        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(options)
                .build();
    }
}