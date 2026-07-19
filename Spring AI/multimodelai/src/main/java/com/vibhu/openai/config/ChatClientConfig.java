package com.vibhu.openai.config;

import com.vibhu.openai.config.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    /**
     * One way to create ChatClient instance - using .create()
     * this Spring AI is OPENAI compatible & running on local with "Docker Model Runner" with gemma 3 model
     * @param openAiChatModel
     */
    @Bean
    public ChatClient openAIChatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.create(openAiChatModel);
    }

    /**
     * Another way to create ChatClient instance - using .builder().build()
     * this Spring AI is OLLAMA compatible & running on local directly with model deepseek-r1:7b & mistral
     * @param ollamaChatModel
     * @return
     */
    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel).build();
    }

    /**
     * DefaultUser, DefaultSystem, DefaultAssistant roles are used in this Spring AI
     * @param ollamaChatModel
     * @return
     */
    @Bean
    public ChatClient ollmaDefaultSystemChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient
                .builder(ollamaChatModel)
                .defaultSystem("""
                    You are an internal HR assistant. \s
                    Your role is to help employees with HR-related queries and provide information about company policies, \s
                    benefits, leave policies and procedures. If a user asks for help with anything else of these topics. \s
                    Kindly inform them that you can only assist with queries related to HR and company policies. \s
                    """)
                .build();
        /*
         * Similar DefaultUser, DefaultAssistance etc. can be used as part of bean config
         */
    }

    /**
     * DefaultAdvior Built-in and Custome Advisor
     * @param ollamaChatModel
     * @return
     */
    @Bean
    public ChatClient ollmaDefaultAdvisorsChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient
                .builder(ollamaChatModel)
                //.defaultAdvisors(new SimpleLoggerAdvisor()). // for single Advisor
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor())) // to use list of advisors
                .defaultSystem("""
                    You are an internal HR assistant. \s
                    Your role is to help employees with HR-related queries and provide information about company policies, \s
                    benefits, leave policies and procedures. If a user asks for help with anything else of these topics. \s
                    Kindly inform them that you can only assist with queries related to HR and company policies. \s
                    """)
                .build();
    }

    /**
     * This Spring AI is configured to use ANTHROPIC model with Claude-v1.3 model by using API key
     * @param anthropicChatModel
     * @return
     */
    @Bean
    public ChatClient anthropicChatClient(AnthropicChatModel anthropicChatModel) {
        return ChatClient.builder(anthropicChatModel).build();
    }
}
