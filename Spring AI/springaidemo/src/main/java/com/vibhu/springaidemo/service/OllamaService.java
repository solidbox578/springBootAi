package com.vibhu.springaidemo.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OllamaService {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(OllamaService.class);

    private final OllamaChatModel ollamaChatModel;

    public ResponseEntity<String> getStringResponseEntityFromOllama(String message) {
        logger.info("Received message from user: {}", message);
        String response = ollamaChatModel.call(message);
        logger.info("Ollama response: {}", response);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> getStringResponseEntityFromOllamaChatClient(String prompt) {
        logger.info("Received prompt from user: {}", prompt);

        ChatClient client = ChatClient.create(ollamaChatModel);
        String response = client.prompt(prompt)
                .call().content();

        logger.info("Ollama ChatClient API response: {}", response);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ChatResponse> getChatResponseEntityFromOllamaChatClient(String prompt) {
        logger.info("Received prompt from user: {}", prompt);

        ChatClient client = ChatClient.create(ollamaChatModel);
        ChatResponse response = client.prompt(prompt)
                .call().chatResponse();

        String modelMetadata = response.getMetadata().getModel();
        logger.info("Ollama ChatClient API response metadata: {}", modelMetadata);

        logger.info("Ollama ChatClient API full response: {}", response);
        return ResponseEntity.ok(response);
    }

}
