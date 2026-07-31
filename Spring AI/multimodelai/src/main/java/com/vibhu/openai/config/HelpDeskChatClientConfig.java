package com.vibhu.openai.config;

import com.vibhu.openai.Tools.HelpDeskTools;
import com.vibhu.openai.config.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class HelpDeskChatClientConfig {

    @Value("classpath:/promptTemplates/helpDeskSystemPromptTemplate.st")
    Resource helpDeskPromptTemplate;

    @Bean("helpDeskChatClient")
    public ChatClient helpDeskChatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, HelpDeskTools helpDeskTools) {
        Advisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();

        return chatClientBuilder
                .defaultSystem(helpDeskPromptTemplate)
                .defaultTools(helpDeskTools)
                .defaultAdvisors(List.of(simpleLoggerAdvisor, memoryAdvisor, tokenUsageAdvisor))
                .build();

    }
}
