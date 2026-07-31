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
public class PromptTemplateController {

    private final ChatClient chatClient;

    @Value("classpath:/promptTemplates/userPromptTemplate.st")
    Resource userPromptTemplate;

    @Value("classpath:/promptTemplates/promptStuffingTemplate.st")
    Resource systemPromptTemplate;

    public PromptTemplateController(@Qualifier("chatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/userTemplate/chat")
    public String userPromptTemplate(@RequestParam("customerName") String customerName, @RequestParam("customerIssue") String customerIssue) {
        return chatClient.prompt()
                .system(promptSystemSpec ->
                        promptSystemSpec.text(systemPromptTemplate))
                .user(promptUserSpec ->
                        promptUserSpec.text(userPromptTemplate)
                        .param("customerName", customerName)
                        .param("customerIssue", customerIssue))
                .call().content();

    }

    @GetMapping("/systemTemplate/chat")
    public String systemPromptTemplate(@RequestParam String message) {
        return chatClient.prompt()
                .system(promptSystemSpec ->
                        promptSystemSpec.text(systemPromptTemplate))
                .user(message)
                .call().content();
    }

}
