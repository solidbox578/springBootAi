package com.vibhu.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatMemoryController {

    private final ChatClient chatClient;

    public ChatMemoryController(@Qualifier("chatMemoryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // Consolidated endpoint: optional userName header, required message param.
    @GetMapping("chat-memory")
    public ResponseEntity<String> chatMemory(@RequestHeader(value = "userName", required = false) String userName,
                                             @RequestParam("message") String message) {
        String conversationId = (userName != null && !userName.isEmpty()) ? userName : "default";
        return ResponseEntity.ok(chatClient.prompt().user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call().content());
    }

}
