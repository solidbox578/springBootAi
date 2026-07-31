package com.vibhu.openai.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class EmbeddingConfig {

    /**
     * VectorStore Bean automatically get injected by Spring AI like
     * QdrantVectorStore.builder(qdrantClient, embeddingModel)
     *                 .collectionName("semantic-cache")
     *                 .initializeSchema(true)
     *                 .build();
     *
     *  As can we see above embeddingModel is required.
     *  For this project, I am explicitly defining the EmbeddingModel bcoz somehow auto injection was not working.
     *  Can be checked later and remove this.
     */

    @Bean
    @Primary
    public EmbeddingModel embeddingModel(OpenAiEmbeddingModel openAiEmbeddingModel) {
        return openAiEmbeddingModel;
    }

    /**
     * OpenAI & Ollama both dependencies are added in my pom file, so Spring AI is creating both ChatModel beans.
     * So to avoid ambiguity, I am explicitly defining the ChatModel bean to use OllamaChatModel.
     * If you want to use OpenAI ChatModel, then you can change the below bean
     * or use
     * spring.ai.ollama.chat.enabled=true
     * spring.ai.openai.chat.enabled=false
     * @param ollamaChatModel
     * @return
     */
    /*@Bean
    @Primary
    ChatModel chatModel(OllamaChatModel ollamaChatModel) {
        return ollamaChatModel;
    }*/
}
