package com.vibhu.openai.controller;

import com.vibhu.openai.Tools.TimeTools;
import okhttp3.Response;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api")
public class TimeToolController {

    private final ChatClient chatClient;
    private final TimeTools timeTools;

    public TimeToolController(@Qualifier("timeChatClient") ChatClient chatClient, TimeTools timeTools) {
        this.chatClient = chatClient;
        this.timeTools = timeTools;
    }

    @GetMapping("/local-time")
    public ResponseEntity<String> getTimeToolResponse(@RequestHeader("username") String username,
                                                      @RequestParam("message") String message) {
        String response = chatClient.prompt()
                //.tools(timeTools)
                .advisors(advisor -> advisor.param(CONVERSATION_ID, username))
                .user(message).call().content();
        return ResponseEntity.ok(response);
    }
}
