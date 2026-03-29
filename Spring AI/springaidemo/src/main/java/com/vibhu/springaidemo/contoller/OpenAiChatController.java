package com.vibhu.springaidemo.contoller;


import com.vibhu.springaidemo.service.OpenAiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/openai")
@CrossOrigin(origins = "*")
public class OpenAiChatController {

    @Autowired
    private OpenAiChatService  openAiChatService;

    @GetMapping("/{message}")
    public ResponseEntity<String> chatWithOpenAI(@PathVariable String message) {
        return openAiChatService.getStringResponseEntityFromOpenAI(message);
    }

    @GetMapping("/message")
    public ResponseEntity<String> chatWithOpenAIParam(@RequestParam String message) {
        return openAiChatService.getStringResponseEntityFromOpenAI(message);
    }

}
