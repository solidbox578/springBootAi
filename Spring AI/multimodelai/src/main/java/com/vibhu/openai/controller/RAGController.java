package com.vibhu.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("api/rag")
public class RAGController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    private final ChatClient webSearchChatClient;

    @Value("classpath:/promptTemplates/systemPromptRandomDataTemplate.st")
    Resource promptTemplatesResource;

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource systemPromptTemplatesResource;

    public RAGController(@Qualifier("chatMemoryChatClient") ChatClient chatClient,
                         @Qualifier("webSearchRAGChatClient") ChatClient webSearchChatClient,  VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.webSearchChatClient = webSearchChatClient;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/random/chat")
    public ResponseEntity<String> randomChat(@RequestHeader("userName") String userName, @RequestParam("message") String message) {
        String conversationId = (userName != null && !userName.isEmpty()) ? userName : "default";

        /*WITH RetrievalAugmentationAdvisor this commented code is not needed. This Advisor will take care of the retrieval*/
       /* SearchRequest searchRequest = SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5).build();
        List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);
        String similarContext = similarDocs.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));*/
        String ragAnswer = chatClient.prompt()
                   /* .system(
                    promptSystemSpec -> promptSystemSpec.text(promptTemplatesResource)
                            .param("documents", similarContext))*/
                    .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, conversationId))
                    .user(message)
                    .call().content();

        return ResponseEntity.ok(ragAnswer);
    }

    @GetMapping("/document/chat")
    public ResponseEntity<String> documentChat(@RequestHeader("userName") String userName, @RequestParam("message") String message) {
        String conversationId = (userName != null && !userName.isEmpty()) ? userName : "default";

        String ragAnswer = chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, conversationId))
                .user(message)
                .call().content();
        return ResponseEntity.ok(ragAnswer);
    }

    @GetMapping("/web-search/chat")
    public ResponseEntity<String> webSearch(@RequestHeader("userName") String userName, @RequestParam("message") String message) {
        String conversationId = (userName != null && !userName.isEmpty()) ? userName : "default";
        String ragAnswer = webSearchChatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, conversationId))
                .user(message)
                .call().content();
        return ResponseEntity.ok(ragAnswer);
    }

}
