package com.vibhu.springaidemo.contoller;

import com.vibhu.springaidemo.service.OllamaService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ollama")
@CrossOrigin(origins = "*")
public class OllamaController {

    @Autowired
    private OllamaService ollamaService;

    @GetMapping("/{message}")
    public ResponseEntity<String> chatWithOllama(@PathVariable String message){
        return ollamaService.getStringResponseEntityFromOllama(message);
    }

    @GetMapping("/message")
    public ResponseEntity<String> chatWithOllamaParam(@RequestParam String message) {
        return ollamaService.getStringResponseEntityFromOllama(message);
    }

    @GetMapping("/chatClient/message")
    public ResponseEntity<String> chatWithOllamaChatClient(@RequestParam String message) {
        return ollamaService.getStringResponseEntityFromOllamaChatClient(message);
    }

    @GetMapping("/chatClient/metadata")
    public ResponseEntity<ChatResponse> chatWithOllamaChatClientMetadata(@RequestParam String message) {
        return ollamaService.getChatResponseEntityFromOllamaChatClient(message);
    }


}
