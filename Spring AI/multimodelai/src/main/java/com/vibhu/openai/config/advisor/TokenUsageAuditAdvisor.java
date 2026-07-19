package com.vibhu.openai.config.advisor;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

public class TokenUsageAuditAdvisor implements CallAdvisor {

    private final Logger logger  = LoggerFactory.getLogger(this.getClass());

    @Override
    public @NonNull ChatClientResponse adviseCall(@NonNull ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        ChatResponse chatResponse = chatClientResponse.chatResponse();
        if(chatResponse != null) {
            Usage usage = chatResponse.getMetadata().getUsage();
            logger.info("Token Usage Details : {}", usage);
        }
        return chatClientResponse;
    }

    @Override
    public @NonNull String getName() {
        return "TokenUsageAuditAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
