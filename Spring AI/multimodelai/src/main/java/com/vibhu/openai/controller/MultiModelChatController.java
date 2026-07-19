package com.vibhu.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class MultiModelChatController {

    private final ChatClient openAiChatClient;

    private final ChatClient ollamaChatClient;

    private final ChatClient anthropicChatClient;

    public MultiModelChatController(@Qualifier("openAIChatClient") ChatClient openAiChatClient,
                                    @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
                                    @Qualifier("anthropicChatClient")  ChatClient anthropicChatClient) {
        this.openAiChatClient = openAiChatClient;
        this.ollamaChatClient = ollamaChatClient;
        this.anthropicChatClient = anthropicChatClient;
    }

    @GetMapping("/openai/chat")
    public String sendMessageToOpenAI(@RequestParam String message) {
        // Logic to send the message to OpenAI and get the response
        return openAiChatClient.prompt(message).call().content();
    }

    @GetMapping("/ollama/chat")
    public String sendMessageToOllama(@RequestParam String message) {
        return ollamaChatClient.prompt(message).call().content();
    }

    @GetMapping("anthropic/chat")
    public String sendMessageToAnthropic(@RequestParam String message) {
        return anthropicChatClient.prompt(message).call().content();
    }
}
