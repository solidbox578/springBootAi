package com.vibhu.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class PromptStuffingController {

    private final ChatClient ollamaChatClient;

    @Value("classpath:/promptTemplates/promptStuffingTemplate.st")
    Resource promptStuffingTemplate;

    public PromptStuffingController(@Qualifier("ollamaChatClient") ChatClient ollamaChatClient) {
        this.ollamaChatClient = ollamaChatClient;
    }

    @GetMapping("/prompt-stuffing/chat")
    public String promptStuffing(@RequestParam String message) {
        return ollamaChatClient.prompt()
                .system(promptSystemSpec ->
                        promptSystemSpec.text(promptStuffingTemplate))
                .user(message)
                .call().content();

    }


}
