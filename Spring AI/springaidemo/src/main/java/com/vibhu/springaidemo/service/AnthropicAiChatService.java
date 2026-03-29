package com.vibhu.springaidemo.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnthropicAiChatService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(AnthropicAiChatService.class);

    private final AnthropicChatModel anthropicChatModel;

    public ResponseEntity<String> getStringResponseEntityFromAnthropic(String message) {
        logger.info("Received message from user: {}", message);
        String response = anthropicChatModel.call(message);
        logger.debug("Anthropic API response: {}", response);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> getStringResponseEntityFromAnthropicChatClient(String prompt) {
        logger.info("Received prompt from user: {}", prompt);

        ChatClient chatClient = ChatClient.create(anthropicChatModel);
        String response = chatClient
                .prompt(prompt)
                .call().content();

        logger.debug("Anthropic ChatClient API response: {}", response);
        return ResponseEntity.ok(response);
    }

}
