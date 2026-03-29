package com.vibhu.springaidemo.contoller;

import com.vibhu.springaidemo.service.AnthropicAiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anthropic")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AnthropicAiChatController {

    private final AnthropicAiChatService anthropicAiChatService;

    @GetMapping("/{message}")
    public ResponseEntity<String> chatWithAnthropic(@PathVariable String message) {
       return anthropicAiChatService.getStringResponseEntityFromAnthropic(message);
    }

    @GetMapping("/message")
    public ResponseEntity<String> chatWithAnthropicQ(@RequestParam String message) {
        return anthropicAiChatService.getStringResponseEntityFromAnthropic(message);
    }

    @GetMapping("/chatClient/message")
    public ResponseEntity<String> chatWithAnthropicQChatClient(@RequestParam String message) {
        return anthropicAiChatService.getStringResponseEntityFromAnthropicChatClient(message);
    }

}
