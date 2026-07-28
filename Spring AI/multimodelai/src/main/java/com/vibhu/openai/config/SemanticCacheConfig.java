package com.vibhu.openai.config;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.ai.chat.cache.semantic.SemanticCache;
import org.springframework.ai.chat.cache.semantic.SemanticCacheAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.redis.cache.semantic.DefaultSemanticCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.*;

@Configuration
public class SemanticCacheConfig {

    @Bean
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
    }

    @Bean("semanticRedisCacheAdvisor")
    SemanticCacheAdvisor semanticCacheAdvisor(SemanticCache semanticCache){
        System.out.println("SemanticCacheAdvisor created");
        return SemanticCacheAdvisor.builder().cache(semanticCache).build();
    }
}
