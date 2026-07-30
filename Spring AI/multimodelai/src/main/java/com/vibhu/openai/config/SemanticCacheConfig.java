package com.vibhu.openai.config;

import io.qdrant.client.QdrantClient;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.ai.chat.cache.semantic.SemanticCache;
import org.springframework.ai.chat.cache.semantic.SemanticCacheAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.ai.vectorstore.redis.cache.semantic.DefaultSemanticCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.*;

@Configuration
public class SemanticCacheConfig {

    /* Given below Bean configuration is for redis semantic cache, where we need separate redis infrastructure */
    /*@Bean
    public RedisClient redisClient() {
        return RedisClient.builder().hostAndPort("localhost", 6379).build();
    }

    @Bean
    SemanticCache semanticCache(RedisClient redisClient, EmbeddingModel embeddingModel){
        System.out.println("SemanticCache bean created");
        return DefaultSemanticCache.builder()
                .jedisClient(redisClient)
                .embeddingModel(embeddingModel)
                .distanceThreshold(0.3)  //Similarity = 1 - Distance, so here similarityThreshold would be 1-0.3 = 0.7
                .indexName("my-cache")
                .prefix("chat:")
                .build();
    }*/

    /*Given below bean configuration is for Qdrant based Semantic Cache. Since we're already using Qdrant for RAG vector db,
    we don't need to separate cache infrastructure */
    @Bean("cacheVectorStore")
    VectorStore cacheVectorStore(QdrantClient  qdrantClient, EmbeddingModel embeddingModel) {
        return QdrantVectorStore.builder(qdrantClient, embeddingModel)
                .collectionName("semantic-cache")
                .initializeSchema(true)
                .build();
    }

    @Bean
    SemanticCache semanticCache(EmbeddingModel embeddingModel, @Qualifier("cacheVectorStore") VectorStore vectorStore) {
        return DefaultSemanticCache.builder()
                .vectorStore(vectorStore)
                .embeddingModel(embeddingModel)
                .similarityThreshold(0.9)
                //.distanceThreshold(0.1)  // Similarity = 1 - Distance, so here similarityThreshold would be 1-0.3 = 0.7
                .prefix("chat:")
                .build();
    }

    @Bean("semanticRedisCacheAdvisor")
    SemanticCacheAdvisor semanticCacheAdvisor(SemanticCache semanticCache){
        System.out.println("SemanticCacheAdvisor created");
        return SemanticCacheAdvisor.builder().cache(semanticCache).build();
    }
}
