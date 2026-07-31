package com.vibhu.openai.config.modelWiseChatClient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnthropicChatClientConfig {

    /**
     * This Spring AI is configured to use ANTHROPIC model with Claude-v1.3 model by using API key
     * @param anthropicChatModel
     * @return
     */
   /* @Bean
    public ChatClient anthropicChatClient(AnthropicChatModel anthropicChatModel) {
        return ChatClient.builder(anthropicChatModel).build();
    }*/
}
