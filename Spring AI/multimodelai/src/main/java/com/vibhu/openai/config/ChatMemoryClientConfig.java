package com.vibhu.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatMemoryClientConfig {


    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .maxMessages(10) //Whenever the LLM is called, include only the latest 10 messages from the conversation.
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
    }

    @Bean
    @Qualifier("chatMemoryChatClient")
    public ChatClient chatMemoryChatClient(OpenAiChatModel openAiChatModel, ChatMemory chatMemory) {
        Advisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        Advisor memoryChatAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        //MessageChatMemoryAdvisor --> ChatMemory (MessageWindowChatMemory) --> ChatRepository (InMemoryChatMemoryRepository)
        return ChatClient.builder(openAiChatModel)
                .defaultAdvisors(List.of(simpleLoggerAdvisor, memoryChatAdvisor))
                .build();
    }

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
}
