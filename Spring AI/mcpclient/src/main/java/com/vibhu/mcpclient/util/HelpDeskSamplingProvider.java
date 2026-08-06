package com.vibhu.mcpclient.util;



import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mcp.annotation.McpSampling;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HelpDeskSamplingProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpDeskSamplingProvider.class);

    private final ChatModel chatModel;

    public HelpDeskSamplingProvider(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @McpSampling(clients = "vibhu")
    public McpSchema.CreateMessageResult handleSamplingRequest(McpSchema.CreateMessageRequest request) {
        LOGGER.info("Received MCP sampling request from server. System prompt: {}", request.systemPrompt());

        List<Message> messages = new ArrayList<>();
        if(request.systemPrompt() !=null && !request.systemPrompt().isBlank()){
            messages.add(new SystemMessage(request.systemPrompt()));
        }

        String userText = request.messages().stream()
                .filter(m -> m.content() instanceof McpSchema.TextContent
                          && m.role().name().equalsIgnoreCase(McpSchema.Role.USER.name()))
                .map(m -> ((McpSchema.TextContent) m.content()).text())
                .collect(Collectors.joining("\n"));

        messages.add(new UserMessage(userText));
        ChatResponse chatResponse = chatModel.call(new Prompt(messages));

        if(chatResponse.getResult() == null){
            throw new IllegalThreadStateException("LLM Returned no result for the MCP Sampling request");
        }

        String generatedText = Objects.requireNonNull(chatResponse.getResult().getOutput().getText());
        String model = chatResponse.getMetadata().getModel();
        LOGGER.info("LLM model '{}' Produced sampling result using : {}", model, generatedText);

        return McpSchema.CreateMessageResult.builder(McpSchema.Role.ASSISTANT, generatedText, model).build();
    }
}
