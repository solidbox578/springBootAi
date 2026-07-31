package com.vibhu.openai.config;

import com.vibhu.openai.Tools.HelpDeskTools;
import com.vibhu.openai.config.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
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
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultSystem(helpDeskPromptTemplate)
                .defaultTools(helpDeskTools)
                .defaultAdvisors(List.of(memoryAdvisor))
                .build();

    }

    /**
     * This bean will allow the ChatClient to throw exact exception occurred while tool executions, if any. It ll not be formatted by LLM
     * @return
     */
    @Bean
    public ToolExecutionExceptionProcessor toolExecutionExceptionProcessor() {
        return new DefaultToolExecutionExceptionProcessor(true);
    }
}
