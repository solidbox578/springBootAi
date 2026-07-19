package com.vibhu.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatMemoryClientConfig {


    /**
     * spring.ai.chat.client.enabled=false    need to remove from application.properties to enable this bean
     * this will consider default model added in the pom.xml for spring-ai-ollama dependency
     * this will use the MessageChatMemoryAdvisor to store the chat messages in the ChatMemory
     * @return
     */
    /*@Bean
    @Qualifier("chatMemoryChatClient")
    public ChatClient chatMemoryChatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        Advisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        Advisor memoryChatAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        //MessageChatMemoryAdvisor --> ChatMemory (MessageWindowChatMemory) --> ChatRepository (InMemoryChatMemoryRepository)
        return chatClientBuilder
                .defaultAdvisors(List.of(simpleLoggerAdvisor, memoryChatAdvisor))
                .build();
    }*/


    @Bean
    @Qualifier("chatMemoryChatClient")
    public ChatClient chatMemoryChatClient(OllamaChatModel ollamaChatModel, ChatMemory chatMemory) {
        Advisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        Advisor memoryChatAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        //MessageChatMemoryAdvisor --> ChatMemory (MessageWindowChatMemory) --> ChatRepository (InMemoryChatMemoryRepository)
        return ChatClient.builder(ollamaChatModel)
                .defaultAdvisors(List.of(simpleLoggerAdvisor, memoryChatAdvisor))
                .build();
    }
}
