package com.rafanegrette.books.services.langchain.config;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class LangchainService {

    @Value("${zilliz.milvus.host}")
    private String host;
    @Value("${zilliz.milvus.token}")
    private String token;

    int dimension = 384;
    private EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

    public VectorStore getStorage(String collectionName) throws ExecutionException, InterruptedException {
        return new VectorStore(collectionName);
    }


    public class VectorStore {
        String collectionName;


        @Getter
        EmbeddingStore<TextSegment> embeddingStore;


        public VectorStore(String collectionName) {
            this.collectionName = collectionName;
            this.embeddingStore = MilvusEmbeddingStore.builder()
                    .uri(host)
                    .collectionName(collectionName)
                    .dimension(dimension)
                    .token(token)
                    .build();

        }

        public void embedText(String text) {

            TextSegment segment = TextSegment.from(text);
            Embedding embedding = embeddingModel.embed(segment).content();
            embeddingStore.add(embedding, segment);
        }

        public String search(String text) {
            Embedding queryEmbedding = embeddingModel.embed(text).content();
            EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                    .queryEmbedding(queryEmbedding)
                    .maxResults(3)
                    .build();
            return embeddingStore.search(embeddingSearchRequest)
                    .matches()
                    .stream()
                    .map(EmbeddingMatch::embedded)
                    .map(TextSegment::text)
                    .reduce("", (s1, s2) -> s1.concat("\n----\n").concat(s2));

        }
    }

}
