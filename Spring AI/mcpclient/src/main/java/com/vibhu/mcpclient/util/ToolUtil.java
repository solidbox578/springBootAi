package com.vibhu.mcpclient.util;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.mcp.SyncMcpToolCallback;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;

public class ToolUtil {

    /**
     * Keeps only the tools whose MCP server name and tool name match the given parameters. If either parameter is null or blank, it is ignored.
     * applied for each request
     * @param mcpSyncClients
     * @param serverName
     * @param toolName
     * @return
     */
    public static ToolCallback[] selectToolsFor(List<McpSyncClient> mcpSyncClients, String serverName, String toolName) {
        return mcpSyncClients.stream()
                .flatMap(client ->
                        client.listTools().tools().stream()
                        .filter(tool -> matches(client.getServerInfo().name(), serverName) && matches(tool.name(), toolName))
                        .map(tool -> (ToolCallback) SyncMcpToolCallback.builder().mcpClient(client).tool(tool).build())
                )
                .toArray(ToolCallback[]::new);
    }

    private static boolean matches(String actual, String expected) {
        return expected==null || expected.isBlank() || actual.toLowerCase().contains(expected.toLowerCase());
    }

}
