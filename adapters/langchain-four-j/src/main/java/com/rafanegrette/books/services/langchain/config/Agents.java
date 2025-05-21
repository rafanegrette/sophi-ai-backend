package com.rafanegrette.books.services.langchain.config;

import dev.langchain4j.community.model.qianfan.QianfanChatModel;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Agents {

    @Value("${openai.authorization}")
    private String authorization;

    @Value("${openai.chat.model}")
    private String modelName;

    @Value("${qwen.apikey}")
    private String apiKeyQwen;

    @Value("${qwen.chat.model}")
    private String modelNameQwen;

    @Autowired
    LangchainService langchainService;

    /* openAI
    @Bean
    public EnglishTeacher englishTeacher() {
        String collectionName = "Neuromancer";

        ChatModel model = OpenAiChatModel.builder()
                .apiKey(authorization)
                .modelName(modelName)
                .logRequests(true)
                .logResponses(true)
                .maxCompletionTokens(1500)
                .build();
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(langchainService.new VectorStore(collectionName).getEmbeddingStore())
                .embeddingModel(new AllMiniLmL6V2EmbeddingModel())
                .maxResults(5)
                .minScore(0.75)
                .build();

        return AiServices.builder(EnglishTeacher.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId -> TokenWindowChatMemory.withMaxTokens(10000, new OpenAiTokenCountEstimator(modelName)))
                .contentRetriever(contentRetriever)
                .build();
    }*/


    // qwen
    @Bean
    public EnglishTeacher englishTeacher() {
        String collectionName = "Neuromancer";

        ChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://dashscope-intl.aliyuncs.com/compatible-mode/v1")
                .apiKey(apiKeyQwen)
                .modelName(modelNameQwen)
                .logRequests(true)
                .logResponses(true)
                //.maxCompletionTokens(1500)
                .build();
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(langchainService.new VectorStore(collectionName).getEmbeddingStore())
                .embeddingModel(new AllMiniLmL6V2EmbeddingModel())
                .maxResults(5)
                .minScore(0.75)
                .build();

        return AiServices.builder(EnglishTeacher.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId -> TokenWindowChatMemory.withMaxTokens(10000, new OpenAiTokenCountEstimator(modelName)))
                .contentRetriever(contentRetriever)
                .build();
    }

}
