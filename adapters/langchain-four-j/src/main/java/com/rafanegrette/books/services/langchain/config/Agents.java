package com.rafanegrette.books.services.langchain.config;

import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class Agents {

    @Value("${openai.authorization}")
    private String authorization;

    @Value("${openai.chat.model}")
    private String modelName;

    @Bean
    EnglishTeacher englishTeacher() {

        ChatModel model = OpenAiChatModel.builder()
                .apiKey(authorization)
                .modelName(modelName)
                .maxCompletionTokens(500)
                .build();

        return AiServices.builder(EnglishTeacher.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId -> TokenWindowChatMemory.withMaxTokens(5000, new OpenAiTokenCountEstimator(modelName)))
                .build();
    }



}
