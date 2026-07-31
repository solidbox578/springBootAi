/*
package com.vibhu.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/role-based")
public class MessageRoleBasedController {

    private final ChatClient ollamaChatClient;
    private final ChatClient ollmaDefaultSystemChatClient;

    public MessageRoleBasedController(@Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
                                      @Qualifier("ollmaDefaultSystemChatClient") ChatClient ollmaDefaultSystemChatClient) {
        this.ollamaChatClient = ollamaChatClient;
        this.ollmaDefaultSystemChatClient = ollmaDefaultSystemChatClient;
    }

    @GetMapping("/ollama/chat")
    public String sendMessageToOllama(@RequestParam String message) {
        String system = """
        You are an internal HR assistant. \s
        Your role is to help employees with HR-related queries and provide information about company policies, \s
        benefits, leave policies and procedures. If a user asks for help with anything else of these topics. \s
        Kindly inform them that you can only assist with queries related to HR and company policies. \s
        """;
        return ollamaChatClient
                .prompt()
                .system(system)
                .user(message)
                .call().content();
    }

    @GetMapping("/ollamaDefault/chat")
    public String sendMessageToDefaultOllama(@RequestParam String message) {
        return ollmaDefaultSystemChatClient
                .prompt()
                //.system(system) Remove it, unless want to override the system message
                .user(message)
                .call().content();
    }

}
*/
