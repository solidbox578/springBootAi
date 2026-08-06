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
import java.util.Map;
import java.util.UUID;

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
                .toolContext(Map.of("progressToken", UUID.randomUUID().toString())) // Example of passing a progress token to the tools
                .tools(toolCallbacks)
                .user(message+"my username is "+username).call().content();
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/ticket-summary")
    public ResponseEntity<String> ticketSummary(@RequestHeader(value = "username", required = false) String username){
        ToolCallback[] toolCallbacks = ToolUtil.selectToolsFor(mcpSyncClients, "helpdesk-mcp-server", null);
        String answer = chatClient.prompt()
                .system("""
                        You orchestrate the 'summerizeTickets' tool. The tool already returns a complete,
                        customer-friendly summary that was generated for this exact request. Return the tool
                        output to the user EXACTLY as it is returned from the tool. Do not add any additional information or context.
                        Your reply must be the verbatim tool response and nothing else.
                        """)
                .user("Please provide a summary of all open tickets for my username: "+username)
                .tools(toolCallbacks)
                .toolContext(Map.of("progressToken", UUID.randomUUID().toString())) // Example of passing a progress token to the tools
                .call().content();

        return ResponseEntity.ok(answer);
    }
}
