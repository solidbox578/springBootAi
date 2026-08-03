package com.vibhu.mcpclient.controller;

import com.vibhu.mcpclient.util.ToolUtil;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MCPClientController {

    private final ChatClient chatClient;
    private final List<McpSyncClient> mcpSyncClients;

    /**
     * For global MCP Server filter. It works automatically
     * @param chatClientBuilder
     * @param mcpSyncClients
     */
   /* public MCPClientController(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = chatClientBuilder
                .defaultTools(toolCallbackProvider)
                .defaultAdvisors(new SimpleLoggerAdvisor()).build();
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestHeader(value = "username", required = false) String username, @RequestParam String message){
        String answer = chatClient.prompt().user(message+"my username is "+username).call().content();
        return ResponseEntity.ok(answer);
    }
    */

    public MCPClientController(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpSyncClients) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor()).build();
        this.mcpSyncClients = mcpSyncClients;
    }

    @GetMapping("/chat-filter")
    public ResponseEntity<String> chatFilter(@RequestHeader(value = "username", required = false) String username, @RequestParam String message){
        ToolCallback[] toolCallbacks = ToolUtil.selectToolsFor(mcpSyncClients, "helpdesk-mcp-server", null);
        String answer = chatClient.prompt()
                .tools(toolCallbacks)
                .user(message+"my username is "+username).call().content();
        return ResponseEntity.ok(answer);
    }
}
