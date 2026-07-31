package com.vibhu.openai.config.modelWiseChatClient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiChatClientConfig {

    /**
     * One way to create ChatClient instance - using .create()
     * this Spring AI is OPENAI compatible & running on local with "Docker Model Runner" with gemma 3 model
     * @param chatClientBuilder
     */
    @Bean("chatClient")
    public ChatClient openAIChatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

}
