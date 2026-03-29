package com.vibhu.springaidemo.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAiChatService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(OpenAiChatService.class);

    private final OpenAiChatModel openAiChatModel;

    public ResponseEntity<String> getStringResponseEntityFromOpenAI(String message) {
        logger.info("Received message from user: {}", message);
        String response = openAiChatModel.call(message);
        logger.debug("OpenAI response: {}", response);
        return ResponseEntity.ok(response);
    }

}
